package com.google.android.gms.internal.location;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ResultHolder;
import com.google.android.gms.location.zzal;

final class zzah extends zzai {
    private final /* synthetic */ zzal zzct;

    zzah(zzaf zzaf, GoogleApiClient googleApiClient, zzal zzal) {
        this.zzct = zzal;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        ((zzaz) anyClient).zza(this.zzct, (ResultHolder<Status>) this);
    }
}
