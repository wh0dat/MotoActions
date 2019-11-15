package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicBoolean;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.ArrayCompositeDisposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.observers.SerializedObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableTakeUntil */
public final class ObservableTakeUntil<T, U> extends AbstractObservableWithUpstream<T, T> {
    final ObservableSource<? extends U> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTakeUntil$TakeUntilObserver */
    static final class TakeUntilObserver<T> extends AtomicBoolean implements Observer<T> {
        private static final long serialVersionUID = 3451719290311127173L;
        final Observer<? super T> actual;
        final ArrayCompositeDisposable frc;

        /* renamed from: s */
        Disposable f478s;

        TakeUntilObserver(Observer<? super T> observer, ArrayCompositeDisposable arrayCompositeDisposable) {
            this.actual = observer;
            this.frc = arrayCompositeDisposable;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f478s, disposable)) {
                this.f478s = disposable;
                this.frc.setResource(0, disposable);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
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

    public ObservableTakeUntil(ObservableSource<T> observableSource, ObservableSource<? extends U> observableSource2) {
        super(observableSource);
        this.other = observableSource2;
    }

    public void subscribeActual(Observer<? super T> observer) {
        final SerializedObserver serializedObserver = new SerializedObserver(observer);
        final ArrayCompositeDisposable arrayCompositeDisposable = new ArrayCompositeDisposable(2);
        TakeUntilObserver takeUntilObserver = new TakeUntilObserver(serializedObserver, arrayCompositeDisposable);
        observer.onSubscribe(arrayCompositeDisposable);
        this.other.subscribe(new Observer<U>() {
            public void onSubscribe(Disposable disposable) {
                arrayCompositeDisposable.setResource(1, disposable);
            }

            public void onNext(U u) {
                arrayCompositeDisposable.dispose();
                serializedObserver.onComplete();
            }

            public void onError(Throwable th) {
                arrayCompositeDisposable.dispose();
                serializedObserver.onError(th);
            }

            public void onComplete() {
                arrayCompositeDisposable.dispose();
                serializedObserver.onComplete();
            }
        });
        this.source.subscribe(takeUntilObserver);
    }
}
