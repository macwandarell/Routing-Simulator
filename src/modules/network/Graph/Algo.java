package modules.network.Graph;

import modules.models.Model;
import modules.network.Link.Link;

import java.util.ArrayList;

public class Algo {
    private int adjSize;
    private ArrayList<ArrayList<Link>> adjList;
    public Algo(Network grid)
    {
        adjSize=grid.getAdjSize();
        adjList=grid.getAdjList();
    }

    public double getWeight(int bandwidth)
    {
        return Math.pow(bandwidth,-1);
    }

    public int findShortestPath(Model u, Model v)
    {

    }
}
