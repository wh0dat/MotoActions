package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.GoogleApiManager.zza;
import com.google.android.gms.tasks.TaskCompletionSource;

public final class zze extends zzc<Void> {
    private final RegisterListenerMethod<AnyClient, ?> zzdw;
    private final UnregisterListenerMethod<AnyClient, ?> zzdx;

    public zze(zzbv zzbv, TaskCompletionSource<Void> taskCompletionSource) {
        super(3, taskCompletionSource);
        this.zzdw = zzbv.zzlt;
        this.zzdx = zzbv.zzlu;
    }

    public final /* bridge */ /* synthetic */ void zza(@NonNull Status status) {
        super.zza(status);
    }

    public final /* bridge */ /* synthetic */ void zza(@NonNull zzaa zzaa, boolean z) {
    }

    public final /* bridge */ /* synthetic */ void zza(@NonNull RuntimeException runtimeException) {
        super.zza(runtimeException);
    }

    public final void zzb(zza<?> zza) throws RemoteException {
        this.zzdw.registerListener(zza.zzae(), this.zzdu);
        if (this.zzdw.getListenerKey() != null) {
            zza.zzbn().put(this.zzdw.getListenerKey(), new zzbv(this.zzdw, this.zzdx));
        }
    }
}
