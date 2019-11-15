package kotlin;

import kotlin.coroutines.experimental.Continuation;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\u001aM\u0010\u0000\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0001\"\u0004\b\u0000\u0010\u00032\u001e\b\b\u0010\u0005\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00030\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0001H\bø\u0001\u0000¢\u0006\u0002\u0010\u0006\u0002\t\n\u0007\b\u001e\u0018\u00020\u0001¨\u0006\u0007"}, mo14495d2 = {"suspend", "Lkotlin/Function1;", "Lkotlin/coroutines/experimental/Continuation;", "R", "", "block", "(Lkotlin/jvm/functions/Function1;)Lkotlin/jvm/functions/Function1;", "kotlin-stdlib"}, mo14496k = 2, mo14497mv = {1, 1, 10})
/* compiled from: Suspend.kt */
public final class SuspendKt {
    @SinceKotlin(version = "1.2")
    @InlineOnly
    private static final <R> Function1<Continuation<? super R>, Object> suspend(Function1<? super Continuation<? super R>, ? extends Object> function1) {
        return function1;
    }
}
