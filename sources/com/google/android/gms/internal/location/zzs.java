package com.google.android.gms.internal.location;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.ListenerHolders;
import com.google.android.gms.location.LocationCallback;

final class zzs extends zzab {
    private final /* synthetic */ LocationCallback zzcm;

    zzs(zzq zzq, GoogleApiClient googleApiClient, LocationCallback locationCallback) {
        this.zzcm = locationCallback;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        ((zzaz) anyClient).zzb(ListenerHolders.createListenerKey(this.zzcm, LocationCallback.class.getSimpleName()), new zzac(this));
    }
}
