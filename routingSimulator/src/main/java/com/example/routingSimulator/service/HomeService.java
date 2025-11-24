package com.example.routingSimulator.service;

import org.springframework.stereotype.Service;
import modules.services.AnsiColor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class HomeService {

    public String getWelcomeMessage() {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader("modules/terminal/banners/logo.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("<br>"); // HTML line break
            }
        } catch (IOException e) {
            e.printStackTrace();
            sb.append("Error loading welcome message.<br>");
        }

        sb.append("<span style='color:blue'>Here is a list of the available commands (case doesn't matter)</span><br>");
        sb.append("<span style='color:red'>/help</span><span style='color:blue'> - prints a list of executable commands</span><br>");
        sb.append("<span style='color:red'>/play</span><span style='color:blue'> - goes to playground page</span><br>");

        return sb.toString();
    }

}
