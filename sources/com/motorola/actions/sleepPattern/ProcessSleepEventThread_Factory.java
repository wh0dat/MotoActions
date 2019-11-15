package com.motorola.actions.sleepPattern;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class ProcessSleepEventThread_Factory implements Factory<ProcessSleepEventThread> {
    private final Provider<AccelerometerReceiver> accelerometerReceiverProvider;
    private final Provider<ActivityRecognitionManager> activityRecognitionManagerProvider;
    private final Provider<SleepPatternProcessing> sleepPatternProcessingProvider;

    public ProcessSleepEventThread_Factory(Provider<SleepPatternProcessing> provider, Provider<AccelerometerReceiver> provider2, Provider<ActivityRecognitionManager> provider3) {
        this.sleepPatternProcessingProvider = provider;
        this.accelerometerReceiverProvider = provider2;
        this.activityRecognitionManagerProvider = provider3;
    }

    public ProcessSleepEventThread get() {
        return new ProcessSleepEventThread((SleepPatternProcessing) this.sleepPatternProcessingProvider.get(), (AccelerometerReceiver) this.accelerometerReceiverProvider.get(), (ActivityRecognitionManager) this.activityRecognitionManagerProvider.get());
    }

    public static Factory<ProcessSleepEventThread> create(Provider<SleepPatternProcessing> provider, Provider<AccelerometerReceiver> provider2, Provider<ActivityRecognitionManager> provider3) {
        return new ProcessSleepEventThread_Factory(provider, provider2, provider3);
    }
}
