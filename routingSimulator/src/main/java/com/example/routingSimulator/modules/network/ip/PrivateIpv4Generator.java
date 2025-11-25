package com.example.routingSimulator.modules.network.ip;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class PrivateIpv4Generator {
    private Random rand= new Random();
    public PrivateIpv4Generator(){}
    public List<String> generateIps(int count) {
        List<String> ipList = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String ip = generateSinglePrivateIP();
                ipList.add(ip);
            }
        return ipList;
    }
    private String generateSinglePrivateIP() {
        int choice = rand.nextInt(3);
        int a = 0, b = 0, c = 0, d = 0;
        if(choice==0){
                // 10.x.x.x
                a = 10;
                b = rand.nextInt(256);
                c = rand.nextInt(256);}
        else if(choice==1){
                // 172.16–31.x.x
                a = 172;
                b = 16 + rand.nextInt(16); // 16–31
                c = rand.nextInt(256);}
        else if(choice==2){
                // 192.168.x.x
                a = 192;
                b = 168;
                c = rand.nextInt(256);
        }
        d = 1 + rand.nextInt(254);
        return a + "." + b + "." + c + "." + d;}
}