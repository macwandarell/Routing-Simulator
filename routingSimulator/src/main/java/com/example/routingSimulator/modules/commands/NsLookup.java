package com.example.routingSimulator.modules.commands;

import com.example.routingSimulator.modules.models.DnsServer.DNSServer;


// I created this class to separate the UI formatting logic from the Database logic.
// The DNSServer works with the raw data, but this class handles how that data looks to the user.
public class NsLookup
{
    public String execute(DNSServer server,String domain)
    {
        StringBuilder builder=new StringBuilder();

        //First, I act on the data: I ask the server to look up the IP
        String resultIP = server.performNsLookup(domain);

        // I also need the DNS server's own IP to mimic the real command output header
        String serverIP = server.getServerIP();

        builder.append("Server: Routing-Simulator-DNS\n");
        builder.append("Address "+serverIP+"\n\n");

        if(resultIP!=null)
        {
            builder.append("Domain: "+domain+"\n");
            builder.append("IP Address: "+resultIP+"\n");
        }
        else
        {
            builder.append("UnKnown can't find " + domain + ": Non-existent domain\\n\n");
        }

        return builder.toString();
    }
}
