package com.example.routingSimulator.modules.commands;

import com.example.routingSimulator.modules.models.DnsServer.DNSServer;


//This class separates the UI formatting logic from the Database logic (DNSServer).
public class NsLookup
{
    public String execute(DNSServer server,String domain)
    {
        StringBuilder builder=new StringBuilder();

        String resultIP = server.performNsLookup(domain);

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
