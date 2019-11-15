package com.motorola.actions;

import android.content.Context;

public class ActionsApplicationContext {
    private static ActionsApplicationContext sInstance;
    private ActionsApplication mContext;

    public static ActionsApplicationContext getInstance() {
        if (sInstance == null) {
            sInstance = new ActionsApplicationContext();
        }
        return sInstance;
    }

    public void initialize(ActionsApplication actionsApplication) {
        this.mContext = actionsApplication;
    }

    public Context getApplicationContext() {
        return this.mContext;
    }

    public ActionsApplication getApplication() {
        return this.mContext;
    }
}
