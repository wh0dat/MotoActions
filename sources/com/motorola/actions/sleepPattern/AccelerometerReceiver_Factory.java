package com.motorola.actions.sleepPattern;

import android.app.Application;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class AccelerometerReceiver_Factory implements Factory<AccelerometerReceiver> {
    private final Provider<Application> contextProvider;
    private final Provider<SleepPatternRepository> sleepPatternRepositoryProvider;

    public AccelerometerReceiver_Factory(Provider<Application> provider, Provider<SleepPatternRepository> provider2) {
        this.contextProvider = provider;
        this.sleepPatternRepositoryProvider = provider2;
    }

    public AccelerometerReceiver get() {
        return new AccelerometerReceiver((Application) this.contextProvider.get(), (SleepPatternRepository) this.sleepPatternRepositoryProvider.get());
    }

    public static Factory<AccelerometerReceiver> create(Provider<Application> provider, Provider<SleepPatternRepository> provider2) {
        return new AccelerometerReceiver_Factory(provider, provider2);
    }

    public static AccelerometerReceiver newAccelerometerReceiver(Application application, SleepPatternRepository sleepPatternRepository) {
        return new AccelerometerReceiver(application, sleepPatternRepository);
    }
}
