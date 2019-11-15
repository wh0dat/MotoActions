package p016io.reactivex.internal.operators.single;

import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;

/* renamed from: io.reactivex.internal.operators.single.SingleOnErrorReturn */
public final class SingleOnErrorReturn<T> extends Single<T> {
    final SingleSource<? extends T> source;
    final T value;
    final Function<? super Throwable, ? extends T> valueSupplier;

    public SingleOnErrorReturn(SingleSource<? extends T> singleSource, Function<? super Throwable, ? extends T> function, T t) {
        this.source = singleSource;
        this.valueSupplier = function;
        this.value = t;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super T> singleObserver) {
        this.source.subscribe(new SingleObserver<T>() {
            public void onError(Throwable th) {
                T t;
                if (SingleOnErrorReturn.this.valueSupplier != null) {
                    try {
                        t = SingleOnErrorReturn.this.valueSupplier.apply(th);
                    } catch (Throwable th2) {
                        Exceptions.throwIfFatal(th2);
                        singleObserver.onError(new CompositeException(th, th2));
                        return;
                    }
                } else {
                    t = SingleOnErrorReturn.this.value;
                }
                if (t == null) {
                    NullPointerException nullPointerException = new NullPointerException("Value supplied was null");
                    nullPointerException.initCause(th);
                    singleObserver.onError(nullPointerException);
                    return;
                }
                singleObserver.onSuccess(t);
            }

            public void onSubscribe(Disposable disposable) {
                singleObserver.onSubscribe(disposable);
            }

            public void onSuccess(T t) {
                singleObserver.onSuccess(t);
            }
        });
    }
}
