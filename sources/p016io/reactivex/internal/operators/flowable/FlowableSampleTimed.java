package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Scheduler;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.SequentialDisposable;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSampleTimed */
public final class FlowableSampleTimed<T> extends AbstractFlowableWithUpstream<T, T> {
    final long period;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSampleTimed$SampleTimedSubscriber */
    static final class SampleTimedSubscriber<T> extends AtomicReference<T> implements Subscriber<T>, Subscription, Runnable {
        private static final long serialVersionUID = -3517602651313910099L;
        final Subscriber<? super T> actual;
        final long period;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f311s;
        final Scheduler scheduler;
        final SequentialDisposable timer = new SequentialDisposable();
        final TimeUnit unit;

        SampleTimedSubscriber(Subscriber<? super T> subscriber, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            this.actual = subscriber;
            this.period = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f311s, subscription)) {
                this.f311s = subscription;
                this.actual.onSubscribe(this);
                this.timer.replace(this.scheduler.schedulePeriodicallyDirect(this, this.period, this.period, this.unit));
                subscription.request(LongCompanionObject.MAX_VALUE);
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

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
            }
        }

        public void cancel() {
            cancelTimer();
            this.f311s.cancel();
        }

        public void run() {
            Object andSet = getAndSet(null);
            if (andSet == null) {
                return;
            }
            if (this.requested.get() != 0) {
                this.actual.onNext(andSet);
                BackpressureHelper.produced(this.requested, 1);
                return;
            }
            cancel();
            this.actual.onError(new MissingBackpressureException("Couldn't emit value due to lack of requests!"));
        }
    }

    public FlowableSampleTimed(Publisher<T> publisher, long j, TimeUnit timeUnit, Scheduler scheduler2) {
        super(publisher);
        this.period = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        SerializedSubscriber serializedSubscriber = new SerializedSubscriber(subscriber);
        Publisher publisher = this.source;
        SampleTimedSubscriber sampleTimedSubscriber = new SampleTimedSubscriber(serializedSubscriber, this.period, this.unit, this.scheduler);
        publisher.subscribe(sampleTimedSubscriber);
    }
}
