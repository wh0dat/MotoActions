package com.motorola.actions;

import android.app.Application;
import android.content.Context;

public class ActionsApplication extends Application {
    public void onCreate() {
        super.onCreate();
        ActionsApplicationContext.getInstance().initialize(this);
    }

    public static Context getAppContext() {
        return ActionsApplicationContext.getInstance().getApplicationContext();
    }
}
