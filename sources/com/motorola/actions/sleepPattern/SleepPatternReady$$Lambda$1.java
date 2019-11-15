package com.motorola.actions.sleepPattern;

import java.util.function.ToDoubleFunction;

final /* synthetic */ class SleepPatternReady$$Lambda$1 implements ToDoubleFunction {
    static final ToDoubleFunction $instance = new SleepPatternReady$$Lambda$1();

    private SleepPatternReady$$Lambda$1() {
    }

    public double applyAsDouble(Object obj) {
        return ((PreprocessingInput) obj).getFrequency();
    }
}
