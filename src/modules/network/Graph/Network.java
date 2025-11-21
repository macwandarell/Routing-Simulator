package modules.network.Graph;

import modules.models.Model;
import modules.network.Link.Link;

import java.util.ArrayList;

public class Network {
    ArrayList<ArrayList<Link>> adjList;
    // stored as [  u->[{v,w1},{a,w2}]  ]

    public Network()
    {
        adjList=new ArrayList<>();
    }

    public void addNode(Model node)
    {
        adjList.add(new ArrayList<>());
    }

    public ArrayList<ArrayList<Link>> getAdjList() {
        return adjList;
    }

    public void setAdjList(ArrayList<ArrayList<Link>> adjList) {
        this.adjList = adjList;
    }

    public int getAdjSize()
    {
        return adjList.size();
    }

    public void addEgde(Link edge)
    {
        adjList.get(edge.getFirst().getModelID()).add(edge);
    }
}
