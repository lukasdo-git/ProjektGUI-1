package interfaces;

import abstracts.SmartDevice;

public interface DeviceObserver {
    void onDeviceEvent(SmartDevice device, String event);
}
