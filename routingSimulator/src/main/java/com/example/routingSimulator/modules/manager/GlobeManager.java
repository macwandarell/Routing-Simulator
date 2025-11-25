package com.example.routingSimulator.modules.manager;
import com.example.routingSimulator.modules.manager.Manager;
import com.example.routingSimulator.modules.models.Model;
import com.example.routingSimulator.modules.network.Graph.Algo;
import com.example.routingSimulator.modules.network.Graph.Network;
import com.example.routingSimulator.modules.network.ip.Ipv4;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import static guru.nidi.graphviz.model.Factory.*;
import java.util.ArrayList;

public class GlobeManager{
    private String name;
    private ArrayList<Manager> managers = new ArrayList<Manager>();
    private ArrayList<Ipv4> publicIps=new ArrayList<Ipv4>();
    public GlobeManager(String name){
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
    public Network grid=new Network();
    public Algo dijkstra=new Algo(grid);
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

    public String printView(){
        MutableGraph g = mutGraph("network").setDirected(false);

        for (Manager m : managers) {
            g.add(mutNode(m.getId()));
        }
        for (Link link : grid.getLinks()) {
            String a = link.getNodeA().getId();
            String b = link.getNodeB().getId();
            g.add(mutNode(a).addLink(mutNode(b)));
        }
        return Graphviz.fromGraph(g).render(Format.SVG).toString();
    }
}
