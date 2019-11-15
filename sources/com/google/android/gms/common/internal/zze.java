package com.google.android.gms.common.internal;

import android.support.p001v4.util.LruCache;

final class zze extends LruCache<K, V> {
    private final /* synthetic */ ExpirableLruCache zzss;

    zze(ExpirableLruCache expirableLruCache, int i) {
        this.zzss = expirableLruCache;
        super(i);
    }

    /* access modifiers changed from: protected */
    public final V create(K k) {
        return this.zzss.create(k);
    }

    /* access modifiers changed from: protected */
    public final void entryRemoved(boolean z, K k, V v, V v2) {
        this.zzss.entryRemoved(z, k, v, v2);
        synchronized (this.zzss.mLock) {
            if (v2 == null) {
                try {
                    if (this.zzss.zzct()) {
                        this.zzss.zzsq.remove(k);
                    }
                } finally {
                }
            }
            if (v2 == null && this.zzss.zzcu()) {
                this.zzss.zzsr.remove(k);
            }
        }
    }

    /* access modifiers changed from: protected */
    public final int sizeOf(K k, V v) {
        return this.zzss.sizeOf(k, v);
    }
}
