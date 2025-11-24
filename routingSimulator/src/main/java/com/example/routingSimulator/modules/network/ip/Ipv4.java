package com.example.routingSimulator.modules.network.ip;

public class Ipv4{
    //octets are defined from left as 1 to right as 4
    private int octet_1;
    private int octet_2;
    private int octet_3;
    private int octet_4;
    public Ipv4(String ipv4) {
        String[] parts= ipv4.split("\\.");
        if(parts.length!=4){
            throw new IllegalArgumentException("Invalid IPv4 length.\n");
        }
        int octet_1=Integer.parseInt(parts[0]);
        int octet_2=Integer.parseInt(parts[1]);
        int octet_3=Integer.parseInt(parts[2]);
        int octet_4=Integer.parseInt(parts[3]);
        if(octet_1>255||octet_1<0||octet_2>255||octet_2<0||octet_3>255||octet_3<0||octet_4>255||octet_4<0){
            throw new IllegalArgumentException("Invalid IPv4 address.\n");
        }
        this.octet_1 = octet_1;
        this.octet_2 = octet_2;
        this.octet_3 = octet_3;
        this.octet_4 = octet_4;
    }
    public String getIpString(){
        String ip= Integer.toString(octet_1)+"."+Integer.toString(octet_2)+"."+Integer.toString(octet_3)+"."+Integer.toString(octet_4);
        return ip;
    }
}