package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Predicate;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSkipWhile */
public final class FlowableSkipWhile<T> extends AbstractFlowableWithUpstream<T, T> {
    final Predicate<? super T> predicate;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSkipWhile$SkipWhileSubscriber */
    static final class SkipWhileSubscriber<T> implements Subscriber<T>, Subscription {
        final Subscriber<? super T> actual;
        boolean notSkipping;
        final Predicate<? super T> predicate;

        /* renamed from: s */
        Subscription f325s;

        SkipWhileSubscriber(Subscriber<? super T> subscriber, Predicate<? super T> predicate2) {
            this.actual = subscriber;
            this.predicate = predicate2;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f325s, subscription)) {
                this.f325s = subscription;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (this.notSkipping) {
                this.actual.onNext(t);
            } else {
                try {
                    if (this.predicate.test(t)) {
                        this.f325s.request(1);
                    } else {
                        this.notSkipping = true;
                        this.actual.onNext(t);
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.f325s.cancel();
                    this.actual.onError(th);
                }
            }
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void request(long j) {
            this.f325s.request(j);
        }

        public void cancel() {
            this.f325s.cancel();
        }
    }

    public FlowableSkipWhile(Publisher<T> publisher, Predicate<? super T> predicate2) {
        super(publisher);
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new SkipWhileSubscriber(subscriber, this.predicate));
    }
}
