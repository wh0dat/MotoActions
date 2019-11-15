package com.google.android.gms.common.api.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

final class zzac implements OnCompleteListener<TResult> {
    private final /* synthetic */ zzaa zzgz;
    private final /* synthetic */ TaskCompletionSource zzha;

    zzac(zzaa zzaa, TaskCompletionSource taskCompletionSource) {
        this.zzgz = zzaa;
        this.zzha = taskCompletionSource;
    }

    public final void onComplete(@NonNull Task<TResult> task) {
        this.zzgz.zzgx.remove(this.zzha);
    }
}
