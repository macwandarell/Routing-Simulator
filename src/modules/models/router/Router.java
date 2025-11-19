package modules.models.router;
import modules.network.ip.Ipv4;
import modules.models.Model;
public class Router extends Model {
    private Ipv4 ipv4;
    private String id;
    public Router(String ipv4,String id){
        super(id);
        try{
        this.ipv4= new Ipv4(ipv4);}
        catch (IllegalArgumentException e){
            System.out.println("Failed to make a router:"+e.getMessage());
        }
        this.id=id;
    }
    public Router(String id){
    super(id);
    this.id=id;}
    public String getId(){
        return this.id;}
    public String getIpv4(){
        return this.ipv4.getIpString();
    }
    public void setId(String id){
        this.id=id;
    }
    @Override
    public void setIpv4(String ipv4){
        try{
            this.ipv4= new Ipv4(ipv4);}
        catch (IllegalArgumentException e){
            System.out.println("Failed to make a router:"+e.getMessage());
        }
    }

    @Override
    public String getType(){
        return "Router";
    }
}