package com.motorola.actions.dagger.modules;

import com.motorola.actions.sleepPattern.instrumentation.SleepPatternAnalyticsAccess;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class SleepPatternModule_ProvidesSleepPatternAnalyticsAccessFactory implements Factory<SleepPatternAnalyticsAccess> {
    private final SleepPatternModule module;

    public SleepPatternModule_ProvidesSleepPatternAnalyticsAccessFactory(SleepPatternModule sleepPatternModule) {
        this.module = sleepPatternModule;
    }

    public SleepPatternAnalyticsAccess get() {
        return (SleepPatternAnalyticsAccess) Preconditions.checkNotNull(this.module.providesSleepPatternAnalyticsAccess(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<SleepPatternAnalyticsAccess> create(SleepPatternModule sleepPatternModule) {
        return new SleepPatternModule_ProvidesSleepPatternAnalyticsAccessFactory(sleepPatternModule);
    }

    public static SleepPatternAnalyticsAccess proxyProvidesSleepPatternAnalyticsAccess(SleepPatternModule sleepPatternModule) {
        return sleepPatternModule.providesSleepPatternAnalyticsAccess();
    }
}
