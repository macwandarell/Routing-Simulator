package modules.models.publicServer;
import modules.network.ip.Ipv4;
import modules.models.Model;

public class PublicServr extends Model{
    private Ipv4 ipv4;
    private String id;
    private String domainName;
    public PublicServer(String ipv4,String id){
        super();
        try{
            this.ipv4= new Ipv4(ipv4);}
        catch (IllegalArgumentException e){
            System.out.println("Failed to make a router:"+e.getMessage());
        }
        this.id=id;
    }
    public PublicServr(String id){
        super();
        this.id=id;
    }
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
    public void setDomainName(String domainName){
        this.domainName=domainName;
    }
    public String getDomainName(){
        return this.domainName;
    }
}