package com.motorola.actions.dagger.modules;

import com.motorola.actions.sleepPattern.repository.SleepPatternDatabase;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class SleepPatternModule_ProvidesSleepPatternDatabaseFactory implements Factory<SleepPatternDatabase> {
    private final SleepPatternModule module;

    public SleepPatternModule_ProvidesSleepPatternDatabaseFactory(SleepPatternModule sleepPatternModule) {
        this.module = sleepPatternModule;
    }

    public SleepPatternDatabase get() {
        return (SleepPatternDatabase) Preconditions.checkNotNull(this.module.providesSleepPatternDatabase(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<SleepPatternDatabase> create(SleepPatternModule sleepPatternModule) {
        return new SleepPatternModule_ProvidesSleepPatternDatabaseFactory(sleepPatternModule);
    }

    public static SleepPatternDatabase proxyProvidesSleepPatternDatabase(SleepPatternModule sleepPatternModule) {
        return sleepPatternModule.providesSleepPatternDatabase();
    }
}
