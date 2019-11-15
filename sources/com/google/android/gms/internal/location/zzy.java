package com.google.android.gms.internal.location;

import android.app.PendingIntent;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

final class zzy extends zzab {
    private final /* synthetic */ PendingIntent zzbx;
    private final /* synthetic */ LocationRequest zzck;

    zzy(zzq zzq, GoogleApiClient googleApiClient, LocationRequest locationRequest, PendingIntent pendingIntent) {
        this.zzck = locationRequest;
        this.zzbx = pendingIntent;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        ((zzaz) anyClient).zza(this.zzck, this.zzbx, (zzaj) new zzac(this));
    }
}
