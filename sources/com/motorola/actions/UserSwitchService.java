package com.motorola.actions;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.motorola.actions.utils.MALogger;

public class UserSwitchService extends Service {
    private static final MALogger LOGGER = new MALogger(UserSwitchService.class);

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        LOGGER.mo11957d("onStart");
        UserSwitchReceiver.getInstance().register();
        return 1;
    }

    public void onDestroy() {
        LOGGER.mo11957d("onDestroy");
        super.onDestroy();
    }

    public static Intent createIntent() {
        return new Intent(ActionsApplication.getAppContext(), UserSwitchService.class);
    }
}
