package p016io.reactivex.internal.util;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Observer;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.exceptions.MissingBackpressureException;
import p016io.reactivex.functions.BooleanSupplier;
import p016io.reactivex.internal.fuseable.SimpleQueue;
import p016io.reactivex.internal.queue.SpscArrayQueue;
import p016io.reactivex.internal.queue.SpscLinkedArrayQueue;

/* renamed from: io.reactivex.internal.util.QueueDrainHelper */
public final class QueueDrainHelper {
    static final long COMPLETED_MASK = Long.MIN_VALUE;
    static final long REQUESTED_MASK = Long.MAX_VALUE;

    private QueueDrainHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static <T, U> void drainLoop(SimpleQueue<T> simpleQueue, Subscriber<? super U> subscriber, boolean z, QueueDrain<T, U> queueDrain) {
        int i;
        long j;
        Subscriber<? super U> subscriber2 = subscriber;
        QueueDrain<T, U> queueDrain2 = queueDrain;
        int i2 = 1;
        while (!checkTerminated(queueDrain.done(), simpleQueue.isEmpty(), subscriber2, z, simpleQueue, queueDrain2)) {
            long requested = queueDrain.requested();
            long j2 = 0;
            while (true) {
                if (j2 == requested) {
                    i = i2;
                    j = j2;
                    break;
                }
                boolean done = queueDrain.done();
                try {
                    Object poll = simpleQueue.poll();
                    boolean z2 = poll == null;
                    i = i2;
                    j = j2;
                    if (!checkTerminated(done, z2, subscriber2, z, simpleQueue, queueDrain2)) {
                        if (z2) {
                            break;
                        }
                        j2 = queueDrain2.accept(subscriber2, poll) ? j + 1 : j;
                        i2 = i;
                    } else {
                        return;
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    subscriber2.onError(th);
                    return;
                }
            }
            if (!(j == 0 || requested == Long.MAX_VALUE)) {
                queueDrain2.produced(j);
            }
            i2 = queueDrain2.leave(-i);
            if (i2 == 0) {
                return;
            }
        }
    }

    public static <T, U> void drainMaxLoop(SimpleQueue<T> simpleQueue, Subscriber<? super U> subscriber, boolean z, Disposable disposable, QueueDrain<T, U> queueDrain) {
        int i = 1;
        while (true) {
            boolean done = queueDrain.done();
            try {
                Object poll = simpleQueue.poll();
                boolean z2 = poll == null;
                if (checkTerminated(done, z2, subscriber, z, simpleQueue, queueDrain)) {
                    if (disposable != null) {
                        disposable.dispose();
                    }
                    return;
                } else if (z2) {
                    i = queueDrain.leave(-i);
                    if (i == 0) {
                        return;
                    }
                } else {
                    long requested = queueDrain.requested();
                    if (requested == 0) {
                        simpleQueue.clear();
                        if (disposable != null) {
                            disposable.dispose();
                        }
                        subscriber.onError(new MissingBackpressureException("Could not emit value due to lack of requests."));
                        return;
                    } else if (queueDrain.accept(subscriber, poll) && requested != Long.MAX_VALUE) {
                        queueDrain.produced(1);
                    }
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                subscriber.onError(th);
                return;
            }
        }
    }

    public static <T, U> boolean checkTerminated(boolean z, boolean z2, Subscriber<?> subscriber, boolean z3, SimpleQueue<?> simpleQueue, QueueDrain<T, U> queueDrain) {
        if (queueDrain.cancelled()) {
            simpleQueue.clear();
            return true;
        }
        if (z) {
            if (!z3) {
                Throwable error = queueDrain.error();
                if (error != null) {
                    simpleQueue.clear();
                    subscriber.onError(error);
                    return true;
                } else if (z2) {
                    subscriber.onComplete();
                    return true;
                }
            } else if (z2) {
                Throwable error2 = queueDrain.error();
                if (error2 != null) {
                    subscriber.onError(error2);
                } else {
                    subscriber.onComplete();
                }
                return true;
            }
        }
        return false;
    }

    public static <T, U> void drainLoop(SimpleQueue<T> simpleQueue, Observer<? super U> observer, boolean z, Disposable disposable, ObservableQueueDrain<T, U> observableQueueDrain) {
        int i = 1;
        while (!checkTerminated(observableQueueDrain.done(), simpleQueue.isEmpty(), observer, z, simpleQueue, disposable, observableQueueDrain)) {
            while (true) {
                boolean done = observableQueueDrain.done();
                try {
                    Object poll = simpleQueue.poll();
                    boolean z2 = poll == null;
                    if (!checkTerminated(done, z2, observer, z, simpleQueue, disposable, observableQueueDrain)) {
                        if (z2) {
                            i = observableQueueDrain.leave(-i);
                            if (i == 0) {
                                return;
                            }
                        } else {
                            observableQueueDrain.accept(observer, poll);
                        }
                    } else {
                        return;
                    }
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    observer.onError(th);
                    return;
                }
            }
        }
    }

    public static <T, U> boolean checkTerminated(boolean z, boolean z2, Observer<?> observer, boolean z3, SimpleQueue<?> simpleQueue, Disposable disposable, ObservableQueueDrain<T, U> observableQueueDrain) {
        if (observableQueueDrain.cancelled()) {
            simpleQueue.clear();
            disposable.dispose();
            return true;
        }
        if (z) {
            if (!z3) {
                Throwable error = observableQueueDrain.error();
                if (error != null) {
                    simpleQueue.clear();
                    disposable.dispose();
                    observer.onError(error);
                    return true;
                } else if (z2) {
                    disposable.dispose();
                    observer.onComplete();
                    return true;
                }
            } else if (z2) {
                disposable.dispose();
                Throwable error2 = observableQueueDrain.error();
                if (error2 != null) {
                    observer.onError(error2);
                } else {
                    observer.onComplete();
                }
                return true;
            }
        }
        return false;
    }

    public static <T> SimpleQueue<T> createQueue(int i) {
        if (i < 0) {
            return new SpscLinkedArrayQueue(-i);
        }
        return new SpscArrayQueue(i);
    }

    public static void request(Subscription subscription, int i) {
        subscription.request(i < 0 ? Long.MAX_VALUE : (long) i);
    }

    public static <T> boolean postCompleteRequest(long j, Subscriber<? super T> subscriber, Queue<T> queue, AtomicLong atomicLong, BooleanSupplier booleanSupplier) {
        long j2;
        AtomicLong atomicLong2;
        long j3 = j;
        do {
            j2 = atomicLong.get();
            atomicLong2 = atomicLong;
        } while (!atomicLong2.compareAndSet(j2, BackpressureHelper.addCap(Long.MAX_VALUE & j2, j3) | (j2 & Long.MIN_VALUE)));
        if (j2 != Long.MIN_VALUE) {
            return false;
        }
        postCompleteDrain(j3 | Long.MIN_VALUE, subscriber, queue, atomicLong2, booleanSupplier);
        return true;
    }

    static boolean isCancelled(BooleanSupplier booleanSupplier) {
        try {
            return booleanSupplier.getAsBoolean();
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            return true;
        }
    }

    static <T> boolean postCompleteDrain(long j, Subscriber<? super T> subscriber, Queue<T> queue, AtomicLong atomicLong, BooleanSupplier booleanSupplier) {
        long j2 = j & Long.MIN_VALUE;
        while (true) {
            if (j2 != j) {
                if (isCancelled(booleanSupplier)) {
                    return true;
                }
                Object poll = queue.poll();
                if (poll == null) {
                    subscriber.onComplete();
                    return true;
                }
                subscriber.onNext(poll);
                j2++;
            } else if (isCancelled(booleanSupplier)) {
                return true;
            } else {
                if (queue.isEmpty()) {
                    subscriber.onComplete();
                    return true;
                }
                j = atomicLong.get();
                if (j == j2) {
                    long addAndGet = atomicLong.addAndGet(-(j2 & Long.MAX_VALUE));
                    if ((Long.MAX_VALUE & addAndGet) == 0) {
                        return false;
                    }
                    j = addAndGet;
                    j2 = addAndGet & Long.MIN_VALUE;
                } else {
                    continue;
                }
            }
        }
    }

    public static <T> void postComplete(Subscriber<? super T> subscriber, Queue<T> queue, AtomicLong atomicLong, BooleanSupplier booleanSupplier) {
        long j;
        long j2;
        AtomicLong atomicLong2;
        if (queue.isEmpty()) {
            subscriber.onComplete();
        } else if (!postCompleteDrain(atomicLong.get(), subscriber, queue, atomicLong, booleanSupplier)) {
            do {
                j = atomicLong.get();
                if ((j & Long.MIN_VALUE) == 0) {
                    j2 = j | Long.MIN_VALUE;
                    atomicLong2 = atomicLong;
                } else {
                    return;
                }
            } while (!atomicLong2.compareAndSet(j, j2));
            if (j != 0) {
                postCompleteDrain(j2, subscriber, queue, atomicLong2, booleanSupplier);
            }
        }
    }
}
