package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import p016io.reactivex.annotations.Experimental;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.internal.fuseable.ConditionalSubscriber;
import p016io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import p016io.reactivex.internal.subscribers.BasicFuseableSubscriber;

@Experimental
/* renamed from: io.reactivex.internal.operators.flowable.FlowableDoAfterNext */
public final class FlowableDoAfterNext<T> extends AbstractFlowableWithUpstream<T, T> {
    final Consumer<? super T> onAfterNext;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDoAfterNext$DoAfterConditionalSubscriber */
    static final class DoAfterConditionalSubscriber<T> extends BasicFuseableConditionalSubscriber<T, T> {
        final Consumer<? super T> onAfterNext;

        DoAfterConditionalSubscriber(ConditionalSubscriber<? super T> conditionalSubscriber, Consumer<? super T> consumer) {
            super(conditionalSubscriber);
            this.onAfterNext = consumer;
        }

        public void onNext(T t) {
            this.actual.onNext(t);
            if (this.sourceMode == 0) {
                try {
                    this.onAfterNext.accept(t);
                } catch (Throwable th) {
                    fail(th);
                }
            }
        }

        public boolean tryOnNext(T t) {
            boolean tryOnNext = this.actual.tryOnNext(t);
            try {
                this.onAfterNext.accept(t);
            } catch (Throwable th) {
                fail(th);
            }
            return tryOnNext;
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public T poll() throws Exception {
            T poll = this.f523qs.poll();
            if (poll != null) {
                this.onAfterNext.accept(poll);
            }
            return poll;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDoAfterNext$DoAfterSubscriber */
    static final class DoAfterSubscriber<T> extends BasicFuseableSubscriber<T, T> {
        final Consumer<? super T> onAfterNext;

        DoAfterSubscriber(Subscriber<? super T> subscriber, Consumer<? super T> consumer) {
            super(subscriber);
            this.onAfterNext = consumer;
        }

        public void onNext(T t) {
            this.actual.onNext(t);
            if (this.sourceMode == 0) {
                try {
                    this.onAfterNext.accept(t);
                } catch (Throwable th) {
                    fail(th);
                }
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public T poll() throws Exception {
            T poll = this.f525qs.poll();
            if (poll != null) {
                this.onAfterNext.accept(poll);
            }
            return poll;
        }
    }

    public FlowableDoAfterNext(Publisher<T> publisher, Consumer<? super T> consumer) {
        super(publisher);
        this.onAfterNext = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        if (subscriber instanceof ConditionalSubscriber) {
            this.source.subscribe(new DoAfterConditionalSubscriber((ConditionalSubscriber) subscriber, this.onAfterNext));
        } else {
            this.source.subscribe(new DoAfterSubscriber(subscriber, this.onAfterNext));
        }
    }
}
