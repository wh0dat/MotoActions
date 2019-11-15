package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;

public final class zzbo<O extends ApiOptions> extends zzaf {
    private final GoogleApi<O> zzks;

    public zzbo(GoogleApi<O> googleApi) {
        super("Method is not supported by connectionless client. APIs supporting connectionless client must not call this method.");
        this.zzks = googleApi;
    }

    public final <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(@NonNull T t) {
        return this.zzks.doRead(t);
    }

    public final <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(@NonNull T t) {
        return this.zzks.doWrite(t);
    }

    public final Context getContext() {
        return this.zzks.getApplicationContext();
    }

    public final Looper getLooper() {
        return this.zzks.getLooper();
    }

    public final void zza(zzch zzch) {
    }

    public final void zzb(zzch zzch) {
    }
}
