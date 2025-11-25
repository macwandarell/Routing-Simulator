package com.example.routingSimulator.service;

import com.example.routingSimulator.modules.network.Graph.Algo;
import com.example.routingSimulator.modules.network.Graph.Network;
import com.example.routingSimulator.modules.network.Link.Link;
import com.example.routingSimulator.modules.network.Link.LinkSpeedType;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.routingSimulator.modules.manager.GlobeManager;
import com.example.routingSimulator.service.SandboxRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.example.routingSimulator.modules.manager.Manager;
import com.example.routingSimulator.modules.models.Model;
import com.example.routingSimulator.modules.models.router.Router;
import com.example.routingSimulator.modules.models.device.Device;
import com.example.routingSimulator.modules.models.dhcp.Dhcp;
import com.example.routingSimulator.modules.models.publicServer.PublicServer;
import com.example.routingSimulator.modules.models.DnsServer.DNSServer;
import java.util.ArrayList;





import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class SandboxService {
    private final ObjectMapper objectMapper =  new ObjectMapper();
    private final SandboxRegistry sandboxRegistry;
    public SandboxService(SandboxRegistry sandboxRegistry) {
        this.sandboxRegistry = sandboxRegistry;
    }
    public String getWelcomeMessage() {
        StringBuilder sb = new StringBuilder();

        // Full-page div with background image and semi-transparent overlay
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
        sb.append("<a href='/play/sandbox' style='color:red;text-decoration:none;'>/play/sandbox</a>").append("&nbsp;&nbsp;<span style='color:yellow;'>- creates a new sandbox to work on</span><br>");
        sb.append("<br><hr style='border-color:gray;'><br>");
        sb.append("<h3 style='color:cyan;'>Create new Sandbox</h3>");
        sb.append("<form method='POST' action='/play/sandbox' " +
                "style='display:flex;flex-direction:column;max-width:400px;'>");
        sb.append("<textarea name='json' " +
                "style='height:150px;width:100%;background:black;color:white;border:1px solid gray;padding:10px;'>");
        sb.append("{\"name\":\"mySandbox\"}");
        sb.append("</textarea>");
        sb.append("<button type='submit' " +
                "style='margin-top:10px;padding:10px;background:darkred;border:none;color:white;'>Create Sandbox</button>");
        sb.append("</form>");

        sb.append("<a href='/play' style='color:red;text-decoration:none;'>/play</a>").append("&nbsp;&nbsp;<span style='color:yellow;'>- goes back to the playground page</span><br>");

        sb.append("</div>");

        return sb.toString();
    }
    public String createSandbox(String json) {
        StringBuilder sb= new StringBuilder();
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
        try {
            JsonNode node=objectMapper.readTree(json);
            String name = node.get("name").asText();
            GlobeManager globeManager = new GlobeManager(name);
            int id = sandboxRegistry.register(globeManager);
            sb.append("<br>");
            sb.append("<span style='color:yellow;'>Created Sandbox Successfully.Here is a list of the available commands (case doesn't matter)</span><br>");
            sb.append("<a href='/play/sandbox/").append(id).append("' style='color:red;text-decoration:none;'>/play/sandbox/").append(id).append("</a>").append("&nbsp;&nbsp;<span style='color:yellow;'>- go to the created sandbox</span><br>");
            sb.append("<a href='/play/sandbox' style='color:red;text-decoration:none;'>/play/sandbox</a>").append("&nbsp;&nbsp;<span style='color:yellow;'>- goes back to the sandbox page</span><br>");
            sb.append("<a href='/play' style='color:red;text-decoration:none;'>/play</a>").append("&nbsp;&nbsp;<span style='color:yellow;'>- goes back to the playground page</span><br>");
            sb.append("</div>");
            return sb.toString();
        } catch (Exception e) {
            sb.append("<br>");
            sb.append("<span style='color:yellow;'>Couldn't create Sandbox:").append(e.getMessage()).append("</span><br>");
            sb.append("<span style='color:yellow;'>Here is a list of the available commands (case doesn't matter)</span><br>");
            sb.append("<a href='/play/sandbox' style='color:red;text-decoration:none;'>/play/sandbox</a>").append("&nbsp;&nbsp;<span style='color:yellow;'>- goes back to the sandbox page</span><br>");
            sb.append("<a href='/play' style='color:red;text-decoration:none;'>/play</a>").append("&nbsp;&nbsp;<span style='color:yellow;'>- goes back to the playground page</span><br>");
            sb.append("</div>");
            return sb.toString();
        }
    }
    public String openSandbox(int id){
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
        sb.append("<h3 style='color:cyan;'>Create new manager in this Sandbox</h3>");
        sb.append("<h3 style='color:cyan;'>Create new manager in this Sandbox</h3>");
        sb.append("<form method='POST' action='/play/sandbox/").append(id).append("' style='display:flex;flex-direction:column;max-width:400px;'>");
        sb.append("<textarea name='json' style='height:150px;width:100%;background:black;color:white;border:1px solid gray;padding:10px;'>");
        sb.append("{\n  \"addManager\": {\n    \"id\": \"manager1\"\n  }\n}");
        sb.append("</textarea>");
        sb.append("<button type='submit' style='margin-top:10px;padding:10px;background:darkred;border:none;color:white;'>Create Manager</button>");
        sb.append("</form>");
        sb.append("<a href='/play' style='color:red;text-decoration:none;'>/play</a>").append("&nbsp;&nbsp;<span style='color:yellow;'>- goes back to the playground page</span><br>");

        sb.append("</div>");

        GlobeManager globeManager= sandboxRegistry.get(id);
        sb.append(globeManager.printView());
        return sb.toString();
    }
    public String updateSandbox(int id, String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            GlobeManager globeManager = sandboxRegistry.get(id);




            if (globeManager == null) {
                return "Sandbox with ID " + id + " not found.";
            }
            String responseMessage = handleCommands(globeManager, root);


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
            sb.append("<h3 style='color:cyan;'>Create new manager in this Sandbox</h3>");
            sb.append("<form method='POST' action='/play/sandbox/").append(id).append("' style='display:flex;flex-direction:column;max-width:400px;'>");
            sb.append("<textarea name='json' style='height:150px;width:100%;background:black;color:white;border:1px solid gray;padding:10px;'>");
            sb.append("{\n  \"addManager\": {\n    \"id\": \"manager1\"\n  }\n}");
            sb.append("</textarea>");
            sb.append("<button type='submit' style='margin-top:10px;padding:10px;background:darkred;border:none;color:white;'>Create Manager</button>");
            sb.append("</form>");


            sb.append("<br><hr style='border-color:gray;'><br>");

            sb.append("<h3 style='color:cyan;'>Add new device in a particular manager in this Sandbox</h3>");
            sb.append("<form method='POST' action='/play/sandbox/").append(id).append("' style='display:flex;flex-direction:column;max-width:400px;'>");
            sb.append("<textarea name='json' style='height:150px;width:100%;background:black;color:white;border:1px solid gray;padding:10px;'>");
            sb.append("{\n  \"addDevice\": {\n    \"managerId\": \"manager1\",\n    \"deviceId\": \"device1\",\n    \"deviceType\": \"device1\"\n }\n}");
            sb.append("</textarea>");
            sb.append("<button type='submit' style='margin-top:10px;padding:10px;background:darkred;border:none;color:white;'>Add Device</button>");
            sb.append("</form>");

            sb.append("<br><hr style='border-color:gray;'><br>");

            sb.append("<h3 style='color:cyan;'>Add dhcp ips to a particular manager dhcp in this Sandbox</h3>");
            sb.append("<form method='POST' action='/play/sandbox/").append(id).append("' style='display:flex;flex-direction:column;max-width:400px;'>");
            sb.append("<textarea name='json' style='height:150px;width:100%;background:black;color:white;border:1px solid gray;padding:10px;'>");
            sb.append("{\n  \"addDhcpIpList\": {\n    \"managerId\": \"manager1\",\n    \"number\": 50\n}\n}");
            sb.append("</textarea>");
            sb.append("<button type='submit' style='margin-top:10px;padding:10px;background:darkred;border:none;color:white;'>Add IPs to DHCP</button>");
            sb.append("</form>");


            sb.append("<br><hr style='border-color:gray;'><br>");
            sb.append("<h3 style='color:cyan;'>Add new link between 2 devices</h3>");
            sb.append("<form method='POST' action='/play/sandbox/").append(id).append("' style='display:flex;flex-direction:column;max-width:400px;'>");
            sb.append("<textarea name='json' style='height:150px;width:100%;background:black;color:white;border:1px solid gray;padding:10px;'>");
            sb.append("{\n  \"addLink\": {\n    \"fromNode\": \"Model 1's id\",\n    \"toNode\": \"Model 2's id\",\n    \"LinkBandwidth\": \"1000\",\n    \"Type\": \"KBPS\"\n }\n}");
            sb.append("</textarea>");
            sb.append("<button type='submit' style='margin-top:10px;padding:10px;background:darkred;border:none;color:white;'>Add Link</button>");
            sb.append("</form>");

            sb.append("<br><hr style='border-color:gray;'><br>");

            sb.append("<h3 style='color:cyan;'>Add Port to a device</h3>");
            sb.append("<form method='POST' action='/play/sandbox/").append(id).append("' style='display:flex;flex-direction:column;max-width:400px;'>");
            sb.append("<textarea name='json' style='height:150px;width:100%;background:black;color:white;border:1px solid gray;padding:10px;'>");
            sb.append("{\n  \"addPort\": {\n    \"modelID\": \"1\",\n    \"portNo\": 1\n }\n}");
            sb.append("</textarea>");
            sb.append("<button type='submit' style='margin-top:10px;padding:10px;background:darkred;border:none;color:white;'>Add Port</button>");
            sb.append("</form>");

            sb.append("<br><hr style='border-color:gray;'><br>");
            sb.append("<h3 style='color:cyan;'>Get Shorted Path between 2 devices</h3>");
            sb.append("<form method='POST' action='/play/sandbox/").append(id).append("' style='display:flex;flex-direction:column;max-width:400px;'>");
            sb.append("<textarea name='json' style='height:150px;width:100%;background:black;color:white;border:1px solid gray;padding:10px;'>");
            sb.append("{\n  \"getshortestpath\": {\n    \"fromNode\": \"Model 1's id\",\n    \"toNode\": \"Model 2's id\"\n }\n}");
            sb.append("</textarea>");
            sb.append("<button type='submit' style='margin-top:10px;padding:10px;background:darkred;border:none;color:white;'>Get Path</button>");
            sb.append("</form>");




            sb.append(String.format(
                    "<a href='/play/sandbox/%s/command' style='color:red;text-decoration:none;'>/play/sandbox/%s/command</a>"
                            + "&nbsp;&nbsp;<span style='color:yellow;'>- goes back to this sandbox page</span><br>",
                    id, id));

            sb.append("<a href='/play' style='color:red;text-decoration:none;'>/play</a>").append("&nbsp;&nbsp;<span style='color:yellow;'>- goes back to the playground page</span><br>");
            sb.append("<p style='color:yellow;'>Last action: " + responseMessage + "</p><br>");

            sb.append("</div>");
            sb.append(globeManager.printView());


            return sb.toString();

        } catch (Exception e) {
            return "Invalid JSON: " + e.getMessage();
        }
    }

    private String handleCommands(GlobeManager globeManager, JsonNode root) {
        try {
            if (root.has("addManager")) {
                JsonNode cmd = root.get("addManager");

                String id = cmd.get("id").asText();
                Manager manager = new Manager(id);
                globeManager.addManager(manager);
                return "Added manager " + id;
            }
            else if (root.has("addDevice")) {
                JsonNode cmd = root.get("addDevice");

                String managerId = cmd.get("managerId").asText();
                String deviceId = cmd.get("deviceId").asText();
                String deviceType= cmd.get("deviceType").asText();
                Manager manager = globeManager.findManagerById(managerId);
                if (manager == null) {
                    return "Manager with ID " + managerId + " not found.";
                }
                Model model = null;

                // change the constructor of this models
                // laude ata nai kya
                 if(deviceType.equals("Device")) {
                     model = new Device(deviceId);
                 }
                 else if(deviceType.equals("DHCP")){
                     model = new Dhcp(deviceId);
                 }
                 else if(deviceType.equals("PublicServer")){
                     model = new PublicServer(deviceId);
                 }
                 else if(deviceType.equals("DNSServer")){
                     model = new DNSServer(deviceId);
                 }
                 else
                if(deviceType.equals("Router")) {
                    model = new Router(deviceId);
                }
                manager.addEntity(model);
                Network grid=globeManager.getNetwork();
                grid.addNode(model);

                    return "Added device " + deviceId + " to manager " + managerId;

            }
            else if (root.has("addDhcpIpList")){
                JsonNode cmd = root.get("addDhcpIpList");
                String managerId = cmd.get("managerId").asText();
                int no = cmd.get("number").asInt();
                Manager manager = globeManager.findManagerById(managerId);
                if (manager == null) {
                    return "Manager with ID " + managerId + " not found.";
                }
                globeManager.giveManagerDhcpIp(managerId,no);
                return "Added DHCP IPs " + no + " ,to manager " + managerId;

            }
            else if(root.has("addLink")){
                JsonNode cmd = root.get("addLink");
                String fromNode = (cmd.get("fromNode").asText());
                String toNode = (cmd.get("toNode").asText());
                int linkBandwidth = Integer.valueOf(cmd.get("LinkBandwidth").asText());
                String type = cmd.get("Type").asText();
//                System.out.println("link errorrrr");
                Link edge= new Link(globeManager.findModelByID(fromNode),globeManager.findModelByID(toNode),linkBandwidth,type);
                Network grid=globeManager.getNetwork();
                grid.addEdge(edge);
//                System.out.println("link errorrrr22");
                return "Added link between Model id:" + fromNode + " and Model id:" + toNode;
            }
            else if(root.has("addPort")){
                JsonNode cmd = root.get("addPort");
                String modelID=cmd.get("modelID").asText();
                int portNo=cmd.get("portNo").asInt();
                Model model = globeManager.findModelByID(modelID);
                if(model instanceof Device){
                    model.addPort(portNo);
                    return  "Added port " + portNo + " to model " + modelID;
                }
                return "The model id is not of a device";
            }
            else if(root.has("getshortestpath")){
                JsonNode cmd = root.get("getshortestpath");
                String fromNode = (cmd.get("fromNode").asText());
                String toNode = (cmd.get("toNode").asText());
                Algo algo= globeManager.getDijkstra();
                List<double[]> pathLinks= algo.findShortestPath(globeManager.findModelByID(fromNode),globeManager.findModelByID(toNode));
                if(pathLinks==null){
                    return "No path found between Model id:" + fromNode + " and Model id:" + toNode;
                }
                StringBuilder pathStr = new StringBuilder();
                pathStr.append("Shortest path from Model id: ").append(fromNode).append(" to Model id: ").append(toNode).append(" is:<br>");
                pathStr.append("Start at Model id: ").append(fromNode).append("<br>");
                for (double[] linkInfo : pathLinks) {
                    int modelId = (int) linkInfo[0];
                    double bandwidth = linkInfo[1];
                    pathStr.append(" --[").append(bandwidth).append(" Kbps]--> Model id: ").append(modelId).append("<br>");
                }
                return pathStr.toString();
            }

            return "Unknown command.";

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    public String getAllManagers(int id){
        StringBuilder sb= new StringBuilder();
        GlobeManager globeManager = sandboxRegistry.get(id);
        if (globeManager == null) {
            return "Sandbox with ID " + id + " not found.";
        }
        ArrayList<Manager> managerArrayList = globeManager.getAllManager();
        for(Manager m: managerArrayList){
            sb.append(m.viewDetails()).append("<br>");
        }
        return sb.toString();
    }
    public String getAllDevices(int id,String mid){
        StringBuilder sb= new StringBuilder();
        GlobeManager globeManager = sandboxRegistry.get(id);
        if (globeManager == null) {
            return "Sandbox with ID " + id + " not found.";
        }
        Manager manager = globeManager.findManagerById(mid);
        if (manager == null) {
            return "Manager with ID " + mid + " not found.";
        }
        ArrayList<Model> deviceArrayList = globeManager.getAllDevicesofManager(mid);
        for(Model m: deviceArrayList){
            if(m!=null){
            sb.append(m.getModelID()).append(" - ").append(m.getId()).append(" - ").append(m.getType()).append("<br>");}
        }
        return sb.toString();
    }

    public String getNetworkGraph(int id){
        StringBuilder sb= new StringBuilder();
        GlobeManager globeManager = sandboxRegistry.get(id);
        if (globeManager == null) {
            return "Sandbox with ID " + id + " not found.";
        }
        Network network = globeManager.getNetwork();
        ArrayList<ArrayList<Link>> adjList = network.getAdjList();
        for(int i=0;i<adjList.size();i++){
            sb.append("Model ID ").append(i).append(" connections:<br>");
            for(Link link: adjList.get(i)){
                sb.append("  -> Model ID ").append(link.getSecond().getModelID())
                  .append(" (Bandwidth: ").append(link.getSpeed(LinkSpeedType.KBPS)).append(" Kbps)<br>");
            }
            sb.append("<br>");
        }
        return sb.toString();
    }


}
