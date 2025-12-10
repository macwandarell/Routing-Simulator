package com.example.routingSimulator.Controller;

import com.example.routingSimulator.service.SandboxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public String createSandbox(@RequestBody String json) {
        return sandboxService.createSandbox(json);
    }
    @GetMapping("/{id}")
    public String openSandbox(@PathVariable int id) {
        return sandboxService.openSandbox(id);
    }
    @PostMapping("/{id}")
    public String updateSandbox(@PathVariable int id, @RequestBody String json) {
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

    @GetMapping("/{id}/links")
    public String getAllLinks(@PathVariable int id){
        return sandboxService.getAllLinks(id);
    }

    @GetMapping("/{id}/links/{u}/{v}")
    public String getLinkDetails(@PathVariable int id,@PathVariable int u,@PathVariable int v) {
        return sandboxService.getLinkDetails(id, u, v);
    }

    @GetMapping("/{id}/links/bandwidth/{u}/{v}")
    public String getLinkBandwidth(@PathVariable int id,@PathVariable int u,@PathVariable int v) {
        return sandboxService.getLinkBandwidth(id, u, v);
    }

    @GetMapping("/{id}/links/{u}")
    public String getAllLinksFromU(@PathVariable int id,@PathVariable int u) {
        return sandboxService.getAllLinksFromU(id, u);
    }

    @DeleteMapping("/{id}/links/{u}/{v}")
    public ResponseEntity<String> deleteLink(@PathVariable int id,
                                             @PathVariable int u,
                                             @PathVariable int v) {

        boolean deleted = sandboxService.deleteLink(id, u, v);

        if (deleted) {
            return ResponseEntity.ok("Link deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Link not found");
        }
    }

}
