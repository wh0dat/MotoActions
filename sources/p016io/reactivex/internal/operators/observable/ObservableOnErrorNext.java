package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.SequentialDisposable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableOnErrorNext */
public final class ObservableOnErrorNext<T> extends AbstractObservableWithUpstream<T, T> {
    final boolean allowFatal;
    final Function<? super Throwable, ? extends ObservableSource<? extends T>> nextSupplier;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableOnErrorNext$OnErrorNextObserver */
    static final class OnErrorNextObserver<T> implements Observer<T> {
        final Observer<? super T> actual;
        final boolean allowFatal;
        final SequentialDisposable arbiter = new SequentialDisposable();
        boolean done;
        final Function<? super Throwable, ? extends ObservableSource<? extends T>> nextSupplier;
        boolean once;

        OnErrorNextObserver(Observer<? super T> observer, Function<? super Throwable, ? extends ObservableSource<? extends T>> function, boolean z) {
            this.actual = observer;
            this.nextSupplier = function;
            this.allowFatal = z;
        }

        public void onSubscribe(Disposable disposable) {
            this.arbiter.replace(disposable);
        }

        public void onNext(T t) {
            if (!this.done) {
                this.actual.onNext(t);
            }
        }

        public void onError(Throwable th) {
            if (!this.once) {
                this.once = true;
                if (!this.allowFatal || (th instanceof Exception)) {
                    try {
                        ObservableSource observableSource = (ObservableSource) this.nextSupplier.apply(th);
                        if (observableSource == null) {
                            NullPointerException nullPointerException = new NullPointerException("Observable is null");
                            nullPointerException.initCause(th);
                            this.actual.onError(nullPointerException);
                            return;
                        }
                        observableSource.subscribe(this);
                    } catch (Throwable th2) {
                        Exceptions.throwIfFatal(th2);
                        this.actual.onError(new CompositeException(th, th2));
                    }
                } else {
                    this.actual.onError(th);
                }
            } else if (this.done) {
                RxJavaPlugins.onError(th);
            } else {
                this.actual.onError(th);
            }
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.once = true;
                this.actual.onComplete();
            }
        }
    }

    public ObservableOnErrorNext(ObservableSource<T> observableSource, Function<? super Throwable, ? extends ObservableSource<? extends T>> function, boolean z) {
        super(observableSource);
        this.nextSupplier = function;
        this.allowFatal = z;
    }

    public void subscribeActual(Observer<? super T> observer) {
        OnErrorNextObserver onErrorNextObserver = new OnErrorNextObserver(observer, this.nextSupplier, this.allowFatal);
        observer.onSubscribe(onErrorNextObserver.arbiter);
        this.source.subscribe(onErrorNextObserver);
    }
}
