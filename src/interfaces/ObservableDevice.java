package interfaces;

import enums.LogType;

public interface ObservableDevice {
    void addObserver(DeviceObserver observer);
    void removeObserver(DeviceObserver observer);
    void notifyObservers(String eventDescription);
    void notifyObservers(LogType eventType, String eventDescription);

}
