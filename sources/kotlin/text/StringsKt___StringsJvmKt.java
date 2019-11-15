package kotlin.text;

import java.util.SortedSet;
import java.util.TreeSet;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\u0010\r\n\u0000\u001a\u0010\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003¨\u0006\u0004"}, mo14495d2 = {"toSortedSet", "Ljava/util/SortedSet;", "", "", "kotlin-stdlib"}, mo14496k = 5, mo14497mv = {1, 1, 10}, mo14499xi = 1, mo14500xs = "kotlin/text/StringsKt")
/* compiled from: _StringsJvm.kt */
class StringsKt___StringsJvmKt extends StringsKt__StringsKt {
    @NotNull
    public static final SortedSet<Character> toSortedSet(@NotNull CharSequence charSequence) {
        Intrinsics.checkParameterIsNotNull(charSequence, "$receiver");
        return (SortedSet) StringsKt.toCollection(charSequence, new TreeSet());
    }
}
