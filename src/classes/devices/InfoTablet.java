package classes.devices;

import abstracts.SmartDevice;
import enums.DeviceStatus;
import enums.DeviceType;
import enums.LogType;
import interfaces.DeviceObserver;
import interfaces.ObservableDevice;
import interfaces.Switchable;

import java.util.ArrayList;
import java.util.List;

public class InfoTablet extends SmartDevice implements DeviceObserver, ObservableDevice, Switchable {

    private Thread thread;
    private boolean running = false;

    private final List<DeviceObserver> observers = new ArrayList<>();

    public InfoTablet(int deviceId, String name, SmartDevice device) {
        super(deviceId, name, DeviceType.INFOTABLET);
        super.setStatus(DeviceStatus.ON);
        observe(device);
        this.thread = createThread();
        thread.start();
    }

    private Thread createThread() {
        return new Thread(() -> {
            while (running) {
                try {
                    simulate();
                    Thread.sleep(500);
                } catch (IllegalAccessException e) {
                    System.out.println(e.getMessage());
                    super.setStatus(DeviceStatus.FAULT);
                    attemptRestart();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    super.setStatus(DeviceStatus.OFF);
                }
            }
        });
    }

    private void attemptRestart() {
        new Thread(() -> {

            System.out.println(super.toString() + "\t Próba automatycznego restartu...");
            try {
                Thread.sleep(3000); // opóźnienie restartu
                if (getStatus() == DeviceStatus.FAULT) {
                    System.out.println(super.toString() + "\t Restart powiódł się.");
                    setStatus(DeviceStatus.ON);
                    notifyObservers(LogType.FAULT_FIXED, "Fault fixed");
                }
            } catch (InterruptedException ignored) {}
        }).start();
    }

    @Override
    public void simulate() throws IllegalAccessException {
        if (super.getStatus() == DeviceStatus.OFF) {
            turnOn();
        }
        if (Math.random() < 0.01) {
            super.setStatus(DeviceStatus.FAULT);
            this.notifyObservers(LogType.FAULT_DETECTED, "Fault detected");
            throw new IllegalAccessException(super.toString() + "\t Tablet uległ awarii!");
        }

        if (Math.random() < 0.05) {
            System.out.println(super.toString() + "\t Odczytanie danych systemowych...");
        }

        if (Math.random() < 0.03) {
            System.out.println(super.toString() + "\t Przypomnienie: sprawdź poziom baterii urządzeń.");
        }
    }

    @Override
    public void onDeviceEvent(SmartDevice device, String event) {
        if(super.getStatus() == DeviceStatus.ON) {
            if(super.isLive()) {
                System.out.println(super.toString() +"\t Wyświetla nowe zdarzenie -> " +device.toString() + event);
            }
        }
    }

    @Override
    public void onDeviceEvent(SmartDevice device, LogType logType, String event) {
        if(super.getStatus() == DeviceStatus.ON) {
            if(super.isLive()) {
                System.out.println(super.toString() +"\t Wyświetla nowe zdarzenie -> " +device.toString() + event);
            }
        }
    }

    @Override
    public void turnOn() {
        if (!running) {
            running = true;
            this.thread = createThread();
            thread.start();
        }
        super.setStatus(DeviceStatus.ON);
        this.notifyObservers("Turned on");
    }

    @Override
    public void turnOff() {
        running = false;
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        super.setStatus(DeviceStatus.OFF);
        this.notifyObservers("Turned off");
    }

    @Override
    public boolean isOn() {
        return super.getStatus() == DeviceStatus.ON;
    }

    public void observe(SmartDevice device) {
        if (device instanceof ObservableDevice observable) {
            observable.addObserver(this);
        }
    }

    @Override
    public void addObserver(DeviceObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(DeviceObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String eventDescription) {
        for(DeviceObserver observer : observers) {
            observer.onDeviceEvent(this, eventDescription);
        }
    }

    @Override
    public void notifyObservers(LogType eventType, String eventDescription) {
        for(DeviceObserver observer : observers) {
            observer.onDeviceEvent(this, eventType, eventDescription);
        }
    }
}
