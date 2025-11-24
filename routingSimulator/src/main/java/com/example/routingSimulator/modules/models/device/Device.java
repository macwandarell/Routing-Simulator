package modules.models.device;

import modules.models.Model;
import modules.network.ip.Ipv4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Device extends Model {

    private Ipv4 ipv4;
    private String id;
    private final List<Integer> activePorts;

    public Device(String ipv4, String id) {
        super();
        this.activePorts = new ArrayList<>();
        try {
            this.ipv4 = new Ipv4(ipv4);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to create device: " + e.getMessage());
        }
        this.id = id;
    }

    public Device(String id) {
        super();
        this.id = id;
        this.activePorts = new ArrayList<>();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIpv4() {
        return (ipv4 != null) ? ipv4.getIpString() : null;
    }

    @Override
    public void setIpv4(String ipv4) {
        try {
            this.ipv4 = new Ipv4(ipv4);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to set IPv4 for device: " + e.getMessage());
        }
    }

    public void addPort(int port) {
        if (port < 0 || port > 65535) {
            System.out.println("Invalid port: " + port);
            return;
        }
        if (!activePorts.contains(port)) {
            activePorts.add(port);
        } else {
            System.out.println("Port " + port + " is already active.");
        }
    }

    public void setPort(int port) {
        addPort(port);
    }

    public List<Integer> getActivePorts() {
        return Collections.unmodifiableList(activePorts);
    }

    public boolean removePort(int port) {
        return activePorts.remove(Integer.valueOf(port));
    }

    @Override
    public String getType() {
        return "Device";
    }
}
