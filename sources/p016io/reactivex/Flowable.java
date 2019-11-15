package p016io.reactivex;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.annotations.BackpressureKind;
import p016io.reactivex.annotations.BackpressureSupport;
import p016io.reactivex.annotations.Experimental;
import p016io.reactivex.annotations.SchedulerSupport;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.flowables.ConnectableFlowable;
import p016io.reactivex.flowables.GroupedFlowable;
import p016io.reactivex.functions.Action;
import p016io.reactivex.functions.BiConsumer;
import p016io.reactivex.functions.BiFunction;
import p016io.reactivex.functions.BiPredicate;
import p016io.reactivex.functions.BooleanSupplier;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.functions.Function;
import p016io.reactivex.functions.Function3;
import p016io.reactivex.functions.Function4;
import p016io.reactivex.functions.Function5;
import p016io.reactivex.functions.Function6;
import p016io.reactivex.functions.Function7;
import p016io.reactivex.functions.Function8;
import p016io.reactivex.functions.Function9;
import p016io.reactivex.functions.LongConsumer;
import p016io.reactivex.functions.Predicate;
import p016io.reactivex.internal.functions.Functions;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.ScalarCallable;
import p016io.reactivex.internal.operators.flowable.BlockingFlowableIterable;
import p016io.reactivex.internal.operators.flowable.BlockingFlowableLatest;
import p016io.reactivex.internal.operators.flowable.BlockingFlowableMostRecent;
import p016io.reactivex.internal.operators.flowable.BlockingFlowableNext;
import p016io.reactivex.internal.operators.flowable.FlowableAllSingle;
import p016io.reactivex.internal.operators.flowable.FlowableAmb;
import p016io.reactivex.internal.operators.flowable.FlowableAnySingle;
import p016io.reactivex.internal.operators.flowable.FlowableBlockingSubscribe;
import p016io.reactivex.internal.operators.flowable.FlowableBuffer;
import p016io.reactivex.internal.operators.flowable.FlowableBufferBoundary;
import p016io.reactivex.internal.operators.flowable.FlowableBufferBoundarySupplier;
import p016io.reactivex.internal.operators.flowable.FlowableBufferExactBoundary;
import p016io.reactivex.internal.operators.flowable.FlowableBufferTimed;
import p016io.reactivex.internal.operators.flowable.FlowableCache;
import p016io.reactivex.internal.operators.flowable.FlowableCollectSingle;
import p016io.reactivex.internal.operators.flowable.FlowableCombineLatest;
import p016io.reactivex.internal.operators.flowable.FlowableConcatArray;
import p016io.reactivex.internal.operators.flowable.FlowableConcatMap;
import p016io.reactivex.internal.operators.flowable.FlowableConcatMapEager;
import p016io.reactivex.internal.operators.flowable.FlowableCountSingle;
import p016io.reactivex.internal.operators.flowable.FlowableCreate;
import p016io.reactivex.internal.operators.flowable.FlowableDebounce;
import p016io.reactivex.internal.operators.flowable.FlowableDebounceTimed;
import p016io.reactivex.internal.operators.flowable.FlowableDefer;
import p016io.reactivex.internal.operators.flowable.FlowableDelay;
import p016io.reactivex.internal.operators.flowable.FlowableDelaySubscriptionOther;
import p016io.reactivex.internal.operators.flowable.FlowableDematerialize;
import p016io.reactivex.internal.operators.flowable.FlowableDetach;
import p016io.reactivex.internal.operators.flowable.FlowableDistinct;
import p016io.reactivex.internal.operators.flowable.FlowableDistinctUntilChanged;
import p016io.reactivex.internal.operators.flowable.FlowableDoAfterNext;
import p016io.reactivex.internal.operators.flowable.FlowableDoFinally;
import p016io.reactivex.internal.operators.flowable.FlowableDoOnEach;
import p016io.reactivex.internal.operators.flowable.FlowableDoOnLifecycle;
import p016io.reactivex.internal.operators.flowable.FlowableElementAtMaybe;
import p016io.reactivex.internal.operators.flowable.FlowableElementAtSingle;
import p016io.reactivex.internal.operators.flowable.FlowableEmpty;
import p016io.reactivex.internal.operators.flowable.FlowableError;
import p016io.reactivex.internal.operators.flowable.FlowableFilter;
import p016io.reactivex.internal.operators.flowable.FlowableFlatMap;
import p016io.reactivex.internal.operators.flowable.FlowableFlatMapCompletableCompletable;
import p016io.reactivex.internal.operators.flowable.FlowableFlatMapMaybe;
import p016io.reactivex.internal.operators.flowable.FlowableFlatMapSingle;
import p016io.reactivex.internal.operators.flowable.FlowableFlattenIterable;
import p016io.reactivex.internal.operators.flowable.FlowableFromArray;
import p016io.reactivex.internal.operators.flowable.FlowableFromCallable;
import p016io.reactivex.internal.operators.flowable.FlowableFromFuture;
import p016io.reactivex.internal.operators.flowable.FlowableFromIterable;
import p016io.reactivex.internal.operators.flowable.FlowableFromPublisher;
import p016io.reactivex.internal.operators.flowable.FlowableGenerate;
import p016io.reactivex.internal.operators.flowable.FlowableGroupBy;
import p016io.reactivex.internal.operators.flowable.FlowableGroupJoin;
import p016io.reactivex.internal.operators.flowable.FlowableHide;
import p016io.reactivex.internal.operators.flowable.FlowableIgnoreElements;
import p016io.reactivex.internal.operators.flowable.FlowableIgnoreElementsCompletable;
import p016io.reactivex.internal.operators.flowable.FlowableInternalHelper;
import p016io.reactivex.internal.operators.flowable.FlowableInternalHelper.RequestMax;
import p016io.reactivex.internal.operators.flowable.FlowableInterval;
import p016io.reactivex.internal.operators.flowable.FlowableIntervalRange;
import p016io.reactivex.internal.operators.flowable.FlowableJoin;
import p016io.reactivex.internal.operators.flowable.FlowableJust;
import p016io.reactivex.internal.operators.flowable.FlowableLastMaybe;
import p016io.reactivex.internal.operators.flowable.FlowableLastSingle;
import p016io.reactivex.internal.operators.flowable.FlowableLift;
import p016io.reactivex.internal.operators.flowable.FlowableMap;
import p016io.reactivex.internal.operators.flowable.FlowableMapNotification;
import p016io.reactivex.internal.operators.flowable.FlowableMaterialize;
import p016io.reactivex.internal.operators.flowable.FlowableNever;
import p016io.reactivex.internal.operators.flowable.FlowableObserveOn;
import p016io.reactivex.internal.operators.flowable.FlowableOnBackpressureBuffer;
import p016io.reactivex.internal.operators.flowable.FlowableOnBackpressureBufferStrategy;
import p016io.reactivex.internal.operators.flowable.FlowableOnBackpressureDrop;
import p016io.reactivex.internal.operators.flowable.FlowableOnBackpressureLatest;
import p016io.reactivex.internal.operators.flowable.FlowableOnErrorNext;
import p016io.reactivex.internal.operators.flowable.FlowableOnErrorReturn;
import p016io.reactivex.internal.operators.flowable.FlowablePublish;
import p016io.reactivex.internal.operators.flowable.FlowablePublishMulticast;
import p016io.reactivex.internal.operators.flowable.FlowableRange;
import p016io.reactivex.internal.operators.flowable.FlowableRangeLong;
import p016io.reactivex.internal.operators.flowable.FlowableReduceMaybe;
import p016io.reactivex.internal.operators.flowable.FlowableRepeat;
import p016io.reactivex.internal.operators.flowable.FlowableRepeatUntil;
import p016io.reactivex.internal.operators.flowable.FlowableRepeatWhen;
import p016io.reactivex.internal.operators.flowable.FlowableReplay;
import p016io.reactivex.internal.operators.flowable.FlowableRetryBiPredicate;
import p016io.reactivex.internal.operators.flowable.FlowableRetryPredicate;
import p016io.reactivex.internal.operators.flowable.FlowableRetryWhen;
import p016io.reactivex.internal.operators.flowable.FlowableSamplePublisher;
import p016io.reactivex.internal.operators.flowable.FlowableSampleTimed;
import p016io.reactivex.internal.operators.flowable.FlowableScalarXMap;
import p016io.reactivex.internal.operators.flowable.FlowableScan;
import p016io.reactivex.internal.operators.flowable.FlowableScanSeed;
import p016io.reactivex.internal.operators.flowable.FlowableSequenceEqualSingle;
import p016io.reactivex.internal.operators.flowable.FlowableSerialized;
import p016io.reactivex.internal.operators.flowable.FlowableSingleMaybe;
import p016io.reactivex.internal.operators.flowable.FlowableSingleSingle;
import p016io.reactivex.internal.operators.flowable.FlowableSkip;
import p016io.reactivex.internal.operators.flowable.FlowableSkipLast;
import p016io.reactivex.internal.operators.flowable.FlowableSkipLastTimed;
import p016io.reactivex.internal.operators.flowable.FlowableSkipUntil;
import p016io.reactivex.internal.operators.flowable.FlowableSkipWhile;
import p016io.reactivex.internal.operators.flowable.FlowableSubscribeOn;
import p016io.reactivex.internal.operators.flowable.FlowableSwitchIfEmpty;
import p016io.reactivex.internal.operators.flowable.FlowableSwitchMap;
import p016io.reactivex.internal.operators.flowable.FlowableTake;
import p016io.reactivex.internal.operators.flowable.FlowableTakeLast;
import p016io.reactivex.internal.operators.flowable.FlowableTakeLastOne;
import p016io.reactivex.internal.operators.flowable.FlowableTakeLastTimed;
import p016io.reactivex.internal.operators.flowable.FlowableTakeUntil;
import p016io.reactivex.internal.operators.flowable.FlowableTakeUntilPredicate;
import p016io.reactivex.internal.operators.flowable.FlowableTakeWhile;
import p016io.reactivex.internal.operators.flowable.FlowableThrottleFirstTimed;
import p016io.reactivex.internal.operators.flowable.FlowableTimeInterval;
import p016io.reactivex.internal.operators.flowable.FlowableTimeout;
import p016io.reactivex.internal.operators.flowable.FlowableTimeoutTimed;
import p016io.reactivex.internal.operators.flowable.FlowableTimer;
import p016io.reactivex.internal.operators.flowable.FlowableToListSingle;
import p016io.reactivex.internal.operators.flowable.FlowableUnsubscribeOn;
import p016io.reactivex.internal.operators.flowable.FlowableUsing;
import p016io.reactivex.internal.operators.flowable.FlowableWindow;
import p016io.reactivex.internal.operators.flowable.FlowableWindowBoundary;
import p016io.reactivex.internal.operators.flowable.FlowableWindowBoundarySelector;
import p016io.reactivex.internal.operators.flowable.FlowableWindowBoundarySupplier;
import p016io.reactivex.internal.operators.flowable.FlowableWindowTimed;
import p016io.reactivex.internal.operators.flowable.FlowableWithLatestFrom;
import p016io.reactivex.internal.operators.flowable.FlowableWithLatestFromMany;
import p016io.reactivex.internal.operators.flowable.FlowableZip;
import p016io.reactivex.internal.operators.flowable.FlowableZipIterable;
import p016io.reactivex.internal.operators.observable.ObservableFromPublisher;
import p016io.reactivex.internal.schedulers.ImmediateThinScheduler;
import p016io.reactivex.internal.subscribers.BlockingFirstSubscriber;
import p016io.reactivex.internal.subscribers.BlockingLastSubscriber;
import p016io.reactivex.internal.subscribers.ForEachWhileSubscriber;
import p016io.reactivex.internal.subscribers.FutureSubscriber;
import p016io.reactivex.internal.subscribers.LambdaSubscriber;
import p016io.reactivex.internal.util.ArrayListSupplier;
import p016io.reactivex.internal.util.ErrorMode;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.internal.util.HashMapSupplier;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.schedulers.Schedulers;
import p016io.reactivex.schedulers.Timed;
import p016io.reactivex.subscribers.SafeSubscriber;
import p016io.reactivex.subscribers.TestSubscriber;

/* renamed from: io.reactivex.Flowable */
public abstract class Flowable<T> implements Publisher<T> {
    static final int BUFFER_SIZE = Math.max(16, Integer.getInteger("rx2.buffer-size", 128).intValue());

    /* access modifiers changed from: protected */
    public abstract void subscribeActual(Subscriber<? super T> subscriber);

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> amb(Iterable<? extends Publisher<? extends T>> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableAmb<T>(null, iterable));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> ambArray(Publisher<? extends T>... publisherArr) {
        ObjectHelper.requireNonNull(publisherArr, "sources is null");
        int length = publisherArr.length;
        if (length == 0) {
            return empty();
        }
        if (length == 1) {
            return fromPublisher(publisherArr[0]);
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableAmb<T>(publisherArr, null));
    }

    public static int bufferSize() {
        return BUFFER_SIZE;
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Publisher<? extends T>[] publisherArr, Function<? super Object[], ? extends R> function) {
        return combineLatest(publisherArr, function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Function<? super Object[], ? extends R> function, Publisher<? extends T>... publisherArr) {
        return combineLatest(publisherArr, function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Publisher<? extends T>[] publisherArr, Function<? super Object[], ? extends R> function, int i) {
        ObjectHelper.requireNonNull(publisherArr, "sources is null");
        if (publisherArr.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCombineLatest<T>(publisherArr, function, i, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Iterable<? extends Publisher<? extends T>> iterable, Function<? super Object[], ? extends R> function) {
        return combineLatest(iterable, function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatest(Iterable<? extends Publisher<? extends T>> iterable, Function<? super Object[], ? extends R> function, int i) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCombineLatest<T>(iterable, function, i, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Publisher<? extends T>[] publisherArr, Function<? super Object[], ? extends R> function) {
        return combineLatestDelayError(publisherArr, function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Function<? super Object[], ? extends R> function, Publisher<? extends T>... publisherArr) {
        return combineLatestDelayError(publisherArr, function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Function<? super Object[], ? extends R> function, int i, Publisher<? extends T>... publisherArr) {
        return combineLatestDelayError(publisherArr, function, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Publisher<? extends T>[] publisherArr, Function<? super Object[], ? extends R> function, int i) {
        ObjectHelper.requireNonNull(publisherArr, "sources is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        if (publisherArr.length == 0) {
            return empty();
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCombineLatest<T>(publisherArr, function, i, true));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Iterable<? extends Publisher<? extends T>> iterable, Function<? super Object[], ? extends R> function) {
        return combineLatestDelayError(iterable, function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> combineLatestDelayError(Iterable<? extends Publisher<? extends T>> iterable, Function<? super Object[], ? extends R> function, int i) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCombineLatest<T>(iterable, function, i, true));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, R> Flowable<R> combineLatest(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, BiFunction<? super T1, ? super T2, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        return combineLatest(Functions.toFunction(biFunction), (Publisher<? extends T>[]) new Publisher[]{publisher, publisher2});
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Flowable<R> combineLatest(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, Publisher<? extends T3> publisher3, Function3<? super T1, ? super T2, ? super T3, ? extends R> function3) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        return combineLatest(Functions.toFunction(function3), (Publisher<? extends T>[]) new Publisher[]{publisher, publisher2, publisher3});
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Flowable<R> combineLatest(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, Publisher<? extends T3> publisher3, Publisher<? extends T4> publisher4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> function4) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        return combineLatest(Functions.toFunction(function4), (Publisher<? extends T>[]) new Publisher[]{publisher, publisher2, publisher3, publisher4});
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Flowable<R> combineLatest(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, Publisher<? extends T3> publisher3, Publisher<? extends T4> publisher4, Publisher<? extends T5> publisher5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> function5) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        return combineLatest(Functions.toFunction(function5), (Publisher<? extends T>[]) new Publisher[]{publisher, publisher2, publisher3, publisher4, publisher5});
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Flowable<R> combineLatest(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, Publisher<? extends T3> publisher3, Publisher<? extends T4> publisher4, Publisher<? extends T5> publisher5, Publisher<? extends T6> publisher6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> function6) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        return combineLatest(Functions.toFunction(function6), (Publisher<? extends T>[]) new Publisher[]{publisher, publisher2, publisher3, publisher4, publisher5, publisher6});
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Flowable<R> combineLatest(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, Publisher<? extends T3> publisher3, Publisher<? extends T4> publisher4, Publisher<? extends T5> publisher5, Publisher<? extends T6> publisher6, Publisher<? extends T7> publisher7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> function7) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        ObjectHelper.requireNonNull(publisher7, "source7 is null");
        return combineLatest(Functions.toFunction(function7), (Publisher<? extends T>[]) new Publisher[]{publisher, publisher2, publisher3, publisher4, publisher5, publisher6, publisher7});
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Flowable<R> combineLatest(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, Publisher<? extends T3> publisher3, Publisher<? extends T4> publisher4, Publisher<? extends T5> publisher5, Publisher<? extends T6> publisher6, Publisher<? extends T7> publisher7, Publisher<? extends T8> publisher8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> function8) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        ObjectHelper.requireNonNull(publisher7, "source7 is null");
        ObjectHelper.requireNonNull(publisher8, "source8 is null");
        return combineLatest(Functions.toFunction(function8), (Publisher<? extends T>[]) new Publisher[]{publisher, publisher2, publisher3, publisher4, publisher5, publisher6, publisher7, publisher8});
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Flowable<R> combineLatest(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, Publisher<? extends T3> publisher3, Publisher<? extends T4> publisher4, Publisher<? extends T5> publisher5, Publisher<? extends T6> publisher6, Publisher<? extends T7> publisher7, Publisher<? extends T8> publisher8, Publisher<? extends T9> publisher9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> function9) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        ObjectHelper.requireNonNull(publisher7, "source7 is null");
        ObjectHelper.requireNonNull(publisher8, "source8 is null");
        ObjectHelper.requireNonNull(publisher9, "source9 is null");
        return combineLatest(Functions.toFunction(function9), (Publisher<? extends T>[]) new Publisher[]{publisher, publisher2, publisher3, publisher4, publisher5, publisher6, publisher7, publisher8, publisher9});
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Iterable<? extends Publisher<? extends T>> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return fromIterable(iterable).concatMapDelayError(Functions.identity(), 2, false);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends Publisher<? extends T>> publisher) {
        return concat(publisher, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends Publisher<? extends T>> publisher, int i) {
        return fromPublisher(publisher).concatMap(Functions.identity(), i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends T> publisher, Publisher<? extends T> publisher2) {
        return concatArray(publisher, publisher2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends T> publisher, Publisher<? extends T> publisher2, Publisher<? extends T> publisher3) {
        return concatArray(publisher, publisher2, publisher3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends T> publisher, Publisher<? extends T> publisher2, Publisher<? extends T> publisher3, Publisher<? extends T> publisher4) {
        return concatArray(publisher, publisher2, publisher3, publisher4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArray(Publisher<? extends T>... publisherArr) {
        if (publisherArr.length == 0) {
            return empty();
        }
        if (publisherArr.length == 1) {
            return fromPublisher(publisherArr[0]);
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatArray<T>(publisherArr, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayDelayError(Publisher<? extends T>... publisherArr) {
        if (publisherArr.length == 0) {
            return empty();
        }
        if (publisherArr.length == 1) {
            return fromPublisher(publisherArr[0]);
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatArray<T>(publisherArr, true));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayEager(Publisher<? extends T>... publisherArr) {
        return concatArrayEager(bufferSize(), bufferSize(), publisherArr);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayEager(int i, int i2, Publisher<? extends T>... publisherArr) {
        FlowableConcatMapEager flowableConcatMapEager = new FlowableConcatMapEager(new FlowableFromArray(publisherArr), Functions.identity(), i, i2, ErrorMode.IMMEDIATE);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableConcatMapEager);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatDelayError(Iterable<? extends Publisher<? extends T>> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return fromIterable(iterable).concatMapDelayError(Functions.identity());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatDelayError(Publisher<? extends Publisher<? extends T>> publisher) {
        return concatDelayError(publisher, bufferSize(), true);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatDelayError(Publisher<? extends Publisher<? extends T>> publisher, int i, boolean z) {
        return fromPublisher(publisher).concatMapDelayError(Functions.identity(), i, z);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Publisher<? extends Publisher<? extends T>> publisher) {
        return concatEager(publisher, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Publisher<? extends Publisher<? extends T>> publisher, int i, int i2) {
        FlowableConcatMapEager flowableConcatMapEager = new FlowableConcatMapEager(publisher, Functions.identity(), i, i2, ErrorMode.IMMEDIATE);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableConcatMapEager);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Iterable<? extends Publisher<? extends T>> iterable) {
        return concatEager(iterable, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Iterable<? extends Publisher<? extends T>> iterable, int i, int i2) {
        FlowableConcatMapEager flowableConcatMapEager = new FlowableConcatMapEager(new FlowableFromIterable(iterable), Functions.identity(), i, i2, ErrorMode.IMMEDIATE);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableConcatMapEager);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> create(FlowableOnSubscribe<T> flowableOnSubscribe, BackpressureStrategy backpressureStrategy) {
        ObjectHelper.requireNonNull(flowableOnSubscribe, "source is null");
        ObjectHelper.requireNonNull(backpressureStrategy, "mode is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCreate<T>(flowableOnSubscribe, backpressureStrategy));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> defer(Callable<? extends Publisher<? extends T>> callable) {
        ObjectHelper.requireNonNull(callable, "supplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDefer<T>(callable));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> empty() {
        return RxJavaPlugins.onAssembly(FlowableEmpty.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> error(Callable<? extends Throwable> callable) {
        ObjectHelper.requireNonNull(callable, "errorSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableError<T>(callable));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> error(Throwable th) {
        ObjectHelper.requireNonNull(th, "throwable is null");
        return error(Functions.justCallable(th));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromArray(T... tArr) {
        ObjectHelper.requireNonNull(tArr, "items is null");
        if (tArr.length == 0) {
            return empty();
        }
        if (tArr.length == 1) {
            return just(tArr[0]);
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFromArray<T>(tArr));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromCallable(Callable<? extends T> callable) {
        ObjectHelper.requireNonNull(callable, "supplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFromCallable<T>(callable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromFuture(Future<? extends T> future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFromFuture<T>(future, 0, null));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromFuture(Future<? extends T> future, long j, TimeUnit timeUnit) {
        ObjectHelper.requireNonNull(future, "future is null");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFromFuture<T>(future, j, timeUnit));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public static <T> Flowable<T> fromFuture(Future<? extends T> future, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future, j, timeUnit).subscribeOn(scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public static <T> Flowable<T> fromFuture(Future<? extends T> future, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future).subscribeOn(scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromIterable(Iterable<? extends T> iterable) {
        ObjectHelper.requireNonNull(iterable, "source is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFromIterable<T>(iterable));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> fromPublisher(Publisher<? extends T> publisher) {
        if (publisher instanceof Flowable) {
            return RxJavaPlugins.onAssembly((Flowable) publisher);
        }
        ObjectHelper.requireNonNull(publisher, "publisher is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFromPublisher<T>(publisher));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> generate(Consumer<Emitter<T>> consumer) {
        ObjectHelper.requireNonNull(consumer, "generator is null");
        return generate(Functions.nullSupplier(), FlowableInternalHelper.simpleGenerator(consumer), Functions.emptyConsumer());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, S> Flowable<T> generate(Callable<S> callable, BiConsumer<S, Emitter<T>> biConsumer) {
        ObjectHelper.requireNonNull(biConsumer, "generator is null");
        return generate(callable, FlowableInternalHelper.simpleBiGenerator(biConsumer), Functions.emptyConsumer());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, S> Flowable<T> generate(Callable<S> callable, BiConsumer<S, Emitter<T>> biConsumer, Consumer<? super S> consumer) {
        ObjectHelper.requireNonNull(biConsumer, "generator is null");
        return generate(callable, FlowableInternalHelper.simpleBiGenerator(biConsumer), consumer);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, S> Flowable<T> generate(Callable<S> callable, BiFunction<S, Emitter<T>, S> biFunction) {
        return generate(callable, biFunction, Functions.emptyConsumer());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, S> Flowable<T> generate(Callable<S> callable, BiFunction<S, Emitter<T>, S> biFunction, Consumer<? super S> consumer) {
        ObjectHelper.requireNonNull(callable, "initialState is null");
        ObjectHelper.requireNonNull(biFunction, "generator is null");
        ObjectHelper.requireNonNull(consumer, "disposeState is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableGenerate<T>(callable, biFunction, consumer));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable<Long> interval(long j, long j2, TimeUnit timeUnit) {
        return interval(j, j2, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public static Flowable<Long> interval(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableInterval flowableInterval = new FlowableInterval(Math.max(0, j), Math.max(0, j2), timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableInterval);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable<Long> interval(long j, TimeUnit timeUnit) {
        return interval(j, j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public static Flowable<Long> interval(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return interval(j, j, timeUnit, scheduler);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable<Long> intervalRange(long j, long j2, long j3, long j4, TimeUnit timeUnit) {
        return intervalRange(j, j2, j3, j4, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public static Flowable<Long> intervalRange(long j, long j2, long j3, long j4, TimeUnit timeUnit, Scheduler scheduler) {
        long j5 = j2;
        long j6 = j3;
        TimeUnit timeUnit2 = timeUnit;
        Scheduler scheduler2 = scheduler;
        int i = (j5 > 0 ? 1 : (j5 == 0 ? 0 : -1));
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(j5);
            throw new IllegalArgumentException(sb.toString());
        } else if (i == 0) {
            return empty().delay(j6, timeUnit2, scheduler2);
        } else {
            long j7 = (j5 - 1) + j;
            if (j <= 0 || j7 >= 0) {
                ObjectHelper.requireNonNull(timeUnit2, "unit is null");
                ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
                FlowableIntervalRange flowableIntervalRange = new FlowableIntervalRange(j, j7, Math.max(0, j6), Math.max(0, j4), timeUnit2, scheduler2);
                return RxJavaPlugins.onAssembly((Flowable<T>) flowableIntervalRange);
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T t) {
        ObjectHelper.requireNonNull(t, "item is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableJust<T>(t));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T t, T t2) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        return fromArray(t, t2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T t, T t2, T t3) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        ObjectHelper.requireNonNull(t3, "The third item is null");
        return fromArray(t, t2, t3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T t, T t2, T t3, T t4) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        ObjectHelper.requireNonNull(t3, "The third item is null");
        ObjectHelper.requireNonNull(t4, "The fourth item is null");
        return fromArray(t, t2, t3, t4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T t, T t2, T t3, T t4, T t5) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        ObjectHelper.requireNonNull(t3, "The third item is null");
        ObjectHelper.requireNonNull(t4, "The fourth item is null");
        ObjectHelper.requireNonNull(t5, "The fifth item is null");
        return fromArray(t, t2, t3, t4, t5);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T t, T t2, T t3, T t4, T t5, T t6) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        ObjectHelper.requireNonNull(t3, "The third item is null");
        ObjectHelper.requireNonNull(t4, "The fourth item is null");
        ObjectHelper.requireNonNull(t5, "The fifth item is null");
        ObjectHelper.requireNonNull(t6, "The sixth item is null");
        return fromArray(t, t2, t3, t4, t5, t6);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T t, T t2, T t3, T t4, T t5, T t6, T t7) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        ObjectHelper.requireNonNull(t3, "The third item is null");
        ObjectHelper.requireNonNull(t4, "The fourth item is null");
        ObjectHelper.requireNonNull(t5, "The fifth item is null");
        ObjectHelper.requireNonNull(t6, "The sixth item is null");
        ObjectHelper.requireNonNull(t7, "The seventh item is null");
        return fromArray(t, t2, t3, t4, t5, t6, t7);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T t, T t2, T t3, T t4, T t5, T t6, T t7, T t8) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        ObjectHelper.requireNonNull(t3, "The third item is null");
        ObjectHelper.requireNonNull(t4, "The fourth item is null");
        ObjectHelper.requireNonNull(t5, "The fifth item is null");
        ObjectHelper.requireNonNull(t6, "The sixth item is null");
        ObjectHelper.requireNonNull(t7, "The seventh item is null");
        ObjectHelper.requireNonNull(t8, "The eighth item is null");
        return fromArray(t, t2, t3, t4, t5, t6, t7, t8);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T t, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        ObjectHelper.requireNonNull(t3, "The third item is null");
        ObjectHelper.requireNonNull(t4, "The fourth item is null");
        ObjectHelper.requireNonNull(t5, "The fifth item is null");
        ObjectHelper.requireNonNull(t6, "The sixth item is null");
        ObjectHelper.requireNonNull(t7, "The seventh item is null");
        ObjectHelper.requireNonNull(t8, "The eighth item is null");
        ObjectHelper.requireNonNull(t9, "The ninth is null");
        return fromArray(t, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> just(T t, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9, T t10) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        ObjectHelper.requireNonNull(t3, "The third item is null");
        ObjectHelper.requireNonNull(t4, "The fourth item is null");
        ObjectHelper.requireNonNull(t5, "The fifth item is null");
        ObjectHelper.requireNonNull(t6, "The sixth item is null");
        ObjectHelper.requireNonNull(t7, "The seventh item is null");
        ObjectHelper.requireNonNull(t8, "The eighth item is null");
        ObjectHelper.requireNonNull(t9, "The ninth item is null");
        ObjectHelper.requireNonNull(t10, "The tenth item is null");
        return fromArray(t, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Iterable<? extends Publisher<? extends T>> iterable, int i, int i2) {
        return fromIterable(iterable).flatMap(Functions.identity(), false, i, i2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArray(int i, int i2, Publisher<? extends T>... publisherArr) {
        return fromArray(publisherArr).flatMap(Functions.identity(), false, i, i2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Iterable<? extends Publisher<? extends T>> iterable) {
        return fromIterable(iterable).flatMap(Functions.identity());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Iterable<? extends Publisher<? extends T>> iterable, int i) {
        return fromIterable(iterable).flatMap(Functions.identity(), i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends Publisher<? extends T>> publisher) {
        return merge(publisher, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends Publisher<? extends T>> publisher, int i) {
        return fromPublisher(publisher).flatMap(Functions.identity(), i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArray(Publisher<? extends T>... publisherArr) {
        return fromArray(publisherArr).flatMap(Functions.identity(), publisherArr.length);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends T> publisher, Publisher<? extends T> publisher2) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        return fromArray(publisher, publisher2).flatMap(Functions.identity(), false, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends T> publisher, Publisher<? extends T> publisher2, Publisher<? extends T> publisher3) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        return fromArray(publisher, publisher2, publisher3).flatMap(Functions.identity(), false, 3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends T> publisher, Publisher<? extends T> publisher2, Publisher<? extends T> publisher3, Publisher<? extends T> publisher4) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        return fromArray(publisher, publisher2, publisher3, publisher4).flatMap(Functions.identity(), false, 4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Iterable<? extends Publisher<? extends T>> iterable) {
        return fromIterable(iterable).flatMap(Functions.identity(), true);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Iterable<? extends Publisher<? extends T>> iterable, int i, int i2) {
        return fromIterable(iterable).flatMap(Functions.identity(), true, i, i2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArrayDelayError(int i, int i2, Publisher<? extends T>... publisherArr) {
        return fromArray(publisherArr).flatMap(Functions.identity(), true, i, i2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Iterable<? extends Publisher<? extends T>> iterable, int i) {
        return fromIterable(iterable).flatMap(Functions.identity(), true, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends Publisher<? extends T>> publisher) {
        return mergeDelayError(publisher, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends Publisher<? extends T>> publisher, int i) {
        return fromPublisher(publisher).flatMap(Functions.identity(), true, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArrayDelayError(Publisher<? extends T>... publisherArr) {
        return fromArray(publisherArr).flatMap(Functions.identity(), true, publisherArr.length);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> publisher, Publisher<? extends T> publisher2) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        return fromArray(publisher, publisher2).flatMap(Functions.identity(), true, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> publisher, Publisher<? extends T> publisher2, Publisher<? extends T> publisher3) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        return fromArray(publisher, publisher2, publisher3).flatMap(Functions.identity(), true, 3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends T> publisher, Publisher<? extends T> publisher2, Publisher<? extends T> publisher3, Publisher<? extends T> publisher4) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        return fromArray(publisher, publisher2, publisher3, publisher4).flatMap(Functions.identity(), true, 4);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T> Flowable<T> never() {
        return RxJavaPlugins.onAssembly(FlowableNever.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Flowable<Integer> range(int i, int i2) {
        if (i2 < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(i2);
            throw new IllegalArgumentException(sb.toString());
        } else if (i2 == 0) {
            return empty();
        } else {
            if (i2 == 1) {
                return just(Integer.valueOf(i));
            }
            if (((long) i) + ((long) (i2 - 1)) <= 2147483647L) {
                return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRange<T>(i, i2));
            }
            throw new IllegalArgumentException("Integer overflow");
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Flowable<Long> rangeLong(long j, long j2) {
        int i = (j2 > 0 ? 1 : (j2 == 0 ? 0 : -1));
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(j2);
            throw new IllegalArgumentException(sb.toString());
        } else if (i == 0) {
            return empty();
        } else {
            if (j2 == 1) {
                return just(Long.valueOf(j));
            }
            long j3 = (j2 - 1) + j;
            if (j <= 0 || j3 >= 0) {
                return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRangeLong<T>(j, j2));
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> publisher, Publisher<? extends T> publisher2) {
        return sequenceEqual(publisher, publisher2, ObjectHelper.equalsPredicate(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> publisher, Publisher<? extends T> publisher2, BiPredicate<? super T, ? super T> biPredicate) {
        return sequenceEqual(publisher, publisher2, biPredicate, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> publisher, Publisher<? extends T> publisher2, BiPredicate<? super T, ? super T> biPredicate, int i) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(biPredicate, "isEqual is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableSequenceEqualSingle<T>(publisher, publisher2, biPredicate, i));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(Publisher<? extends T> publisher, Publisher<? extends T> publisher2, int i) {
        return sequenceEqual(publisher, publisher2, ObjectHelper.equalsPredicate(), i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> switchOnNext(Publisher<? extends Publisher<? extends T>> publisher, int i) {
        return fromPublisher(publisher).switchMap(Functions.identity(), i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> switchOnNext(Publisher<? extends Publisher<? extends T>> publisher) {
        return fromPublisher(publisher).switchMap(Functions.identity());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> switchOnNextDelayError(Publisher<? extends Publisher<? extends T>> publisher) {
        return switchOnNextDelayError(publisher, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> switchOnNextDelayError(Publisher<? extends Publisher<? extends T>> publisher, int i) {
        return fromPublisher(publisher).switchMapDelayError(Functions.identity(), i);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public static Flowable<Long> timer(long j, TimeUnit timeUnit) {
        return timer(j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public static Flowable<Long> timer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTimer<T>(Math.max(0, j), timeUnit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public static <T> Flowable<T> unsafeCreate(Publisher<T> publisher) {
        ObjectHelper.requireNonNull(publisher, "onSubscribe is null");
        if (!(publisher instanceof Flowable)) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFromPublisher<T>(publisher));
        }
        throw new IllegalArgumentException("unsafeCreate(Flowable) should be upgraded");
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T, D> Flowable<T> using(Callable<? extends D> callable, Function<? super D, ? extends Publisher<? extends T>> function, Consumer<? super D> consumer) {
        return using(callable, function, consumer, true);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public static <T, D> Flowable<T> using(Callable<? extends D> callable, Function<? super D, ? extends Publisher<? extends T>> function, Consumer<? super D> consumer, boolean z) {
        ObjectHelper.requireNonNull(callable, "resourceSupplier is null");
        ObjectHelper.requireNonNull(function, "sourceSupplier is null");
        ObjectHelper.requireNonNull(consumer, "disposer is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableUsing<T>(callable, function, consumer, z));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> zip(Iterable<? extends Publisher<? extends T>> iterable, Function<? super Object[], ? extends R> function) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(iterable, "sources is null");
        FlowableZip flowableZip = new FlowableZip(null, iterable, function, bufferSize(), false);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableZip);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> zip(Publisher<? extends Publisher<? extends T>> publisher, Function<? super Object[], ? extends R> function) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        return fromPublisher(publisher).toList().flatMapPublisher(FlowableInternalHelper.zipIterable(function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, BiFunction<? super T1, ? super T2, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), false, bufferSize(), publisher, publisher2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, BiFunction<? super T1, ? super T2, ? extends R> biFunction, boolean z) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), z, bufferSize(), publisher, publisher2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, R> Flowable<R> zip(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, BiFunction<? super T1, ? super T2, ? extends R> biFunction, boolean z, int i) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), z, i, publisher, publisher2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Flowable<R> zip(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, Publisher<? extends T3> publisher3, Function3<? super T1, ? super T2, ? super T3, ? extends R> function3) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        return zipArray(Functions.toFunction(function3), false, bufferSize(), publisher, publisher2, publisher3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Flowable<R> zip(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, Publisher<? extends T3> publisher3, Publisher<? extends T4> publisher4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> function4) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        return zipArray(Functions.toFunction(function4), false, bufferSize(), publisher, publisher2, publisher3, publisher4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Flowable<R> zip(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, Publisher<? extends T3> publisher3, Publisher<? extends T4> publisher4, Publisher<? extends T5> publisher5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> function5) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        return zipArray(Functions.toFunction(function5), false, bufferSize(), publisher, publisher2, publisher3, publisher4, publisher5);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Flowable<R> zip(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, Publisher<? extends T3> publisher3, Publisher<? extends T4> publisher4, Publisher<? extends T5> publisher5, Publisher<? extends T6> publisher6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> function6) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        return zipArray(Functions.toFunction(function6), false, bufferSize(), publisher, publisher2, publisher3, publisher4, publisher5, publisher6);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Flowable<R> zip(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, Publisher<? extends T3> publisher3, Publisher<? extends T4> publisher4, Publisher<? extends T5> publisher5, Publisher<? extends T6> publisher6, Publisher<? extends T7> publisher7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> function7) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        ObjectHelper.requireNonNull(publisher7, "source7 is null");
        return zipArray(Functions.toFunction(function7), false, bufferSize(), publisher, publisher2, publisher3, publisher4, publisher5, publisher6, publisher7);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Flowable<R> zip(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, Publisher<? extends T3> publisher3, Publisher<? extends T4> publisher4, Publisher<? extends T5> publisher5, Publisher<? extends T6> publisher6, Publisher<? extends T7> publisher7, Publisher<? extends T8> publisher8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> function8) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        ObjectHelper.requireNonNull(publisher7, "source7 is null");
        ObjectHelper.requireNonNull(publisher8, "source8 is null");
        return zipArray(Functions.toFunction(function8), false, bufferSize(), publisher, publisher2, publisher3, publisher4, publisher5, publisher6, publisher7, publisher8);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Flowable<R> zip(Publisher<? extends T1> publisher, Publisher<? extends T2> publisher2, Publisher<? extends T3> publisher3, Publisher<? extends T4> publisher4, Publisher<? extends T5> publisher5, Publisher<? extends T6> publisher6, Publisher<? extends T7> publisher7, Publisher<? extends T8> publisher8, Publisher<? extends T9> publisher9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> function9) {
        ObjectHelper.requireNonNull(publisher, "source1 is null");
        ObjectHelper.requireNonNull(publisher2, "source2 is null");
        ObjectHelper.requireNonNull(publisher3, "source3 is null");
        ObjectHelper.requireNonNull(publisher4, "source4 is null");
        ObjectHelper.requireNonNull(publisher5, "source5 is null");
        ObjectHelper.requireNonNull(publisher6, "source6 is null");
        ObjectHelper.requireNonNull(publisher7, "source7 is null");
        ObjectHelper.requireNonNull(publisher8, "source8 is null");
        ObjectHelper.requireNonNull(publisher9, "source9 is null");
        return zipArray(Functions.toFunction(function9), false, bufferSize(), publisher, publisher2, publisher3, publisher4, publisher5, publisher6, publisher7, publisher8, publisher9);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> zipArray(Function<? super Object[], ? extends R> function, boolean z, int i, Publisher<? extends T>... publisherArr) {
        if (publisherArr.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        FlowableZip flowableZip = new FlowableZip(publisherArr, null, function, i, z);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableZip);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T, R> Flowable<R> zipIterable(Iterable<? extends Publisher<? extends T>> iterable, Function<? super Object[], ? extends R> function, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        FlowableZip flowableZip = new FlowableZip(null, iterable, function, i, z);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableZip);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Boolean> all(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableAllSingle<T>(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> ambWith(Publisher<? extends T> publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return ambArray(this, publisher);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Boolean> any(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableAnySingle<T>(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingFirst() {
        BlockingFirstSubscriber blockingFirstSubscriber = new BlockingFirstSubscriber();
        subscribe((Subscriber<? super T>) blockingFirstSubscriber);
        T blockingGet = blockingFirstSubscriber.blockingGet();
        if (blockingGet != null) {
            return blockingGet;
        }
        throw new NoSuchElementException();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingFirst(T t) {
        BlockingFirstSubscriber blockingFirstSubscriber = new BlockingFirstSubscriber();
        subscribe((Subscriber<? super T>) blockingFirstSubscriber);
        T blockingGet = blockingFirstSubscriber.blockingGet();
        return blockingGet != null ? blockingGet : t;
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingForEach(Consumer<? super T> consumer) {
        Iterator it = blockingIterable().iterator();
        while (it.hasNext()) {
            try {
                consumer.accept(it.next());
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                ((Disposable) it).dispose();
                throw ExceptionHelper.wrapOrThrow(th);
            }
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Iterable<T> blockingIterable() {
        return blockingIterable(bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Iterable<T> blockingIterable(int i) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        return new BlockingFlowableIterable(this, i);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingLast() {
        BlockingLastSubscriber blockingLastSubscriber = new BlockingLastSubscriber();
        subscribe((Subscriber<? super T>) blockingLastSubscriber);
        T blockingGet = blockingLastSubscriber.blockingGet();
        if (blockingGet != null) {
            return blockingGet;
        }
        throw new NoSuchElementException();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingLast(T t) {
        BlockingLastSubscriber blockingLastSubscriber = new BlockingLastSubscriber();
        subscribe((Subscriber<? super T>) blockingLastSubscriber);
        T blockingGet = blockingLastSubscriber.blockingGet();
        return blockingGet != null ? blockingGet : t;
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Iterable<T> blockingLatest() {
        return new BlockingFlowableLatest(this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Iterable<T> blockingMostRecent(T t) {
        return new BlockingFlowableMostRecent(this, t);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Iterable<T> blockingNext() {
        return new BlockingFlowableNext(this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingSingle() {
        return singleOrError().blockingGet();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final T blockingSingle(T t) {
        return single(t).blockingGet();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Future<T> toFuture() {
        return (Future) subscribeWith(new FutureSubscriber());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe() {
        FlowableBlockingSubscribe.subscribe(this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> consumer) {
        FlowableBlockingSubscribe.subscribe(this, consumer, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2) {
        FlowableBlockingSubscribe.subscribe(this, consumer, consumer2, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action) {
        FlowableBlockingSubscribe.subscribe(this, consumer, consumer2, action);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final void blockingSubscribe(Subscriber<? super T> subscriber) {
        FlowableBlockingSubscribe.subscribe(this, subscriber);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<List<T>> buffer(int i) {
        return buffer(i, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<List<T>> buffer(int i, int i2) {
        return buffer(i, i2, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Flowable<U> buffer(int i, int i2, Callable<U> callable) {
        ObjectHelper.verifyPositive(i, "count");
        ObjectHelper.verifyPositive(i2, "skip");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableBuffer<T>(this, i, i2, callable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Flowable<U> buffer(int i, Callable<U> callable) {
        return buffer(i, i, callable);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<List<T>> buffer(long j, long j2, TimeUnit timeUnit) {
        return buffer(j, j2, timeUnit, Schedulers.computation(), ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<List<T>> buffer(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return buffer(j, j2, timeUnit, scheduler, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final <U extends Collection<? super T>> Flowable<U> buffer(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, Callable<U> callable) {
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        Callable<U> callable2 = callable;
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        FlowableBufferTimed flowableBufferTimed = new FlowableBufferTimed(this, j, j2, timeUnit2, scheduler2, callable2, Integer.MAX_VALUE, false);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableBufferTimed);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<List<T>> buffer(long j, TimeUnit timeUnit) {
        return buffer(j, timeUnit, Schedulers.computation(), Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<List<T>> buffer(long j, TimeUnit timeUnit, int i) {
        return buffer(j, timeUnit, Schedulers.computation(), i);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<List<T>> buffer(long j, TimeUnit timeUnit, Scheduler scheduler, int i) {
        return buffer(j, timeUnit, scheduler, i, ArrayListSupplier.asCallable(), false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final <U extends Collection<? super T>> Flowable<U> buffer(long j, TimeUnit timeUnit, Scheduler scheduler, int i, Callable<U> callable, boolean z) {
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        Callable<U> callable2 = callable;
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "count");
        FlowableBufferTimed flowableBufferTimed = new FlowableBufferTimed(this, j, j, timeUnit2, scheduler2, callable2, i2, z);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableBufferTimed);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<List<T>> buffer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return buffer(j, timeUnit, scheduler, Integer.MAX_VALUE, ArrayListSupplier.asCallable(), false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <TOpening, TClosing> Flowable<List<T>> buffer(Flowable<? extends TOpening> flowable, Function<? super TOpening, ? extends Publisher<? extends TClosing>> function) {
        return buffer(flowable, function, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <TOpening, TClosing, U extends Collection<? super T>> Flowable<U> buffer(Flowable<? extends TOpening> flowable, Function<? super TOpening, ? extends Publisher<? extends TClosing>> function, Callable<U> callable) {
        ObjectHelper.requireNonNull(flowable, "openingIndicator is null");
        ObjectHelper.requireNonNull(function, "closingIndicator is null");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableBufferBoundary<T>(this, flowable, function, callable));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<List<T>> buffer(Publisher<B> publisher) {
        return buffer(publisher, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<List<T>> buffer(Publisher<B> publisher, int i) {
        return buffer(publisher, Functions.createArrayList(i));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B, U extends Collection<? super T>> Flowable<U> buffer(Publisher<B> publisher, Callable<U> callable) {
        ObjectHelper.requireNonNull(publisher, "boundaryIndicator is null");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableBufferExactBoundary<T>(this, publisher, callable));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<List<T>> buffer(Callable<? extends Publisher<B>> callable) {
        return buffer(callable, ArrayListSupplier.asCallable());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B, U extends Collection<? super T>> Flowable<U> buffer(Callable<? extends Publisher<B>> callable, Callable<U> callable2) {
        ObjectHelper.requireNonNull(callable, "boundaryIndicatorSupplier is null");
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableBufferBoundarySupplier<T>(this, callable, callable2));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> cache() {
        return cacheWithInitialCapacity(16);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> cacheWithInitialCapacity(int i) {
        ObjectHelper.verifyPositive(i, "initialCapacity");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCache<T>(this, i));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <U> Flowable<U> cast(Class<U> cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return map(Functions.castFunction(cls));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Single<U> collect(Callable<? extends U> callable, BiConsumer<? super U, ? super T> biConsumer) {
        ObjectHelper.requireNonNull(callable, "initialItemSupplier is null");
        ObjectHelper.requireNonNull(biConsumer, "collector is null");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableCollectSingle<T>(this, callable, biConsumer));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Single<U> collectInto(U u, BiConsumer<? super U, ? super T> biConsumer) {
        ObjectHelper.requireNonNull(u, "initialItem is null");
        return collect(Functions.justCallable(u), biConsumer);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <R> Flowable<R> compose(FlowableTransformer<T, R> flowableTransformer) {
        return fromPublisher(flowableTransformer.apply(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMap(Function<? super T, ? extends Publisher<? extends R>> function) {
        return concatMap(function, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMap(Function<? super T, ? extends Publisher<? extends R>> function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        if (this instanceof ScalarCallable) {
            Object call = ((ScalarCallable) this).call();
            if (call == null) {
                return empty();
            }
            return FlowableScalarXMap.scalarXMap(call, function);
        }
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatMap<T>(this, function, i, ErrorMode.IMMEDIATE));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapDelayError(Function<? super T, ? extends Publisher<? extends R>> function) {
        return concatMapDelayError(function, 2, true);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapDelayError(Function<? super T, ? extends Publisher<? extends R>> function, int i, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        if (this instanceof ScalarCallable) {
            Object call = ((ScalarCallable) this).call();
            if (call == null) {
                return empty();
            }
            return FlowableScalarXMap.scalarXMap(call, function);
        }
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatMap<T>(this, function, i, z ? ErrorMode.END : ErrorMode.BOUNDARY));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapEager(Function<? super T, ? extends Publisher<? extends R>> function) {
        return concatMapEager(function, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapEager(Function<? super T, ? extends Publisher<? extends R>> function, int i, int i2) {
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "prefetch");
        FlowableConcatMapEager flowableConcatMapEager = new FlowableConcatMapEager(this, function, i, i2, ErrorMode.IMMEDIATE);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableConcatMapEager);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapEagerDelayError(Function<? super T, ? extends Publisher<? extends R>> function, boolean z) {
        return concatMapEagerDelayError(function, bufferSize(), bufferSize(), z);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> concatMapEagerDelayError(Function<? super T, ? extends Publisher<? extends R>> function, int i, int i2, boolean z) {
        FlowableConcatMapEager flowableConcatMapEager = new FlowableConcatMapEager(this, function, i, i2, z ? ErrorMode.END : ErrorMode.BOUNDARY);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableConcatMapEager);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> function) {
        return concatMapIterable(function, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFlattenIterable<T>(this, function, i));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> concatWith(Publisher<? extends T> publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return concat((Publisher<? extends T>) this, publisher);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Boolean> contains(Object obj) {
        ObjectHelper.requireNonNull(obj, "item is null");
        return any(Functions.equalsWith(obj));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Long> count() {
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableCountSingle<T>(this));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <U> Flowable<T> debounce(Function<? super T, ? extends Publisher<U>> function) {
        ObjectHelper.requireNonNull(function, "debounceIndicator is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDebounce<T>(this, function));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> debounce(long j, TimeUnit timeUnit) {
        return debounce(j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> debounce(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableDebounceTimed flowableDebounceTimed = new FlowableDebounceTimed(this, j, timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableDebounceTimed);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> defaultIfEmpty(T t) {
        ObjectHelper.requireNonNull(t, "item is null");
        return switchIfEmpty(just(t));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<T> delay(Function<? super T, ? extends Publisher<U>> function) {
        ObjectHelper.requireNonNull(function, "itemDelayIndicator is null");
        return flatMap(FlowableInternalHelper.itemDelay(function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> delay(long j, TimeUnit timeUnit) {
        return delay(j, timeUnit, Schedulers.computation(), false);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> delay(long j, TimeUnit timeUnit, boolean z) {
        return delay(j, timeUnit, Schedulers.computation(), z);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> delay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delay(j, timeUnit, scheduler, false);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> delay(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableDelay flowableDelay = new FlowableDelay(this, Math.max(0, j), timeUnit, scheduler, z);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableDelay);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, V> Flowable<T> delay(Publisher<U> publisher, Function<? super T, ? extends Publisher<V>> function) {
        return delaySubscription(publisher).delay(function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<T> delaySubscription(Publisher<U> publisher) {
        ObjectHelper.requireNonNull(publisher, "subscriptionIndicator is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDelaySubscriptionOther<T>(this, publisher));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> delaySubscription(long j, TimeUnit timeUnit) {
        return delaySubscription(j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> delaySubscription(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delaySubscription(timer(j, timeUnit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <T2> Flowable<T2> dematerialize() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDematerialize<T>(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> distinct() {
        return distinct(Functions.identity(), Functions.createHashSet());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K> Flowable<T> distinct(Function<? super T, K> function) {
        return distinct(function, Functions.createHashSet());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K> Flowable<T> distinct(Function<? super T, K> function, Callable<? extends Collection<? super K>> callable) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(callable, "collectionSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDistinct<T>(this, function, callable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> distinctUntilChanged() {
        return distinctUntilChanged(Functions.identity());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K> Flowable<T> distinctUntilChanged(Function<? super T, K> function) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDistinctUntilChanged<T>(this, function, ObjectHelper.equalsPredicate()));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> distinctUntilChanged(BiPredicate<? super T, ? super T> biPredicate) {
        ObjectHelper.requireNonNull(biPredicate, "comparer is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDistinctUntilChanged<T>(this, Functions.identity(), biPredicate));
    }

    @Experimental
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doFinally(Action action) {
        ObjectHelper.requireNonNull(action, "onFinally is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDoFinally<T>(this, action));
    }

    @Experimental
    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doAfterNext(Consumer<? super T> consumer) {
        ObjectHelper.requireNonNull(consumer, "onAfterNext is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDoAfterNext<T>(this, consumer));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doAfterTerminate(Action action) {
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, action);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnCancel(Action action) {
        return doOnLifecycle(Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, action);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnComplete(Action action) {
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), action, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    private Flowable<T> doOnEach(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action, Action action2) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ObjectHelper.requireNonNull(consumer2, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ObjectHelper.requireNonNull(action2, "onAfterTerminate is null");
        FlowableDoOnEach flowableDoOnEach = new FlowableDoOnEach(this, consumer, consumer2, action, action2);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableDoOnEach);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnEach(Consumer<? super Notification<T>> consumer) {
        ObjectHelper.requireNonNull(consumer, "consumer is null");
        return doOnEach(Functions.notificationOnNext(consumer), Functions.notificationOnError(consumer), Functions.notificationOnComplete(consumer), Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnEach(Subscriber<? super T> subscriber) {
        ObjectHelper.requireNonNull(subscriber, "subscriber is null");
        return doOnEach(FlowableInternalHelper.subscriberOnNext(subscriber), FlowableInternalHelper.subscriberOnError(subscriber), FlowableInternalHelper.subscriberOnComplete(subscriber), Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnError(Consumer<? super Throwable> consumer) {
        return doOnEach(Functions.emptyConsumer(), consumer, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnLifecycle(Consumer<? super Subscription> consumer, LongConsumer longConsumer, Action action) {
        ObjectHelper.requireNonNull(consumer, "onSubscribe is null");
        ObjectHelper.requireNonNull(longConsumer, "onRequest is null");
        ObjectHelper.requireNonNull(action, "onCancel is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDoOnLifecycle<T>(this, consumer, longConsumer, action));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnNext(Consumer<? super T> consumer) {
        return doOnEach(consumer, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnRequest(LongConsumer longConsumer) {
        return doOnLifecycle(Functions.emptyConsumer(), longConsumer, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnSubscribe(Consumer<? super Subscription> consumer) {
        return doOnLifecycle(consumer, Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> doOnTerminate(Action action) {
        return doOnEach(Functions.emptyConsumer(), Functions.actionConsumer(action), action, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Maybe<T> elementAt(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly((Maybe<T>) new FlowableElementAtMaybe<T>(this, j));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("index >= 0 required but it was ");
        sb.append(j);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> elementAt(long j, T t) {
        if (j < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("index >= 0 required but it was ");
            sb.append(j);
            throw new IndexOutOfBoundsException(sb.toString());
        }
        ObjectHelper.requireNonNull(t, "defaultItem is null");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableElementAtSingle<T>(this, j, t));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> elementAtOrError(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly((Single<T>) new FlowableElementAtSingle<T>(this, j, null));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("index >= 0 required but it was ");
        sb.append(j);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> filter(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFilter<T>(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Maybe<T> firstElement() {
        return elementAt(0);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Single<T> first(T t) {
        return elementAt(0, t);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Single<T> firstOrError() {
        return elementAtOrError(0);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> function) {
        return flatMap(function, false, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> function, boolean z) {
        return flatMap(function, z, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> function, int i) {
        return flatMap(function, false, i, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> function, boolean z, int i) {
        return flatMap(function, z, i, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> function, boolean z, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        if (this instanceof ScalarCallable) {
            Object call = ((ScalarCallable) this).call();
            if (call == null) {
                return empty();
            }
            return FlowableScalarXMap.scalarXMap(call, function);
        }
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "bufferSize");
        FlowableFlatMap flowableFlatMap = new FlowableFlatMap(this, function, z, i, i2);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableFlatMap);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> function, Function<? super Throwable, ? extends Publisher<? extends R>> function2, Callable<? extends Publisher<? extends R>> callable) {
        ObjectHelper.requireNonNull(function, "onNextMapper is null");
        ObjectHelper.requireNonNull(function2, "onErrorMapper is null");
        ObjectHelper.requireNonNull(callable, "onCompleteSupplier is null");
        return merge((Publisher<? extends Publisher<? extends T>>) new FlowableMapNotification<Object>(this, function, function2, callable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends R>> function, Function<Throwable, ? extends Publisher<? extends R>> function2, Callable<? extends Publisher<? extends R>> callable, int i) {
        ObjectHelper.requireNonNull(function, "onNextMapper is null");
        ObjectHelper.requireNonNull(function2, "onErrorMapper is null");
        ObjectHelper.requireNonNull(callable, "onCompleteSupplier is null");
        return merge((Publisher<? extends Publisher<? extends T>>) new FlowableMapNotification<Object>(this, function, function2, callable), i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        return flatMap(function, biFunction, false, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction, boolean z) {
        return flatMap(function, biFunction, z, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction, boolean z, int i) {
        return flatMap(function, biFunction, z, i, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction, boolean z, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.requireNonNull(biFunction, "combiner is null");
        return flatMap(FlowableInternalHelper.flatMapWithCombiner(function, biFunction), z, i, i2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> flatMap(Function<? super T, ? extends Publisher<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction, int i) {
        return flatMap(function, biFunction, false, i, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> function) {
        return flatMapCompletable(function, false, Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> function, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        return RxJavaPlugins.onAssembly((Completable) new FlowableFlatMapCompletableCompletable(this, function, z, i));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> function) {
        return flatMapIterable(function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFlattenIterable<T>(this, function, i));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, V> Flowable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> function, BiFunction<? super T, ? super U, ? extends V> biFunction) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.requireNonNull(biFunction, "resultSelector is null");
        return flatMap(FlowableInternalHelper.flatMapIntoIterable(function), biFunction, false, bufferSize(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, V> Flowable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> function, BiFunction<? super T, ? super U, ? extends V> biFunction, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.requireNonNull(biFunction, "resultSelector is null");
        return flatMap(FlowableInternalHelper.flatMapIntoIterable(function), biFunction, false, bufferSize(), i);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> function) {
        return flatMapMaybe(function, false, Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> function, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFlatMapMaybe<T>(this, function, z, i));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> function) {
        return flatMapSingle(function, false, Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> function, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableFlatMapSingle<T>(this, function, z, i));
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Disposable forEach(Consumer<? super T> consumer) {
        return subscribe(consumer);
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> predicate) {
        return forEachWhile(predicate, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> predicate, Consumer<? super Throwable> consumer) {
        return forEachWhile(predicate, consumer, Functions.EMPTY_ACTION);
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> predicate, Consumer<? super Throwable> consumer, Action action) {
        ObjectHelper.requireNonNull(predicate, "onNext is null");
        ObjectHelper.requireNonNull(consumer, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ForEachWhileSubscriber forEachWhileSubscriber = new ForEachWhileSubscriber(predicate, consumer, action);
        subscribe((Subscriber<? super T>) forEachWhileSubscriber);
        return forEachWhileSubscriber;
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K> Flowable<GroupedFlowable<K, T>> groupBy(Function<? super T, ? extends K> function) {
        return groupBy(function, Functions.identity(), false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K> Flowable<GroupedFlowable<K, T>> groupBy(Function<? super T, ? extends K> function, boolean z) {
        return groupBy(function, Functions.identity(), z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        return groupBy(function, function2, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, boolean z) {
        return groupBy(function, function2, z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <K, V> Flowable<GroupedFlowable<K, V>> groupBy(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        FlowableGroupBy flowableGroupBy = new FlowableGroupBy(this, function, function2, i, z);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableGroupBy);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <TRight, TLeftEnd, TRightEnd, R> Flowable<R> groupJoin(Publisher<? extends TRight> publisher, Function<? super T, ? extends Publisher<TLeftEnd>> function, Function<? super TRight, ? extends Publisher<TRightEnd>> function2, BiFunction<? super T, ? super Flowable<TRight>, ? extends R> biFunction) {
        FlowableGroupJoin flowableGroupJoin = new FlowableGroupJoin(this, publisher, function, function2, biFunction);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableGroupJoin);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> hide() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableHide<T>(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Completable ignoreElements() {
        return RxJavaPlugins.onAssembly((Completable) new FlowableIgnoreElementsCompletable(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<Boolean> isEmpty() {
        return all(Functions.alwaysFalse());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <TRight, TLeftEnd, TRightEnd, R> Flowable<R> join(Publisher<? extends TRight> publisher, Function<? super T, ? extends Publisher<TLeftEnd>> function, Function<? super TRight, ? extends Publisher<TRightEnd>> function2, BiFunction<? super T, ? super TRight, ? extends R> biFunction) {
        FlowableJoin flowableJoin = new FlowableJoin(this, publisher, function, function2, biFunction);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableJoin);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Maybe<T> lastElement() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new FlowableLastMaybe<T>(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> last(T t) {
        ObjectHelper.requireNonNull(t, "defaultItem");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableLastSingle<T>(this, t));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> lastOrError() {
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableLastSingle<T>(this, null));
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> lift(FlowableOperator<? extends R, ? super T> flowableOperator) {
        ObjectHelper.requireNonNull(flowableOperator, "lifter is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableLift<T>(this, flowableOperator));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <R> Flowable<R> map(Function<? super T, ? extends R> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableMap<T>(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<Notification<T>> materialize() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableMaterialize<T>(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> mergeWith(Publisher<? extends T> publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return merge((Publisher<? extends T>) this, publisher);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> observeOn(Scheduler scheduler) {
        return observeOn(scheduler, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> observeOn(Scheduler scheduler, boolean z) {
        return observeOn(scheduler, z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> observeOn(Scheduler scheduler, boolean z, int i) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableObserveOn<T>(this, scheduler, z, i));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <U> Flowable<U> ofType(Class<U> cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return filter(Functions.isInstanceOf(cls)).cast(cls);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer() {
        return onBackpressureBuffer(bufferSize(), false, true);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(boolean z) {
        return onBackpressureBuffer(bufferSize(), z, true);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int i) {
        return onBackpressureBuffer(i, false, false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int i, boolean z) {
        return onBackpressureBuffer(i, z, false);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int i, boolean z, boolean z2) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        FlowableOnBackpressureBuffer flowableOnBackpressureBuffer = new FlowableOnBackpressureBuffer(this, i, z2, z, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableOnBackpressureBuffer);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int i, boolean z, boolean z2, Action action) {
        ObjectHelper.requireNonNull(action, "onOverflow is null");
        FlowableOnBackpressureBuffer flowableOnBackpressureBuffer = new FlowableOnBackpressureBuffer(this, i, z2, z, action);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableOnBackpressureBuffer);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(int i, Action action) {
        return onBackpressureBuffer(i, false, false, action);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureBuffer(long j, Action action, BackpressureOverflowStrategy backpressureOverflowStrategy) {
        ObjectHelper.requireNonNull(backpressureOverflowStrategy, "strategy is null");
        ObjectHelper.verifyPositive(j, "capacity");
        FlowableOnBackpressureBufferStrategy flowableOnBackpressureBufferStrategy = new FlowableOnBackpressureBufferStrategy(this, j, action, backpressureOverflowStrategy);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableOnBackpressureBufferStrategy);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureDrop() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableOnBackpressureDrop<T>(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureDrop(Consumer<? super T> consumer) {
        ObjectHelper.requireNonNull(consumer, "onDrop is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableOnBackpressureDrop<T>(this, consumer));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> onBackpressureLatest() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableOnBackpressureLatest<T>(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onErrorResumeNext(Function<? super Throwable, ? extends Publisher<? extends T>> function) {
        ObjectHelper.requireNonNull(function, "resumeFunction is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableOnErrorNext<T>(this, function, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onErrorResumeNext(Publisher<? extends T> publisher) {
        ObjectHelper.requireNonNull(publisher, "next is null");
        return onErrorResumeNext(Functions.justFunction(publisher));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onErrorReturn(Function<? super Throwable, ? extends T> function) {
        ObjectHelper.requireNonNull(function, "valueSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableOnErrorReturn<T>(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onErrorReturnItem(T t) {
        ObjectHelper.requireNonNull(t, "item is null");
        return onErrorReturn(Functions.justFunction(t));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> onExceptionResumeNext(Publisher<? extends T> publisher) {
        ObjectHelper.requireNonNull(publisher, "next is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableOnErrorNext<T>(this, Functions.justFunction(publisher), true));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> onTerminateDetach() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDetach<T>(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final ConnectableFlowable<T> publish() {
        return publish(bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> publish(Function<? super Flowable<T>, ? extends Publisher<R>> function) {
        return publish(function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> publish(Function<? super Flowable<T>, ? extends Publisher<? extends R>> function, int i) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowablePublishMulticast<T>(this, function, i, false));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final ConnectableFlowable<T> publish(int i) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        return FlowablePublish.create(this, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> rebatchRequests(int i) {
        return observeOn(ImmediateThinScheduler.INSTANCE, true, i);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Maybe<T> reduce(BiFunction<T, T, T> biFunction) {
        ObjectHelper.requireNonNull(biFunction, "reducer is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new FlowableReduceMaybe<T>(this, biFunction));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Single<R> reduce(R r, BiFunction<R, ? super T, R> biFunction) {
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableSingleSingle<T>(scan(r, biFunction).takeLast(1), null));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <R> Single<R> reduceWith(Callable<R> callable, BiFunction<R, ? super T, R> biFunction) {
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableSingleSingle<T>(scanWith(callable, biFunction).takeLast(1), null));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat() {
        return repeat(LongCompanionObject.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat(long j) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("times >= 0 required but it was ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        } else if (i == 0) {
            return empty();
        } else {
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRepeat<T>(this, j));
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatUntil(BooleanSupplier booleanSupplier) {
        ObjectHelper.requireNonNull(booleanSupplier, "stop is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRepeatUntil<T>(this, booleanSupplier));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<?>> function) {
        ObjectHelper.requireNonNull(function, "handler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRepeatWhen<T>(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final ConnectableFlowable<T> replay() {
        return FlowableReplay.createFrom(this);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> function) {
        ObjectHelper.requireNonNull(function, "selector is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this), function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> function, int i) {
        ObjectHelper.requireNonNull(function, "selector is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, i), function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> function, int i, long j, TimeUnit timeUnit) {
        return replay(function, i, j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> function, int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObjectHelper.requireNonNull(function, "selector is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, i, j, timeUnit, scheduler), function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> function, int i, Scheduler scheduler) {
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, i), FlowableInternalHelper.replayFunction(function, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> function, long j, TimeUnit timeUnit) {
        return replay(function, j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> function, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this, j, timeUnit, scheduler), function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final <R> Flowable<R> replay(Function<? super Flowable<T>, ? extends Publisher<R>> function, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.multicastSelector(FlowableInternalHelper.replayCallable(this), FlowableInternalHelper.replayFunction(function, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final ConnectableFlowable<T> replay(int i) {
        return FlowableReplay.create((Publisher<T>) this, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final ConnectableFlowable<T> replay(int i, long j, TimeUnit timeUnit) {
        return replay(i, j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final ConnectableFlowable<T> replay(int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return FlowableReplay.create(this, j, timeUnit, scheduler, i);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final ConnectableFlowable<T> replay(int i, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.observeOn(replay(i), scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final ConnectableFlowable<T> replay(long j, TimeUnit timeUnit) {
        return replay(j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final ConnectableFlowable<T> replay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.create(this, j, timeUnit, scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final ConnectableFlowable<T> replay(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return FlowableReplay.observeOn(replay(), scheduler);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry() {
        return retry(LongCompanionObject.MAX_VALUE, Functions.alwaysTrue());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry(BiPredicate<? super Integer, ? super Throwable> biPredicate) {
        ObjectHelper.requireNonNull(biPredicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRetryBiPredicate<T>(this, biPredicate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry(long j) {
        return retry(j, Functions.alwaysTrue());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry(long j, Predicate<? super Throwable> predicate) {
        if (j < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("times >= 0 required but it was ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        }
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRetryPredicate<T>(this, j, predicate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retry(Predicate<? super Throwable> predicate) {
        return retry(LongCompanionObject.MAX_VALUE, predicate);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retryUntil(BooleanSupplier booleanSupplier) {
        ObjectHelper.requireNonNull(booleanSupplier, "stop is null");
        return retry(LongCompanionObject.MAX_VALUE, Functions.predicateReverseFor(booleanSupplier));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<?>> function) {
        ObjectHelper.requireNonNull(function, "handler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableRetryWhen<T>(this, function));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final void safeSubscribe(Subscriber<? super T> subscriber) {
        ObjectHelper.requireNonNull(subscriber, "s is null");
        if (subscriber instanceof SafeSubscriber) {
            subscribe(subscriber);
        } else {
            subscribe((Subscriber<? super T>) new SafeSubscriber<Object>(subscriber));
        }
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> sample(long j, TimeUnit timeUnit) {
        return sample(j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> sample(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableSampleTimed flowableSampleTimed = new FlowableSampleTimed(this, j, timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableSampleTimed);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <U> Flowable<T> sample(Publisher<U> publisher) {
        ObjectHelper.requireNonNull(publisher, "sampler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSamplePublisher<T>(this, publisher));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> scan(BiFunction<T, T, T> biFunction) {
        ObjectHelper.requireNonNull(biFunction, "accumulator is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableScan<T>(this, biFunction));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> scan(R r, BiFunction<R, ? super T, R> biFunction) {
        ObjectHelper.requireNonNull(r, "seed is null");
        return scanWith(Functions.justCallable(r), biFunction);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> scanWith(Callable<R> callable, BiFunction<R, ? super T, R> biFunction) {
        ObjectHelper.requireNonNull(callable, "seedSupplier is null");
        ObjectHelper.requireNonNull(biFunction, "accumulator is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableScanSeed<T>(this, callable, biFunction));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> serialize() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSerialized<T>(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> share() {
        return publish().refCount();
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Maybe<T> singleElement() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new FlowableSingleMaybe<T>(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> single(T t) {
        ObjectHelper.requireNonNull(t, "defaultItem is null");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableSingleSingle<T>(this, t));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<T> singleOrError() {
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableSingleSingle<T>(this, null));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> skip(long j) {
        if (j <= 0) {
            return RxJavaPlugins.onAssembly(this);
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSkip<T>(this, j));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> skip(long j, TimeUnit timeUnit) {
        return skipUntil(timer(j, timeUnit));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> skip(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return skipUntil(timer(j, timeUnit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> skipLast(int i) {
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(i);
            throw new IndexOutOfBoundsException(sb.toString());
        } else if (i == 0) {
            return RxJavaPlugins.onAssembly(this);
        } else {
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSkipLast<T>(this, i));
        }
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> skipLast(long j, TimeUnit timeUnit) {
        return skipLast(j, timeUnit, Schedulers.computation(), false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Flowable<T> skipLast(long j, TimeUnit timeUnit, boolean z) {
        return skipLast(j, timeUnit, Schedulers.computation(), z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("custom")
    public final Flowable<T> skipLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return skipLast(j, timeUnit, scheduler, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("custom")
    public final Flowable<T> skipLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        return skipLast(j, timeUnit, scheduler, z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("custom")
    public final Flowable<T> skipLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        FlowableSkipLastTimed flowableSkipLastTimed = new FlowableSkipLastTimed(this, j, timeUnit, scheduler, i << 1, z);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableSkipLastTimed);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<T> skipUntil(Publisher<U> publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSkipUntil<T>(this, publisher));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> skipWhile(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSkipWhile<T>(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> sorted() {
        return toList().toFlowable().map(Functions.listSorter(Functions.naturalComparator())).flatMapIterable(Functions.identity());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> sorted(Comparator<? super T> comparator) {
        return toList().toFlowable().map(Functions.listSorter(comparator)).flatMapIterable(Functions.identity());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> startWith(Iterable<? extends T> iterable) {
        return concatArray(fromIterable(iterable), this);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> startWith(Publisher<? extends T> publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return concatArray(publisher, this);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> startWith(T t) {
        ObjectHelper.requireNonNull(t, "item is null");
        return concatArray(just(t), this);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> startWithArray(T... tArr) {
        Flowable fromArray = fromArray(tArr);
        if (fromArray == empty()) {
            return RxJavaPlugins.onAssembly(this);
        }
        return concatArray(fromArray, this);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION, RequestMax.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> consumer) {
        return subscribe(consumer, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION, RequestMax.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2) {
        return subscribe(consumer, consumer2, Functions.EMPTY_ACTION, RequestMax.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action) {
        return subscribe(consumer, consumer2, action, RequestMax.INSTANCE);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action, Consumer<? super Subscription> consumer3) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ObjectHelper.requireNonNull(consumer2, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ObjectHelper.requireNonNull(consumer3, "onSubscribe is null");
        LambdaSubscriber lambdaSubscriber = new LambdaSubscriber(consumer, consumer2, action, consumer3);
        subscribe((Subscriber<? super T>) lambdaSubscriber);
        return lambdaSubscriber;
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final void subscribe(Subscriber<? super T> subscriber) {
        ObjectHelper.requireNonNull(subscriber, "s is null");
        try {
            Subscriber onSubscribe = RxJavaPlugins.onSubscribe(this, subscriber);
            ObjectHelper.requireNonNull(onSubscribe, "Plugin returned null Subscriber");
            subscribeActual(onSubscribe);
        } catch (NullPointerException e) {
            throw e;
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            RxJavaPlugins.onError(th);
            NullPointerException nullPointerException = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
            nullPointerException.initCause(th);
            throw nullPointerException;
        }
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final <E extends Subscriber<? super T>> E subscribeWith(E e) {
        subscribe((Subscriber<? super T>) e);
        return e;
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("custom")
    public final Flowable<T> subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSubscribeOn<T>(this, scheduler, this instanceof FlowableCreate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> switchIfEmpty(Publisher<? extends T> publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSwitchIfEmpty<T>(this, publisher));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMap(Function<? super T, ? extends Publisher<? extends R>> function) {
        return switchMap(function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMap(Function<? super T, ? extends Publisher<? extends R>> function, int i) {
        return switchMap0(function, i, false);
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMapDelayError(Function<? super T, ? extends Publisher<? extends R>> function) {
        return switchMapDelayError(function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> switchMapDelayError(Function<? super T, ? extends Publisher<? extends R>> function, int i) {
        return switchMap0(function, i, true);
    }

    /* access modifiers changed from: 0000 */
    public <R> Flowable<R> switchMap0(Function<? super T, ? extends Publisher<? extends R>> function, int i, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        if (this instanceof ScalarCallable) {
            Object call = ((ScalarCallable) this).call();
            if (call == null) {
                return empty();
            }
            return FlowableScalarXMap.scalarXMap(call, function);
        }
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableSwitchMap<T>(this, function, i, z));
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> take(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTake<T>(this, j));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("count >= 0 required but it was ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> take(long j, TimeUnit timeUnit) {
        return takeUntil((Publisher<U>) timer(j, timeUnit));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("custom")
    public final Flowable<T> take(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return takeUntil((Publisher<U>) timer(j, timeUnit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> takeLast(int i) {
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(i);
            throw new IndexOutOfBoundsException(sb.toString());
        } else if (i == 0) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableIgnoreElements<T>(this));
        } else {
            if (i == 1) {
                return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTakeLastOne<T>(this));
            }
            return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTakeLast<T>(this, i));
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> takeLast(long j, long j2, TimeUnit timeUnit) {
        return takeLast(j, j2, timeUnit, Schedulers.computation(), false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return takeLast(j, j2, timeUnit, scheduler, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
        long j3 = j;
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "bufferSize");
        if (j3 < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(j3);
            throw new IndexOutOfBoundsException(sb.toString());
        }
        FlowableTakeLastTimed flowableTakeLastTimed = new FlowableTakeLastTimed(this, j3, j2, timeUnit2, scheduler2, i2, z);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableTakeLastTimed);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> takeLast(long j, TimeUnit timeUnit) {
        return takeLast(j, timeUnit, Schedulers.computation(), false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> takeLast(long j, TimeUnit timeUnit, boolean z) {
        return takeLast(j, timeUnit, Schedulers.computation(), z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return takeLast(j, timeUnit, scheduler, false, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        return takeLast(j, timeUnit, scheduler, z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> takeLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
        return takeLast(LongCompanionObject.MAX_VALUE, j, timeUnit, scheduler, z, i);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> takeUntil(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "stopPredicate is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTakeUntilPredicate<T>(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <U> Flowable<T> takeUntil(Publisher<U> publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTakeUntil<T>(this, publisher));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<T> takeWhile(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTakeWhile<T>(this, predicate));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> throttleFirst(long j, TimeUnit timeUnit) {
        return throttleFirst(j, timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> throttleFirst(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableThrottleFirstTimed flowableThrottleFirstTimed = new FlowableThrottleFirstTimed(this, j, timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableThrottleFirstTimed);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> throttleLast(long j, TimeUnit timeUnit) {
        return sample(j, timeUnit);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> throttleLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return sample(j, timeUnit, scheduler);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> throttleWithTimeout(long j, TimeUnit timeUnit) {
        return debounce(j, timeUnit);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<T> throttleWithTimeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return debounce(j, timeUnit, scheduler);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timeInterval() {
        return timeInterval(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timeInterval(Scheduler scheduler) {
        return timeInterval(TimeUnit.MILLISECONDS, scheduler);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timeInterval(TimeUnit timeUnit) {
        return timeInterval(timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timeInterval(TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTimeInterval<T>(this, timeUnit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <V> Flowable<T> timeout(Function<? super T, ? extends Publisher<V>> function) {
        return timeout0(null, function, null);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <V> Flowable<T> timeout(Function<? super T, ? extends Publisher<V>> function, Flowable<? extends T> flowable) {
        ObjectHelper.requireNonNull(flowable, "other is null");
        return timeout0(null, function, flowable);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> timeout(long j, TimeUnit timeUnit) {
        return timeout0(j, timeUnit, null, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<T> timeout(long j, TimeUnit timeUnit, Flowable<? extends T> flowable) {
        ObjectHelper.requireNonNull(flowable, "other is null");
        return timeout0(j, timeUnit, flowable, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("custom")
    public final Flowable<T> timeout(long j, TimeUnit timeUnit, Scheduler scheduler, Flowable<? extends T> flowable) {
        ObjectHelper.requireNonNull(flowable, "other is null");
        return timeout0(j, timeUnit, flowable, scheduler);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("custom")
    public final Flowable<T> timeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout0(j, timeUnit, null, scheduler);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <U, V> Flowable<T> timeout(Publisher<U> publisher, Function<? super T, ? extends Publisher<V>> function) {
        ObjectHelper.requireNonNull(publisher, "firstTimeoutIndicator is null");
        return timeout0(publisher, function, null);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, V> Flowable<T> timeout(Publisher<U> publisher, Function<? super T, ? extends Publisher<V>> function, Publisher<? extends T> publisher2) {
        ObjectHelper.requireNonNull(publisher, "firstTimeoutSelector is null");
        ObjectHelper.requireNonNull(publisher2, "other is null");
        return timeout0(publisher, function, publisher2);
    }

    private Flowable<T> timeout0(long j, TimeUnit timeUnit, Flowable<? extends T> flowable, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "timeUnit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        FlowableTimeoutTimed flowableTimeoutTimed = new FlowableTimeoutTimed(this, j, timeUnit, scheduler, flowable);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableTimeoutTimed);
    }

    private <U, V> Flowable<T> timeout0(Publisher<U> publisher, Function<? super T, ? extends Publisher<V>> function, Publisher<? extends T> publisher2) {
        ObjectHelper.requireNonNull(function, "itemTimeoutIndicator is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableTimeout<T>(this, publisher, function, publisher2));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timestamp() {
        return timestamp(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timestamp(Scheduler scheduler) {
        return timestamp(TimeUnit.MILLISECONDS, scheduler);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timestamp(TimeUnit timeUnit) {
        return timestamp(timeUnit, Schedulers.computation());
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final Flowable<Timed<T>> timestamp(TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return map(Functions.timestampWith(timeUnit, scheduler));
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    /* renamed from: to */
    public final <R> R mo12695to(Function<? super Flowable<T>, R> function) {
        try {
            return function.apply(this);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toList() {
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableToListSingle<T>(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toList(int i) {
        ObjectHelper.verifyPositive(i, "capacityHint");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableToListSingle<T>(this, Functions.createArrayList(i)));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Single<U> toList(Callable<U> callable) {
        ObjectHelper.requireNonNull(callable, "collectionSupplier is null");
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableToListSingle<T>(this, callable));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K> Single<Map<K, T>> toMap(Function<? super T, ? extends K> function) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeySelector(function));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeyValueSelector(function, function2));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, Callable<? extends Map<K, V>> callable) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        return collect(callable, Functions.toMapKeyValueSelector(function, function2));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K> Single<Map<K, Collection<T>>> toMultimap(Function<? super T, ? extends K> function) {
        return toMultimap(function, Functions.identity(), HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        return toMultimap(function, function2, HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, Callable<? extends Map<K, Collection<V>>> callable, Function<? super K, ? extends Collection<? super V>> function3) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        ObjectHelper.requireNonNull(callable, "mapSupplier is null");
        ObjectHelper.requireNonNull(function3, "collectionFactory is null");
        return collect(callable, Functions.toMultimapKeyValueSelector(function, function2, function3));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, Callable<Map<K, Collection<V>>> callable) {
        return toMultimap(function, function2, callable, ArrayListSupplier.asFunction());
    }

    @BackpressureSupport(BackpressureKind.NONE)
    @SchedulerSupport("none")
    public final Observable<T> toObservable() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFromPublisher<T>(this));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList() {
        return toSortedList(Functions.naturalComparator());
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(Comparator<? super T> comparator) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList().map(Functions.listSorter(comparator));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(Comparator<? super T> comparator, int i) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList(i).map(Functions.listSorter(comparator));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(int i) {
        return toSortedList(Functions.naturalComparator(), i);
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("custom")
    public final Flowable<T> unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableUnsubscribeOn<T>(this, scheduler));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<Flowable<T>> window(long j) {
        return window(j, j, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<Flowable<T>> window(long j, long j2) {
        return window(j, j2, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<Flowable<T>> window(long j, long j2, int i) {
        ObjectHelper.verifyPositive(j2, "skip");
        ObjectHelper.verifyPositive(j, "count");
        ObjectHelper.verifyPositive(i, "bufferSize");
        FlowableWindow flowableWindow = new FlowableWindow(this, j, j2, i);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableWindow);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<Flowable<T>> window(long j, long j2, TimeUnit timeUnit) {
        return window(j, j2, timeUnit, Schedulers.computation(), bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return window(j, j2, timeUnit, scheduler, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, int i) {
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "bufferSize");
        long j3 = j;
        ObjectHelper.verifyPositive(j3, "timespan");
        long j4 = j2;
        ObjectHelper.verifyPositive(j4, "timeskip");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        FlowableWindowTimed flowableWindowTimed = new FlowableWindowTimed(this, j3, j4, timeUnit2, scheduler2, LongCompanionObject.MAX_VALUE, i2, false);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableWindowTimed);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<Flowable<T>> window(long j, TimeUnit timeUnit) {
        return window(j, timeUnit, Schedulers.computation(), (long) LongCompanionObject.MAX_VALUE, false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<Flowable<T>> window(long j, TimeUnit timeUnit, long j2) {
        return window(j, timeUnit, Schedulers.computation(), j2, false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("io.reactivex:computation")
    public final Flowable<Flowable<T>> window(long j, TimeUnit timeUnit, long j2, boolean z) {
        return window(j, timeUnit, Schedulers.computation(), j2, z);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return window(j, timeUnit, scheduler, (long) LongCompanionObject.MAX_VALUE, false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2) {
        return window(j, timeUnit, scheduler, j2, false);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2, boolean z) {
        return window(j, timeUnit, scheduler, j2, z, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("custom")
    public final Flowable<Flowable<T>> window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2, boolean z, int i) {
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "bufferSize");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        long j3 = j2;
        ObjectHelper.verifyPositive(j3, "count");
        FlowableWindowTimed flowableWindowTimed = new FlowableWindowTimed(this, j, j, timeUnit2, scheduler2, j3, i2, z);
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableWindowTimed);
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<Flowable<T>> window(Publisher<B> publisher) {
        return window(publisher, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<Flowable<T>> window(Publisher<B> publisher, int i) {
        ObjectHelper.requireNonNull(publisher, "boundaryIndicator is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableWindowBoundary<T>(this, publisher, i));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <U, V> Flowable<Flowable<T>> window(Publisher<U> publisher, Function<? super U, ? extends Publisher<V>> function) {
        return window(publisher, function, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <U, V> Flowable<Flowable<T>> window(Publisher<U> publisher, Function<? super U, ? extends Publisher<V>> function, int i) {
        ObjectHelper.requireNonNull(publisher, "openingIndicator is null");
        ObjectHelper.requireNonNull(function, "closingIndicator is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableWindowBoundarySelector<T>(this, publisher, function, i));
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<Flowable<T>> window(Callable<? extends Publisher<B>> callable) {
        return window(callable, bufferSize());
    }

    @BackpressureSupport(BackpressureKind.ERROR)
    @SchedulerSupport("none")
    public final <B> Flowable<Flowable<T>> window(Callable<? extends Publisher<B>> callable, int i) {
        ObjectHelper.requireNonNull(callable, "boundaryIndicatorSupplier is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableWindowBoundarySupplier<T>(this, callable, i));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> withLatestFrom(Publisher<? extends U> publisher, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        ObjectHelper.requireNonNull(biFunction, "combiner is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableWithLatestFrom<T>(this, biFunction, publisher));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <T1, T2, R> Flowable<R> withLatestFrom(Publisher<T1> publisher, Publisher<T2> publisher2, Function3<? super T, ? super T1, ? super T2, R> function3) {
        return withLatestFrom((Publisher<?>[]) new Publisher[]{publisher, publisher2}, Functions.toFunction(function3));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <T1, T2, T3, R> Flowable<R> withLatestFrom(Publisher<T1> publisher, Publisher<T2> publisher2, Publisher<T3> publisher3, Function4<? super T, ? super T1, ? super T2, ? super T3, R> function4) {
        return withLatestFrom((Publisher<?>[]) new Publisher[]{publisher, publisher2, publisher3}, Functions.toFunction(function4));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <T1, T2, T3, T4, R> Flowable<R> withLatestFrom(Publisher<T1> publisher, Publisher<T2> publisher2, Publisher<T3> publisher3, Publisher<T4> publisher4, Function5<? super T, ? super T1, ? super T2, ? super T3, ? super T4, R> function5) {
        return withLatestFrom((Publisher<?>[]) new Publisher[]{publisher, publisher2, publisher3, publisher4}, Functions.toFunction(function5));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <R> Flowable<R> withLatestFrom(Publisher<?>[] publisherArr, Function<? super Object[], R> function) {
        ObjectHelper.requireNonNull(publisherArr, "others is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableWithLatestFromMany<T>((Publisher<T>) this, publisherArr, function));
    }

    @BackpressureSupport(BackpressureKind.PASS_THROUGH)
    @SchedulerSupport("none")
    public final <R> Flowable<R> withLatestFrom(Iterable<? extends Publisher<?>> iterable, Function<? super Object[], R> function) {
        ObjectHelper.requireNonNull(iterable, "others is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableWithLatestFromMany<T>((Publisher<T>) this, iterable, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> zipWith(Iterable<U> iterable, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(iterable, "other is null");
        ObjectHelper.requireNonNull(biFunction, "zipper is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableZipIterable<T>(this, iterable, biFunction));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> zipWith(Publisher<? extends U> publisher, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return zip(this, publisher, biFunction);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> zipWith(Publisher<? extends U> publisher, BiFunction<? super T, ? super U, ? extends R> biFunction, boolean z) {
        return zip((Publisher<? extends T1>) this, publisher, biFunction, z);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U, R> Flowable<R> zipWith(Publisher<? extends U> publisher, BiFunction<? super T, ? super U, ? extends R> biFunction, boolean z, int i) {
        return zip((Publisher<? extends T1>) this, publisher, biFunction, z, i);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final TestSubscriber<T> test() {
        TestSubscriber<T> testSubscriber = new TestSubscriber<>();
        subscribe((Subscriber<? super T>) testSubscriber);
        return testSubscriber;
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final TestSubscriber<T> test(long j) {
        TestSubscriber<T> testSubscriber = new TestSubscriber<>(j);
        subscribe((Subscriber<? super T>) testSubscriber);
        return testSubscriber;
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final TestSubscriber<T> test(long j, boolean z) {
        TestSubscriber<T> testSubscriber = new TestSubscriber<>(j);
        if (z) {
            testSubscriber.cancel();
        }
        subscribe((Subscriber<? super T>) testSubscriber);
        return testSubscriber;
    }
}
