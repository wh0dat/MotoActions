package kotlin.collections;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.ArrayIteratorKt;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0017\n\u0000\n\u0002\u0010\u001c\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0002¨\u0006\u0005¸\u0006\u0000"}, mo14495d2 = {"kotlin/collections/CollectionsKt__IterablesKt$Iterable$1", "", "(Lkotlin/jvm/functions/Function0;)V", "iterator", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: Iterables.kt */
public final class ArraysKt___ArraysKt$asIterable$$inlined$Iterable$1 implements Iterable<T>, KMappedMarker {
    final /* synthetic */ Object[] receiver$0$inlined;

    public ArraysKt___ArraysKt$asIterable$$inlined$Iterable$1(Object[] objArr) {
        this.receiver$0$inlined = objArr;
    }

    @NotNull
    public Iterator<T> iterator() {
        return ArrayIteratorKt.iterator(this.receiver$0$inlined);
    }
}
