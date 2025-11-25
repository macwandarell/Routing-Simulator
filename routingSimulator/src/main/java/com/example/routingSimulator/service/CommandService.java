package com.example.routingSimulator.service;

import org.springframework.stereotype.Service;

@Service
public class CommandService {

    public int print(int id) {
        // Placeholder implementation for the ping command
        return id;
    }

    public String getWelcomeMessage(int id) {
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
        sb.append("<h1 style=\"text-align: center; text-shadow: 2px 2px 4px #000000;\">Command Interface</h1>");
        sb.append("<p> The id of this sandbox is: " + id + "</p>");
        sb.append("<p>Welcome to the Command Interface of the Routing Simulator Sandbox!</p>");
        sb.append("<p>Here, you can execute various commands to interact with your sandbox environment .</p>");
        sb.append("<h2>Available Commands:</h2>");
        sb.append("<ul>");
        sb.append("<li>PingCommand</li>");
        sb.append("<li>NMap</li>");
        sb.append("<li>NSLookup</li>");

        return sb.toString();
    }
}
