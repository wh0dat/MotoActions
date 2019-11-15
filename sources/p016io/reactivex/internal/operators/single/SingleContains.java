package p016io.reactivex.internal.operators.single;

import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.BiPredicate;

/* renamed from: io.reactivex.internal.operators.single.SingleContains */
public final class SingleContains<T> extends Single<Boolean> {
    final BiPredicate<Object, Object> comparer;
    final SingleSource<T> source;
    final Object value;

    public SingleContains(SingleSource<T> singleSource, Object obj, BiPredicate<Object, Object> biPredicate) {
        this.source = singleSource;
        this.value = obj;
        this.comparer = biPredicate;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super Boolean> singleObserver) {
        this.source.subscribe(new SingleObserver<T>() {
            public void onSubscribe(Disposable disposable) {
                singleObserver.onSubscribe(disposable);
            }

            public void onSuccess(T t) {
                try {
                    singleObserver.onSuccess(Boolean.valueOf(SingleContains.this.comparer.test(t, SingleContains.this.value)));
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
