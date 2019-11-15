package p016io.reactivex.internal.operators.single;

import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposables;

/* renamed from: io.reactivex.internal.operators.single.SingleJust */
public final class SingleJust<T> extends Single<T> {
    final T value;

    public SingleJust(T t) {
        this.value = t;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> singleObserver) {
        singleObserver.onSubscribe(Disposables.disposed());
        singleObserver.onSuccess(this.value);
    }
}
