package com.example.routingSimulator.Controller;

import com.example.routingSimulator.service.SandboxService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

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
    @GetMapping("/{id}")
    public String openSandbox(@PathVariable int id) {
        return sandboxService.openSandbox(id);
    }
    @PostMapping("/{id}")
    public String updateSandbox(@PathVariable int id, @RequestParam("json") String json) {
        return sandboxService.updateSandbox(id, json);
    }
    @GetMapping("/{id}/managers")
    public String getAllManagers(@PathVariable int id){
        return sandboxService.getAllManagers(id);
    }
    @GetMapping("/{id}/{mid}/devices")
    public String getAllDevices(@PathVariable int id,@PathVariable String mid){
        return sandboxService.getAllDevices(id,mid);
    }
    @GetMapping("/{id}/{deviceid}")
    public String getOpenPorts(@PathVariable int id,@PathVariable String deviceid){
        return sandboxService.getAllPorts(id,deviceid);
    }
    @GetMapping("/{id}/network")
    public String getNetworkGraph(@PathVariable int id){
        return sandboxService.getNetworkGraph(id);
    }

    @GetMapping("/{id}/network/path/{u}/{v}")
    public String getPathFromUtoV(@PathVariable int id,@PathVariable int u,@PathVariable int v)
    {
        return sandboxService.getPathFromUtoV(id,u,v);
    }

    @GetMapping("/{id}/network/path/cost/{u}/{v}")//just a mathematical cost
    public String getCostFromUtoV(@PathVariable int id,@PathVariable int u,@PathVariable int v)
    {
        return sandboxService.getCostFromUtoV(id,u,v);
    }

    @GetMapping("/{id}/network/distance/{u}")
    public String getDistanceFromUtoAll(@PathVariable int id,@PathVariable int u)
    {
        return sandboxService.getDistanceFromUtoAll(id,u);
    }

    @GetMapping("{id}/network/fullpathDetails/{u}/{v}")
    public String getFullPathDetailsFromUtoV(@PathVariable int id,@PathVariable int u,@PathVariable int v)
    {
        return sandboxService.getFullPathDetailsFromUtoV(id,u,v);
    }

}
