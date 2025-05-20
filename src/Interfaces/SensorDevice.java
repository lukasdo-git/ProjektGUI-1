package Interfaces;

public interface SensorDevice<T> {
    public T readValue();
    public String getUnit();
}
