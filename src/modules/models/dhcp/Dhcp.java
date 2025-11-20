package modules.models.dhcp;
import modules.network.ip.Ipv4;
import modules.models.Model;
import java.util.ArrayList;
public class Dhcp extends Model{
    private String id;
    private Ipv4 ipv4;
    private ArrayList<Ipv4> ip_list = new ArrayList<Ipv4>();
    public Dhcp(String ipv4, String id){
        super();
        try{
            this.ipv4= new Ipv4(ipv4);}
        catch (IllegalArgumentException e){
            System.out.println("Failed to make a dhcp server: "+e.getMessage());
        }
        this.id=id;
    }
    public String getId(){
        return this.id;
    }
    public String getIpv4(){
        return this.ipv4.getIpString();
    }
    public void setId(String id){
        this.id=id;
    }
    public void addIpToList(String ipv4) {
        try {
            Ipv4 ipv4New = new Ipv4(ipv4);
            ip_list.add(ipv4New);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to add ip to dhcp: " + e.getMessage());
        }
    }
    public String assignIp(){
        try{
            if(ip_list.size()==0) {
                throw new ArrayIndexOutOfBoundsException("All ips given to this dhcp have been assigned");
            }
            Ipv4 ip= ip_list.remove(ip_list.size()-1);
            String assignip=ip.getIpString();
            return assignip;

    }
    catch (ArrayIndexOutOfBoundsException e){
        System.out.println("Failed to assign ip" + e.getMessage());}
        return null;
    }
    @Override
    public String getType(){
        return "DHCP";
    }
}