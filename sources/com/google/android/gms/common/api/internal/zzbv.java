package com.google.android.gms.common.api.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api.AnyClient;

public final class zzbv {
    public final RegisterListenerMethod<AnyClient, ?> zzlt;
    public final UnregisterListenerMethod<AnyClient, ?> zzlu;

    public zzbv(@NonNull RegisterListenerMethod<AnyClient, ?> registerListenerMethod, @NonNull UnregisterListenerMethod<AnyClient, ?> unregisterListenerMethod) {
        this.zzlt = registerListenerMethod;
        this.zzlu = unregisterListenerMethod;
    }
}
