package com.example.routingSimulator.modules.commands;

import com.example.routingSimulator.modules.manager.GlobeManager;
import com.example.routingSimulator.modules.models.DnsServer.DNSServer;
import com.example.routingSimulator.modules.models.Model;
import com.example.routingSimulator.modules.network.ip.Ipv4;
import com.example.routingSimulator.modules.models.device.Device;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Simple Nmap-like port scanner for the routing simulator.
 *
 * Contract:
 * - Input: target (IPv4 string or domain name), port range, timeout in ms
 * - Output: prints found open ports (and a simple summary) to stdout
 * - Errors: prints user-friendly messages for resolution failures or invalid args
 *
 * Notes/Assumptions:
 * - If the target is not a valid IPv4 string, we attempt DNS resolution via DNSServer.getIpForDomain
 * - "Free ports" are interpreted as "open ports" (commonly how nmap reports reachable services)
 * - Scanning is done by attempting a TCP connect with a timeout; no raw packets are used.
 */
public class NmapCommand {

    private final DNSServer dnsServer;
    private final GlobeManager globeManager;
    private final boolean simulateOnly;
    private final Map<Integer, String> commonServices = new HashMap<>();
    
    public NmapCommand(GlobeManager globeManager, DNSServer dnsServer, boolean simulateOnly) {
        this.globeManager = globeManager;
        this.dnsServer = dnsServer;
        this.simulateOnly = simulateOnly;
        loadServicesFromFile("data/services.txt");
    }

    public NmapCommand(GlobeManager globeManager, DNSServer dnsServer) {
        this(globeManager, dnsServer, false);
    }

    public boolean isIpv4(String input) {
        if (input == null) return false;
        // Very simple IPv4 validation
        String[] parts = input.trim().split("\\.");
        if (parts.length != 4) return false;
        try {
            for (String p : parts) {
                int v = Integer.parseInt(p);
                if (v < 0 || v > 255) return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String resolveTargetToIp(String target) {
        if (isIpv4(target)) return target;

        // Attempt to resolve using the provided DNSServer
        try {
            Ipv4 ip = dnsServer.getIpForDomain(target);
            return ip.getIpString();
        } catch (IllegalArgumentException e) {
            // let caller handle null
            return null;
        }
    }

    /**
     * Scans ports in the inclusive range [startPort, endPort] on the given target.
     * Prints open ports to stdout.
     */
    public String scan(String target, int startPort, int endPort, int timeoutMs) {
        StringBuilder sb = new StringBuilder();
        if (target == null || target.trim().isEmpty()) {
            sb.append("Nmap Error: target is empty\n");
            return sb.toString();
        }

        if (startPort < 1 || endPort > 65535 || startPort > endPort) {
            sb.append("Nmap Error: invalid port range\n");
            return sb.toString();
        }

        String ip = resolveTargetToIp(target);
        if (ip == null) {
            sb.append("Nmap: failed to resolve target '").append(target).append("'\n");
            return sb.toString();
        }
        sb.append("Nmap scan report for ").append(target).append(" (").append(ip).append(")\n");
        sb.append("PORT\tSTATE\tSERVICE\n");

        // If globeManager knows about a model at this IP and it's a Device, use its active ports.
        Model found = globeManager.findModelByIp(ip);
        List<Integer> openPorts = new ArrayList<>();

        if (found != null && found instanceof Device) {
            Device dev = (Device) found;
            for (Integer p : dev.getActivePorts()) {
                if (p >= startPort && p <= endPort) {
                    openPorts.add(p);
                    String svc = commonServices.getOrDefault(p, "");
                    sb.append(p).append("\topen\t").append(svc).append("\n");
                }
            }
        } else {
            // No device found in simulation: fall back to a quick simulated scan using timeouts.
            if (!simulateOnly) {
                for (int port = startPort; port <= endPort; port++) {
                    try (Socket socket = new Socket()) {
                        socket.connect(new InetSocketAddress(ip, port), timeoutMs);
                        sb.append(port).append("\topen\t").append(commonServices.getOrDefault(port, "")).append("\n");
                        openPorts.add(port);
                    } catch (Exception e) {
                        // treat as filtered/closed
                    }
                }
            }
            // otherwise no open ports in simulation
        }

        sb.append("\n");
        if (openPorts.isEmpty()) {
            sb.append("Not shown: ").append(endPort - startPort + 1).append(" filtered tcp ports (no-response)\n\n");
            sb.append("No open ports found in range ").append(startPort).append("-").append(endPort).append(".\n");
        } else {
            sb.append("Open ports: ");
            for (int i = 0; i < openPorts.size(); i++) {
                sb.append(openPorts.get(i));
                if (i < openPorts.size() - 1) sb.append(", ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void loadServicesFromFile(String path) {
        File f = new File(path);
        if (!f.exists() || !f.isFile()) {
            // fallback defaults
            commonServices.put(22, "ssh");
            commonServices.put(80, "http");
            commonServices.put(443, "https");
            commonServices.put(9929, "nping-echo");
            commonServices.put(31337, "Elite");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                // split on whitespace; allow service names with hyphens
                String[] parts = line.split("\\s+", 2);
                if (parts.length >= 2) {
                    try {
                        int port = Integer.parseInt(parts[0]);
                        String svc = parts[1].trim();
                        commonServices.put(port, svc);
                    } catch (NumberFormatException ignored) {
                        // skip invalid lines
                    }
                }
            }
        } catch (Exception e) {
            // on error, populate fallback
            commonServices.put(22, "ssh");
            commonServices.put(80, "http");
            commonServices.put(443, "https");
            commonServices.put(9929, "nping-echo");
            commonServices.put(31337, "Elite");
        }
    }

    /**
     * Convenience overload: scans common ports 1..1024 with 200ms timeout per port.
     */
    public void scan(String target) {
        scan(target, 1, 1024, 200);
    }
}
