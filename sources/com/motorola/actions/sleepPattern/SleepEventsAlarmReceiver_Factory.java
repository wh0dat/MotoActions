package com.motorola.actions.sleepPattern;

import dagger.internal.Factory;
import javax.inject.Provider;

public final class SleepEventsAlarmReceiver_Factory implements Factory<SleepEventsAlarmReceiver> {
    private final Provider<ProcessSleepEventThread> processSleepEventThreadProvider;

    public SleepEventsAlarmReceiver_Factory(Provider<ProcessSleepEventThread> provider) {
        this.processSleepEventThreadProvider = provider;
    }

    public SleepEventsAlarmReceiver get() {
        return new SleepEventsAlarmReceiver((ProcessSleepEventThread) this.processSleepEventThreadProvider.get());
    }

    public static Factory<SleepEventsAlarmReceiver> create(Provider<ProcessSleepEventThread> provider) {
        return new SleepEventsAlarmReceiver_Factory(provider);
    }

    public static SleepEventsAlarmReceiver newSleepEventsAlarmReceiver(ProcessSleepEventThread processSleepEventThread) {
        return new SleepEventsAlarmReceiver(processSleepEventThread);
    }
}
