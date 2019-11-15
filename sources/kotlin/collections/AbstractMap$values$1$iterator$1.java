package kotlin.collections;

import java.util.Iterator;
import java.util.Map.Entry;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0015\n\u0000\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\t\u0010\u0003\u001a\u00020\u0004H\u0002J\u000e\u0010\u0005\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\u0006¨\u0006\u0007"}, mo14495d2 = {"kotlin/collections/AbstractMap$values$1$iterator$1", "", "(Ljava/util/Iterator;)V", "hasNext", "", "next", "()Ljava/lang/Object;", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: AbstractMap.kt */
public final class AbstractMap$values$1$iterator$1 implements Iterator<V>, KMappedMarker {
    final /* synthetic */ Iterator $entryIterator;

    public void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    AbstractMap$values$1$iterator$1(Iterator it) {
        this.$entryIterator = it;
    }

    public boolean hasNext() {
        return this.$entryIterator.hasNext();
    }

    public V next() {
        return ((Entry) this.$entryIterator.next()).getValue();
    }
}
