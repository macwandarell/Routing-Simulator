import modules.models.device.Device;
import modules.manager.Manager;
import modules.manager.GlobeManager;
import modules.models.dhcp.Dhcp;
import java.util.Scanner;
import java.io.FileReader;

public class Main2{
    public static void main(String[] args) throws Exception {

        Device device = new Device("192.168.0.10", "DEV001");

        GlobeManager globalManager = new GlobeManager();
        Manager manager1 = new Manager("MID01");

        globalManager.addManager(manager1);

        Dhcp dhcp1 = new Dhcp("10.0.0.1", "D100");
        manager1.addEntity(dhcp1);

        Scanner in = new Scanner(new FileReader("data/dhcp_ip_list_1.txt"));
        while (in.hasNext()) {
            manager1.addIpToDhcp(in.next());
        }
        in.close();

        device.addPort(80);
        device.addPort(443);
        device.addPort(22);

        device.addPort(80);

        manager1.addEntity(device);

        System.out.println("Device ID: " + device.getId());
        System.out.println("Device IPv4: " + device.getIpv4());
        System.out.println("Active Ports: " + device.getActivePorts());
    }
}
