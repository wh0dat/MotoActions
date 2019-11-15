package kotlin.collections;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\n\n\u0000\n\u0002\u0010\r\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002 \u00012\u0006\u0010\u0003\u001a\u0002H\u0002H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo14495d2 = {"<anonymous>", "", "E", "it", "invoke", "(Ljava/lang/Object;)Ljava/lang/CharSequence;"}, mo14496k = 3, mo14497mv = {1, 1, 10})
/* compiled from: AbstractCollection.kt */
final class AbstractCollection$toString$1 extends Lambda implements Function1<E, CharSequence> {
    final /* synthetic */ AbstractCollection this$0;

    AbstractCollection$toString$1(AbstractCollection abstractCollection) {
        this.this$0 = abstractCollection;
        super(1);
    }

    @NotNull
    public final CharSequence invoke(E e) {
        return e == this.this$0 ? "(this Collection)" : String.valueOf(e);
    }
}
