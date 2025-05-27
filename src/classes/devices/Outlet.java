package classes.devices;

import abstracts.SmartDevice;
import enums.DeviceStatus;
import enums.DeviceType;
import enums.LogType;
import interfaces.DeviceObserver;
import interfaces.ObservableDevice;
import interfaces.Switchable;

import java.util.ArrayList;
import java.util.List;

public class Outlet extends SmartDevice implements Switchable, ObservableDevice {
    private boolean powered;
    private double totalEnergyConsumed;
    private final int overloadThreshold;
    boolean running = true;

    private final List<DeviceObserver> observers = new ArrayList<>();

    public Outlet(int deviceId, String deviceName, int overloadThreshold) {
        super(deviceId, deviceName, DeviceType.OUTLET);
        this.overloadThreshold = overloadThreshold;
        Thread thread = new Thread(() -> {
            while (running) {
                try {
                    simulate();
                    Thread.sleep(1000);
                } catch (IllegalAccessException e) {
                    setStatus(DeviceStatus.FAULT);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
    }

    @Override
    public void simulate() throws IllegalAccessException {
        if (getStatus() == DeviceStatus.FAULT) {
            notifyObservers(LogType.FAULT_DETECTED, "Fault detected");
            throw new IllegalAccessException("Gniazdko jest uszkodzone.");
        }

        if (!powered) return;

        double powerUsage = 200 + Math.random() * 1000;

        if (powerUsage > overloadThreshold) {
            setStatus(DeviceStatus.FAULT);
            notifyObservers(LogType.FAULT_DETECTED, "Overload! Power drawn: " + (int) powerUsage + "W");
            return;
        }

        totalEnergyConsumed += powerUsage * (1.0 / 3600);
        notifyObservers(LogType.READING, "Power draw: " + (int) powerUsage + "W | Total usage: " + String.format("%.2f", totalEnergyConsumed) + "Wh");
    }

    @Override
    public void turnOn() {
        powered = true;
        setStatus(DeviceStatus.ON);
        notifyObservers("Turned on");
    }

    @Override
    public void turnOff() {
        powered = false;
        setStatus(DeviceStatus.OFF);
        notifyObservers("Turned off");
    }

    @Override
    public boolean isOn() {
        return powered;
    }

    @Override
    public void addObserver(DeviceObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(DeviceObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String eventDescription) {
        for(DeviceObserver observer : observers) {
            observer.onDeviceEvent(this, eventDescription);
        }
    }

    @Override
    public void notifyObservers(LogType eventType, String eventDescription) {
        for(DeviceObserver observer : observers) {
            observer.onDeviceEvent(this, eventType, eventDescription);
        }
    }
}
