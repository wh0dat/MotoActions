package p016io.reactivex.internal.operators.single;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import p016io.reactivex.Flowable;
import p016io.reactivex.Observable;
import p016io.reactivex.SingleSource;
import p016io.reactivex.functions.Function;

/* renamed from: io.reactivex.internal.operators.single.SingleInternalHelper */
public final class SingleInternalHelper {

    /* renamed from: io.reactivex.internal.operators.single.SingleInternalHelper$NoSuchElementCallable */
    enum NoSuchElementCallable implements Callable<NoSuchElementException> {
        INSTANCE;

        public NoSuchElementException call() throws Exception {
            return new NoSuchElementException();
        }
    }

    /* renamed from: io.reactivex.internal.operators.single.SingleInternalHelper$ToFlowable */
    enum ToFlowable implements Function<SingleSource, Publisher> {
        INSTANCE;

        public Publisher apply(SingleSource singleSource) {
            return new SingleToFlowable(singleSource);
        }
    }

    /* renamed from: io.reactivex.internal.operators.single.SingleInternalHelper$ToFlowableIterable */
    static final class ToFlowableIterable<T> implements Iterable<Flowable<T>> {
        private final Iterable<? extends SingleSource<? extends T>> sources;

        ToFlowableIterable(Iterable<? extends SingleSource<? extends T>> iterable) {
            this.sources = iterable;
        }

        public Iterator<Flowable<T>> iterator() {
            return new ToFlowableIterator(this.sources.iterator());
        }
    }

    /* renamed from: io.reactivex.internal.operators.single.SingleInternalHelper$ToFlowableIterator */
    static final class ToFlowableIterator<T> implements Iterator<Flowable<T>> {
        private final Iterator<? extends SingleSource<? extends T>> sit;

        ToFlowableIterator(Iterator<? extends SingleSource<? extends T>> it) {
            this.sit = it;
        }

        public boolean hasNext() {
            return this.sit.hasNext();
        }

        public Flowable<T> next() {
            return new SingleToFlowable((SingleSource) this.sit.next());
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* renamed from: io.reactivex.internal.operators.single.SingleInternalHelper$ToObservable */
    enum ToObservable implements Function<SingleSource, Observable> {
        INSTANCE;

        public Observable apply(SingleSource singleSource) {
            return new SingleToObservable(singleSource);
        }
    }

    private SingleInternalHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> Callable<NoSuchElementException> emptyThrower() {
        return NoSuchElementCallable.INSTANCE;
    }

    public static <T> Function<SingleSource<? extends T>, Publisher<? extends T>> toFlowable() {
        return ToFlowable.INSTANCE;
    }

    public static <T> Iterable<? extends Flowable<T>> iterableToFlowable(Iterable<? extends SingleSource<? extends T>> iterable) {
        return new ToFlowableIterable(iterable);
    }

    public static <T> Function<SingleSource<? extends T>, Observable<? extends T>> toObservable() {
        return ToObservable.INSTANCE;
    }
}
