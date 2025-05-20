import Classes.Lightbulb;
import Classes.Outlet;
import Classes.TemperatureSensor;

public class Main {
    public static void main(String[] args) {
        TemperatureSensor temp001 = new TemperatureSensor("temp001");
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        temp001.changeBattery();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        temp001.changeBattery();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        temp001.changeBattery();
    }
}
