package com.motorola.actions.sleepPattern.repository;

import com.motorola.actions.sleepPattern.data.AccelDao;
import com.motorola.actions.sleepPattern.data.PreprocessingDao;
import com.motorola.actions.sleepPattern.data.PreviewDao;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SleepPatternDataSource_Factory implements Factory<SleepPatternDataSource> {
    private final Provider<AccelDao> accelDaoProvider;
    private final Provider<PreprocessingDao> preprocessingDaoProvider;
    private final Provider<PreviewDao> previewDaoProvider;

    public SleepPatternDataSource_Factory(Provider<PreviewDao> provider, Provider<PreprocessingDao> provider2, Provider<AccelDao> provider3) {
        this.previewDaoProvider = provider;
        this.preprocessingDaoProvider = provider2;
        this.accelDaoProvider = provider3;
    }

    public SleepPatternDataSource get() {
        return new SleepPatternDataSource((PreviewDao) this.previewDaoProvider.get(), (PreprocessingDao) this.preprocessingDaoProvider.get(), (AccelDao) this.accelDaoProvider.get());
    }

    public static Factory<SleepPatternDataSource> create(Provider<PreviewDao> provider, Provider<PreprocessingDao> provider2, Provider<AccelDao> provider3) {
        return new SleepPatternDataSource_Factory(provider, provider2, provider3);
    }
}
