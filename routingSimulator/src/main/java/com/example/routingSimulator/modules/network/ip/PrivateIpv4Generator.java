package com.example.routingSimulator.modules.network.ip;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class PrivateIpv4Generator {
    private Random rand= new Random();
    public static int no=0;
    public void generateIps(int count){
        try(FileWriter writer= new FileWriter("data/ip_list_"+no+".txt")){
            for (int i = 0; i < count; i++) {
                String ip = generateSinglePrivateIP();
                writer.write(ip + "\n");
            }
            System.out.println("Generated " + count + " IPs in ip_list_"+no+".txt");
            no++;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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