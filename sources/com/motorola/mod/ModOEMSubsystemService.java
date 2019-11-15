package com.motorola.mod;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.system.OsConstants;
import android.util.Log;
import com.motorola.mod.IModManager.Stub;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ModOEMSubsystemService extends Service {
    public static final long DEFAULT_FIRMWARE_UPDATE_TIMEOUT = 240000;
    public static final String SUBSYSTEM_PROPERTY_KEY_LOGS = "logs";
    public static final String SUBSYSTEM_PROPERTY_KEY_STATUS = "status";
    public static final String SUBSYSTEM_PROPERTY_KEY_VERSION = "version";
    public static final String SUBSYSTEM_STATUS_IDLE = "idle";
    public static final String SUBSYSTEM_STATUS_IN_UPDATING = "in_updating";
    public static final String SUBSYSTEM_STATUS_IN_USE = "in_use";
    public static final String SUBSYSTEM_STATUS_UNKNOWN = "unknown";

    /* renamed from: a */
    private static final boolean f177a = (!Build.USER.equals(Build.TYPE));

    /* renamed from: b */
    private Handler f178b;

    /* renamed from: c */
    private HandlerThread f179c;

    /* renamed from: d */
    private long f180d = DEFAULT_FIRMWARE_UPDATE_TIMEOUT;

    /* renamed from: e */
    private IBinder f181e;

    /* access modifiers changed from: protected */
    public abstract String getSubsystemProperty(int i, int i2, UUID uuid, String str, ModManager modManager);

    public IBinder onBind(Intent intent) {
        return null;
    }

    /* access modifiers changed from: protected */
    public abstract int onOEMImages(int i, int i2, UUID uuid, List<ParcelFileDescriptor> list, List<Integer> list2, ModManager modManager);

    /* access modifiers changed from: protected */
    public abstract int setSubsystemProperty(int i, int i2, UUID uuid, String str, String str2, ModManager modManager);

    public void setTimeout(long j) {
        this.f180d = j;
        IBinder iBinder = this.f181e;
        if (iBinder != null) {
            Parcel obtain = Parcel.obtain();
            Parcel obtain2 = Parcel.obtain();
            obtain.writeLong(j);
            try {
                iBinder.transact(2, obtain, obtain2, 0);
            } catch (RemoteException unused) {
            }
            obtain2.readException();
            obtain.recycle();
            obtain2.recycle();
        }
    }

    public void onCreate() {
        super.onCreate();
        m73a();
    }

    public void onDestroy() {
        super.onDestroy();
        this.f179c.quitSafely();
    }

    /* renamed from: a */
    private void m73a() {
        this.f179c = new HandlerThread("oemsubsyssvc");
        this.f179c.start();
        this.f178b = new Handler(this.f179c.getLooper());
    }

    public int onStartCommand(final Intent intent, int i, int i2) {
        if (this.f179c == null) {
            m73a();
        }
        if (ModManager.ACTION_OEM_SUBSYSTEM.equals(intent.getAction())) {
            String stringExtra = intent.getStringExtra(ModManager.EXTRA_SUBSYSTEM_ACTION);
            if (f177a) {
                StringBuilder sb = new StringBuilder();
                sb.append("onStartCommand: ");
                sb.append(stringExtra);
                Log.i("ModOEMSubsysSvc", sb.toString());
            }
            if (ModManager.ACTION_OEM_SUBSYSTEM_UPDATE.equals(stringExtra)) {
                this.f178b.post(new Runnable() {
                    public void run() {
                        ModOEMSubsystemService.this.m75a(intent);
                    }
                });
            } else if (ModManager.ACTION_OEM_SUBSYSTEM_GET.equals(stringExtra)) {
                this.f178b.post(new Runnable() {
                    public void run() {
                        ModOEMSubsystemService.this.m77b(intent);
                    }
                });
            } else if (ModManager.ACTION_OEM_SUBSYSTEM_SET.equals(stringExtra)) {
                this.f178b.post(new Runnable() {
                    public void run() {
                        ModOEMSubsystemService.this.m79c(intent);
                    }
                });
            }
        }
        return 2;
    }

    /* access modifiers changed from: private */
    /* renamed from: a */
    public void m75a(Intent intent) {
        ArrayList arrayList;
        int i;
        UUID uuid;
        Intent intent2 = intent;
        int i2 = OsConstants.EINVAL;
        int intExtra = intent2.getIntExtra(ModManager.EXTRA_SEQUENCE_NUMBER, -1);
        int intExtra2 = intent2.getIntExtra("vid", -1);
        int intExtra3 = intent2.getIntExtra("pid", -1);
        String stringExtra = intent2.getStringExtra(ModManager.EXTRA_PACKAGE);
        ParcelUuid parcelUuid = (ParcelUuid) intent2.getParcelableExtra("uid");
        Bundle bundleExtra = intent2.getBundleExtra(ModManager.EXTRA_FD_LIST);
        IBinder binder = bundleExtra.getBinder(ModManager.EXTRA_FD_LIST);
        ArrayList integerArrayListExtra = intent2.getIntegerArrayListExtra(ModManager.EXTRA_FILE_SIZES);
        PendingIntent pendingIntent = (PendingIntent) intent2.getParcelableExtra(ModManager.EXTRA_RESULT_RECEIVER);
        this.f181e = binder;
        setTimeout(this.f180d);
        Parcel obtain = Parcel.obtain();
        Parcel obtain2 = Parcel.obtain();
        int i3 = 0;
        try {
            binder.transact(1, obtain, obtain2, 0);
            int readInt = obtain2.readInt();
            ArrayList arrayList2 = new ArrayList();
            while (i3 < readInt) {
                int i4 = readInt;
                arrayList2.add(obtain2.readFileDescriptor());
                i3++;
                readInt = i4;
            }
            obtain2.readException();
            obtain.recycle();
            obtain2.recycle();
            if (arrayList2 == null || arrayList2.size() == 0 || integerArrayListExtra == null || integerArrayListExtra.size() != arrayList2.size()) {
                m74a(intExtra, i2, stringExtra, arrayList2, pendingIntent);
                return;
            }
            if (intExtra2 == -1 || intExtra3 == -1) {
                arrayList = arrayList2;
            } else {
                try {
                    ModManager modManager = new ModManager(this, Stub.asInterface(bundleExtra.getBinder(ModManager.EXTRA_MOD_MANAGER)));
                    if (parcelUuid == null) {
                        uuid = null;
                    } else {
                        uuid = parcelUuid.getUuid();
                    }
                    ArrayList arrayList3 = integerArrayListExtra;
                    arrayList = arrayList2;
                    try {
                        i = onOEMImages(intExtra2, intExtra3, uuid, arrayList2, arrayList3, modManager);
                    } catch (Exception e) {
                        e = e;
                        Log.e("ModOEMSubsysSvc", "Exception in handling OEM images", e);
                        i = i2;
                        m74a(intExtra, i, stringExtra, arrayList, pendingIntent);
                    }
                } catch (Exception e2) {
                    e = e2;
                    arrayList = arrayList2;
                    Log.e("ModOEMSubsysSvc", "Exception in handling OEM images", e);
                    i = i2;
                    m74a(intExtra, i, stringExtra, arrayList, pendingIntent);
                }
                m74a(intExtra, i, stringExtra, arrayList, pendingIntent);
            }
            i = i2;
            m74a(intExtra, i, stringExtra, arrayList, pendingIntent);
        } catch (RemoteException unused) {
            m74a(intExtra, i2, stringExtra, null, pendingIntent);
            obtain.recycle();
            obtain2.recycle();
        }
    }

    /* renamed from: a */
    private void m74a(int i, int i2, String str, List<ParcelFileDescriptor> list, PendingIntent pendingIntent) {
        if (pendingIntent != null) {
            try {
                Intent intent = new Intent();
                intent.putExtra(ModManager.EXTRA_RESULT_CODE, i2);
                intent.putExtra(ModManager.EXTRA_SEQUENCE_NUMBER, i);
                intent.putExtra(ModManager.EXTRA_PACKAGE, str);
                pendingIntent.send(this, i2, intent);
            } catch (CanceledException unused) {
                Log.e("ModOEMSubsysSvc", "The result receiver is gone");
            }
        }
        if (list != null) {
            for (ParcelFileDescriptor close : list) {
                try {
                    close.close();
                } catch (Exception unused2) {
                }
            }
        }
        this.f181e = null;
    }

    /* access modifiers changed from: private */
    /* renamed from: b */
    public void m77b(Intent intent) {
        UUID uuid;
        Intent intent2 = intent;
        int intExtra = intent2.getIntExtra(ModManager.EXTRA_SEQUENCE_NUMBER, -1);
        int intExtra2 = intent2.getIntExtra("vid", -1);
        int intExtra3 = intent2.getIntExtra("pid", -1);
        ParcelUuid parcelUuid = (ParcelUuid) intent2.getParcelableExtra("uid");
        String stringExtra = intent2.getStringExtra(ModManager.EXTRA_PACKAGE);
        PendingIntent pendingIntent = (PendingIntent) intent2.getParcelableExtra(ModManager.EXTRA_RESULT_RECEIVER);
        String stringExtra2 = intent2.getStringExtra(ModManager.EXTRA_MOD_PROPERTY);
        Bundle bundleExtra = intent2.getBundleExtra(ModManager.EXTRA_MOD_MANAGER);
        String str = null;
        if (!(intExtra2 == -1 || intExtra3 == -1)) {
            try {
                ModManager modManager = new ModManager(this, Stub.asInterface(bundleExtra.getBinder(ModManager.EXTRA_MOD_MANAGER)));
                if (parcelUuid == null) {
                    uuid = null;
                } else {
                    uuid = parcelUuid.getUuid();
                }
                str = getSubsystemProperty(intExtra2, intExtra3, uuid, stringExtra2, modManager);
            } catch (Exception e) {
                Log.e("ModOEMSubsysSvc", "Exception in handling OEM get", e);
            }
        }
        if (pendingIntent != null) {
            try {
                Intent intent3 = new Intent();
                intent3.putExtra(ModManager.EXTRA_RESULT_CODE, str);
                intent3.putExtra("vid", intExtra2);
                intent3.putExtra("pid", intExtra3);
                intent3.putExtra("uid", parcelUuid);
                intent3.putExtra(ModManager.EXTRA_PACKAGE, stringExtra);
                intent3.putExtra(ModManager.EXTRA_MOD_PROPERTY, stringExtra2);
                intent3.putExtra(ModManager.EXTRA_SEQUENCE_NUMBER, intExtra);
                pendingIntent.send(this, 0, intent3);
            } catch (CanceledException unused) {
                Log.e("ModOEMSubsysSvc", "The result receiver is gone");
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0081 A[SYNTHETIC, Splitter:B:20:0x0081] */
    /* JADX WARNING: Removed duplicated region for block: B:25:? A[RETURN, SYNTHETIC] */
    /* renamed from: c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void m79c(android.content.Intent r20) {
        /*
            r19 = this;
            r8 = r19
            r1 = r20
            java.lang.String r2 = "seq"
            r3 = -1
            int r9 = r1.getIntExtra(r2, r3)
            java.lang.String r2 = "vid"
            int r10 = r1.getIntExtra(r2, r3)
            java.lang.String r2 = "pid"
            int r11 = r1.getIntExtra(r2, r3)
            java.lang.String r2 = "uid"
            android.os.Parcelable r2 = r1.getParcelableExtra(r2)
            r12 = r2
            android.os.ParcelUuid r12 = (android.os.ParcelUuid) r12
            java.lang.String r2 = "package"
            java.lang.String r13 = r1.getStringExtra(r2)
            java.lang.String r2 = "receiver"
            android.os.Parcelable r2 = r1.getParcelableExtra(r2)
            r14 = r2
            android.app.PendingIntent r14 = (android.app.PendingIntent) r14
            java.lang.String r2 = "property"
            java.lang.String r15 = r1.getStringExtra(r2)
            java.lang.String r2 = "value"
            java.lang.String r7 = r1.getStringExtra(r2)
            java.lang.String r2 = "modmanager"
            android.os.Bundle r1 = r1.getBundleExtra(r2)
            int r16 = android.system.OsConstants.EINVAL
            if (r10 == r3) goto L_0x007c
            if (r11 == r3) goto L_0x007c
            java.lang.String r2 = "modmanager"
            android.os.IBinder r1 = r1.getBinder(r2)     // Catch:{ Exception -> 0x0071 }
            com.motorola.mod.ModManager r6 = new com.motorola.mod.ModManager     // Catch:{ Exception -> 0x0071 }
            com.motorola.mod.IModManager r1 = com.motorola.mod.IModManager.Stub.asInterface(r1)     // Catch:{ Exception -> 0x0071 }
            r6.<init>(r8, r1)     // Catch:{ Exception -> 0x0071 }
            if (r12 != 0) goto L_0x005b
            r1 = 0
        L_0x0059:
            r4 = r1
            goto L_0x0060
        L_0x005b:
            java.util.UUID r1 = r12.getUuid()     // Catch:{ Exception -> 0x0071 }
            goto L_0x0059
        L_0x0060:
            r1 = r8
            r2 = r10
            r3 = r11
            r5 = r15
            r17 = r6
            r6 = r7
            r8 = r7
            r7 = r17
            int r1 = r1.setSubsystemProperty(r2, r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x006f }
            goto L_0x007f
        L_0x006f:
            r0 = move-exception
            goto L_0x0073
        L_0x0071:
            r0 = move-exception
            r8 = r7
        L_0x0073:
            r1 = r0
            java.lang.String r2 = "ModOEMSubsysSvc"
            java.lang.String r3 = "Exception in handling OEM set"
            android.util.Log.e(r2, r3, r1)
            goto L_0x007d
        L_0x007c:
            r8 = r7
        L_0x007d:
            r1 = r16
        L_0x007f:
            if (r14 == 0) goto L_0x00bc
            android.content.Intent r2 = new android.content.Intent     // Catch:{ CanceledException -> 0x00b5 }
            r2.<init>()     // Catch:{ CanceledException -> 0x00b5 }
            java.lang.String r3 = "code"
            r2.putExtra(r3, r1)     // Catch:{ CanceledException -> 0x00b5 }
            java.lang.String r1 = "vid"
            r2.putExtra(r1, r10)     // Catch:{ CanceledException -> 0x00b5 }
            java.lang.String r1 = "pid"
            r2.putExtra(r1, r11)     // Catch:{ CanceledException -> 0x00b5 }
            java.lang.String r1 = "uid"
            r2.putExtra(r1, r12)     // Catch:{ CanceledException -> 0x00b5 }
            java.lang.String r1 = "package"
            r2.putExtra(r1, r13)     // Catch:{ CanceledException -> 0x00b5 }
            java.lang.String r1 = "property"
            r2.putExtra(r1, r15)     // Catch:{ CanceledException -> 0x00b5 }
            java.lang.String r1 = "value"
            r2.putExtra(r1, r8)     // Catch:{ CanceledException -> 0x00b5 }
            java.lang.String r1 = "seq"
            r2.putExtra(r1, r9)     // Catch:{ CanceledException -> 0x00b5 }
            r1 = 0
            r3 = r19
            r14.send(r3, r1, r2)     // Catch:{ CanceledException -> 0x00b5 }
            goto L_0x00bc
        L_0x00b5:
            java.lang.String r1 = "ModOEMSubsysSvc"
            java.lang.String r2 = "The result receiver is gone"
            android.util.Log.e(r1, r2)
        L_0x00bc:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.mod.ModOEMSubsystemService.m79c(android.content.Intent):void");
    }
}
