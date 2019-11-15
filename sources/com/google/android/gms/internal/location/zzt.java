package com.google.android.gms.internal.location;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;

final class zzt extends zzab {
    private final /* synthetic */ boolean zzcn;

    zzt(zzq zzq, GoogleApiClient googleApiClient, boolean z) {
        this.zzcn = z;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        ((zzaz) anyClient).zza(this.zzcn);
        setResult(Status.RESULT_SUCCESS);
    }
}
