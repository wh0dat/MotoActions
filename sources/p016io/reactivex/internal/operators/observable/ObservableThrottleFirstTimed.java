package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.observers.SerializedObserver;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableThrottleFirstTimed */
public final class ObservableThrottleFirstTimed<T> extends AbstractObservableWithUpstream<T, T> {
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableThrottleFirstTimed$DebounceTimedObserver */
    static final class DebounceTimedObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable, Runnable {
        private static final long serialVersionUID = 786994795061867455L;
        final Observer<? super T> actual;
        boolean done;
        volatile boolean gate;

        /* renamed from: s */
        Disposable f481s;
        final long timeout;
        final TimeUnit unit;
        final Worker worker;

        DebounceTimedObserver(Observer<? super T> observer, long j, TimeUnit timeUnit, Worker worker2) {
            this.actual = observer;
            this.timeout = j;
            this.unit = timeUnit;
            this.worker = worker2;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f481s, disposable)) {
                this.f481s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.gate && !this.done) {
                this.gate = true;
                this.actual.onNext(t);
                Disposable disposable = (Disposable) get();
                if (disposable != null) {
                    disposable.dispose();
                }
                DisposableHelper.replace(this, this.worker.schedule(this, this.timeout, this.unit));
            }
        }

        public void run() {
            this.gate = false;
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            DisposableHelper.dispose(this);
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                DisposableHelper.dispose(this);
                this.worker.dispose();
                this.actual.onComplete();
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this);
            this.worker.dispose();
            this.f481s.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    public ObservableThrottleFirstTimed(ObservableSource<T> observableSource, long j, TimeUnit timeUnit, Scheduler scheduler2) {
        super(observableSource);
        this.timeout = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    public void subscribeActual(Observer<? super T> observer) {
        ObservableSource observableSource = this.source;
        DebounceTimedObserver debounceTimedObserver = new DebounceTimedObserver(new SerializedObserver(observer), this.timeout, this.unit, this.scheduler.createWorker());
        observableSource.subscribe(debounceTimedObserver);
    }
}
