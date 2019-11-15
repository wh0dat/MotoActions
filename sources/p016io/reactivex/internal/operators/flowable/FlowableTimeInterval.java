package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Scheduler;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.schedulers.Timed;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeInterval */
public final class FlowableTimeInterval<T> extends AbstractFlowableWithUpstream<T, Timed<T>> {
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTimeInterval$TimeIntervalSubscriber */
    static final class TimeIntervalSubscriber<T> implements Subscriber<T>, Subscription {
        final Subscriber<? super Timed<T>> actual;
        long lastTime;

        /* renamed from: s */
        Subscription f335s;
        final Scheduler scheduler;
        final TimeUnit unit;

        TimeIntervalSubscriber(Subscriber<? super Timed<T>> subscriber, TimeUnit timeUnit, Scheduler scheduler2) {
            this.actual = subscriber;
            this.scheduler = scheduler2;
            this.unit = timeUnit;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f335s, subscription)) {
                this.lastTime = this.scheduler.now(this.unit);
                this.f335s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            long now = this.scheduler.now(this.unit);
            long j = this.lastTime;
            this.lastTime = now;
            this.actual.onNext(new Timed(t, now - j, this.unit));
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void request(long j) {
            this.f335s.request(j);
        }

        public void cancel() {
            this.f335s.cancel();
        }
    }

    public FlowableTimeInterval(Publisher<T> publisher, TimeUnit timeUnit, Scheduler scheduler2) {
        super(publisher);
        this.scheduler = scheduler2;
        this.unit = timeUnit;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super Timed<T>> subscriber) {
        this.source.subscribe(new TimeIntervalSubscriber(subscriber, this.unit, this.scheduler));
    }
}
