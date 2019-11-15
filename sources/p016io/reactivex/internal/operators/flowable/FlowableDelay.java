package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableDelay */
public final class FlowableDelay<T> extends AbstractFlowableWithUpstream<T, T> {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDelay$DelaySubscriber */
    static final class DelaySubscriber<T> implements Subscriber<T>, Subscription {
        final Subscriber<? super T> actual;
        final long delay;
        final boolean delayError;

        /* renamed from: s */
        Subscription f266s;
        final TimeUnit unit;

        /* renamed from: w */
        final Worker f267w;

        DelaySubscriber(Subscriber<? super T> subscriber, long j, TimeUnit timeUnit, Worker worker, boolean z) {
            this.actual = subscriber;
            this.delay = j;
            this.unit = timeUnit;
            this.f267w = worker;
            this.delayError = z;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f266s, subscription)) {
                this.f266s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(final T t) {
            this.f267w.schedule(new Runnable() {
                public void run() {
                    DelaySubscriber.this.actual.onNext(t);
                }
            }, this.delay, this.unit);
        }

        public void onError(final Throwable th) {
            this.f267w.schedule(new Runnable() {
                public void run() {
                    try {
                        DelaySubscriber.this.actual.onError(th);
                    } finally {
                        DelaySubscriber.this.f267w.dispose();
                    }
                }
            }, this.delayError ? this.delay : 0, this.unit);
        }

        public void onComplete() {
            this.f267w.schedule(new Runnable() {
                public void run() {
                    try {
                        DelaySubscriber.this.actual.onComplete();
                    } finally {
                        DelaySubscriber.this.f267w.dispose();
                    }
                }
            }, this.delay, this.unit);
        }

        public void request(long j) {
            this.f266s.request(j);
        }

        public void cancel() {
            this.f267w.dispose();
            this.f266s.cancel();
        }
    }

    public FlowableDelay(Publisher<T> publisher, long j, TimeUnit timeUnit, Scheduler scheduler2, boolean z) {
        super(publisher);
        this.delay = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.delayError = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        Subscriber<? super T> subscriber2;
        if (this.delayError) {
            subscriber2 = subscriber;
        } else {
            subscriber2 = new SerializedSubscriber<>(subscriber);
        }
        Worker createWorker = this.scheduler.createWorker();
        Publisher publisher = this.source;
        DelaySubscriber delaySubscriber = new DelaySubscriber(subscriber2, this.delay, this.unit, createWorker, this.delayError);
        publisher.subscribe(delaySubscriber);
    }
}
