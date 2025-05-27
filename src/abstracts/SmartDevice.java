package abstracts;

import enums.DeviceStatus;
import enums.DeviceType;

import java.util.UUID;

public abstract class SmartDevice {
    private UUID deviceUUID;
    private int deviceId;
    private String deviceName;
    private DeviceStatus deviceStatus;
    private DeviceType deviceType;
    private boolean live;

    public SmartDevice(int deviceId, String deviceName, DeviceType deviceType) {
        this.deviceUUID = UUID.randomUUID();
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.deviceStatus = DeviceStatus.OFF;
    }

    public abstract void simulate() throws IllegalAccessException;
    public void setLive() {
        this.live = !this.live;
    }
    public boolean isLive() {
        return this.live;
    }

    public void setStatus(DeviceStatus status) {
        this.deviceStatus = status;
    };
    public DeviceStatus getStatus() {
        return deviceStatus;
    };
    public UUID getDeviceUUID() {
        return deviceUUID;
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


    @Override
    public String toString() {
        return "["+this.deviceUUID +"] "+this.deviceName+": \tStatus "+this.deviceStatus +" ";
    }
}
