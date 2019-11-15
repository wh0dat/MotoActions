package kotlin.collections;

import java.util.Enumeration;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010(\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u001f\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0002¨\u0006\u0004"}, mo14495d2 = {"iterator", "", "T", "Ljava/util/Enumeration;", "kotlin-stdlib"}, mo14496k = 5, mo14497mv = {1, 1, 10}, mo14499xi = 1, mo14500xs = "kotlin/collections/CollectionsKt")
/* compiled from: IteratorsJVM.kt */
class CollectionsKt__IteratorsJVMKt extends CollectionsKt__IterablesKt {
    @NotNull
    public static final <T> Iterator<T> iterator(@NotNull Enumeration<T> enumeration) {
        Intrinsics.checkParameterIsNotNull(enumeration, "$receiver");
        return new CollectionsKt__IteratorsJVMKt$iterator$1<>(enumeration);
    }
}
