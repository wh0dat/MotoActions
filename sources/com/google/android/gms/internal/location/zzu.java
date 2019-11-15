package com.google.android.gms.internal.location;

import android.location.Location;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;

final class zzu extends zzab {
    private final /* synthetic */ Location zzco;

    zzu(zzq zzq, GoogleApiClient googleApiClient, Location location) {
        this.zzco = location;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        ((zzaz) anyClient).zza(this.zzco);
        setResult(Status.RESULT_SUCCESS);
    }
}
