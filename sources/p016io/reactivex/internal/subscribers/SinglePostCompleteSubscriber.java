package p016io.reactivex.internal.subscribers;

import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.subscribers.SinglePostCompleteSubscriber */
public abstract class SinglePostCompleteSubscriber<T, R> extends AtomicLong implements Subscriber<T>, Subscription {
    static final long COMPLETE_MASK = Long.MIN_VALUE;
    static final long REQUEST_MASK = Long.MAX_VALUE;
    private static final long serialVersionUID = 7917814472626990048L;
    protected final Subscriber<? super R> actual;
    protected long produced;

    /* renamed from: s */
    protected Subscription f549s;
    protected R value;

    /* access modifiers changed from: protected */
    public void onDrop(R r) {
    }

    public SinglePostCompleteSubscriber(Subscriber<? super R> subscriber) {
        this.actual = subscriber;
    }

    public void onSubscribe(Subscription subscription) {
        if (SubscriptionHelper.validate(this.f549s, subscription)) {
            this.f549s = subscription;
            this.actual.onSubscribe(this);
        }
    }

    /* access modifiers changed from: protected */
    public final void complete(R r) {
        long j = this.produced;
        if (j != 0) {
            BackpressureHelper.produced(this, j);
        }
        while (true) {
            long j2 = get();
            if ((j2 & Long.MIN_VALUE) != 0) {
                onDrop(r);
                return;
            } else if ((j2 & Long.MAX_VALUE) != 0) {
                lazySet(-9223372036854775807L);
                this.actual.onNext(r);
                this.actual.onComplete();
                return;
            } else {
                this.value = r;
                if (!compareAndSet(0, Long.MIN_VALUE)) {
                    this.value = null;
                } else {
                    return;
                }
            }
        }
    }

    public final void request(long j) {
        long j2;
        if (SubscriptionHelper.validate(j)) {
            do {
                j2 = get();
                if ((j2 & Long.MIN_VALUE) != 0) {
                    if (compareAndSet(Long.MIN_VALUE, -9223372036854775807L)) {
                        this.actual.onNext(this.value);
                        this.actual.onComplete();
                        return;
                    }
                    return;
                }
            } while (!compareAndSet(j2, BackpressureHelper.addCap(j2, j)));
            this.f549s.request(j);
        }
    }

    public void cancel() {
        this.f549s.cancel();
    }
}
