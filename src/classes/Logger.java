package classes;

import abstracts.SmartDevice;
import classes.house.Room;
import enums.LogType;
import interfaces.DeviceLogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger implements DeviceLogger {

    private String filePath;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Logger() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        this.filePath = Paths.get(System.getProperty("user.dir"), "log_" + timestamp + ".tsv").toString();
        initializeFileWithHeader();
    }

    private void initializeFileWithHeader() {
        File file = new File(filePath);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                String timestamp = LocalDateTime.now().format(formatter);
                writer.write("# Log start: " + timestamp);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Błąd przy tworzeniu pliku logu: " + e.getMessage());
            }
        }
    }

    @Override
    public <T extends SmartDevice> void log(SmartDevice device, LogType category, String message) {
        String timestamp = LocalDateTime.now().format(formatter);

        String roomName = "-";
        Room room = device.getRoom();
        if (room != null) {
            roomName = room.getName();
        }

        String line = String.format("%s %s %s %s %s %s", timestamp,
                device.getDeviceName(),
                device.getDeviceType(),
                roomName,
                category,
                message);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Błąd zapisu logu: " + e.getMessage());
        }
    }
}
