package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import p016io.reactivex.Observable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.BiPredicate;
import p016io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableRetryBiPredicate */
public final class ObservableRetryBiPredicate<T> extends AbstractObservableWithUpstream<T, T> {
    final BiPredicate<? super Integer, ? super Throwable> predicate;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableRetryBiPredicate$RetryBiObserver */
    static final class RetryBiObserver<T> extends AtomicInteger implements Observer<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Observer<? super T> actual;
        final BiPredicate<? super Integer, ? super Throwable> predicate;
        int retries;

        /* renamed from: sa */
        final SequentialDisposable f454sa;
        final ObservableSource<? extends T> source;

        RetryBiObserver(Observer<? super T> observer, BiPredicate<? super Integer, ? super Throwable> biPredicate, SequentialDisposable sequentialDisposable, ObservableSource<? extends T> observableSource) {
            this.actual = observer;
            this.f454sa = sequentialDisposable;
            this.source = observableSource;
            this.predicate = biPredicate;
        }

        public void onSubscribe(Disposable disposable) {
            this.f454sa.update(disposable);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable th) {
            try {
                BiPredicate<? super Integer, ? super Throwable> biPredicate = this.predicate;
                int i = this.retries + 1;
                this.retries = i;
                if (!biPredicate.test(Integer.valueOf(i), th)) {
                    this.actual.onError(th);
                } else {
                    subscribeNext();
                }
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.actual.onError(new CompositeException(th, th2));
            }
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int i = 1;
                while (!this.f454sa.isDisposed()) {
                    this.source.subscribe(this);
                    i = addAndGet(-i);
                    if (i == 0) {
                    }
                }
            }
        }
    }

    public ObservableRetryBiPredicate(Observable<T> observable, BiPredicate<? super Integer, ? super Throwable> biPredicate) {
        super(observable);
        this.predicate = biPredicate;
    }

    public void subscribeActual(Observer<? super T> observer) {
        SequentialDisposable sequentialDisposable = new SequentialDisposable();
        observer.onSubscribe(sequentialDisposable);
        new RetryBiObserver(observer, this.predicate, sequentialDisposable, this.source).subscribeNext();
    }
}
