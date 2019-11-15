package com.motorola.actions;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.motorola.actions.utils.MALogger;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferenceManager {
    private static final MALogger LOGGER = new MALogger(SharedPreferenceManager.class);
    private static final String MOTO_ACTIONS_SHARED_PREFERENCES_NAME = "com.motorola.actions_preferences";
    private static final List<String> SHARED_PREFERENCES_MOVE_LIST = new ArrayList();
    @SuppressLint({"StaticFieldLeak"})
    private static Context sDirectBootContext;
    private static boolean sIsUnitTest;

    public static void setContextForTesting(Context context) {
        sDirectBootContext = context;
        sIsUnitTest = true;
    }

    private static void moveSharedPreference(@NonNull String str) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Moving ");
        sb.append(str);
        sb.append(" to Direct Boot area");
        mALogger.mo11957d(sb.toString());
        if (!sDirectBootContext.moveSharedPreferencesFrom(ActionsApplication.getAppContext(), str)) {
            LOGGER.mo11957d("Failed to migrate shared preferences");
        } else {
            LOGGER.mo11957d("Success to move");
        }
    }

    private static boolean isAlreadyMoved(String str) {
        if (sIsUnitTest) {
            return true;
        }
        return SHARED_PREFERENCES_MOVE_LIST.contains(str);
    }

    private static void addToMovedList(String str) {
        if (!SHARED_PREFERENCES_MOVE_LIST.contains(str)) {
            SHARED_PREFERENCES_MOVE_LIST.add(str);
        }
    }

    public static SharedPreferences getSharedPreferences(String str) {
        SharedPreferences sharedPreferences;
        String str2 = str == null ? MOTO_ACTIONS_SHARED_PREFERENCES_NAME : str;
        if (!sIsUnitTest) {
            if (!MultiUserManager.isSystemUser()) {
                LOGGER.mo11957d("Not System User, Setting context for other users");
                sDirectBootContext = ActionsApplication.getAppContext();
            } else if (!isAlreadyMoved(str2)) {
                if (sDirectBootContext == null) {
                    LOGGER.mo11957d("System User, setting Direct Boot Context");
                    sDirectBootContext = ActionsApplication.getAppContext().createDeviceProtectedStorageContext();
                }
                moveSharedPreference(str2);
                addToMovedList(str2);
            }
        }
        try {
            if (TextUtils.equals(str2, MOTO_ACTIONS_SHARED_PREFERENCES_NAME)) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(sDirectBootContext);
            } else {
                sharedPreferences = sDirectBootContext.getSharedPreferences(str, 0);
            }
            return sharedPreferences;
        } catch (IllegalStateException e) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Error to get the SharedPreferences, e=");
            sb.append(e);
            mALogger.mo11959e(sb.toString());
            return null;
        }
    }

    public static void remove(String str, String str2) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        if (sharedPreferences != null) {
            sharedPreferences.edit().remove(str2).apply();
        }
    }

    public static void remove(String str) {
        remove(null, str);
    }

    public static boolean contains(String str, String str2) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        if (sharedPreferences != null) {
            return sharedPreferences.contains(str2);
        }
        return false;
    }

    public static boolean contains(String str) {
        return contains(null, str);
    }

    public static boolean getBoolean(String str, String str2, boolean z) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        return sharedPreferences != null ? sharedPreferences.getBoolean(str2, z) : z;
    }

    public static boolean getBoolean(String str, boolean z) {
        return getBoolean(null, str, z);
    }

    public static int getInt(String str, String str2, int i) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        return sharedPreferences != null ? sharedPreferences.getInt(str2, i) : i;
    }

    public static int getInt(String str, int i) {
        return getInt(null, str, i);
    }

    public static long getLong(String str, String str2, long j) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        return sharedPreferences != null ? sharedPreferences.getLong(str2, j) : j;
    }

    public static long getLong(String str, long j) {
        return getLong(null, str, j);
    }

    public static float getFloat(String str, String str2, float f) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        return sharedPreferences != null ? sharedPreferences.getFloat(str2, f) : f;
    }

    public static float getFloat(String str, float f) {
        return getFloat(null, str, f);
    }

    public static String getString(String str, String str2, String str3) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        return sharedPreferences != null ? sharedPreferences.getString(str2, str3) : str3;
    }

    public static String getString(String str, String str2) {
        return getString(null, str, str2);
    }

    public static void putBoolean(String str, String str2, boolean z) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putBoolean(str2, z).apply();
        }
    }

    public static void putBoolean(String str, boolean z) {
        putBoolean(null, str, z);
    }

    public static void putInt(String str, String str2, int i) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putInt(str2, i).apply();
        }
    }

    public static void putInt(String str, int i) {
        putInt(null, str, i);
    }

    public static void putLong(String str, String str2, long j) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putLong(str2, j).apply();
        }
    }

    public static void putLong(String str, long j) {
        putLong(null, str, j);
    }

    public static void putFloat(String str, String str2, float f) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putFloat(str2, f).apply();
        }
    }

    public static void putFloat(String str, float f) {
        putFloat(null, str, f);
    }

    public static void putString(String str, String str2, String str3) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        if (sharedPreferences != null) {
            sharedPreferences.edit().putString(str2, str3).apply();
        }
    }

    public static void putString(String str, String str2) {
        putString(null, str, str2);
    }

    public static void clear(String str) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        if (sharedPreferences != null) {
            sharedPreferences.edit().clear().apply();
        }
    }

    public static void clear() {
        clear(null);
    }

    public static void registerOnSharedPreferenceChangeListener(String str, OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        if (onSharedPreferenceChangeListener != null && sharedPreferences != null) {
            sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        }
    }

    public static void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        registerOnSharedPreferenceChangeListener(null, onSharedPreferenceChangeListener);
    }

    public static void unregisterOnSharedPreferenceChangeListener(String str, OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        SharedPreferences sharedPreferences = getSharedPreferences(str);
        if (onSharedPreferenceChangeListener != null && sharedPreferences != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
        }
    }

    public static void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        unregisterOnSharedPreferenceChangeListener(null, onSharedPreferenceChangeListener);
    }
}
