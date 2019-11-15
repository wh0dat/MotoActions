package p016io.reactivex.internal.operators.single;

import java.util.concurrent.Callable;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.internal.disposables.EmptyDisposable;

/* renamed from: io.reactivex.internal.operators.single.SingleFromCallable */
public final class SingleFromCallable<T> extends Single<T> {
    final Callable<? extends T> callable;

    public SingleFromCallable(Callable<? extends T> callable2) {
        this.callable = callable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> singleObserver) {
        singleObserver.onSubscribe(EmptyDisposable.INSTANCE);
        try {
            Object call = this.callable.call();
            if (call != null) {
                singleObserver.onSuccess(call);
            } else {
                singleObserver.onError(new NullPointerException("The callable returned a null value"));
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            singleObserver.onError(th);
        }
    }
}
