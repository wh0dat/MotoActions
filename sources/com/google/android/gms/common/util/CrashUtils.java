package com.google.android.gms.common.util;

import android.content.Context;
import android.os.DropBoxManager;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.annotation.concurrent.GuardedBy;

public final class CrashUtils {
    private static final String[] zzzc = {"android.", "com.android.", "dalvik.", "java.", "javax."};
    private static DropBoxManager zzzd = null;
    private static boolean zzze = false;
    private static boolean zzzf = false;
    private static boolean zzzg = false;
    private static int zzzh = -1;
    @GuardedBy("CrashUtils.class")
    private static int zzzi;
    @GuardedBy("CrashUtils.class")
    private static int zzzj;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorDialogData {
        public static final int AVG_CRASH_FREQ = 2;
        public static final int BINDER_CRASH = 268435456;
        public static final int DYNAMITE_CRASH = 536870912;
        public static final int FORCED_SHUSHED_BY_WRAPPER = 4;
        public static final int NONE = 0;
        public static final int POPUP_FREQ = 1;
        public static final int SUPPRESSED = 1073741824;
    }

    public static boolean addDynamiteErrorToDropBox(Context context, Throwable th) {
        return addErrorToDropBoxInternal(context, th, ErrorDialogData.DYNAMITE_CRASH);
    }

    @Deprecated
    public static boolean addErrorToDropBox(Context context, Throwable th) {
        return addDynamiteErrorToDropBox(context, th);
    }

    public static boolean addErrorToDropBoxInternal(Context context, String str, String str2, int i) {
        return zza(context, str, str2, i, null);
    }

    public static boolean addErrorToDropBoxInternal(Context context, Throwable th, int i) {
        boolean z;
        try {
            Preconditions.checkNotNull(context);
            Preconditions.checkNotNull(th);
            if (!isPackageSide()) {
                return false;
            }
            if (!zzdb()) {
                th = zza(th);
                if (th == null) {
                    return false;
                }
            }
            return zza(context, Log.getStackTraceString(th), ProcessUtils.getMyProcessName(), i, th);
        } catch (Exception e) {
            try {
                z = zzdb();
            } catch (Exception e2) {
                Log.e("CrashUtils", "Error determining which process we're running in!", e2);
                z = false;
            }
            if (z) {
                throw e;
            }
            Log.e("CrashUtils", "Error adding exception to DropBox!", e);
            return false;
        }
    }

    private static boolean isPackageSide() {
        if (zzze) {
            return zzzf;
        }
        return false;
    }

    public static boolean isSystemClassPrefixInternal(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (String startsWith : zzzc) {
            if (str.startsWith(startsWith)) {
                return true;
            }
        }
        return false;
    }

    @VisibleForTesting
    public static synchronized void setTestVariables(DropBoxManager dropBoxManager, boolean z, boolean z2, int i) {
        synchronized (CrashUtils.class) {
            zzze = true;
            zzzd = dropBoxManager;
            zzzg = z;
            zzzf = z2;
            zzzh = i;
            zzzi = 0;
            zzzj = 0;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:49|50|51|52|53|54|55|56|57|(4:58|59|(2:60|(1:62)(1:63))|64)) */
    /* JADX WARNING: Can't wrap try/catch for region: R(15:47|48|49|50|51|52|53|54|55|56|57|58|59|(2:60|(1:62)(1:63))|64) */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:69|(2:78|79)|80|81) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:53:0x0152 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:55:0x0159 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:80:0x018f */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00b0  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00b7  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00cf  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00d8  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ea  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00ed  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x016c A[Catch:{ IOException -> 0x0177, all -> 0x0174 }, LOOP:0: B:60:0x0166->B:62:0x016c, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0170 A[EDGE_INSN: B:63:0x0170->B:64:? ?: BREAK  
    EDGE_INSN: B:63:0x0170->B:64:? ?: BREAK  
    EDGE_INSN: B:63:0x0170->B:64:? ?: BREAK  , SYNTHETIC, Splitter:B:63:0x0170] */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0170 A[EDGE_INSN: B:63:0x0170->B:64:? ?: BREAK  
    EDGE_INSN: B:63:0x0170->B:64:? ?: BREAK  , SYNTHETIC, Splitter:B:63:0x0170] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0186 A[SYNTHETIC, Splitter:B:75:0x0186] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x018c A[SYNTHETIC, Splitter:B:78:0x018c] */
    @com.google.android.gms.common.util.VisibleForTesting
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized java.lang.String zza(android.content.Context r7, java.lang.String r8, java.lang.String r9, int r10) {
        /*
            java.lang.Class<com.google.android.gms.common.util.CrashUtils> r0 = com.google.android.gms.common.util.CrashUtils.class
            monitor-enter(r0)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0196 }
            r2 = 1024(0x400, float:1.435E-42)
            r1.<init>(r2)     // Catch:{ all -> 0x0196 }
            java.lang.String r2 = "Process: "
            r1.append(r2)     // Catch:{ all -> 0x0196 }
            java.lang.String r9 = com.google.android.gms.common.util.Strings.nullToEmpty(r9)     // Catch:{ all -> 0x0196 }
            r1.append(r9)     // Catch:{ all -> 0x0196 }
            java.lang.String r9 = "\n"
            r1.append(r9)     // Catch:{ all -> 0x0196 }
            java.lang.String r9 = "Package: com.google.android.gms"
            r1.append(r9)     // Catch:{ all -> 0x0196 }
            r9 = 12451009(0xbdfcc1, float:1.744758E-38)
            java.lang.String r2 = "12.4.51 (020308-{{cl}})"
            boolean r3 = zzdb()     // Catch:{ all -> 0x0196 }
            r4 = 0
            if (r3 == 0) goto L_0x004f
            com.google.android.gms.common.wrappers.PackageManagerWrapper r3 = com.google.android.gms.common.wrappers.Wrappers.packageManager(r7)     // Catch:{ Exception -> 0x0047 }
            java.lang.String r5 = r7.getPackageName()     // Catch:{ Exception -> 0x0047 }
            android.content.pm.PackageInfo r3 = r3.getPackageInfo(r5, r4)     // Catch:{ Exception -> 0x0047 }
            int r5 = r3.versionCode     // Catch:{ Exception -> 0x0047 }
            java.lang.String r9 = r3.versionName     // Catch:{ Exception -> 0x0043 }
            if (r9 == 0) goto L_0x0041
            java.lang.String r9 = r3.versionName     // Catch:{ Exception -> 0x0043 }
            r2 = r9
        L_0x0041:
            r9 = r5
            goto L_0x004f
        L_0x0043:
            r9 = move-exception
            r3 = r9
            r9 = r5
            goto L_0x0048
        L_0x0047:
            r3 = move-exception
        L_0x0048:
            java.lang.String r5 = "CrashUtils"
            java.lang.String r6 = "Error while trying to get the package information! Using static version."
            android.util.Log.w(r5, r6, r3)     // Catch:{ all -> 0x0196 }
        L_0x004f:
            java.lang.String r3 = " v"
            r1.append(r3)     // Catch:{ all -> 0x0196 }
            r1.append(r9)     // Catch:{ all -> 0x0196 }
            boolean r9 = android.text.TextUtils.isEmpty(r2)     // Catch:{ all -> 0x0196 }
            if (r9 != 0) goto L_0x0096
            java.lang.String r9 = "("
            boolean r9 = r2.contains(r9)     // Catch:{ all -> 0x0196 }
            if (r9 == 0) goto L_0x0089
            java.lang.String r9 = ")"
            boolean r9 = r2.contains(r9)     // Catch:{ all -> 0x0196 }
            if (r9 != 0) goto L_0x0089
            java.lang.String r9 = "-"
            boolean r9 = r2.endsWith(r9)     // Catch:{ all -> 0x0196 }
            if (r9 == 0) goto L_0x007f
            java.lang.String r9 = java.lang.String.valueOf(r2)     // Catch:{ all -> 0x0196 }
            java.lang.String r2 = "111111111"
            java.lang.String r2 = r9.concat(r2)     // Catch:{ all -> 0x0196 }
        L_0x007f:
            java.lang.String r9 = java.lang.String.valueOf(r2)     // Catch:{ all -> 0x0196 }
            java.lang.String r2 = ")"
            java.lang.String r2 = r9.concat(r2)     // Catch:{ all -> 0x0196 }
        L_0x0089:
            java.lang.String r9 = " ("
            r1.append(r9)     // Catch:{ all -> 0x0196 }
            r1.append(r2)     // Catch:{ all -> 0x0196 }
            java.lang.String r9 = ")"
            r1.append(r9)     // Catch:{ all -> 0x0196 }
        L_0x0096:
            java.lang.String r9 = "\n"
            r1.append(r9)     // Catch:{ all -> 0x0196 }
            java.lang.String r9 = "Build: "
            r1.append(r9)     // Catch:{ all -> 0x0196 }
            java.lang.String r9 = android.os.Build.FINGERPRINT     // Catch:{ all -> 0x0196 }
            r1.append(r9)     // Catch:{ all -> 0x0196 }
            java.lang.String r9 = "\n"
            r1.append(r9)     // Catch:{ all -> 0x0196 }
            boolean r9 = android.os.Debug.isDebuggerConnected()     // Catch:{ all -> 0x0196 }
            if (r9 == 0) goto L_0x00b5
            java.lang.String r9 = "Debugger: Connected\n"
            r1.append(r9)     // Catch:{ all -> 0x0196 }
        L_0x00b5:
            if (r10 == 0) goto L_0x00c4
            java.lang.String r9 = "DD-EDD: "
            r1.append(r9)     // Catch:{ all -> 0x0196 }
            r1.append(r10)     // Catch:{ all -> 0x0196 }
            java.lang.String r9 = "\n"
            r1.append(r9)     // Catch:{ all -> 0x0196 }
        L_0x00c4:
            java.lang.String r9 = "\n"
            r1.append(r9)     // Catch:{ all -> 0x0196 }
            boolean r9 = android.text.TextUtils.isEmpty(r8)     // Catch:{ all -> 0x0196 }
            if (r9 != 0) goto L_0x00d2
            r1.append(r8)     // Catch:{ all -> 0x0196 }
        L_0x00d2:
            boolean r8 = zzdb()     // Catch:{ all -> 0x0196 }
            if (r8 == 0) goto L_0x00ea
            java.lang.String r8 = "logcat_for_system_app_crash"
            int r9 = zzzh     // Catch:{ all -> 0x0196 }
            if (r9 < 0) goto L_0x00e1
            int r7 = zzzh     // Catch:{ all -> 0x0196 }
            goto L_0x00eb
        L_0x00e1:
            android.content.ContentResolver r7 = r7.getContentResolver()     // Catch:{ all -> 0x0196 }
            int r7 = android.provider.Settings.Secure.getInt(r7, r8, r4)     // Catch:{ all -> 0x0196 }
            goto L_0x00eb
        L_0x00ea:
            r7 = r4
        L_0x00eb:
            if (r7 <= 0) goto L_0x0190
            java.lang.String r8 = "\n"
            r1.append(r8)     // Catch:{ all -> 0x0196 }
            r8 = 0
            java.lang.ProcessBuilder r9 = new java.lang.ProcessBuilder     // Catch:{ IOException -> 0x017c }
            r10 = 13
            java.lang.String[] r10 = new java.lang.String[r10]     // Catch:{ IOException -> 0x017c }
            java.lang.String r2 = "/system/bin/logcat"
            r10[r4] = r2     // Catch:{ IOException -> 0x017c }
            java.lang.String r2 = "-v"
            r3 = 1
            r10[r3] = r2     // Catch:{ IOException -> 0x017c }
            r2 = 2
            java.lang.String r5 = "time"
            r10[r2] = r5     // Catch:{ IOException -> 0x017c }
            r2 = 3
            java.lang.String r5 = "-b"
            r10[r2] = r5     // Catch:{ IOException -> 0x017c }
            r2 = 4
            java.lang.String r5 = "events"
            r10[r2] = r5     // Catch:{ IOException -> 0x017c }
            r2 = 5
            java.lang.String r5 = "-b"
            r10[r2] = r5     // Catch:{ IOException -> 0x017c }
            r2 = 6
            java.lang.String r5 = "system"
            r10[r2] = r5     // Catch:{ IOException -> 0x017c }
            r2 = 7
            java.lang.String r5 = "-b"
            r10[r2] = r5     // Catch:{ IOException -> 0x017c }
            r2 = 8
            java.lang.String r5 = "main"
            r10[r2] = r5     // Catch:{ IOException -> 0x017c }
            r2 = 9
            java.lang.String r5 = "-b"
            r10[r2] = r5     // Catch:{ IOException -> 0x017c }
            r2 = 10
            java.lang.String r5 = "crash"
            r10[r2] = r5     // Catch:{ IOException -> 0x017c }
            r2 = 11
            java.lang.String r5 = "-t"
            r10[r2] = r5     // Catch:{ IOException -> 0x017c }
            r2 = 12
            java.lang.String r7 = java.lang.String.valueOf(r7)     // Catch:{ IOException -> 0x017c }
            r10[r2] = r7     // Catch:{ IOException -> 0x017c }
            r9.<init>(r10)     // Catch:{ IOException -> 0x017c }
            java.lang.ProcessBuilder r7 = r9.redirectErrorStream(r3)     // Catch:{ IOException -> 0x017c }
            java.lang.Process r7 = r7.start()     // Catch:{ IOException -> 0x017c }
            java.io.OutputStream r9 = r7.getOutputStream()     // Catch:{ IOException -> 0x0152 }
            r9.close()     // Catch:{ IOException -> 0x0152 }
        L_0x0152:
            java.io.InputStream r9 = r7.getErrorStream()     // Catch:{ IOException -> 0x0159 }
            r9.close()     // Catch:{ IOException -> 0x0159 }
        L_0x0159:
            java.io.InputStreamReader r9 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x017c }
            java.io.InputStream r7 = r7.getInputStream()     // Catch:{ IOException -> 0x017c }
            r9.<init>(r7)     // Catch:{ IOException -> 0x017c }
            r7 = 8192(0x2000, float:1.14794E-41)
            char[] r7 = new char[r7]     // Catch:{ IOException -> 0x0177, all -> 0x0174 }
        L_0x0166:
            int r8 = r9.read(r7)     // Catch:{ IOException -> 0x0177, all -> 0x0174 }
            if (r8 <= 0) goto L_0x0170
            r1.append(r7, r4, r8)     // Catch:{ IOException -> 0x0177, all -> 0x0174 }
            goto L_0x0166
        L_0x0170:
            r9.close()     // Catch:{ IOException -> 0x0190 }
            goto L_0x0190
        L_0x0174:
            r7 = move-exception
            r8 = r9
            goto L_0x018a
        L_0x0177:
            r7 = move-exception
            r8 = r9
            goto L_0x017d
        L_0x017a:
            r7 = move-exception
            goto L_0x018a
        L_0x017c:
            r7 = move-exception
        L_0x017d:
            java.lang.String r9 = "CrashUtils"
            java.lang.String r10 = "Error running logcat"
            android.util.Log.e(r9, r10, r7)     // Catch:{ all -> 0x017a }
            if (r8 == 0) goto L_0x0190
            r8.close()     // Catch:{ IOException -> 0x0190 }
            goto L_0x0190
        L_0x018a:
            if (r8 == 0) goto L_0x018f
            r8.close()     // Catch:{ IOException -> 0x018f }
        L_0x018f:
            throw r7     // Catch:{ all -> 0x0196 }
        L_0x0190:
            java.lang.String r7 = r1.toString()     // Catch:{ all -> 0x0196 }
            monitor-exit(r0)
            return r7
        L_0x0196:
            r7 = move-exception
            monitor-exit(r0)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.CrashUtils.zza(android.content.Context, java.lang.String, java.lang.String, int):java.lang.String");
    }

    @VisibleForTesting
    private static synchronized Throwable zza(Throwable th) {
        synchronized (CrashUtils.class) {
            LinkedList linkedList = new LinkedList();
            while (th != null) {
                linkedList.push(th);
                th = th.getCause();
            }
            Throwable th2 = null;
            boolean z = false;
            while (!linkedList.isEmpty()) {
                Throwable th3 = (Throwable) linkedList.pop();
                StackTraceElement[] stackTrace = th3.getStackTrace();
                ArrayList arrayList = new ArrayList();
                arrayList.add(new StackTraceElement(th3.getClass().getName(), "<filtered>", "<filtered>", 1));
                boolean z2 = z;
                for (StackTraceElement stackTraceElement : stackTrace) {
                    String className = stackTraceElement.getClassName();
                    String fileName = stackTraceElement.getFileName();
                    boolean z3 = !TextUtils.isEmpty(fileName) && fileName.startsWith(":com.google.android.gms");
                    z2 |= z3;
                    if (!z3 && !isSystemClassPrefixInternal(className)) {
                        stackTraceElement = new StackTraceElement("<filtered>", "<filtered>", "<filtered>", 1);
                    }
                    arrayList.add(stackTraceElement);
                }
                th2 = th2 == null ? new Throwable("<filtered>") : new Throwable("<filtered>", th2);
                th2.setStackTrace((StackTraceElement[]) arrayList.toArray(new StackTraceElement[0]));
                z = z2;
            }
            if (!z) {
                return null;
            }
            return th2;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0056, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x0058, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized boolean zza(android.content.Context r4, java.lang.String r5, java.lang.String r6, int r7, java.lang.Throwable r8) {
        /*
            java.lang.Class<com.google.android.gms.common.util.CrashUtils> r0 = com.google.android.gms.common.util.CrashUtils.class
            monitor-enter(r0)
            com.google.android.gms.common.internal.Preconditions.checkNotNull(r4)     // Catch:{ all -> 0x0059 }
            boolean r1 = isPackageSide()     // Catch:{ all -> 0x0059 }
            r2 = 0
            if (r1 == 0) goto L_0x0057
            boolean r1 = com.google.android.gms.common.util.Strings.isEmptyOrWhitespace(r5)     // Catch:{ all -> 0x0059 }
            if (r1 == 0) goto L_0x0014
            goto L_0x0057
        L_0x0014:
            int r1 = r5.hashCode()     // Catch:{ all -> 0x0059 }
            if (r8 != 0) goto L_0x001d
            int r8 = zzzj     // Catch:{ all -> 0x0059 }
            goto L_0x0021
        L_0x001d:
            int r8 = r8.hashCode()     // Catch:{ all -> 0x0059 }
        L_0x0021:
            int r3 = zzzi     // Catch:{ all -> 0x0059 }
            if (r3 != r1) goto L_0x002b
            int r3 = zzzj     // Catch:{ all -> 0x0059 }
            if (r3 != r8) goto L_0x002b
            monitor-exit(r0)
            return r2
        L_0x002b:
            zzzi = r1     // Catch:{ all -> 0x0059 }
            zzzj = r8     // Catch:{ all -> 0x0059 }
            android.os.DropBoxManager r8 = zzzd     // Catch:{ all -> 0x0059 }
            if (r8 == 0) goto L_0x0036
            android.os.DropBoxManager r8 = zzzd     // Catch:{ all -> 0x0059 }
            goto L_0x003e
        L_0x0036:
            java.lang.String r8 = "dropbox"
            java.lang.Object r8 = r4.getSystemService(r8)     // Catch:{ all -> 0x0059 }
            android.os.DropBoxManager r8 = (android.os.DropBoxManager) r8     // Catch:{ all -> 0x0059 }
        L_0x003e:
            if (r8 == 0) goto L_0x0055
            java.lang.String r1 = "system_app_crash"
            boolean r1 = r8.isTagEnabled(r1)     // Catch:{ all -> 0x0059 }
            if (r1 != 0) goto L_0x0049
            goto L_0x0055
        L_0x0049:
            java.lang.String r4 = zza(r4, r5, r6, r7)     // Catch:{ all -> 0x0059 }
            java.lang.String r5 = "system_app_crash"
            r8.addText(r5, r4)     // Catch:{ all -> 0x0059 }
            r4 = 1
            monitor-exit(r0)
            return r4
        L_0x0055:
            monitor-exit(r0)
            return r2
        L_0x0057:
            monitor-exit(r0)
            return r2
        L_0x0059:
            r4 = move-exception
            monitor-exit(r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.util.CrashUtils.zza(android.content.Context, java.lang.String, java.lang.String, int, java.lang.Throwable):boolean");
    }

    private static boolean zzdb() {
        if (zzze) {
            return zzzg;
        }
        return false;
    }
}
