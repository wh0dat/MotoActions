package com.motorola.actions.sleepPattern;

import java.util.function.BiFunction;

final /* synthetic */ class AccelerometerReceiver$$Lambda$1 implements BiFunction {
    static final BiFunction $instance = new AccelerometerReceiver$$Lambda$1();

    private AccelerometerReceiver$$Lambda$1() {
    }

    public Object apply(Object obj, Object obj2) {
        return Integer.valueOf(Integer.sum(((Integer) obj).intValue(), ((Integer) obj2).intValue()));
    }
}
