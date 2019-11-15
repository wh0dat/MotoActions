package p016io.reactivex.internal.operators.single;

import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.single.SingleDoOnSubscribe */
public final class SingleDoOnSubscribe<T> extends Single<T> {
    final Consumer<? super Disposable> onSubscribe;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDoOnSubscribe$DoOnSubscribeSingleObserver */
    static final class DoOnSubscribeSingleObserver<T> implements SingleObserver<T> {
        final SingleObserver<? super T> actual;
        boolean done;
        final Consumer<? super Disposable> onSubscribe;

        DoOnSubscribeSingleObserver(SingleObserver<? super T> singleObserver, Consumer<? super Disposable> consumer) {
            this.actual = singleObserver;
            this.onSubscribe = consumer;
        }

        public void onSubscribe(Disposable disposable) {
            try {
                this.onSubscribe.accept(disposable);
                this.actual.onSubscribe(disposable);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.done = true;
                disposable.dispose();
                EmptyDisposable.error(th, this.actual);
            }
        }

        public void onSuccess(T t) {
            if (!this.done) {
                this.actual.onSuccess(t);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
            } else {
                this.actual.onError(th);
            }
        }
    }

    public SingleDoOnSubscribe(SingleSource<T> singleSource, Consumer<? super Disposable> consumer) {
        this.source = singleSource;
        this.onSubscribe = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> singleObserver) {
        this.source.subscribe(new DoOnSubscribeSingleObserver(singleObserver, this.onSubscribe));
    }
}
