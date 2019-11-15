package kotlin.collections;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.ArrayIteratorsKt;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo14495d2 = {"<anonymous>", "Lkotlin/collections/BooleanIterator;", "invoke"}, mo14496k = 3, mo14497mv = {1, 1, 10})
/* compiled from: _Arrays.kt */
final class ArraysKt___ArraysKt$withIndex$8 extends Lambda implements Function0<BooleanIterator> {
    final /* synthetic */ boolean[] receiver$0;

    ArraysKt___ArraysKt$withIndex$8(boolean[] zArr) {
        this.receiver$0 = zArr;
        super(0);
    }

    @NotNull
    public final BooleanIterator invoke() {
        return ArrayIteratorsKt.iterator(this.receiver$0);
    }
}
