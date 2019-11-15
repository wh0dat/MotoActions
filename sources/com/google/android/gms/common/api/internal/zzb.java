package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import android.os.TransactionTooLargeException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.GoogleApiManager.zza;
import com.google.android.gms.common.util.PlatformVersion;

public abstract class zzb {
    private final int type;

    public zzb(int i) {
        this.type = i;
    }

    /* access modifiers changed from: private */
    public static Status zza(RemoteException remoteException) {
        StringBuilder sb = new StringBuilder();
        if (PlatformVersion.isAtLeastIceCreamSandwichMR1() && (remoteException instanceof TransactionTooLargeException)) {
            sb.append("TransactionTooLargeException: ");
        }
        sb.append(remoteException.getLocalizedMessage());
        return new Status(8, sb.toString());
    }

    public abstract void zza(@NonNull Status status);

    public abstract void zza(zza<?> zza) throws DeadObjectException;

    public abstract void zza(@NonNull zzaa zzaa, boolean z);

    public abstract void zza(@NonNull RuntimeException runtimeException);
}
