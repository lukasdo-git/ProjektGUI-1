package classes.devices;

import abstracts.SmartDevice;
import enums.DeviceStatus;
import interfaces.Switchable;

public class Heater extends SmartDevice implements Switchable {
    private boolean running = false;
    private Thread thread;
    private final double heatingPower;

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

    }

    @Override
    public void turnOff() {

    }

    @Override
    public boolean isOn() {
        return false;
    }
}
