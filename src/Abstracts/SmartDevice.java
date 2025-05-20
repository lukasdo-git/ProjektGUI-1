package Abstracts;

import Enums.DeviceStatus;

import java.util.UUID;

public abstract class SmartDevice {
    UUID deviceId;
    String deviceName;
    DeviceStatus deviceStatus;
    DeviceStatus[] validStatuses;

    public abstract void simulate();

    public void setStatus(DeviceStatus status) {
        this.deviceStatus = status;
    };
    public DeviceStatus getStatus() {
        return deviceStatus;
    };

    @Override
    public String toString() {
        return "["+this.deviceId+"] "+this.deviceName+": Status "+this.deviceStatus;
    }
}
