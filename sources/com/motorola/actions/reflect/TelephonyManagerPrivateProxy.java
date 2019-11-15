package com.motorola.actions.reflect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import com.motorola.actions.utils.MALogger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressLint({"PrivateApi"})
public class TelephonyManagerPrivateProxy {
    private static final MALogger LOGGER = new MALogger(TelephonyManagerPrivateProxy.class);
    private static final Method SILENCE_RINGER;
    private TelephonyManager mTelephonyManager;

    static {
        Method method;
        try {
            method = TelephonyManager.class.getDeclaredMethod("silenceRinger", new Class[0]);
        } catch (NoSuchMethodException e) {
            LOGGER.mo11964w("Reflection error", e);
            method = null;
        }
        SILENCE_RINGER = method;
    }

    public TelephonyManagerPrivateProxy(Context context) {
        this.mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
    }

    public void silenceRinger() {
        try {
            SILENCE_RINGER.invoke(this.mTelephonyManager, new Object[0]);
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOGGER.mo11965w(e);
        }
    }
}
