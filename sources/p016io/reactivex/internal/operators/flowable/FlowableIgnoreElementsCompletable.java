package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.Flowable;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.fuseable.FuseToFlowable;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableIgnoreElementsCompletable */
public final class FlowableIgnoreElementsCompletable<T> extends Completable implements FuseToFlowable<T> {
    final Publisher<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableIgnoreElementsCompletable$IgnoreElementsSubscriber */
    static final class IgnoreElementsSubscriber<T> implements Subscriber<T>, Disposable {
        final CompletableObserver actual;

        /* renamed from: s */
        Subscription f291s;

        public void onNext(T t) {
        }

        IgnoreElementsSubscriber(CompletableObserver completableObserver) {
            this.actual = completableObserver;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f291s, subscription)) {
                this.f291s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onError(Throwable th) {
            this.f291s = SubscriptionHelper.CANCELLED;
            this.actual.onError(th);
        }

        public void onComplete() {
            this.f291s = SubscriptionHelper.CANCELLED;
            this.actual.onComplete();
        }

        public void dispose() {
            this.f291s.cancel();
            this.f291s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f291s == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableIgnoreElementsCompletable(Publisher<T> publisher) {
        this.source = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.source.subscribe(new IgnoreElementsSubscriber(completableObserver));
    }

    public Flowable<T> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableIgnoreElements<T>(this.source));
    }
}
