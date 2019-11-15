package com.motorola.mod;

import android.os.IBinder;
import android.os.RemoteException;
import com.motorola.mod.IModCamera.Stub;

/* renamed from: com.motorola.mod.c */
class C0665c extends ModCamera {

    /* renamed from: a */
    private IModCamera f205a;

    C0665c(IBinder iBinder) {
        this.f205a = Stub.asInterface(iBinder);
    }

    public String getProperty(String str) {
        try {
            return this.f205a.getProperty(str);
        } catch (RemoteException unused) {
            return null;
        }
    }
}
