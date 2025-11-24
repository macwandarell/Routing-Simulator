package com.example.routingSimulator.modules.network.Graph;

import com.example.routingSimulator.modules.models.Model;
import com.example.routingSimulator.modules.network.Link.Link;
import com.example.routingSimulator.modules.network.Link.LinkSpeedType;

import java.util.*;


// everywhere in this class the bandwidth will be used in Kbps only(shortest unit)


public class Algo {
    private int adjSize;

    //stores the adjaceny list for the graph(network)
    private ArrayList<ArrayList<Link>> adjList;

    //to re-construct the path
    private int[] lastPrev;
    private double[] lastDist;

    public Algo(Network grid)
    {
        adjList=grid.getAdjList();
    }

    // get the cost from bandwidth -> higher the bandwidth faster the speed lesser the cost
    public double getCostfromBandwidth(double bandwidth)
    {
        return 1.0/bandwidth;
    }

    // used to list down linkbandwidth in re-construction
    private double getLinkBandwidth(int a,int b)
    {
        for(Link e:adjList.get(a))
        {
            if(e.getSecond().getModelID()==b)
            {
                return e.getSpeed(LinkSpeedType.KBPS);
            }
        }
        return -1;
    }

    // main logic for dijkstra construct a list of dist which stores the shortest length from src to every other node or you can say target
    public double[] dijkstra(int src,int target)
    {
        this.adjSize=this.adjList.size();//dynamically get the size of adj list
        double[] dist=new double[adjSize];
        int[] prev =new int[adjSize];
        boolean[] vis =new boolean[adjSize];

        Arrays.fill(dist,Double.POSITIVE_INFINITY);// FILL THE DIST WITH POS INF (IF NOT REACHABEL)
        Arrays.fill(prev,-1); //Safety case

        dist[src]=0.0;

        PriorityQueue<double[]>pq=  new PriorityQueue<>(Comparator.comparingDouble(a->a[0]));
        // pq is stored {weight,v} and it is comparing only the a[0] the first index to sort

        pq.add(new double[]{0.0,src});

        while(!pq.isEmpty())
        {
            double[] t=pq.poll();
            int u=(int)t[1];
            if(vis[u]) continue;
            vis[u]=true;

            if(u==target) break; // early exiting the loop once we reach the target

            for(Link e:adjList.get(u))// loop over all neighbours of u to get the min distance
            {
                int v=e.getSecond().getModelID();
                double cost=getCostfromBandwidth(e.getSpeed(LinkSpeedType.KBPS));

                double nd=dist[u]+cost;
                if(nd<dist[v])
                {
                    dist[v]=nd;
                    prev[v]=u;
                    pq.add(new double[]{nd,v});
                }
            }
        }
        this.lastPrev=prev;// used in re-construction
        this.lastDist=dist;// same as above
        return dist;// returns the list we formed using dijkstra for re-construction of the path
    }


    // gives the shortest path from Model u to Model v including all the inernal nodes with their respective Linkbandwidth
    public List<double[]> findShortestPath(Model u, Model v)
    {


        int src=u.getModelID();
        int target=v.getModelID();

        // throw exception for modelid is correct or not

        dijkstra(src,target); // creates the dist array and store in lastDist

        if(lastDist==null || Double.isInfinite(lastDist[target]))
        {
            return new ArrayList<>();// unreachable then return null
        }

        //reconstruct path
        ArrayList<Integer> nodes=new ArrayList<>();// array containing only internal nodes
        for(int at=target;at!=-1;at=lastPrev[at])
        {
            nodes.add(at);// backtracking type for path
        }

        Collections.reverse(nodes); // reverse the path as we go from target to src

        List<double[]> path=new ArrayList<>();// array containing both internal node and its Linkbandwidth
        for(int i=0;i+1<nodes.size();i++)
        {
            int a=nodes.get(i);
            int b=nodes.get(i+1);
            double w=getLinkBandwidth(a,b);
            path.add(new double[]{b,w});
        }
        return path;
    }

}
