package modules.service;
import org.springframework.stereotype.Service;
import modules.services.AnsiColor;

@Service
public class HomeHelpService{
    public String getHomeHelpMessage(){
        Stringbuilder sb = new Stringbuilder();
        BufferedReader reader = new BufferedReader(new FileReader("modules/terminal/banners/logo.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        sb.append(AnsiColor.YELLOW.wrap("Here is a list of the available commands(case doesn't matter)"));
        sb.append(AnsiColor.RED.wrap("/help")+AnsiColor.YELLOW.wrap(" - prints a list of executable commands"));
        sb.append(AnsiColor.RED.wrap("/"))
        return sb;
    }

}