import modules.models.DnsServer.DNSServer;
import modules.manager.NmapCommand;
import modules.manager.GlobeManager;
import modules.manager.Manager;
import modules.models.dhcp.Dhcp;
import modules.models.device.Device;

/**
 * Simple test runner for NmapCommand.
 *
 * This demonstrates:
 * - resolving a domain via the simulated DNSServer
 * - scanning a direct IPv4 address
 * - handling a DNS resolution failure
 *
 * Note: NmapCommand performs real TCP connect() calls; results depend on the runtime environment.
 */
public class Main3 {
    public static void main(String[] args) throws Exception {
        // Create a DNSServer (simulated) and add a sample record for convenience
    DNSServer dns = new DNSServer("192.0.2.1");
    dns.addDnsRecord("example.local", "127.0.0.1");

    // Create GlobeManager and pass it to NmapCommand so scans can use simulated device data when available
    GlobeManager globeManager = new GlobeManager();

    // Demo manager + dhcp + device so we can simulate open ports without real network services
    Manager demoManager = new Manager("DEMO01");
    globeManager.addManager(demoManager);

    Dhcp dhcp = new Dhcp("10.0.0.1", "D_DEMO");
    demoManager.addEntity(dhcp);
    // Add a demo IP that matches our DNS entry
    demoManager.addIpToDhcp("127.0.0.1");

    Device demoDevice = new Device("DEV_DEMO");
    // Add common service ports so simulate-only scan will show them
    demoDevice.addPort(22);
    demoDevice.addPort(80);
    demoDevice.addPort(9929);
    demoDevice.addPort(31337);
    demoManager.addEntity(demoDevice);

    // add demoDevice to the network graph so dijkstra can compute a path
    globeManager.grid.addNode(demoDevice);

    // create a scanner node (this program) and connect it to the demo device with a link
    modules.models.Model scannerModel = new modules.models.Model();
    globeManager.grid.addNode(scannerModel);
    // create a link with reasonable bandwidth to simulate low latency (e.g., 10,000 Kbps)
    modules.network.Link.Link link = new modules.network.Link.Link(scannerModel, demoDevice, 10000.0, "Kbps");
    globeManager.grid.addEdge(link);

    // create nmap in simulate-only mode so output is deterministic for demo
    NmapCommand nmap = new NmapCommand(globeManager, dns, true);

        java.util.Scanner scanner = new java.util.Scanner(System.in);

        System.out.println("Interactive NmapCommand tester. Type commands like: nmap scanme.nmap.org 20-80 200");
        System.out.println("Or just: nmap scanme.nmap.org\nType 'q' or 'exit' to quit.");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if (line == null) break;
            line = line.trim();
            if (line.isEmpty()) continue;
            if (line.equalsIgnoreCase("q") || line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) break;

            String[] parts = line.split("\\s+");
            if (!parts[0].equalsIgnoreCase("nmap")) {
                System.out.println("Unknown command. Use: nmap <target> [port-range] [timeoutMs]");
                continue;
            }

            if (parts.length < 2) {
                System.out.println("Usage: nmap <target> [port-range] [timeoutMs]");
                continue;
            }

            String target = parts[1];
            int startPort = 1;
            int endPort = 1024;
            int timeout = 200;

            if (parts.length >= 3) {
                String p = parts[2];
                try {
                    if (p.contains("-")) {
                        String[] rr = p.split("-");
                        startPort = Integer.parseInt(rr[0]);
                        endPort = Integer.parseInt(rr[1]);
                    } else {
                        // single port
                        int port = Integer.parseInt(p);
                        startPort = port;
                        endPort = port;
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid port or port-range: " + p);
                    continue;
                }
            }

            if (parts.length >= 4) {
                try {
                    timeout = Integer.parseInt(parts[3]);
                } catch (NumberFormatException nfe) {
                    System.out.println("Invalid timeout value: " + parts[3]);
                    continue;
                }
            }

            // Print a starting header similar to nmap
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
            String when = java.time.ZonedDateTime.now().format(fmt);
            System.out.println("Starting Nmap 7.97 ( https://nmap.org ) at " + when);

            // Try to resolve for a short 'Host is up' line (best-effort)
            String resolvedIp = null;
            boolean hostResolved = false;
            try {
                modules.network.ip.Ipv4 ipObj = dns.getIpForDomain(target);
                resolvedIp = ipObj.getIpString();
                hostResolved = true;
            } catch (IllegalArgumentException e) {
                // maybe target is already an IP (simple IPv4 check)
                if (target.matches("^\\d+\\.\\d+\\.\\d+\\.\\d+$")) {
                    resolvedIp = target;
                    hostResolved = true;
                }
            } catch (Exception ignored) {
            }

            if (hostResolved) {
                double latency = 0.0;
                // Try to compute a sensible latency using the network graph if possible.
                // try to use the network graph to compute a sensible latency between
                // the program's scanner node (scannerModel) and the destination node
                try {
                    // nothing to do here; the graph-based latency is computed below using scannerModel
                } catch (Exception ignored) {
                }

                // Use Dijkstra on the simulator graph to compute a deterministic latency
                try {
                    modules.models.Model sourceModel = scannerModel;
                    modules.models.Model destModel = globeManager.findModelByIp(resolvedIp);
                    if (sourceModel != null && destModel != null) {
                        modules.network.Graph.Algo algo = new modules.network.Graph.Algo(globeManager.grid);
                        // run dijkstra from source id to target id and use the returned cost
                        double[] dist = algo.dijkstra(sourceModel.getModelID(), destModel.getModelID());
                        int targetId = destModel.getModelID();
                        if (targetId >= 0 && targetId < dist.length && !Double.isInfinite(dist[targetId])) {
                            // convert cost to deterministic RTT (ms) similarly to PingCommand
                            double baseRttMs = Math.max(1.0, 2.0 * dist[targetId] * 100.0);
                            latency = baseRttMs / 1000.0; // convert to seconds
                        } else {
                            latency = 0.1; // default when unreachable
                        }
                    } else {
                        latency = 0.1;
                    }
                } catch (Exception e) {
                    latency = 0.1;
                }

                System.out.printf("Host is up (%.2fs latency).%n", latency);
            }

            // Run the scan (NmapCommand will print its own report)
            nmap.scan(target, startPort, endPort, timeout);
            System.out.println("Nmap done: 1 IP address (1 host up) scanned\n");
        }

        System.out.println("Exiting interactive tester.");
        scanner.close();
    }
}
