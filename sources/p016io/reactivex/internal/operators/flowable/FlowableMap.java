package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.ConditionalSubscriber;
import p016io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import p016io.reactivex.internal.subscribers.BasicFuseableSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableMap */
public final class FlowableMap<T, U> extends AbstractFlowableWithUpstream<T, U> {
    final Function<? super T, ? extends U> mapper;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableMap$MapConditionalSubscriber */
    static final class MapConditionalSubscriber<T, U> extends BasicFuseableConditionalSubscriber<T, U> {
        final Function<? super T, ? extends U> mapper;

        MapConditionalSubscriber(ConditionalSubscriber<? super U> conditionalSubscriber, Function<? super T, ? extends U> function) {
            super(conditionalSubscriber);
            this.mapper = function;
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.sourceMode != 0) {
                    this.actual.onNext(null);
                    return;
                }
                try {
                    this.actual.onNext(ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper function returned a null value."));
                } catch (Throwable th) {
                    fail(th);
                }
            }
        }

        public boolean tryOnNext(T t) {
            if (this.done) {
                return false;
            }
            try {
                return this.actual.tryOnNext(ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper function returned a null value."));
            } catch (Throwable th) {
                fail(th);
                return true;
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public U poll() throws Exception {
            Object poll = this.f523qs.poll();
            if (poll != null) {
                return ObjectHelper.requireNonNull(this.mapper.apply(poll), "The mapper function returned a null value.");
            }
            return null;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableMap$MapSubscriber */
    static final class MapSubscriber<T, U> extends BasicFuseableSubscriber<T, U> {
        final Function<? super T, ? extends U> mapper;

        MapSubscriber(Subscriber<? super U> subscriber, Function<? super T, ? extends U> function) {
            super(subscriber);
            this.mapper = function;
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.sourceMode != 0) {
                    this.actual.onNext(null);
                    return;
                }
                try {
                    this.actual.onNext(ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper function returned a null value."));
                } catch (Throwable th) {
                    fail(th);
                }
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public U poll() throws Exception {
            Object poll = this.f525qs.poll();
            if (poll != null) {
                return ObjectHelper.requireNonNull(this.mapper.apply(poll), "The mapper function returned a null value.");
            }
            return null;
        }
    }

    public FlowableMap(Publisher<T> publisher, Function<? super T, ? extends U> function) {
        super(publisher);
        this.mapper = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super U> subscriber) {
        if (subscriber instanceof ConditionalSubscriber) {
            this.source.subscribe(new MapConditionalSubscriber((ConditionalSubscriber) subscriber, this.mapper));
        } else {
            this.source.subscribe(new MapSubscriber(subscriber, this.mapper));
        }
    }
}
