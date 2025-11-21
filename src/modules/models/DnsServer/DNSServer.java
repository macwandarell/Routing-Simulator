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

    // Returns true if domain is valid (not already taken)
    public boolean validateDomain(String domainName){
        return !dnsRecords.containsKey(domainName);
    }

    // Adds a DNS record if the domain name is valid
    public void addDnsRecord(String domainName, String ipAddress){
        if(validateDomain(domainName)) {
            dnsRecords.put(domainName, ipAddress);
        }else{
            throw new IllegalArgumentException("Domain name already taken: " + domainName);
        }
    }

    // Retrieves the IP address for a given domain name
    public Ipv4 getIpForDomain(String domainName){
        String ipAddress=dnsRecords.get(domainName);
        if(ipAddress==null){
            throw new IllegalArgumentException("Domain name not found: " + domainName);
        }
        return new Ipv4(ipAddress);
    }
}