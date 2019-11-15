package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Flowable;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.fuseable.FuseToFlowable;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableCountSingle */
public final class FlowableCountSingle<T> extends Single<Long> implements FuseToFlowable<Long> {
    final Publisher<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableCountSingle$CountSubscriber */
    static final class CountSubscriber implements Subscriber<Object>, Disposable {
        final SingleObserver<? super Long> actual;
        long count;

        /* renamed from: s */
        Subscription f263s;

        CountSubscriber(SingleObserver<? super Long> singleObserver) {
            this.actual = singleObserver;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f263s, subscription)) {
                this.f263s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(Object obj) {
            this.count++;
        }

        public void onError(Throwable th) {
            this.f263s = SubscriptionHelper.CANCELLED;
            this.actual.onError(th);
        }

        public void onComplete() {
            this.f263s = SubscriptionHelper.CANCELLED;
            this.actual.onSuccess(Long.valueOf(this.count));
        }

        public void dispose() {
            this.f263s.cancel();
            this.f263s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f263s == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableCountSingle(Publisher<T> publisher) {
        this.source = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Long> singleObserver) {
        this.source.subscribe(new CountSubscriber(singleObserver));
    }

    public Flowable<Long> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCount<T>(this.source));
    }
}
