package p016io.reactivex.internal.operators.flowable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.reactivestreams.Publisher;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.internal.util.NotificationLite;
import p016io.reactivex.subscribers.DefaultSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent */
public final class BlockingFlowableMostRecent<T> implements Iterable<T> {
    final T initialValue;
    final Publisher<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent$MostRecentSubscriber */
    static final class MostRecentSubscriber<T> extends DefaultSubscriber<T> {
        volatile Object value;

        MostRecentSubscriber(T t) {
            this.value = NotificationLite.next(t);
        }

        public void onComplete() {
            this.value = NotificationLite.complete();
        }

        public void onError(Throwable th) {
            this.value = NotificationLite.error(th);
        }

        public void onNext(T t) {
            this.value = NotificationLite.next(t);
        }

        public Iterator<T> getIterable() {
            return new Iterator<T>() {
                private Object buf;

                public boolean hasNext() {
                    this.buf = MostRecentSubscriber.this.value;
                    return !NotificationLite.isComplete(this.buf);
                }

                public T next() {
                    Object obj = null;
                    try {
                        if (this.buf == null) {
                            obj = MostRecentSubscriber.this.value;
                        }
                        if (NotificationLite.isComplete(this.buf)) {
                            throw new NoSuchElementException();
                        } else if (NotificationLite.isError(this.buf)) {
                            throw ExceptionHelper.wrapOrThrow(NotificationLite.getError(this.buf));
                        } else {
                            T value = NotificationLite.getValue(this.buf);
                            this.buf = obj;
                            return value;
                        }
                    } finally {
                        this.buf = obj;
                    }
                }

                public void remove() {
                    throw new UnsupportedOperationException("Read only iterator");
                }
            };
        }
    }

    public BlockingFlowableMostRecent(Publisher<? extends T> publisher, T t) {
        this.source = publisher;
        this.initialValue = t;
    }

    public Iterator<T> iterator() {
        MostRecentSubscriber mostRecentSubscriber = new MostRecentSubscriber(this.initialValue);
        this.source.subscribe(mostRecentSubscriber);
        return mostRecentSubscriber.getIterable();
    }
}
