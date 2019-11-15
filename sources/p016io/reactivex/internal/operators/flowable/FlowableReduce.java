package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.BiFunction;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableReduce */
public final class FlowableReduce<T> extends AbstractFlowableWithUpstream<T, T> {
    final BiFunction<T, T, T> reducer;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReduce$ReduceSubscriber */
    static final class ReduceSubscriber<T> extends DeferredScalarSubscription<T> implements Subscriber<T> {
        private static final long serialVersionUID = -4663883003264602070L;
        final BiFunction<T, T, T> reducer;

        /* renamed from: s */
        Subscription f304s;

        ReduceSubscriber(Subscriber<? super T> subscriber, BiFunction<T, T, T> biFunction) {
            super(subscriber);
            this.reducer = biFunction;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f304s, subscription)) {
                this.f304s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (this.f304s != SubscriptionHelper.CANCELLED) {
                Object obj = this.value;
                if (obj == null) {
                    this.value = t;
                } else {
                    try {
                        this.value = ObjectHelper.requireNonNull(this.reducer.apply(obj, t), "The reducer returned a null value");
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.f304s.cancel();
                        onError(th);
                    }
                }
            }
        }

        public void onError(Throwable th) {
            if (this.f304s == SubscriptionHelper.CANCELLED) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.f304s = SubscriptionHelper.CANCELLED;
            this.actual.onError(th);
        }

        public void onComplete() {
            if (this.f304s != SubscriptionHelper.CANCELLED) {
                this.f304s = SubscriptionHelper.CANCELLED;
                Object obj = this.value;
                if (obj != null) {
                    complete(obj);
                } else {
                    this.actual.onComplete();
                }
            }
        }

        public void cancel() {
            super.cancel();
            this.f304s.cancel();
            this.f304s = SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableReduce(Publisher<T> publisher, BiFunction<T, T, T> biFunction) {
        super(publisher);
        this.reducer = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new ReduceSubscriber(subscriber, this.reducer));
    }
}
