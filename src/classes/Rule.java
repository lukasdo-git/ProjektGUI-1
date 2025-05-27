package classes;

import abstracts.SmartDevice;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Rule<T extends SmartDevice> {
    private Predicate<T> condition;
    private Consumer<T> action;
    private T actionDevice;

    public Rule(Predicate<T> condition, Consumer<T> action, T actionDevice) {
        this.condition = condition;
        this.action = action;
        this.actionDevice = actionDevice;
    }

    public boolean test() {
        return condition.test(actionDevice);
    }

    public void execute() {
        if(test()) action.accept(actionDevice);
    }

    public void forceExecute() {
        action.accept(actionDevice);
    }

    public T getActionDevice() {
        return actionDevice;
    }
}
