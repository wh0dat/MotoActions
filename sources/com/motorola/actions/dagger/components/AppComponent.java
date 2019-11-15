package com.motorola.actions.dagger.components;

import com.motorola.actions.dagger.modules.AppModule;
import com.motorola.actions.dagger.modules.SleepPatternModule;
import com.motorola.actions.debug.DebugFragment;
import com.motorola.actions.sleepPattern.SleepPatternService;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class, SleepPatternModule.class})
public interface AppComponent {
    void inject(DebugFragment debugFragment);

    void inject(SleepPatternService sleepPatternService);
}
