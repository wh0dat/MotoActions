package com.motorola.actions.sleepPattern;

import android.app.Application;
import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ActivityRecognitionManager_Factory implements Factory<ActivityRecognitionManager> {
    private final Provider<Application> contextProvider;
    private final Provider<SleepPatternRepository> sleepPatternRepositoryProvider;

    public ActivityRecognitionManager_Factory(Provider<Application> provider, Provider<SleepPatternRepository> provider2) {
        this.contextProvider = provider;
        this.sleepPatternRepositoryProvider = provider2;
    }

    public ActivityRecognitionManager get() {
        return new ActivityRecognitionManager((Application) this.contextProvider.get(), (SleepPatternRepository) this.sleepPatternRepositoryProvider.get());
    }

    public static Factory<ActivityRecognitionManager> create(Provider<Application> provider, Provider<SleepPatternRepository> provider2) {
        return new ActivityRecognitionManager_Factory(provider, provider2);
    }

    public static ActivityRecognitionManager newActivityRecognitionManager(Application application, SleepPatternRepository sleepPatternRepository) {
        return new ActivityRecognitionManager(application, sleepPatternRepository);
    }
}
