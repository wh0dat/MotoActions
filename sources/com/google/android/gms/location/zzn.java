package com.google.android.gms.location;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.RegisterListenerMethod;
import com.google.android.gms.internal.location.zzaj;
import com.google.android.gms.internal.location.zzaz;
import com.google.android.gms.internal.location.zzbd;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zzn extends RegisterListenerMethod<zzaz, LocationCallback> {
    private final /* synthetic */ zzbd zzy;
    private final /* synthetic */ ListenerHolder zzz;

    zzn(FusedLocationProviderClient fusedLocationProviderClient, ListenerHolder listenerHolder, zzbd zzbd, ListenerHolder listenerHolder2) {
        this.zzy = zzbd;
        this.zzz = listenerHolder2;
        super(listenerHolder);
    }

    /* access modifiers changed from: protected */
    public final /* synthetic */ void registerListener(AnyClient anyClient, TaskCompletionSource taskCompletionSource) throws RemoteException {
        ((zzaz) anyClient).zza(this.zzy, this.zzz, (zzaj) new zza(taskCompletionSource));
    }
}
