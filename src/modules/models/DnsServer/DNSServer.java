package modules.models.DnsServer;

import modules.models.Model;
import modules.network.ip.Ipv4;
import java.util.HashMap;
import java.io.*;

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
        return !dnsRecords.containsKey(normalize(domainName));
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
    public String performNsLookup(String domainName) {
        String cleanDomain = normalize(domainName);
        String ip = dnsRecords.get(cleanDomain);

        if (ip != null) {
            return ip;
        } else {
            return null;
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

    private void saveRecords()
    {
        String filePath = "dns_records.txt";
        // The "try(...)" syntax automatically closes the file when done (very important!)
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath)))
        {
            for (HashMap.Entry<String, String> entry : dnsRecords.entrySet())
            {
                String Domain = entry.getKey();
                String IpAddress = entry.getValue();

                String line = Domain + "," + IpAddress;

                writer.write(line);
                writer.newLine():
            }
        }


    }
}