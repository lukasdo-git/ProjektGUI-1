package classes.devices;

import abstracts.SmartDevice;
import classes.house.Room;
import enums.DeviceStatus;
import enums.DeviceType;
import interfaces.SensorDevice;

public class TemperatureSensor extends SmartDevice implements SensorDevice<Double> {

    private Double temperature;
    private int batteryCycles = 50;
    private Room room;
    private int readCycleTime = 1000;

    private Thread thread;
    private boolean running = false;
    private boolean started = false;

    public TemperatureSensor(int deviceId, String deviceName) {
        super(deviceId, deviceName, DeviceType.TEMPSENSOR);
        super.setStatus(DeviceStatus.ACTIVE);
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

    @Override
    public void simulate() throws IllegalAccessException {
        if(super.getStatus() == DeviceStatus.FAULT) {
            if(super.isLive()) System.out.println(super.toString() + "\t BŁĄD CZUJNIKA");
            throw new IllegalAccessException("Błąd czujnika");
        }
        if(this.batteryCycles <=15) {
            super.setStatus(DeviceStatus.LOW_BATTERY);
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

    }
}
