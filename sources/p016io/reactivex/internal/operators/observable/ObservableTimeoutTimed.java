package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.ObserverFullArbiter;
import p016io.reactivex.internal.observers.FullArbiterObserver;
import p016io.reactivex.observers.SerializedObserver;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableTimeoutTimed */
public final class ObservableTimeoutTimed<T> extends AbstractObservableWithUpstream<T, T> {
    static final Disposable NEW_TIMER = new Disposable() {
        public void dispose() {
        }

        public boolean isDisposed() {
            return true;
        }
    };
    final ObservableSource<? extends T> other;
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTimeoutTimed$TimeoutTimedObserver */
    static final class TimeoutTimedObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
        private static final long serialVersionUID = -8387234228317808253L;
        final Observer<? super T> actual;
        volatile boolean done;
        volatile long index;

        /* renamed from: s */
        Disposable f485s;
        final long timeout;
        final TimeUnit unit;
        final Worker worker;

        TimeoutTimedObserver(Observer<? super T> observer, long j, TimeUnit timeUnit, Worker worker2) {
            this.actual = observer;
            this.timeout = j;
            this.unit = timeUnit;
            this.worker = worker2;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f485s, disposable)) {
                this.f485s = disposable;
                this.actual.onSubscribe(this);
                scheduleTimeout(0);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.index + 1;
                this.index = j;
                this.actual.onNext(t);
                scheduleTimeout(j);
            }
        }

        /* access modifiers changed from: 0000 */
        public void scheduleTimeout(final long j) {
            Disposable disposable = (Disposable) get();
            if (disposable != null) {
                disposable.dispose();
            }
            if (compareAndSet(disposable, ObservableTimeoutTimed.NEW_TIMER)) {
                DisposableHelper.replace(this, this.worker.schedule(new Runnable() {
                    public void run() {
                        if (j == TimeoutTimedObserver.this.index) {
                            TimeoutTimedObserver.this.done = true;
                            DisposableHelper.dispose(TimeoutTimedObserver.this);
                            TimeoutTimedObserver.this.f485s.dispose();
                            TimeoutTimedObserver.this.actual.onError(new TimeoutException());
                            TimeoutTimedObserver.this.worker.dispose();
                        }
                    }
                }, this.timeout, this.unit));
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            dispose();
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                dispose();
                this.actual.onComplete();
            }
        }

        public void dispose() {
            this.worker.dispose();
            DisposableHelper.dispose(this);
            this.f485s.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTimeoutTimed$TimeoutTimedOtherObserver */
    static final class TimeoutTimedOtherObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
        private static final long serialVersionUID = -4619702551964128179L;
        final Observer<? super T> actual;
        final ObserverFullArbiter<T> arbiter;
        volatile boolean done;
        volatile long index;
        final ObservableSource<? extends T> other;

        /* renamed from: s */
        Disposable f486s;
        final long timeout;
        final TimeUnit unit;
        final Worker worker;

        TimeoutTimedOtherObserver(Observer<? super T> observer, long j, TimeUnit timeUnit, Worker worker2, ObservableSource<? extends T> observableSource) {
            this.actual = observer;
            this.timeout = j;
            this.unit = timeUnit;
            this.worker = worker2;
            this.other = observableSource;
            this.arbiter = new ObserverFullArbiter<>(observer, this, 8);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f486s, disposable)) {
                this.f486s = disposable;
                if (this.arbiter.setDisposable(disposable)) {
                    this.actual.onSubscribe(this.arbiter);
                    scheduleTimeout(0);
                }
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.index + 1;
                this.index = j;
                if (this.arbiter.onNext(t, this.f486s)) {
                    scheduleTimeout(j);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void scheduleTimeout(final long j) {
            Disposable disposable = (Disposable) get();
            if (disposable != null) {
                disposable.dispose();
            }
            if (compareAndSet(disposable, ObservableTimeoutTimed.NEW_TIMER)) {
                DisposableHelper.replace(this, this.worker.schedule(new Runnable() {
                    public void run() {
                        if (j == TimeoutTimedOtherObserver.this.index) {
                            TimeoutTimedOtherObserver.this.done = true;
                            TimeoutTimedOtherObserver.this.f486s.dispose();
                            DisposableHelper.dispose(TimeoutTimedOtherObserver.this);
                            TimeoutTimedOtherObserver.this.subscribeNext();
                            TimeoutTimedOtherObserver.this.worker.dispose();
                        }
                    }
                }, this.timeout, this.unit));
            }
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            this.other.subscribe(new FullArbiterObserver(this.arbiter));
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.worker.dispose();
            DisposableHelper.dispose(this);
            this.arbiter.onError(th, this.f486s);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.worker.dispose();
                DisposableHelper.dispose(this);
                this.arbiter.onComplete(this.f486s);
            }
        }

        public void dispose() {
            this.worker.dispose();
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    public ObservableTimeoutTimed(ObservableSource<T> observableSource, long j, TimeUnit timeUnit, Scheduler scheduler2, ObservableSource<? extends T> observableSource2) {
        super(observableSource);
        this.timeout = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.other = observableSource2;
    }

    public void subscribeActual(Observer<? super T> observer) {
        if (this.other == null) {
            ObservableSource observableSource = this.source;
            TimeoutTimedObserver timeoutTimedObserver = new TimeoutTimedObserver(new SerializedObserver(observer), this.timeout, this.unit, this.scheduler.createWorker());
            observableSource.subscribe(timeoutTimedObserver);
            return;
        }
        ObservableSource observableSource2 = this.source;
        TimeoutTimedOtherObserver timeoutTimedOtherObserver = new TimeoutTimedOtherObserver(observer, this.timeout, this.unit, this.scheduler.createWorker(), this.other);
        observableSource2.subscribe(timeoutTimedOtherObserver);
    }
}
