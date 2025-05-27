package classes.devices;

import abstracts.SmartDevice;
import classes.house.Room;
import enums.DeviceStatus;
import enums.DeviceType;
import enums.LogType;
import interfaces.DeviceObserver;
import interfaces.ObservableDevice;
import interfaces.SensorDevice;

import java.util.ArrayList;
import java.util.List;

public class TemperatureSensor extends SmartDevice implements SensorDevice<Double>, ObservableDevice {

    private Double temperature;
    private int batteryCycles = 50;
    private Room room;
    private int readCycleTime = 1000;

    private Thread thread;
    private boolean running = false;
    private boolean started = false;

    private List<DeviceObserver> observers = new ArrayList<>();

    public TemperatureSensor(int deviceId, String deviceName) {
        super(deviceId, deviceName, DeviceType.TEMPSENSOR);
        super.setStatus(DeviceStatus.ACTIVE);
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

    @Override
    public Double readValue() {
        if(super.getStatus() == DeviceStatus.FAULT) return 0.0;
        return this.temperature;
    }

    @Override
    public String getUnit() {
        return "Celsius";
    }

    @Override
    public void simulate() throws IllegalAccessException {
        if(super.getStatus() == DeviceStatus.FAULT) {
            if(super.isLive()) System.out.println(super.toString() + "\t BŁĄD CZUJNIKA");
            notifyObservers(LogType.FAULT_DETECTED, "Fault detected");
            throw new IllegalAccessException("Błąd czujnika");
        }
        if(this.batteryCycles <=15) {
            super.setStatus(DeviceStatus.LOW_BATTERY);
            notifyObservers(LogType.FAULT_DETECTED, "Battery is low");
            if(super.isLive()) System.out.println(super.toString() + "\t UWAGA - niski poziom baterii");
        }

        double rollForFault = Math.random();
        if(rollForFault < (super.getStatus() == DeviceStatus.LOW_BATTERY ? (0.15 - (this.batteryCycles*0.01)) : 0.02)) {
            super.setStatus(DeviceStatus.FAULT);
            this.running = false;
        }

        this.batteryCycles -= 1;
        this.temperature = this.room.getTemperature();
        if(super.isLive()) System.out.println(super.toString()+"\t odczyt temperatury: " + this.readValue());
        this.notifyObservers(LogType.READING, "Temperature: " + this.temperature);
    }

    public void changeBattery() {

        running = false;
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        this.batteryCycles = 50;
        super.setStatus(DeviceStatus.ACTIVE);
        System.out.println(super.toString()+"\t Wymieniono baterię");
        notifyObservers(LogType.FAULT_FIXED, "battery changed");

        running = true;
        thread = new Thread(() -> {
            while (running) {
                try {
                    this.simulate();
                    Thread.sleep(readCycleTime);
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

    public void setRoom(Room room) {
        this.room = room;

        if(!started) {
            started = true;
            running = true;

            this.thread = new Thread(() -> {
                while(running) {
                    try {
                        this.simulate();
                        Thread.sleep(readCycleTime);
                    } catch (IllegalAccessException | InterruptedException e) {
                        running = false;
                        throw new RuntimeException(e);
                    }
                }
            }
            );
            thread.start();
        }
    }

    public Room getRoom() {
        return room;
    }
}
