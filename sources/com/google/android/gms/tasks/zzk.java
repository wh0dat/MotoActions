package com.google.android.gms.tasks;

import android.support.annotation.NonNull;
import java.util.concurrent.Executor;
import javax.annotation.concurrent.GuardedBy;

final class zzk<TResult> implements zzq<TResult> {
    /* access modifiers changed from: private */
    public final Object mLock = new Object();
    private final Executor zzafk;
    /* access modifiers changed from: private */
    @GuardedBy("mLock")
    public OnFailureListener zzafu;

    public zzk(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
        this.zzafk = executor;
        this.zzafu = onFailureListener;
    }

    public final void cancel() {
        synchronized (this.mLock) {
            this.zzafu = null;
        }
    }

    public final void onComplete(@NonNull Task<TResult> task) {
        if (!task.isSuccessful() && !task.isCanceled()) {
            synchronized (this.mLock) {
                if (this.zzafu != null) {
                    this.zzafk.execute(new zzl(this, task));
                }
            }
        }
    }
}
