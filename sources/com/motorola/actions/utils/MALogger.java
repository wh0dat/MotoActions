package com.motorola.actions.utils;

import android.util.Log;

public class MALogger {
    private String mTag;

    public MALogger(Class<?> cls) {
        StringBuilder sb = new StringBuilder();
        sb.append(cls.getPackage().getName().replaceFirst("^com.motorola.", "mot."));
        sb.append(".");
        sb.append(cls.getSimpleName());
        this.mTag = sb.toString();
    }

    /* renamed from: e */
    public void mo11959e(String str) {
        Log.e(this.mTag, str);
    }

    /* renamed from: e */
    public void mo11960e(String str, Throwable th) {
        Log.e(this.mTag, str, th);
    }

    /* renamed from: w */
    public void mo11963w(String str) {
        Log.w(this.mTag, str);
    }

    /* renamed from: w */
    public void mo11965w(Throwable th) {
        Log.w(this.mTag, th);
    }

    /* renamed from: w */
    public void mo11964w(String str, Throwable th) {
        Log.w(this.mTag, str, th);
    }

    /* renamed from: i */
    public void mo11962i(String str, Throwable th) {
        if (!Constants.PRODUCTION_MODE) {
            Log.i(this.mTag, str, th);
        }
    }

    /* renamed from: i */
    public void mo11961i(String str) {
        if (!Constants.PRODUCTION_MODE) {
            Log.i(this.mTag, str);
        }
    }

    /* renamed from: d */
    public void mo11957d(String str) {
        if (!Constants.PRODUCTION_MODE) {
            Log.d(this.mTag, str);
        }
    }

    /* renamed from: d */
    public void mo11958d(String str, Throwable th) {
        if (!Constants.PRODUCTION_MODE) {
            Log.d(this.mTag, str, th);
        }
    }

    public final boolean canLogDebug() {
        return !Constants.PRODUCTION_MODE;
    }
}
