import classes.devices.TemperatureSensor;
import classes.house.Room;
import enums.RoomType;

public class Main {
    public static void main(String[] args) {
        TemperatureSensor temp001 = new TemperatureSensor("temp001");
        Room room001 = new Room("Kuchnia", RoomType.KUCHNIA, 21);
        room001.addDevice(temp001);

        Thread b = new Thread(() -> {
           while (true) {
               try {
                   Thread.sleep(3000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               room001.updateTemperature();
           }
        });
        b.start();

        Thread a = new Thread(() -> {
           while (true) {
                try {
                   Thread.sleep(15000);
                   temp001.changeBattery();
               } catch (InterruptedException e) {
                    e.printStackTrace();
                }
           }
        });
        a.start();
    }
}
