package com.motorola.actions.microScreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.motorola.actions.microScreen.instrumentation.MicroScreenInstrumentation;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemMicroscreen;
import com.motorola.actions.utils.MALogger;

public class MicroScreenReceiver extends BroadcastReceiver {
    private static final String EXTRA_LEAVE_MODE = "leaveMicroScreenMode";
    private static final String EXTRA_LEAVE_MODE_CONTENT_OVERLAY_PERMISSON = "overlayPermissionOff";
    static final String EXTRA_LEAVE_MODE_OTHER = "O";
    public static final String EXTRA_OVERLAY_PERMISSION_STATUS = "overlayPermissionStatus";
    private static final MALogger LOGGER = new MALogger(MicroScreenReceiver.class);
    public static final String MOTO_ACTIONS_MICROSCREEN_RECEIVER_ACTION = "com.motorola.actions.microScreen.MICROSCREEN_EXIT_RECEIVER";
    public static final String MOTO_ACTION_MICROSCREEN_OVERLAY_PERMISSION_STATUS = "com.motorola.actions.microScreen.MOTO_ACTION_MICROSCREEN_OVERLAY_PERMISSION_STATUS";
    private static boolean sCheckingPermissionFromMoto = false;

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            if (MOTO_ACTIONS_MICROSCREEN_RECEIVER_ACTION.equals(intent.getAction())) {
                String string = intent.getExtras().getString(EXTRA_LEAVE_MODE, null);
                LOGGER.mo11957d("MOTO_ACTION_MICROSCREEN_RECEIVER_ACTION received");
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("leaveMicroScreenMode = ");
                sb.append(string);
                mALogger.mo11957d(sb.toString());
                if (!TextUtils.isEmpty(string)) {
                    if (string.equals(EXTRA_LEAVE_MODE_CONTENT_OVERLAY_PERMISSON) || (string.equals(EXTRA_LEAVE_MODE_OTHER) && sCheckingPermissionFromMoto)) {
                        MicroScreenNotificationManager.notifyLostPermission();
                        MicroScreenService.stopMicroScreenService();
                        ActionsSettingsProvider.notifyChange(ContainerProviderItemMicroscreen.TABLE_NAME);
                    } else {
                        MicroScreenNotificationManager.dismissNotification();
                        MicroScreenInstrumentation.registerMicroScreenOff(string);
                    }
                }
            }
            setCheckingPermissionFromMoto(false);
        }
    }

    public static void setCheckingPermissionFromMoto(boolean z) {
        sCheckingPermissionFromMoto = z;
    }
}
