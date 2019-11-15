package com.google.android.gms.common.api.internal;

import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.internal.BaseGmsClient.ConnectionProgressReportCallbacks;
import com.google.android.gms.common.internal.Preconditions;
import java.lang.ref.WeakReference;

final class zzal implements ConnectionProgressReportCallbacks {
    private final Api<?> mApi;
    /* access modifiers changed from: private */
    public final boolean zzfo;
    private final WeakReference<zzaj> zzhw;

    public zzal(zzaj zzaj, Api<?> api, boolean z) {
        this.zzhw = new WeakReference<>(zzaj);
        this.mApi = api;
        this.zzfo = z;
    }

    public final void onReportServiceBinding(@NonNull ConnectionResult connectionResult) {
        zzaj zzaj = (zzaj) this.zzhw.get();
        if (zzaj != null) {
            Preconditions.checkState(Looper.myLooper() == zzaj.zzhf.zzfq.getLooper(), "onReportServiceBinding must be called on the GoogleApiClient handler thread");
            zzaj.zzga.lock();
            try {
                if (zzaj.zze(0)) {
                    if (!connectionResult.isSuccess()) {
                        zzaj.zzb(connectionResult, this.mApi, this.zzfo);
                    }
                    if (zzaj.zzar()) {
                        zzaj.zzas();
                    }
                }
            } finally {
                zzaj.zzga.unlock();
            }
        }
    }
}
