package modules.commands;

import modules.manager.GlobeManager;
import modules.network.Graph.Algo;
import java.util.concurrent.ThreadLocalRandom;



public class PingCommand {

    private static final double LATENCY_SCALE = 100.0;
    private final GlobeManager globeManager;

    public PingCommand(GlobeManager globeManager) {
        this.globeManager = globeManager;
    }

    public void ping(int srcModelId, int destModelId, String destIp, int count) {
        if (globeManager.publicIpExists(destIp)) {
            pingPublic(srcModelId, destModelId, destIp, count);
        } else {
            pingInternal(srcModelId, destModelId, destIp, count);
        }
    }

    private void pingPublic(int srcModelId, int destModelId, String destIp, int count) {
        if (!globeManager.publicIpExists(destIp)) {
            System.out.println("Destination host unreachable.");
            return;
        }
        Algo algo = new Algo(globeManager.grid);
        double[] dist = algo.dijkstra(srcModelId, destModelId);
        if (destModelId < 0 || destModelId >= dist.length || Double.isInfinite(dist[destModelId])) {
            System.out.println("No route to host.");
            return;
        }
        double baseRttMs = computeRttFromCost(dist[destModelId]);
        printPingOutput(destIp, baseRttMs, count);
    }

    private void pingInternal(int srcModelId, int destModelId, String destIp, int count) {
        Algo algo = new Algo(globeManager.grid);
        double[] dist = algo.dijkstra(srcModelId, destModelId);
        if (destModelId < 0 || destModelId >= dist.length || Double.isInfinite(dist[destModelId])) {
            System.out.println("No route to host.");
            return;
        }
        double baseRttMs = computeRttFromCost(dist[destModelId]);
        printPingOutput(destIp, baseRttMs, count);
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

    private void printPingOutput(String destIp, double baseRtt, int count) {
        System.out.println("Pinging " + destIp + " with 32 bytes of data:");
        for (int i = 0; i < count; i++) {
            double rtt = addJitter(baseRtt);
            System.out.printf("Reply from %s: time=%.2fms%n", destIp, rtt);
        }
    }

}


