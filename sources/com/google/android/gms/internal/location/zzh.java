package com.google.android.gms.internal.location;

import android.app.PendingIntent;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ResultHolder;
import com.google.android.gms.location.ActivityTransitionRequest;

final class zzh extends zzj {
    private final /* synthetic */ ActivityTransitionRequest zzby;
    private final /* synthetic */ PendingIntent zzbz;

    zzh(zze zze, GoogleApiClient googleApiClient, ActivityTransitionRequest activityTransitionRequest, PendingIntent pendingIntent) {
        this.zzby = activityTransitionRequest;
        this.zzbz = pendingIntent;
        super(googleApiClient);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void doExecute(AnyClient anyClient) throws RemoteException {
        ((zzaz) anyClient).zza(this.zzby, this.zzbz, (ResultHolder<Status>) this);
    }
}
