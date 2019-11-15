package com.motorola.actions.dagger.modules;

import android.app.Application;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AppModule_ProvidesApplicationFactory implements Factory<Application> {
    private final AppModule module;

    public AppModule_ProvidesApplicationFactory(AppModule appModule) {
        this.module = appModule;
    }

    public Application get() {
        return (Application) Preconditions.checkNotNull(this.module.providesApplication(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Application> create(AppModule appModule) {
        return new AppModule_ProvidesApplicationFactory(appModule);
    }

    public static Application proxyProvidesApplication(AppModule appModule) {
        return appModule.providesApplication();
    }
}
