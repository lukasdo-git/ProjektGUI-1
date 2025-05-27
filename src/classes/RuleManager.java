package classes;

import abstracts.SmartDevice;
import classes.devices.Heater;
import classes.devices.Lightbulb;
import classes.devices.TemperatureSensor;
import classes.house.House;
import classes.house.Room;
import enums.DeviceStatus;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class RuleManager {

    private static RuleManager instance;
    private List<House> houseList;
    private Thread ruleThread;
    private boolean running = true;

    public static RuleManager getInstance(List<House> houseList) {
        if(instance == null) instance = new RuleManager(houseList);
        return instance;
    }

    private RuleManager(List<House> houseList) {
        this.houseList = houseList;

        this.ruleThread = new Thread(() -> {
            while (running) {
                for (House house : houseList) {
                    for (Room room : house.getRooms()) {
                        for (Rule<? extends SmartDevice> rule : room.getRules()) {
                            try {
                                rule.execute();
                            } catch (Exception e) {
                                System.err.println("Błąd przy wykonaniu reguły: " + e.getMessage());
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    running = false;
                    break;
                }
            }
        });
        ruleThread.start();
    }

    public void shutdown() {
        running = false;
        ruleThread.interrupt();
    }

    public Rule<? extends SmartDevice> parseRule(SmartDevice condDevice, SmartDevice actionDevice, String condition, String action) {
        Predicate<SmartDevice> predicate;
        Consumer<SmartDevice> consumer;

        if (condDevice instanceof TemperatureSensor sensor) {
            if (condition.matches("temp ?< ?(\\d+(\\.\\d+)?)")) {
                double threshold = Double.parseDouble(condition.replaceAll("[^\\d.]", ""));
                predicate = dev -> sensor.readValue() < threshold;
            } else if (condition.matches("temp ?> ?(\\d+(\\.\\d+)?)")) {
                double threshold = Double.parseDouble(condition.replaceAll("[^\\d.]", ""));
                predicate = dev -> sensor.readValue() > threshold;
            } else if (condition.matches("temp ?= ?(\\d+(\\.\\d+)?)")) {
                double threshold = Double.parseDouble(condition.replaceAll("[^\\d.]", ""));
                predicate = dev -> sensor.readValue() == threshold;
            } else {
                System.out.println("Nieznany warunek temperatury.");
                return null;
            }
        } else if (condition.toLowerCase().startsWith("status == ")) {
            try {
                String statusStr = condition.substring(10).trim().toUpperCase();
                DeviceStatus status = DeviceStatus.valueOf(statusStr);
                predicate = dev -> dev.getStatus() == status;
            } catch (IllegalArgumentException e) {
                System.out.println("Nieznany status: " + condition.substring(10).trim());
                return null;
            }
        } else {
            System.out.println("Nieobsługiwany warunek.");
            return null;
        }

        if (actionDevice instanceof Heater heater) {
            if (action.equalsIgnoreCase("turnOn")) {
                consumer = dev -> heater.turnOn();
            } else if (action.equalsIgnoreCase("turnOff")) {
                consumer = dev -> heater.turnOff();
            } else {
                System.out.println("Nieobsługiwana akcja.");
                return null;
            }
        } else if (actionDevice instanceof Lightbulb bulb) {
            if (action.equalsIgnoreCase("turnOn")) {
                consumer = dev -> bulb.turnOn();
            } else if (action.equalsIgnoreCase("turnOff")) {
                consumer = dev -> bulb.turnOff();
            } else if (action.toLowerCase().startsWith("changecolor")) {
                String[] parts = action.split(" ");
                if (parts.length == 4) {
                    try {
                        int h = Integer.parseInt(parts[1]);
                        double s = Double.parseDouble(parts[2]);
                        double v = Double.parseDouble(parts[3]);
                        consumer = dev -> bulb.changeColor(h, s, v);
                    } catch (NumberFormatException e) {
                        System.out.println("Niepoprawne parametry koloru.");
                        return null;
                    }
                } else {
                    System.out.println("Zła liczba parametrów dla changeColor.");
                    return null;
                }
            } else {
                System.out.println("Nieobsługiwana akcja dla LightBulb.");
                return null;
            }
        } else {
            System.out.println("Nieobsługiwane urządzenie do akcji.");
            return null;
        }

        return new Rule<>(predicate, consumer, condDevice);
    }
}