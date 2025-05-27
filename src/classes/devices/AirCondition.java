package classes.devices;

import abstracts.SmartDevice;
import classes.house.Room;
import enums.DeviceStatus;
import enums.DeviceType;
import interfaces.Switchable;

public class AirCondition extends SmartDevice implements Switchable {

    private boolean running;
    private Thread thread;
    private final int chillingPower;
    private final int chillingCycleTime = 1000;
    private Room room;

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
