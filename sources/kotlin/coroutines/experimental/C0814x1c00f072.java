package kotlin.coroutines.experimental;

import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0002¨\u0006\u0005¸\u0006\u0000"}, mo14495d2 = {"kotlin/sequences/SequencesKt__SequencesKt$Sequence$1", "Lkotlin/sequences/Sequence;", "(Lkotlin/jvm/functions/Function0;)V", "iterator", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* renamed from: kotlin.coroutines.experimental.SequenceBuilderKt__SequenceBuilderKt$buildSequence$$inlined$Sequence$1 */
/* compiled from: Sequences.kt */
public final class C0814x1c00f072 implements Sequence<T> {
    final /* synthetic */ Function2 $builderAction$inlined;

    public C0814x1c00f072(Function2 function2) {
        this.$builderAction$inlined = function2;
    }

    @NotNull
    public Iterator<T> iterator() {
        return SequenceBuilderKt.buildIterator(this.$builderAction$inlined);
    }
}
