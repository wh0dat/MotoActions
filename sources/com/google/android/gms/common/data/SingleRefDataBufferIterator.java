package com.google.android.gms.common.data;

import java.util.NoSuchElementException;

public class SingleRefDataBufferIterator<T> extends DataBufferIterator<T> {
    private T zzoj;

    public SingleRefDataBufferIterator(DataBuffer<T> dataBuffer) {
        super(dataBuffer);
    }

    public T next() {
        if (!hasNext()) {
            int i = this.mPosition;
            StringBuilder sb = new StringBuilder(46);
            sb.append("Cannot advance the iterator beyond ");
            sb.append(i);
            throw new NoSuchElementException(sb.toString());
        }
        this.mPosition++;
        if (this.mPosition == 0) {
            this.zzoj = this.mDataBuffer.get(0);
            if (!(this.zzoj instanceof DataBufferRef)) {
                String valueOf = String.valueOf(this.zzoj.getClass());
                StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf).length() + 44);
                sb2.append("DataBuffer reference of type ");
                sb2.append(valueOf);
                sb2.append(" is not movable");
                throw new IllegalStateException(sb2.toString());
            }
        } else {
            ((DataBufferRef) this.zzoj).setDataRow(this.mPosition);
        }
        return this.zzoj;
    }
}
