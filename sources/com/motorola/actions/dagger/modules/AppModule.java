package com.motorola.actions.dagger.modules;

import android.app.Application;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    /* access modifiers changed from: 0000 */
    @Singleton
    @Provides
    public Application providesApplication() {
        return this.mApplication;
    }
}
