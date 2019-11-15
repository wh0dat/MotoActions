package p016io.reactivex.internal.operators.single;

import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;

/* renamed from: io.reactivex.internal.operators.single.SingleMap */
public final class SingleMap<T, R> extends Single<R> {
    final Function<? super T, ? extends R> mapper;
    final SingleSource<? extends T> source;

    public SingleMap(SingleSource<? extends T> singleSource, Function<? super T, ? extends R> function) {
        this.source = singleSource;
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super R> singleObserver) {
        this.source.subscribe(new SingleObserver<T>() {
            public void onSubscribe(Disposable disposable) {
                singleObserver.onSubscribe(disposable);
            }

            public void onSuccess(T t) {
                try {
                    singleObserver.onSuccess(SingleMap.this.mapper.apply(t));
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    onError(th);
                }
            }

            public void onError(Throwable th) {
                singleObserver.onError(th);
            }
        });
    }
}
