package classes.devices;

import abstracts.SmartDevice;
import classes.house.Room;
import enums.DeviceStatus;
import enums.DeviceType;
import interfaces.DeviceObserver;
import interfaces.ObservableDevice;
import interfaces.SensorDevice;

import java.util.ArrayList;
import java.util.List;

public class HumiditySensor extends SmartDevice implements SensorDevice<Double>, ObservableDevice {

    private double humidity;
    private Room room;
    private int readCycleTime = 1500;

    private Thread thread;
    private boolean running = false;
    private boolean started = false;

    private final List<DeviceObserver> observers = new ArrayList<>();

    public HumiditySensor(int deviceId, String deviceName) {
        super(deviceId, deviceName, DeviceType.HUMIDITYSENSOR);
        super.setStatus(DeviceStatus.ON);
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
    public Double readValue() {
        if(super.getStatus() == DeviceStatus.FAULT) return 0.0;
        return this.humidity;
    }

    @Override
    public String getUnit() {
        return "g/m3";
    }

    @Override
    public void simulate() throws IllegalAccessException {
        if(super.getStatus() == DeviceStatus.OFF) return;
        if(super.getStatus() == DeviceStatus.FAULT) {
            if(super.isLive()) System.out.println(super.toString() + "\t BŁĄD CZUJNIKA");
            throw new IllegalAccessException("Błąd czujnika.");
        }

        double rollForFault = Math.random();
        if(rollForFault < (0.01)) {
            super.setStatus(DeviceStatus.FAULT);
            this.running = false;
        }

        this.humidity = this.room.getHumidity();
        if(super.isLive()) System.out.println(super.toString()+"\t odczyt wilgotności: " + this.readValue());
        this.notifyObservers("Humidity: " + this.humidity);
    }

    public void fixFault() {
        running = false;
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        super.setStatus(DeviceStatus.ON);
        System.out.println(super.toString()+"\t Usunięto usterkę");

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
}
