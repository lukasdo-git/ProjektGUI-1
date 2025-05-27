package classes;

import abstracts.SmartDevice;
import enums.LogType;
import interfaces.DeviceObserver;

public class LoggerObserver implements DeviceObserver {

    private Logger logger;

    public LoggerObserver(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void onDeviceEvent(SmartDevice device, String event) {
        logger.log(device, LogType.STATUS_CHANGE, event);
    }

    public void onDeviceEvent(SmartDevice device, LogType category, String event) {
        logger.log(device, category, event);
    }


}
