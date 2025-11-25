package com.example.routingSimulator.service;

import com.example.routingSimulator.modules.manager.GlobeManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SandboxRegistry {
    private final Map<Integer, GlobeManager> sandboxes = new HashMap<>();
    private int nextId = 1;
    public int register(GlobeManager manager) {
        int id = nextId++;
        sandboxes.put(id, manager);
        return id;
    }
    public GlobeManager get(int id) {
        return sandboxes.get(id);
    }
}
