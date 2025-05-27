package classes.house;

import abstracts.SmartDevice;
import classes.Rule;
import classes.devices.AirCondition;
import classes.devices.Heater;
import classes.devices.HumiditySensor;
import classes.devices.TemperatureSensor;
import enums.DeviceStatus;
import enums.RoomType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Room {

    private String name;
    private int id;
    private RoomType type;
    private List<SmartDevice> devices;
    private Thread thread;
    private boolean running = true;

    private double currentTemp;
    private double ambientTemp;
    private double currentHumidity;
    private double ambientHumidity;

    private List<Rule<? extends SmartDevice>> rules = new ArrayList<>();

    public Room(String name, int id, RoomType type, double ambientTemp) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.devices = new ArrayList<>();
        this.ambientTemp = ambientTemp;
        this.currentTemp = ambientTemp + (Math.random()*10) - 5;
        this.ambientHumidity = (Math.random()*5) + 12;
        this.currentHumidity = ambientHumidity + (Math.random()*2) - 1;
        this.thread = new Thread(() -> {
            while(running) {
                try{
                    updateTemperature();
                    updateHumidity();
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

    public Boolean hasRules() { return rules.size() > 0; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public RoomType getRoomType() { return type; }
    public int getId() { return id; }
    public double getTemperature() { return currentTemp; }
    public double getHumidity() { return currentHumidity; }
    public boolean hasDevices() { return devices.size() > 0; }
    public List<SmartDevice> getDevices() { return devices; }
    public int getNumOfDevices() { return devices.size(); }

    public void addDevice(SmartDevice device) {
        devices.add(device);

        if(device instanceof TemperatureSensor sensor) {
            sensor.setRoom(this);
        }
        if(device instanceof HumiditySensor sensor) {
            sensor.setRoom(this);
        }
        if(device instanceof Heater heater) {
            heater.setRoom(this);
        }
        if(device instanceof AirCondition aircon) {
            aircon.setRoom(this);
        }
    }

    public <T extends SmartDevice> List<T> getDevicesByType(Class<T> type) {
        return devices.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .collect(Collectors.toList());
    }

    private void updateTemperature() {
        if(currentTemp > ambientTemp) currentTemp -= (Math.random()*3)-1.5;
        if(currentTemp < ambientTemp) currentTemp += (Math.random()*3)-1.5;

        List<Heater> activeHeaters = getDevicesByType(Heater.class).stream()
                .filter(h -> h.getStatus() == DeviceStatus.ON)
                .toList();
        List<AirCondition> activeAirconditioners = getDevicesByType(AirCondition.class).stream()
                .filter(h -> h.getStatus() == DeviceStatus.ON)
                .toList();

        int totalHeatingPower = 0;
        for(Heater heater : activeHeaters) {
            totalHeatingPower += heater.getHeatingPower();
        }

        int totalCoolingPower = 0;
        for(AirCondition aircon : activeAirconditioners) {
            totalCoolingPower += aircon.getCoolingPower();
        }

        currentTemp += 0.1 * ((double) (totalHeatingPower-totalCoolingPower) /100);
    }

    private void updateHumidity() {
        if(currentHumidity > ambientHumidity) currentHumidity -= Math.random();
        if(currentHumidity < ambientHumidity) currentHumidity += Math.random();
    }

}
