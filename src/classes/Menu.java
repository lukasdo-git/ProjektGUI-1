package classes;

import classes.house.House;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    private int menu;
    private int chosenHouseNumber;
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
        if(menu == 0) {
            if(houseArrayList.isEmpty()) {
                System.out.println("\nBrak zarejestrowanych domów.\n");

                System.out.println("Dostępne polecenia: (D)odaj dom ");
            } else {
                System.out.println("\nZarejestrowane domy:");
                for(House house : houseArrayList) {
                    System.out.println("["+house.getId()+"] "+house.getName());
                }
                System.out.println("\nDostępne polecenia: (D)odaj dom, (U)suń dom, (Z)mień nazwę domu");
                System.out.println("lub podaj numer domu");
            }


        }
        if(menu == 1) {
            House house = houseArrayList.get(chosenHouseNumber - 1);
            System.out.println("\nZarządzasz teraz domem: ["+ house.getId() +"] "+house.getName());
            if(!house.hasRooms()){
                System.out.println("Brak zarejestrowanych pokoi w tym domu.");
                System.out.println("Dostępne polecenia: (D)odaj pokój");
            }

        }

        System.out.print("\bPodaj polecenie: ");
        String input = scanner.nextLine();
        execute(input);
    }

    private void execute(String input) {
        char command = Character.toLowerCase(input.charAt(0));
        if(menu == 0) {
            if(command == 'd') {
                System.out.println("ID domu: ["+(houseArrayList.size()+1)+"]");
                System.out.print("Nazwa domu: ");
                String name = scanner.nextLine();
                System.out.print("Szerokość geograficzna (12,34567): ");
                float xCoordinate = 0;
                try {
                    xCoordinate = scanner.nextFloat();
                    scanner.nextLine(); // konsumpcja \n
                } catch (InputMismatchException e) {
                    System.out.println("Niepoprawnie podana wartość. Podaj liczbę zmiennoprzecinkową.");
                    execute(""+command);
                }
                System.out.print("Wysokość geograficzna (12,34567): ");
                float yCoordinate = 0;
                try {
                    yCoordinate = scanner.nextFloat();
                    scanner.nextLine(); // konsumpcja \n
                } catch (InputMismatchException e) {
                    System.out.println("Niepoprawnie podana wartość. Podaj liczbę zmiennoprzecinkową");
                    execute(""+command);
                }
                houseArrayList.add(new House(name, houseArrayList.size()+1, xCoordinate, yCoordinate));
                display();
            }
            if(houseArrayList.isEmpty()) return;
            if(command == 'u') {
                System.out.print("Podaj numer domu: ");
                int n = scanner.nextInt();
                scanner.nextLine();
                House house = null;
                try {
                    house = houseArrayList.get(n - 1);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Ten dom nie istnieje!");
                    execute(""+command);
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
                House house = null;
                try {
                    house = houseArrayList.get(n - 1);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Ten dom nie istnieje!");
                    execute(""+command);
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

        if(menu == 1) {
            if(command == 'd') {

            }
        }
    }
}
