package com.motorola.actions.reflect;

import android.annotation.SuppressLint;
import com.motorola.actions.utils.MALogger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressLint({"PrivateApi"})
public class SystemPropertiesProxy {
    private static final Class<?> CLASS_SYSTEM_PROPERTIES;
    private static final boolean IS_INITIALIZED;
    private static final MALogger LOGGER = new MALogger(SystemPropertiesProxy.class);
    private static final Method METHOD_GET;
    private static final Method METHOD_GET_BOOLEAN;

    static {
        Method method;
        Class<?> cls;
        boolean z = true;
        Method method2 = null;
        try {
            cls = Class.forName("android.os.SystemProperties");
            try {
                method = cls.getMethod("get", new Class[]{String.class});
                try {
                    method2 = cls.getMethod("getBoolean", new Class[]{String.class, Boolean.TYPE});
                } catch (ClassNotFoundException | NoSuchMethodException e) {
                    e = e;
                    LOGGER.mo11964w("Reflection error", e);
                    z = false;
                    METHOD_GET = method;
                    METHOD_GET_BOOLEAN = method2;
                    CLASS_SYSTEM_PROPERTIES = cls;
                    IS_INITIALIZED = z;
                }
            } catch (ClassNotFoundException | NoSuchMethodException e2) {
                e = e2;
                method = null;
                LOGGER.mo11964w("Reflection error", e);
                z = false;
                METHOD_GET = method;
                METHOD_GET_BOOLEAN = method2;
                CLASS_SYSTEM_PROPERTIES = cls;
                IS_INITIALIZED = z;
            }
        } catch (ClassNotFoundException | NoSuchMethodException e3) {
            e = e3;
            cls = null;
            method = null;
            LOGGER.mo11964w("Reflection error", e);
            z = false;
            METHOD_GET = method;
            METHOD_GET_BOOLEAN = method2;
            CLASS_SYSTEM_PROPERTIES = cls;
            IS_INITIALIZED = z;
        }
        METHOD_GET = method;
        METHOD_GET_BOOLEAN = method2;
        CLASS_SYSTEM_PROPERTIES = cls;
        IS_INITIALIZED = z;
    }

    public static String get(String str) {
        if (IS_INITIALIZED) {
            try {
                return (String) METHOD_GET.invoke(CLASS_SYSTEM_PROPERTIES, new Object[]{str});
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.mo11965w(e);
            }
        }
        return null;
    }

    public static boolean getBoolean(String str, boolean z) {
        if (IS_INITIALIZED) {
            try {
                return ((Boolean) METHOD_GET_BOOLEAN.invoke(CLASS_SYSTEM_PROPERTIES, new Object[]{str, Boolean.valueOf(z)})).booleanValue();
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.mo11965w(e);
            }
        }
        return z;
    }
}
