package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Flowable;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Predicate;
import p016io.reactivex.internal.fuseable.FuseToFlowable;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableAnySingle */
public final class FlowableAnySingle<T> extends Single<Boolean> implements FuseToFlowable<Boolean> {
    final Predicate<? super T> predicate;
    final Publisher<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableAnySingle$AnySubscriber */
    static final class AnySubscriber<T> implements Subscriber<T>, Disposable {
        final SingleObserver<? super Boolean> actual;
        boolean done;
        final Predicate<? super T> predicate;

        /* renamed from: s */
        Subscription f244s;

        AnySubscriber(SingleObserver<? super Boolean> singleObserver, Predicate<? super T> predicate2) {
            this.actual = singleObserver;
            this.predicate = predicate2;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f244s, subscription)) {
                this.f244s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    if (this.predicate.test(t)) {
                        this.done = true;
                        this.f244s.cancel();
                        this.f244s = SubscriptionHelper.CANCELLED;
                        this.actual.onSuccess(Boolean.valueOf(true));
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.f244s.cancel();
                    this.f244s = SubscriptionHelper.CANCELLED;
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
            this.f244s = SubscriptionHelper.CANCELLED;
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.f244s = SubscriptionHelper.CANCELLED;
                this.actual.onSuccess(Boolean.valueOf(false));
            }
        }

        public void dispose() {
            this.f244s.cancel();
            this.f244s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f244s == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableAnySingle(Publisher<T> publisher, Predicate<? super T> predicate2) {
        this.source = publisher;
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Boolean> singleObserver) {
        this.source.subscribe(new AnySubscriber(singleObserver, this.predicate));
    }

    public Flowable<Boolean> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableAny<T>(this.source, this.predicate));
    }
}
