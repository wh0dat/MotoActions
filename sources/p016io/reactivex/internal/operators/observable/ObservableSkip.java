package p016io.reactivex.internal.operators.observable;

import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSkip */
public final class ObservableSkip<T> extends AbstractObservableWithUpstream<T, T> {

    /* renamed from: n */
    final long f466n;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSkip$SkipObserver */
    static final class SkipObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;

        /* renamed from: d */
        Disposable f467d;
        long remaining;

        SkipObserver(Observer<? super T> observer, long j) {
            this.actual = observer;
            this.remaining = j;
        }

        public void onSubscribe(Disposable disposable) {
            this.f467d = disposable;
            this.actual.onSubscribe(this);
        }

        public void onNext(T t) {
            if (this.remaining != 0) {
                this.remaining--;
            } else {
                this.actual.onNext(t);
            }
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void dispose() {
            this.f467d.dispose();
        }

        public boolean isDisposed() {
            return this.f467d.isDisposed();
        }
    }

    public ObservableSkip(ObservableSource<T> observableSource, long j) {
        super(observableSource);
        this.f466n = j;
    }

    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new SkipObserver(observer, this.f466n));
    }
}
