package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import p016io.reactivex.functions.BiPredicate;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.fuseable.ConditionalSubscriber;
import p016io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import p016io.reactivex.internal.subscribers.BasicFuseableSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableDistinctUntilChanged */
public final class FlowableDistinctUntilChanged<T, K> extends AbstractFlowableWithUpstream<T, T> {
    final BiPredicate<? super K, ? super K> comparer;
    final Function<? super T, K> keySelector;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDistinctUntilChanged$DistinctUntilChangedConditionalSubscriber */
    static final class DistinctUntilChangedConditionalSubscriber<T, K> extends BasicFuseableConditionalSubscriber<T, T> {
        final BiPredicate<? super K, ? super K> comparer;
        boolean hasValue;
        final Function<? super T, K> keySelector;
        K last;

        DistinctUntilChangedConditionalSubscriber(ConditionalSubscriber<? super T> conditionalSubscriber, Function<? super T, K> function, BiPredicate<? super K, ? super K> biPredicate) {
            super(conditionalSubscriber);
            this.keySelector = function;
            this.comparer = biPredicate;
        }

        public void onNext(T t) {
            if (!tryOnNext(t)) {
                this.f524s.request(1);
            }
        }

        public boolean tryOnNext(T t) {
            if (this.done) {
                return false;
            }
            if (this.sourceMode != 0) {
                return this.actual.tryOnNext(t);
            }
            try {
                K apply = this.keySelector.apply(t);
                if (this.hasValue) {
                    boolean test = this.comparer.test(this.last, apply);
                    this.last = apply;
                    if (test) {
                        return false;
                    }
                } else {
                    this.hasValue = true;
                    this.last = apply;
                }
                this.actual.onNext(t);
                return true;
            } catch (Throwable th) {
                fail(th);
                return true;
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public T poll() throws Exception {
            while (true) {
                T poll = this.f523qs.poll();
                if (poll == null) {
                    return null;
                }
                K apply = this.keySelector.apply(poll);
                if (!this.hasValue) {
                    this.hasValue = true;
                    this.last = apply;
                    return poll;
                } else if (!this.comparer.test(this.last, apply)) {
                    this.last = apply;
                    return poll;
                } else {
                    this.last = apply;
                    if (this.sourceMode != 1) {
                        this.f524s.request(1);
                    }
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDistinctUntilChanged$DistinctUntilChangedSubscriber */
    static final class DistinctUntilChangedSubscriber<T, K> extends BasicFuseableSubscriber<T, T> implements ConditionalSubscriber<T> {
        final BiPredicate<? super K, ? super K> comparer;
        boolean hasValue;
        final Function<? super T, K> keySelector;
        K last;

        DistinctUntilChangedSubscriber(Subscriber<? super T> subscriber, Function<? super T, K> function, BiPredicate<? super K, ? super K> biPredicate) {
            super(subscriber);
            this.keySelector = function;
            this.comparer = biPredicate;
        }

        public void onNext(T t) {
            if (!tryOnNext(t)) {
                this.f526s.request(1);
            }
        }

        public boolean tryOnNext(T t) {
            if (this.done) {
                return false;
            }
            if (this.sourceMode != 0) {
                this.actual.onNext(t);
                return true;
            }
            try {
                K apply = this.keySelector.apply(t);
                if (this.hasValue) {
                    boolean test = this.comparer.test(this.last, apply);
                    this.last = apply;
                    if (test) {
                        return false;
                    }
                } else {
                    this.hasValue = true;
                    this.last = apply;
                }
                this.actual.onNext(t);
                return true;
            } catch (Throwable th) {
                fail(th);
                return true;
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public T poll() throws Exception {
            while (true) {
                T poll = this.f525qs.poll();
                if (poll == null) {
                    return null;
                }
                K apply = this.keySelector.apply(poll);
                if (!this.hasValue) {
                    this.hasValue = true;
                    this.last = apply;
                    return poll;
                } else if (!this.comparer.test(this.last, apply)) {
                    this.last = apply;
                    return poll;
                } else {
                    this.last = apply;
                    if (this.sourceMode != 1) {
                        this.f526s.request(1);
                    }
                }
            }
        }
    }

    public FlowableDistinctUntilChanged(Publisher<T> publisher, Function<? super T, K> function, BiPredicate<? super K, ? super K> biPredicate) {
        super(publisher);
        this.keySelector = function;
        this.comparer = biPredicate;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        if (subscriber instanceof ConditionalSubscriber) {
            this.source.subscribe(new DistinctUntilChangedConditionalSubscriber((ConditionalSubscriber) subscriber, this.keySelector, this.comparer));
            return;
        }
        this.source.subscribe(new DistinctUntilChangedSubscriber(subscriber, this.keySelector, this.comparer));
    }
}
