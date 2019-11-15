package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Predicate;
import p016io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableAny */
public final class FlowableAny<T> extends AbstractFlowableWithUpstream<T, Boolean> {
    final Predicate<? super T> predicate;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableAny$AnySubscriber */
    static final class AnySubscriber<T> extends DeferredScalarSubscription<Boolean> implements Subscriber<T> {
        private static final long serialVersionUID = -2311252482644620661L;
        boolean done;
        final Predicate<? super T> predicate;

        /* renamed from: s */
        Subscription f243s;

        AnySubscriber(Subscriber<? super Boolean> subscriber, Predicate<? super T> predicate2) {
            super(subscriber);
            this.predicate = predicate2;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f243s, subscription)) {
                this.f243s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    if (this.predicate.test(t)) {
                        this.done = true;
                        this.f243s.cancel();
                        complete(Boolean.valueOf(true));
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.f243s.cancel();
                    onError(th);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                complete(Boolean.valueOf(false));
            }
        }

        public void cancel() {
            super.cancel();
            this.f243s.cancel();
        }
    }

    public FlowableAny(Publisher<T> publisher, Predicate<? super T> predicate2) {
        super(publisher);
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super Boolean> subscriber) {
        this.source.subscribe(new AnySubscriber(subscriber, this.predicate));
    }
}
