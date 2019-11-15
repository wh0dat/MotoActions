package p016io.reactivex.internal.subscriptions;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.internal.util.NotificationLite;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.subscriptions.FullArbiter */
public final class FullArbiter<T> extends FullArbiterPad2 implements Subscription {
    static final Subscription INITIAL = new Subscription() {
        public void cancel() {
        }

        public void request(long j) {
        }
    };
    static final Object REQUEST = new Object();
    final Subscriber<? super T> actual;
    volatile boolean cancelled;
    final SpscLinkedArrayQueue<Object> queue;
    long requested;
    Disposable resource;

    /* renamed from: s */
    volatile Subscription f550s = INITIAL;

    public FullArbiter(Subscriber<? super T> subscriber, Disposable disposable, int i) {
        this.actual = subscriber;
        this.resource = disposable;
        this.queue = new SpscLinkedArrayQueue<>(i);
    }

    public void request(long j) {
        if (SubscriptionHelper.validate(j)) {
            BackpressureHelper.add(this.missedRequested, j);
            this.queue.offer(REQUEST, REQUEST);
            drain();
        }
    }

    public void cancel() {
        if (!this.cancelled) {
            this.cancelled = true;
            dispose();
        }
    }

    /* access modifiers changed from: 0000 */
    public void dispose() {
        Disposable disposable = this.resource;
        this.resource = null;
        if (disposable != null) {
            disposable.dispose();
        }
    }

    public boolean setSubscription(Subscription subscription) {
        if (this.cancelled) {
            if (subscription != null) {
                subscription.cancel();
            }
            return false;
        }
        ObjectHelper.requireNonNull(subscription, "s is null");
        this.queue.offer(this.f550s, NotificationLite.subscription(subscription));
        drain();
        return true;
    }

    public boolean onNext(T t, Subscription subscription) {
        if (this.cancelled) {
            return false;
        }
        this.queue.offer(subscription, NotificationLite.next(t));
        drain();
        return true;
    }

    public void onError(Throwable th, Subscription subscription) {
        if (this.cancelled) {
            RxJavaPlugins.onError(th);
            return;
        }
        this.queue.offer(subscription, NotificationLite.error(th));
        drain();
    }

    public void onComplete(Subscription subscription) {
        this.queue.offer(subscription, NotificationLite.complete());
        drain();
    }

    /* access modifiers changed from: 0000 */
    public void drain() {
        if (this.wip.getAndIncrement() == 0) {
            SpscLinkedArrayQueue<Object> spscLinkedArrayQueue = this.queue;
            Subscriber<? super T> subscriber = this.actual;
            int i = 1;
            while (true) {
                Object poll = spscLinkedArrayQueue.poll();
                if (poll == null) {
                    i = this.wip.addAndGet(-i);
                    if (i == 0) {
                        return;
                    }
                } else {
                    Object poll2 = spscLinkedArrayQueue.poll();
                    if (poll == REQUEST) {
                        long andSet = this.missedRequested.getAndSet(0);
                        if (andSet != 0) {
                            this.requested = BackpressureHelper.addCap(this.requested, andSet);
                            this.f550s.request(andSet);
                        }
                    } else if (poll == this.f550s) {
                        if (NotificationLite.isSubscription(poll2)) {
                            Subscription subscription = NotificationLite.getSubscription(poll2);
                            if (!this.cancelled) {
                                this.f550s = subscription;
                                long j = this.requested;
                                if (j != 0) {
                                    subscription.request(j);
                                }
                            } else {
                                subscription.cancel();
                            }
                        } else if (NotificationLite.isError(poll2)) {
                            spscLinkedArrayQueue.clear();
                            dispose();
                            Throwable error = NotificationLite.getError(poll2);
                            if (!this.cancelled) {
                                this.cancelled = true;
                                subscriber.onError(error);
                            } else {
                                RxJavaPlugins.onError(error);
                            }
                        } else if (NotificationLite.isComplete(poll2)) {
                            spscLinkedArrayQueue.clear();
                            dispose();
                            if (!this.cancelled) {
                                this.cancelled = true;
                                subscriber.onComplete();
                            }
                        } else {
                            long j2 = this.requested;
                            if (j2 != 0) {
                                subscriber.onNext(NotificationLite.getValue(poll2));
                                this.requested = j2 - 1;
                            }
                        }
                    }
                }
            }
        }
    }
}
