package p016io.reactivex.internal.operators.maybe;

import p016io.reactivex.MaybeObserver;
import p016io.reactivex.MaybeOperator;
import p016io.reactivex.MaybeSource;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeLift */
public final class MaybeLift<T, R> extends AbstractMaybeWithUpstream<T, R> {
    final MaybeOperator<? extends R, ? super T> operator;

    public MaybeLift(MaybeSource<T> maybeSource, MaybeOperator<? extends R, ? super T> maybeOperator) {
        super(maybeSource);
        this.operator = maybeOperator;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super R> maybeObserver) {
        try {
            this.source.subscribe((MaybeObserver) ObjectHelper.requireNonNull(this.operator.apply(maybeObserver), "The operator returned a null MaybeObserver"));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, maybeObserver);
        }
    }
}
