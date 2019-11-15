package com.motorola.actions.dagger.modules;

import com.motorola.actions.sleepPattern.data.PreprocessingDao;
import com.motorola.actions.sleepPattern.repository.SleepPatternDatabase;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SleepPatternModule_ProvidesPreprocessingDaoFactory implements Factory<PreprocessingDao> {
    private final SleepPatternModule module;
    private final Provider<SleepPatternDatabase> sleepPatternDatabaseProvider;

    public SleepPatternModule_ProvidesPreprocessingDaoFactory(SleepPatternModule sleepPatternModule, Provider<SleepPatternDatabase> provider) {
        this.module = sleepPatternModule;
        this.sleepPatternDatabaseProvider = provider;
    }

    public PreprocessingDao get() {
        return (PreprocessingDao) Preconditions.checkNotNull(this.module.providesPreprocessingDao((SleepPatternDatabase) this.sleepPatternDatabaseProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<PreprocessingDao> create(SleepPatternModule sleepPatternModule, Provider<SleepPatternDatabase> provider) {
        return new SleepPatternModule_ProvidesPreprocessingDaoFactory(sleepPatternModule, provider);
    }

    public static PreprocessingDao proxyProvidesPreprocessingDao(SleepPatternModule sleepPatternModule, SleepPatternDatabase sleepPatternDatabase) {
        return sleepPatternModule.providesPreprocessingDao(sleepPatternDatabase);
    }
}
