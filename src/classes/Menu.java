package classes;

import abstracts.SmartDevice;
import classes.devices.Heater;
import classes.devices.TemperatureSensor;
import classes.house.House;
import classes.house.Room;
import enums.DeviceType;
import enums.RoomType;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    private int menu;
    private int chosenHouseNumber;
    private House chosenHouse;
    private int chosenRoomNumber;
    private Room chosenRoom;
    private static ArrayList<House> houseArrayList = new ArrayList<>();
    private boolean running;
    private Scanner scanner = new Scanner(System.in);


    public void run() {
        menu = 0;
        running = true;
        Thread menu = new Thread(()->{
            System.out.println("Symulowany system zarządzania SmartHouse\n");
            while(running) {
                display();
            }
        });
        menu.start();
    }

    private void display() {
        // menu wyboru domków
        if(menu == 0) {
            if(houseArrayList.isEmpty()) {
                System.out.println("\nBrak zarejestrowanych domów.\n");

                System.out.println("Dostępne polecenia: (D)odaj dom ");
            } else {
                System.out.println("\nZarejestrowane domy:");
                for(House house : houseArrayList) {
                    System.out.printf("[%s] \t(%.5f; %.5f)\t %s\n", house.getId(), house.getXCoord(), house.getYCoord(), house.getName());
                }
                System.out.println("\nDostępne polecenia: (D)odaj dom, (U)suń dom, (Z)mień nazwę domu");
                System.out.println("lub podaj numer domu");
            }


        }

        // menu zarządzania pokojami w domku
        if(menu == 1) {
            chosenHouse = houseArrayList.get(chosenHouseNumber - 1);
            System.out.println("\nZarządzasz teraz domem: ["+ chosenHouse.getId() +"] "+chosenHouse.getName());
            if(!chosenHouse.hasRooms()){
                System.out.println("Brak zarejestrowanych pokoi w tym domu.");
                System.out.println("Dostępne polecenia: (W)róć, (D)odaj pokój");
            } else {
                System.out.println("Zarejestrowane pokoje:");
                for(Room room : chosenHouse.getRooms()) {
                    System.out.println("["+room.getId()+"|"+room.getRoomType()+"] "+room.getName());
                }
                System.out.println("Dostępne polecenia: (W)róć, (D)odaj pokój, (U)suń pokój, (Z)mień nazwę pokoju");
                System.out.println("lub podaj numer pokoju");
            }
        }

        // menu zarządzania urządzeniami w pokoju
        if(menu == 2) {
            chosenRoom = chosenHouse.getRooms().get(chosenRoomNumber-1);
            System.out.println("\nZarządzasz teraz pokojem: ["+ chosenRoom.getId() + "|"+chosenRoom.getRoomType()+"] "+chosenRoom.getName());
            if(!chosenRoom.hasDevices()){
                System.out.println("Brak zarejestrowanych urządzeń.");
                System.out.println("Dostępne polecenia: (W)róć, (D)odaj urządzenie");
            } else {
                System.out.println("Zarejestrowane urządzenia:");
                for(SmartDevice device : chosenRoom.getDevices()) {
                    System.out.println("["+device.getDeviceId()+"] "+device.getDeviceName());
                    System.out.println("Dostępne polecenia: (W)róć, (D)odaj urządzenie, (U)suń urządzenie, (Z)mień nazwę urządzenia");
                    System.out.println("lub podaj numer urządzenia (aby symulować w czasie rzeczywistym) ");
                }
            }
        }

        // menu zarządzania regułami automatycznymi
        if(menu == 3) {
            System.out.println("\nZarządzasz regułami w pokoju : ["+chosenRoom.getId()+"] "+chosenRoom.getName());
        }

        System.out.print("\bPodaj polecenie: ");
        String input = scanner.nextLine();
        execute(input);
    }

    private void execute(String input) {
        char command = Character.toLowerCase(input.charAt(0));

        if(menu == 2) {
            if(command == 'w') {
                menu = 1;
            }
            if(command == 'd') {
                System.out.println("ID urządzenia: ["+(chosenRoom.getNumOfDevices()+1)+"]");
                System.out.print("Nazwa urządzenia: ");
                String name = scanner.nextLine();
                System.out.print("Dostępne rodzaje: ");
                for(DeviceType type : DeviceType.values()) {
                    System.out.print(type + " ");
                }
                System.out.print("\nRodzaj urządzenia: ");
                DeviceType type;
                try{
                    type = DeviceType.valueOf(scanner.nextLine().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Niepoprawny rodzaj!");
                    execute(""+command);
                    return;
                }
                newDevice(chosenRoom.getNumOfDevices()+1, name, type);

            }
            if(!chosenRoom.hasDevices()) return;
            if(command == 'u') {
                System.out.println("Podaj numer urządzenia: ");
                int n = Integer.parseInt(scanner.nextLine());
                SmartDevice device;
                try {
                    device = chosenRoom.getDevices().get(n-1);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("To urządzenie nie istnieje!");
                    execute(""+command);
                    return;
                }
                System.out.println("Napewno chcesz usunąć urządzenie '"+device.getDeviceName()+"'?");
                System.out.println("Jeżeli tak, wpisz nazwę urządzenia aby potwierdzić: ");
                String confirm = scanner.nextLine();
                if(confirm.equals(device.getDeviceName())) chosenRoom.getDevices().remove(n - 1);
                else System.out.println("Niezgodność nazw. Nie usunięto pokoju.");
            }
            if(command == 'z') {
                System.out.println("Podaj numer urządzenia: ");
                int n = Integer.parseInt(scanner.nextLine());
                SmartDevice device;
                try {
                    device = chosenRoom.getDevices().get(n-1);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("To urządzenie nie istnieje!");
                    execute(""+command);
                    return;
                }
                System.out.println("Zmieniasz nazwę urządzenia '"+device.getDeviceName()+"'");
                System.out.print("Nazwa urządzenia: ");
                String newName = scanner.nextLine();
                device.setDeviceName(newName);
            }
            if(command == 'r') return; //dodać reguły
            if(Character.isDigit(command)) {
                chosenRoom.getDevices().get(Integer.parseInt(input)-1).setLive();
            }
        }

        if(menu == 1) {
            if(command == 'w') {
                menu = 0;
            }
            if(command == 'd') {
                System.out.println("ID pokoju: ["+(chosenHouse.getNumOfRooms()+1)+"]");
                System.out.print("Nazwa pokoju: ");
                String nazwa = scanner.nextLine();
                System.out.print("Dostępne kategorie: ");
                for( RoomType type : RoomType.values() ) {
                    System.out.print(type+" ");
                }
                System.out.print("\nKategoria pokoju: ");
                RoomType kategoria;
                try {
                    kategoria = RoomType.valueOf(scanner.nextLine().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Niepoprawna kategoria!");
                    execute(""+command);
                    return;
                }
                System.out.print("Temperatura pokojowa dla symulacji ogrzewania (zmiennoprzecinkowa): ");
                double temp = scanner.nextDouble();
                scanner.nextLine();
                chosenHouse.addRoom(new Room(nazwa, chosenHouse.getNumOfRooms()+1, kategoria, temp));
            }
            if(!chosenHouse.hasRooms()) return;
            if(command == 'u') {
                System.out.println("Podaj numer pokoju: ");
                int n = Integer.parseInt(scanner.nextLine());
                Room room;
                try {
                    room = chosenHouse.getRooms().get(n - 1);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Ten pokój nie istnieje!");
                    execute(""+command);
                    return;
                }
                System.out.println("\nNapewno chcesz usunąć pokój '"+room.getName()+"'?");
                System.out.println("Jeżeli tak, wpisz nazwę pokoju aby potwierdzić: ");
                String confirm = scanner.nextLine();
                if(confirm.equals(room.getName())) chosenHouse.getRooms().remove(n - 1);
                else System.out.println("Niezgodność nazw. Nie usunięto pokoju.");
            }
            if(command == 'z') {
                System.out.println("Podaj numer pokoju: ");
                int n = scanner.nextInt();
                scanner.nextLine();
                Room room;
                try {
                    room = chosenHouse.getRooms().get(n - 1);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Ten pokój nie istnieje!");
                    execute(""+command);
                    return;
                }
                System.out.println("Zmieniasz nazwę pokoju '"+room.getName()+"'");
                System.out.print("Nazwa pokoju: ");
                String newName = scanner.nextLine();
                room.setName(newName);
            }
            if(Character.isDigit(command)) {
                menu = 2;
                chosenRoomNumber = Integer.parseInt(input);
            }
        }

        if(menu == 0) {
            if(command == 'd') {
                System.out.println("ID domu: ["+(houseArrayList.size()+1)+"]");
                System.out.print("Nazwa domu: ");
                String name = scanner.nextLine();
                System.out.print("Szerokość geograficzna (12,34567): ");
                float xCoordinate;
                try {
                    xCoordinate = scanner.nextFloat();
                    scanner.nextLine(); // konsumpcja \n
                } catch (InputMismatchException e) {
                    scanner.nextLine(); // konsumpcja \n
                    System.out.println("Niepoprawnie podana wartość. Podaj liczbę zmiennoprzecinkową.");
                    execute(""+command);
                    return;
                }
                System.out.print("Wysokość geograficzna (12,34567): ");
                float yCoordinate;
                try {
                    yCoordinate = scanner.nextFloat();
                    scanner.nextLine(); // konsumpcja \n
                } catch (InputMismatchException e) {
                    scanner.nextLine(); // konsumpcja \n
                    System.out.println("Niepoprawnie podana wartość. Podaj liczbę zmiennoprzecinkową");
                    execute(""+command);
                    return;
                }
                houseArrayList.add(new House(name, houseArrayList.size()+1, xCoordinate, yCoordinate));
                display();
            }
            if(houseArrayList.isEmpty()) return;
            if(command == 'u') {
                System.out.print("Podaj numer domu: ");
                int n = scanner.nextInt();
                scanner.nextLine();
                House house;
                try {
                    house = houseArrayList.get(n - 1);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Ten dom nie istnieje!");
                    execute(""+command);
                    return;
                }
                System.out.println("\nNapewno chcesz usunąć dom '"+house.getName()+"'?");
                System.out.println("Jeżeli tak, wpisz nazwę domu aby potwierdzić: ");
                String confirm = scanner.nextLine();
                if(confirm.equals(house.getName())) houseArrayList.remove(n - 1);
                else System.out.println("Niezgodność nazw. Nie usunięto domu.");
            }
            if(command == 'z') {
                System.out.print("Podaj numer domu: ");
                int n = scanner.nextInt();
                scanner.nextLine();
                House house;
                try {
                    house = houseArrayList.get(n - 1);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Ten dom nie istnieje!");
                    execute(""+command);
                    return;
                }
                System.out.println("Zmieniasz nazwę domu '"+house.getName()+"'");
                System.out.print("Podaj nową nazwę: ");
                String newName = scanner.nextLine();
                house.setName(newName);
            }
            if(Character.isDigit(command)) {
                menu = 1;
                chosenHouseNumber = Integer.parseInt(input);
            }
        }
    }

    private void newDevice(int id, String name, DeviceType type) {
        SmartDevice newDevice = null;
        if(type == DeviceType.TEMPSENSOR) {
            newDevice = new TemperatureSensor(id, name);
        }
        if(type == DeviceType.HEATER) {
            System.out.print("Podaj moc grzewczą grzejnika (W): ");
            int heatingPower = 0;
            boolean repeat = true;
            while(repeat) {
                try {
                    heatingPower = Integer.parseInt(scanner.nextLine());
                    repeat = false;
                } catch (InputMismatchException e) {
                    System.out.println("Niepoprawnie podana moc grzewcza grzejnika!");
                    System.out.println("Musi być liczba całkowita, na przykład 1000");
                }
            }

            newDevice = new Heater(id, name, heatingPower);
        }
        if(type == DeviceType.LIGHTBULB) {
            System.out.println("Podaj kolor na jaki świeci żarówka (H,S,V): ");
            int h = 0;
            double s = 0,v = 0;
            boolean repeat = true;
            while(repeat) {
                String[] input = scanner.nextLine().split(",");
                h = Integer.parseInt(input[0]);
                s = Double.parseDouble(input[1]);
                v = Double.parseDouble(input[2]);
            }

        }
        if(type == DeviceType.OUTLET) {}
        chosenRoom.addDevice(newDevice);
    }
}
