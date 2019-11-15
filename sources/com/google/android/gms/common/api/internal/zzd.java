package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.api.internal.GoogleApiManager.zza;

public final class zzd<A extends ApiMethodImpl<? extends Result, AnyClient>> extends zzb {
    private final A zzdv;

    public zzd(int i, A a) {
        super(i);
        this.zzdv = a;
    }

    public final void zza(@NonNull Status status) {
        this.zzdv.setFailedResult(status);
    }

    public final void zza(zza<?> zza) throws DeadObjectException {
        try {
            this.zzdv.run(zza.zzae());
        } catch (RuntimeException e) {
            zza(e);
        }
    }

    public final void zza(@NonNull zzaa zzaa, boolean z) {
        zzaa.zza((BasePendingResult<? extends Result>) this.zzdv, z);
    }

    public final void zza(@NonNull RuntimeException runtimeException) {
        String simpleName = runtimeException.getClass().getSimpleName();
        String localizedMessage = runtimeException.getLocalizedMessage();
        StringBuilder sb = new StringBuilder(String.valueOf(simpleName).length() + 2 + String.valueOf(localizedMessage).length());
        sb.append(simpleName);
        sb.append(": ");
        sb.append(localizedMessage);
        this.zzdv.setFailedResult(new Status(10, sb.toString()));
    }
}
