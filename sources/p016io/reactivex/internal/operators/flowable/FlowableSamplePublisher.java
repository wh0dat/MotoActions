package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Flowable;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSamplePublisher */
public final class FlowableSamplePublisher<T> extends Flowable<T> {
    final Publisher<?> other;
    final Publisher<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSamplePublisher$SamplePublisherSubscriber */
    static final class SamplePublisherSubscriber<T> extends AtomicReference<T> implements Subscriber<T>, Subscription {
        private static final long serialVersionUID = -3517602651313910099L;
        final Subscriber<? super T> actual;
        final AtomicReference<Subscription> other = new AtomicReference<>();
        final AtomicLong requested = new AtomicLong();

        /* renamed from: s */
        Subscription f310s;
        final Publisher<?> sampler;

        SamplePublisherSubscriber(Subscriber<? super T> subscriber, Publisher<?> publisher) {
            this.actual = subscriber;
            this.sampler = publisher;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f310s, subscription)) {
                this.f310s = subscription;
                this.actual.onSubscribe(this);
                if (this.other.get() == null) {
                    this.sampler.subscribe(new SamplerSubscriber(this));
                    subscription.request(LongCompanionObject.MAX_VALUE);
                }
            }
        }

        public void onNext(T t) {
            lazySet(t);
        }

        public void onError(Throwable th) {
            SubscriptionHelper.cancel(this.other);
            this.actual.onError(th);
        }

        public void onComplete() {
            SubscriptionHelper.cancel(this.other);
            this.actual.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public boolean setOther(Subscription subscription) {
            return SubscriptionHelper.setOnce(this.other, subscription);
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
            }
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.other);
            this.f310s.cancel();
        }

        public void error(Throwable th) {
            cancel();
            this.actual.onError(th);
        }

        public void complete() {
            cancel();
            this.actual.onComplete();
        }

        public void emit() {
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

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSamplePublisher$SamplerSubscriber */
    static final class SamplerSubscriber<T> implements Subscriber<Object> {
        final SamplePublisherSubscriber<T> parent;

        SamplerSubscriber(SamplePublisherSubscriber<T> samplePublisherSubscriber) {
            this.parent = samplePublisherSubscriber;
        }

        public void onSubscribe(Subscription subscription) {
            if (this.parent.setOther(subscription)) {
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(Object obj) {
            this.parent.emit();
        }

        public void onError(Throwable th) {
            this.parent.error(th);
        }

        public void onComplete() {
            this.parent.complete();
        }
    }

    public FlowableSamplePublisher(Publisher<T> publisher, Publisher<?> publisher2) {
        this.source = publisher;
        this.other = publisher2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new SamplePublisherSubscriber(new SerializedSubscriber(subscriber), this.other));
    }
}
