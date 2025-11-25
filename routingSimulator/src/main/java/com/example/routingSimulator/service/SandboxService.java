package com.example.routingSimulator.service;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.routingSimulator.modules.manager.GlobeManager;
import com.example.routingSimulator.service.SandboxRegistry;
import com.fasterxml.jackson.databind.JsonNode;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
                + "background-color: rgba(0,0,0,0.5); "  // semi-transparent overlay
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
        //i want the user to give a json object here and use post for him to create a new sandbox, the output of which will give the user the id of the sandbox
        sb.append("<a href='/play/sandbox' style='color:red;text-decoration:none;'>/play/sandbox</a>").append("&nbsp;&nbsp;<span style='color:yellow;'>- creates a new sandbox to work on</span><br>");
        sb.append("<br><hr style='border-color:gray;'><br>");
        sb.append("<h3 style='color:cyan;'>Create new Sandbox</h3>");
        sb.append("<form method='POST' action='/play/sandbox' " +
                "style='display:flex;flex-direction:column;max-width:400px;'>");
        sb.append("<textarea name='json' placeholder='Enter JSON here...' " +
                "style='height:150px;width:100%;background:black;color:white;border:1px solid gray;padding:10px;'></textarea>");
        sb.append("<button type='submit' " +
                "style='margin-top:10px;padding:10px;background:darkred;border:none;color:white;'>Create Sandbox</button>");
        sb.append("</form>");
        sb.append("<a href='/play' style='color:red;text-decoration:none;'>/play</a>").append("&nbsp;&nbsp;<span style='color:yellow;'>- goes back to the playground page</span><br>");

        sb.append("</div>");

        return sb.toString();
    }
    public String createSandbox(String json) {
        try {
            JsonNode node=objectMapper.readTree(json);
            String name = node.get("name").asText();
            GlobeManager globeManager = new GlobeManager(name);
            int id = sandboxRegistry.register(globeManager);
            return "Sandbox created with ID: " + id;
        } catch (Exception e) {
            return "Invalid JSON format: " + e.getMessage();
        }
    }

}
