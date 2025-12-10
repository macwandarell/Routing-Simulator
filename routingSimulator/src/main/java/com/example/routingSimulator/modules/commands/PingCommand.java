package com.example.routingSimulator.modules.commands;

import com.example.routingSimulator.modules.manager.GlobeManager;
import com.example.routingSimulator.modules.network.Graph.Algo;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PingCommand {

    // We use a constant scaling factor that converts path "cost" from the graph
    // into milliseconds for Round Trip Time (RTT) calculation
    private static final double LATENCY_SCALE = 100.0;

    // Reference to the global network manager (contains all routers, links, devices)
    private final GlobeManager globeManager;

    // Constructor to initialize the PingCommand with access to the network graph
    public PingCommand(GlobeManager globeManager) {
        this.globeManager = globeManager;
    }

    /**
     * Simulates a ping operation between two models in the network.
     *
     * @param srcModelId  ID of the source node in the graph
     * @param destModelId ID of the destination node in the graph
     * @param destIp      Destination IP (for display / context only)
     * @param count       Number of ping attempts to simulate
     * @return List of RTT times (in milliseconds) for each ping
     */
    public List<Double> pingForApi(int srcModelId, int destModelId, String destIp, int count) {

        // We get total number of nodes (models) in the network graph
        int nodeCount = globeManager.grid.getAdjList().size();

        // This is to validate that the graph is not empty and model IDs are within valid range
        if (nodeCount == 0 ||
                srcModelId < 0 || srcModelId >= nodeCount ||
                destModelId < 0 || destModelId >= nodeCount) {
            System.out.println("⚠️ Invalid source/destination ID or empty network.");
            return List.of(); // Return empty list if network or IDs are invalid
        }

        // Here we create an instance of Dijkstra's algorithm on the current network
        Algo algo = new Algo(globeManager.grid);

        // Running Dijkstra to get the shortest path cost between source and destination
        double[] dist = algo.dijkstra(srcModelId, destModelId);

        // In case where the destination is unreachable, return an empty list
        if (Double.isInfinite(dist[destModelId])) {
            System.out.println("⚠️ No valid route found.");
            return List.of();
        }

        // Converting the path cost to a base Round Trip Time (RTT) in milliseconds
        double baseRttMs = computeRttFromCost(dist[destModelId]);

        // List to hold RTT values for each ping attempt
        List<Double> times = new ArrayList<>();

        // we simulate multiple ping attempts with random jitter (variation)
        for (int i = 0; i < count; i++) {
            times.add(addJitter(baseRttMs));
        }

        // Return list of RTTs (used by API to compute min/avg/max)
        return times;
    }

    /**
     * Converts the shortest path "cost" to a base Round Trip Time (RTT) in milliseconds.
     * RTT = 2 * (path cost) * LATENCY_SCALE
     * The factor of 2 represents the round-trip (send + receive).
     * Ensures RTT is never below 1ms for realism.
     */
    private double computeRttFromCost(double cost) {
        double rtt = 2.0 * cost * LATENCY_SCALE;
        return Math.max(1.0, rtt); // This is to avoid unrealistic 0.0 RTTs
    }

    /**
     * Adds random jitter (±15%) to simulate real-world network fluctuation.
     * Ensures RTT never drops below 1ms.
     */
    private double addJitter(double baseRtt) {
        double jitterRange = baseRtt * 0.15; // 15% variability
        double delta = ThreadLocalRandom.current().nextDouble(-jitterRange, jitterRange);
        return Math.max(1.0, baseRtt + delta);
    }
}
