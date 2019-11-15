package com.google.android.gms.internal.location;

import android.app.PendingIntent;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ResultHolder;
import com.google.android.gms.location.GeofencingRequest;

final class zzag extends zzai {
    private final /* synthetic */ PendingIntent zzbz;
    private final /* synthetic */ GeofencingRequest zzcs;

    zzag(zzaf zzaf, GoogleApiClient googleApiClient, GeofencingRequest geofencingRequest, PendingIntent pendingIntent) {
        this.zzcs = geofencingRequest;
        this.zzbz = pendingIntent;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        ((zzaz) anyClient).zza(this.zzcs, this.zzbz, (ResultHolder<Status>) this);
    }
}
