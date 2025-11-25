package com.example.routingSimulator.Controller;

import com.example.routingSimulator.service.SandboxService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/play/sandbox")
public class SandboxController {
    private final SandboxService sandboxService;
    public SandboxController(SandboxService sandboxService) {
        this.sandboxService = sandboxService;
    }

    @GetMapping
    public String index() {
        return sandboxService.getWelcomeMessage();
    }
    @PostMapping
    public String createSandbox(@RequestParam("json") String json) {
        return sandboxService.createSandbox(json);
    }

}
