package p016io.reactivex.internal.operators.flowable;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableTakeLast */
public final class FlowableTakeLast<T> extends AbstractFlowableWithUpstream<T, T> {
    final int count;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableTakeLast$TakeLastSubscriber */
    static final class TakeLastSubscriber<T> extends ArrayDeque<T> implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = 7240042530241604978L;
        final Subscriber<? super T> actual;
        volatile boolean cancelled;
        final int count;
        volatile boolean done;
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f328s;
        final AtomicInteger wip = new AtomicInteger();

        TakeLastSubscriber(Subscriber<? super T> subscriber, int i) {
            this.actual = subscriber;
            this.count = i;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f328s, subscription)) {
                this.f328s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (this.count == size()) {
                poll();
            }
            offer(t);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }

        public void cancel() {
            this.cancelled = true;
            this.f328s.cancel();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (this.wip.getAndIncrement() == 0) {
                Subscriber<? super T> subscriber = this.actual;
                long j = this.requested.get();
                while (!this.cancelled) {
                    if (this.done) {
                        long j2 = 0;
                        while (j2 != j) {
                            if (!this.cancelled) {
                                Object poll = poll();
                                if (poll == null) {
                                    subscriber.onComplete();
                                    return;
                                } else {
                                    subscriber.onNext(poll);
                                    j2++;
                                }
                            } else {
                                return;
                            }
                        }
                        if (!(j2 == 0 || j == LongCompanionObject.MAX_VALUE)) {
                            j = this.requested.addAndGet(-j2);
                        }
                    }
                    if (this.wip.decrementAndGet() == 0) {
                    }
                }
            }
        }
    }

    public FlowableTakeLast(Publisher<T> publisher, int i) {
        super(publisher);
        this.count = i;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new TakeLastSubscriber(subscriber, this.count));
    }
}
