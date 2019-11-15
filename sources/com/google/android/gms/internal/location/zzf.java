package com.google.android.gms.internal.location;

import android.app.PendingIntent;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;

final class zzf extends zzj {
    private final /* synthetic */ long zzbw;
    private final /* synthetic */ PendingIntent zzbx;

    zzf(zze zze, GoogleApiClient googleApiClient, long j, PendingIntent pendingIntent) {
        this.zzbw = j;
        this.zzbx = pendingIntent;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        ((zzaz) anyClient).zza(this.zzbw, this.zzbx);
        setResult(Status.RESULT_SUCCESS);
    }
}
