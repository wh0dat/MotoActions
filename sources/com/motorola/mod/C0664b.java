package com.motorola.mod;

import android.os.IBinder;
import android.os.RemoteException;
import com.motorola.mod.IModBattery.Stub;

/* renamed from: com.motorola.mod.b */
class C0664b extends ModBattery {

    /* renamed from: a */
    private IModBattery f204a;

    C0664b(IBinder iBinder) {
        this.f204a = Stub.asInterface(iBinder);
    }

    public int getIntProperty(int i) {
        try {
            return this.f204a.getIntProperty(i);
        } catch (RemoteException unused) {
            return Integer.MIN_VALUE;
        }
    }

    public void setIntProperty(int i, int i2) {
        try {
            this.f204a.setIntProperty(i, i2);
        } catch (RemoteException unused) {
        }
    }
}
