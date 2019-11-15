package p016io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.Flowable;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.fuseable.QueueSubscription;
import p016io.reactivex.internal.fuseable.SimpleQueue;
import p016io.reactivex.internal.queue.SpscArrayQueue;
import p016io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableConcat */
public final class CompletableConcat extends Completable {
    final int prefetch;
    final Publisher<? extends CompletableSource> sources;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableConcat$CompletableConcatSubscriber */
    static final class CompletableConcatSubscriber extends AtomicInteger implements Subscriber<CompletableSource>, Disposable {
        private static final long serialVersionUID = 9032184911934499404L;
        volatile boolean active;
        final CompletableObserver actual;
        int consumed;
        volatile boolean done;
        final ConcatInnerObserver inner = new ConcatInnerObserver(this);
        final int limit;
        final AtomicBoolean once = new AtomicBoolean();
        final int prefetch;
        SimpleQueue<CompletableSource> queue;

        /* renamed from: s */
        Subscription f229s;
        int sourceFused;

        /* renamed from: io.reactivex.internal.operators.completable.CompletableConcat$CompletableConcatSubscriber$ConcatInnerObserver */
        static final class ConcatInnerObserver extends AtomicReference<Disposable> implements CompletableObserver {
            private static final long serialVersionUID = -5454794857847146511L;
            final CompletableConcatSubscriber parent;

            ConcatInnerObserver(CompletableConcatSubscriber completableConcatSubscriber) {
                this.parent = completableConcatSubscriber;
            }

            public void onSubscribe(Disposable disposable) {
                DisposableHelper.replace(this, disposable);
            }

            public void onError(Throwable th) {
                this.parent.innerError(th);
            }

            public void onComplete() {
                this.parent.innerComplete();
            }
        }

        CompletableConcatSubscriber(CompletableObserver completableObserver, int i) {
            this.actual = completableObserver;
            this.prefetch = i;
            this.limit = i - (i >> 2);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.f229s, subscription)) {
                this.f229s = subscription;
                long j = this.prefetch == Integer.MAX_VALUE ? LongCompanionObject.MAX_VALUE : (long) this.prefetch;
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(3);
                    if (requestFusion == 1) {
                        this.sourceFused = requestFusion;
                        this.queue = queueSubscription;
                        this.done = true;
                        this.actual.onSubscribe(this);
                        drain();
                        return;
                    } else if (requestFusion == 2) {
                        this.sourceFused = requestFusion;
                        this.queue = queueSubscription;
                        this.actual.onSubscribe(this);
                        subscription.request(j);
                        return;
                    }
                }
                if (this.prefetch == Integer.MAX_VALUE) {
                    this.queue = new SpscLinkedArrayQueue(Flowable.bufferSize());
                } else {
                    this.queue = new SpscArrayQueue(this.prefetch);
                }
                this.actual.onSubscribe(this);
                subscription.request(j);
            }
        }

        public void onNext(CompletableSource completableSource) {
            if (this.sourceFused != 0 || this.queue.offer(completableSource)) {
                drain();
            } else {
                onError(new MissingBackpressureException());
            }
        }

        public void onError(Throwable th) {
            if (this.once.compareAndSet(false, true)) {
                DisposableHelper.dispose(this.inner);
                this.actual.onError(th);
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void dispose() {
            this.f229s.cancel();
            DisposableHelper.dispose(this.inner);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) this.inner.get());
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                while (!isDisposed()) {
                    if (!this.active) {
                        boolean z = this.done;
                        try {
                            CompletableSource completableSource = (CompletableSource) this.queue.poll();
                            boolean z2 = completableSource == null;
                            if (z && z2) {
                                if (this.once.compareAndSet(false, true)) {
                                    this.actual.onComplete();
                                }
                                return;
                            } else if (!z2) {
                                this.active = true;
                                completableSource.subscribe(this.inner);
                                request();
                            }
                        } catch (Throwable th) {
                            Exceptions.throwIfFatal(th);
                            innerError(th);
                            return;
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void request() {
            if (this.sourceFused != 1) {
                int i = this.consumed + 1;
                if (i == this.limit) {
                    this.consumed = 0;
                    this.f229s.request((long) i);
                    return;
                }
                this.consumed = i;
            }
        }

        /* access modifiers changed from: 0000 */
        public void innerError(Throwable th) {
            if (this.once.compareAndSet(false, true)) {
                this.f229s.cancel();
                this.actual.onError(th);
                return;
            }
            RxJavaPlugins.onError(th);
        }

        /* access modifiers changed from: 0000 */
        public void innerComplete() {
            this.active = false;
            drain();
        }
    }

    public CompletableConcat(Publisher<? extends CompletableSource> publisher, int i) {
        this.sources = publisher;
        this.prefetch = i;
    }

    public void subscribeActual(CompletableObserver completableObserver) {
        this.sources.subscribe(new CompletableConcatSubscriber(completableObserver, this.prefetch));
    }
}
