package Classes;

import Abstracts.SmartDevice;
import Enums.DeviceStatus;
import Interfaces.SensorDevice;

public class TemperatureSensor extends SmartDevice implements SensorDevice<Double> {

    private Double temperature;
    private int batteryCycles;

    private Thread thread;
    private boolean running;

    public TemperatureSensor(String deviceName) {
        super(deviceName);
        super.setStatus(DeviceStatus.ACTIVE);
        this.batteryCycles = 50;
        this.running = true;
        this.thread = new Thread(() -> {
            while(running) {
                try {
                    this.simulate();
                    Thread.sleep(200);
                } catch (IllegalAccessException | InterruptedException e) {
                    running = false;
                    throw new RuntimeException(e);
                }
            }
        }
        );
        thread.start();
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

    public void changeBattery() {
        thread.interrupt();
        this.batteryCycles = 50;
        super.setStatus(DeviceStatus.ACTIVE);
        System.out.println(super.toString()+"\t Wymieniono baterię");

        if(!running || !thread.isAlive()) {
            running = true;
            this.thread = new Thread(() -> {
                while(running) {
                    try {
                        this.simulate();
                        Thread.sleep(200);
                    } catch (IllegalAccessException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            thread.start();
        }

    }

    @Override
    public void simulate() throws IllegalAccessException {
        if(super.getStatus() == DeviceStatus.FAULT) {
            this.running = false;
            throw new IllegalAccessException("\t Błąd czujnika.");
        }
        if(this.batteryCycles <=15) {
            super.setStatus(DeviceStatus.LOW_BATTERY);
            System.out.println(super.toString() + "\t UWAGA - niski poziom baterii");
        }

        double rollForFault = Math.random();
        if(rollForFault < (super.getStatus() == DeviceStatus.LOW_BATTERY ? (0.15 - (this.batteryCycles*0.01)) : 0.02)) {
            super.setStatus(DeviceStatus.FAULT);
            this.running = false;
        }

        this.batteryCycles -= 1;
        this.temperature = (Math.random() * 80) - 40;
        System.out.println(super.toString()+"\t odczyt temperatury: " + this.readValue());

    }
}
