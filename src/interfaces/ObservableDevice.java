package interfaces;

public interface ObservableDevice {
    void addObserver(DeviceObserver observer);
    void removeObserver(DeviceObserver observer);
    void notifyObservers(String eventDescription);

}
