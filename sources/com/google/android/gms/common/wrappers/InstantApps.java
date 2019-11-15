package com.google.android.gms.common.wrappers;

import android.content.Context;
import com.google.android.gms.common.util.PlatformVersion;

public class InstantApps {
    private static Context zzaay;
    private static Boolean zzaaz;

    public static synchronized boolean isInstantApp(Context context) {
        Boolean valueOf;
        synchronized (InstantApps.class) {
            Context applicationContext = context.getApplicationContext();
            if (zzaay == null || zzaaz == null || zzaay != applicationContext) {
                zzaaz = null;
                if (PlatformVersion.isAtLeastO()) {
                    valueOf = Boolean.valueOf(applicationContext.getPackageManager().isInstantApp());
                } else {
                    try {
                        context.getClassLoader().loadClass("com.google.android.instantapps.supervisor.InstantAppsRuntime");
                        zzaaz = Boolean.valueOf(true);
                    } catch (ClassNotFoundException unused) {
                        valueOf = Boolean.valueOf(false);
                    }
                    zzaay = applicationContext;
                    boolean booleanValue = zzaaz.booleanValue();
                    return booleanValue;
                }
                zzaaz = valueOf;
                zzaay = applicationContext;
                boolean booleanValue2 = zzaaz.booleanValue();
                return booleanValue2;
            }
            boolean booleanValue3 = zzaaz.booleanValue();
            return booleanValue3;
        }
    }
}
