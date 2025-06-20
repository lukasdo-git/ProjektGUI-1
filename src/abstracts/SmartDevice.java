package abstracts;

import classes.house.Room;
import enums.DeviceStatus;
import enums.DeviceType;

import java.util.UUID;

public abstract class SmartDevice {

    private final UUID deviceUUID;
    private final int deviceId;
    private String deviceName;
    private DeviceStatus deviceStatus;
    private final DeviceType deviceType;
    private Room room;
    private boolean live;

    public SmartDevice(int deviceId, String deviceName, DeviceType deviceType) {
        this.deviceUUID = UUID.randomUUID();
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceStatus = DeviceStatus.OFF;
    }

    public abstract void simulate() throws IllegalAccessException;

    @Override
    public String toString() {
        return "["+this.deviceUUID +"] "+this.deviceName+": \tStatus "+this.deviceStatus +" ";
    }

    public void setLive() { this.live = !this.live;}
    public boolean isLive() {
        return this.live;
    }

    public void setStatus(DeviceStatus status) {
        this.deviceStatus = status;
    }

    public DeviceStatus getStatus() {
        return deviceStatus;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setRoom(Room room) { this.room = room; }
    public Room getRoom() { return room; }
}
