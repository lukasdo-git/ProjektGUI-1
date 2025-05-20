package Classes;

import Abstracts.SmartDevice;
import Interfaces.Switchable;

public class Outlet extends SmartDevice implements Switchable {
    private double powerDraw;
    private int numberOfSockets;
    private boolean devicePluggedIn;

    public Outlet(String deviceName, int numberOfSockets) {
        this.powerDraw = 0;
        this.numberOfSockets = numberOfSockets;
        this.devicePluggedIn = false;
        super(deviceName);
    }

    @Override
    public void simulate() {

    }

    @Override
    public void turnOn() throws IllegalStateException{
        if(!devicePluggedIn) {
            throw new IllegalStateException("Brak podłączonych urządzeń.");
        }
    }

    @Override
    public void turnOff() {

    }

    @Override
    public boolean isOn() {
        return false;
    }
}
