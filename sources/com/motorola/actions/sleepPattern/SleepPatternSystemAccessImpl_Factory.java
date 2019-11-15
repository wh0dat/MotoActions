package com.motorola.actions.sleepPattern;

import android.app.Application;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class SleepPatternSystemAccessImpl_Factory implements Factory<SleepPatternSystemAccessImpl> {
    private final Provider<Application> applicationProvider;

    public SleepPatternSystemAccessImpl_Factory(Provider<Application> provider) {
        this.applicationProvider = provider;
    }

    public SleepPatternSystemAccessImpl get() {
        return new SleepPatternSystemAccessImpl((Application) this.applicationProvider.get());
    }

    public static Factory<SleepPatternSystemAccessImpl> create(Provider<Application> provider) {
        return new SleepPatternSystemAccessImpl_Factory(provider);
    }
}
