package modules.models;
public class Model{
    private static int publicID=0;
    private int modelID;
    public Model(){
        modelID=publicID;
        publicID=publicID+1;

    }
    public String viewDetails() {
        return "Model ID: " + this.modelID;
    }

    public void setModelID(int modelID){
        this.modelID=modelID;
    }
    public int getModelID(){return this.modelID;}
    public String getType(){return null;}
    public void setIpv4(String ipv4){}
}