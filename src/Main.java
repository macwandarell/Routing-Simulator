import modules.models.router.Router;
import modules.manager.Manager;
import modules.manager.GlobeManager;
import modules.models.dhcp.Dhcp;
public class Main{
    public static void main(String[] args){
        Router router = new Router("10.100.0.1","R001");
        GlobeManager globalmanager= new GlobeManager();
        Manager manager1= new Manager("ID1");
        globalmanager.addManager(manager1);
        manager1.addEntity(router);
        Dhcp dhcp1 = new Dhcp("192.0.0.1","D001");
        manager1.addEntity(dhcp1);

    }

}