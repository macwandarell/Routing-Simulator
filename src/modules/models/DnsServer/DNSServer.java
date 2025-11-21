package modules.models.DnsServer;

import modules.models.Model;
import modules.network.ip.Ipv4;
import java.util.HashMap;

public class DNSServer extends Model {
    HashMap<String,String> dnsRecords; // Maps domain names to IP addresses
    Ipv4 ipv4;

    // Constructor
    public DNSServer(String ipv4){
        super();
        this.ipv4 = new Ipv4(ipv4);
        dnsRecords=new HashMap<String,String>();
    }

    private String normalize(String domain) {
        return domain == null ? "" : domain.trim().toLowerCase();
    }

    // Returns true if domain is valid (not already taken)
    public boolean validateDomain(String domainName){
        return !dnsRecords.containsKey(domainName);
    }

    // Adds a DNS record if the domain name is valid
    public void addDnsRecord(String domainName, String ipAddress){
        String cleanDomain = normalize(domainName);

        if(validateDomain(cleanDomain)) {
            dnsRecords.put(cleanDomain, ipAddress);
        }else{
            throw new IllegalArgumentException("Domain name already taken: " + domainName);
        }
    }

    public boolean removeDnsRecord(String domainName) {
        String cleanDomain = normalize(domainName);

        if (dnsRecords.containsKey(cleanDomain)) {
            dnsRecords.remove(cleanDomain);
            return true;
        }
        return false; // Return false if nothing was deleted
    }


    // This prints the classic Windows/Linux DNS output style
    public void performNsLookup(String domainName) {
        String cleanDomain = normalize(domainName);
        String ip = dnsRecords.get(cleanDomain);

        System.out.println("Server:  RoutingSimulator-DNS");
        System.out.println("Address: 127.0.0.1 (Simulated)");
        System.out.println();

        if (ip != null) {
            System.out.println("Name:    " + cleanDomain);
            System.out.println("Address: " + ip);
        } else {
            System.out.println("*** UnKnown can't find " + domainName + ": Non-existent domain");
        }
    }

    // Retrieves the IP address for a given domain name
    public Ipv4 getIpForDomain(String domainName) {
        String cleanDomain = normalize(domainName);
        String ipAddress = dnsRecords.get(cleanDomain);

        if (ipAddress == null) {
            throw new IllegalArgumentException("Ping Error: Host not found [" + domainName + "]");
        }
        return new Ipv4(ipAddress);
    }
}