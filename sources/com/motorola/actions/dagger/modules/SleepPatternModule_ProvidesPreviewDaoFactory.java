package com.motorola.actions.dagger.modules;

import com.motorola.actions.sleepPattern.data.PreviewDao;
import com.motorola.actions.sleepPattern.repository.SleepPatternDatabase;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SleepPatternModule_ProvidesPreviewDaoFactory implements Factory<PreviewDao> {
    private final SleepPatternModule module;
    private final Provider<SleepPatternDatabase> sleepPatternDatabaseProvider;

    public SleepPatternModule_ProvidesPreviewDaoFactory(SleepPatternModule sleepPatternModule, Provider<SleepPatternDatabase> provider) {
        this.module = sleepPatternModule;
        this.sleepPatternDatabaseProvider = provider;
    }

    public PreviewDao get() {
        return (PreviewDao) Preconditions.checkNotNull(this.module.providesPreviewDao((SleepPatternDatabase) this.sleepPatternDatabaseProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<PreviewDao> create(SleepPatternModule sleepPatternModule, Provider<SleepPatternDatabase> provider) {
        return new SleepPatternModule_ProvidesPreviewDaoFactory(sleepPatternModule, provider);
    }

    public static PreviewDao proxyProvidesPreviewDao(SleepPatternModule sleepPatternModule, SleepPatternDatabase sleepPatternDatabase) {
        return sleepPatternModule.providesPreviewDao(sleepPatternDatabase);
    }
}
