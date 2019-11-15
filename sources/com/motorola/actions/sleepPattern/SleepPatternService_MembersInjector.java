package com.motorola.actions.sleepPattern;

import dagger.MembersInjector;
import javax.inject.Provider;

public final class SleepPatternService_MembersInjector implements MembersInjector<SleepPatternService> {
    private final Provider<ProcessSleepEventThread> mProcessSleepEventThreadProvider;
    private final Provider<SleepEventsAlarmReceiver> mSleepEventsAlarmReceiverProvider;

    public SleepPatternService_MembersInjector(Provider<SleepEventsAlarmReceiver> provider, Provider<ProcessSleepEventThread> provider2) {
        this.mSleepEventsAlarmReceiverProvider = provider;
        this.mProcessSleepEventThreadProvider = provider2;
    }

    public static MembersInjector<SleepPatternService> create(Provider<SleepEventsAlarmReceiver> provider, Provider<ProcessSleepEventThread> provider2) {
        return new SleepPatternService_MembersInjector(provider, provider2);
    }

    public void injectMembers(SleepPatternService sleepPatternService) {
        injectMSleepEventsAlarmReceiver(sleepPatternService, (SleepEventsAlarmReceiver) this.mSleepEventsAlarmReceiverProvider.get());
        injectMProcessSleepEventThread(sleepPatternService, (ProcessSleepEventThread) this.mProcessSleepEventThreadProvider.get());
    }

    public static void injectMSleepEventsAlarmReceiver(SleepPatternService sleepPatternService, SleepEventsAlarmReceiver sleepEventsAlarmReceiver) {
        sleepPatternService.mSleepEventsAlarmReceiver = sleepEventsAlarmReceiver;
    }

    public static void injectMProcessSleepEventThread(SleepPatternService sleepPatternService, ProcessSleepEventThread processSleepEventThread) {
        sleepPatternService.mProcessSleepEventThread = processSleepEventThread;
    }
}
