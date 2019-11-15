package p016io.reactivex.internal.operators.flowable;

import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Flowable;
import p016io.reactivex.Maybe;
import p016io.reactivex.MaybeObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.BiFunction;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.FuseToFlowable;
import p016io.reactivex.internal.fuseable.HasUpstreamPublisher;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableReduceMaybe */
public final class FlowableReduceMaybe<T> extends Maybe<T> implements HasUpstreamPublisher<T>, FuseToFlowable<T> {
    final BiFunction<T, T, T> reducer;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReduceMaybe$ReduceSubscriber */
    static final class ReduceSubscriber<T> implements Subscriber<T>, Disposable {
        final MaybeObserver<? super T> actual;
        boolean done;
        final BiFunction<T, T, T> reducer;

        /* renamed from: s */
        Subscription f305s;
        T value;

        ReduceSubscriber(MaybeObserver<? super T> maybeObserver, BiFunction<T, T, T> biFunction) {
            this.actual = maybeObserver;
            this.reducer = biFunction;
        }

        public void dispose() {
            this.f305s.cancel();
            this.done = true;
        }

        public boolean isDisposed() {
            return this.done;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f305s, subscription)) {
                this.f305s = subscription;
                this.actual.onSubscribe(this);
                subscription.request(LongCompanionObject.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                T t2 = this.value;
                if (t2 == null) {
                    this.value = t;
                } else {
                    try {
                        this.value = ObjectHelper.requireNonNull(this.reducer.apply(t2, t), "The reducer returned a null value");
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.f305s.cancel();
                        onError(th);
                    }
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
                T t = this.value;
                if (t != null) {
                    this.actual.onSuccess(t);
                } else {
                    this.actual.onComplete();
                }
            }
        }
    }

    public FlowableReduceMaybe(Flowable<T> flowable, BiFunction<T, T, T> biFunction) {
        this.source = flowable;
        this.reducer = biFunction;
    }

    public Publisher<T> source() {
        return this.source;
    }

    public Flowable<T> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableReduce<T>(this.source, this.reducer));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> maybeObserver) {
        this.source.subscribe((Subscriber<? super T>) new ReduceSubscriber<Object>(maybeObserver, this.reducer));
    }
}
