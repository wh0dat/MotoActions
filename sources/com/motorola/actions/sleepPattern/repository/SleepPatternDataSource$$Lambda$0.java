package com.motorola.actions.sleepPattern.repository;

import com.motorola.actions.sleepPattern.repository.entities.AccelEntity;
import java.util.function.Function;

final /* synthetic */ class SleepPatternDataSource$$Lambda$0 implements Function {
    static final Function $instance = new SleepPatternDataSource$$Lambda$0();

    private SleepPatternDataSource$$Lambda$0() {
    }

    public Object apply(Object obj) {
        return SleepPatternDataSource.lambda$markDataAsDebugData$0$SleepPatternDataSource((AccelEntity) obj);
    }
}
