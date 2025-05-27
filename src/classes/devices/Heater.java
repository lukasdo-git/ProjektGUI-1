package classes.devices;

import abstracts.SmartDevice;
import classes.house.Room;
import enums.DeviceStatus;
import enums.DeviceType;
import interfaces.Switchable;

public class Heater extends SmartDevice implements Switchable {
    private boolean running;
    private Thread thread;
    private final int heatingPower;
    private final int heatCycleTime = 1000;
    private Room room;

    public Heater(int deviceId, String deviceName, int heatingPower) {
        super(deviceId, deviceName, DeviceType.HEATER);
        this.heatingPower = heatingPower;
        this.setStatus(DeviceStatus.ON);

        running = true;
        thread = new Thread(() -> {
            while (running) {
                try {
                    this.simulate();
                    Thread.sleep(heatCycleTime);
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
            if(super.isLive()) {
                System.out.println(super.toString() + "\t ogrzewa pokój " + this.room.getName());
            }
        }
    }

    public int getHeatingPower() {
        return heatingPower;
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
        return super.getStatus() == DeviceStatus.ON;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
