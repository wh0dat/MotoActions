package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.TaskCompletionSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public final class zzaa {
    /* access modifiers changed from: private */
    public final Map<BasePendingResult<?>, Boolean> zzgw = Collections.synchronizedMap(new WeakHashMap());
    /* access modifiers changed from: private */
    public final Map<TaskCompletionSource<?>, Boolean> zzgx = Collections.synchronizedMap(new WeakHashMap());

    private final void zza(boolean z, Status status) {
        HashMap hashMap;
        HashMap hashMap2;
        synchronized (this.zzgw) {
            hashMap = new HashMap(this.zzgw);
        }
        synchronized (this.zzgx) {
            hashMap2 = new HashMap(this.zzgx);
        }
        for (Entry entry : hashMap.entrySet()) {
            if (z || ((Boolean) entry.getValue()).booleanValue()) {
                ((BasePendingResult) entry.getKey()).zzb(status);
            }
        }
        for (Entry entry2 : hashMap2.entrySet()) {
            if (z || ((Boolean) entry2.getValue()).booleanValue()) {
                ((TaskCompletionSource) entry2.getKey()).trySetException(new ApiException(status));
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public final void zza(BasePendingResult<? extends Result> basePendingResult, boolean z) {
        this.zzgw.put(basePendingResult, Boolean.valueOf(z));
        basePendingResult.addStatusListener(new zzab(this, basePendingResult));
    }

    /* access modifiers changed from: 0000 */
    public final <TResult> void zza(TaskCompletionSource<TResult> taskCompletionSource, boolean z) {
        this.zzgx.put(taskCompletionSource, Boolean.valueOf(z));
        taskCompletionSource.getTask().addOnCompleteListener(new zzac(this, taskCompletionSource));
    }

    /* access modifiers changed from: 0000 */
    public final boolean zzaj() {
        return !this.zzgw.isEmpty() || !this.zzgx.isEmpty();
    }

    public final void zzak() {
        zza(false, GoogleApiManager.zzjj);
    }

    public final void zzal() {
        zza(true, zzck.zzmm);
    }
}
