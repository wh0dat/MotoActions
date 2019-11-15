package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.BiFunction;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableScan */
public final class ObservableScan<T> extends AbstractObservableWithUpstream<T, T> {
    final BiFunction<T, T, T> accumulator;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableScan$ScanObserver */
    static final class ScanObserver<T> implements Observer<T>, Disposable {
        final BiFunction<T, T, T> accumulator;
        final Observer<? super T> actual;

        /* renamed from: s */
        Disposable f458s;
        T value;

        ScanObserver(Observer<? super T> observer, BiFunction<T, T, T> biFunction) {
            this.actual = observer;
            this.accumulator = biFunction;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f458s, disposable)) {
                this.f458s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f458s.dispose();
        }

        public boolean isDisposed() {
            return this.f458s.isDisposed();
        }

        public void onNext(T t) {
            Observer<? super T> observer = this.actual;
            T t2 = this.value;
            if (t2 == null) {
                this.value = t;
                observer.onNext(t);
            } else {
                try {
                    T requireNonNull = ObjectHelper.requireNonNull(this.accumulator.apply(t2, t), "The value returned by the accumulator is null");
                    this.value = requireNonNull;
                    observer.onNext(requireNonNull);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.f458s.dispose();
                    observer.onError(th);
                }
            }
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    public ObservableScan(ObservableSource<T> observableSource, BiFunction<T, T, T> biFunction) {
        super(observableSource);
        this.accumulator = biFunction;
    }

    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new ScanObserver(observer, this.accumulator));
    }
}
