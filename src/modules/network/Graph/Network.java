package modules.network.Graph;

import java.util.ArrayList;

public class Network {
    ArrayList<ArrayList<int[]>> adjlist;
    // stored as [  u->[{v,w1},{a,w2}]  ]

    public void addnode(int modelid)
    {
        adjlist.add(new ArrayList<>());
    }

    public void addegde(int fromid,int toid,int weigth)
    {
        adjlist.get(fromid).add(new int[]{toid,weigth});
    }
}
