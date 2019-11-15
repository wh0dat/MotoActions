package kotlin.sequences;

import java.util.HashSet;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo14495d2 = {"<anonymous>", "", "T", "it", "invoke", "(Ljava/lang/Object;)Z"}, mo14496k = 3, mo14497mv = {1, 1, 10})
/* compiled from: _Sequences.kt */
final class SequencesKt___SequencesKt$minus$2$iterator$1 extends Lambda implements Function1<T, Boolean> {
    final /* synthetic */ HashSet $other;

    SequencesKt___SequencesKt$minus$2$iterator$1(HashSet hashSet) {
        this.$other = hashSet;
        super(1);
    }

    public final boolean invoke(T t) {
        return this.$other.contains(t);
    }
}
