package p016io.reactivex.internal.operators.observable;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import p016io.reactivex.Emitter;
import p016io.reactivex.Notification;
import p016io.reactivex.Observable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.Scheduler;
import p016io.reactivex.functions.Action;
import p016io.reactivex.functions.BiConsumer;
import p016io.reactivex.functions.BiFunction;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.functions.Function;
import p016io.reactivex.functions.Predicate;
import p016io.reactivex.internal.functions.Functions;
import p016io.reactivex.observables.ConnectableObservable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper */
public final class ObservableInternalHelper {

    /* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper$ErrorMapperFilter */
    enum ErrorMapperFilter implements Function<Notification<Object>, Throwable>, Predicate<Notification<Object>> {
        INSTANCE;

        public Throwable apply(Notification<Object> notification) throws Exception {
            return notification.getError();
        }

        public boolean test(Notification<Object> notification) throws Exception {
            return notification.isOnError();
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper$FlatMapIntoIterable */
    static final class FlatMapIntoIterable<T, U> implements Function<T, ObservableSource<U>> {
        private final Function<? super T, ? extends Iterable<? extends U>> mapper;

        FlatMapIntoIterable(Function<? super T, ? extends Iterable<? extends U>> function) {
            this.mapper = function;
        }

        public ObservableSource<U> apply(T t) throws Exception {
            return new ObservableFromIterable((Iterable) this.mapper.apply(t));
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper$FlatMapWithCombinerInner */
    static final class FlatMapWithCombinerInner<U, R, T> implements Function<U, R> {
        private final BiFunction<? super T, ? super U, ? extends R> combiner;

        /* renamed from: t */
        private final T f443t;

        FlatMapWithCombinerInner(BiFunction<? super T, ? super U, ? extends R> biFunction, T t) {
            this.combiner = biFunction;
            this.f443t = t;
        }

        public R apply(U u) throws Exception {
            return this.combiner.apply(this.f443t, u);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper$FlatMapWithCombinerOuter */
    static final class FlatMapWithCombinerOuter<T, R, U> implements Function<T, ObservableSource<R>> {
        private final BiFunction<? super T, ? super U, ? extends R> combiner;
        private final Function<? super T, ? extends ObservableSource<? extends U>> mapper;

        FlatMapWithCombinerOuter(BiFunction<? super T, ? super U, ? extends R> biFunction, Function<? super T, ? extends ObservableSource<? extends U>> function) {
            this.combiner = biFunction;
            this.mapper = function;
        }

        public ObservableSource<R> apply(T t) throws Exception {
            return new ObservableMap((ObservableSource) this.mapper.apply(t), new FlatMapWithCombinerInner(this.combiner, t));
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper$ItemDelayFunction */
    static final class ItemDelayFunction<T, U> implements Function<T, ObservableSource<T>> {
        final Function<? super T, ? extends ObservableSource<U>> itemDelay;

        ItemDelayFunction(Function<? super T, ? extends ObservableSource<U>> function) {
            this.itemDelay = function;
        }

        public ObservableSource<T> apply(T t) throws Exception {
            return new ObservableTake((ObservableSource) this.itemDelay.apply(t), 1).map(Functions.justFunction(t)).defaultIfEmpty(t);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper$MapToInt */
    enum MapToInt implements Function<Object, Object> {
        INSTANCE;

        public Object apply(Object obj) throws Exception {
            return Integer.valueOf(0);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper$ObserverOnComplete */
    static final class ObserverOnComplete<T> implements Action {
        final Observer<T> observer;

        ObserverOnComplete(Observer<T> observer2) {
            this.observer = observer2;
        }

        public void run() throws Exception {
            this.observer.onComplete();
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper$ObserverOnError */
    static final class ObserverOnError<T> implements Consumer<Throwable> {
        final Observer<T> observer;

        ObserverOnError(Observer<T> observer2) {
            this.observer = observer2;
        }

        public void accept(Throwable th) throws Exception {
            this.observer.onError(th);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper$ObserverOnNext */
    static final class ObserverOnNext<T> implements Consumer<T> {
        final Observer<T> observer;

        ObserverOnNext(Observer<T> observer2) {
            this.observer = observer2;
        }

        public void accept(T t) throws Exception {
            this.observer.onNext(t);
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper$RepeatWhenOuterHandler */
    static final class RepeatWhenOuterHandler implements Function<Observable<Notification<Object>>, ObservableSource<?>> {
        private final Function<? super Observable<Object>, ? extends ObservableSource<?>> handler;

        RepeatWhenOuterHandler(Function<? super Observable<Object>, ? extends ObservableSource<?>> function) {
            this.handler = function;
        }

        public ObservableSource<?> apply(Observable<Notification<Object>> observable) throws Exception {
            return (ObservableSource) this.handler.apply(observable.map(MapToInt.INSTANCE));
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper$RetryWhenInner */
    static final class RetryWhenInner implements Function<Observable<Notification<Object>>, ObservableSource<?>> {
        private final Function<? super Observable<Throwable>, ? extends ObservableSource<?>> handler;

        RetryWhenInner(Function<? super Observable<Throwable>, ? extends ObservableSource<?>> function) {
            this.handler = function;
        }

        public ObservableSource<?> apply(Observable<Notification<Object>> observable) throws Exception {
            return (ObservableSource) this.handler.apply(observable.takeWhile(ErrorMapperFilter.INSTANCE).map(ErrorMapperFilter.INSTANCE));
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper$SimpleBiGenerator */
    static final class SimpleBiGenerator<T, S> implements BiFunction<S, Emitter<T>, S> {
        final BiConsumer<S, Emitter<T>> consumer;

        SimpleBiGenerator(BiConsumer<S, Emitter<T>> biConsumer) {
            this.consumer = biConsumer;
        }

        public S apply(S s, Emitter<T> emitter) throws Exception {
            this.consumer.accept(s, emitter);
            return s;
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper$SimpleGenerator */
    static final class SimpleGenerator<T, S> implements BiFunction<S, Emitter<T>, S> {
        final Consumer<Emitter<T>> consumer;

        SimpleGenerator(Consumer<Emitter<T>> consumer2) {
            this.consumer = consumer2;
        }

        public S apply(S s, Emitter<T> emitter) throws Exception {
            this.consumer.accept(emitter);
            return s;
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableInternalHelper$ZipIterableFunction */
    static final class ZipIterableFunction<T, R> implements Function<List<ObservableSource<? extends T>>, ObservableSource<? extends R>> {
        private final Function<? super Object[], ? extends R> zipper;

        ZipIterableFunction(Function<? super Object[], ? extends R> function) {
            this.zipper = function;
        }

        public ObservableSource<? extends R> apply(List<ObservableSource<? extends T>> list) {
            return Observable.zipIterable(list, this.zipper, false, Observable.bufferSize());
        }
    }

    private ObservableInternalHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static <T, S> BiFunction<S, Emitter<T>, S> simpleGenerator(Consumer<Emitter<T>> consumer) {
        return new SimpleGenerator(consumer);
    }

    public static <T, S> BiFunction<S, Emitter<T>, S> simpleBiGenerator(BiConsumer<S, Emitter<T>> biConsumer) {
        return new SimpleBiGenerator(biConsumer);
    }

    public static <T, U> Function<T, ObservableSource<T>> itemDelay(Function<? super T, ? extends ObservableSource<U>> function) {
        return new ItemDelayFunction(function);
    }

    public static <T> Consumer<T> observerOnNext(Observer<T> observer) {
        return new ObserverOnNext(observer);
    }

    public static <T> Consumer<Throwable> observerOnError(Observer<T> observer) {
        return new ObserverOnError(observer);
    }

    public static <T> Action observerOnComplete(Observer<T> observer) {
        return new ObserverOnComplete(observer);
    }

    public static <T, U, R> Function<T, ObservableSource<R>> flatMapWithCombiner(Function<? super T, ? extends ObservableSource<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        return new FlatMapWithCombinerOuter(biFunction, function);
    }

    public static <T, U> Function<T, ObservableSource<U>> flatMapIntoIterable(Function<? super T, ? extends Iterable<? extends U>> function) {
        return new FlatMapIntoIterable(function);
    }

    public static Function<Observable<Notification<Object>>, ObservableSource<?>> repeatWhenHandler(Function<? super Observable<Object>, ? extends ObservableSource<?>> function) {
        return new RepeatWhenOuterHandler(function);
    }

    public static <T> Callable<ConnectableObservable<T>> replayCallable(final Observable<T> observable) {
        return new Callable<ConnectableObservable<T>>() {
            public ConnectableObservable<T> call() {
                return observable.replay();
            }
        };
    }

    public static <T> Callable<ConnectableObservable<T>> replayCallable(final Observable<T> observable, final int i) {
        return new Callable<ConnectableObservable<T>>() {
            public ConnectableObservable<T> call() {
                return observable.replay(i);
            }
        };
    }

    public static <T> Callable<ConnectableObservable<T>> replayCallable(Observable<T> observable, int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        final Observable<T> observable2 = observable;
        final int i2 = i;
        final long j2 = j;
        final TimeUnit timeUnit2 = timeUnit;
        final Scheduler scheduler2 = scheduler;
        C07613 r0 = new Callable<ConnectableObservable<T>>() {
            public ConnectableObservable<T> call() {
                return observable2.replay(i2, j2, timeUnit2, scheduler2);
            }
        };
        return r0;
    }

    public static <T> Callable<ConnectableObservable<T>> replayCallable(Observable<T> observable, long j, TimeUnit timeUnit, Scheduler scheduler) {
        final Observable<T> observable2 = observable;
        final long j2 = j;
        final TimeUnit timeUnit2 = timeUnit;
        final Scheduler scheduler2 = scheduler;
        C07624 r0 = new Callable<ConnectableObservable<T>>() {
            public ConnectableObservable<T> call() {
                return observable2.replay(j2, timeUnit2, scheduler2);
            }
        };
        return r0;
    }

    public static <T, R> Function<Observable<T>, ObservableSource<R>> replayFunction(final Function<? super Observable<T>, ? extends ObservableSource<R>> function, final Scheduler scheduler) {
        return new Function<Observable<T>, ObservableSource<R>>() {
            public ObservableSource<R> apply(Observable<T> observable) throws Exception {
                return Observable.wrap((ObservableSource) function.apply(observable)).observeOn(scheduler);
            }
        };
    }

    public static <T> Function<Observable<Notification<Object>>, ObservableSource<?>> retryWhenHandler(Function<? super Observable<Throwable>, ? extends ObservableSource<?>> function) {
        return new RetryWhenInner(function);
    }

    public static <T, R> Function<List<ObservableSource<? extends T>>, ObservableSource<? extends R>> zipIterable(Function<? super Object[], ? extends R> function) {
        return new ZipIterableFunction(function);
    }
}
