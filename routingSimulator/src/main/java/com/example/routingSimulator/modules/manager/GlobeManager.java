package com.example.routingSimulator.modules.manager;
import com.example.routingSimulator.modules.manager.Manager;
import com.example.routingSimulator.modules.models.Model;
import com.example.routingSimulator.modules.network.Graph.Algo;
import com.example.routingSimulator.modules.network.Graph.Network;
import com.example.routingSimulator.modules.network.Link.Link;
import com.example.routingSimulator.modules.network.ip.Ipv4;
import com.example.routingSimulator.modules.network.ip.PrivateIpv4Generator;
import com.example.routingSimulator.modules.models.DnsServer.DNSServer;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.model.Node;
import guru.nidi.graphviz.model.MutableNode;
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
    private DNSServer dnsServer;
    
    private String name;
    private ArrayList<Manager> managers;
    private ArrayList<Ipv4> publicIps;


    public Algo getDijkstra()
    {
        return dijkstra;
    }
    public ArrayList<Manager> getAllManager()
    {
        return managers;
    }
    public Network getNetwork()
    {
        return grid;
    }
    public ArrayList<Model> getAllDevicesofManager(String id)
    {
        for(Manager m:managers)
        {
            if(m.viewDetails().equals(id))
            {
                return m.getAllEntities();
            }
        }
        return null;
    }

    public GlobeManager(String name){

        grid=new Network();
        dijkstra=new Algo(grid);
        managers = new ArrayList<Manager>();
        publicIps=new ArrayList<Ipv4>();
        dnsServer=new DNSServer("192.168.1.1");

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
    public DNSServer getDnsServer() {
        return dnsServer;
    }
    public Model findModelByID(String Id){
        for(Manager m : managers){
            Model model = m.findModelById(Id);
            if(model != null) return model;
        }
        return null;
    }
    private MutableNode createNodeForModel(Model model) {
        String id = String.valueOf(model.getModelID());
        String type = model.getClass().getSimpleName();

        Shape shape;
        Color color;

        switch (type) {
            case "Router":
                shape = Shape.BOX;
                color = Color.BLUE;
                break;

            case "Dhcp":
                shape = Shape.ELLIPSE;
                color = Color.ORANGE;
                break;

            case "Device":
                shape = Shape.CIRCLE;
                color = Color.GREEN;
                break;

            case "PublicServer":
                shape = Shape.DIAMOND;
                color = Color.RED;
                break;

            default:
                shape = Shape.OVAL;
                color = Color.GRAY;
        }

        return mutNode(id)
                .add(Label.of(type + "\nID: " + id))
                .add(shape)
                .add(color);
    }
    public String printView() {
        MutableGraph g = mutGraph("network").setDirected(false);

        // Add all entity nodes (pretty version)
        for (Manager manager : managers) {
            ArrayList<Model> entities = manager.getAllEntities();
            for (Model model : entities) {
                if (model != null) {
                    g.add(createNodeForModel(model));
                }
            }
        }

        // Add edges (connections in the adjacency list)
        ArrayList<ArrayList<Link>> adjList = grid.getAdjList();
        for (ArrayList<Link> a : adjList) {
            for (Link e : a) {
                Model first = e.getFirst();
                Model second = e.getSecond();

                if (first != null && second != null) {
                    String id1 = String.valueOf(first.getModelID());
                    String id2 = String.valueOf(second.getModelID());

                    g.add(mutNode(id1).addLink(mutNode(id2)));
                }
            }
        }

        return Graphviz.fromGraph(g).render(Format.SVG).toString();
    }
}
