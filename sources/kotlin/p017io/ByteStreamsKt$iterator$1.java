package kotlin.p017io;

import java.io.BufferedInputStream;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.ByteIterator;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000)\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\t\u0010\u0012\u001a\u00020\u0004H\u0002J\b\u0010\t\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0015H\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001a\u0010\t\u001a\u00020\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u0006\"\u0004\b\u0011\u0010\b¨\u0006\u0016"}, mo14495d2 = {"kotlin/io/ByteStreamsKt$iterator$1", "Lkotlin/collections/ByteIterator;", "(Ljava/io/BufferedInputStream;)V", "finished", "", "getFinished", "()Z", "setFinished", "(Z)V", "nextByte", "", "getNextByte", "()I", "setNextByte", "(I)V", "nextPrepared", "getNextPrepared", "setNextPrepared", "hasNext", "", "prepareNext", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* renamed from: kotlin.io.ByteStreamsKt$iterator$1 */
/* compiled from: IOStreams.kt */
public final class ByteStreamsKt$iterator$1 extends ByteIterator {
    private boolean finished;
    private int nextByte = -1;
    private boolean nextPrepared;
    final /* synthetic */ BufferedInputStream receiver$0;

    ByteStreamsKt$iterator$1(BufferedInputStream bufferedInputStream) {
        this.receiver$0 = bufferedInputStream;
    }

    public final int getNextByte() {
        return this.nextByte;
    }

    public final void setNextByte(int i) {
        this.nextByte = i;
    }

    public final boolean getNextPrepared() {
        return this.nextPrepared;
    }

    public final void setNextPrepared(boolean z) {
        this.nextPrepared = z;
    }

    public final boolean getFinished() {
        return this.finished;
    }

    public final void setFinished(boolean z) {
        this.finished = z;
    }

    private final void prepareNext() {
        if (!this.nextPrepared && !this.finished) {
            this.nextByte = this.receiver$0.read();
            boolean z = true;
            this.nextPrepared = true;
            if (this.nextByte != -1) {
                z = false;
            }
            this.finished = z;
        }
    }

    public boolean hasNext() {
        prepareNext();
        return !this.finished;
    }

    public byte nextByte() {
        prepareNext();
        if (this.finished) {
            throw new NoSuchElementException("Input stream is over.");
        }
        byte b = (byte) this.nextByte;
        this.nextPrepared = false;
        return b;
    }
}
