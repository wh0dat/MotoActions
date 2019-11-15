package kotlin.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.CollectionToArray;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\"\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004H\b¢\u0006\u0002\u0010\u0005\u001a4\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0001\"\u0004\b\u0000\u0010\u00062\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u00042\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0001H\b¢\u0006\u0002\u0010\b\u001a\u001f\u0010\t\u001a\b\u0012\u0004\u0012\u0002H\u00060\n\"\u0004\b\u0000\u0010\u00062\u0006\u0010\u000b\u001a\u0002H\u0006¢\u0006\u0002\u0010\f\u001a1\u0010\r\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00020\u0001\"\u0004\b\u0000\u0010\u0006*\n\u0012\u0006\b\u0001\u0012\u0002H\u00060\u00012\u0006\u0010\u000e\u001a\u00020\u000fH\u0000¢\u0006\u0002\u0010\u0010\u001a\u001f\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00060\n\"\u0004\b\u0000\u0010\u0006*\b\u0012\u0004\u0012\u0002H\u00060\u0012H\b¨\u0006\u0013"}, mo14495d2 = {"copyToArrayImpl", "", "", "collection", "", "(Ljava/util/Collection;)[Ljava/lang/Object;", "T", "array", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "listOf", "", "element", "(Ljava/lang/Object;)Ljava/util/List;", "copyToArrayOfAny", "isVarargs", "", "([Ljava/lang/Object;Z)[Ljava/lang/Object;", "toList", "Ljava/util/Enumeration;", "kotlin-stdlib"}, mo14496k = 5, mo14497mv = {1, 1, 10}, mo14499xi = 1, mo14500xs = "kotlin/collections/CollectionsKt")
/* compiled from: CollectionsJVM.kt */
class CollectionsKt__CollectionsJVMKt {
    @NotNull
    public static final <T> List<T> listOf(T t) {
        List<T> singletonList = Collections.singletonList(t);
        Intrinsics.checkExpressionValueIsNotNull(singletonList, "java.util.Collections.singletonList(element)");
        return singletonList;
    }

    @InlineOnly
    private static final <T> List<T> toList(@NotNull Enumeration<T> enumeration) {
        ArrayList list = Collections.list(enumeration);
        Intrinsics.checkExpressionValueIsNotNull(list, "java.util.Collections.list(this)");
        return list;
    }

    @InlineOnly
    private static final Object[] copyToArrayImpl(Collection<?> collection) {
        return CollectionToArray.toArray(collection);
    }

    @InlineOnly
    private static final <T> T[] copyToArrayImpl(Collection<?> collection, T[] tArr) {
        if (tArr == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
        }
        T[] array = CollectionToArray.toArray(collection, tArr);
        if (array != null) {
            return array;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
    }

    @NotNull
    public static final <T> Object[] copyToArrayOfAny(@NotNull T[] tArr, boolean z) {
        Intrinsics.checkParameterIsNotNull(tArr, "$receiver");
        if (z && Intrinsics.areEqual((Object) tArr.getClass(), (Object) Object[].class)) {
            return tArr;
        }
        Object[] copyOf = Arrays.copyOf(tArr, tArr.length, Object[].class);
        Intrinsics.checkExpressionValueIsNotNull(copyOf, "java.util.Arrays.copyOf(… Array<Any?>::class.java)");
        return copyOf;
    }
}
