package modules.models.router;
import modules.network.ip.Ipv4;
public class Router {
    private Ipv4 ipv4;
    private String id;
    public Router(String ipv4,String id){
        try{
        this.ipv4= new Ipv4(ipv4);}
        catch (IllegalArgumentException e){
            System.out.println("Failed to make a router:"+e.getMessage());
        }
        this.id=id;
    }
    public String getId(){
        return this.id;
    }
    public String getIpv4(){
        return this.ipv4.getIpString();
    }
    public String setId(String id){
        this.id=id;
    }
}