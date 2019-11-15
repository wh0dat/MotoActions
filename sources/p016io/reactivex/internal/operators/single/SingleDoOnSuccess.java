package p016io.reactivex.internal.operators.single;

import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Consumer;

/* renamed from: io.reactivex.internal.operators.single.SingleDoOnSuccess */
public final class SingleDoOnSuccess<T> extends Single<T> {
    final Consumer<? super T> onSuccess;
    final SingleSource<T> source;

    public SingleDoOnSuccess(SingleSource<T> singleSource, Consumer<? super T> consumer) {
        this.source = singleSource;
        this.onSuccess = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super T> singleObserver) {
        this.source.subscribe(new SingleObserver<T>() {
            public void onSubscribe(Disposable disposable) {
                singleObserver.onSubscribe(disposable);
            }

            public void onSuccess(T t) {
                try {
                    SingleDoOnSuccess.this.onSuccess.accept(t);
                    singleObserver.onSuccess(t);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    singleObserver.onError(th);
                }
            }

            public void onError(Throwable th) {
                singleObserver.onError(th);
            }
        });
    }
}
