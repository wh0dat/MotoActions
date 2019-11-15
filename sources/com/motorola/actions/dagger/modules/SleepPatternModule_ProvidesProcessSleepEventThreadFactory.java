package com.motorola.actions.dagger.modules;

import com.motorola.actions.sleepPattern.AccelerometerReceiver;
import com.motorola.actions.sleepPattern.ActivityRecognitionManager;
import com.motorola.actions.sleepPattern.ProcessSleepEventThread;
import com.motorola.actions.sleepPattern.SleepPatternProcessing;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SleepPatternModule_ProvidesProcessSleepEventThreadFactory implements Factory<ProcessSleepEventThread> {
    private final Provider<AccelerometerReceiver> accelerometerReceiverProvider;
    private final Provider<ActivityRecognitionManager> activityRecognitionManagerProvider;
    private final SleepPatternModule module;
    private final Provider<SleepPatternProcessing> sleepPatternProcessingProvider;

    public SleepPatternModule_ProvidesProcessSleepEventThreadFactory(SleepPatternModule sleepPatternModule, Provider<SleepPatternProcessing> provider, Provider<AccelerometerReceiver> provider2, Provider<ActivityRecognitionManager> provider3) {
        this.module = sleepPatternModule;
        this.sleepPatternProcessingProvider = provider;
        this.accelerometerReceiverProvider = provider2;
        this.activityRecognitionManagerProvider = provider3;
    }

    public ProcessSleepEventThread get() {
        return (ProcessSleepEventThread) Preconditions.checkNotNull(this.module.providesProcessSleepEventThread((SleepPatternProcessing) this.sleepPatternProcessingProvider.get(), (AccelerometerReceiver) this.accelerometerReceiverProvider.get(), (ActivityRecognitionManager) this.activityRecognitionManagerProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<ProcessSleepEventThread> create(SleepPatternModule sleepPatternModule, Provider<SleepPatternProcessing> provider, Provider<AccelerometerReceiver> provider2, Provider<ActivityRecognitionManager> provider3) {
        return new SleepPatternModule_ProvidesProcessSleepEventThreadFactory(sleepPatternModule, provider, provider2, provider3);
    }

    public static ProcessSleepEventThread proxyProvidesProcessSleepEventThread(SleepPatternModule sleepPatternModule, SleepPatternProcessing sleepPatternProcessing, AccelerometerReceiver accelerometerReceiver, ActivityRecognitionManager activityRecognitionManager) {
        return sleepPatternModule.providesProcessSleepEventThread(sleepPatternProcessing, accelerometerReceiver, activityRecognitionManager);
    }
}
