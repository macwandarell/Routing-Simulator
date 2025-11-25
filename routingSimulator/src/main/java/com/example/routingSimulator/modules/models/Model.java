package com.example.routingSimulator.modules.models;
public abstract class Model{
    private static int publicID=0;
    private int modelID;
    public Model(){
        modelID=publicID;// i guess need to comment ask vedant
        publicID=publicID+1;

    }
    public String viewDetails() {
        return "Model ID: " + this.modelID;
    }
    public void addPort(int port){}
    public void setModelID(int modelID){
        this.modelID=modelID;
    }
    public int getModelID(){return this.modelID;}
    public String getType(){return null;}
    public void setIpv4(String ipv4){}
    public abstract String getId();
}

//add model directly to network on adding or else error can come