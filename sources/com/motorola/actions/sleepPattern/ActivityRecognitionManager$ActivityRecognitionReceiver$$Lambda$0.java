package com.motorola.actions.sleepPattern;

import java.util.function.BiFunction;

final /* synthetic */ class ActivityRecognitionManager$ActivityRecognitionReceiver$$Lambda$0 implements BiFunction {
    static final BiFunction $instance = new ActivityRecognitionManager$ActivityRecognitionReceiver$$Lambda$0();

    private ActivityRecognitionManager$ActivityRecognitionReceiver$$Lambda$0() {
    }

    public Object apply(Object obj, Object obj2) {
        return Integer.valueOf(Integer.sum(((Integer) obj).intValue(), ((Integer) obj2).intValue()));
    }
}
