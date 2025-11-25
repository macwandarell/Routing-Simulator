package com.example.routingSimulator.Controller;

import com.example.routingSimulator.service.PlayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/play")
public class PlayController {
    private final PlayService playService;

    public PlayController(PlayService playService) {
        this.playService = playService;
    }

    @GetMapping
    public String index() {
        return playService.getWelcomeMessage();
    }

}
