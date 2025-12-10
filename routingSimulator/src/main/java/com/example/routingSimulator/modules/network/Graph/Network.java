package com.example.routingSimulator.modules.network.Graph;

import com.example.routingSimulator.modules.models.Model;
import com.example.routingSimulator.modules.network.Link.Link;
import com.example.routingSimulator.modules.network.Link.LinkSpeedType;

import java.sql.SQLOutput;
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
//        System.out.println("Algo add edge");
        int u=edge.getFirst().getModelID();
//        System.out.println(u);
        int v=edge.getSecond().getModelID();
//        System.out.println(v);
        // as this is an undirected graph we need to add both edges
        Link reverseEdge=new Link(edge.getSecond(),edge.getFirst(),edge.getSpeed(LinkSpeedType.KBPS),"Kbps");
//        System.out.println("Algo rev edge");
        adjList.get(u).add(edge);
//        System.out.println("Algo add edge u");

        adjList.get(v).add(reverseEdge);
//        System.out.println("Algo add edge v");
    }

    public boolean removeEdge(int u,int v)
    {
        boolean removed=false;
        ArrayList<Link> edgesFromU=adjList.get(u);
        for(int i=0;i<edgesFromU.size();i++)
        {
            if(edgesFromU.get(i).getSecond().getModelID()==v)
            {
                edgesFromU.remove(i);
                removed=true;
                break;
            }
        }
        ArrayList<Link> edgesFromV=adjList.get(v);
        for(int i=0;i<edgesFromV.size();i++)
        {
            if(edgesFromV.get(i).getSecond().getModelID()==u)
            {
                edgesFromV.remove(i);
                removed=true;
                break;
            }
        }
        return removed;
    }

    // add all the methods mentioned in SRS


}
