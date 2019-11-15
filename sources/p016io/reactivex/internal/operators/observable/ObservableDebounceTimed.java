package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.observers.SerializedObserver;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableDebounceTimed */
public final class ObservableDebounceTimed<T> extends AbstractObservableWithUpstream<T, T> {
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDebounceTimed$DebounceEmitter */
    static final class DebounceEmitter<T> extends AtomicReference<Disposable> implements Runnable, Disposable {
        private static final long serialVersionUID = 6812032969491025141L;
        final long idx;
        final AtomicBoolean once = new AtomicBoolean();
        final DebounceTimedObserver<T> parent;
        final T value;

        DebounceEmitter(T t, long j, DebounceTimedObserver<T> debounceTimedObserver) {
            this.value = t;
            this.idx = j;
            this.parent = debounceTimedObserver;
        }

        public void run() {
            if (this.once.compareAndSet(false, true)) {
                this.parent.emit(this.idx, this.value, this);
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return get() == DisposableHelper.DISPOSED;
        }

        public void setResource(Disposable disposable) {
            DisposableHelper.replace(this, disposable);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableDebounceTimed$DebounceTimedObserver */
    static final class DebounceTimedObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;
        boolean done;
        volatile long index;

        /* renamed from: s */
        Disposable f419s;
        final long timeout;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final TimeUnit unit;
        final Worker worker;

        DebounceTimedObserver(Observer<? super T> observer, long j, TimeUnit timeUnit, Worker worker2) {
            this.actual = observer;
            this.timeout = j;
            this.unit = timeUnit;
            this.worker = worker2;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f419s, disposable)) {
                this.f419s = disposable;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.index + 1;
                this.index = j;
                Disposable disposable = (Disposable) this.timer.get();
                if (disposable != null) {
                    disposable.dispose();
                }
                DebounceEmitter debounceEmitter = new DebounceEmitter(t, j, this);
                if (this.timer.compareAndSet(disposable, debounceEmitter)) {
                    debounceEmitter.setResource(this.worker.schedule(debounceEmitter, this.timeout, this.unit));
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            DisposableHelper.dispose(this.timer);
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Disposable disposable = (Disposable) this.timer.get();
                if (disposable != DisposableHelper.DISPOSED) {
                    DebounceEmitter debounceEmitter = (DebounceEmitter) disposable;
                    if (debounceEmitter != null) {
                        debounceEmitter.run();
                    }
                    DisposableHelper.dispose(this.timer);
                    this.worker.dispose();
                    this.actual.onComplete();
                }
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this.timer);
            this.worker.dispose();
            this.f419s.dispose();
        }

        public boolean isDisposed() {
            return this.timer.get() == DisposableHelper.DISPOSED;
        }

        /* access modifiers changed from: 0000 */
        public void emit(long j, T t, DebounceEmitter<T> debounceEmitter) {
            if (j == this.index) {
                this.actual.onNext(t);
                debounceEmitter.dispose();
            }
        }
    }

    public ObservableDebounceTimed(ObservableSource<T> observableSource, long j, TimeUnit timeUnit, Scheduler scheduler2) {
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
