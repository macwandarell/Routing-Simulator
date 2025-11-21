package modules.manager;
import modules.manager.Manager;
import modules.network.Graph.Algo;
import modules.network.Graph.Network;

import java.util.ArrayList;

public class GlobeManager{
    private ArrayList<Manager> managers = new ArrayList<Manager>();
    public GlobeManager(){}
    public Network grid=new Network();
    public Algo dijkstra=new Algo(grid);

    public void addManager(Manager manager){
        managers.add(manager);
        System.out.println("Successfully added: "+ manager.viewDetails());
    }
}
