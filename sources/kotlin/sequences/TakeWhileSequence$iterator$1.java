package kotlin.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000#\n\u0000\n\u0002\u0010(\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\t\u0010\u0014\u001a\u00020\u0015H\u0002J\u000e\u0010\u0016\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\bR\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0005R\u001e\u0010\u0006\u001a\u0004\u0018\u00018\u0000X\u000e¢\u0006\u0010\n\u0002\u0010\u000b\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001a\u0010\f\u001a\u00020\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011¨\u0006\u0017"}, mo14495d2 = {"kotlin/sequences/TakeWhileSequence$iterator$1", "", "(Lkotlin/sequences/TakeWhileSequence;)V", "iterator", "getIterator", "()Ljava/util/Iterator;", "nextItem", "getNextItem", "()Ljava/lang/Object;", "setNextItem", "(Ljava/lang/Object;)V", "Ljava/lang/Object;", "nextState", "", "getNextState", "()I", "setNextState", "(I)V", "calcNext", "", "hasNext", "", "next", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: Sequences.kt */
public final class TakeWhileSequence$iterator$1 implements Iterator<T>, KMappedMarker {
    @NotNull
    private final Iterator<T> iterator;
    @Nullable
    private T nextItem;
    private int nextState = -1;
    final /* synthetic */ TakeWhileSequence this$0;

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    TakeWhileSequence$iterator$1(TakeWhileSequence takeWhileSequence) {
        this.this$0 = takeWhileSequence;
        this.iterator = takeWhileSequence.sequence.iterator();
    }

    @NotNull
    public final Iterator<T> getIterator() {
        return this.iterator;
    }

    public final int getNextState() {
        return this.nextState;
    }

    public final void setNextState(int i) {
        this.nextState = i;
    }

    @Nullable
    public final T getNextItem() {
        return this.nextItem;
    }

    public final void setNextItem(@Nullable T t) {
        this.nextItem = t;
    }

    private final void calcNext() {
        if (this.iterator.hasNext()) {
            T next = this.iterator.next();
            if (((Boolean) this.this$0.predicate.invoke(next)).booleanValue()) {
                this.nextState = 1;
                this.nextItem = next;
                return;
            }
        }
        this.nextState = 0;
    }

    public T next() {
        if (this.nextState == -1) {
            calcNext();
        }
        if (this.nextState == 0) {
            throw new NoSuchElementException();
        }
        T t = this.nextItem;
        this.nextItem = null;
        this.nextState = -1;
        return t;
    }

    public boolean hasNext() {
        if (this.nextState == -1) {
            calcNext();
        }
        return this.nextState == 1;
    }
}
