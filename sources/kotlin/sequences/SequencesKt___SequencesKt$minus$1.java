package kotlin.sequences;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.Ref.BooleanRef;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0002¨\u0006\u0005"}, mo14495d2 = {"kotlin/sequences/SequencesKt___SequencesKt$minus$1", "Lkotlin/sequences/Sequence;", "(Lkotlin/sequences/Sequence;Ljava/lang/Object;)V", "iterator", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: _Sequences.kt */
public final class SequencesKt___SequencesKt$minus$1 implements Sequence<T> {
    final /* synthetic */ Object $element;
    final /* synthetic */ Sequence receiver$0;

    SequencesKt___SequencesKt$minus$1(Sequence<? extends T> sequence, Object obj) {
        this.receiver$0 = sequence;
        this.$element = obj;
    }

    @NotNull
    public Iterator<T> iterator() {
        BooleanRef booleanRef = new BooleanRef();
        booleanRef.element = false;
        return SequencesKt.filter(this.receiver$0, new SequencesKt___SequencesKt$minus$1$iterator$1(this, booleanRef)).iterator();
    }
}
