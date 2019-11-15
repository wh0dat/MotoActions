package kotlin.sequences;

import java.util.Iterator;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010(\n\u0000\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H¦\u0002¨\u0006\u0005"}, mo14495d2 = {"Lkotlin/sequences/Sequence;", "T", "", "iterator", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: Sequence.kt */
public interface Sequence<T> {
    @NotNull
    Iterator<T> iterator();
}
