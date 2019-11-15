package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.BiFunction;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.observers.SerializedObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableWithLatestFrom */
public final class ObservableWithLatestFrom<T, U, R> extends AbstractObservableWithUpstream<T, R> {
    final BiFunction<? super T, ? super U, ? extends R> combiner;
    final ObservableSource<? extends U> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableWithLatestFrom$WithLatestFromObserver */
    static final class WithLatestFromObserver<T, U, R> extends AtomicReference<U> implements Observer<T>, Disposable {
        private static final long serialVersionUID = -312246233408980075L;
        final Observer<? super R> actual;
        final BiFunction<? super T, ? super U, ? extends R> combiner;
        final AtomicReference<Disposable> other = new AtomicReference<>();

        /* renamed from: s */
        final AtomicReference<Disposable> f503s = new AtomicReference<>();

        WithLatestFromObserver(Observer<? super R> observer, BiFunction<? super T, ? super U, ? extends R> biFunction) {
            this.actual = observer;
            this.combiner = biFunction;
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this.f503s, disposable);
        }

        public void onNext(T t) {
            Object obj = get();
            if (obj != null) {
                try {
                    this.actual.onNext(this.combiner.apply(t, obj));
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    dispose();
                    this.actual.onError(th);
                }
            }
        }

        public void onError(Throwable th) {
            DisposableHelper.dispose(this.other);
            this.actual.onError(th);
        }

        public void onComplete() {
            DisposableHelper.dispose(this.other);
            this.actual.onComplete();
        }

        public void dispose() {
            DisposableHelper.dispose(this.f503s);
            DisposableHelper.dispose(this.other);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.f503s.get());
        }

        public boolean setOther(Disposable disposable) {
            return DisposableHelper.setOnce(this.other, disposable);
        }

        public void otherError(Throwable th) {
            DisposableHelper.dispose(this.f503s);
            this.actual.onError(th);
        }
    }

    public ObservableWithLatestFrom(ObservableSource<T> observableSource, BiFunction<? super T, ? super U, ? extends R> biFunction, ObservableSource<? extends U> observableSource2) {
        super(observableSource);
        this.combiner = biFunction;
        this.other = observableSource2;
    }

    public void subscribeActual(Observer<? super R> observer) {
        final WithLatestFromObserver withLatestFromObserver = new WithLatestFromObserver(new SerializedObserver(observer), this.combiner);
        observer.onSubscribe(withLatestFromObserver);
        this.other.subscribe(new Observer<U>() {
            public void onComplete() {
            }

            public void onSubscribe(Disposable disposable) {
                withLatestFromObserver.setOther(disposable);
            }

            public void onNext(U u) {
                withLatestFromObserver.lazySet(u);
            }

            public void onError(Throwable th) {
                withLatestFromObserver.otherError(th);
            }
        });
        this.source.subscribe(withLatestFromObserver);
    }
}
