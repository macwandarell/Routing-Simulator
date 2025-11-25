package com.example.routingSimulator.modules.commands;

import com.example.routingSimulator.modules.manager.GlobeManager;
import com.example.routingSimulator.modules.network.Graph.Algo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PingCommand {

    private static final double LATENCY_SCALE = 100.0;
    private final GlobeManager globeManager;

    public PingCommand(GlobeManager globeManager) {
        this.globeManager = globeManager;
    }

    public List<Double> pingForApi(int srcModelId, int destModelId, String destIp, int count) {
        int nodeCount = globeManager.grid.getAdjList().size();

        if (nodeCount == 0 ||
                srcModelId < 0 || srcModelId >= nodeCount ||
                destModelId < 0 || destModelId >= nodeCount) {
            System.out.println("⚠️ Invalid source/destination ID or empty network.");
            return List.of();
        }

        Algo algo = new Algo(globeManager.grid);
        double[] dist = algo.dijkstra(srcModelId, destModelId);

        if (Double.isInfinite(dist[destModelId])) {
            System.out.println("⚠️ No valid route found.");
            return List.of();
        }

        double baseRttMs = computeRttFromCost(dist[destModelId]);
        List<Double> times = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            times.add(addJitter(baseRttMs));
        }
        return times;
    }

    private double computeRttFromCost(double cost) {
        double rtt = 2.0 * cost * LATENCY_SCALE;
        return Math.max(1.0, rtt);
    }

    private double addJitter(double baseRtt) {
        double jitterRange = baseRtt * 0.15;
        double delta = ThreadLocalRandom.current().nextDouble(-jitterRange, jitterRange);
        return Math.max(1.0, baseRtt + delta);
    }
}
