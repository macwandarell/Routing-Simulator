package com.example.routingSimulator.Controller;

import com.example.routingSimulator.service.CommandService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/play/sandbox/{id}/command")
public class CommandController {

    private final CommandService commandService;
    public CommandController(CommandService commandService) {
        this.commandService = commandService;
    }

    @GetMapping
    public String index(@PathVariable int id) {
        return commandService.getWelcomeMessage(id);
    }



}
