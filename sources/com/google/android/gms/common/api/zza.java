package com.google.android.gms.common.api;

import com.google.android.gms.common.api.PendingResult.StatusListener;

final class zza implements StatusListener {
    private final /* synthetic */ Batch zzch;

    zza(Batch batch) {
        this.zzch = batch;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0067, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onComplete(com.google.android.gms.common.api.Status r4) {
        /*
            r3 = this;
            com.google.android.gms.common.api.Batch r0 = r3.zzch
            java.lang.Object r0 = r0.mLock
            monitor-enter(r0)
            com.google.android.gms.common.api.Batch r1 = r3.zzch     // Catch:{ all -> 0x0068 }
            boolean r1 = r1.isCanceled()     // Catch:{ all -> 0x0068 }
            if (r1 == 0) goto L_0x0011
            monitor-exit(r0)     // Catch:{ all -> 0x0068 }
            return
        L_0x0011:
            boolean r1 = r4.isCanceled()     // Catch:{ all -> 0x0068 }
            r2 = 1
            if (r1 == 0) goto L_0x001e
            com.google.android.gms.common.api.Batch r4 = r3.zzch     // Catch:{ all -> 0x0068 }
            r4.zzcf = true     // Catch:{ all -> 0x0068 }
            goto L_0x0029
        L_0x001e:
            boolean r4 = r4.isSuccess()     // Catch:{ all -> 0x0068 }
            if (r4 != 0) goto L_0x0029
            com.google.android.gms.common.api.Batch r4 = r3.zzch     // Catch:{ all -> 0x0068 }
            r4.zzce = true     // Catch:{ all -> 0x0068 }
        L_0x0029:
            com.google.android.gms.common.api.Batch r4 = r3.zzch     // Catch:{ all -> 0x0068 }
            r4.zzcd = r4.zzcd - 1     // Catch:{ all -> 0x0068 }
            com.google.android.gms.common.api.Batch r4 = r3.zzch     // Catch:{ all -> 0x0068 }
            int r4 = r4.zzcd     // Catch:{ all -> 0x0068 }
            if (r4 != 0) goto L_0x0066
            com.google.android.gms.common.api.Batch r4 = r3.zzch     // Catch:{ all -> 0x0068 }
            boolean r4 = r4.zzcf     // Catch:{ all -> 0x0068 }
            if (r4 == 0) goto L_0x0044
            com.google.android.gms.common.api.Batch r3 = r3.zzch     // Catch:{ all -> 0x0068 }
            com.google.android.gms.common.api.zza.super.cancel()     // Catch:{ all -> 0x0068 }
            goto L_0x0066
        L_0x0044:
            com.google.android.gms.common.api.Batch r4 = r3.zzch     // Catch:{ all -> 0x0068 }
            boolean r4 = r4.zzce     // Catch:{ all -> 0x0068 }
            if (r4 == 0) goto L_0x0054
            com.google.android.gms.common.api.Status r4 = new com.google.android.gms.common.api.Status     // Catch:{ all -> 0x0068 }
            r1 = 13
            r4.<init>(r1)     // Catch:{ all -> 0x0068 }
            goto L_0x0056
        L_0x0054:
            com.google.android.gms.common.api.Status r4 = com.google.android.gms.common.api.Status.RESULT_SUCCESS     // Catch:{ all -> 0x0068 }
        L_0x0056:
            com.google.android.gms.common.api.Batch r1 = r3.zzch     // Catch:{ all -> 0x0068 }
            com.google.android.gms.common.api.BatchResult r2 = new com.google.android.gms.common.api.BatchResult     // Catch:{ all -> 0x0068 }
            com.google.android.gms.common.api.Batch r3 = r3.zzch     // Catch:{ all -> 0x0068 }
            com.google.android.gms.common.api.PendingResult[] r3 = r3.zzcg     // Catch:{ all -> 0x0068 }
            r2.<init>(r4, r3)     // Catch:{ all -> 0x0068 }
            r1.setResult(r2)     // Catch:{ all -> 0x0068 }
        L_0x0066:
            monitor-exit(r0)     // Catch:{ all -> 0x0068 }
            return
        L_0x0068:
            r3 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0068 }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.zza.onComplete(com.google.android.gms.common.api.Status):void");
    }
}
