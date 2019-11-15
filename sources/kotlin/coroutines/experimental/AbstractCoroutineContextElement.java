package kotlin.coroutines.experimental;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.coroutines.experimental.CoroutineContext.Element;
import kotlin.coroutines.experimental.CoroutineContext.Element.DefaultImpls;
import kotlin.coroutines.experimental.CoroutineContext.Key;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b'\u0018\u00002\u00020\u0001B\u0011\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003¢\u0006\u0002\u0010\u0004R\u0018\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, mo14495d2 = {"Lkotlin/coroutines/experimental/AbstractCoroutineContextElement;", "Lkotlin/coroutines/experimental/CoroutineContext$Element;", "key", "Lkotlin/coroutines/experimental/CoroutineContext$Key;", "(Lkotlin/coroutines/experimental/CoroutineContext$Key;)V", "getKey", "()Lkotlin/coroutines/experimental/CoroutineContext$Key;", "kotlin-stdlib"}, mo14496k = 1, mo14497mv = {1, 1, 10})
@SinceKotlin(version = "1.1")
/* compiled from: CoroutineContextImpl.kt */
public abstract class AbstractCoroutineContextElement implements Element {
    @NotNull
    private final Key<?> key;

    public AbstractCoroutineContextElement(@NotNull Key<?> key2) {
        Intrinsics.checkParameterIsNotNull(key2, "key");
        this.key = key2;
    }

    public <R> R fold(R r, @NotNull Function2<? super R, ? super Element, ? extends R> function2) {
        Intrinsics.checkParameterIsNotNull(function2, "operation");
        return DefaultImpls.fold(this, r, function2);
    }

    @Nullable
    public <E extends Element> E get(@NotNull Key<E> key2) {
        Intrinsics.checkParameterIsNotNull(key2, "key");
        return DefaultImpls.get(this, key2);
    }

    @NotNull
    public Key<?> getKey() {
        return this.key;
    }

    @NotNull
    public CoroutineContext minusKey(@NotNull Key<?> key2) {
        Intrinsics.checkParameterIsNotNull(key2, "key");
        return DefaultImpls.minusKey(this, key2);
    }

    @NotNull
    public CoroutineContext plus(@NotNull CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        return DefaultImpls.plus(this, coroutineContext);
    }
}
