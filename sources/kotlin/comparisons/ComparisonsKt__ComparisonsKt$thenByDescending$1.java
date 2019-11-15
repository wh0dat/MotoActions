package kotlin.comparisons;

import java.util.Comparator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u000e\u0010\u0003\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u00022\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007"}, mo14495d2 = {"<anonymous>", "", "T", "a", "kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I"}, mo14496k = 3, mo14497mv = {1, 1, 10})
/* compiled from: Comparisons.kt */
public final class ComparisonsKt__ComparisonsKt$thenByDescending$1<T> implements Comparator<T> {
    final /* synthetic */ Function1 $selector;
    final /* synthetic */ Comparator receiver$0;

    public ComparisonsKt__ComparisonsKt$thenByDescending$1(Comparator comparator, Function1 function1) {
        this.receiver$0 = comparator;
        this.$selector = function1;
    }

    public final int compare(T t, T t2) {
        int compare = this.receiver$0.compare(t, t2);
        return compare != 0 ? compare : ComparisonsKt.compareValues((Comparable) this.$selector.invoke(t2), (Comparable) this.$selector.invoke(t));
    }
}
