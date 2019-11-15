package p016io.reactivex.internal.operators.observable;

import java.util.ArrayDeque;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableTakeLast */
public final class ObservableTakeLast<T> extends AbstractObservableWithUpstream<T, T> {
    final int count;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTakeLast$TakeLastObserver */
    static final class TakeLastObserver<T> extends ArrayDeque<T> implements Observer<T>, Disposable {
        private static final long serialVersionUID = 7240042530241604978L;
        final Observer<? super T> actual;
        volatile boolean cancelled;
        final int count;

        /* renamed from: s */
        Disposable f475s;

        TakeLastObserver(Observer<? super T> observer, int i) {
            this.actual = observer;
            this.count = i;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f475s, disposable)) {
                this.f475s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (this.count == size()) {
                poll();
            }
            offer(t);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            Observer<? super T> observer = this.actual;
            while (!this.cancelled) {
                Object poll = poll();
                if (poll == null) {
                    if (!this.cancelled) {
                        observer.onComplete();
                    }
                    return;
                }
                observer.onNext(poll);
            }
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.f475s.dispose();
            }
        }

        public boolean isDisposed() {
            return this.cancelled;
        }
    }

    public ObservableTakeLast(ObservableSource<T> observableSource, int i) {
        super(observableSource);
        this.count = i;
    }

    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new TakeLastObserver(observer, this.count));
    }
}
