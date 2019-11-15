package com.google.android.gms.internal.location;

import android.os.Looper;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.ListenerHolders;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

final class zzx extends zzab {
    private final /* synthetic */ LocationRequest zzck;
    private final /* synthetic */ LocationCallback zzcm;
    private final /* synthetic */ Looper zzcp;

    zzx(zzq zzq, GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationCallback locationCallback, Looper looper) {
        this.zzck = locationRequest;
        this.zzcm = locationCallback;
        this.zzcp = looper;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        ((zzaz) anyClient).zza(zzbd.zza(this.zzck), ListenerHolders.createListenerHolder(this.zzcm, zzbm.zza(this.zzcp), LocationCallback.class.getSimpleName()), (zzaj) new zzac(this));
    }
}
