package com.example.routingSimulator.modules.manager;
import com.example.routingSimulator.modules.models.Model;
import java.util.ArrayList;
import com.example.routingSimulator.modules.models.dhcp.Dhcp;
import com.example.routingSimulator.modules.models.publicServer.PublicServer;


public class Manager{
    private String managerID;
    private ArrayList<Model> entities = new ArrayList<Model>();
    private Dhcp dhcpServer;
    private PublicServer publicServer;
    private String publicIp;
    public Manager(String managerID){
        this.managerID=managerID;
    }
    public void addEntity(Model entity){
        if(entity.getType().equals("DHCP")){
            dhcpServer = (Dhcp)entity;
            return;
        }
        if(entity.getType().equals("PublicServer")){
            publicServer = (PublicServer) entity;
            publicServer.setIpv4(publicIp);
            return;
        }
        String ipv4= dhcpServer.assignIp();
        if(ipv4==null){
            System.out.println("Failed to assign any ip.");
            return;
        }
        entity.setIpv4(ipv4);
        entities.add(entity);
        System.out.println("Successfully added: "+ entity.viewDetails());
    }
    public void setPublicIp(String ipv4){this.publicIp=ipv4;}
    public String viewDetails(){
       return this.managerID;
    }
    public void addIpToDhcp(String ipv4){
        dhcpServer.addIpToList(ipv4);
    }
    public String getPublicIp(){return this.publicIp;}

    /**
     * Find an entity (Model) managed by this Manager that has the given IPv4 string.
     * Returns the Model if found, otherwise null.
     */
    public modules.models.Model findEntityByIp(String ip) {
        for (modules.models.Model entity : entities) {
            try {
                if (entity instanceof modules.models.device.Device) {
                    modules.models.device.Device d = (modules.models.device.Device) entity;
                    if (ip.equals(d.getIpv4())) return d;
                } else if (entity instanceof modules.models.router.Router) {
                    modules.models.router.Router r = (modules.models.router.Router) entity;
                    if (ip.equals(r.getIpv4())) return r;
                } else if (entity instanceof modules.models.publicServer.PublicServer) {
                    modules.models.publicServer.PublicServer p = (modules.models.publicServer.PublicServer) entity;
                    if (ip.equals(p.getIpv4())) return p;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
