package interfaces;
import abstracts.SmartDevice;
import enums.LogType;

public interface DeviceLogger {

    <T extends SmartDevice> void log(SmartDevice device, LogType category, String message);
}
