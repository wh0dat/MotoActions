package p016io.reactivex.internal.operators.flowable;

import java.util.ArrayDeque;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSkipLast */
public final class FlowableSkipLast<T> extends AbstractFlowableWithUpstream<T, T> {
    final int skip;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSkipLast$SkipLastSubscriber */
    static final class SkipLastSubscriber<T> extends ArrayDeque<T> implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = -3807491841935125653L;
        final Subscriber<? super T> actual;

        /* renamed from: s */
        Subscription f322s;
        final int skip;

        SkipLastSubscriber(Subscriber<? super T> subscriber, int i) {
            super(i);
            this.actual = subscriber;
            this.skip = i;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f322s, subscription)) {
                this.f322s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (this.skip == size()) {
                this.actual.onNext(poll());
            } else {
                this.f322s.request(1);
            }
            offer(t);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void request(long j) {
            this.f322s.request(j);
        }

        public void cancel() {
            this.f322s.cancel();
        }
    }

    public FlowableSkipLast(Publisher<T> publisher, int i) {
        super(publisher);
        this.skip = i;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new SkipLastSubscriber(subscriber, this.skip));
    }
}
