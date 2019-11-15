package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableFlattenIterable */
public final class ObservableFlattenIterable<T, R> extends AbstractObservableWithUpstream<T, R> {
    final Function<? super T, ? extends Iterable<? extends R>> mapper;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableFlattenIterable$FlattenIterableObserver */
    static final class FlattenIterableObserver<T, R> implements Observer<T>, Disposable {
        final Observer<? super R> actual;

        /* renamed from: d */
        Disposable f436d;
        final Function<? super T, ? extends Iterable<? extends R>> mapper;

        FlattenIterableObserver(Observer<? super R> observer, Function<? super T, ? extends Iterable<? extends R>> function) {
            this.actual = observer;
            this.mapper = function;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f436d, disposable)) {
                this.f436d = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (this.f436d != DisposableHelper.DISPOSED) {
                try {
                    Observer<? super R> observer = this.actual;
                    for (Object requireNonNull : (Iterable) this.mapper.apply(t)) {
                        try {
                            try {
                                observer.onNext(ObjectHelper.requireNonNull(requireNonNull, "The iterator returned a null value"));
                            } catch (Throwable th) {
                                Exceptions.throwIfFatal(th);
                                this.f436d.dispose();
                                onError(th);
                                return;
                            }
                        } catch (Throwable th2) {
                            Exceptions.throwIfFatal(th2);
                            this.f436d.dispose();
                            onError(th2);
                            return;
                        }
                    }
                } catch (Throwable th3) {
                    Exceptions.throwIfFatal(th3);
                    this.f436d.dispose();
                    onError(th3);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.f436d == DisposableHelper.DISPOSED) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.f436d = DisposableHelper.DISPOSED;
            this.actual.onError(th);
        }

        public void onComplete() {
            if (this.f436d != DisposableHelper.DISPOSED) {
                this.f436d = DisposableHelper.DISPOSED;
                this.actual.onComplete();
            }
        }

        public boolean isDisposed() {
            return this.f436d.isDisposed();
        }

        public void dispose() {
            this.f436d.dispose();
            this.f436d = DisposableHelper.DISPOSED;
        }
    }

    public ObservableFlattenIterable(ObservableSource<T> observableSource, Function<? super T, ? extends Iterable<? extends R>> function) {
        super(observableSource);
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super R> observer) {
        this.source.subscribe(new FlattenIterableObserver(observer, this.mapper));
    }
}
