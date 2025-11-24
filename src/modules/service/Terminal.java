package modules.terminal;
import modules.network.ip.PrivateIpv4Generator;
import modules.models.router.Router;
import modules.manager.Manager;
import modules.manager.GlobeManager;
import modules.models.dhcp.Dhcp;
import modules.models.publicServer.PublicServer;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;


public class Terminal {
    // Source - https://stackoverflow.com/a
// Posted by WhiteFang34, modified by community. See post 'Timeline' for change history
// Retrieved 2025-11-24, License - CC BY-SA 3.0
    public Terminal(){}
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    private void header(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("modules/terminal/banners/logo.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
            System.out.println(ANSI_YELLOW+"Here is a list of the available commands(case doesn't matter)"+ANSI_RESET);
            System.out.println(ANSI_RED+"help"+ANSI_RESET +ANSI_YELLOW+" - prints a list of executable commands"+ANSI_RESET);
            System.out.println(ANSI_RED+"exit"+ANSI_RESET+ANSI_YELLOW+" - exits the simulator"+ANSI_RESET);

        } catch (java.io.FileNotFoundException e) {
            System.out.println("Logo file not found!");
            e.printStackTrace();
        } catch (java.io.IOException e) {
            System.out.println("Error reading the logo file!");
            e.printStackTrace();
        }
    }
    public void start(){
        header();
        Scanner scanner = new Scanner(System.in);
        String command="";
        while(!command.equals("exit")){
            System.out.println(ANSI_GREEN+"Enter which command you want to execute, type help to list commands"+ANSI_RESET);
            command=scanner.nextLine().trim().toLowerCase();
            switch(command){
                case "help":
                     header();
                     break;
                case "exit":
                    System.out.println(ANSI_YELLOW+"Goodbye :)"+ANSI_RESET);
                    break;
                case "play":
                    System.out.println(ANSI_YELLOW+"Taking you to the playgrounnd"+ANSI_RESET);

                default:
                    System.out.println(ANSI_RED+"Unknown command:"+ANSI_RESET);
            }
        }

    System.out.println("Ok");}

}