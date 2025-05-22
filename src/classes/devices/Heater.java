package classes.devices;

import abstracts.SmartDevice;
import classes.house.Room;
import enums.DeviceStatus;
import interfaces.Switchable;

public class Heater extends SmartDevice implements Switchable {
    private boolean running = false;
    private Thread thread;
    private final double heatingPower;
    private Room room;

    public Heater(String deviceName, double heatingPower) {
        super(deviceName);
        this.heatingPower = heatingPower;
        this.setStatus(DeviceStatus.OFF);
    }


    @Override
    public void simulate() throws IllegalAccessException {

    }

    @Override
    public void turnOn() {
        super.setStatus(DeviceStatus.ON);
    }

    @Override
    public void turnOff() {
        super.setStatus(DeviceStatus.OFF);
    }

    @Override
    public boolean isOn() {
        return super.getStatus() == DeviceStatus.ON;
    }
}
