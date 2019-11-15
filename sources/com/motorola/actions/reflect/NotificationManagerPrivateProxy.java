package com.motorola.actions.reflect;

import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.util.ArrayMap;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map.Entry;

public class NotificationManagerPrivateProxy {
    private static final Method GET_ZEN_MODE_CONFIG;
    private static final MALogger LOGGER = new MALogger(NotificationManagerPrivateProxy.class);

    static {
        Method method;
        try {
            method = Class.forName("android.app.NotificationManager").getDeclaredMethod("getZenModeConfig", new Class[0]);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            LOGGER.mo11964w("Reflection error", e);
            method = null;
        }
        GET_ZEN_MODE_CONFIG = method;
    }

    public static boolean isAtLeastOneAutomaticRuleActive(Context context) {
        boolean z;
        try {
            Object invoke = GET_ZEN_MODE_CONFIG.invoke((NotificationManager) context.getSystemService("notification"), new Object[0]);
            z = false;
            for (Entry entry : ((ArrayMap) invoke.getClass().getDeclaredField("automaticRules").get(invoke)).entrySet()) {
                try {
                    boolean booleanValue = ((Boolean) entry.getValue().getClass().getMethod("isAutomaticActive", new Class[0]).invoke(entry.getValue(), new Object[0])).booleanValue();
                    if (booleanValue) {
                        return booleanValue;
                    }
                    z = booleanValue;
                } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException e) {
                    e = e;
                    LOGGER.mo11965w(e);
                    return z;
                }
            }
            return z;
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException e2) {
            e = e2;
            z = false;
            LOGGER.mo11965w(e);
            return z;
        }
    }

    public static boolean setZenMode(int i) {
        try {
            Class cls = Class.forName("android.app.NotificationManager");
            NotificationManager notificationManager = (NotificationManager) cls.getMethod("from", new Class[]{Context.class}).invoke(cls, new Object[]{ActionsApplication.getAppContext()});
            cls.getMethod("setZenMode", new Class[]{Integer.TYPE, Uri.class, String.class}).invoke(notificationManager, new Object[]{Integer.valueOf(i), null, "ZenModeAccess"});
            return true;
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            LOGGER.mo11960e("setZenMode", e);
            return false;
        }
    }
}
