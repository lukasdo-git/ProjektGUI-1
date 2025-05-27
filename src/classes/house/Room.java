package classes.house;

import abstracts.SmartDevice;
import classes.Rule;
import classes.devices.Heater;
import classes.devices.TemperatureSensor;
import enums.DeviceStatus;
import enums.RoomType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Room {

    private String name;
    private final int id;
    private final RoomType type;
    private List<SmartDevice> devices;
    private Thread thread;
    private boolean running = true;

    private final List<Rule<? extends SmartDevice>> rules = new ArrayList<>();

    private double currentTemp;
    private final double ambientTemp;

    public Room(String name, int id, RoomType type, double ambientTemp) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.devices = new ArrayList<>();
        this.ambientTemp = ambientTemp;
        this.currentTemp = ambientTemp + (Math.random()*10) - 5;
        this.thread = new Thread(() -> {
            while(running) {
                try{
                    updateTemperature();
                    Thread.sleep(600);
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void addRule(Rule<? extends SmartDevice> rule) {
        rules.add(rule);
    }

    public void removeRule(int i) {
        rules.remove(i);
    }

    public List<Rule<? extends SmartDevice>> getRules() {
        return rules;
    }

    public Boolean hasRules() {
        return rules.size() > 0;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }
    public RoomType getRoomType() { return type; }
    public int getId() { return id; }
    public double getTemperature() {
        return currentTemp;
    }
    public boolean hasDevices() {
        return devices.size() > 0;
    }
    public List<SmartDevice> getDevices() {
        return devices;
    }
    public int getNumOfDevices() {
        return devices.size();
    }

    public void addDevice(SmartDevice device) {
        devices.add(device);

        if(device instanceof TemperatureSensor sensor) {
            sensor.setRoom(this);
        }
        if(device instanceof Heater heater) {
            heater.setRoom(this);
        }
    }

    public <T extends SmartDevice> List<T> getDevicesByType(Class<T> type) {
        return devices.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    public void updateTemperature() {
        if(currentTemp > ambientTemp) currentTemp -= (Math.random()*3)-1.5;
        if(currentTemp < ambientTemp) currentTemp += (Math.random()*3)-1.5;

        List<Heater> activeHeaters = getDevicesByType(Heater.class).stream()
                .filter(h -> h.getStatus() == DeviceStatus.ON)
                .toList();

        int totalHeatingPower = 0;
        for(Heater heater : activeHeaters) {
            totalHeatingPower += heater.getHeatingPower();
        }

        currentTemp += 0.1 * ((double) totalHeatingPower /100);
    }


}
