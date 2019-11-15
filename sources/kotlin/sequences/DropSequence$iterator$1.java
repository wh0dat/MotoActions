package kotlin.sequences;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000#\n\u0000\n\u0002\u0010(\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\f\u001a\u00020\rH\u0002J\t\u0010\u000e\u001a\u00020\u000fH\u0002J\u000e\u0010\u0010\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u0011R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0005R\u001a\u0010\u0006\u001a\u00020\u0007X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000b¨\u0006\u0012"}, mo14495d2 = {"kotlin/sequences/DropSequence$iterator$1", "", "(Lkotlin/sequences/DropSequence;)V", "iterator", "getIterator", "()Ljava/util/Iterator;", "left", "", "getLeft", "()I", "setLeft", "(I)V", "drop", "", "hasNext", "", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: Sequences.kt */
public final class DropSequence$iterator$1 implements Iterator<T>, KMappedMarker {
    @NotNull
    private final Iterator<T> iterator;
    private int left;
    final /* synthetic */ DropSequence this$0;

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    DropSequence$iterator$1(DropSequence dropSequence) {
        this.this$0 = dropSequence;
        this.iterator = dropSequence.sequence.iterator();
        this.left = dropSequence.count;
    }

    @NotNull
    public final Iterator<T> getIterator() {
        return this.iterator;
    }

    public final int getLeft() {
        return this.left;
    }

    public final void setLeft(int i) {
        this.left = i;
    }

    private final void drop() {
        while (this.left > 0 && this.iterator.hasNext()) {
            this.iterator.next();
            this.left--;
        }
    }

    public T next() {
        drop();
        return this.iterator.next();
    }

    public boolean hasNext() {
        drop();
        return this.iterator.hasNext();
    }
}
