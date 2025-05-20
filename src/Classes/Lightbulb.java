package Classes;

import Abstracts.SmartDevice;
import Interfaces.Switchable;

public class Lightbulb extends SmartDevice implements Switchable {
    int hue;
    double saturation;
    double value;

    public Lightbulb(int hue, double saturation, double value) {
        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
    }

    @Override
    public void simulate() {

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
