package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeUnit;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.observers.SerializedObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableDelay */
public final class ObservableDelay<T> extends AbstractObservableWithUpstream<T, T> {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDelay$DelayObserver */
    static final class DelayObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;
        final long delay;
        final boolean delayError;

        /* renamed from: s */
        Disposable f420s;
        final TimeUnit unit;

        /* renamed from: w */
        final Worker f421w;

        DelayObserver(Observer<? super T> observer, long j, TimeUnit timeUnit, Worker worker, boolean z) {
            this.actual = observer;
            this.delay = j;
            this.unit = timeUnit;
            this.f421w = worker;
            this.delayError = z;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f420s, disposable)) {
                this.f420s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(final T t) {
            this.f421w.schedule(new Runnable() {
                public void run() {
                    DelayObserver.this.actual.onNext(t);
                }
            }, this.delay, this.unit);
        }

        public void onError(final Throwable th) {
            this.f421w.schedule(new Runnable() {
                public void run() {
                    try {
                        DelayObserver.this.actual.onError(th);
                    } finally {
                        DelayObserver.this.f421w.dispose();
                    }
                }
            }, this.delayError ? this.delay : 0, this.unit);
        }

        public void onComplete() {
            this.f421w.schedule(new Runnable() {
                public void run() {
                    try {
                        DelayObserver.this.actual.onComplete();
                    } finally {
                        DelayObserver.this.f421w.dispose();
                    }
                }
            }, this.delay, this.unit);
        }

        public void dispose() {
            this.f421w.dispose();
            this.f420s.dispose();
        }

        public boolean isDisposed() {
            return this.f421w.isDisposed();
        }
    }

    public ObservableDelay(ObservableSource<T> observableSource, long j, TimeUnit timeUnit, Scheduler scheduler2, boolean z) {
        super(observableSource);
        this.delay = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.delayError = z;
    }

    public void subscribeActual(Observer<? super T> observer) {
        Observer<? super T> observer2;
        if (this.delayError) {
            observer2 = observer;
        } else {
            observer2 = new SerializedObserver<>(observer);
        }
        Worker createWorker = this.scheduler.createWorker();
        ObservableSource observableSource = this.source;
        DelayObserver delayObserver = new DelayObserver(observer2, this.delay, this.unit, createWorker, this.delayError);
        observableSource.subscribe(delayObserver);
    }
}
