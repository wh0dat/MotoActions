package com.motorola.actions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.utils.MALogger;

final class UserSwitchReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(UserSwitchReceiver.class);
    private boolean mRegistered;

    private static class SingletonHolder {
        static final UserSwitchReceiver INSTANCE = new UserSwitchReceiver();

        private SingletonHolder() {
        }
    }

    private UserSwitchReceiver() {
    }

    public static UserSwitchReceiver getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void onReceive(Context context, Intent intent) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("intent = ");
        sb.append(intent);
        mALogger.mo11957d(sb.toString());
        if (intent != null) {
            boolean equals = "android.intent.action.USER_BACKGROUND".equals(intent.getAction());
            int intExtra = intent.getIntExtra("android.intent.extra.user_handle", -1);
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Switch received. User ");
            sb2.append(intExtra);
            sb2.append(" sent background = ");
            sb2.append(equals);
            mALogger2.mo11957d(sb2.toString());
            if (equals) {
                BootReceiver.stopAllFeatures(context);
                PackageStatusReceiver.getInstance().unregister();
                return;
            }
            BootReceiver.startAllFeatures(context, ServiceStartReason.USER_SWITCH_FOREGROUND);
            PackageStatusReceiver.getInstance().register();
        }
    }

    /* access modifiers changed from: 0000 */
    public void register() {
        if (!this.mRegistered) {
            LOGGER.mo11957d("Register user switch receiver");
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_FOREGROUND");
            intentFilter.addAction("android.intent.action.USER_BACKGROUND");
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }
    }
}
