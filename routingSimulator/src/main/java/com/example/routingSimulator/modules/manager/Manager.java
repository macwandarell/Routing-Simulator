package com.example.routingSimulator.modules.manager;
import com.example.routingSimulator.modules.models.Model;
import java.util.ArrayList;

import com.example.routingSimulator.modules.models.device.Device;
import com.example.routingSimulator.modules.models.dhcp.Dhcp;
import com.example.routingSimulator.modules.models.publicServer.PublicServer;
import com.example.routingSimulator.modules.models.router.Router;


public class Manager{
    private String managerID;
    private ArrayList<Model> entities = new ArrayList<Model>();
    private Dhcp dhcpServer;
    private PublicServer publicServer;
    private String publicIp;
    public Manager(String managerID){
        this.managerID=managerID;
    }
    public ArrayList<Model> getAllEntities()
    {
        ArrayList<Model> allEntities = new ArrayList<>(entities);
        if (publicServer != null) {
            allEntities.add(publicServer);
        }
        return allEntities;
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
    public String getPublicServerId(){if(publicServer!=null){return publicServer.getId();} return null;}
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
    public Model findEntityByIp(String ip) {
        for (Model entity : entities) {
            try {
                if (entity instanceof Device) {
                    Device d = (Device) entity;
                    if (ip.equals(d.getIpv4())) return d;
                } else if (entity instanceof Router) {
                    Router r = (Router) entity;
                    if (ip.equals(r.getIpv4())) return r;
                } else if (entity instanceof PublicServer) {
                    PublicServer p = (PublicServer) entity;
                    if (ip.equals(p.getIpv4())) return p;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }
    public Model findModelById(String id){
        for(Model entity: entities){
            if(model.getModelID().equals(id)){
                return entity;
            }
        }
        return null;
    }
}
