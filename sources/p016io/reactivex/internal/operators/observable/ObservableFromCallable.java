package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.Callable;
import p016io.reactivex.Observable;
import p016io.reactivex.Observer;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.observers.DeferredScalarDisposable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableFromCallable */
public final class ObservableFromCallable<T> extends Observable<T> implements Callable<T> {
    final Callable<? extends T> callable;

    public ObservableFromCallable(Callable<? extends T> callable2) {
        this.callable = callable2;
    }

    public void subscribeActual(Observer<? super T> observer) {
        DeferredScalarDisposable deferredScalarDisposable = new DeferredScalarDisposable(observer);
        observer.onSubscribe(deferredScalarDisposable);
        if (!deferredScalarDisposable.isDisposed()) {
            try {
                deferredScalarDisposable.complete(ObjectHelper.requireNonNull(this.callable.call(), "Callable returned null"));
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                if (!deferredScalarDisposable.isDisposed()) {
                    observer.onError(th);
                } else {
                    RxJavaPlugins.onError(th);
                }
            }
        }
    }

    public T call() throws Exception {
        return this.callable.call();
    }
}
