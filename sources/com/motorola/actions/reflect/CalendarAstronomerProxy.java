package com.motorola.actions.reflect;

import android.annotation.SuppressLint;
import com.motorola.actions.utils.MALogger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CalendarAstronomerProxy {
    private static final MALogger LOGGER = new MALogger(CalendarAstronomerProxy.class);
    private Object mClass;
    private Method mMethodGetSunRise;

    @SuppressLint({"PrivateApi"})
    public CalendarAstronomerProxy(double d, double d2) {
        try {
            this.mClass = Class.forName("android.icu.impl.CalendarAstronomer").getConstructor(new Class[]{Double.TYPE, Double.TYPE}).newInstance(new Object[]{Double.valueOf(d), Double.valueOf(d2)});
            this.mMethodGetSunRise = this.mClass.getClass().getDeclaredMethod("getSunRiseSet", new Class[]{Boolean.TYPE});
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Reflection class error - Exception: ");
            sb.append(e);
            mALogger.mo11959e(sb.toString());
        }
    }

    public long getSunRiseSet(boolean z) {
        try {
            this.mMethodGetSunRise.invoke(this.mClass, new Object[]{Boolean.valueOf(z)});
            return ((Long) this.mMethodGetSunRise.invoke(this.mClass, new Object[]{Boolean.valueOf(z)})).longValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("getSunRiseSet - Reflection method error - Exception: ");
            sb.append(e);
            mALogger.mo11957d(sb.toString());
            return 0;
        }
    }
}
