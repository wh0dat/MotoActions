package com.google.android.gms.dynamite;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.DynamiteApi;
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.IDynamiteLoaderV2.Stub;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import javax.annotation.concurrent.GuardedBy;

public final class DynamiteModule {
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION = new zzd();
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING = new zze();
    public static final VersionPolicy PREFER_HIGHEST_OR_REMOTE_VERSION = new zzf();
    public static final VersionPolicy PREFER_HIGHEST_OR_REMOTE_VERSION_NO_FORCE_STAGING = new zzg();
    public static final VersionPolicy PREFER_LOCAL = new zzc();
    public static final VersionPolicy PREFER_REMOTE = new zzb();
    @GuardedBy("DynamiteModule.class")
    private static Boolean zzabr;
    @GuardedBy("DynamiteModule.class")
    private static IDynamiteLoader zzabs;
    @GuardedBy("DynamiteModule.class")
    private static IDynamiteLoaderV2 zzabt;
    @GuardedBy("DynamiteModule.class")
    private static String zzabu;
    private static final ThreadLocal<zza> zzabv = new ThreadLocal<>();
    private static final IVersions zzabw = new zza();
    private final Context zzabx;

    @DynamiteApi
    public static class DynamiteLoaderClassLoader {
        @GuardedBy("DynamiteLoaderClassLoader.class")
        public static ClassLoader sClassLoader;
    }

    public static class LoadingException extends Exception {
        private LoadingException(String str) {
            super(str);
        }

        /* synthetic */ LoadingException(String str, zza zza) {
            this(str);
        }

        private LoadingException(String str, Throwable th) {
            super(str, th);
        }

        /* synthetic */ LoadingException(String str, Throwable th, zza zza) {
            this(str, th);
        }
    }

    public interface VersionPolicy {

        public interface IVersions {
            int getLocalVersion(Context context, String str);

            int getRemoteVersion(Context context, String str, boolean z) throws LoadingException;
        }

        public static class SelectionResult {
            public int localVersion = 0;
            public int remoteVersion = 0;
            public int selection = 0;
        }

        SelectionResult selectModule(Context context, String str, IVersions iVersions) throws LoadingException;
    }

    private static class zza {
        public Cursor zzaby;

        private zza() {
        }

        /* synthetic */ zza(zza zza) {
            this();
        }
    }

    private static class zzb implements IVersions {
        private final int zzabz;
        private final int zzaca = 0;

        public zzb(int i, int i2) {
            this.zzabz = i;
        }

        public final int getLocalVersion(Context context, String str) {
            return this.zzabz;
        }

        public final int getRemoteVersion(Context context, String str, boolean z) {
            return 0;
        }
    }

    private DynamiteModule(Context context) {
        this.zzabx = (Context) Preconditions.checkNotNull(context);
    }

    public static int getLocalVersion(Context context, String str) {
        try {
            ClassLoader classLoader = context.getApplicationContext().getClassLoader();
            StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 61);
            sb.append("com.google.android.gms.dynamite.descriptors.");
            sb.append(str);
            sb.append(".ModuleDescriptor");
            Class loadClass = classLoader.loadClass(sb.toString());
            Field declaredField = loadClass.getDeclaredField("MODULE_ID");
            Field declaredField2 = loadClass.getDeclaredField("MODULE_VERSION");
            if (declaredField.get(null).equals(str)) {
                return declaredField2.getInt(null);
            }
            String valueOf = String.valueOf(declaredField.get(null));
            StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf).length() + 51 + String.valueOf(str).length());
            sb2.append("Module descriptor id '");
            sb2.append(valueOf);
            sb2.append("' didn't match expected id '");
            sb2.append(str);
            sb2.append("'");
            Log.e("DynamiteModule", sb2.toString());
            return 0;
        } catch (ClassNotFoundException unused) {
            StringBuilder sb3 = new StringBuilder(String.valueOf(str).length() + 45);
            sb3.append("Local module descriptor class for ");
            sb3.append(str);
            sb3.append(" not found.");
            Log.w("DynamiteModule", sb3.toString());
            return 0;
        } catch (Exception e) {
            String str2 = "DynamiteModule";
            String str3 = "Failed to load module descriptor class: ";
            String valueOf2 = String.valueOf(e.getMessage());
            Log.e(str2, valueOf2.length() != 0 ? str3.concat(valueOf2) : new String(str3));
            return 0;
        }
    }

    public static Uri getQueryUri(String str, boolean z) {
        String str2 = z ? ProviderConstants.API_PATH_FORCE_STAGING : ProviderConstants.API_PATH;
        StringBuilder sb = new StringBuilder(String.valueOf(str2).length() + 42 + String.valueOf(str).length());
        sb.append("content://com.google.android.gms.chimera/");
        sb.append(str2);
        sb.append("/");
        sb.append(str);
        return Uri.parse(sb.toString());
    }

    public static int getRemoteVersion(Context context, String str) {
        return getRemoteVersion(context, str, false);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(2:39|40) */
    /* JADX WARNING: Can't wrap try/catch for region: R(4:15|16|17|18) */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        r2.set(null, java.lang.ClassLoader.getSystemClassLoader());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0085, code lost:
        r1 = r2;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0035 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:39:0x007c */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00ba A[SYNTHETIC, Splitter:B:55:0x00ba] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x00e1  */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:22:0x0050=Splitter:B:22:0x0050, B:17:0x0035=Splitter:B:17:0x0035, B:34:0x0079=Splitter:B:34:0x0079} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getRemoteVersion(android.content.Context r8, java.lang.String r9, boolean r10) {
        /*
            java.lang.Class<com.google.android.gms.dynamite.DynamiteModule> r0 = com.google.android.gms.dynamite.DynamiteModule.class
            monitor-enter(r0)
            java.lang.Boolean r1 = zzabr     // Catch:{ all -> 0x00e6 }
            if (r1 != 0) goto L_0x00b3
            android.content.Context r1 = r8.getApplicationContext()     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException -> 0x008a }
            java.lang.ClassLoader r1 = r1.getClassLoader()     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException -> 0x008a }
            java.lang.Class<com.google.android.gms.dynamite.DynamiteModule$DynamiteLoaderClassLoader> r2 = com.google.android.gms.dynamite.DynamiteModule.DynamiteLoaderClassLoader.class
            java.lang.String r2 = r2.getName()     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException -> 0x008a }
            java.lang.Class r1 = r1.loadClass(r2)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException -> 0x008a }
            java.lang.String r2 = "sClassLoader"
            java.lang.reflect.Field r2 = r1.getDeclaredField(r2)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException -> 0x008a }
            monitor-enter(r1)     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException -> 0x008a }
            r3 = 0
            java.lang.Object r4 = r2.get(r3)     // Catch:{ all -> 0x0087 }
            java.lang.ClassLoader r4 = (java.lang.ClassLoader) r4     // Catch:{ all -> 0x0087 }
            if (r4 == 0) goto L_0x0038
            java.lang.ClassLoader r2 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ all -> 0x0087 }
            if (r4 != r2) goto L_0x0032
        L_0x002f:
            java.lang.Boolean r2 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x0087 }
            goto L_0x0084
        L_0x0032:
            zza(r4)     // Catch:{ LoadingException -> 0x0035 }
        L_0x0035:
            java.lang.Boolean r2 = java.lang.Boolean.TRUE     // Catch:{ all -> 0x0087 }
            goto L_0x0084
        L_0x0038:
            java.lang.String r4 = "com.google.android.gms"
            android.content.Context r5 = r8.getApplicationContext()     // Catch:{ all -> 0x0087 }
            java.lang.String r5 = r5.getPackageName()     // Catch:{ all -> 0x0087 }
            boolean r4 = r4.equals(r5)     // Catch:{ all -> 0x0087 }
            if (r4 == 0) goto L_0x0050
            java.lang.ClassLoader r4 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ all -> 0x0087 }
            r2.set(r3, r4)     // Catch:{ all -> 0x0087 }
            goto L_0x002f
        L_0x0050:
            int r4 = zzb(r8, r9, r10)     // Catch:{ LoadingException -> 0x007c }
            java.lang.String r5 = zzabu     // Catch:{ LoadingException -> 0x007c }
            if (r5 == 0) goto L_0x0079
            java.lang.String r5 = zzabu     // Catch:{ LoadingException -> 0x007c }
            boolean r5 = r5.isEmpty()     // Catch:{ LoadingException -> 0x007c }
            if (r5 == 0) goto L_0x0061
            goto L_0x0079
        L_0x0061:
            com.google.android.gms.dynamite.zzh r5 = new com.google.android.gms.dynamite.zzh     // Catch:{ LoadingException -> 0x007c }
            java.lang.String r6 = zzabu     // Catch:{ LoadingException -> 0x007c }
            java.lang.ClassLoader r7 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ LoadingException -> 0x007c }
            r5.<init>(r6, r7)     // Catch:{ LoadingException -> 0x007c }
            zza(r5)     // Catch:{ LoadingException -> 0x007c }
            r2.set(r3, r5)     // Catch:{ LoadingException -> 0x007c }
            java.lang.Boolean r5 = java.lang.Boolean.TRUE     // Catch:{ LoadingException -> 0x007c }
            zzabr = r5     // Catch:{ LoadingException -> 0x007c }
            monitor-exit(r1)     // Catch:{ all -> 0x0087 }
            monitor-exit(r0)     // Catch:{ all -> 0x00e6 }
            return r4
        L_0x0079:
            monitor-exit(r1)     // Catch:{ all -> 0x0087 }
            monitor-exit(r0)     // Catch:{ all -> 0x00e6 }
            return r4
        L_0x007c:
            java.lang.ClassLoader r4 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ all -> 0x0087 }
            r2.set(r3, r4)     // Catch:{ all -> 0x0087 }
            goto L_0x002f
        L_0x0084:
            monitor-exit(r1)     // Catch:{ all -> 0x0087 }
            r1 = r2
            goto L_0x00b1
        L_0x0087:
            r2 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0087 }
            throw r2     // Catch:{ ClassNotFoundException | IllegalAccessException | NoSuchFieldException -> 0x008a }
        L_0x008a:
            r1 = move-exception
            java.lang.String r2 = "DynamiteModule"
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ all -> 0x00e6 }
            java.lang.String r3 = java.lang.String.valueOf(r1)     // Catch:{ all -> 0x00e6 }
            int r3 = r3.length()     // Catch:{ all -> 0x00e6 }
            int r3 = r3 + 30
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x00e6 }
            r4.<init>(r3)     // Catch:{ all -> 0x00e6 }
            java.lang.String r3 = "Failed to load module via V2: "
            r4.append(r3)     // Catch:{ all -> 0x00e6 }
            r4.append(r1)     // Catch:{ all -> 0x00e6 }
            java.lang.String r1 = r4.toString()     // Catch:{ all -> 0x00e6 }
            android.util.Log.w(r2, r1)     // Catch:{ all -> 0x00e6 }
            java.lang.Boolean r1 = java.lang.Boolean.FALSE     // Catch:{ all -> 0x00e6 }
        L_0x00b1:
            zzabr = r1     // Catch:{ all -> 0x00e6 }
        L_0x00b3:
            monitor-exit(r0)     // Catch:{ all -> 0x00e6 }
            boolean r0 = r1.booleanValue()
            if (r0 == 0) goto L_0x00e1
            int r8 = zzb(r8, r9, r10)     // Catch:{ LoadingException -> 0x00bf }
            return r8
        L_0x00bf:
            r8 = move-exception
            java.lang.String r9 = "DynamiteModule"
            java.lang.String r10 = "Failed to retrieve remote module version: "
            java.lang.String r8 = r8.getMessage()
            java.lang.String r8 = java.lang.String.valueOf(r8)
            int r0 = r8.length()
            if (r0 == 0) goto L_0x00d7
            java.lang.String r8 = r10.concat(r8)
            goto L_0x00dc
        L_0x00d7:
            java.lang.String r8 = new java.lang.String
            r8.<init>(r10)
        L_0x00dc:
            android.util.Log.w(r9, r8)
            r8 = 0
            return r8
        L_0x00e1:
            int r8 = zza(r8, r9, r10)
            return r8
        L_0x00e6:
            r8 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00e6 }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.getRemoteVersion(android.content.Context, java.lang.String, boolean):int");
    }

    @VisibleForTesting
    public static synchronized Boolean getUseV2ForTesting() {
        Boolean bool;
        synchronized (DynamiteModule.class) {
            bool = zzabr;
        }
        return bool;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0080, code lost:
        if (r1.zzaby != null) goto L_0x0082;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00e1, code lost:
        if (r1.zzaby != null) goto L_0x0082;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.android.gms.dynamite.DynamiteModule load(android.content.Context r10, com.google.android.gms.dynamite.DynamiteModule.VersionPolicy r11, java.lang.String r12) throws com.google.android.gms.dynamite.DynamiteModule.LoadingException {
        /*
            java.lang.ThreadLocal<com.google.android.gms.dynamite.DynamiteModule$zza> r0 = zzabv
            java.lang.Object r0 = r0.get()
            com.google.android.gms.dynamite.DynamiteModule$zza r0 = (com.google.android.gms.dynamite.DynamiteModule.zza) r0
            com.google.android.gms.dynamite.DynamiteModule$zza r1 = new com.google.android.gms.dynamite.DynamiteModule$zza
            r2 = 0
            r1.<init>(r2)
            java.lang.ThreadLocal<com.google.android.gms.dynamite.DynamiteModule$zza> r3 = zzabv
            r3.set(r1)
            com.google.android.gms.dynamite.DynamiteModule$VersionPolicy$IVersions r3 = zzabw     // Catch:{ all -> 0x0131 }
            com.google.android.gms.dynamite.DynamiteModule$VersionPolicy$SelectionResult r3 = r11.selectModule(r10, r12, r3)     // Catch:{ all -> 0x0131 }
            java.lang.String r4 = "DynamiteModule"
            int r5 = r3.localVersion     // Catch:{ all -> 0x0131 }
            int r6 = r3.remoteVersion     // Catch:{ all -> 0x0131 }
            java.lang.String r7 = java.lang.String.valueOf(r12)     // Catch:{ all -> 0x0131 }
            int r7 = r7.length()     // Catch:{ all -> 0x0131 }
            int r7 = r7 + 68
            java.lang.String r8 = java.lang.String.valueOf(r12)     // Catch:{ all -> 0x0131 }
            int r8 = r8.length()     // Catch:{ all -> 0x0131 }
            int r7 = r7 + r8
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x0131 }
            r8.<init>(r7)     // Catch:{ all -> 0x0131 }
            java.lang.String r7 = "Considering local module "
            r8.append(r7)     // Catch:{ all -> 0x0131 }
            r8.append(r12)     // Catch:{ all -> 0x0131 }
            java.lang.String r7 = ":"
            r8.append(r7)     // Catch:{ all -> 0x0131 }
            r8.append(r5)     // Catch:{ all -> 0x0131 }
            java.lang.String r5 = " and remote module "
            r8.append(r5)     // Catch:{ all -> 0x0131 }
            r8.append(r12)     // Catch:{ all -> 0x0131 }
            java.lang.String r5 = ":"
            r8.append(r5)     // Catch:{ all -> 0x0131 }
            r8.append(r6)     // Catch:{ all -> 0x0131 }
            java.lang.String r5 = r8.toString()     // Catch:{ all -> 0x0131 }
            android.util.Log.i(r4, r5)     // Catch:{ all -> 0x0131 }
            int r4 = r3.selection     // Catch:{ all -> 0x0131 }
            if (r4 == 0) goto L_0x0107
            int r4 = r3.selection     // Catch:{ all -> 0x0131 }
            r5 = -1
            if (r4 != r5) goto L_0x006b
            int r4 = r3.localVersion     // Catch:{ all -> 0x0131 }
            if (r4 == 0) goto L_0x0107
        L_0x006b:
            int r4 = r3.selection     // Catch:{ all -> 0x0131 }
            r6 = 1
            if (r4 != r6) goto L_0x0076
            int r4 = r3.remoteVersion     // Catch:{ all -> 0x0131 }
            if (r4 != 0) goto L_0x0076
            goto L_0x0107
        L_0x0076:
            int r4 = r3.selection     // Catch:{ all -> 0x0131 }
            if (r4 != r5) goto L_0x008d
            com.google.android.gms.dynamite.DynamiteModule r10 = zzd(r10, r12)     // Catch:{ all -> 0x0131 }
            android.database.Cursor r11 = r1.zzaby
            if (r11 == 0) goto L_0x0087
        L_0x0082:
            android.database.Cursor r11 = r1.zzaby
            r11.close()
        L_0x0087:
            java.lang.ThreadLocal<com.google.android.gms.dynamite.DynamiteModule$zza> r11 = zzabv
            r11.set(r0)
            return r10
        L_0x008d:
            int r4 = r3.selection     // Catch:{ all -> 0x0131 }
            if (r4 != r6) goto L_0x00ec
            int r4 = r3.remoteVersion     // Catch:{ LoadingException -> 0x00a6 }
            com.google.android.gms.dynamite.DynamiteModule r4 = zza(r10, r12, r4)     // Catch:{ LoadingException -> 0x00a6 }
            android.database.Cursor r10 = r1.zzaby
            if (r10 == 0) goto L_0x00a0
            android.database.Cursor r10 = r1.zzaby
            r10.close()
        L_0x00a0:
            java.lang.ThreadLocal<com.google.android.gms.dynamite.DynamiteModule$zza> r10 = zzabv
            r10.set(r0)
            return r4
        L_0x00a6:
            r4 = move-exception
            java.lang.String r6 = "DynamiteModule"
            java.lang.String r7 = "Failed to load remote module: "
            java.lang.String r8 = r4.getMessage()     // Catch:{ all -> 0x0131 }
            java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch:{ all -> 0x0131 }
            int r9 = r8.length()     // Catch:{ all -> 0x0131 }
            if (r9 == 0) goto L_0x00be
            java.lang.String r7 = r7.concat(r8)     // Catch:{ all -> 0x0131 }
            goto L_0x00c4
        L_0x00be:
            java.lang.String r8 = new java.lang.String     // Catch:{ all -> 0x0131 }
            r8.<init>(r7)     // Catch:{ all -> 0x0131 }
            r7 = r8
        L_0x00c4:
            android.util.Log.w(r6, r7)     // Catch:{ all -> 0x0131 }
            int r6 = r3.localVersion     // Catch:{ all -> 0x0131 }
            if (r6 == 0) goto L_0x00e4
            com.google.android.gms.dynamite.DynamiteModule$zzb r6 = new com.google.android.gms.dynamite.DynamiteModule$zzb     // Catch:{ all -> 0x0131 }
            int r3 = r3.localVersion     // Catch:{ all -> 0x0131 }
            r7 = 0
            r6.<init>(r3, r7)     // Catch:{ all -> 0x0131 }
            com.google.android.gms.dynamite.DynamiteModule$VersionPolicy$SelectionResult r11 = r11.selectModule(r10, r12, r6)     // Catch:{ all -> 0x0131 }
            int r11 = r11.selection     // Catch:{ all -> 0x0131 }
            if (r11 != r5) goto L_0x00e4
            com.google.android.gms.dynamite.DynamiteModule r10 = zzd(r10, r12)     // Catch:{ all -> 0x0131 }
            android.database.Cursor r11 = r1.zzaby
            if (r11 == 0) goto L_0x0087
            goto L_0x0082
        L_0x00e4:
            com.google.android.gms.dynamite.DynamiteModule$LoadingException r10 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException     // Catch:{ all -> 0x0131 }
            java.lang.String r11 = "Remote load failed. No local fallback found."
            r10.<init>(r11, r4, r2)     // Catch:{ all -> 0x0131 }
            throw r10     // Catch:{ all -> 0x0131 }
        L_0x00ec:
            com.google.android.gms.dynamite.DynamiteModule$LoadingException r10 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException     // Catch:{ all -> 0x0131 }
            int r11 = r3.selection     // Catch:{ all -> 0x0131 }
            r12 = 47
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0131 }
            r3.<init>(r12)     // Catch:{ all -> 0x0131 }
            java.lang.String r12 = "VersionPolicy returned invalid code:"
            r3.append(r12)     // Catch:{ all -> 0x0131 }
            r3.append(r11)     // Catch:{ all -> 0x0131 }
            java.lang.String r11 = r3.toString()     // Catch:{ all -> 0x0131 }
            r10.<init>(r11, r2)     // Catch:{ all -> 0x0131 }
            throw r10     // Catch:{ all -> 0x0131 }
        L_0x0107:
            com.google.android.gms.dynamite.DynamiteModule$LoadingException r10 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException     // Catch:{ all -> 0x0131 }
            int r11 = r3.localVersion     // Catch:{ all -> 0x0131 }
            int r12 = r3.remoteVersion     // Catch:{ all -> 0x0131 }
            r3 = 91
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x0131 }
            r4.<init>(r3)     // Catch:{ all -> 0x0131 }
            java.lang.String r3 = "No acceptable module found. Local version is "
            r4.append(r3)     // Catch:{ all -> 0x0131 }
            r4.append(r11)     // Catch:{ all -> 0x0131 }
            java.lang.String r11 = " and remote version is "
            r4.append(r11)     // Catch:{ all -> 0x0131 }
            r4.append(r12)     // Catch:{ all -> 0x0131 }
            java.lang.String r11 = "."
            r4.append(r11)     // Catch:{ all -> 0x0131 }
            java.lang.String r11 = r4.toString()     // Catch:{ all -> 0x0131 }
            r10.<init>(r11, r2)     // Catch:{ all -> 0x0131 }
            throw r10     // Catch:{ all -> 0x0131 }
        L_0x0131:
            r10 = move-exception
            android.database.Cursor r11 = r1.zzaby
            if (r11 == 0) goto L_0x013b
            android.database.Cursor r11 = r1.zzaby
            r11.close()
        L_0x013b:
            java.lang.ThreadLocal<com.google.android.gms.dynamite.DynamiteModule$zza> r11 = zzabv
            r11.set(r0)
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.load(android.content.Context, com.google.android.gms.dynamite.DynamiteModule$VersionPolicy, java.lang.String):com.google.android.gms.dynamite.DynamiteModule");
    }

    public static Cursor queryForDynamiteModule(Context context, String str, boolean z) {
        return context.getContentResolver().query(getQueryUri(str, z), null, null, null, null);
    }

    @VisibleForTesting
    public static synchronized void resetInternalStateForTesting() {
        synchronized (DynamiteModule.class) {
            zzabs = null;
            zzabt = null;
            zzabu = null;
            zzabr = null;
            synchronized (DynamiteLoaderClassLoader.class) {
                DynamiteLoaderClassLoader.sClassLoader = null;
            }
        }
    }

    @VisibleForTesting
    public static synchronized void setUseV2ForTesting(Boolean bool) {
        synchronized (DynamiteModule.class) {
            zzabr = bool;
        }
    }

    private static int zza(Context context, String str, boolean z) {
        IDynamiteLoader zzg = zzg(context);
        if (zzg == null) {
            return 0;
        }
        try {
            return zzg.getModuleVersion2(ObjectWrapper.wrap(context), str, z);
        } catch (RemoteException e) {
            String str2 = "DynamiteModule";
            String str3 = "Failed to retrieve remote module version: ";
            String valueOf = String.valueOf(e.getMessage());
            Log.w(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
            return 0;
        }
    }

    private static Context zza(Context context, String str, int i, Cursor cursor, IDynamiteLoaderV2 iDynamiteLoaderV2) {
        try {
            return (Context) ObjectWrapper.unwrap(iDynamiteLoaderV2.loadModule2(ObjectWrapper.wrap(context), str, i, ObjectWrapper.wrap(cursor)));
        } catch (Exception e) {
            String str2 = "DynamiteModule";
            String str3 = "Failed to load DynamiteLoader: ";
            String valueOf = String.valueOf(e.toString());
            Log.e(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
            return null;
        }
    }

    private static DynamiteModule zza(Context context, String str, int i) throws LoadingException {
        Boolean bool;
        synchronized (DynamiteModule.class) {
            bool = zzabr;
        }
        if (bool != null) {
            return bool.booleanValue() ? zzc(context, str, i) : zzb(context, str, i);
        }
        throw new LoadingException("Failed to determine which loading route to use.", (zza) null);
    }

    @GuardedBy("DynamiteModule.class")
    private static void zza(ClassLoader classLoader) throws LoadingException {
        try {
            zzabt = Stub.asInterface((IBinder) classLoader.loadClass("com.google.android.gms.dynamiteloader.DynamiteLoaderV2").getConstructor(new Class[0]).newInstance(new Object[0]));
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new LoadingException("Failed to instantiate dynamite loader", e, null);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:48:0x0061  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int zzb(android.content.Context r2, java.lang.String r3, boolean r4) throws com.google.android.gms.dynamite.DynamiteModule.LoadingException {
        /*
            r0 = 0
            android.database.Cursor r2 = queryForDynamiteModule(r2, r3, r4)     // Catch:{ Exception -> 0x004f, all -> 0x004c }
            if (r2 == 0) goto L_0x003d
            boolean r3 = r2.moveToFirst()     // Catch:{ Exception -> 0x003b }
            if (r3 != 0) goto L_0x000e
            goto L_0x003d
        L_0x000e:
            r3 = 0
            int r3 = r2.getInt(r3)     // Catch:{ Exception -> 0x003b }
            if (r3 <= 0) goto L_0x0035
            java.lang.Class<com.google.android.gms.dynamite.DynamiteModule> r4 = com.google.android.gms.dynamite.DynamiteModule.class
            monitor-enter(r4)     // Catch:{ Exception -> 0x003b }
            r1 = 2
            java.lang.String r1 = r2.getString(r1)     // Catch:{ all -> 0x0032 }
            zzabu = r1     // Catch:{ all -> 0x0032 }
            monitor-exit(r4)     // Catch:{ all -> 0x0032 }
            java.lang.ThreadLocal<com.google.android.gms.dynamite.DynamiteModule$zza> r4 = zzabv     // Catch:{ Exception -> 0x003b }
            java.lang.Object r4 = r4.get()     // Catch:{ Exception -> 0x003b }
            com.google.android.gms.dynamite.DynamiteModule$zza r4 = (com.google.android.gms.dynamite.DynamiteModule.zza) r4     // Catch:{ Exception -> 0x003b }
            if (r4 == 0) goto L_0x0035
            android.database.Cursor r1 = r4.zzaby     // Catch:{ Exception -> 0x003b }
            if (r1 != 0) goto L_0x0035
            r4.zzaby = r2     // Catch:{ Exception -> 0x003b }
            r2 = r0
            goto L_0x0035
        L_0x0032:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0032 }
            throw r3     // Catch:{ Exception -> 0x003b }
        L_0x0035:
            if (r2 == 0) goto L_0x003a
            r2.close()
        L_0x003a:
            return r3
        L_0x003b:
            r3 = move-exception
            goto L_0x0051
        L_0x003d:
            java.lang.String r3 = "DynamiteModule"
            java.lang.String r4 = "Failed to retrieve remote module version."
            android.util.Log.w(r3, r4)     // Catch:{ Exception -> 0x003b }
            com.google.android.gms.dynamite.DynamiteModule$LoadingException r3 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException     // Catch:{ Exception -> 0x003b }
            java.lang.String r4 = "Failed to connect to dynamite module ContentResolver."
            r3.<init>(r4, r0)     // Catch:{ Exception -> 0x003b }
            throw r3     // Catch:{ Exception -> 0x003b }
        L_0x004c:
            r3 = move-exception
            r2 = r0
            goto L_0x005f
        L_0x004f:
            r3 = move-exception
            r2 = r0
        L_0x0051:
            boolean r4 = r3 instanceof com.google.android.gms.dynamite.DynamiteModule.LoadingException     // Catch:{ all -> 0x005e }
            if (r4 == 0) goto L_0x0056
            throw r3     // Catch:{ all -> 0x005e }
        L_0x0056:
            com.google.android.gms.dynamite.DynamiteModule$LoadingException r4 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException     // Catch:{ all -> 0x005e }
            java.lang.String r1 = "V2 version check failed"
            r4.<init>(r1, r3, r0)     // Catch:{ all -> 0x005e }
            throw r4     // Catch:{ all -> 0x005e }
        L_0x005e:
            r3 = move-exception
        L_0x005f:
            if (r2 == 0) goto L_0x0064
            r2.close()
        L_0x0064:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.zzb(android.content.Context, java.lang.String, boolean):int");
    }

    private static DynamiteModule zzb(Context context, String str, int i) throws LoadingException {
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 51);
        sb.append("Selected remote version of ");
        sb.append(str);
        sb.append(", version >= ");
        sb.append(i);
        Log.i("DynamiteModule", sb.toString());
        IDynamiteLoader zzg = zzg(context);
        if (zzg == null) {
            throw new LoadingException("Failed to create IDynamiteLoader.", (zza) null);
        }
        try {
            IObjectWrapper createModuleContext = zzg.createModuleContext(ObjectWrapper.wrap(context), str, i);
            if (ObjectWrapper.unwrap(createModuleContext) != null) {
                return new DynamiteModule((Context) ObjectWrapper.unwrap(createModuleContext));
            }
            throw new LoadingException("Failed to load remote module.", (zza) null);
        } catch (RemoteException e) {
            throw new LoadingException("Failed to load remote module.", e, null);
        }
    }

    private static DynamiteModule zzc(Context context, String str, int i) throws LoadingException {
        IDynamiteLoaderV2 iDynamiteLoaderV2;
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 51);
        sb.append("Selected remote version of ");
        sb.append(str);
        sb.append(", version >= ");
        sb.append(i);
        Log.i("DynamiteModule", sb.toString());
        synchronized (DynamiteModule.class) {
            iDynamiteLoaderV2 = zzabt;
        }
        if (iDynamiteLoaderV2 == null) {
            throw new LoadingException("DynamiteLoaderV2 was not cached.", (zza) null);
        }
        zza zza2 = (zza) zzabv.get();
        if (zza2 == null || zza2.zzaby == null) {
            throw new LoadingException("No result cursor", (zza) null);
        }
        Context zza3 = zza(context.getApplicationContext(), str, i, zza2.zzaby, iDynamiteLoaderV2);
        if (zza3 != null) {
            return new DynamiteModule(zza3);
        }
        throw new LoadingException("Failed to get module context", (zza) null);
    }

    private static DynamiteModule zzd(Context context, String str) {
        String str2 = "DynamiteModule";
        String str3 = "Selected local version of ";
        String valueOf = String.valueOf(str);
        Log.i(str2, valueOf.length() != 0 ? str3.concat(valueOf) : new String(str3));
        return new DynamiteModule(context.getApplicationContext());
    }

    private static IDynamiteLoader zzg(Context context) {
        synchronized (DynamiteModule.class) {
            if (zzabs != null) {
                IDynamiteLoader iDynamiteLoader = zzabs;
                return iDynamiteLoader;
            } else if (GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context) != 0) {
                return null;
            } else {
                try {
                    IDynamiteLoader asInterface = IDynamiteLoader.Stub.asInterface((IBinder) context.createPackageContext("com.google.android.gms", 3).getClassLoader().loadClass("com.google.android.gms.chimera.container.DynamiteLoaderImpl").newInstance());
                    if (asInterface != null) {
                        zzabs = asInterface;
                        return asInterface;
                    }
                } catch (Exception e) {
                    String str = "DynamiteModule";
                    String str2 = "Failed to load IDynamiteLoader from GmsCore: ";
                    String valueOf = String.valueOf(e.getMessage());
                    Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                }
            }
        }
        return null;
    }

    public final Context getModuleContext() {
        return this.zzabx;
    }

    public final IBinder instantiate(String str) throws LoadingException {
        try {
            return (IBinder) this.zzabx.getClassLoader().loadClass(str).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            String str2 = "Failed to instantiate module class: ";
            String valueOf = String.valueOf(str);
            throw new LoadingException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2), e, null);
        }
    }
}
