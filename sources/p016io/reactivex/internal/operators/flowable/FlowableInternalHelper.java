package p016io.reactivex.internal.operators.flowable;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Emitter;
import p016io.reactivex.Flowable;
import p016io.reactivex.Scheduler;
import p016io.reactivex.flowables.ConnectableFlowable;
import p016io.reactivex.functions.Action;
import p016io.reactivex.functions.BiConsumer;
import p016io.reactivex.functions.BiFunction;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.functions.Functions;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableInternalHelper */
public final class FlowableInternalHelper {

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableInternalHelper$FlatMapIntoIterable */
    static final class FlatMapIntoIterable<T, U> implements Function<T, Publisher<U>> {
        private final Function<? super T, ? extends Iterable<? extends U>> mapper;

        FlatMapIntoIterable(Function<? super T, ? extends Iterable<? extends U>> function) {
            this.mapper = function;
        }

        public Publisher<U> apply(T t) throws Exception {
            return new FlowableFromIterable((Iterable) this.mapper.apply(t));
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableInternalHelper$FlatMapWithCombinerInner */
    static final class FlatMapWithCombinerInner<U, R, T> implements Function<U, R> {
        private final BiFunction<? super T, ? super U, ? extends R> combiner;

        /* renamed from: t */
        private final T f292t;

        FlatMapWithCombinerInner(BiFunction<? super T, ? super U, ? extends R> biFunction, T t) {
            this.combiner = biFunction;
            this.f292t = t;
        }

        public R apply(U u) throws Exception {
            return this.combiner.apply(this.f292t, u);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableInternalHelper$FlatMapWithCombinerOuter */
    static final class FlatMapWithCombinerOuter<T, R, U> implements Function<T, Publisher<R>> {
        private final BiFunction<? super T, ? super U, ? extends R> combiner;
        private final Function<? super T, ? extends Publisher<? extends U>> mapper;

        FlatMapWithCombinerOuter(BiFunction<? super T, ? super U, ? extends R> biFunction, Function<? super T, ? extends Publisher<? extends U>> function) {
            this.combiner = biFunction;
            this.mapper = function;
        }

        public Publisher<R> apply(T t) throws Exception {
            return new FlowableMap((Publisher) this.mapper.apply(t), new FlatMapWithCombinerInner(this.combiner, t));
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableInternalHelper$ItemDelayFunction */
    static final class ItemDelayFunction<T, U> implements Function<T, Publisher<T>> {
        final Function<? super T, ? extends Publisher<U>> itemDelay;

        ItemDelayFunction(Function<? super T, ? extends Publisher<U>> function) {
            this.itemDelay = function;
        }

        public Publisher<T> apply(T t) throws Exception {
            return new FlowableTake((Publisher) this.itemDelay.apply(t), 1).map(Functions.justFunction(t)).defaultIfEmpty(t);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableInternalHelper$RequestMax */
    public enum RequestMax implements Consumer<Subscription> {
        INSTANCE;

        public void accept(Subscription subscription) throws Exception {
            subscription.request(LongCompanionObject.MAX_VALUE);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableInternalHelper$SimpleBiGenerator */
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

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableInternalHelper$SimpleGenerator */
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

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableInternalHelper$SubscriberOnComplete */
    static final class SubscriberOnComplete<T> implements Action {
        final Subscriber<T> subscriber;

        SubscriberOnComplete(Subscriber<T> subscriber2) {
            this.subscriber = subscriber2;
        }

        public void run() throws Exception {
            this.subscriber.onComplete();
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableInternalHelper$SubscriberOnError */
    static final class SubscriberOnError<T> implements Consumer<Throwable> {
        final Subscriber<T> subscriber;

        SubscriberOnError(Subscriber<T> subscriber2) {
            this.subscriber = subscriber2;
        }

        public void accept(Throwable th) throws Exception {
            this.subscriber.onError(th);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableInternalHelper$SubscriberOnNext */
    static final class SubscriberOnNext<T> implements Consumer<T> {
        final Subscriber<T> subscriber;

        SubscriberOnNext(Subscriber<T> subscriber2) {
            this.subscriber = subscriber2;
        }

        public void accept(T t) throws Exception {
            this.subscriber.onNext(t);
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableInternalHelper$ZipIterableFunction */
    static final class ZipIterableFunction<T, R> implements Function<List<Publisher<? extends T>>, Publisher<? extends R>> {
        private final Function<? super Object[], ? extends R> zipper;

        ZipIterableFunction(Function<? super Object[], ? extends R> function) {
            this.zipper = function;
        }

        public Publisher<? extends R> apply(List<Publisher<? extends T>> list) {
            return Flowable.zipIterable(list, this.zipper, false, Flowable.bufferSize());
        }
    }

    private FlowableInternalHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static <T, S> BiFunction<S, Emitter<T>, S> simpleGenerator(Consumer<Emitter<T>> consumer) {
        return new SimpleGenerator(consumer);
    }

    public static <T, S> BiFunction<S, Emitter<T>, S> simpleBiGenerator(BiConsumer<S, Emitter<T>> biConsumer) {
        return new SimpleBiGenerator(biConsumer);
    }

    public static <T, U> Function<T, Publisher<T>> itemDelay(Function<? super T, ? extends Publisher<U>> function) {
        return new ItemDelayFunction(function);
    }

    public static <T> Consumer<T> subscriberOnNext(Subscriber<T> subscriber) {
        return new SubscriberOnNext(subscriber);
    }

    public static <T> Consumer<Throwable> subscriberOnError(Subscriber<T> subscriber) {
        return new SubscriberOnError(subscriber);
    }

    public static <T> Action subscriberOnComplete(Subscriber<T> subscriber) {
        return new SubscriberOnComplete(subscriber);
    }

    public static <T, U, R> Function<T, Publisher<R>> flatMapWithCombiner(Function<? super T, ? extends Publisher<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        return new FlatMapWithCombinerOuter(biFunction, function);
    }

    public static <T, U> Function<T, Publisher<U>> flatMapIntoIterable(Function<? super T, ? extends Iterable<? extends U>> function) {
        return new FlatMapIntoIterable(function);
    }

    public static <T> Callable<ConnectableFlowable<T>> replayCallable(final Flowable<T> flowable) {
        return new Callable<ConnectableFlowable<T>>() {
            public ConnectableFlowable<T> call() {
                return flowable.replay();
            }
        };
    }

    public static <T> Callable<ConnectableFlowable<T>> replayCallable(final Flowable<T> flowable, final int i) {
        return new Callable<ConnectableFlowable<T>>() {
            public ConnectableFlowable<T> call() {
                return flowable.replay(i);
            }
        };
    }

    public static <T> Callable<ConnectableFlowable<T>> replayCallable(Flowable<T> flowable, int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        final Flowable<T> flowable2 = flowable;
        final int i2 = i;
        final long j2 = j;
        final TimeUnit timeUnit2 = timeUnit;
        final Scheduler scheduler2 = scheduler;
        C07263 r0 = new Callable<ConnectableFlowable<T>>() {
            public ConnectableFlowable<T> call() {
                return flowable2.replay(i2, j2, timeUnit2, scheduler2);
            }
        };
        return r0;
    }

    public static <T> Callable<ConnectableFlowable<T>> replayCallable(Flowable<T> flowable, long j, TimeUnit timeUnit, Scheduler scheduler) {
        final Flowable<T> flowable2 = flowable;
        final long j2 = j;
        final TimeUnit timeUnit2 = timeUnit;
        final Scheduler scheduler2 = scheduler;
        C07274 r0 = new Callable<ConnectableFlowable<T>>() {
            public ConnectableFlowable<T> call() {
                return flowable2.replay(j2, timeUnit2, scheduler2);
            }
        };
        return r0;
    }

    public static <T, R> Function<Flowable<T>, Publisher<R>> replayFunction(final Function<? super Flowable<T>, ? extends Publisher<R>> function, final Scheduler scheduler) {
        return new Function<Flowable<T>, Publisher<R>>() {
            public Publisher<R> apply(Flowable<T> flowable) throws Exception {
                return Flowable.fromPublisher((Publisher) function.apply(flowable)).observeOn(scheduler);
            }
        };
    }

    public static <T, R> Function<List<Publisher<? extends T>>, Publisher<? extends R>> zipIterable(Function<? super Object[], ? extends R> function) {
        return new ZipIterableFunction(function);
    }
}
