package classes.devices;

import abstracts.SmartDevice;
import classes.house.Room;
import enums.DeviceStatus;
import enums.DeviceType;
import enums.LogType;
import interfaces.DeviceObserver;
import interfaces.ObservableDevice;
import interfaces.Switchable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Lightbulb extends SmartDevice implements Switchable, ObservableDevice {
    private int hue;
    private double saturation;
    private double value;
    private DeviceStatus status;
    private Room room;


    private final List<DeviceObserver> observers = new ArrayList<>();

    public Lightbulb(int deviceId, String deviceName, int hue, double saturation, double value) {
        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
        super(deviceId, deviceName, DeviceType.LIGHTBULB);
    }

    private void setRoom(Room room) {
        this.room = room;
    }

    private Room getRoom() {
        return room;
    }

    @Override
    public void simulate() throws IllegalAccessException {
        if( super.getStatus() != DeviceStatus.ON ) {
            throw new IllegalAccessException("\t Żarówka jest wyłączona.");
        }
        System.out.println(super.toString() + "\t symulacja: świeci na kolor " + this.getRGBColor().toString());
        notifyObservers(LogType.READING, "shines with RGB color " + this.getRGBColor().toString());
    }

    @Override
    public void turnOn() {
        super.setStatus(DeviceStatus.ON);
        notifyObservers("Turned on");
    }

    @Override
    public void turnOff() {
        super.setStatus(DeviceStatus.OFF);
        notifyObservers("Turned off");
    }

    @Override
    public boolean isOn() {
        return status == DeviceStatus.ON;
    }

    public void changeColor(int hue, double saturation, double value) {
        if(hue<0 || hue>360) {
            System.out.println("H musi być 0-360");
            return;
        }
        if(saturation<0 || saturation>1) {
            System.out.println("S musi być 0.0-1.0");
        }
        if(value<0 || value>1) {
            System.out.println("V musi być 0.0-1.0");
        }
        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
    }

    public Color getRGBColor() {
        double C = value * saturation;
        double X = C * (1 - Math.abs((hue / 60.0) % 2 - 1));
        double m = value - C;

        double r1 = 0, g1 = 0, b1 = 0;

        if (hue < 60) {
            r1 = C; g1 = X; b1 = 0;
        } else if (hue < 120) {
            r1 = X; g1 = C; b1 = 0;
        } else if (hue < 180) {
            r1 = 0; g1 = C; b1 = X;
        } else if (hue < 240) {
            r1 = 0; g1 = X; b1 = C;
        } else if (hue < 300) {
            r1 = X; g1 = 0; b1 = C;
        } else {
            r1 = C; g1 = 0; b1 = X;
        }

        int r = (int) Math.round((r1 + m) * 255);
        int g = (int) Math.round((g1 + m) * 255);
        int b = (int) Math.round((b1 + m) * 255);

        r = Math.min(255, Math.max(0, r));
        g = Math.min(255, Math.max(0, g));
        b = Math.min(255, Math.max(0, b));

        notifyObservers(LogType.STATUS_CHANGE, "changed color to RGB " + r + " " + g + " " + b);
        return new Color(r, g, b);
    }

    @Override
    public String toString() {
        return super.toString();
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
