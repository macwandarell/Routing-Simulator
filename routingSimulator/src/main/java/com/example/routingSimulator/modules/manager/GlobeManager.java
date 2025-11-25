package com.example.routingSimulator.modules.manager;
import com.example.routingSimulator.modules.manager.Manager;
import com.example.routingSimulator.modules.models.Model;
import com.example.routingSimulator.modules.network.Graph.Algo;
import com.example.routingSimulator.modules.network.Graph.Network;
import com.example.routingSimulator.modules.network.Link.Link;
import com.example.routingSimulator.modules.network.ip.Ipv4;
import com.example.routingSimulator.modules.network.ip.PrivateIpv4Generator;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import static guru.nidi.graphviz.model.Factory.*;
import java.util.ArrayList;
import java.util.List;

public class GlobeManager{
    
    
    public Network grid;
    public Algo dijkstra;
    private PrivateIpv4Generator generator= new PrivateIpv4Generator();
    
    private String name;
    private ArrayList<Manager> managers;
    private ArrayList<Ipv4> publicIps;

    public GlobeManager(String name){

        grid=new Network();
        dijkstra=new Algo(grid);
        managers = new ArrayList<Manager>();
        publicIps=new ArrayList<Ipv4>();

        int first = 193;
        int second = 168;

        for (int third = 0; third <= 255; third++) {
            for (int fourth = 0; fourth <= 255; fourth++) {
                String ip = first + "." + second + "." + third + "." + fourth;
                Ipv4 ipadd= new Ipv4(ip);
                publicIps.add(ipadd);
            }
        }
        this.name = name;
    }
    public void addManager(Manager manager){
        manager.setPublicIp(assignIp());
        managers.add(manager);
        System.out.println("Successfully added: "+ manager.viewDetails());
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    private String assignIp(){
        try{
            if(publicIps.size()==0) {
                throw new ArrayIndexOutOfBoundsException("All ips given to this dhcp have been assigned");
            }
            Ipv4 ip= publicIps.remove(publicIps.size()-1);
            String assignip=ip.getIpString();
            return assignip;

        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Failed to assign ip" + e.getMessage());}
        return null;
    }
    public void giveManagerDhcpIp(String id,int no){
        for(Manager manager:managers){
            if(manager.viewDetails().equals(id)){
                List<String> ips = generator.generateIps(no);
                for(String ip : ips){
                    manager.addIpToDhcp(ip);
                }
                break;
            }
        }
    }
    public boolean publicIpExists(String ipv4){
        for(Manager manager:managers){
            if(manager.getPublicIp().equals(ipv4)){
                return true;
            }
        }
        return false;
    }

    /**
     * Find a Model by IPv4 address across all managers. Returns null if none found.
     */
    public Model findModelByIp(String ip) {
        for (Manager m : managers) {
            Model found = m.findEntityByIp(ip);
            if (found != null) return found;
        }
        return null;
    }
    public Manager findManagerById(String id){
        for(Manager m: managers){
            if(m.viewDetails().equals(id)){
                return m;
            }
        }
        return null;
    }
    public String printView()
    {
        MutableGraph g=mutGraph("network").setDirected(false);

        for(Manager manager:managers)
        {
            ArrayList<Model> entities=manager.getAllEntities();
            for(Model model:entities)
            {
                String modelid=Integer.toString(model.getModelID());
                g.add(mutNode(modelid));
            }
        }

        ArrayList<ArrayList<Link>> adjList=grid.getAdjList();
        for(ArrayList<Link> a:adjList)
        {
            for(Link e:a)
            {
                String node1=Integer.toString(e.getFirst().getModelID());
                String node2=Integer.toString(e.getSecond().getModelID());
                g.add(mutNode(node1).addLink(mutNode(node2)));

            }
        }

        return Graphviz.fromGraph(g).render(Format.SVG).toString();
        
    }
}
