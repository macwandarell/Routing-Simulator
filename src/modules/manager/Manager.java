package modules.manager;
import modules.models.Model;
import java.util.ArrayList;

public class Manager{
    private String managerID;
    private ArrayList<Model> entities = new ArrayList<Model>();
    public Manager(String managerID){
        this.managerID=managerID;
    }
    public void addEntity(Model entity){
        entities.add(entity);
        System.out.println("Successfully added: "+ entity.viewDetails());
    }
    public String viewDetails(){
       return this.managerID;
    }
}
