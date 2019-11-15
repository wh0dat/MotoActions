package com.google.android.gms.internal.location;

import android.app.PendingIntent;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;

final class zzaa extends zzab {
    private final /* synthetic */ PendingIntent zzbx;

    zzaa(zzq zzq, GoogleApiClient googleApiClient, PendingIntent pendingIntent) {
        this.zzbx = pendingIntent;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        ((zzaz) anyClient).zza(this.zzbx, (zzaj) new zzac(this));
    }
}
