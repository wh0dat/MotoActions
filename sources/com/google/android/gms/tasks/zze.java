package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;

final class zze<TResult, TContinuationResult> implements OnCanceledListener, OnFailureListener, OnSuccessListener<TContinuationResult>, zzq<TResult> {
    private final Executor zzafk;
    /* access modifiers changed from: private */
    public final Continuation<TResult, Task<TContinuationResult>> zzafl;
    /* access modifiers changed from: private */
    public final zzu<TContinuationResult> zzafm;

    public zze(@NonNull Executor executor, @NonNull Continuation<TResult, Task<TContinuationResult>> continuation, @NonNull zzu<TContinuationResult> zzu) {
        this.zzafk = executor;
        this.zzafl = continuation;
        this.zzafm = zzu;
    }

    public final void cancel() {
        throw new UnsupportedOperationException();
    }

    public final void onCanceled() {
        this.zzafm.zzdp();
    }

    public final void onComplete(@NonNull Task<TResult> task) {
        this.zzafk.execute(new zzf(this, task));
    }

    public final void onFailure(@NonNull Exception exc) {
        this.zzafm.setException(exc);
    }

    public final void onSuccess(TContinuationResult tcontinuationresult) {
        this.zzafm.setResult(tcontinuationresult);
    }
}
