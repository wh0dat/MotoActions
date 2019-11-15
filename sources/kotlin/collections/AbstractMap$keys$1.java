package kotlin.collections;

import java.util.Iterator;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010\nJ\u000f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00028\u00000\fH\u0002R\u0014\u0010\u0003\u001a\u00020\u00048VX\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\r"}, mo14495d2 = {"kotlin/collections/AbstractMap$keys$1", "Lkotlin/collections/AbstractSet;", "(Lkotlin/collections/AbstractMap;)V", "size", "", "getSize", "()I", "contains", "", "element", "(Ljava/lang/Object;)Z", "iterator", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: AbstractMap.kt */
public final class AbstractMap$keys$1 extends AbstractSet<K> {
    final /* synthetic */ AbstractMap this$0;

    AbstractMap$keys$1(AbstractMap abstractMap) {
        this.this$0 = abstractMap;
    }

    public boolean contains(Object obj) {
        return this.this$0.containsKey(obj);
    }

    @NotNull
    public Iterator<K> iterator() {
        return new AbstractMap$keys$1$iterator$1<>(this.this$0.entrySet().iterator());
    }

    public int getSize() {
        return this.this$0.size();
    }
}
