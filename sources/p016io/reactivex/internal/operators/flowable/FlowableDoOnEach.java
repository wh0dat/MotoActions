package p016io.reactivex.internal.operators.flowable;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import p016io.reactivex.exceptions.CompositeException;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Action;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.internal.fuseable.ConditionalSubscriber;
import p016io.reactivex.internal.subscribers.BasicFuseableConditionalSubscriber;
import p016io.reactivex.internal.subscribers.BasicFuseableSubscriber;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableDoOnEach */
public final class FlowableDoOnEach<T> extends AbstractFlowableWithUpstream<T, T> {
    final Action onAfterTerminate;
    final Action onComplete;
    final Consumer<? super Throwable> onError;
    final Consumer<? super T> onNext;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDoOnEach$DoOnEachConditionalSubscriber */
    static final class DoOnEachConditionalSubscriber<T> extends BasicFuseableConditionalSubscriber<T, T> {
        final Action onAfterTerminate;
        final Action onComplete;
        final Consumer<? super Throwable> onError;
        final Consumer<? super T> onNext;

        DoOnEachConditionalSubscriber(ConditionalSubscriber<? super T> conditionalSubscriber, Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action, Action action2) {
            super(conditionalSubscriber);
            this.onNext = consumer;
            this.onError = consumer2;
            this.onComplete = action;
            this.onAfterTerminate = action2;
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.sourceMode != 0) {
                    this.actual.onNext(null);
                    return;
                }
                try {
                    this.onNext.accept(t);
                    this.actual.onNext(t);
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
                this.onNext.accept(t);
                return this.actual.tryOnNext(t);
            } catch (Throwable th) {
                fail(th);
                return false;
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            boolean z = true;
            this.done = true;
            try {
                this.onError.accept(th);
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.actual.onError(new CompositeException(th, th2));
                z = false;
            }
            if (z) {
                this.actual.onError(th);
            }
            try {
                this.onAfterTerminate.run();
            } catch (Throwable th3) {
                Exceptions.throwIfFatal(th3);
                RxJavaPlugins.onError(th3);
            }
        }

        public void onComplete() {
            if (!this.done) {
                try {
                    this.onComplete.run();
                    this.done = true;
                    this.actual.onComplete();
                    try {
                        this.onAfterTerminate.run();
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        RxJavaPlugins.onError(th);
                    }
                } catch (Throwable th2) {
                    fail(th2);
                }
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public T poll() throws Exception {
            T poll = this.f523qs.poll();
            if (poll != null) {
                try {
                    this.onNext.accept(poll);
                } finally {
                    this.onAfterTerminate.run();
                }
            } else if (this.sourceMode == 1) {
                this.onComplete.run();
                this.onAfterTerminate.run();
            }
            return poll;
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDoOnEach$DoOnEachSubscriber */
    static final class DoOnEachSubscriber<T> extends BasicFuseableSubscriber<T, T> {
        final Action onAfterTerminate;
        final Action onComplete;
        final Consumer<? super Throwable> onError;
        final Consumer<? super T> onNext;

        DoOnEachSubscriber(Subscriber<? super T> subscriber, Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action, Action action2) {
            super(subscriber);
            this.onNext = consumer;
            this.onError = consumer2;
            this.onComplete = action;
            this.onAfterTerminate = action2;
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.sourceMode != 0) {
                    this.actual.onNext(null);
                    return;
                }
                try {
                    this.onNext.accept(t);
                    this.actual.onNext(t);
                } catch (Throwable th) {
                    fail(th);
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            boolean z = true;
            this.done = true;
            try {
                this.onError.accept(th);
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.actual.onError(new CompositeException(th, th2));
                z = false;
            }
            if (z) {
                this.actual.onError(th);
            }
            try {
                this.onAfterTerminate.run();
            } catch (Throwable th3) {
                Exceptions.throwIfFatal(th3);
                RxJavaPlugins.onError(th3);
            }
        }

        public void onComplete() {
            if (!this.done) {
                try {
                    this.onComplete.run();
                    this.done = true;
                    this.actual.onComplete();
                    try {
                        this.onAfterTerminate.run();
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        RxJavaPlugins.onError(th);
                    }
                } catch (Throwable th2) {
                    fail(th2);
                }
            }
        }

        public int requestFusion(int i) {
            return transitiveBoundaryFusion(i);
        }

        public T poll() throws Exception {
            T poll = this.f525qs.poll();
            if (poll != null) {
                try {
                    this.onNext.accept(poll);
                } finally {
                    this.onAfterTerminate.run();
                }
            } else if (this.sourceMode == 1) {
                this.onComplete.run();
                this.onAfterTerminate.run();
            }
            return poll;
        }
    }

    public FlowableDoOnEach(Publisher<T> publisher, Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action, Action action2) {
        super(publisher);
        this.onNext = consumer;
        this.onError = consumer2;
        this.onComplete = action;
        this.onAfterTerminate = action2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        if (subscriber instanceof ConditionalSubscriber) {
            Publisher publisher = this.source;
            DoOnEachConditionalSubscriber doOnEachConditionalSubscriber = new DoOnEachConditionalSubscriber((ConditionalSubscriber) subscriber, this.onNext, this.onError, this.onComplete, this.onAfterTerminate);
            publisher.subscribe(doOnEachConditionalSubscriber);
            return;
        }
        Publisher publisher2 = this.source;
        DoOnEachSubscriber doOnEachSubscriber = new DoOnEachSubscriber(subscriber, this.onNext, this.onError, this.onComplete, this.onAfterTerminate);
        publisher2.subscribe(doOnEachSubscriber);
    }
}
