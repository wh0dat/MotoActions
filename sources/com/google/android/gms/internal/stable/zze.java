package com.google.android.gms.internal.stable;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
import java.util.HashMap;

public final class zze {

    public static class zza implements BaseColumns {
        private static HashMap<Uri, zzh> zzagq = new HashMap<>();

        private static zzh zza(ContentResolver contentResolver, Uri uri) {
            zzh zzh = (zzh) zzagq.get(uri);
            if (zzh == null) {
                zzh zzh2 = new zzh();
                zzagq.put(uri, zzh2);
                contentResolver.registerContentObserver(uri, true, new zzf(null, zzh2));
                return zzh2;
            } else if (!zzh.zzagu.getAndSet(false)) {
                return zzh;
            } else {
                synchronized (zzh) {
                    zzh.zzags.clear();
                    zzh.zzagt = new Object();
                }
                return zzh;
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:13:0x001e, code lost:
            r2 = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
            r4 = r10;
            r5 = r11;
            r10 = r4.query(r5, new java.lang.String[]{com.motorola.mod.ModManager.EXTRA_MOD_VALUE}, "name=?", new java.lang.String[]{r12}, null);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0034, code lost:
            if (r10 == null) goto L_0x0053;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x003a, code lost:
            if (r10.moveToFirst() != false) goto L_0x003d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x003d, code lost:
            r3 = r10.getString(0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:?, code lost:
            zza(r1, r0, r12, r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0044, code lost:
            if (r10 == null) goto L_0x0080;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0046, code lost:
            r10.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0049, code lost:
            return r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x004a, code lost:
            r0 = e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x004c, code lost:
            r11 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x004d, code lost:
            r2 = r10;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x004f, code lost:
            r0 = e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0050, code lost:
            r3 = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0051, code lost:
            r2 = r10;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:?, code lost:
            zza(r1, r0, r12, null);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0056, code lost:
            if (r10 == null) goto L_0x005b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0058, code lost:
            r10.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x005b, code lost:
            return null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x005c, code lost:
            r11 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x005e, code lost:
            r0 = e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x005f, code lost:
            r3 = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0060, code lost:
            r10 = "GoogleSettings";
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:?, code lost:
            r1 = new java.lang.StringBuilder("Can't get key ");
            r1.append(r12);
            r1.append(" from ");
            r1.append(r11);
            android.util.Log.e(r10, r1.toString(), r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x007b, code lost:
            if (r2 != null) goto L_0x007d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x007d, code lost:
            r2.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0080, code lost:
            return r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x0081, code lost:
            if (r2 != null) goto L_0x0083;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x0083, code lost:
            r2.close();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0086, code lost:
            throw r11;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Removed duplicated region for block: B:28:0x004c A[ExcHandler: all (th java.lang.Throwable), Splitter:B:17:0x0036] */
        /* JADX WARNING: Removed duplicated region for block: B:45:0x007d  */
        /* JADX WARNING: Removed duplicated region for block: B:48:0x0083  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        protected static java.lang.String zza(android.content.ContentResolver r10, android.net.Uri r11, java.lang.String r12) {
            /*
                java.lang.Class<com.google.android.gms.internal.stable.zze$zza> r0 = com.google.android.gms.internal.stable.zze.zza.class
                monitor-enter(r0)
                com.google.android.gms.internal.stable.zzh r1 = zza(r10, r11)     // Catch:{ all -> 0x008a }
                monitor-exit(r0)     // Catch:{ all -> 0x008a }
                monitor-enter(r1)
                java.lang.Object r0 = r1.zzagt     // Catch:{ all -> 0x0087 }
                java.util.HashMap<java.lang.String, java.lang.String> r2 = r1.zzags     // Catch:{ all -> 0x0087 }
                boolean r2 = r2.containsKey(r12)     // Catch:{ all -> 0x0087 }
                if (r2 == 0) goto L_0x001d
                java.util.HashMap<java.lang.String, java.lang.String> r10 = r1.zzags     // Catch:{ all -> 0x0087 }
                java.lang.Object r10 = r10.get(r12)     // Catch:{ all -> 0x0087 }
                java.lang.String r10 = (java.lang.String) r10     // Catch:{ all -> 0x0087 }
                monitor-exit(r1)     // Catch:{ all -> 0x0087 }
                return r10
            L_0x001d:
                monitor-exit(r1)     // Catch:{ all -> 0x0087 }
                r2 = 0
                java.lang.String r3 = "value"
                java.lang.String[] r6 = new java.lang.String[]{r3}     // Catch:{ SQLException -> 0x005e }
                java.lang.String r7 = "name=?"
                r3 = 1
                java.lang.String[] r8 = new java.lang.String[r3]     // Catch:{ SQLException -> 0x005e }
                r3 = 0
                r8[r3] = r12     // Catch:{ SQLException -> 0x005e }
                r9 = 0
                r4 = r10
                r5 = r11
                android.database.Cursor r10 = r4.query(r5, r6, r7, r8, r9)     // Catch:{ SQLException -> 0x005e }
                if (r10 == 0) goto L_0x0053
                boolean r4 = r10.moveToFirst()     // Catch:{ SQLException -> 0x004f, all -> 0x004c }
                if (r4 != 0) goto L_0x003d
                goto L_0x0053
            L_0x003d:
                java.lang.String r3 = r10.getString(r3)     // Catch:{ SQLException -> 0x004f, all -> 0x004c }
                zza(r1, r0, r12, r3)     // Catch:{ SQLException -> 0x004a, all -> 0x004c }
                if (r10 == 0) goto L_0x0080
                r10.close()
                return r3
            L_0x004a:
                r0 = move-exception
                goto L_0x0051
            L_0x004c:
                r11 = move-exception
                r2 = r10
                goto L_0x0081
            L_0x004f:
                r0 = move-exception
                r3 = r2
            L_0x0051:
                r2 = r10
                goto L_0x0060
            L_0x0053:
                zza(r1, r0, r12, r2)     // Catch:{ SQLException -> 0x004f, all -> 0x004c }
                if (r10 == 0) goto L_0x005b
                r10.close()
            L_0x005b:
                return r2
            L_0x005c:
                r11 = move-exception
                goto L_0x0081
            L_0x005e:
                r0 = move-exception
                r3 = r2
            L_0x0060:
                java.lang.String r10 = "GoogleSettings"
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x005c }
                java.lang.String r4 = "Can't get key "
                r1.<init>(r4)     // Catch:{ all -> 0x005c }
                r1.append(r12)     // Catch:{ all -> 0x005c }
                java.lang.String r12 = " from "
                r1.append(r12)     // Catch:{ all -> 0x005c }
                r1.append(r11)     // Catch:{ all -> 0x005c }
                java.lang.String r11 = r1.toString()     // Catch:{ all -> 0x005c }
                android.util.Log.e(r10, r11, r0)     // Catch:{ all -> 0x005c }
                if (r2 == 0) goto L_0x0080
                r2.close()
            L_0x0080:
                return r3
            L_0x0081:
                if (r2 == 0) goto L_0x0086
                r2.close()
            L_0x0086:
                throw r11
            L_0x0087:
                r10 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0087 }
                throw r10
            L_0x008a:
                r10 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x008a }
                throw r10
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.stable.zze.zza.zza(android.content.ContentResolver, android.net.Uri, java.lang.String):java.lang.String");
        }

        private static void zza(zzh zzh, Object obj, String str, String str2) {
            synchronized (zzh) {
                if (obj == zzh.zzagt) {
                    zzh.zzags.put(str, str2);
                }
            }
        }
    }
}
