package com.motorola.actions.dagger.modules;

import android.app.Application;
import com.motorola.actions.sleepPattern.SleepPatternSystemAccess;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class SleepPatternModule_ProvidesSleepPatternSystemAccessFactory implements Factory<SleepPatternSystemAccess> {
    private final Provider<Application> applicationProvider;
    private final SleepPatternModule module;

    public SleepPatternModule_ProvidesSleepPatternSystemAccessFactory(SleepPatternModule sleepPatternModule, Provider<Application> provider) {
        this.module = sleepPatternModule;
        this.applicationProvider = provider;
    }

    public SleepPatternSystemAccess get() {
        return (SleepPatternSystemAccess) Preconditions.checkNotNull(this.module.providesSleepPatternSystemAccess((Application) this.applicationProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<SleepPatternSystemAccess> create(SleepPatternModule sleepPatternModule, Provider<Application> provider) {
        return new SleepPatternModule_ProvidesSleepPatternSystemAccessFactory(sleepPatternModule, provider);
    }

    public static SleepPatternSystemAccess proxyProvidesSleepPatternSystemAccess(SleepPatternModule sleepPatternModule, Application application) {
        return sleepPatternModule.providesSleepPatternSystemAccess(application);
    }
}
