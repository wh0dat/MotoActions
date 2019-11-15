package kotlin.collections;

import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004H\u0002¨\u0006\u0005¸\u0006\u0000"}, mo14495d2 = {"kotlin/sequences/SequencesKt__SequencesKt$Sequence$1", "Lkotlin/sequences/Sequence;", "(Lkotlin/jvm/functions/Function0;)V", "iterator", "", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
/* compiled from: Sequences.kt */
public final class SlidingWindowKt$windowedSequence$$inlined$Sequence$1 implements Sequence<List<? extends T>> {
    final /* synthetic */ boolean $partialWindows$inlined;
    final /* synthetic */ boolean $reuseBuffer$inlined;
    final /* synthetic */ int $size$inlined;
    final /* synthetic */ int $step$inlined;
    final /* synthetic */ Sequence receiver$0$inlined;

    public SlidingWindowKt$windowedSequence$$inlined$Sequence$1(Sequence sequence, int i, int i2, boolean z, boolean z2) {
        this.receiver$0$inlined = sequence;
        this.$size$inlined = i;
        this.$step$inlined = i2;
        this.$partialWindows$inlined = z;
        this.$reuseBuffer$inlined = z2;
    }

    @NotNull
    public Iterator<List<? extends T>> iterator() {
        return SlidingWindowKt.windowedIterator(this.receiver$0$inlined.iterator(), this.$size$inlined, this.$step$inlined, this.$partialWindows$inlined, this.$reuseBuffer$inlined);
    }
}
