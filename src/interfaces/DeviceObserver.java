package interfaces;

import abstracts.SmartDevice;
import enums.LogType;

public interface DeviceObserver {
    void onDeviceEvent(SmartDevice device, String event);
    void onDeviceEvent(SmartDevice device, LogType logType, String event);
}
