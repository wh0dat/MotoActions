package com.motorola.actions.dagger.modules;

import com.motorola.actions.sleepPattern.data.AccelDao;
import com.motorola.actions.sleepPattern.data.PreprocessingDao;
import com.motorola.actions.sleepPattern.data.PreviewDao;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SleepPatternModule_ProvidesSleepPatternRepositoryFactory implements Factory<SleepPatternRepository> {
    private final Provider<AccelDao> accelDaoProvider;
    private final SleepPatternModule module;
    private final Provider<PreprocessingDao> preprocessingDaoProvider;
    private final Provider<PreviewDao> previewDaoProvider;

    public SleepPatternModule_ProvidesSleepPatternRepositoryFactory(SleepPatternModule sleepPatternModule, Provider<AccelDao> provider, Provider<PreprocessingDao> provider2, Provider<PreviewDao> provider3) {
        this.module = sleepPatternModule;
        this.accelDaoProvider = provider;
        this.preprocessingDaoProvider = provider2;
        this.previewDaoProvider = provider3;
    }

    public SleepPatternRepository get() {
        return (SleepPatternRepository) Preconditions.checkNotNull(this.module.providesSleepPatternRepository((AccelDao) this.accelDaoProvider.get(), (PreprocessingDao) this.preprocessingDaoProvider.get(), (PreviewDao) this.previewDaoProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<SleepPatternRepository> create(SleepPatternModule sleepPatternModule, Provider<AccelDao> provider, Provider<PreprocessingDao> provider2, Provider<PreviewDao> provider3) {
        return new SleepPatternModule_ProvidesSleepPatternRepositoryFactory(sleepPatternModule, provider, provider2, provider3);
    }

    public static SleepPatternRepository proxyProvidesSleepPatternRepository(SleepPatternModule sleepPatternModule, AccelDao accelDao, PreprocessingDao preprocessingDao, PreviewDao previewDao) {
        return sleepPatternModule.providesSleepPatternRepository(accelDao, preprocessingDao, previewDao);
    }
}
