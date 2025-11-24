package com.example.routingSimulator.modules.network.Graph;

import com.example.routingSimulator.modules.models.Model;
import com.example.routingSimulator.modules.network.Link.Link;
import com.example.routingSimulator.modules.network.Link.LinkSpeedType;

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
        node.setModelID(adjList.size());
        adjList.add(new ArrayList<>());
    }

    public ArrayList<ArrayList<Link>> getAdjList() {
        return adjList;
    }

//    public void setAdjList(ArrayList<ArrayList<Link>> adjList) {
//        this.adjList = adjList;
//    }

    public int getAdjSize()
    {
        return adjList.size();
    }

    public void addEdge(Link edge)
    {
        int u=edge.getFirst().getModelID();
        int v=edge.getSecond().getModelID();
        // as this is an undirected graph we need to add both edges
        Link reverseEdge=new Link(edge.getSecond(),edge.getFirst(),edge.getSpeed(LinkSpeedType.KBPS),"Kbps");
        adjList.get(u).add(edge);
        adjList.get(v).add(reverseEdge);
    }

    // add all the methods mentioned in SRS


}
