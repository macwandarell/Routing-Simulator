package modules.manager;
import modules.models.Model;
import java.util.ArrayList;
import modules.models.dhcp.Dhcp;
import modules.models.publicServer.PublicServer;


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
}
