package classes.house;

import abstracts.SmartDevice;
import classes.devices.TemperatureSensor;
import enums.DeviceStatus;
import enums.RoomType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Room {

    private final String name;
    private final RoomType type;
    private List<SmartDevice> devices;

    private double currentTemp;
    private final double ambientTemp;

    public Room(String name, RoomType type, double ambientTemp) {
        this.name = name;
        this.type = type;
        this.devices = new ArrayList<>();
        this.ambientTemp = ambientTemp;
        this.currentTemp = ambientTemp + (Math.random()*10) - 5;
    }

    public String getName() {
        return name;
    }

    public void addDevice(SmartDevice device) {
        devices.add(device);

        if(device instanceof TemperatureSensor sensor) {
            sensor.setRoom(this);
        }
    }

    public double getTemperature() {
        return currentTemp;
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

        //int activeHeaters = getDevicesByType(Heater.class).stream()
        //        .filter(h -> h.getStatus() == DeviceStatus.ON)
        //        .count();

        //currentTemp += 0.1 * activeHeaters;
    }


}
