package modules.manager;
import modules.manager.Manager;
import java.util.ArrayList;

public class GlobeManager{
    private ArrayList<Manager> managers = new ArrayList<Manager>();
    public GlobeManager(){}
    public void addManager(Manager manager){
        managers.add(manager);
        System.out.println("Successfully added: "+ manager.viewDetails());
    }
}
