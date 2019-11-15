package com.motorola.mod;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import com.motorola.mod.IModListener.Stub;
import com.motorola.mod.ModDevice.Interface;
import com.motorola.mod.ModProtocol.Protocol;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

public class ModManager {
    public static final String ACTION_BIND_MANAGER = "com.motorola.mod.action.BIND_MANAGER";
    public static final String ACTION_MOD_ATTACH = "com.motorola.mod.action.MOD_ATTACH";
    public static final String ACTION_MOD_ATTACH_FAILED = "com.motorola.mod.action.MOD_ATTACH_FAILED";
    public static final String ACTION_MOD_CAPABILITY_CHANGED = "com.motorola.mod.action.MOD_CAPABILITY_CHANGED";
    public static final String ACTION_MOD_DETACH = "com.motorola.mod.action.MOD_DETACH";
    public static final String ACTION_MOD_ENUMERATION_DONE = "com.motorola.mod.action.MOD_ENUMERATION_DONE";
    public static final String ACTION_MOD_ERROR = "com.motorola.mod.action.MOD_ERROR";
    public static final String ACTION_MOD_EVENT = "com.motorola.mod.action.MOD_EVENT";
    public static final String ACTION_MOD_FIRMWARE_CHECK_UPDATE_ERROR = "com.motorola.mod.action.MOD_FIRMWARE_CHECK_UPDATE_ERROR";
    public static final String ACTION_MOD_FIRMWARE_DOWNLOAD_STATUS = "com.motorola.mod.action.MOD_FIRMWARE_DOWNLOAD_STATUS";
    public static final String ACTION_MOD_FIRMWARE_UPDATE_AVAILABLE = "com.motorola.mod.action.MOD_FIRMWARE_UPDATE_AVAILABLE";
    public static final String ACTION_MOD_FIRMWARE_UPDATE_CANCELLED = "com.motorola.mod.action.MOD_FW_UPDATE_CANCELLED";
    public static final String ACTION_MOD_FIRMWARE_UPDATE_CANCEL_STATUS = "com.motorola.mod.action.MOD_FW_UPDATE_CANCEL_STATUS";
    public static final String ACTION_MOD_FIRMWARE_UPDATE_DONE = "com.motorola.mod.action.MOD_FIRMWARE_UPDATE_DONE";
    public static final String ACTION_MOD_FIRMWARE_UPDATE_START = "com.motorola.mod.action.MOD_FIRMWARE_UPDATE_START";
    public static final String ACTION_MOD_FIRMWARE_UPDATE_STATUS = "com.motorola.mod.action.FMW_UPDATE_STATUS";
    public static final String ACTION_MOD_PRE_ATTACH_USER_CONSENT = "com.motorola.mod.action.MOD_PRE_ATTACH_USER_CONSENT";
    public static final String ACTION_MOD_REQUEST_FIRMWARE = "com.motorola.mod.action.MOD_REQUEST_FIRMWARE";
    public static final String ACTION_MOD_SERVICE_STARTED = "com.motorola.mod.action.SERVICE_STARTED";
    public static final String ACTION_MOD_USB_CONFLICT_DETECTED = "com.motorola.mod.action.MOD_USB_CONFLICT_DETECTED";
    public static final String ACTION_OEM_SUBSYSTEM = "com.motorola.mod.action.OEM_SUBSYSTEM";
    public static final String ACTION_OEM_SUBSYSTEM_GET = "com.motorola.mod.action.OEM_SUBSYSTEM_GET ";
    public static final String ACTION_OEM_SUBSYSTEM_SET = "com.motorola.mod.action.OEM_SUBSYSTEM_SET ";
    public static final String ACTION_OEM_SUBSYSTEM_UPDATE = "com.motorola.mod.action.OEM_SUBSYSTEM_UPDATE";
    public static final String ACTION_REQUEST_CONSENT_FOR_UNSECURE_FIRMWARE_UPDATE = "com.motorola.mod.action.UNSEC_FMW_CONSENT_REQ";
    public static final String ACTION_USER_CONSENT_RESP_FOR_UNSECURE_FIRMWARE = "com.motorola.mod.action.UNSEC_FMW_CONSENT_RESP";
    public static final String CAT_SCHEME = "cat://";
    public static final String EC_SCHEME = "ec://";
    public static final String EXTRA_BUILT_IN_CONFIGS = "bic";
    public static final String EXTRA_CALLER = "caller";
    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_CERTIFIED = "certified";
    public static final String EXTRA_FD_LIST = "fdlist";
    public static final String EXTRA_FILE_SIZES = "fsizes";
    public static final String EXTRA_FIRMWARE_DOWNLOAD_PERCENT = "percent";
    public static final String EXTRA_FIRMWARE_DOWNLOAD_STATUS = "status";
    public static final String EXTRA_FIRMWARE_RELEASE_NOTES = "release_notes";
    public static final String EXTRA_FIRMWARE_SIZE = "size";
    public static final String EXTRA_FIRMWARE_VERSION = "version";
    public static final String EXTRA_GB_VERSION_MAJOR = "verma";
    public static final String EXTRA_GB_VERSION_MINOR = "vermi";
    public static final String EXTRA_I2CS = "i2cs";
    public static final String EXTRA_I2PS = "i2ps";
    public static final String EXTRA_IFACES = "ifaces";
    public static final String EXTRA_LEGAL_NOTICE = "li";
    public static final String EXTRA_MANIFEST_SIZE = "size";
    public static final String EXTRA_MINSDK = "minSDK";
    public static final String EXTRA_MOD_DEVICE = "mod";
    public static final String EXTRA_MOD_ERROR = "mod_error";
    public static final String EXTRA_MOD_EVENT = "mod_event";
    public static final String EXTRA_MOD_FIRMARE_IS_MANDATORY = "isMandatory";
    public static final String EXTRA_MOD_FIRMARE_URI = "firmware_uri";
    public static final String EXTRA_MOD_MANAGER = "modmanager";
    public static final String EXTRA_MOD_PROPERTY = "property";
    public static final String EXTRA_MOD_VALUE = "value";
    public static final String EXTRA_PACKAGE = "package";
    public static final String EXTRA_PRODUCT = "product";
    public static final String EXTRA_PRODUCT_ID = "pid";
    public static final String EXTRA_PROTOCOLS = "protocols";
    public static final String EXTRA_REASON = "reason";
    public static final String EXTRA_RESULT_CODE = "code";
    public static final String EXTRA_RESULT_RECEIVER = "receiver";
    public static final String EXTRA_SEQUENCE_NUMBER = "seq";
    public static final String EXTRA_SUBSYSTEM_ACTION = "subaction";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_UNIQUE_ID = "uid";
    public static final String EXTRA_UPDATE_STATUS_CURR = "fmw_update_status_curr";
    public static final String EXTRA_UPDATE_STATUS_PREV = "fmw_update_status_prev";
    public static final String EXTRA_USB = "usb";
    public static final String EXTRA_VENDOR = "vendor";
    public static final String EXTRA_VENDOR_CONFIGS = "vc";
    public static final String EXTRA_VENDOR_ID = "vid";
    public static final int FIRMWARE_UPDATE_CANCELLED_BY_OTA_SESSION_CONFLICT = 2;
    public static final int FIRMWARE_UPDATE_CANCELLED_BY_SERVER = 1;
    public static final int INSTALL_OTA_ERROR_LOW_BATTERY = 1;
    public static final int INSTALL_OTA_ERROR_LOW_BATTERY_IN_RECOVERY = 3;
    public static final int INSTALL_OTA_ERROR_NONE = 0;
    public static final int INSTALL_OTA_ERROR_OTHER = 2;
    public static final String LI_SCHEME = "li://";
    public static final int MDK_DEVELOPER_VID = 66;
    public static final int MDK_VID = 786;
    public static final int MOD_CURRENT_LIMIT_HIGH = 1;
    public static final int MOD_CURRENT_LIMIT_LOW = 0;
    public static final int MOD_ERROR_COMMUNICATION_RESET = 5;
    public static final int MOD_ERROR_RECOVERY_ATTEMPT = 2;
    public static final int MOD_ERROR_RECOVERY_FAILED = 3;
    public static final int MOD_ERROR_RECOVERY_SUCCESS = 4;
    public static final int MOD_ERROR_SHORT_DETECTED = 0;
    public static final int MOD_ERROR_SHORT_RECOVERY_FAIL = 1;
    public static final int MOD_EVENT_ATTACHED = 0;
    public static final int MOD_EVENT_DETACHED = 1;
    public static final int MOD_EVENT_USB_CONFLICT = 2;
    public static final ComponentName MOD_SERVICE_NAME = new ComponentName("com.motorola.modservice", "com.motorola.modservice.ModManagerService");
    public static final String PACKAGE_SCHEME = "pkg://";
    public static final String PERMISSION_MOD_ACCESS_INFO = "com.motorola.mod.permission.MOD_ACCESS_INFO";
    public static final String PERMISSION_MOD_INTERNAL = "com.motorola.mod.permission.MOD_INTERNAL";
    public static final String PERMISSION_USE_RAW_PROTOCOL = "com.motorola.mod.permission.RAW_PROTOCOL";
    public static final int PHONE_BATTERY_THRESHOLD_LEVL_FOR_INSTALL_MOD_FIRMWARE = 20;
    public static final String SCHEME_OEM_IMG = "mod_oem_image://";
    public static final int SERVICE_DISABLED = 4;
    public static final int SERVICE_INVALID = 5;
    public static final int SERVICE_MISSING = 1;
    public static final int SERVICE_UPDATING = 2;
    public static final int SERVICE_VERSION_UPDATE_REQUIRED = 3;
    public static final int SUCCESS = 0;
    public static final int USB_EXT = 1;
    public static final int USB_MAIN = 0;
    public static final int USB_TYPE_2_0 = 2;
    public static final int USB_TYPE_3_0 = 3;

    /* renamed from: e */
    private static ArrayMap<Class<?>, Protocol> f169e = new ArrayMap<>();

    /* renamed from: a */
    private Context f170a;

    /* renamed from: b */
    private IModManager f171b;
    /* access modifiers changed from: private */

    /* renamed from: c */
    public ArrayMap<ModListener, int[]> f172c;

    /* renamed from: d */
    private C0653a f173d;

    public enum FirmwareDownloadStatus {
        DOWNLOAD_STATUS_DONE,
        DOWNLOAD_STATUS_IN_PROGRESS,
        DOWNLOAD_STATUS_FAIL,
        DOWNLOAD_STATUS_FAIL_NO_WIFI,
        DOWNLOAD_STATUS_FAIL_NETWORK,
        DOWNLOAD_STATUS_FAIL_CANCELLED,
        DOWNLOAD_STATUS_FAIL_INSUFFICIENT_SPACE
    }

    /* renamed from: com.motorola.mod.ModManager$a */
    private class C0653a extends Stub {
        private C0653a() {
        }

        public void onDeviceHotplug(ModDevice modDevice, boolean z) {
            ArrayMap arrayMap;
            ArraySet declaredProtocols = ModManager.getDeclaredProtocols(modDevice);
            if (declaredProtocols != null) {
                synchronized (ModManager.this) {
                    arrayMap = new ArrayMap(ModManager.this.f172c);
                }
                for (Entry entry : arrayMap.entrySet()) {
                    if (ModManager.intersectProtocol(declaredProtocols, (int[]) entry.getValue())) {
                        ((ModListener) entry.getKey()).onDeviceHotplug(modDevice, z);
                    }
                }
            }
        }

        public void onInterfaceUpDown(ModDevice modDevice, Interface interfaceR, boolean z) {
            ArrayMap arrayMap;
            ArraySet declaredProtocols = ModManager.getDeclaredProtocols(modDevice);
            if (declaredProtocols != null) {
                synchronized (ModManager.this) {
                    arrayMap = new ArrayMap(ModManager.this.f172c);
                }
                for (Entry entry : arrayMap.entrySet()) {
                    if (ModManager.intersectProtocol(declaredProtocols, (int[]) entry.getValue())) {
                        ((ModListener) entry.getKey()).onInterfaceUpDown(modDevice, interfaceR, z);
                    }
                }
            }
        }

        public void onConnectionStatusChanged(ModDevice modDevice, ModConnection modConnection) {
            ArrayMap arrayMap;
            ArraySet declaredProtocols = ModManager.getDeclaredProtocols(modDevice);
            if (declaredProtocols != null) {
                synchronized (ModManager.this) {
                    arrayMap = new ArrayMap(ModManager.this.f172c);
                }
                for (Entry entry : arrayMap.entrySet()) {
                    if (ModManager.intersectProtocol(declaredProtocols, (int[]) entry.getValue())) {
                        ((ModListener) entry.getKey()).onConnectionStatusChanged(modDevice, modConnection);
                    }
                }
            }
        }

        public void onLinuxDeviceChanged(ModDevice modDevice, ModInterfaceDelegation modInterfaceDelegation, boolean z) {
            ArrayMap arrayMap;
            ArraySet declaredProtocols = ModManager.getDeclaredProtocols(modDevice);
            if (declaredProtocols != null) {
                synchronized (ModManager.this) {
                    arrayMap = new ArrayMap(ModManager.this.f172c);
                }
                for (Entry entry : arrayMap.entrySet()) {
                    if (ModManager.intersectProtocol(declaredProtocols, (int[]) entry.getValue())) {
                        ((ModListener) entry.getKey()).onLinuxDeviceChanged(modDevice, modInterfaceDelegation, z);
                    }
                }
            }
        }

        public void onEnumerationDoneD(ModDevice modDevice) {
            ArrayMap arrayMap;
            ArraySet declaredProtocols = ModManager.getDeclaredProtocols(modDevice);
            if (declaredProtocols != null) {
                synchronized (ModManager.this) {
                    arrayMap = new ArrayMap(ModManager.this.f172c);
                }
                for (Entry entry : arrayMap.entrySet()) {
                    if (ModManager.intersectProtocol(declaredProtocols, (int[]) entry.getValue())) {
                        ((ModListener) entry.getKey()).onEnumerationDone(modDevice);
                    }
                }
            }
        }

        public void onEnumerationDoneI(ModDevice modDevice, Interface interfaceR, boolean z) {
            ArrayMap arrayMap;
            ArraySet declaredProtocols = ModManager.getDeclaredProtocols(modDevice);
            if (declaredProtocols != null) {
                synchronized (ModManager.this) {
                    arrayMap = new ArrayMap(ModManager.this.f172c);
                }
                for (Entry entry : arrayMap.entrySet()) {
                    if (ModManager.intersectProtocol(declaredProtocols, (int[]) entry.getValue())) {
                        ((ModListener) entry.getKey()).onEnumerationDone(modDevice, interfaceR, z);
                    }
                }
            }
        }

        public void onCapabilityChanged(ModDevice modDevice) {
            ArrayMap arrayMap;
            ArraySet declaredProtocols = ModManager.getDeclaredProtocols(modDevice);
            if (declaredProtocols != null) {
                synchronized (ModManager.this) {
                    arrayMap = new ArrayMap(ModManager.this.f172c);
                }
                for (Entry entry : arrayMap.entrySet()) {
                    if (ModManager.intersectProtocol(declaredProtocols, (int[]) entry.getValue())) {
                        ((ModListener) entry.getKey()).onCapabilityChanged(modDevice);
                    }
                }
            }
        }
    }

    static {
        f169e.put(ModBattery.class, Protocol.BATTERY);
        f169e.put(ModDisplay.class, Protocol.MODS_DISPLAY);
        f169e.put(ModBacklight.class, Protocol.LIGHTS);
        f169e.put(ModCamera.class, Protocol.CAMERA_EXT);
    }

    public ModManager(Context context, IModManager iModManager) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
            if (applicationInfo.targetSdkVersion < 23) {
                StringBuilder sb = new StringBuilder();
                sb.append("Target SDK version is too low: Expected: 23, got ");
                sb.append(applicationInfo.targetSdkVersion);
                throw new IllegalStateException(sb.toString());
            }
        } catch (NameNotFoundException unused) {
            Log.e("ModManager", "This should never happen");
        }
        this.f170a = context;
        this.f171b = iModManager;
        this.f172c = new ArrayMap<>();
        this.f173d = new C0653a();
    }

    public boolean registerModListener(ModListener modListener, int[] iArr) {
        int[] iArr2;
        synchronized (this) {
            if (iArr != null) {
                try {
                    int size = this.f172c.size();
                    if (size > 0) {
                        ArraySet arraySet = new ArraySet();
                        for (int i = 0; i < size; i++) {
                            for (int valueOf : (int[]) this.f172c.valueAt(i)) {
                                arraySet.add(Integer.valueOf(valueOf));
                            }
                        }
                        for (int valueOf2 : iArr) {
                            arraySet.add(Integer.valueOf(valueOf2));
                        }
                        Integer[] numArr = (Integer[]) arraySet.toArray(new Integer[arraySet.size()]);
                        iArr2 = new int[arraySet.size()];
                        int length = numArr.length;
                        int i2 = 0;
                        int i3 = 0;
                        while (i2 < length) {
                            int i4 = i3 + 1;
                            iArr2[i3] = numArr[i2].intValue();
                            i2++;
                            i3 = i4;
                        }
                    } else {
                        iArr2 = iArr;
                    }
                } finally {
                    while (true) {
                    }
                }
            } else {
                iArr2 = null;
                int size2 = this.f172c.size();
                if (size2 > 0) {
                    ArraySet arraySet2 = new ArraySet();
                    for (int i5 = 0; i5 < size2; i5++) {
                        for (int valueOf3 : (int[]) this.f172c.valueAt(i5)) {
                            arraySet2.add(Integer.valueOf(valueOf3));
                        }
                    }
                    Integer[] numArr2 = (Integer[]) arraySet2.toArray(new Integer[arraySet2.size()]);
                    iArr2 = new int[arraySet2.size()];
                    int length2 = numArr2.length;
                    int i6 = 0;
                    int i7 = 0;
                    while (i6 < length2) {
                        int i8 = i7 + 1;
                        iArr2[i7] = numArr2[i6].intValue();
                        i6++;
                        i7 = i8;
                    }
                }
            }
        }
        try {
            this.f171b.registerModListener(this.f173d, iArr2);
            synchronized (this) {
                if (iArr != null) {
                    try {
                        this.f172c.put(modListener, iArr);
                    } catch (Throwable th) {
                        throw th;
                    }
                } else {
                    this.f172c.remove(modListener);
                }
            }
            return true;
        } catch (RemoteException e) {
            Log.e("ModManager", "Unable to talk to Mod service", e);
            return false;
        }
    }

    public List<ModDevice> getModList(boolean z) throws RemoteException {
        return getModList(new int[]{256}, z);
    }

    public List<ModDevice> getModList(int[] iArr, boolean z) throws RemoteException {
        return this.f171b.getModList(iArr, z);
    }

    public IModAuthenticator getAuthenticator() throws RemoteException {
        return this.f171b.getAuthenticator();
    }

    public String getDefaultModPackage(ModDevice modDevice) {
        return modDevice.getPackage();
    }

    public List<ModConnection> getConnectionsByDevice(ModDevice modDevice) throws RemoteException {
        return this.f171b.getConnectionsByDevice(modDevice);
    }

    public int requestUpdateFirmware(ModDevice modDevice, List<Uri> list) throws RemoteException {
        return this.f171b.requestUpdateFirmware(modDevice, list);
    }

    public List<ModInterfaceDelegation> getModInterfaceDelegationsByProtocol(ModDevice modDevice, Protocol protocol) throws RemoteException {
        return this.f171b.getModInterfaceDelegationsByProtocol(modDevice, protocol.getValue());
    }

    public ParcelFileDescriptor openModInterface(ModInterfaceDelegation modInterfaceDelegation, int i) throws RemoteException {
        return this.f171b.openModInterface(modInterfaceDelegation, i);
    }

    public void checkForModFirmwareUpdate(int i, int i2, UUID uuid, boolean z) throws RemoteException {
        this.f171b.checkForModFirmwareUpdates(i, i2, uuid.toString(), z);
    }

    public void downloadModFirmware(int i, int i2, UUID uuid, boolean z) throws RemoteException {
        this.f171b.downloadModFirmware(i, i2, uuid.toString(), z);
    }

    public void changeDownloadPreference(int i, int i2, UUID uuid, boolean z) throws RemoteException {
        this.f171b.changeDownloadPreference(i, i2, uuid.toString(), z);
    }

    public void cancelModFirmwareUpdate(int i, int i2, UUID uuid) throws RemoteException {
        this.f171b.cancelModFirmwareUpdate(i, i2, uuid.toString());
    }

    public int getModFirmwareDownloadPercent(int i, int i2, UUID uuid) throws RemoteException {
        return this.f171b.getDownloadPercent(i, i2, uuid.toString());
    }

    public int installOtaPackage(ModDevice modDevice, Uri uri, boolean z) throws RemoteException {
        return this.f171b.installOtaPackage(modDevice, uri, z);
    }

    /* renamed from: a */
    private static String m72a(String str) {
        String str2 = "";
        try {
            Class cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getDeclaredMethod("get", new Class[]{String.class}).invoke(cls, new Object[]{str});
        } catch (Exception unused) {
            return str2;
        }
    }

    public static ArraySet<Integer> getDeclaredProtocols(ModDevice modDevice) {
        if (modDevice == null) {
            return null;
        }
        ArrayList<Protocol> declaredProtocols = modDevice.getDeclaredProtocols();
        if (declaredProtocols == null) {
            return null;
        }
        ArraySet<Integer> arraySet = new ArraySet<>();
        for (Protocol value : declaredProtocols) {
            arraySet.add(Integer.valueOf(value.getValue()));
        }
        return arraySet;
    }

    public static boolean intersectProtocol(ArraySet<Integer> arraySet, int[] iArr) {
        if (iArr == null) {
            return false;
        }
        for (int i : iArr) {
            if (i == 256 || arraySet.contains(Integer.valueOf(i))) {
                return true;
            }
        }
        return false;
    }

    public final Object getClassManager(ModDevice modDevice, Protocol protocol) {
        if (modDevice == null || protocol == null) {
            return null;
        }
        ArrayList declaredProtocols = modDevice.getDeclaredProtocols();
        if (declaredProtocols == null || !declaredProtocols.contains(protocol)) {
            return null;
        }
        try {
            IBinder classManager = this.f171b.getClassManager(modDevice, protocol.getValue());
            if (classManager == null) {
                return null;
            }
            switch (protocol) {
                case BATTERY:
                    return new C0664b(classManager);
                case LIGHTS:
                    return new C0663a(classManager);
                case MODS_DISPLAY:
                    return new C0666d(classManager);
                case CAMERA_EXT:
                    return new C0665c(classManager);
                default:
                    return null;
            }
        } catch (RemoteException unused) {
            return null;
        }
    }

    public final <T> T getClassManager(ModDevice modDevice, Class<T> cls) {
        Protocol protocol = (Protocol) f169e.get(cls);
        if (protocol != null) {
            return getClassManager(modDevice, protocol);
        }
        return null;
    }

    public final Object getClassManager(Protocol protocol) {
        try {
            List modList = getModList(true);
            if (modList == null || modList.size() == 0) {
                return null;
            }
            return getClassManager((ModDevice) modList.get(0), protocol);
        } catch (RemoteException unused) {
            return null;
        }
    }

    public final <T> T getClassManager(Class<T> cls) {
        try {
            List modList = getModList(true);
            if (modList == null || modList.size() == 0) {
                return null;
            }
            return getClassManager((ModDevice) modList.get(0), cls);
        } catch (RemoteException unused) {
            return null;
        }
    }

    public static int isModServicesAvailable(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Resources resources = context.getResources();
        String packageName = context.getPackageName();
        try {
            int integer = resources.getInteger(resources.getIdentifier("moto_mod_services_version", "integer", packageName));
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 128);
                if (applicationInfo != null) {
                    Bundle bundle = applicationInfo.metaData;
                    if (bundle != null) {
                        int intValue = Integer.valueOf(bundle.getInt("com.motorola.mod.version")).intValue();
                        int i = intValue - (intValue % 1000);
                        if (integer != i) {
                            Log.e("ModManager", "Please do not hardcode your own version in the metadata");
                            return 5;
                        }
                        try {
                            PackageInfo packageInfo = packageManager.getPackageInfo("com.motorola.modservice", 0);
                            if (packageInfo == null) {
                                Log.e("ModManager", "Cannot find Moto Mod service");
                                return 1;
                            } else if (packageInfo.versionCode < i) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Moto Mod service out of date. Requires 1000 but found ");
                                sb.append(packageInfo.versionCode);
                                Log.e("ModManager", sb.toString());
                                return 3;
                            } else {
                                try {
                                    ApplicationInfo applicationInfo2 = packageManager.getApplicationInfo("com.motorola.modservice", 0);
                                    if (applicationInfo2 == null) {
                                        Log.e("ModManager", "Cannot find Moto Mod service");
                                        return 1;
                                    } else if (!applicationInfo2.enabled) {
                                        return 4;
                                    } else {
                                        return 0;
                                    }
                                } catch (NameNotFoundException unused) {
                                    Log.e("ModManager", "Cannot find Moto Mod service");
                                    return 1;
                                }
                            }
                        } catch (NameNotFoundException unused2) {
                            Log.e("ModManager", "Moto Mod service is missing.");
                            return 1;
                        }
                    }
                }
                Log.e("ModManager", "A required meta-data tag in your app's AndroidManifest.xml does not exist. You must have the following declaration in your <application> element: <meta-data android:name=\"com.motorola.mod.version\" android:value=\"@integer/moto_mod_services_version\" />");
                return 5;
            } catch (NameNotFoundException unused3) {
                Log.e("ModManager", "This should never happen");
                return 5;
            } catch (NumberFormatException unused4) {
                Log.e("ModManager", "Invalid version code in project configuration.");
                return 5;
            }
        } catch (NotFoundException unused5) {
            Log.e("ModManager", "The Moto Mod service resources were not found, check your project configuration to ensure that the resources are included.");
            return 5;
        }
    }

    public static int getModSdkVersion(Context context) {
        int i = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.motorola.modservice", 0);
            if (!(packageInfo == null || packageInfo.versionCode == 0)) {
                i = packageInfo.versionCode / 1000;
            }
            return i;
        } catch (NameNotFoundException unused) {
            Log.e("ModManager", "Moto Mod service is missing.");
            return 0;
        }
    }

    public static int getModPlatformSDKVersion() {
        String a = m72a("sys.mod.platformsdkversion");
        if (a != null) {
            try {
                return Integer.valueOf(a).intValue();
            } catch (NumberFormatException unused) {
                Log.e("ModManager", "Cannot get the Platfrm MOD version");
            }
        }
        return 0;
    }

    public boolean initSetupComplete(ModDevice modDevice) throws RemoteException {
        if (modDevice == null) {
            return false;
        }
        return this.f171b.initSetupComplete(modDevice);
    }

    public boolean setCurrentLimit(ModDevice modDevice, int i) throws RemoteException {
        if (modDevice == null || (i != 1 && i != 0)) {
            return false;
        }
        return this.f171b.setCurrentLimit(modDevice, i);
    }

    public boolean setUsbPriority(int i) throws RemoteException {
        if (i == 0 || i == 1) {
            return this.f171b.setUsbPriority(i);
        }
        return false;
    }

    public void reset(ModDevice modDevice) throws RemoteException {
        this.f171b.reset(modDevice);
    }

    public int[] getSupportedProtocols() {
        try {
            return this.f171b.getSupportedProtocols();
        } catch (RemoteException unused) {
            Log.e("ModManager", "Please upgrade ModService and Platform");
            return null;
        }
    }

    public boolean isProtocolSupported(Protocol protocol) {
        try {
            return this.f171b.isProtocolSupported(protocol.getValue());
        } catch (RemoteException unused) {
            Log.e("ModManager", "This API is not supported, please upgrade ModService and Platform");
            return false;
        }
    }
}
