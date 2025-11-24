package modules.terminal;
import java.util.Scanner;

public class Playground{
    Scanner scanner = new Scanner();
    public Playground(){}
    private void header(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("modules/terminal/banners/playground.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
            System.out.println(ANSI_YELLOW+"Here is a list of the available commands(case doesn't matter)"+ANSI_RESET);
            System.out.println(ANSI_RED+"help"+ANSI_RESET+ANSI_YELLOW+" - prints a list of executable commands"+ANSI_RESET);
            System.out.println(ANSI_RED+"exit"+ANSI_RESET+ANSI_YELLOW+" - exits the playground"+ANSI_RESET);
            System.out.println(ANSI_RED+"new <name>"+ANSI_RESET+ANSI_YELLOW+" - creates a new playground with a na")

        } catch (java.io.FileNotFoundException e) {
            System.out.println("Logo file not found!");
            e.printStackTrace();
        } catch (java.io.IOException e) {
            System.out.println("Error reading the logo file!");
            e.printStackTrace();
        }
    }
    public void start(){

    }
}