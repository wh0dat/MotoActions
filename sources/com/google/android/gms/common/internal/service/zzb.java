package com.google.android.gms.common.internal.service;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;

final class zzb extends zzd {
    zzb(CommonApiImpl commonApiImpl, GoogleApiClient googleApiClient) {
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        ((ICommonService) ((CommonClient) anyClient).getService()).clearDefaultAccount(new zza(this));
    }
}
