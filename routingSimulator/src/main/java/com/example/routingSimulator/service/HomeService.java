package modules.service;

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
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            sb.append("Error loading welcome message.\n");
        }

        sb.append(AnsiColor.YELLOW.wrap("Here is a list of the available commands (case doesn't matter)\n"));
        sb.append(AnsiColor.RED.wrap("/help"))
                .append(AnsiColor.YELLOW.wrap(" - prints a list of executable commands"));

        return sb.toString();
    }
}
