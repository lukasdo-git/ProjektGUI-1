package classes.devices;

import abstracts.SmartDevice;
import classes.house.Room;
import enums.DeviceStatus;
import enums.DeviceType;
import enums.LogType;
import interfaces.DeviceObserver;
import interfaces.ObservableDevice;
import interfaces.Switchable;

import java.util.ArrayList;
import java.util.List;

public class AirCondition extends SmartDevice implements Switchable, ObservableDevice {

    private boolean running;
    private Thread thread;
    private final int chillingPower;
    private final int chillingCycleTime = 1000;
    private Room room;

    private final List<DeviceObserver> observers = new ArrayList<>();

    public AirCondition(int deviceId, String deviceName, int chillingPower) {
        super(deviceId, deviceName, DeviceType.AIRCON);
        this.chillingPower = chillingPower;

        running = true;
        thread = new Thread(() -> {
            while (running) {
                try {
                    this.simulate();
                    Thread.sleep(chillingCycleTime);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    running = false;
                    Thread.currentThread().interrupt();
                }
            }
        });
        thread.start();
    }


    @Override
    public void simulate() throws IllegalAccessException {
        if(super.getStatus() == DeviceStatus.ON) {
            this.notifyObservers(LogType.READING, "is chilling room " + this.room.getName());
            if(super.isLive()) {
                System.out.println(super.toString() + "\t ochładza pokój " + this.room.getName());
            }
        }

    }

    public int getCoolingPower() {
        return chillingPower;
    }

    @Override
    public void turnOn() {
        super.setStatus(DeviceStatus.ON);
        this.notifyObservers("Turned on");
    }

    @Override
    public void turnOff() {
        super.setStatus(DeviceStatus.OFF);
        this.notifyObservers("Turned off");
    }

    @Override
    public boolean isOn() {
        return super.getStatus() == DeviceStatus.ON;
    }

    public void setRoom(Room room) {
        this.room = room;
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
