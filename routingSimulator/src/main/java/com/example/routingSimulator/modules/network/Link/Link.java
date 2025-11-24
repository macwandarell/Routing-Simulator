package com.example.routingSimulator.modules.network.Link;
import modules.models.Model;

// Link class to provide a physical Layer between two models
public class Link {
    private Model first; // endpoint of link
    private Model second; // endpoint of link
    private double bandwidth; // link bandwidth speed
    private LinkSpeedType type; // Link bandwidth speed unit

    // Link Constructor
    public Link(Model first,Model second, double bandwidth, String type){
        this.first = first;
        this.second = second;
        this.bandwidth = bandwidth;

        // converting string type to enum type
        if (type.toLowerCase().equals("kbps")) {
            this.type = LinkSpeedType.KBPS;
        } else if (type.toLowerCase().equals("mbps")) {
            this.type = LinkSpeedType.MBPS;
        } else if (type.toLowerCase().equals("gbps")) {
            this.type = LinkSpeedType.GBPS;
        } else if (type.toLowerCase().equals("tbps")) {
            this.type = LinkSpeedType.TBPS;
        } else {
            throw new IllegalArgumentException("Invalid Speed type please enter values from {Kbps, Mbps, Gbps, Tbps}.");
        }
    }

    // method to get speed in desired type
    public double getSpeed(LinkSpeedType type){
        return this.bandwidth * Math.pow(1024,this.type.ordinal()-type.ordinal());
    }

    // Getters and Setters
    public Model getFirst() {
        return first;
    }

    public void setFirst(Model first) {
        this.first = first;
    }

    public Model getSecond() {
        return second;
    }

    public void setSecond(Model second) {
        this.second = second;
    }

    public double getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(double bandwidth) {
        this.bandwidth = bandwidth;
    }

    public LinkSpeedType getType() {
        return type;
    }

    public void setType(LinkSpeedType type) {
        this.type = type;
    }
}
