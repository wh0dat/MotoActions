package com.motorola.actions.attentivedisplay.util;

import android.util.SparseArray;

public final class ByteArrayPool {
    private static final ThreadLocal<ByteArrayPool> INSTANCES = new ThreadLocal<ByteArrayPool>() {
        /* access modifiers changed from: protected */
        public ByteArrayPool initialValue() {
            return new ByteArrayPool();
        }
    };
    private SparseArray<byte[]> mPool;

    private ByteArrayPool() {
        this.mPool = new SparseArray<>();
    }

    public static ByteArrayPool getInstance() {
        return (ByteArrayPool) INSTANCES.get();
    }

    public byte[] acquire(int i) {
        byte[] bArr = (byte[]) this.mPool.get(i);
        return bArr == null ? new byte[i] : bArr;
    }

    public void release(byte[] bArr) {
        this.mPool.put(bArr.length, bArr);
    }
}
