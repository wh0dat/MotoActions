package com.motorola.actions.zenmode;

import com.motorola.actions.ActionsApplication;
import com.motorola.actions.reflect.NotificationManagerPrivateProxy;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SettingsWrapper;

public class ZenModeAccess {
    public static final String ACTIONS_PREVIOUS_INTERRUPTION_FILTER = "actions_previous_interruption_filter";
    public static final int EXIT_ZEN_MODE_FAIL = 2;
    public static final int EXIT_ZEN_MODE_SUCCESS = 1;
    private static final String KEY_ZEN_MODE = "zen_mode";
    private static final MALogger LOGGER = new MALogger(ZenModeAccess.class);
    private static final String MOTOROLA_PERMISSION_MANAGE_NOTIFICATIONS = "com.motorola.permission.MANAGE_NOTIFICATIONS";
    public static final int SET_INTERRUPTION_FILTER_ALREADY_SET = 2;
    public static final int SET_INTERRUPTION_FILTER_FAIL = 3;
    public static final int SET_INTERRUPTION_FILTER_SUCCESS = 1;
    private static final int ZEN_MODE_IMPORTANT_INTERRUPTIONS = 1;
    private static final int ZEN_MODE_OFF = 0;

    public static boolean isZenModeOn() {
        return SettingsWrapper.getGlobalInt(KEY_ZEN_MODE) != 0;
    }

    public static int enterZenMode(int i) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("enterZenMode(");
        sb.append(i);
        sb.append(")");
        mALogger.mo11957d(sb.toString());
        int i2 = 1;
        if (!NotificationManagerPrivateProxy.setZenMode(1)) {
            i2 = 3;
        }
        MALogger mALogger2 = LOGGER;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Set Zen Mode result ");
        sb2.append(i2);
        mALogger2.mo11957d(sb2.toString());
        return i2;
    }

    public static int exitZenMode() {
        LOGGER.mo11957d("exitZenMode()");
        if (!NotificationManagerPrivateProxy.setZenMode(0)) {
            return 2;
        }
        LOGGER.mo11957d("Set Zen Mode to 0");
        return 1;
    }

    public static boolean hasMotorolaManageNotificationsPermission() {
        boolean z = ActionsApplication.getAppContext().checkSelfPermission(MOTOROLA_PERMISSION_MANAGE_NOTIFICATIONS) == 0;
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("hasMotorolaManageNotificationsPermission ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        return z;
    }
}
