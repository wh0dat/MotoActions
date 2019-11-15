package com.google.android.gms.internal.location;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.ListenerHolders;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

final class zzr extends zzab {
    private final /* synthetic */ LocationRequest zzck;
    private final /* synthetic */ LocationListener zzcl;

    zzr(zzq zzq, GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener) {
        this.zzck = locationRequest;
        this.zzcl = locationListener;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        ((zzaz) anyClient).zza(this.zzck, ListenerHolders.createListenerHolder(this.zzcl, zzbm.zzc(), LocationListener.class.getSimpleName()), (zzaj) new zzac(this));
    }
}
