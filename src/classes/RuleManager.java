package classes;

import abstracts.SmartDevice;
import classes.house.House;
import classes.house.Room;

import java.util.List;

public class RuleManager {

    private static RuleManager instance;
    private final List<House> houseList;
    private Thread ruleThread;
    private boolean running = true;

    public static RuleManager getInstance(List<House> houseList) {
        if(instance == null) instance = new RuleManager(houseList);
        return instance;
    }

    public RuleManager(List<House> houseList) {
        this.houseList = houseList;
        startThread();
    }

    private void runRuleLoop() {
        while(running) {
            for(House house : houseList) {
                for(Room room : house.getRooms()) {
                    for(Rule<? extends SmartDevice> rule : room.getRules()) {
                        try {
                            rule.execute();
                        } catch(Exception ex) {
                            System.out.println("Błąd przy wykonywaniu reguły " + ex.getMessage());
                        }
                    }
                }
            }
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                running = false;
                break;
            }
        }
    }

    private void startThread() {
        ruleThread = new Thread(() -> {
            while(running) {
                try {
                    runRuleLoop();
                } catch(Exception e) {
                    System.err.println("[RuleManager] Błąd w pętli reguł " + e.getMessage());
                    e.printStackTrace();
                    try {
                        Thread.sleep(2000);
                        System.err.println("[RuleManager] Restartuję pętlę reguł");
                    } catch(InterruptedException ie) {
                        running = false;
                    }
                }
            }
        });
        ruleThread.start();
    }
}
