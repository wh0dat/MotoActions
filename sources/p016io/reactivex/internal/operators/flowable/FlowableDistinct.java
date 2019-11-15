package p016io.reactivex.internal.operators.flowable;

import java.util.Collection;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import p016io.reactivex.internal.subscriptions.EmptySubscription;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableDistinct */
public final class FlowableDistinct<T, K> extends AbstractFlowableWithUpstream<T, T> {
    final Callable<? extends Collection<? super K>> collectionSupplier;
    final Function<? super T, K> keySelector;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDistinct$DistinctSubscriber */
    static final class DistinctSubscriber<T, K> extends BasicFuseableSubscriber<T, T> {
        final Collection<? super K> collection;
        final Function<? super T, K> keySelector;

        DistinctSubscriber(Subscriber<? super T> subscriber, Function<? super T, K> function, Collection<? super K> collection2) {
            super(subscriber);
            this.keySelector = function;
            this.collection = collection2;
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.sourceMode == 0) {
                    try {
                        if (this.collection.add(ObjectHelper.requireNonNull(this.keySelector.apply(t), "The keySelector returned a null key"))) {
                            this.actual.onNext(t);
                        } else {
                            this.f526s.request(1);
                        }
                    } catch (Throwable th) {
                        fail(th);
                    }
                } else {
                    this.actual.onNext(null);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.collection.clear();
            this.actual.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.collection.clear();
                this.actual.onComplete();
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public T poll() throws Exception {
            T poll;
            while (true) {
                poll = this.f525qs.poll();
                if (poll == null || this.collection.add(ObjectHelper.requireNonNull(this.keySelector.apply(poll), "The keySelector returned a null key"))) {
                    return poll;
                }
                if (this.sourceMode == 2) {
                    this.f526s.request(1);
                }
            }
            return poll;
        }

        public void clear() {
            this.collection.clear();
            super.clear();
        }
    }

    public FlowableDistinct(Publisher<T> publisher, Function<? super T, K> function, Callable<? extends Collection<? super K>> callable) {
        super(publisher);
        this.keySelector = function;
        this.collectionSupplier = callable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        try {
            this.source.subscribe(new DistinctSubscriber(subscriber, this.keySelector, (Collection) ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.")));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptySubscription.error(th, subscriber);
        }
    }
}
