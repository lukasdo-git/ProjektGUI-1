package classes.devices;

import abstracts.SmartDevice;
import enums.DeviceStatus;
import interfaces.Switchable;

import java.awt.*;

public class Lightbulb extends SmartDevice implements Switchable {
    private final int hue;
    private final double saturation;
    private final double value;
    private DeviceStatus status;

    public Lightbulb(String name, int hue, double saturation, double value) {
        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
        super(name);
    }

    @Override
    public void simulate() throws IllegalAccessException {
        if( super.getStatus() != DeviceStatus.ON ) {
            throw new IllegalAccessException("\t Żarówka jest wyłączona.");
        }
        System.out.println(super.toString() + "\t symulacja: świeci na kolor " + this.getRGBColor().toString());
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
        return status == DeviceStatus.ON;
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

        return new Color(r, g, b);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
