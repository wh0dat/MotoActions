package com.motorola.actions.dagger.modules;

import com.motorola.actions.sleepPattern.data.AccelDao;
import com.motorola.actions.sleepPattern.repository.SleepPatternDatabase;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SleepPatternModule_ProvidesAccelDaoFactory implements Factory<AccelDao> {
    private final SleepPatternModule module;
    private final Provider<SleepPatternDatabase> sleepPatternDatabaseProvider;

    public SleepPatternModule_ProvidesAccelDaoFactory(SleepPatternModule sleepPatternModule, Provider<SleepPatternDatabase> provider) {
        this.module = sleepPatternModule;
        this.sleepPatternDatabaseProvider = provider;
    }

    public AccelDao get() {
        return (AccelDao) Preconditions.checkNotNull(this.module.providesAccelDao((SleepPatternDatabase) this.sleepPatternDatabaseProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<AccelDao> create(SleepPatternModule sleepPatternModule, Provider<SleepPatternDatabase> provider) {
        return new SleepPatternModule_ProvidesAccelDaoFactory(sleepPatternModule, provider);
    }

    public static AccelDao proxyProvidesAccelDao(SleepPatternModule sleepPatternModule, SleepPatternDatabase sleepPatternDatabase) {
        return sleepPatternModule.providesAccelDao(sleepPatternDatabase);
    }
}
