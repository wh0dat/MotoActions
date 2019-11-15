package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.internal.TaskApiCall.Builder;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zzcf extends TaskApiCall<A, ResultT> {
    private final /* synthetic */ Builder zzmc;

    zzcf(Builder builder, Feature[] featureArr, boolean z) {
        this.zzmc = builder;
        super(featureArr, z);
    }

    /* access modifiers changed from: protected */
    public final void doExecute(A a, TaskCompletionSource<ResultT> taskCompletionSource) throws RemoteException {
        this.zzmc.zzmb.accept(a, taskCompletionSource);
    }
}
