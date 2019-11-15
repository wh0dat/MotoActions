package com.motorola.mod;

import android.os.IBinder;
import android.os.RemoteException;
import com.motorola.mod.IModBacklight.Stub;

/* renamed from: com.motorola.mod.a */
class C0663a extends ModBacklight {

    /* renamed from: a */
    private IModBacklight f203a;

    C0663a(IBinder iBinder) {
        this.f203a = Stub.asInterface(iBinder);
    }

    public boolean setModBacklightMode(int i) {
        try {
            return this.f203a.setModBacklightMode(i);
        } catch (RemoteException unused) {
            return false;
        }
    }

    public int getModBacklightMode() {
        try {
            return this.f203a.getModBacklightMode();
        } catch (RemoteException unused) {
            return -1;
        }
    }

    public boolean setModBacklightBrightness(byte b) {
        try {
            return this.f203a.setModBacklightBrightness(b);
        } catch (RemoteException unused) {
            return false;
        }
    }

    public byte getModBacklightBrightness() {
        try {
            return this.f203a.getModBacklightBrightness();
        } catch (RemoteException unused) {
            return 0;
        }
    }
}
