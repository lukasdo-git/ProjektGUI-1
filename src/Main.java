
import classes.Menu;
import classes.devices.Heater;

public class Main {
    public static void main(String[] args) {
        String current = System.getProperty("user.dir");
        System.out.println("Current working directory in Java : " + current);
        Menu menu = new Menu();
        menu.run();
    }
}
