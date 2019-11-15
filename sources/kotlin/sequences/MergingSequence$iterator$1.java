package kotlin.sequences;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0015\n\u0000\n\u0002\u0010(\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\t\u0010\b\u001a\u00020\tH\u0002J\u000e\u0010\n\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u000bR\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0005R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00028\u00010\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u0005¨\u0006\f"}, mo14495d2 = {"kotlin/sequences/MergingSequence$iterator$1", "", "(Lkotlin/sequences/MergingSequence;)V", "iterator1", "getIterator1", "()Ljava/util/Iterator;", "iterator2", "getIterator2", "hasNext", "", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: Sequences.kt */
public final class MergingSequence$iterator$1 implements Iterator<V>, KMappedMarker {
    @NotNull
    private final Iterator<T1> iterator1;
    @NotNull
    private final Iterator<T2> iterator2;
    final /* synthetic */ MergingSequence this$0;

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    MergingSequence$iterator$1(MergingSequence mergingSequence) {
        this.this$0 = mergingSequence;
        this.iterator1 = mergingSequence.sequence1.iterator();
        this.iterator2 = mergingSequence.sequence2.iterator();
    }

    @NotNull
    public final Iterator<T1> getIterator1() {
        return this.iterator1;
    }

    @NotNull
    public final Iterator<T2> getIterator2() {
        return this.iterator2;
    }

    public V next() {
        return this.this$0.transform.invoke(this.iterator1.next(), this.iterator2.next());
    }

    public boolean hasNext() {
        return this.iterator1.hasNext() && this.iterator2.hasNext();
    }
}
