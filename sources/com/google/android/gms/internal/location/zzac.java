package com.google.android.gms.internal.location;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BaseImplementation.ResultHolder;

final class zzac extends zzak {
    private final ResultHolder<Status> zzcq;

    public zzac(ResultHolder<Status> resultHolder) {
        this.zzcq = resultHolder;
    }

    public final void zza(zzad zzad) {
        this.zzcq.setResult(zzad.getStatus());
    }
}
