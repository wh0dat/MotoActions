package p016io.reactivex.internal.operators.completable;

import java.util.concurrent.Callable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;

/* renamed from: io.reactivex.internal.operators.completable.CompletableToSingle */
public final class CompletableToSingle<T> extends Single<T> {
    final T completionValue;
    final Callable<? extends T> completionValueSupplier;
    final CompletableSource source;

    public CompletableToSingle(CompletableSource completableSource, Callable<? extends T> callable, T t) {
        this.source = completableSource;
        this.completionValue = t;
        this.completionValueSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super T> singleObserver) {
        this.source.subscribe(new CompletableObserver() {
            public void onComplete() {
                T t;
                if (CompletableToSingle.this.completionValueSupplier != null) {
                    try {
                        t = CompletableToSingle.this.completionValueSupplier.call();
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        singleObserver.onError(th);
                        return;
                    }
                } else {
                    t = CompletableToSingle.this.completionValue;
                }
                if (t == null) {
                    singleObserver.onError(new NullPointerException("The value supplied is null"));
                } else {
                    singleObserver.onSuccess(t);
                }
            }

            public void onError(Throwable th) {
                singleObserver.onError(th);
            }

            public void onSubscribe(Disposable disposable) {
                singleObserver.onSubscribe(disposable);
            }
        });
    }
}
