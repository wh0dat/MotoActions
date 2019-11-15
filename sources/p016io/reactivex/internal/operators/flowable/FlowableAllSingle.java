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

/* renamed from: io.reactivex.internal.operators.flowable.FlowableAllSingle */
public final class FlowableAllSingle<T> extends Single<Boolean> implements FuseToFlowable<Boolean> {
    final Predicate<? super T> predicate;
    final Publisher<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableAllSingle$AllSubscriber */
    static final class AllSubscriber<T> implements Subscriber<T>, Disposable {
        final SingleObserver<? super Boolean> actual;
        boolean done;
        final Predicate<? super T> predicate;

        /* renamed from: s */
        Subscription f242s;

        AllSubscriber(SingleObserver<? super Boolean> singleObserver, Predicate<? super T> predicate2) {
            this.actual = singleObserver;
            this.predicate = predicate2;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f242s, subscription)) {
                this.f242s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    if (!this.predicate.test(t)) {
                        this.done = true;
                        this.f242s.cancel();
                        this.f242s = SubscriptionHelper.CANCELLED;
                        this.actual.onSuccess(Boolean.valueOf(false));
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.f242s.cancel();
                    this.f242s = SubscriptionHelper.CANCELLED;
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
            this.f242s = SubscriptionHelper.CANCELLED;
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.f242s = SubscriptionHelper.CANCELLED;
                this.actual.onSuccess(Boolean.valueOf(true));
            }
        }

        public void dispose() {
            this.f242s.cancel();
            this.f242s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f242s == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableAllSingle(Publisher<T> publisher, Predicate<? super T> predicate2) {
        this.source = publisher;
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Boolean> singleObserver) {
        this.source.subscribe(new AllSubscriber(singleObserver, this.predicate));
    }

    public Flowable<Boolean> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableAll<T>(this.source, this.predicate));
    }
}
