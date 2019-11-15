package p016io.reactivex.internal.operators.observable;

import java.util.ArrayDeque;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSkipLast */
public final class ObservableSkipLast<T> extends AbstractObservableWithUpstream<T, T> {
    final int skip;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSkipLast$SkipLastObserver */
    static final class SkipLastObserver<T> extends ArrayDeque<T> implements Observer<T>, Disposable {
        private static final long serialVersionUID = -3807491841935125653L;
        final Observer<? super T> actual;

        /* renamed from: s */
        Disposable f468s;
        final int skip;

        SkipLastObserver(Observer<? super T> observer, int i) {
            super(i);
            this.actual = observer;
            this.skip = i;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f468s, disposable)) {
                this.f468s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.f468s.dispose();
        }

        public boolean isDisposed() {
            return this.f468s.isDisposed();
        }

        public void onNext(T t) {
            if (this.skip == size()) {
                this.actual.onNext(poll());
            }
            offer(t);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    public ObservableSkipLast(ObservableSource<T> observableSource, int i) {
        super(observableSource);
        this.skip = i;
    }

    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new SkipLastObserver(observer, this.skip));
    }
}
