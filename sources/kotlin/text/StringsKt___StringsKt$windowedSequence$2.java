package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlin.ranges.RangesKt;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\f\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo14495d2 = {"<anonymous>", "R", "index", "", "invoke", "(I)Ljava/lang/Object;"}, mo14496k = 3, mo14497mv = {1, 1, 10})
/* compiled from: _Strings.kt */
final class StringsKt___StringsKt$windowedSequence$2 extends Lambda implements Function1<Integer, R> {
    final /* synthetic */ int $size;
    final /* synthetic */ Function1 $transform;
    final /* synthetic */ CharSequence receiver$0;

    StringsKt___StringsKt$windowedSequence$2(CharSequence charSequence, Function1 function1, int i) {
        this.receiver$0 = charSequence;
        this.$transform = function1;
        this.$size = i;
        super(1);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return invoke(((Number) obj).intValue());
    }

    public final R invoke(int i) {
        return this.$transform.invoke(this.receiver$0.subSequence(i, RangesKt.coerceAtMost(this.$size + i, this.receiver$0.length())));
    }
}
