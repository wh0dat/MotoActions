package p016io.reactivex.internal.operators.single;

import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Consumer;

/* renamed from: io.reactivex.internal.operators.single.SingleDoOnError */
public final class SingleDoOnError<T> extends Single<T> {
    final Consumer<? super Throwable> onError;
    final SingleSource<T> source;

    public SingleDoOnError(SingleSource<T> singleSource, Consumer<? super Throwable> consumer) {
        this.source = singleSource;
        this.onError = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super T> singleObserver) {
        this.source.subscribe(new SingleObserver<T>() {
            public void onSubscribe(Disposable disposable) {
                singleObserver.onSubscribe(disposable);
            }

            public void onSuccess(T t) {
                singleObserver.onSuccess(t);
            }

            public void onError(Throwable th) {
                try {
                    SingleDoOnError.this.onError.accept(th);
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    th = new CompositeException(th, th2);
                }
                singleObserver.onError(th);
            }
        });
    }
}
