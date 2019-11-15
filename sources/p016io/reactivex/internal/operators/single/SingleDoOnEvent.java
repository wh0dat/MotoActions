package p016io.reactivex.internal.operators.single;

import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.BiConsumer;

/* renamed from: io.reactivex.internal.operators.single.SingleDoOnEvent */
public final class SingleDoOnEvent<T> extends Single<T> {
    final BiConsumer<? super T, ? super Throwable> onEvent;
    final SingleSource<T> source;

    public SingleDoOnEvent(SingleSource<T> singleSource, BiConsumer<? super T, ? super Throwable> biConsumer) {
        this.source = singleSource;
        this.onEvent = biConsumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super T> singleObserver) {
        this.source.subscribe(new SingleObserver<T>() {
            public void onSubscribe(Disposable disposable) {
                singleObserver.onSubscribe(disposable);
            }

            public void onSuccess(T t) {
                try {
                    SingleDoOnEvent.this.onEvent.accept(t, null);
                    singleObserver.onSuccess(t);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    singleObserver.onError(th);
                }
            }

            public void onError(Throwable th) {
                try {
                    SingleDoOnEvent.this.onEvent.accept(null, th);
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    th = new CompositeException(th, th2);
                }
                singleObserver.onError(th);
            }
        });
    }
}
