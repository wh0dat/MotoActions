package com.motorola.actions.sleepPattern;

import com.motorola.actions.sleepPattern.instrumentation.SleepPatternAnalyticsAccess;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SleepPatternProcessing_Factory implements Factory<SleepPatternProcessing> {
    private final Provider<SleepPatternAnalyticsAccess> sleepPatternAnalyticsAccessProvider;
    private final Provider<SleepPatternRepository> sleepPatternRepositoryProvider;
    private final Provider<SleepPatternSystemAccess> sleepPatternSystemAccessProvider;

    public SleepPatternProcessing_Factory(Provider<SleepPatternRepository> provider, Provider<SleepPatternSystemAccess> provider2, Provider<SleepPatternAnalyticsAccess> provider3) {
        this.sleepPatternRepositoryProvider = provider;
        this.sleepPatternSystemAccessProvider = provider2;
        this.sleepPatternAnalyticsAccessProvider = provider3;
    }

    public SleepPatternProcessing get() {
        return new SleepPatternProcessing((SleepPatternRepository) this.sleepPatternRepositoryProvider.get(), (SleepPatternSystemAccess) this.sleepPatternSystemAccessProvider.get(), (SleepPatternAnalyticsAccess) this.sleepPatternAnalyticsAccessProvider.get());
    }

    public static Factory<SleepPatternProcessing> create(Provider<SleepPatternRepository> provider, Provider<SleepPatternSystemAccess> provider2, Provider<SleepPatternAnalyticsAccess> provider3) {
        return new SleepPatternProcessing_Factory(provider, provider2, provider3);
    }

    public static SleepPatternProcessing newSleepPatternProcessing(SleepPatternRepository sleepPatternRepository, SleepPatternSystemAccess sleepPatternSystemAccess, SleepPatternAnalyticsAccess sleepPatternAnalyticsAccess) {
        return new SleepPatternProcessing(sleepPatternRepository, sleepPatternSystemAccess, sleepPatternAnalyticsAccess);
    }
}
