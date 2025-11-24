package modules.models.DnsServer;

import modules.models.Model;
import modules.network.ip.Ipv4;
import java.util.HashMap;
import java.io.*;

public class DNSServer extends Model
{
    HashMap<String,String> dnsRecords; // Maps domain names to IP addresses
    Ipv4 ipv4;

    // Constructor
    public DNSServer(String ipv4)
    {
        super();
        this.ipv4 = new Ipv4(ipv4);
        dnsRecords=new HashMap<String,String>();
        loadRecords();
    }

    public HashMap<String, String> getDnsRecords() {
        return dnsRecords;
    }

    private void loadRecords() {
        String filePath = "dns_records.txt";
        File file = new File(filePath);

        // 1. Check if file exists first!
        if (!file.exists()) {
            System.out.println("No DNS record file found. Starting with empty database.");
            return; // Stop here, don't try to read
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Read line by line until the file ends (returns null)
            while ((line = reader.readLine()) != null) {

                // Example line: "google.com,192.168.1.1"
                String[] parts = line.split(",");

                // Make sure the line is valid (has 2 parts) before using it
                if (parts.length == 2) {
                    String domain = parts[0];
                    String ip = parts[1];

                    // Put it back into memory
                    dnsRecords.put(domain, ip);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading DNS records: " + e.getMessage());
        }
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
                writer.newLine();
            }
        }
        catch (IOException e)
        {
            System.out.println("Error saving the DNS Records: " + e.getMessage());
        }
    }

    private String normalize(String domain)
    {
        return domain == null ? "" : domain.trim().toLowerCase();
    }

    // Returns true if domain is valid (not already taken)
    public boolean validateDomain(String domainName)
    {
        return !dnsRecords.containsKey(normalize(domainName));
    }

    // Adds a DNS record if the domain name is valid
    public void addDnsRecord(String domainName, String ipAddress)
    {
        String cleanDomain = normalize(domainName);

        if(validateDomain(cleanDomain)) {
            dnsRecords.put(cleanDomain, ipAddress);
            saveRecords();
        }else{
            throw new IllegalArgumentException("Domain name already taken: " + domainName);
        }
    }

    public boolean removeDnsRecord(String domainName)
    {
        String cleanDomain = normalize(domainName);

        if (dnsRecords.containsKey(cleanDomain)) {
            dnsRecords.remove(cleanDomain);
            saveRecords();
            return true;
        }
        return false; // Return false if nothing was deleted
    }

    // This prints the classic Windows/Linux DNS output style
    public String performNsLookup(String domainName)
    {
        String cleanDomain = normalize(domainName);
        String ip = dnsRecords.get(cleanDomain);

        if (ip != null) {
            return ip;
        } else {
            return null;
        }
    }

    // Retrieves the IP address for a given domain name
    public Ipv4 getIpForDomain(String domainName)
    {
        String cleanDomain = normalize(domainName);
        String ipAddress = dnsRecords.get(cleanDomain);

        if (ipAddress == null) {
            throw new IllegalArgumentException("Ping Error: Host not found [" + domainName + "]");
        }
        return new Ipv4(ipAddress);
    }
}
