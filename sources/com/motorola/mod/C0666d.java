package com.motorola.mod;

import android.os.IBinder;
import android.os.RemoteException;
import com.motorola.mod.IModDisplay.Stub;

/* renamed from: com.motorola.mod.d */
class C0666d extends ModDisplay {

    /* renamed from: a */
    private IModDisplay f206a;

    C0666d(IBinder iBinder) {
        this.f206a = Stub.asInterface(iBinder);
    }

    public boolean setModDisplayState(int i) {
        try {
            return this.f206a.setModDisplayState(i);
        } catch (RemoteException unused) {
            return false;
        }
    }

    public int getModDisplayState() {
        try {
            return this.f206a.getModDisplayState();
        } catch (RemoteException unused) {
            return -1;
        }
    }

    public boolean setModDisplayFollowState(int i) {
        try {
            return this.f206a.setModDisplayFollowState(i);
        } catch (RemoteException unused) {
            return false;
        }
    }

    public int getModDisplayFollowState() {
        try {
            return this.f206a.getModDisplayFollowState();
        } catch (RemoteException unused) {
            return -1;
        }
    }

    public boolean setPrivacyMode(int i) {
        try {
            int modPlatformSDKVersion = ModManager.getModPlatformSDKVersion();
            if (modPlatformSDKVersion >= 101) {
                return this.f206a.setPrivacyMode(i);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("setPrivacyMode() is not support for this Mod Platform ");
            sb.append(modPlatformSDKVersion);
            throw new UnsupportedOperationException(sb.toString());
        } catch (RemoteException unused) {
            return false;
        }
    }

    public int getPrivacyMode() {
        try {
            int modPlatformSDKVersion = ModManager.getModPlatformSDKVersion();
            if (modPlatformSDKVersion >= 101) {
                return this.f206a.getPrivacyMode();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("getPrivacyMode() is not support for this Mod Platform ");
            sb.append(modPlatformSDKVersion);
            throw new UnsupportedOperationException(sb.toString());
        } catch (RemoteException unused) {
            return -1;
        }
    }
}
