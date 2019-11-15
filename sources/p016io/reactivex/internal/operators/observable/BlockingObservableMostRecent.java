package p016io.reactivex.internal.operators.observable;

import java.util.Iterator;
import java.util.NoSuchElementException;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.internal.util.NotificationLite;
import p016io.reactivex.observers.DefaultObserver;

/* renamed from: io.reactivex.internal.operators.observable.BlockingObservableMostRecent */
public final class BlockingObservableMostRecent<T> implements Iterable<T> {
    final T initialValue;
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.BlockingObservableMostRecent$MostRecentObserver */
    static final class MostRecentObserver<T> extends DefaultObserver<T> {
        volatile Object value;

        MostRecentObserver(T t) {
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
                    this.buf = MostRecentObserver.this.value;
                    return !NotificationLite.isComplete(this.buf);
                }

                public T next() {
                    Object obj = null;
                    try {
                        if (this.buf == null) {
                            obj = MostRecentObserver.this.value;
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

    public BlockingObservableMostRecent(ObservableSource<T> observableSource, T t) {
        this.source = observableSource;
        this.initialValue = t;
    }

    public Iterator<T> iterator() {
        MostRecentObserver mostRecentObserver = new MostRecentObserver(this.initialValue);
        this.source.subscribe(mostRecentObserver);
        return mostRecentObserver.getIterable();
    }
}
