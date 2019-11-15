package p016io.reactivex.internal.operators.flowable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import p016io.reactivex.Flowable;
import p016io.reactivex.Notification;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.subscribers.DisposableSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableNext */
public final class BlockingFlowableNext<T> implements Iterable<T> {
    final Publisher<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableNext$NextIterator */
    static final class NextIterator<T> implements Iterator<T> {
        private Throwable error;
        private boolean hasNext = true;
        private boolean isNextConsumed = true;
        private final Publisher<? extends T> items;
        private T next;
        private final NextSubscriber<T> observer;
        private boolean started;

        NextIterator(Publisher<? extends T> publisher, NextSubscriber<T> nextSubscriber) {
            this.items = publisher;
            this.observer = nextSubscriber;
        }

        public boolean hasNext() {
            if (this.error != null) {
                throw ExceptionHelper.wrapOrThrow(this.error);
            }
            boolean z = false;
            if (!this.hasNext) {
                return false;
            }
            if (!this.isNextConsumed || moveToNext()) {
                z = true;
            }
            return z;
        }

        private boolean moveToNext() {
            try {
                if (!this.started) {
                    this.started = true;
                    this.observer.setWaiting();
                    Flowable.fromPublisher(this.items).materialize().subscribe((Subscriber<? super T>) this.observer);
                }
                Notification takeNext = this.observer.takeNext();
                if (takeNext.isOnNext()) {
                    this.isNextConsumed = false;
                    this.next = takeNext.getValue();
                    return true;
                }
                this.hasNext = false;
                if (takeNext.isOnComplete()) {
                    return false;
                }
                if (takeNext.isOnError()) {
                    this.error = takeNext.getError();
                    throw ExceptionHelper.wrapOrThrow(this.error);
                }
                throw new IllegalStateException("Should not reach here");
            } catch (InterruptedException e) {
                this.observer.dispose();
                this.error = e;
                throw ExceptionHelper.wrapOrThrow(e);
            }
        }

        public T next() {
            if (this.error != null) {
                throw ExceptionHelper.wrapOrThrow(this.error);
            } else if (hasNext()) {
                this.isNextConsumed = true;
                return this.next;
            } else {
                throw new NoSuchElementException("No more elements");
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("Read only iterator");
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableNext$NextSubscriber */
    static final class NextSubscriber<T> extends DisposableSubscriber<Notification<T>> {
        private final BlockingQueue<Notification<T>> buf = new ArrayBlockingQueue(1);
        final AtomicInteger waiting = new AtomicInteger();

        public void onComplete() {
        }

        NextSubscriber() {
        }

        public void onError(Throwable th) {
            RxJavaPlugins.onError(th);
        }

        public void onNext(Notification<T> notification) {
            if (this.waiting.getAndSet(0) == 1 || !notification.isOnNext()) {
                while (!this.buf.offer(notification)) {
                    Notification<T> notification2 = (Notification) this.buf.poll();
                    if (notification2 != null && !notification2.isOnNext()) {
                        notification = notification2;
                    }
                }
            }
        }

        public Notification<T> takeNext() throws InterruptedException {
            setWaiting();
            return (Notification) this.buf.take();
        }

        /* access modifiers changed from: 0000 */
        public void setWaiting() {
            this.waiting.set(1);
        }
    }

    public BlockingFlowableNext(Publisher<? extends T> publisher) {
        this.source = publisher;
    }

    public Iterator<T> iterator() {
        return new NextIterator(this.source, new NextSubscriber());
    }
}
