package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Observable;
import p016io.reactivex.Observer;
import p016io.reactivex.Scheduler;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.EmptyDisposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableTimer */
public final class ObservableTimer extends Observable<Long> {
    final long delay;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTimer$IntervalOnceObserver */
    static final class IntervalOnceObserver extends AtomicReference<Disposable> implements Disposable, Runnable {
        private static final long serialVersionUID = -2809475196591179431L;
        final Observer<? super Long> actual;

        IntervalOnceObserver(Observer<? super Long> observer) {
            this.actual = observer;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return get() == DisposableHelper.DISPOSED;
        }

        public void run() {
            if (!isDisposed()) {
                this.actual.onNext(Long.valueOf(0));
                this.actual.onComplete();
                lazySet(EmptyDisposable.INSTANCE);
            }
        }

        public void setResource(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }
    }

    public ObservableTimer(long j, TimeUnit timeUnit, Scheduler scheduler2) {
        this.delay = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    public void subscribeActual(Observer<? super Long> observer) {
        IntervalOnceObserver intervalOnceObserver = new IntervalOnceObserver(observer);
        observer.onSubscribe(intervalOnceObserver);
        intervalOnceObserver.setResource(this.scheduler.scheduleDirect(intervalOnceObserver, this.delay, this.unit));
    }
}
