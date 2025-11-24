package modules.manager;
import modules.manager.Manager;
import modules.network.Graph.Algo;
import modules.network.Graph.Network;
import modules.network.ip.Ipv4;
import java.util.ArrayList;

public class GlobeManager{
    private ArrayList<Manager> managers = new ArrayList<Manager>();
    private ArrayList<Ipv4> publicIps=new ArrayList<Ipv4>();
    public GlobeManager(){
        int first = 193;
        int second = 168;

        for (int third = 0; third <= 255; third++) {
            for (int fourth = 0; fourth <= 255; fourth++) {
                String ip = first + "." + second + "." + third + "." + fourth;
                Ipv4 ipadd= new Ipv4(ip);
                publicIps.add(ipadd);
            }
        }
    }
    public Network grid=new Network();
    public Algo dijkstra=new Algo(grid);
    public void addManager(Manager manager){
        manager.setPublicIp(assignIp());
        managers.add(manager);
        System.out.println("Successfully added: "+ manager.viewDetails());
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
}
