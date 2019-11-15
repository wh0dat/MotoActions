package com.google.android.gms.internal.stable;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

public class zzi {
    private static final Uri CONTENT_URI = Uri.parse("content://com.google.android.gsf.gservices");
    private static HashMap<String, String> zzagq;
    private static final Uri zzagv = Uri.parse("content://com.google.android.gsf.gservices/prefix");
    private static final Pattern zzagw = Pattern.compile("^(1|true|t|on|yes|y)$", 2);
    private static final Pattern zzagx = Pattern.compile("^(0|false|f|off|no|n)$", 2);
    /* access modifiers changed from: private */
    public static final AtomicBoolean zzagy = new AtomicBoolean();
    private static final HashMap<String, Boolean> zzagz = new HashMap<>();
    private static final HashMap<String, Integer> zzaha = new HashMap<>();
    private static final HashMap<String, Long> zzahb = new HashMap<>();
    private static final HashMap<String, Float> zzahc = new HashMap<>();
    private static Object zzahd;
    private static boolean zzahe;
    private static String[] zzahf = new String[0];

    public static int getInt(ContentResolver contentResolver, String str, int i) {
        int i2;
        Object zzb = zzb(contentResolver);
        Integer num = (Integer) zza(zzaha, str, (T) Integer.valueOf(i));
        if (num != null) {
            return num.intValue();
        }
        String zza = zza(contentResolver, str, (String) null);
        if (zza != null) {
            try {
                i2 = Integer.parseInt(zza);
                num = Integer.valueOf(i2);
            } catch (NumberFormatException unused) {
            }
            zza(zzb, zzaha, str, num);
            return i2;
        }
        i2 = i;
        zza(zzb, zzaha, str, num);
        return i2;
    }

    public static long getLong(ContentResolver contentResolver, String str, long j) {
        Long l;
        Object zzb = zzb(contentResolver);
        Long l2 = (Long) zza(zzahb, str, (T) Long.valueOf(j));
        if (l2 != null) {
            return l2.longValue();
        }
        String zza = zza(contentResolver, str, (String) null);
        if (zza != null) {
            try {
                long parseLong = Long.parseLong(zza);
                l = Long.valueOf(parseLong);
                j = parseLong;
            } catch (NumberFormatException unused) {
            }
            zza(zzb, zzahb, str, l);
            return j;
        }
        l = l2;
        zza(zzb, zzahb, str, l);
        return j;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0012, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static <T> T zza(java.util.HashMap<java.lang.String, T> r2, java.lang.String r3, T r4) {
        /*
            java.lang.Class<com.google.android.gms.internal.stable.zzi> r0 = com.google.android.gms.internal.stable.zzi.class
            monitor-enter(r0)
            boolean r1 = r2.containsKey(r3)     // Catch:{ all -> 0x0016 }
            if (r1 == 0) goto L_0x0013
            java.lang.Object r2 = r2.get(r3)     // Catch:{ all -> 0x0016 }
            if (r2 == 0) goto L_0x0010
            goto L_0x0011
        L_0x0010:
            r2 = r4
        L_0x0011:
            monitor-exit(r0)     // Catch:{ all -> 0x0016 }
            return r2
        L_0x0013:
            monitor-exit(r0)     // Catch:{ all -> 0x0016 }
            r2 = 0
            return r2
        L_0x0016:
            r2 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0016 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.stable.zzi.zza(java.util.HashMap, java.lang.String, java.lang.Object):java.lang.Object");
    }

    /* JADX INFO: finally extract failed */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001d, code lost:
        return r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x005c, code lost:
        return r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x005e, code lost:
        return r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0063, code lost:
        r13 = r13.query(CONTENT_URI, null, null, new java.lang.String[]{r14}, null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x0071, code lost:
        if (r13 == null) goto L_0x0095;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0077, code lost:
        if (r13.moveToFirst() != false) goto L_0x007a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x007a, code lost:
        r0 = r13.getString(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x007e, code lost:
        if (r0 == null) goto L_0x0087;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0084, code lost:
        if (r0.equals(r15) == false) goto L_0x0087;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0086, code lost:
        r0 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0087, code lost:
        zza(r1, r14, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x008a, code lost:
        if (r0 == null) goto L_0x008d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x008c, code lost:
        r15 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x008d, code lost:
        if (r13 == null) goto L_0x0092;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x008f, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0092, code lost:
        return r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0093, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
        zza(r1, r14, (java.lang.String) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0099, code lost:
        if (r13 == null) goto L_0x009e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x009b, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x009e, code lost:
        return r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x009f, code lost:
        if (r13 != null) goto L_0x00a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00a1, code lost:
        r13.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00a4, code lost:
        throw r14;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String zza(android.content.ContentResolver r13, java.lang.String r14, java.lang.String r15) {
        /*
            java.lang.Class<com.google.android.gms.internal.stable.zzi> r0 = com.google.android.gms.internal.stable.zzi.class
            monitor-enter(r0)
            zza(r13)     // Catch:{ all -> 0x00a5 }
            java.lang.Object r1 = zzahd     // Catch:{ all -> 0x00a5 }
            java.util.HashMap<java.lang.String, java.lang.String> r2 = zzagq     // Catch:{ all -> 0x00a5 }
            boolean r2 = r2.containsKey(r14)     // Catch:{ all -> 0x00a5 }
            if (r2 == 0) goto L_0x001e
            java.util.HashMap<java.lang.String, java.lang.String> r13 = zzagq     // Catch:{ all -> 0x00a5 }
            java.lang.Object r13 = r13.get(r14)     // Catch:{ all -> 0x00a5 }
            java.lang.String r13 = (java.lang.String) r13     // Catch:{ all -> 0x00a5 }
            if (r13 == 0) goto L_0x001b
            goto L_0x001c
        L_0x001b:
            r13 = r15
        L_0x001c:
            monitor-exit(r0)     // Catch:{ all -> 0x00a5 }
            return r13
        L_0x001e:
            java.lang.String[] r2 = zzahf     // Catch:{ all -> 0x00a5 }
            int r3 = r2.length     // Catch:{ all -> 0x00a5 }
            r4 = 0
            r5 = r4
        L_0x0023:
            r6 = 1
            if (r5 >= r3) goto L_0x0062
            r7 = r2[r5]     // Catch:{ all -> 0x00a5 }
            boolean r7 = r14.startsWith(r7)     // Catch:{ all -> 0x00a5 }
            if (r7 == 0) goto L_0x005f
            boolean r1 = zzahe     // Catch:{ all -> 0x00a5 }
            if (r1 == 0) goto L_0x003a
            java.util.HashMap<java.lang.String, java.lang.String> r1 = zzagq     // Catch:{ all -> 0x00a5 }
            boolean r1 = r1.isEmpty()     // Catch:{ all -> 0x00a5 }
            if (r1 == 0) goto L_0x005d
        L_0x003a:
            java.lang.String[] r1 = zzahf     // Catch:{ all -> 0x00a5 }
            java.util.HashMap<java.lang.String, java.lang.String> r2 = zzagq     // Catch:{ all -> 0x00a5 }
            java.util.Map r13 = zza(r13, r1)     // Catch:{ all -> 0x00a5 }
            r2.putAll(r13)     // Catch:{ all -> 0x00a5 }
            zzahe = r6     // Catch:{ all -> 0x00a5 }
            java.util.HashMap<java.lang.String, java.lang.String> r13 = zzagq     // Catch:{ all -> 0x00a5 }
            boolean r13 = r13.containsKey(r14)     // Catch:{ all -> 0x00a5 }
            if (r13 == 0) goto L_0x005d
            java.util.HashMap<java.lang.String, java.lang.String> r13 = zzagq     // Catch:{ all -> 0x00a5 }
            java.lang.Object r13 = r13.get(r14)     // Catch:{ all -> 0x00a5 }
            java.lang.String r13 = (java.lang.String) r13     // Catch:{ all -> 0x00a5 }
            if (r13 == 0) goto L_0x005a
            goto L_0x005b
        L_0x005a:
            r13 = r15
        L_0x005b:
            monitor-exit(r0)     // Catch:{ all -> 0x00a5 }
            return r13
        L_0x005d:
            monitor-exit(r0)     // Catch:{ all -> 0x00a5 }
            return r15
        L_0x005f:
            int r5 = r5 + 1
            goto L_0x0023
        L_0x0062:
            monitor-exit(r0)     // Catch:{ all -> 0x00a5 }
            android.net.Uri r8 = CONTENT_URI
            r9 = 0
            r10 = 0
            java.lang.String[] r11 = new java.lang.String[r6]
            r11[r4] = r14
            r12 = 0
            r7 = r13
            android.database.Cursor r13 = r7.query(r8, r9, r10, r11, r12)
            if (r13 == 0) goto L_0x0095
            boolean r0 = r13.moveToFirst()     // Catch:{ all -> 0x0093 }
            if (r0 != 0) goto L_0x007a
            goto L_0x0095
        L_0x007a:
            java.lang.String r0 = r13.getString(r6)     // Catch:{ all -> 0x0093 }
            if (r0 == 0) goto L_0x0087
            boolean r2 = r0.equals(r15)     // Catch:{ all -> 0x0093 }
            if (r2 == 0) goto L_0x0087
            r0 = r15
        L_0x0087:
            zza(r1, r14, r0)     // Catch:{ all -> 0x0093 }
            if (r0 == 0) goto L_0x008d
            r15 = r0
        L_0x008d:
            if (r13 == 0) goto L_0x0092
            r13.close()
        L_0x0092:
            return r15
        L_0x0093:
            r14 = move-exception
            goto L_0x009f
        L_0x0095:
            r0 = 0
            zza(r1, r14, r0)     // Catch:{ all -> 0x0093 }
            if (r13 == 0) goto L_0x009e
            r13.close()
        L_0x009e:
            return r15
        L_0x009f:
            if (r13 == 0) goto L_0x00a4
            r13.close()
        L_0x00a4:
            throw r14
        L_0x00a5:
            r13 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00a5 }
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.stable.zzi.zza(android.content.ContentResolver, java.lang.String, java.lang.String):java.lang.String");
    }

    private static Map<String, String> zza(ContentResolver contentResolver, String... strArr) {
        Cursor query = contentResolver.query(zzagv, null, null, strArr, null);
        TreeMap treeMap = new TreeMap();
        if (query == null) {
            return treeMap;
        }
        while (query.moveToNext()) {
            try {
                treeMap.put(query.getString(0), query.getString(1));
            } finally {
                query.close();
            }
        }
        return treeMap;
    }

    private static void zza(ContentResolver contentResolver) {
        if (zzagq == null) {
            zzagy.set(false);
            zzagq = new HashMap<>();
            zzahd = new Object();
            zzahe = false;
            contentResolver.registerContentObserver(CONTENT_URI, true, new zzj(null));
            return;
        }
        if (zzagy.getAndSet(false)) {
            zzagq.clear();
            zzagz.clear();
            zzaha.clear();
            zzahb.clear();
            zzahc.clear();
            zzahd = new Object();
            zzahe = false;
        }
    }

    private static void zza(Object obj, String str, String str2) {
        synchronized (zzi.class) {
            if (obj == zzahd) {
                zzagq.put(str, str2);
            }
        }
    }

    private static <T> void zza(Object obj, HashMap<String, T> hashMap, String str, T t) {
        synchronized (zzi.class) {
            if (obj == zzahd) {
                hashMap.put(str, t);
                zzagq.remove(str);
            }
        }
    }

    public static boolean zza(ContentResolver contentResolver, String str, boolean z) {
        Object zzb = zzb(contentResolver);
        Boolean bool = (Boolean) zza(zzagz, str, (T) Boolean.valueOf(z));
        if (bool != null) {
            return bool.booleanValue();
        }
        String zza = zza(contentResolver, str, (String) null);
        if (zza != null && !zza.equals("")) {
            if (zzagw.matcher(zza).matches()) {
                bool = Boolean.valueOf(true);
                z = true;
            } else if (zzagx.matcher(zza).matches()) {
                bool = Boolean.valueOf(false);
                z = false;
            } else {
                StringBuilder sb = new StringBuilder("attempt to read gservices key ");
                sb.append(str);
                sb.append(" (value \"");
                sb.append(zza);
                sb.append("\") as boolean");
                Log.w("Gservices", sb.toString());
            }
        }
        zza(zzb, zzagz, str, bool);
        return z;
    }

    private static Object zzb(ContentResolver contentResolver) {
        Object obj;
        synchronized (zzi.class) {
            zza(contentResolver);
            obj = zzahd;
        }
        return obj;
    }
}
