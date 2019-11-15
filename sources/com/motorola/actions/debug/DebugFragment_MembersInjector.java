package com.motorola.actions.debug;

import com.motorola.actions.sleepPattern.repository.SleepPatternRepository;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class DebugFragment_MembersInjector implements MembersInjector<DebugFragment> {
    private final Provider<SleepPatternRepository> mSleepPatternRepositoryProvider;

    public DebugFragment_MembersInjector(Provider<SleepPatternRepository> provider) {
        this.mSleepPatternRepositoryProvider = provider;
    }

    public static MembersInjector<DebugFragment> create(Provider<SleepPatternRepository> provider) {
        return new DebugFragment_MembersInjector(provider);
    }

    public void injectMembers(DebugFragment debugFragment) {
        injectMSleepPatternRepository(debugFragment, (SleepPatternRepository) this.mSleepPatternRepositoryProvider.get());
    }

    public static void injectMSleepPatternRepository(DebugFragment debugFragment, SleepPatternRepository sleepPatternRepository) {
        debugFragment.mSleepPatternRepository = sleepPatternRepository;
    }
}
