package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.ArrayCompositeDisposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.observers.SerializedObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSkipUntil */
public final class ObservableSkipUntil<T, U> extends AbstractObservableWithUpstream<T, T> {
    final ObservableSource<U> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSkipUntil$SkipUntilObserver */
    static final class SkipUntilObserver<T> implements Observer<T> {
        final Observer<? super T> actual;
        final ArrayCompositeDisposable frc;
        volatile boolean notSkipping;
        boolean notSkippingLocal;

        /* renamed from: s */
        Disposable f471s;

        SkipUntilObserver(Observer<? super T> observer, ArrayCompositeDisposable arrayCompositeDisposable) {
            this.actual = observer;
            this.frc = arrayCompositeDisposable;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f471s, disposable)) {
                this.f471s = disposable;
                this.frc.setResource(0, disposable);
            }
        }

        public void onNext(T t) {
            if (this.notSkippingLocal) {
                this.actual.onNext(t);
            } else if (this.notSkipping) {
                this.notSkippingLocal = true;
                this.actual.onNext(t);
            }
        }

        public void onError(Throwable th) {
            this.frc.dispose();
            this.actual.onError(th);
        }

        public void onComplete() {
            this.frc.dispose();
            this.actual.onComplete();
        }
    }

    public ObservableSkipUntil(ObservableSource<T> observableSource, ObservableSource<U> observableSource2) {
        super(observableSource);
        this.other = observableSource2;
    }

    public void subscribeActual(Observer<? super T> observer) {
        final SerializedObserver serializedObserver = new SerializedObserver(observer);
        final ArrayCompositeDisposable arrayCompositeDisposable = new ArrayCompositeDisposable(2);
        serializedObserver.onSubscribe(arrayCompositeDisposable);
        final SkipUntilObserver skipUntilObserver = new SkipUntilObserver(serializedObserver, arrayCompositeDisposable);
        this.other.subscribe(new Observer<U>() {

            /* renamed from: s */
            Disposable f470s;

            public void onSubscribe(Disposable disposable) {
                if (DisposableHelper.validate(this.f470s, disposable)) {
                    this.f470s = disposable;
                    arrayCompositeDisposable.setResource(1, disposable);
                }
            }

            public void onNext(U u) {
                this.f470s.dispose();
                skipUntilObserver.notSkipping = true;
            }

            public void onError(Throwable th) {
                arrayCompositeDisposable.dispose();
                serializedObserver.onError(th);
            }

            public void onComplete() {
                skipUntilObserver.notSkipping = true;
            }
        });
        this.source.subscribe(skipUntilObserver);
    }
}
