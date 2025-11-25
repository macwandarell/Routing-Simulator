package com.example.routingSimulator.service;
import com.example.routingSimulator.modules.commands.PingCommand;
import java.util.List;

// START OF MODIFICATION: Added necessary imports for DNS logic
import java.util.Map;
import com.example.routingSimulator.modules.commands.NsLookup;

import com.example.routingSimulator.modules.manager.GlobeManager;
import com.example.routingSimulator.modules.manager.Manager;
import com.example.routingSimulator.modules.models.DnsServer.DNSServer;
import com.example.routingSimulator.modules.models.Model;
import com.example.routingSimulator.modules.models.device.Device;
import com.example.routingSimulator.modules.models.dhcp.Dhcp;
import com.example.routingSimulator.modules.models.publicServer.PublicServer;
import com.example.routingSimulator.modules.models.router.Router;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import com.example.routingSimulator.modules.commands.NmapCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class CommandService {

    private final ObjectMapper objectMapper =  new ObjectMapper();
    private final SandboxRegistry sandboxRegistry;
    public CommandService(SandboxRegistry sandboxRegistry) {
        this.sandboxRegistry = sandboxRegistry;
    }
    public int print(int id) {
        // Placeholder implementation for the ping command
        return id;
    }

    public String getWelcomeMessage(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style=\""
                + "background-image: url('/banners/background.jpg'); "
                + "background-size: cover; "
                + "background-repeat: no-repeat; "
                + "background-position: center; "
                + "width: 100vw; "
                + "height: 100vh; "
                + "color: white; "
                + "font-family: monospace; "
                + "padding: 20px; "
                + "box-sizing: border-box; "
                + "background-color: rgba(0,0,0,0.5); "
                + "overflow-y: auto;"
                + "\">");

        // Load logo.txt from classpath
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/banners/sandbox.txt")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("<br>");
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            sb.append("Error loading welcome message.<br>");
        }

        // Commands section
        sb.append("<br>");
        sb.append("<span style='color:yellow;'>Here is a list of the available commands (case doesn't matter)</span><br>");
        sb.append("<a href='/play/sandbox/").append(id).append("' style='color:red;text-decoration:none;'>/play/sandbox/").append(id).append("</a>").append("&nbsp;&nbsp;<span style='color:yellow;'>- The current page you are in</span><br>");
        sb.append("<br><hr style='border-color:gray;'><br>");
        sb.append("<h3 style='color:cyan;'>NSLookup Command</h3>");
        // START OF MODIFICATION: Updated HTML to show correct DNS JSON format
        sb.append("<p style='font-size:12px;color:gray;'>Keys: 'nslookup', 'addDnsRecord', 'deleteDnsRecord', 'getDnsRecords'</p>");
        sb.append("<form method='POST' action='/play/sandbox/").append(id).append("/command").append("' style='display:flex;flex-direction:column;max-width:400px;'>");
        sb.append("<textarea name='json' style='height:150px;width:100%;background:black;color:white;border:1px solid gray;padding:10px;'>");
        sb.append("{\n  \"nslookup\": {\n    \"domain\": \"google.com\"\n  }\n}");
        sb.append("</textarea>");

        sb.append("<button type='submit' style='margin-top:10px;padding:10px;background:darkred;border:none;color:white;'>NSLookup</button>");
        sb.append("</form>");


        sb.append("<br><hr style='border-color:gray;'><br>");

        sb.append("<h3 style='color:cyan;'>NMAP</h3>");
        sb.append("<form method='POST' action='/play/sandbox/").append(id).append("/command").append("' style='display:flex;flex-direction:column;max-width:400px;'>");
        sb.append("<textarea name='json' style='height:150px;width:100%;background:black;color:white;border:1px solid gray;padding:10px;'>");
        sb.append("{\n" + "  \"nmap\": {\n" + "    \"target\": \"scanme.nmap.org\",\n" + "    \"startPort\": 1,\n" + "    \"endPort\": 1024,\n" + "    \"timeoutMs\": 200\n" + "  }\n" + "}");
        sb.append("</textarea>");
        sb.append("<button type='submit' style='margin-top:10px;padding:10px;background:darkred;border:none;color:white;'>NMAP</button>");
        sb.append("</form>");

        sb.append("<br><hr style='border-color:gray;'><br>");

        sb.append("<h3 style='color:cyan;'>Ping</h3>");
        sb.append("<form method='POST' action='/play/sandbox/").append(id).append("command").append("' style='display:flex;flex-direction:column;max-width:400px;'>");
        sb.append("<textarea name='json' style='height:150px;width:100%;background:black;color:white;border:1px solid gray;padding:10px;'>");
        sb.append("{\n" +
                "  \"Ping\": {\n" +
                "    \"modelID\": \"1\",\n" +
                "    \"destIp\": \"193.168.0.45\",\n" +
                "    \"count\": 4\n" +
                "  }\n" +
                "}");

        sb.append("</textarea>");
        sb.append("<button type='submit' style='margin-top:10px;padding:10px;background:darkred;border:none;color:white;'>PING</button>");
        sb.append("</form>");
        sb.append("<div id='pingOutput' "
                + "style='margin-top:15px;padding:10px;background-color:black;color:lime;"
                + "border:1px solid gray;width:100%;max-width:400px;min-height:50px;"
                + "font-family:monospace;white-space:pre-wrap;'>"
                + "Output will appear here..."
                + "</div>");
        sb.append("<script>"
                + "document.querySelector(\"form[action='/play/sandbox/" + id + "'] button[type='submit']\").addEventListener('click', function(e) {"
                + "e.preventDefault();"
                + "var form = this.closest('form');"
                + "var data = new FormData(form);"
                + "fetch(form.action, { method: 'POST', body: data })"
                + ".then(r => r.text())"
                + ".then(html => {"
                + "document.getElementById('pingOutput').innerText = html;"
                + "})"
                + ".catch(err => {"
                + "document.getElementById('pingOutput').innerText = 'Error: ' + err;"
                + "});"
                + "});"
                + "</script>");



        sb.append(String.format(
                "<a href='/play/sandbox/%s/command' style='color:red;text-decoration:none;'>/play/sandbox/%s/command</a>"
                        + "&nbsp;&nbsp;<span style='color:yellow;'>- goes back to this sandbox page</span><br>",
                id, id));

        sb.append("<a href='/play' style='color:red;text-decoration:none;'>/play</a>").append("&nbsp;&nbsp;<span style='color:yellow;'>- goes back to the playground page</span><br>");
//        sb.append("<p style='color:yellow;'>Last action: " + responseMessage + "</p><br>");

        sb.append("</div>");
//        sb.append(globeManager.printView());


        return sb.toString();

    }

    public String updateCommand(int id, String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            GlobeManager globeManager = sandboxRegistry.get(id);




            if (globeManager == null) {
                return "Sandbox with ID " + id + " not found.";
            }
            String responseMessage = handleCommands(globeManager, root);


            // START OF MODIFICATION: Prepend the Result to the existing HTML structure
            // This reuses the getWelcomeMessage method but inserts the result
            String baseHtml = getWelcomeMessage(id);
            String resultHtml = "<div style='border: 1px solid lime; padding: 10px; background: rgba(0,0,0,0.8); color:lime; font-family:monospace; margin: 20px;'><strong>RESULT:</strong><br>" + responseMessage + "</div>";
            return baseHtml.replaceFirst("\">", "\">" + resultHtml);
            // END OF MODIFICATION

        } catch (Exception e) {
            return "Invalid JSON: " + e.getMessage();
        }
    }

    private String handleCommands(GlobeManager globeManager, JsonNode root) {
        try {

            // START OF MODIFICATION: Replaced incorrect "addManager" logic with DNS Logic using globeManager
            // Check for any of the 4 DNS keys
            if (root.has("nslookup") || root.has("addDnsRecord") || root.has("deleteDnsRecord") || root.has("getDnsRecords")) {

                // Get the DNS Server from the GlobeManager as requested
                DNSServer dnsServer = globeManager.getDnsServer();

                if (dnsServer == null) {
                    return "Error: No DNS Server instance found in this Sandbox.";
                }

                if (root.has("nslookup")) {
                    JsonNode cmd = root.get("nslookup");
                    String domain = cmd.get("domain").asText();
                    NsLookup lookup = new NsLookup();
                    return lookup.execute(dnsServer, domain);
                }
                else if (root.has("addDnsRecord")) {
                    JsonNode cmd = root.get("addDnsRecord");
                    String domain = cmd.get("domain").asText();
                    String ip = cmd.get("ip").asText();
                    dnsServer.addDnsRecord(domain, ip);
                    return "Success: Added DNS Record [" + domain + " -> " + ip + "]";
                }
                else if (root.has("deleteDnsRecord")) {
                    JsonNode cmd = root.get("deleteDnsRecord");
                    String domain = cmd.get("domain").asText();
                    boolean removed = dnsServer.removeDnsRecord(domain);
                    return removed ? "Success: Removed record for " + domain : "Error: Domain not found.";
                }
                else if (root.has("getDnsRecords")) {
                    Map<String, String> records = dnsServer.getDnsRecords();
                    if (records.isEmpty()) return "DNS Cache is empty.";
                    StringBuilder sb = new StringBuilder("Current DNS Records:\n");
                    records.forEach((k, v) -> sb.append(k).append(" -> ").append(v).append("\n"));
                    return sb.toString();
                }
            }
            // END OF MODIFICATION

            else if (root.has("nmap")) {
                JsonNode cmd = root.get("nmap");

                String tar = cmd.get("target").asText();
                String stport = cmd.get("startPort").asText();
                String edport = cmd.get("endPort").asText();
                String timeout = cmd.get("timeoutMs").asText();

                int startp = Integer.parseInt(stport);
                int endp = Integer.parseInt(edport);
                int timeo = Integer.parseInt(timeout);

                DNSServer dns = globeManager.getDnsServer();
                NmapCommand nmap = new NmapCommand(globeManager, dns, true);
                String result = nmap.scan(tar,startp,endp, timeo);
                return result;

            }
            else if (root.has("Ping")) {
                JsonNode cmd = root.get("Ping");

                String modelID = cmd.get("modelID").asText();
                String destIp = cmd.get("destIp").asText();
                int count = cmd.has("count") ? cmd.get("count").asInt() : 4;

                Model srcModel = globeManager.findModelByID(modelID);
                if (srcModel == null) {
                    return "Source IP " + modelID + " not found in this sandbox.";
                }

                Model destModel = globeManager.findModelByIp(destIp);
                if (destModel == null) {
                    return "Destination IP " + destIp + " not found in this sandbox.";
                }

                PingCommand pingCommand = new PingCommand(globeManager);
                List<Double> times = pingCommand.pingForApi(
                        srcModel.getModelID(),
                        destModel.getModelID(),
                        destIp,
                        count
                );

                if (times.isEmpty()) {
                    return "Ping failed: no route between " + modelID + " and " + destIp + ".";
                }

                double min = times.get(0);
                double max = times.get(0);
                double sum = 0.0;
                for (double t : times) {
                    if (t < min) min = t;
                    if (t > max) max = t;
                    sum += t;
                }
                double avg = sum / times.size();

                return String.format(
                        "Ping %s -> %s : %d packets, min=%.2fms, avg=%.2fms, max=%.2fms",
                        modelID, destIp, times.size(), min, avg, max
                );
            }


            return "Unknown command.";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
