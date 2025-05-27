package classes.devices;

import abstracts.SmartDevice;
import enums.DeviceStatus;
import enums.DeviceType;
import interfaces.DeviceObserver;
import interfaces.ObservableDevice;
import interfaces.Switchable;

public class InfoTablet extends SmartDevice implements DeviceObserver, Switchable {

    private Thread thread;
    private boolean running = false;

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
    public void turnOn() {
        if (!running) {
            running = true;
            this.thread = createThread();
            thread.start();
        }
        super.setStatus(DeviceStatus.ON);
    }

    @Override
    public void turnOff() {
        running = false;
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
        super.setStatus(DeviceStatus.OFF);
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
}
