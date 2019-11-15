package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Flowable;
import p016io.reactivex.Maybe;
import p016io.reactivex.MaybeObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.fuseable.FuseToFlowable;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSingleMaybe */
public final class FlowableSingleMaybe<T> extends Maybe<T> implements FuseToFlowable<T> {
    final Publisher<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSingleMaybe$SingleElementSubscriber */
    static final class SingleElementSubscriber<T> implements Subscriber<T>, Disposable {
        final MaybeObserver<? super T> actual;
        boolean done;

        /* renamed from: s */
        Subscription f318s;
        T value;

        SingleElementSubscriber(MaybeObserver<? super T> maybeObserver) {
            this.actual = maybeObserver;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f318s, subscription)) {
                this.f318s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.value != null) {
                    this.done = true;
                    this.f318s.cancel();
                    this.f318s = SubscriptionHelper.CANCELLED;
                    this.actual.onError(new IllegalArgumentException("Sequence contains more than one element!"));
                    return;
                }
                this.value = t;
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.f318s = SubscriptionHelper.CANCELLED;
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.f318s = SubscriptionHelper.CANCELLED;
                T t = this.value;
                this.value = null;
                if (t == null) {
                    this.actual.onComplete();
                } else {
                    this.actual.onSuccess(t);
                }
            }
        }

        public void dispose() {
            this.f318s.cancel();
            this.f318s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.f318s == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableSingleMaybe(Publisher<T> publisher) {
        this.source = publisher;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> maybeObserver) {
        this.source.subscribe(new SingleElementSubscriber(maybeObserver));
    }

    public Flowable<T> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSingle<T>(this.source, null));
    }
}
