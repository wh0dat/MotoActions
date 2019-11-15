package kotlin.sequences;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.collections.IndexedValue;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000!\n\u0000\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\t\u0010\r\u001a\u00020\u000eH\u0002J\u000f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002H\u0002R\u001a\u0010\u0004\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00028\u00000\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f¨\u0006\u0010"}, mo14495d2 = {"kotlin/sequences/IndexingSequence$iterator$1", "", "Lkotlin/collections/IndexedValue;", "(Lkotlin/sequences/IndexingSequence;)V", "index", "", "getIndex", "()I", "setIndex", "(I)V", "iterator", "getIterator", "()Ljava/util/Iterator;", "hasNext", "", "next", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: Sequences.kt */
public final class IndexingSequence$iterator$1 implements Iterator<IndexedValue<? extends T>>, KMappedMarker {
    private int index;
    @NotNull
    private final Iterator<T> iterator;
    final /* synthetic */ IndexingSequence this$0;

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    IndexingSequence$iterator$1(IndexingSequence indexingSequence) {
        this.this$0 = indexingSequence;
        this.iterator = indexingSequence.sequence.iterator();
    }

    @NotNull
    public final Iterator<T> getIterator() {
        return this.iterator;
    }

    public final int getIndex() {
        return this.index;
    }

    public final void setIndex(int i) {
        this.index = i;
    }

    @NotNull
    public IndexedValue<T> next() {
        int i = this.index;
        this.index = i + 1;
        return new IndexedValue<>(i, this.iterator.next());
    }

    public boolean hasNext() {
        return this.iterator.hasNext();
    }
}
