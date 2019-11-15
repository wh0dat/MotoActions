package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Flowable;
import p016io.reactivex.Scheduler;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableTimer */
public final class FlowableTimer extends Flowable<Long> {
    final long delay;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimer$IntervalOnceSubscriber */
    static final class IntervalOnceSubscriber extends AtomicReference<Disposable> implements Subscription, Runnable {
        private static final long serialVersionUID = -2809475196591179431L;
        final Subscriber<? super Long> actual;
        volatile boolean requested;

        IntervalOnceSubscriber(Subscriber<? super Long> subscriber) {
            this.actual = subscriber;
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                this.requested = true;
            }
        }

        public void cancel() {
            DisposableHelper.dispose(this);
        }

        public void run() {
            if (get() != DisposableHelper.DISPOSED) {
                if (this.requested) {
                    this.actual.onNext(Long.valueOf(0));
                    this.actual.onComplete();
                } else {
                    this.actual.onError(new MissingBackpressureException("Can't deliver value due to lack of requests"));
                }
                lazySet(EmptyDisposable.INSTANCE);
            }
        }

        public void setResource(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }
    }

    public FlowableTimer(long j, TimeUnit timeUnit, Scheduler scheduler2) {
        this.delay = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    public void subscribeActual(Subscriber<? super Long> subscriber) {
        IntervalOnceSubscriber intervalOnceSubscriber = new IntervalOnceSubscriber(subscriber);
        subscriber.onSubscribe(intervalOnceSubscriber);
        intervalOnceSubscriber.setResource(this.scheduler.scheduleDirect(intervalOnceSubscriber, this.delay, this.unit));
    }
}
