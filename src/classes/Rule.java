package classes;

import abstracts.SmartDevice;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Rule<T extends SmartDevice> {
    private final Predicate<T> condition;
    private final Consumer<T> action;
    private final T device;

    public Rule(Predicate<T> condition, Consumer<T> action, T resultDevice) {
        this.condition = condition;
        this.action = action;
        this.device = resultDevice;
    }

    public boolean test() {
        return condition.test(device);
    }

    public void execute() {
        if(test()) action.accept(device);
    }

    public T getDevice() {
        return device;
    }
}
