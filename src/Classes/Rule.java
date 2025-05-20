package Classes;

import Abstracts.SmartDevice;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Rule<T,Y> {
    Predicate<T> condition;
    SmartDevice testedDevice;
    Consumer<Y> effect;
    SmartDevice resultDevice;


}
