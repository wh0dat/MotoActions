package kotlin;

import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.InlineMarker;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0012\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a*\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0005H\b¢\u0006\u0002\u0010\u0006¨\u0006\u0007"}, mo14495d2 = {"synchronized", "R", "lock", "", "block", "Lkotlin/Function0;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "kotlin-stdlib"}, mo14496k = 5, mo14497mv = {1, 1, 10}, mo14499xi = 1, mo14500xs = "kotlin/StandardKt")
/* compiled from: Synchronized.kt */
class StandardKt__SynchronizedKt extends StandardKt__StandardKt {
    @InlineOnly
    /* renamed from: synchronized reason: not valid java name */
    private static final <R> R m107synchronized(Object obj, Function0<? extends R> function0) {
        R invoke;
        synchronized (obj) {
            try {
                invoke = function0.invoke();
            } finally {
                InlineMarker.finallyStart(1);
                InlineMarker.finallyEnd(1);
            }
        }
        InlineMarker.finallyEnd(1);
        return invoke;
    }
}
