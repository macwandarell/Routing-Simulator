import modules.models.router.Router;
import modules.manager.Manager;
import modules.manager.GlobeManager;
import modules.models.dhcp.Dhcp;
import modules.models.publicServer.PublicServer;
import java.util.Scanner;
import java.io.FileReader;
public class Main{
    public static void main(String[] args) throws Exception{
        Router router = new Router("R001");
        GlobeManager globalmanager= new GlobeManager();
        Manager manager1= new Manager("ID1");
        globalmanager.addManager(manager1);
        Dhcp dhcp1 = new Dhcp("192.0.0.1","D001");
        manager1.addEntity(dhcp1);
        Scanner in = new Scanner(new FileReader("data/dhcp_ip_list_1.txt"));
        while(in.hasNext()) {
            manager1.addIpToDhcp(in.next());
        }
        in.close();
        Router router1 = new Router("R002");
        manager1.addEntity(router1);
        PublicServer publicServer1= new PublicServer("P001");
        manager1.addEntity(publicServer1);
        manager1.addEntity(router);

    }

}