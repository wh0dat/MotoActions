package com.google.android.gms.common.config;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Process;
import android.os.UserManager;
import android.support.annotation.Nullable;
import android.util.Log;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.internal.stable.zzg;
import com.google.android.gms.internal.stable.zzi;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.annotation.concurrent.GuardedBy;

public abstract class GservicesValue<T> {
    private static final Object sLock = new Object();
    /* access modifiers changed from: private */
    public static zza zzmu = null;
    private static int zzmv = 0;
    private static Context zzmw = null;
    private static String zzmx = "com.google.android.providers.gsf.permission.READ_GSERVICES";
    @GuardedBy("sLock")
    private static HashSet<String> zzmy;
    protected final T mDefaultValue;
    protected final String mKey;
    private T zzmz = null;

    private interface zza {
        Long getLong(String str, Long l);

        String getString(String str, String str2);

        Boolean zza(String str, Boolean bool);

        Double zza(String str, Double d);

        Float zza(String str, Float f);

        Integer zza(String str, Integer num);

        String zzb(String str, String str2);
    }

    private static class zzb implements zza {
        /* access modifiers changed from: private */
        public static final Collection<GservicesValue<?>> zzna = new HashSet();

        private zzb() {
        }

        /* synthetic */ zzb(zza zza) {
            this();
        }

        public final Long getLong(String str, Long l) {
            return l;
        }

        public final String getString(String str, String str2) {
            return str2;
        }

        public final Boolean zza(String str, Boolean bool) {
            return bool;
        }

        public final Double zza(String str, Double d) {
            return d;
        }

        public final Float zza(String str, Float f) {
            return f;
        }

        public final Integer zza(String str, Integer num) {
            return num;
        }

        public final String zzb(String str, String str2) {
            return str2;
        }
    }

    @Deprecated
    private static class zzc implements zza {
        private final Map<String, ?> values;

        public zzc(Map<String, ?> map) {
            this.values = map;
        }

        private final <T> T zza(String str, T t) {
            return this.values.containsKey(str) ? this.values.get(str) : t;
        }

        public final Long getLong(String str, Long l) {
            return (Long) zza(str, (T) l);
        }

        public final String getString(String str, String str2) {
            return (String) zza(str, (T) str2);
        }

        public final Boolean zza(String str, Boolean bool) {
            return (Boolean) zza(str, (T) bool);
        }

        public final Double zza(String str, Double d) {
            return (Double) zza(str, (T) d);
        }

        public final Float zza(String str, Float f) {
            return (Float) zza(str, (T) f);
        }

        public final Integer zza(String str, Integer num) {
            return (Integer) zza(str, (T) num);
        }

        public final String zzb(String str, String str2) {
            return (String) zza(str, (T) str2);
        }
    }

    private static class zzd implements zza {
        private final ContentResolver mContentResolver;

        public zzd(ContentResolver contentResolver) {
            this.mContentResolver = contentResolver;
        }

        public final Long getLong(String str, Long l) {
            return Long.valueOf(zzi.getLong(this.mContentResolver, str, l.longValue()));
        }

        public final String getString(String str, String str2) {
            return zzi.zza(this.mContentResolver, str, str2);
        }

        public final Boolean zza(String str, Boolean bool) {
            return Boolean.valueOf(zzi.zza(this.mContentResolver, str, bool.booleanValue()));
        }

        public final Double zza(String str, Double d) {
            String zza = zzi.zza(this.mContentResolver, str, (String) null);
            if (zza != null) {
                try {
                    return Double.valueOf(Double.parseDouble(zza));
                } catch (NumberFormatException unused) {
                }
            }
            return d;
        }

        public final Float zza(String str, Float f) {
            String zza = zzi.zza(this.mContentResolver, str, (String) null);
            if (zza != null) {
                try {
                    return Float.valueOf(Float.parseFloat(zza));
                } catch (NumberFormatException unused) {
                }
            }
            return f;
        }

        public final Integer zza(String str, Integer num) {
            return Integer.valueOf(zzi.getInt(this.mContentResolver, str, num.intValue()));
        }

        public final String zzb(String str, String str2) {
            return zzg.zza(this.mContentResolver, str, str2);
        }
    }

    protected GservicesValue(String str, T t) {
        this.mKey = str;
        this.mDefaultValue = t;
    }

    @Deprecated
    @VisibleForTesting
    public static void forceInit(Context context) {
        forceInit(context, new HashSet());
    }

    @VisibleForTesting
    public static void forceInit(Context context, @Nullable HashSet<String> hashSet) {
        zza(context, new zzd(context.getContentResolver()), hashSet);
    }

    @TargetApi(24)
    public static SharedPreferences getDirectBootCache(Context context) {
        return context.getApplicationContext().createDeviceProtectedStorageContext().getSharedPreferences("gservices-direboot-cache", 0);
    }

    public static int getSharedUserId() {
        return zzmv;
    }

    @Deprecated
    public static void init(Context context) {
        init(context, zzd(context) ? new HashSet() : null);
    }

    public static void init(Context context, @Nullable HashSet<String> hashSet) {
        synchronized (sLock) {
            if (zzmu == null) {
                zza(context, new zzd(context.getContentResolver()), hashSet);
            }
            if (zzmv == 0) {
                try {
                    zzmv = context.getPackageManager().getApplicationInfo("com.google.android.gms", 0).uid;
                } catch (NameNotFoundException e) {
                    Log.e("GservicesValue", e.toString());
                }
            }
        }
    }

    @Deprecated
    @VisibleForTesting
    public static void initForTests() {
        zza(null, new zzb(null), new HashSet());
    }

    @VisibleForTesting
    public static void initForTests(Context context, @Nullable HashSet<String> hashSet) {
        zza(context, new zzb(null), hashSet);
    }

    @Deprecated
    @VisibleForTesting
    public static void initForTests(String str, Object obj) {
        HashMap hashMap = new HashMap(1);
        hashMap.put(str, obj);
        initForTests(hashMap);
    }

    @Deprecated
    @VisibleForTesting
    public static void initForTests(Map<String, ?> map) {
        synchronized (sLock) {
            zzmu = new zzc(map);
        }
    }

    public static boolean isInitialized() {
        boolean z;
        synchronized (sLock) {
            z = zzmu != null;
        }
        return z;
    }

    public static GservicesValue<String> partnerSetting(String str, String str2) {
        return new zzg(str, str2);
    }

    @VisibleForTesting
    public static void resetAllOverrides() {
        synchronized (sLock) {
            if (zzcg()) {
                for (GservicesValue resetOverride : zzb.zzna) {
                    resetOverride.resetOverride();
                }
                zzb.zzna.clear();
            }
        }
    }

    public static GservicesValue<Double> value(String str, Double d) {
        return new zzd(str, d);
    }

    public static GservicesValue<Float> value(String str, Float f) {
        return new zze(str, f);
    }

    public static GservicesValue<Integer> value(String str, Integer num) {
        return new zzc(str, num);
    }

    public static GservicesValue<Long> value(String str, Long l) {
        return new zzb(str, l);
    }

    public static GservicesValue<String> value(String str, String str2) {
        return new zzf(str, str2);
    }

    public static GservicesValue<Boolean> value(String str, boolean z) {
        return new zza(str, Boolean.valueOf(z));
    }

    @TargetApi(24)
    private static void zza(@Nullable Context context, zza zza2, @Nullable HashSet<String> hashSet) {
        synchronized (sLock) {
            zzmu = zza2;
            zzmy = null;
            zzmw = null;
            if (context != null && zzd(context)) {
                zzmy = hashSet;
                zzmw = context.getApplicationContext().createDeviceProtectedStorageContext();
            }
        }
    }

    private static boolean zzcg() {
        boolean z;
        synchronized (sLock) {
            if (!(zzmu instanceof zzb)) {
                if (!(zzmu instanceof zzc)) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    @TargetApi(24)
    private static boolean zzd(Context context) {
        if (!PlatformVersion.isAtLeastN()) {
            return false;
        }
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        return !userManager.isUserUnlocked() && !userManager.isUserRunning(Process.myUserHandle());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0092, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:?, code lost:
        r1 = android.os.Binder.clearCallingIdentity();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        r6 = retrieve(r6.mKey);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        android.os.Binder.restoreCallingIdentity(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00a1, code lost:
        android.os.StrictMode.setThreadPolicy(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00a4, code lost:
        return r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00a5, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
        android.os.Binder.restoreCallingIdentity(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00a9, code lost:
        throw r6;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00aa, code lost:
        android.os.StrictMode.setThreadPolicy(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00ad, code lost:
        throw r6;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:45:0x0094 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final T get() {
        /*
            r6 = this;
            T r0 = r6.zzmz
            if (r0 == 0) goto L_0x0007
            T r6 = r6.zzmz
            return r6
        L_0x0007:
            android.os.StrictMode$ThreadPolicy r0 = android.os.StrictMode.allowThreadDiskReads()
            java.lang.Object r1 = sLock
            monitor-enter(r1)
            android.content.Context r2 = zzmw     // Catch:{ all -> 0x00b1 }
            if (r2 == 0) goto L_0x001c
            android.content.Context r2 = zzmw     // Catch:{ all -> 0x00b1 }
            boolean r2 = zzd(r2)     // Catch:{ all -> 0x00b1 }
            if (r2 == 0) goto L_0x001c
            r2 = 1
            goto L_0x001d
        L_0x001c:
            r2 = 0
        L_0x001d:
            java.util.HashSet<java.lang.String> r3 = zzmy     // Catch:{ all -> 0x00b1 }
            android.content.Context r4 = zzmw     // Catch:{ all -> 0x00b1 }
            monitor-exit(r1)     // Catch:{ all -> 0x00b1 }
            if (r2 == 0) goto L_0x007f
            java.lang.String r0 = "GservicesValue"
            r1 = 3
            boolean r0 = android.util.Log.isLoggable(r0, r1)
            if (r0 == 0) goto L_0x004b
            java.lang.String r0 = "GservicesValue"
            java.lang.String r1 = "Gservice value accessed during directboot: "
            java.lang.String r2 = r6.mKey
            java.lang.String r2 = java.lang.String.valueOf(r2)
            int r5 = r2.length()
            if (r5 == 0) goto L_0x0042
            java.lang.String r1 = r1.concat(r2)
            goto L_0x0048
        L_0x0042:
            java.lang.String r2 = new java.lang.String
            r2.<init>(r1)
            r1 = r2
        L_0x0048:
            android.util.Log.d(r0, r1)
        L_0x004b:
            if (r3 == 0) goto L_0x0076
            java.lang.String r0 = r6.mKey
            boolean r0 = r3.contains(r0)
            if (r0 != 0) goto L_0x0076
            java.lang.String r0 = "GservicesValue"
            java.lang.String r1 = "Gservices key not whitelisted for directboot access: "
            java.lang.String r2 = r6.mKey
            java.lang.String r2 = java.lang.String.valueOf(r2)
            int r3 = r2.length()
            if (r3 == 0) goto L_0x006a
            java.lang.String r1 = r1.concat(r2)
            goto L_0x0070
        L_0x006a:
            java.lang.String r2 = new java.lang.String
            r2.<init>(r1)
            r1 = r2
        L_0x0070:
            android.util.Log.e(r0, r1)
            T r6 = r6.mDefaultValue
            return r6
        L_0x0076:
            java.lang.String r0 = r6.mKey
            T r1 = r6.mDefaultValue
            java.lang.Object r6 = r6.retrieveFromDirectBootCache(r4, r0, r1)
            return r6
        L_0x007f:
            java.lang.Object r2 = sLock
            monitor-enter(r2)
            r1 = 0
            zzmy = r1     // Catch:{ all -> 0x00ae }
            zzmw = r1     // Catch:{ all -> 0x00ae }
            monitor-exit(r2)     // Catch:{ all -> 0x00ae }
            java.lang.String r1 = r6.mKey     // Catch:{ SecurityException -> 0x0094 }
            java.lang.Object r1 = r6.retrieve(r1)     // Catch:{ SecurityException -> 0x0094 }
            android.os.StrictMode.setThreadPolicy(r0)
            return r1
        L_0x0092:
            r6 = move-exception
            goto L_0x00aa
        L_0x0094:
            long r1 = android.os.Binder.clearCallingIdentity()     // Catch:{ all -> 0x0092 }
            java.lang.String r3 = r6.mKey     // Catch:{ all -> 0x00a5 }
            java.lang.Object r6 = r6.retrieve(r3)     // Catch:{ all -> 0x00a5 }
            android.os.Binder.restoreCallingIdentity(r1)     // Catch:{ all -> 0x0092 }
            android.os.StrictMode.setThreadPolicy(r0)
            return r6
        L_0x00a5:
            r6 = move-exception
            android.os.Binder.restoreCallingIdentity(r1)     // Catch:{ all -> 0x0092 }
            throw r6     // Catch:{ all -> 0x0092 }
        L_0x00aa:
            android.os.StrictMode.setThreadPolicy(r0)
            throw r6
        L_0x00ae:
            r6 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x00ae }
            throw r6
        L_0x00b1:
            r6 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x00b1 }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.config.GservicesValue.get():java.lang.Object");
    }

    @Deprecated
    public final T getBinderSafe() {
        return get();
    }

    public String getKey() {
        return this.mKey;
    }

    @VisibleForTesting
    public void override(T t) {
        if (!(zzmu instanceof zzb)) {
            Log.w("GservicesValue", "GservicesValue.override(): test should probably call initForTests() first");
        }
        this.zzmz = t;
        synchronized (sLock) {
            if (zzcg()) {
                zzb.zzna.add(this);
            }
        }
    }

    @VisibleForTesting
    public void resetOverride() {
        this.zzmz = null;
    }

    /* access modifiers changed from: protected */
    public abstract T retrieve(String str);

    /* access modifiers changed from: protected */
    @TargetApi(24)
    public T retrieveFromDirectBootCache(Context context, String str, T t) {
        throw new UnsupportedOperationException("The Gservices classes used does not support direct-boot");
    }
}
