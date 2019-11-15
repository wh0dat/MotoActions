package com.motorola.actions.checkin;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.motorola.actions.BuildConfig;
import com.motorola.actions.reflect.CheckinEventProxy;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class CommonCheckinAttributes {
    private static final String KEY_APK_VER = "APKVER";
    private static final String KEY_DAILY_ENABLE_SOURCE = "en_by";
    public static final String KEY_DAILY_ENABLE_SOURCE_TYPE_DEFAULT = "d";
    public static final String KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP = "m";
    public static final String KEY_DAILY_ENABLE_SOURCE_TYPE_SETTINGS = "s";
    public static final String KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL = "t";
    private static final String KEY_DDATE = "ddate";
    public static final String KEY_ENABLED = "en";

    private CommonCheckinAttributes() {
    }

    @SuppressLint({"SimpleDateFormat"})
    private static void addDDate(CheckinEventProxy checkinEventProxy) {
        checkinEventProxy.setValue(KEY_DDATE, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    private static void addEnabled(CheckinEventProxy checkinEventProxy, boolean z) {
        checkinEventProxy.setValue(KEY_ENABLED, z ? "1" : "0");
    }

    public static void addCommonDailyAttributes(CheckinEventProxy checkinEventProxy, boolean z, @Nullable String str) {
        addDDate(checkinEventProxy);
        addEnabled(checkinEventProxy, z);
        addApkVer(checkinEventProxy);
        addSourceEnable(checkinEventProxy, z, str);
    }

    public static void addApkVer(CheckinContainer checkinContainer) {
        checkinContainer.setValue(KEY_APK_VER, (int) BuildConfig.VERSION_CODE);
    }

    private static void addSourceEnable(CheckinEventProxy checkinEventProxy, boolean z, @Nullable String str) {
        if (z && !TextUtils.isEmpty(str)) {
            checkinEventProxy.setValue(KEY_DAILY_ENABLE_SOURCE, str);
        }
    }
}
