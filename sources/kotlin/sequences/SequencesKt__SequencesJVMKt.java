package kotlin.sequences;

import java.util.Enumeration;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.internal.InlineOnly;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u001f\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\b¨\u0006\u0004"}, mo14495d2 = {"asSequence", "Lkotlin/sequences/Sequence;", "T", "Ljava/util/Enumeration;", "kotlin-stdlib"}, mo14496k = 5, mo14497mv = {1, 1, 10}, mo14499xi = 1, mo14500xs = "kotlin/sequences/SequencesKt")
/* compiled from: SequencesJVM.kt */
class SequencesKt__SequencesJVMKt {
    @InlineOnly
    private static final <T> Sequence<T> asSequence(@NotNull Enumeration<T> enumeration) {
        return SequencesKt.asSequence(CollectionsKt.iterator(enumeration));
    }
}
