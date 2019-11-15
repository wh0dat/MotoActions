package com.google.android.gms.common.internal;

import android.support.annotation.NonNull;

public class GmsServiceEndpoint {
    @NonNull
    private final String mPackageName;
    private final int zztq;
    @NonNull
    private final String zzue;
    private final boolean zzuf;

    public GmsServiceEndpoint(@NonNull String str, @NonNull String str2, boolean z, int i) {
        this.mPackageName = str;
        this.zzue = str2;
        this.zzuf = z;
        this.zztq = i;
    }

    /* access modifiers changed from: 0000 */
    public final int getBindFlags() {
        return this.zztq;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public final String getPackageName() {
        return this.mPackageName;
    }

    /* access modifiers changed from: 0000 */
    @NonNull
    public final String zzcw() {
        return this.zzue;
    }
}
