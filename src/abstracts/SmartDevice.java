package abstracts;

import enums.DeviceStatus;

import java.util.UUID;

public abstract class SmartDevice {
    private UUID deviceId;
    private String deviceName;
    private DeviceStatus deviceStatus;

    public SmartDevice(String deviceName) {
        this.deviceId = UUID.randomUUID();
        this.deviceName = deviceName;
        this.deviceStatus = DeviceStatus.OFF;
    }

    public abstract void simulate() throws IllegalAccessException;

    public void setStatus(DeviceStatus status) {
        this.deviceStatus = status;
    };
    public DeviceStatus getStatus() {
        return deviceStatus;
    };

    @Override
    public String toString() {
        return "["+this.deviceId+"] "+this.deviceName+": \tStatus "+this.deviceStatus;
    }
}
