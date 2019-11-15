package com.motorola.actions.attentivedisplay.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.motorola.actions.SharedPreferenceManager;

public final class AttentiveDisplayHelper {
    public static final int ID_CAMERA_PERMISSION = 1;
    public static final int ID_CAMERA_PERMISSION_DENIED = 2;
    public static final int ID_CAMERA_PERMISSION_TUTORIAL = 4;
    public static final int ID_DRAW_OVER_PERMISSION = 3;
    public static final String STAY_ON_KEY = "ad_stay_on";

    private AttentiveDisplayHelper() {
    }

    public static void openOverlayPermissionSetting(@NonNull Activity activity) {
        Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
        StringBuilder sb = new StringBuilder();
        sb.append("package:");
        sb.append(activity.getPackageName());
        intent.setData(Uri.parse(sb.toString()));
        activity.startActivity(intent);
    }

    public static void openAppSettings(@NonNull Activity activity) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        StringBuilder sb = new StringBuilder();
        sb.append("package:");
        sb.append(activity.getPackageName());
        intent.setData(Uri.parse(sb.toString()));
        activity.startActivity(intent);
    }

    public static boolean isEnabled() {
        return SharedPreferenceManager.getBoolean(STAY_ON_KEY, false);
    }
}
