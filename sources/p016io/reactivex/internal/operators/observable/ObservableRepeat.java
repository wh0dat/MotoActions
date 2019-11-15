package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import kotlin.jvm.internal.LongCompanionObject;
import p016io.reactivex.Observable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableRepeat */
public final class ObservableRepeat<T> extends AbstractObservableWithUpstream<T, T> {
    final long count;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableRepeat$RepeatObserver */
    static final class RepeatObserver<T> extends AtomicInteger implements Observer<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Observer<? super T> actual;
        long remaining;

        /* renamed from: sd */
        final SequentialDisposable f452sd;
        final ObservableSource<? extends T> source;

        RepeatObserver(Observer<? super T> observer, long j, SequentialDisposable sequentialDisposable, ObservableSource<? extends T> observableSource) {
            this.actual = observer;
            this.f452sd = sequentialDisposable;
            this.source = observableSource;
            this.remaining = j;
        }

        public void onSubscribe(Disposable disposable) {
            this.f452sd.replace(disposable);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            long j = this.remaining;
            if (j != LongCompanionObject.MAX_VALUE) {
                this.remaining = j - 1;
            }
            if (j != 0) {
                subscribeNext();
            } else {
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int i = 1;
                while (!this.f452sd.isDisposed()) {
                    this.source.subscribe(this);
                    i = addAndGet(-i);
                    if (i == 0) {
                    }
                }
            }
        }
    }

    public ObservableRepeat(Observable<T> observable, long j) {
        super(observable);
        this.count = j;
    }

    public void subscribeActual(Observer<? super T> observer) {
        SequentialDisposable sequentialDisposable = new SequentialDisposable();
        observer.onSubscribe(sequentialDisposable);
        long j = this.count;
        long j2 = LongCompanionObject.MAX_VALUE;
        if (j != LongCompanionObject.MAX_VALUE) {
            j2 = this.count - 1;
        }
        RepeatObserver repeatObserver = new RepeatObserver(observer, j2, sequentialDisposable, this.source);
        repeatObserver.subscribeNext();
    }
}
