package com.motorola.actions.sleepPattern;

import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.function.IntFunction;

final /* synthetic */ class SleepPatternReady$$Lambda$0 implements IntFunction {
    private final List arg$1;
    private final DoubleAdder arg$2;

    SleepPatternReady$$Lambda$0(List list, DoubleAdder doubleAdder) {
        this.arg$1 = list;
        this.arg$2 = doubleAdder;
    }

    public Object apply(int i) {
        return SleepPatternReady.lambda$isSlotsAcceptableDifference$0$SleepPatternReady(this.arg$1, this.arg$2, i);
    }
}
