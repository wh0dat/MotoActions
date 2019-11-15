package p016io.reactivex.internal.observers;

import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Action;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.observers.DisposableLambdaObserver */
public final class DisposableLambdaObserver<T> implements Observer<T>, Disposable {
    final Observer<? super T> actual;
    final Action onDispose;
    final Consumer<? super Disposable> onSubscribe;

    /* renamed from: s */
    Disposable f215s;

    public DisposableLambdaObserver(Observer<? super T> observer, Consumer<? super Disposable> consumer, Action action) {
        this.actual = observer;
        this.onSubscribe = consumer;
        this.onDispose = action;
    }

    public void onSubscribe(Disposable disposable) {
        try {
            this.onSubscribe.accept(disposable);
            if (DisposableHelper.validate(this.f215s, disposable)) {
                this.f215s = disposable;
                this.actual.onSubscribe(this);
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            disposable.dispose();
            RxJavaPlugins.onError(th);
            EmptyDisposable.error(th, this.actual);
        }
    }

    public void onNext(T t) {
        this.actual.onNext(t);
    }

    public void onError(Throwable th) {
        this.actual.onError(th);
    }

    public void onComplete() {
        this.actual.onComplete();
    }

    public void dispose() {
        try {
            this.onDispose.run();
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            RxJavaPlugins.onError(th);
        }
        this.f215s.dispose();
    }

    public boolean isDisposed() {
        return this.f215s.isDisposed();
    }
}
