package com.motorola.actions.reflect;

import android.content.ContentResolver;
import android.support.annotation.NonNull;
import com.motorola.actions.MultiUserManager;
import com.motorola.actions.checkin.CheckinContainer;
import com.motorola.actions.utils.MALogger;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CheckinEventProxy implements CheckinContainer {
    private static final MALogger LOGGER = new MALogger(CheckinEventProxy.class);
    private static final Constructor<?> METHOD_CONSTRUCTOR;
    private static final Method METHOD_PUBLISH;
    private static final Method METHOD_SET_BOOLEAN_VALUE;
    private static final Method METHOD_SET_DOUBLE_VALUE;
    private static final Method METHOD_SET_INT_VALUE;
    private static final Method METHOD_SET_LONG_VALUE;
    private static final Method METHOD_SET_STRING_VALUE;
    private static final boolean SUCCESSFULLY_INIT;
    private final Object mCheckinEvent;
    private final boolean mIsLameDuck;

    static {
        Method method;
        Method method2;
        Method method3;
        Constructor<?> constructor;
        Method method4;
        Method method5;
        Method method6 = null;
        boolean z = true;
        try {
            Class cls = Class.forName("com.motorola.android.provider.CheckinEvent");
            Class cls2 = Class.forName("com.motorola.data.event.api.Event");
            constructor = cls.getDeclaredConstructor(new Class[]{String.class, String.class, String.class, Long.TYPE});
            Method declaredMethod = cls2.getDeclaredMethod("setValue", new Class[]{String.class, String.class});
            method3 = cls2.getDeclaredMethod("setValue", new Class[]{String.class, Boolean.TYPE});
            try {
                method2 = cls2.getDeclaredMethod("setValue", new Class[]{String.class, Integer.TYPE});
                try {
                    method = cls2.getDeclaredMethod("setValue", new Class[]{String.class, Long.TYPE});
                } catch (ClassNotFoundException | NoSuchMethodException unused) {
                    method4 = null;
                    method = null;
                    LOGGER.mo11963w("Reflection failed");
                    method5 = null;
                    constructor = null;
                    z = false;
                    METHOD_SET_STRING_VALUE = method6;
                    METHOD_SET_BOOLEAN_VALUE = method3;
                    METHOD_SET_INT_VALUE = method2;
                    METHOD_SET_LONG_VALUE = method;
                    METHOD_SET_DOUBLE_VALUE = method4;
                    METHOD_PUBLISH = method5;
                    METHOD_CONSTRUCTOR = constructor;
                    SUCCESSFULLY_INIT = z;
                }
                try {
                    method4 = cls2.getDeclaredMethod("setValue", new Class[]{String.class, Double.TYPE});
                    try {
                        method5 = cls.getDeclaredMethod("publish", new Class[]{Object.class});
                        method6 = declaredMethod;
                    } catch (ClassNotFoundException | NoSuchMethodException unused2) {
                        LOGGER.mo11963w("Reflection failed");
                        method5 = null;
                        constructor = null;
                        z = false;
                        METHOD_SET_STRING_VALUE = method6;
                        METHOD_SET_BOOLEAN_VALUE = method3;
                        METHOD_SET_INT_VALUE = method2;
                        METHOD_SET_LONG_VALUE = method;
                        METHOD_SET_DOUBLE_VALUE = method4;
                        METHOD_PUBLISH = method5;
                        METHOD_CONSTRUCTOR = constructor;
                        SUCCESSFULLY_INIT = z;
                    }
                } catch (ClassNotFoundException | NoSuchMethodException unused3) {
                    method4 = null;
                    LOGGER.mo11963w("Reflection failed");
                    method5 = null;
                    constructor = null;
                    z = false;
                    METHOD_SET_STRING_VALUE = method6;
                    METHOD_SET_BOOLEAN_VALUE = method3;
                    METHOD_SET_INT_VALUE = method2;
                    METHOD_SET_LONG_VALUE = method;
                    METHOD_SET_DOUBLE_VALUE = method4;
                    METHOD_PUBLISH = method5;
                    METHOD_CONSTRUCTOR = constructor;
                    SUCCESSFULLY_INIT = z;
                }
            } catch (ClassNotFoundException | NoSuchMethodException unused4) {
                method4 = null;
                method2 = null;
                method = method2;
                LOGGER.mo11963w("Reflection failed");
                method5 = null;
                constructor = null;
                z = false;
                METHOD_SET_STRING_VALUE = method6;
                METHOD_SET_BOOLEAN_VALUE = method3;
                METHOD_SET_INT_VALUE = method2;
                METHOD_SET_LONG_VALUE = method;
                METHOD_SET_DOUBLE_VALUE = method4;
                METHOD_PUBLISH = method5;
                METHOD_CONSTRUCTOR = constructor;
                SUCCESSFULLY_INIT = z;
            }
        } catch (ClassNotFoundException | NoSuchMethodException unused5) {
            method4 = null;
            method3 = null;
            method2 = null;
            method = method2;
            LOGGER.mo11963w("Reflection failed");
            method5 = null;
            constructor = null;
            z = false;
            METHOD_SET_STRING_VALUE = method6;
            METHOD_SET_BOOLEAN_VALUE = method3;
            METHOD_SET_INT_VALUE = method2;
            METHOD_SET_LONG_VALUE = method;
            METHOD_SET_DOUBLE_VALUE = method4;
            METHOD_PUBLISH = method5;
            METHOD_CONSTRUCTOR = constructor;
            SUCCESSFULLY_INIT = z;
        }
        METHOD_SET_STRING_VALUE = method6;
        METHOD_SET_BOOLEAN_VALUE = method3;
        METHOD_SET_INT_VALUE = method2;
        METHOD_SET_LONG_VALUE = method;
        METHOD_SET_DOUBLE_VALUE = method4;
        METHOD_PUBLISH = method5;
        METHOD_CONSTRUCTOR = constructor;
        SUCCESSFULLY_INIT = z;
    }

    public static boolean isInitialized() {
        return SUCCESSFULLY_INIT;
    }

    public CheckinEventProxy(String str, String str2, String str3, long j) {
        boolean z = false;
        Object obj = null;
        if (isInitialized()) {
            try {
                obj = METHOD_CONSTRUCTOR.newInstance(new Object[]{str, str2, str3, Long.valueOf(j)});
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                LOGGER.mo11965w(e);
                z = true;
            }
        }
        this.mIsLameDuck = z;
        this.mCheckinEvent = obj;
    }

    public void setValue(@NonNull String str, String str2) {
        if (isInitialized() && !this.mIsLameDuck) {
            try {
                METHOD_SET_STRING_VALUE.invoke(this.mCheckinEvent, new Object[]{str, str2});
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.mo11965w(e);
            }
        }
    }

    public void setValue(@NonNull String str, boolean z) {
        if (isInitialized() && !this.mIsLameDuck) {
            try {
                METHOD_SET_BOOLEAN_VALUE.invoke(this.mCheckinEvent, new Object[]{str, Boolean.valueOf(z)});
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.mo11965w(e);
            }
        }
    }

    public void setValue(@NonNull String str, int i) {
        if (isInitialized() && !this.mIsLameDuck) {
            try {
                METHOD_SET_INT_VALUE.invoke(this.mCheckinEvent, new Object[]{str, Integer.valueOf(i)});
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.mo11965w(e);
            }
        }
    }

    public void setValue(String str, long j) {
        if (isInitialized() && !this.mIsLameDuck) {
            try {
                METHOD_SET_LONG_VALUE.invoke(this.mCheckinEvent, new Object[]{str, Long.valueOf(j)});
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.mo11965w(e);
            }
        }
    }

    public void setValue(@NonNull String str, double d) {
        if (isInitialized() && !this.mIsLameDuck) {
            try {
                METHOD_SET_DOUBLE_VALUE.invoke(this.mCheckinEvent, new Object[]{str, Double.valueOf(d)});
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.mo11965w(e);
            }
        }
    }

    public void publish(ContentResolver contentResolver) {
        if (MultiUserManager.isSystemUser() && isInitialized() && !this.mIsLameDuck) {
            try {
                METHOD_PUBLISH.invoke(this.mCheckinEvent, new Object[]{contentResolver});
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.mo11965w(e);
            }
        }
    }
}
