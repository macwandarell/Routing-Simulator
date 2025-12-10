package com.example.routingSimulator.modules.models.DnsServer;

import com.example.routingSimulator.modules.manager.GlobeManager;
import com.example.routingSimulator.modules.models.Model;
import com.example.routingSimulator.modules.models.publicServer.PublicServer;
import com.example.routingSimulator.modules.network.ip.Ipv4;
import java.util.HashMap;
import java.io.*;


public class DNSServer extends Model
{
    // I am using a HashMap here because looking up a Key (Domain) to get a Value (IP) is very fast
    HashMap<String,String> dnsRecords;

    // I need a reference to the GlobeManager so I can check if an IP exists in the simulation before adding it
    private final GlobeManager globeManager;

    Ipv4 ipv4;
    private String id;

    // Constructors
    public DNSServer(String ipv4,String id,GlobeManager globeManager)
    {
        super();
        this.ipv4 = new Ipv4(ipv4);
        dnsRecords=new HashMap<String,String>();
        this.id=id;
        this.globeManager = globeManager;

        // I want to load the records from the file as soon as the server starts up
        loadRecords();
    }

    //constructor overloading
    // I made this second constructor just in case I don't want to pass an ID manually
    public DNSServer(String ipv4, GlobeManager globeManager)
    {
        super();
        this.ipv4 = new Ipv4(ipv4);
        dnsRecords=new HashMap<String,String>();
        loadRecords();
        this.globeManager = globeManager;
    }


    //getters
    public String getServerIP()
    {
        return this.ipv4.getIpString();
    }

    public HashMap<String, String> getDnsRecords()
    {
        return dnsRecords;
    }


    // This method handles reading the "dns_records.txt" file and putting the data into my HashMap
    private void loadRecords()
    {
        String filePath = "dns_records.txt";
        File file = new File(filePath);

        // Check if file exists first
        if (!file.exists())
        {
            System.out.println("No DNS record file found. Starting with empty database.");
            return; //stopping here since file doesn't exist
        }

        // Chaining: I wrap 'FileReader' inside 'BufferedReader'.
        // The benefit is this makes it faster and gives me the '.readLine()' method.
        // The try(...) block ensures the file closes automatically when I'm done.
        try (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            String line;
            // Reading line by line until the file ends (returns null)
            while ((line = reader.readLine()) != null)
            {

                // Example line: "google.com,192.168.1.1"
                String[] parts = line.split(",");

                // Make sure the line is valid (has 2 parts) before using it
                if (parts.length == 2) {
                    String domain = parts[0];
                    String ip = parts[1];

                    // Putting it back into memory
                    dnsRecords.put(domain, ip);
                }
            }
        } catch (IOException e)
        {
            System.out.println("Error loading DNS records: " + e.getMessage());
        }
    }
    public String getId()
    {
        return this.id;

    }

    // This saves my current HashMap data back to the text file so I don't lose it
    private void saveRecords()
    {
        String filePath = "dns_records.txt";

        // Using BufferedWriter because it's more efficient for writing text
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath)))
        {
            //We iterate through every Key-Value pair in the HashMap.
            for (HashMap.Entry<String, String> entry : dnsRecords.entrySet())
            {
                String Domain = entry.getKey();
                String IpAddress = entry.getValue();

                //Formatting the String
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

    //convert all domains to lowercase and check
    //Google.com is equivalent to google.com
    private String normalize(String domain)
    {
        return domain == null ? "" : domain.trim().toLowerCase();
    }

    // Returns true if domain is valid (not already taken)
    public boolean validateDomain(String domainName)
    {
        return !dnsRecords.containsKey(normalize(domainName));
    }


    // This adds a new DNS record, but only if the domain is valid and the IP exists
    public void addDnsRecord(String domainName, String ipAddress)
    {
        String cleanDomain = normalize(domainName);

        if(validateDomain(cleanDomain))
        {
            // using the globeManager to find the actual object associated with this IP
            Model model = globeManager.findModelByIp(ipAddress);

            //implemented this to ensure domains map only to valid End-Servers, not to Routers or non-existent IPs.
            if(model == null || model.getType().compareTo("PublicServer") != 0){
                throw new IllegalArgumentException("PublicServer doesnt exist " + domainName);
            }

            PublicServer temp = (PublicServer) model;

            temp.setDomainName(domainName);
            dnsRecords.put(cleanDomain, ipAddress);

            // Saving to the file immediately
            saveRecords();

        }else
        {
            throw new IllegalArgumentException("Domain name already taken: " + domainName);
        }
    }

    public boolean removeDnsRecord(String domainName)
    {
        String cleanDomain = normalize(domainName);

        if (dnsRecords.containsKey(cleanDomain))
        {
            dnsRecords.remove(cleanDomain);

            // Updating the file
            saveRecords();

            return true;
        }
        return false; // Return false if nothing was deleted
    }

    // This acts like the standard nslookup command
    // If I give it a domain, it gives me the IP string
    public String performNsLookup(String domainName)
    {
        String cleanDomain = normalize(domainName);
        String ip = dnsRecords.get(cleanDomain);

        if (ip != null)
        {
            return ip;
        } else {
            return null;
        }
    }

    // This is similar to lookup, but it returns my Ipv4 object
    // I use this when I need the actual object for network operations, not just the string
    public Ipv4 getIpForDomain(String domainName)
    {
        String cleanDomain = normalize(domainName);
        String ipAddress = dnsRecords.get(cleanDomain);

        if (ipAddress == null)
        {
            throw new IllegalArgumentException("Ping Error: Host not found [" + domainName + "]");
        }
        return new Ipv4(ipAddress);
    }
}
