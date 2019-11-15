package p016io.reactivex.internal.operators.maybe;

import p016io.reactivex.Maybe;
import p016io.reactivex.MaybeObserver;
import p016io.reactivex.disposables.Disposables;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeError */
public final class MaybeError<T> extends Maybe<T> {
    final Throwable error;

    public MaybeError(Throwable th) {
        this.error = th;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> maybeObserver) {
        maybeObserver.onSubscribe(Disposables.disposed());
        maybeObserver.onError(this.error);
    }
}
