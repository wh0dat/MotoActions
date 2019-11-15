package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.Scheduler;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.observers.SerializedObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSampleTimed */
public final class ObservableSampleTimed<T> extends AbstractObservableWithUpstream<T, T> {
    final long period;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSampleTimed$SampleTimedObserver */
    static final class SampleTimedObserver<T> extends AtomicReference<T> implements Observer<T>, Disposable, Runnable {
        private static final long serialVersionUID = -3517602651313910099L;
        final Observer<? super T> actual;
        final long period;

        /* renamed from: s */
        Disposable f456s;
        final Scheduler scheduler;
        final AtomicReference<Disposable> timer = new AtomicReference<>();
        final TimeUnit unit;

        SampleTimedObserver(Observer<? super T> observer, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            this.actual = observer;
            this.period = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.f456s, disposable)) {
                this.f456s = disposable;
                this.actual.onSubscribe(this);
                DisposableHelper.replace(this.timer, this.scheduler.schedulePeriodicallyDirect(this, this.period, this.period, this.unit));
            }
        }

        public void onNext(T t) {
            lazySet(t);
        }

        public void onError(Throwable th) {
            cancelTimer();
            this.actual.onError(th);
        }

        public void onComplete() {
            cancelTimer();
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void cancelTimer() {
            DisposableHelper.dispose(this.timer);
        }

        public void dispose() {
            cancelTimer();
            this.f456s.dispose();
        }

        public boolean isDisposed() {
            return this.f456s.isDisposed();
        }

        public void run() {
            Object andSet = getAndSet(null);
            if (andSet != null) {
                this.actual.onNext(andSet);
            }
        }
    }

    public ObservableSampleTimed(ObservableSource<T> observableSource, long j, TimeUnit timeUnit, Scheduler scheduler2) {
        super(observableSource);
        this.period = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    public void subscribeActual(Observer<? super T> observer) {
        SerializedObserver serializedObserver = new SerializedObserver(observer);
        ObservableSource observableSource = this.source;
        SampleTimedObserver sampleTimedObserver = new SampleTimedObserver(serializedObserver, this.period, this.unit, this.scheduler);
        observableSource.subscribe(sampleTimedObserver);
    }
}
