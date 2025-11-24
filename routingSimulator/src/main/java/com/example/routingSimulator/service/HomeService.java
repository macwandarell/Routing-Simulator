package com.example.routingSimulator.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class HomeService {

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
                new InputStreamReader(getClass().getResourceAsStream("/banners/logo.txt")))) {
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
        sb.append("<span style='color:red;'>/</span>&nbsp;&nbsp;<span style='color:yellow;'>- prints a list of executable commands at home</span><br>");
        sb.append("<span style='color:red;'>/play</span>&nbsp;&nbsp;<span style='color:yellow;'>- goes to playground page</span><br>");

        sb.append("</div>");

        return sb.toString();
    }

}
