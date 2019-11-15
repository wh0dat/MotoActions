package com.motorola.actions.reflect;

import android.annotation.SuppressLint;
import com.motorola.actions.utils.MALogger;

@SuppressLint({"PrivateApi"})
public class TelephonyIntentsProxy {
    public static final String ACTION_CALL_DISCONNECTED;
    private static final MALogger LOGGER = new MALogger(TelephonyIntentsProxy.class);

    static {
        String str = null;
        try {
            str = Class.forName("com.android.internal.telephony.TelephonyIntents").getDeclaredField("ACTION_CALL_DISCONNECTED").get(null).toString();
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            LOGGER.mo11964w("Reflection error", e);
        }
        ACTION_CALL_DISCONNECTED = str;
    }
}
