package modules.models;
public class Model{
    private String modelID;
    public Model(String modelID){
        this.modelID=modelID;
    }
    public String viewDetails(){
        return this.modelID;
    }
    public void setModelID(String modelID){
        this.modelID=modelID;
    }
    public String getType(){return null;}
    public void setIpv4(String ipv4){}
}