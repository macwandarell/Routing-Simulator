package com.example.routingSimulator.Controller;

import com.example.routingSimulator.service.CommandService;
import org.springframework.web.bind.annotation.*;

@RestController  //Notation that tells SpringBoot that it is ready to accept requestd form the internet
@RequestMapping("/play/sandbox/{id}/command")
public class CommandController {

    private final CommandService commandService;
    public CommandController(CommandService commandService) {
        this.commandService = commandService;
    }


    //Logic: It asks the Service: "Give me the welcome message (HTML) for Sandbox 5.If the Sandbox ID is 5"
    @GetMapping
    public String index(@PathVariable int id) {
        return commandService.getWelcomeMessage(id);
    }

    //This runs when the user clicks 'Execute' on the Black Box (sends a POST request).
    //  @PathVariable: Captures the Sandbox ID (e.g., '1') from the URL.
    // @RequestParam("json"): This is the LINK. It grabs the text typed inside the HTML <textarea name="json">.
    // Action: We pass the raw text to the Service (the 'Kitchen') to parse and execute.
    @PostMapping
    public String updateCommand(@PathVariable int id, @RequestParam("json") String json) {
        return commandService.updateCommand(id,json);
    }




}
