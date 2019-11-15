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
import p016io.reactivex.annotations.BackpressureKind;
import p016io.reactivex.annotations.BackpressureSupport;
import p016io.reactivex.annotations.Experimental;
import p016io.reactivex.annotations.SchedulerSupport;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
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
import p016io.reactivex.functions.Predicate;
import p016io.reactivex.internal.functions.Functions;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.ScalarCallable;
import p016io.reactivex.internal.observers.BlockingFirstObserver;
import p016io.reactivex.internal.observers.BlockingLastObserver;
import p016io.reactivex.internal.observers.ForEachWhileObserver;
import p016io.reactivex.internal.observers.FutureObserver;
import p016io.reactivex.internal.observers.LambdaObserver;
import p016io.reactivex.internal.operators.flowable.FlowableFromObservable;
import p016io.reactivex.internal.operators.flowable.FlowableOnBackpressureError;
import p016io.reactivex.internal.operators.observable.BlockingObservableIterable;
import p016io.reactivex.internal.operators.observable.BlockingObservableLatest;
import p016io.reactivex.internal.operators.observable.BlockingObservableMostRecent;
import p016io.reactivex.internal.operators.observable.BlockingObservableNext;
import p016io.reactivex.internal.operators.observable.ObservableAllSingle;
import p016io.reactivex.internal.operators.observable.ObservableAmb;
import p016io.reactivex.internal.operators.observable.ObservableAnySingle;
import p016io.reactivex.internal.operators.observable.ObservableBlockingSubscribe;
import p016io.reactivex.internal.operators.observable.ObservableBuffer;
import p016io.reactivex.internal.operators.observable.ObservableBufferBoundary;
import p016io.reactivex.internal.operators.observable.ObservableBufferBoundarySupplier;
import p016io.reactivex.internal.operators.observable.ObservableBufferExactBoundary;
import p016io.reactivex.internal.operators.observable.ObservableBufferTimed;
import p016io.reactivex.internal.operators.observable.ObservableCache;
import p016io.reactivex.internal.operators.observable.ObservableCollectSingle;
import p016io.reactivex.internal.operators.observable.ObservableCombineLatest;
import p016io.reactivex.internal.operators.observable.ObservableConcatMap;
import p016io.reactivex.internal.operators.observable.ObservableConcatMapEager;
import p016io.reactivex.internal.operators.observable.ObservableCountSingle;
import p016io.reactivex.internal.operators.observable.ObservableCreate;
import p016io.reactivex.internal.operators.observable.ObservableDebounce;
import p016io.reactivex.internal.operators.observable.ObservableDebounceTimed;
import p016io.reactivex.internal.operators.observable.ObservableDefer;
import p016io.reactivex.internal.operators.observable.ObservableDelay;
import p016io.reactivex.internal.operators.observable.ObservableDelaySubscriptionOther;
import p016io.reactivex.internal.operators.observable.ObservableDematerialize;
import p016io.reactivex.internal.operators.observable.ObservableDetach;
import p016io.reactivex.internal.operators.observable.ObservableDistinct;
import p016io.reactivex.internal.operators.observable.ObservableDistinctUntilChanged;
import p016io.reactivex.internal.operators.observable.ObservableDoAfterNext;
import p016io.reactivex.internal.operators.observable.ObservableDoFinally;
import p016io.reactivex.internal.operators.observable.ObservableDoOnEach;
import p016io.reactivex.internal.operators.observable.ObservableDoOnLifecycle;
import p016io.reactivex.internal.operators.observable.ObservableElementAtMaybe;
import p016io.reactivex.internal.operators.observable.ObservableElementAtSingle;
import p016io.reactivex.internal.operators.observable.ObservableEmpty;
import p016io.reactivex.internal.operators.observable.ObservableError;
import p016io.reactivex.internal.operators.observable.ObservableFilter;
import p016io.reactivex.internal.operators.observable.ObservableFlatMap;
import p016io.reactivex.internal.operators.observable.ObservableFlatMapCompletableCompletable;
import p016io.reactivex.internal.operators.observable.ObservableFlatMapMaybe;
import p016io.reactivex.internal.operators.observable.ObservableFlatMapSingle;
import p016io.reactivex.internal.operators.observable.ObservableFlattenIterable;
import p016io.reactivex.internal.operators.observable.ObservableFromArray;
import p016io.reactivex.internal.operators.observable.ObservableFromCallable;
import p016io.reactivex.internal.operators.observable.ObservableFromFuture;
import p016io.reactivex.internal.operators.observable.ObservableFromIterable;
import p016io.reactivex.internal.operators.observable.ObservableFromPublisher;
import p016io.reactivex.internal.operators.observable.ObservableFromUnsafeSource;
import p016io.reactivex.internal.operators.observable.ObservableGenerate;
import p016io.reactivex.internal.operators.observable.ObservableGroupBy;
import p016io.reactivex.internal.operators.observable.ObservableGroupJoin;
import p016io.reactivex.internal.operators.observable.ObservableHide;
import p016io.reactivex.internal.operators.observable.ObservableIgnoreElements;
import p016io.reactivex.internal.operators.observable.ObservableIgnoreElementsCompletable;
import p016io.reactivex.internal.operators.observable.ObservableInternalHelper;
import p016io.reactivex.internal.operators.observable.ObservableInterval;
import p016io.reactivex.internal.operators.observable.ObservableIntervalRange;
import p016io.reactivex.internal.operators.observable.ObservableJoin;
import p016io.reactivex.internal.operators.observable.ObservableJust;
import p016io.reactivex.internal.operators.observable.ObservableLastMaybe;
import p016io.reactivex.internal.operators.observable.ObservableLastSingle;
import p016io.reactivex.internal.operators.observable.ObservableLift;
import p016io.reactivex.internal.operators.observable.ObservableMap;
import p016io.reactivex.internal.operators.observable.ObservableMapNotification;
import p016io.reactivex.internal.operators.observable.ObservableMaterialize;
import p016io.reactivex.internal.operators.observable.ObservableNever;
import p016io.reactivex.internal.operators.observable.ObservableObserveOn;
import p016io.reactivex.internal.operators.observable.ObservableOnErrorNext;
import p016io.reactivex.internal.operators.observable.ObservableOnErrorReturn;
import p016io.reactivex.internal.operators.observable.ObservablePublish;
import p016io.reactivex.internal.operators.observable.ObservablePublishSelector;
import p016io.reactivex.internal.operators.observable.ObservableRange;
import p016io.reactivex.internal.operators.observable.ObservableRangeLong;
import p016io.reactivex.internal.operators.observable.ObservableRedo;
import p016io.reactivex.internal.operators.observable.ObservableRepeat;
import p016io.reactivex.internal.operators.observable.ObservableRepeatUntil;
import p016io.reactivex.internal.operators.observable.ObservableReplay;
import p016io.reactivex.internal.operators.observable.ObservableRetryBiPredicate;
import p016io.reactivex.internal.operators.observable.ObservableRetryPredicate;
import p016io.reactivex.internal.operators.observable.ObservableSampleTimed;
import p016io.reactivex.internal.operators.observable.ObservableSampleWithObservable;
import p016io.reactivex.internal.operators.observable.ObservableScalarXMap;
import p016io.reactivex.internal.operators.observable.ObservableScan;
import p016io.reactivex.internal.operators.observable.ObservableScanSeed;
import p016io.reactivex.internal.operators.observable.ObservableSequenceEqualSingle;
import p016io.reactivex.internal.operators.observable.ObservableSerialized;
import p016io.reactivex.internal.operators.observable.ObservableSingleMaybe;
import p016io.reactivex.internal.operators.observable.ObservableSingleSingle;
import p016io.reactivex.internal.operators.observable.ObservableSkip;
import p016io.reactivex.internal.operators.observable.ObservableSkipLast;
import p016io.reactivex.internal.operators.observable.ObservableSkipLastTimed;
import p016io.reactivex.internal.operators.observable.ObservableSkipUntil;
import p016io.reactivex.internal.operators.observable.ObservableSkipWhile;
import p016io.reactivex.internal.operators.observable.ObservableSubscribeOn;
import p016io.reactivex.internal.operators.observable.ObservableSwitchIfEmpty;
import p016io.reactivex.internal.operators.observable.ObservableSwitchMap;
import p016io.reactivex.internal.operators.observable.ObservableTake;
import p016io.reactivex.internal.operators.observable.ObservableTakeLast;
import p016io.reactivex.internal.operators.observable.ObservableTakeLastOne;
import p016io.reactivex.internal.operators.observable.ObservableTakeLastTimed;
import p016io.reactivex.internal.operators.observable.ObservableTakeUntil;
import p016io.reactivex.internal.operators.observable.ObservableTakeUntilPredicate;
import p016io.reactivex.internal.operators.observable.ObservableTakeWhile;
import p016io.reactivex.internal.operators.observable.ObservableThrottleFirstTimed;
import p016io.reactivex.internal.operators.observable.ObservableTimeInterval;
import p016io.reactivex.internal.operators.observable.ObservableTimeout;
import p016io.reactivex.internal.operators.observable.ObservableTimeoutTimed;
import p016io.reactivex.internal.operators.observable.ObservableTimer;
import p016io.reactivex.internal.operators.observable.ObservableToList;
import p016io.reactivex.internal.operators.observable.ObservableToListSingle;
import p016io.reactivex.internal.operators.observable.ObservableUnsubscribeOn;
import p016io.reactivex.internal.operators.observable.ObservableUsing;
import p016io.reactivex.internal.operators.observable.ObservableWindow;
import p016io.reactivex.internal.operators.observable.ObservableWindowBoundary;
import p016io.reactivex.internal.operators.observable.ObservableWindowBoundarySelector;
import p016io.reactivex.internal.operators.observable.ObservableWindowBoundarySupplier;
import p016io.reactivex.internal.operators.observable.ObservableWindowTimed;
import p016io.reactivex.internal.operators.observable.ObservableWithLatestFrom;
import p016io.reactivex.internal.operators.observable.ObservableWithLatestFromMany;
import p016io.reactivex.internal.operators.observable.ObservableZip;
import p016io.reactivex.internal.operators.observable.ObservableZipIterable;
import p016io.reactivex.internal.util.ArrayListSupplier;
import p016io.reactivex.internal.util.ErrorMode;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.internal.util.HashMapSupplier;
import p016io.reactivex.observables.ConnectableObservable;
import p016io.reactivex.observables.GroupedObservable;
import p016io.reactivex.observers.SafeObserver;
import p016io.reactivex.observers.TestObserver;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.schedulers.Schedulers;
import p016io.reactivex.schedulers.Timed;

/* renamed from: io.reactivex.Observable */
public abstract class Observable<T> implements ObservableSource<T> {
    /* access modifiers changed from: protected */
    public abstract void subscribeActual(Observer<? super T> observer);

    @SchedulerSupport("none")
    public static <T> Observable<T> amb(Iterable<? extends ObservableSource<? extends T>> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableAmb<T>(null, iterable));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> ambArray(ObservableSource<? extends T>... observableSourceArr) {
        ObjectHelper.requireNonNull(observableSourceArr, "sources is null");
        int length = observableSourceArr.length;
        if (length == 0) {
            return empty();
        }
        if (length == 1) {
            return wrap(observableSourceArr[0]);
        }
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableAmb<T>(observableSourceArr, null));
    }

    public static int bufferSize() {
        return Flowable.bufferSize();
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatest(Function<? super Object[], ? extends R> function, int i, ObservableSource<? extends T>... observableSourceArr) {
        return combineLatest(observableSourceArr, function, i);
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatest(Iterable<? extends ObservableSource<? extends T>> iterable, Function<? super Object[], ? extends R> function) {
        return combineLatest(iterable, function, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatest(Iterable<? extends ObservableSource<? extends T>> iterable, Function<? super Object[], ? extends R> function, int i) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableCombineLatest observableCombineLatest = new ObservableCombineLatest(null, iterable, function, i << 1, false);
        return RxJavaPlugins.onAssembly((Observable<T>) observableCombineLatest);
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatest(ObservableSource<? extends T>[] observableSourceArr, Function<? super Object[], ? extends R> function) {
        return combineLatest(observableSourceArr, function, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatest(ObservableSource<? extends T>[] observableSourceArr, Function<? super Object[], ? extends R> function, int i) {
        ObjectHelper.requireNonNull(observableSourceArr, "sources is null");
        if (observableSourceArr.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableCombineLatest observableCombineLatest = new ObservableCombineLatest(observableSourceArr, null, function, i << 1, false);
        return RxJavaPlugins.onAssembly((Observable<T>) observableCombineLatest);
    }

    @SchedulerSupport("none")
    public static <T1, T2, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, BiFunction<? super T1, ? super T2, ? extends R> biFunction) {
        return combineLatest(Functions.toFunction(biFunction), bufferSize(), (ObservableSource<? extends T>[]) new ObservableSource[]{observableSource, observableSource2});
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, Function3<? super T1, ? super T2, ? super T3, ? extends R> function3) {
        return combineLatest(Functions.toFunction(function3), bufferSize(), (ObservableSource<? extends T>[]) new ObservableSource[]{observableSource, observableSource2, observableSource3});
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> function4) {
        return combineLatest(Functions.toFunction(function4), bufferSize(), (ObservableSource<? extends T>[]) new ObservableSource[]{observableSource, observableSource2, observableSource3, observableSource4});
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> function5) {
        return combineLatest(Functions.toFunction(function5), bufferSize(), (ObservableSource<? extends T>[]) new ObservableSource[]{observableSource, observableSource2, observableSource3, observableSource4, observableSource5});
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> function6) {
        return combineLatest(Functions.toFunction(function6), bufferSize(), (ObservableSource<? extends T>[]) new ObservableSource[]{observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6});
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, ObservableSource<? extends T7> observableSource7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> function7) {
        return combineLatest(Functions.toFunction(function7), bufferSize(), (ObservableSource<? extends T>[]) new ObservableSource[]{observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7});
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, ObservableSource<? extends T7> observableSource7, ObservableSource<? extends T8> observableSource8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> function8) {
        return combineLatest(Functions.toFunction(function8), bufferSize(), (ObservableSource<? extends T>[]) new ObservableSource[]{observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7, observableSource8});
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> combineLatest(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, ObservableSource<? extends T7> observableSource7, ObservableSource<? extends T8> observableSource8, ObservableSource<? extends T9> observableSource9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> function9) {
        return combineLatest(Functions.toFunction(function9), bufferSize(), (ObservableSource<? extends T>[]) new ObservableSource[]{observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7, observableSource8, observableSource9});
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatestDelayError(ObservableSource<? extends T>[] observableSourceArr, Function<? super Object[], ? extends R> function) {
        return combineLatestDelayError(observableSourceArr, function, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatestDelayError(Function<? super Object[], ? extends R> function, int i, ObservableSource<? extends T>... observableSourceArr) {
        return combineLatestDelayError(observableSourceArr, function, i);
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatestDelayError(ObservableSource<? extends T>[] observableSourceArr, Function<? super Object[], ? extends R> function, int i) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObjectHelper.requireNonNull(function, "combiner is null");
        if (observableSourceArr.length == 0) {
            return empty();
        }
        ObservableCombineLatest observableCombineLatest = new ObservableCombineLatest(observableSourceArr, null, function, i << 1, true);
        return RxJavaPlugins.onAssembly((Observable<T>) observableCombineLatest);
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatestDelayError(Iterable<? extends ObservableSource<? extends T>> iterable, Function<? super Object[], ? extends R> function) {
        return combineLatestDelayError(iterable, function, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> combineLatestDelayError(Iterable<? extends ObservableSource<? extends T>> iterable, Function<? super Object[], ? extends R> function, int i) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableCombineLatest observableCombineLatest = new ObservableCombineLatest(null, iterable, function, i << 1, true);
        return RxJavaPlugins.onAssembly((Observable<T>) observableCombineLatest);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concat(Iterable<? extends ObservableSource<? extends T>> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return fromIterable(iterable).concatMapDelayError(Functions.identity(), bufferSize(), false);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concat(ObservableSource<? extends ObservableSource<? extends T>> observableSource) {
        return concat(observableSource, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concat(ObservableSource<? extends ObservableSource<? extends T>> observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableConcatMap<T>(observableSource, Functions.identity(), i, ErrorMode.IMMEDIATE));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concat(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2) {
        return concatArray(observableSource, observableSource2);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concat(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, ObservableSource<? extends T> observableSource3) {
        return concatArray(observableSource, observableSource2, observableSource3);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concat(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, ObservableSource<? extends T> observableSource3, ObservableSource<? extends T> observableSource4) {
        return concatArray(observableSource, observableSource2, observableSource3, observableSource4);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatArray(ObservableSource<? extends T>... observableSourceArr) {
        if (observableSourceArr.length == 0) {
            return empty();
        }
        if (observableSourceArr.length == 1) {
            return wrap(observableSourceArr[0]);
        }
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableConcatMap<T>(fromArray(observableSourceArr), Functions.identity(), bufferSize(), ErrorMode.BOUNDARY));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatArrayDelayError(ObservableSource<? extends T>... observableSourceArr) {
        if (observableSourceArr.length == 0) {
            return empty();
        }
        if (observableSourceArr.length == 1) {
            return wrap(observableSourceArr[0]);
        }
        return concatDelayError((ObservableSource<? extends ObservableSource<? extends T>>) fromArray(observableSourceArr));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatArrayEager(ObservableSource<? extends T>... observableSourceArr) {
        return concatArrayEager(bufferSize(), bufferSize(), observableSourceArr);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatArrayEager(int i, int i2, ObservableSource<? extends T>... observableSourceArr) {
        return fromArray(observableSourceArr).concatMapEagerDelayError(Functions.identity(), i, i2, false);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatDelayError(Iterable<? extends ObservableSource<? extends T>> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return concatDelayError((ObservableSource<? extends ObservableSource<? extends T>>) fromIterable(iterable));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatDelayError(ObservableSource<? extends ObservableSource<? extends T>> observableSource) {
        return concatDelayError(observableSource, bufferSize(), true);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatDelayError(ObservableSource<? extends ObservableSource<? extends T>> observableSource, int i, boolean z) {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableConcatMap<T>(observableSource, Functions.identity(), i, z ? ErrorMode.END : ErrorMode.BOUNDARY));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatEager(ObservableSource<? extends ObservableSource<? extends T>> observableSource) {
        return concatEager(observableSource, bufferSize(), bufferSize());
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatEager(ObservableSource<? extends ObservableSource<? extends T>> observableSource, int i, int i2) {
        return wrap(observableSource).concatMapEager(Functions.identity(), i, i2);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatEager(Iterable<? extends ObservableSource<? extends T>> iterable) {
        return concatEager(iterable, bufferSize(), bufferSize());
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concatEager(Iterable<? extends ObservableSource<? extends T>> iterable, int i, int i2) {
        return fromIterable(iterable).concatMapEagerDelayError(Functions.identity(), i, i2, false);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> create(ObservableOnSubscribe<T> observableOnSubscribe) {
        ObjectHelper.requireNonNull(observableOnSubscribe, "source is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableCreate<T>(observableOnSubscribe));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> defer(Callable<? extends ObservableSource<? extends T>> callable) {
        ObjectHelper.requireNonNull(callable, "supplier is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableDefer<T>(callable));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> empty() {
        return RxJavaPlugins.onAssembly(ObservableEmpty.INSTANCE);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> error(Callable<? extends Throwable> callable) {
        ObjectHelper.requireNonNull(callable, "errorSupplier is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableError<T>(callable));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> error(Throwable th) {
        ObjectHelper.requireNonNull(th, "e is null");
        return error(Functions.justCallable(th));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> fromArray(T... tArr) {
        ObjectHelper.requireNonNull(tArr, "items is null");
        if (tArr.length == 0) {
            return empty();
        }
        if (tArr.length == 1) {
            return just(tArr[0]);
        }
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFromArray<T>(tArr));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> fromCallable(Callable<? extends T> callable) {
        ObjectHelper.requireNonNull(callable, "supplier is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFromCallable<T>(callable));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> fromFuture(Future<? extends T> future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFromFuture<T>(future, 0, null));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> fromFuture(Future<? extends T> future, long j, TimeUnit timeUnit) {
        ObjectHelper.requireNonNull(future, "future is null");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFromFuture<T>(future, j, timeUnit));
    }

    @SchedulerSupport("custom")
    public static <T> Observable<T> fromFuture(Future<? extends T> future, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future, j, timeUnit).subscribeOn(scheduler);
    }

    @SchedulerSupport("custom")
    public static <T> Observable<T> fromFuture(Future<? extends T> future, Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return fromFuture(future).subscribeOn(scheduler);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> fromIterable(Iterable<? extends T> iterable) {
        ObjectHelper.requireNonNull(iterable, "source is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFromIterable<T>(iterable));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public static <T> Observable<T> fromPublisher(Publisher<? extends T> publisher) {
        ObjectHelper.requireNonNull(publisher, "publisher is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFromPublisher<T>(publisher));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> generate(Consumer<Emitter<T>> consumer) {
        ObjectHelper.requireNonNull(consumer, "generator  is null");
        return generate(Functions.nullSupplier(), ObservableInternalHelper.simpleGenerator(consumer), Functions.emptyConsumer());
    }

    @SchedulerSupport("none")
    public static <T, S> Observable<T> generate(Callable<S> callable, BiConsumer<S, Emitter<T>> biConsumer) {
        ObjectHelper.requireNonNull(biConsumer, "generator  is null");
        return generate(callable, ObservableInternalHelper.simpleBiGenerator(biConsumer), Functions.emptyConsumer());
    }

    @SchedulerSupport("none")
    public static <T, S> Observable<T> generate(Callable<S> callable, BiConsumer<S, Emitter<T>> biConsumer, Consumer<? super S> consumer) {
        ObjectHelper.requireNonNull(biConsumer, "generator  is null");
        return generate(callable, ObservableInternalHelper.simpleBiGenerator(biConsumer), consumer);
    }

    @SchedulerSupport("none")
    public static <T, S> Observable<T> generate(Callable<S> callable, BiFunction<S, Emitter<T>, S> biFunction) {
        return generate(callable, biFunction, Functions.emptyConsumer());
    }

    @SchedulerSupport("none")
    public static <T, S> Observable<T> generate(Callable<S> callable, BiFunction<S, Emitter<T>, S> biFunction, Consumer<? super S> consumer) {
        ObjectHelper.requireNonNull(callable, "initialState is null");
        ObjectHelper.requireNonNull(biFunction, "generator  is null");
        ObjectHelper.requireNonNull(consumer, "disposeState is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableGenerate<T>(callable, biFunction, consumer));
    }

    @SchedulerSupport("io.reactivex:computation")
    public static Observable<Long> interval(long j, long j2, TimeUnit timeUnit) {
        return interval(j, j2, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public static Observable<Long> interval(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableInterval observableInterval = new ObservableInterval(Math.max(0, j), Math.max(0, j2), timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Observable<T>) observableInterval);
    }

    @SchedulerSupport("io.reactivex:computation")
    public static Observable<Long> interval(long j, TimeUnit timeUnit) {
        return interval(j, j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public static Observable<Long> interval(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return interval(j, j, timeUnit, scheduler);
    }

    @SchedulerSupport("io.reactivex:computation")
    public static Observable<Long> intervalRange(long j, long j2, long j3, long j4, TimeUnit timeUnit) {
        return intervalRange(j, j2, j3, j4, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public static Observable<Long> intervalRange(long j, long j2, long j3, long j4, TimeUnit timeUnit, Scheduler scheduler) {
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
                ObservableIntervalRange observableIntervalRange = new ObservableIntervalRange(j, j7, Math.max(0, j6), Math.max(0, j4), timeUnit2, scheduler2);
                return RxJavaPlugins.onAssembly((Observable<T>) observableIntervalRange);
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T t) {
        ObjectHelper.requireNonNull(t, "The item is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableJust<T>(t));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T t, T t2) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        return fromArray(t, t2);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T t, T t2, T t3) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        ObjectHelper.requireNonNull(t3, "The third item is null");
        return fromArray(t, t2, t3);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T t, T t2, T t3, T t4) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        ObjectHelper.requireNonNull(t3, "The third item is null");
        ObjectHelper.requireNonNull(t4, "The fourth item is null");
        return fromArray(t, t2, t3, t4);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T t, T t2, T t3, T t4, T t5) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        ObjectHelper.requireNonNull(t3, "The third item is null");
        ObjectHelper.requireNonNull(t4, "The fourth item is null");
        ObjectHelper.requireNonNull(t5, "The fifth item is null");
        return fromArray(t, t2, t3, t4, t5);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T t, T t2, T t3, T t4, T t5, T t6) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        ObjectHelper.requireNonNull(t3, "The third item is null");
        ObjectHelper.requireNonNull(t4, "The fourth item is null");
        ObjectHelper.requireNonNull(t5, "The fifth item is null");
        ObjectHelper.requireNonNull(t6, "The sixth item is null");
        return fromArray(t, t2, t3, t4, t5, t6);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T t, T t2, T t3, T t4, T t5, T t6, T t7) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        ObjectHelper.requireNonNull(t3, "The third item is null");
        ObjectHelper.requireNonNull(t4, "The fourth item is null");
        ObjectHelper.requireNonNull(t5, "The fifth item is null");
        ObjectHelper.requireNonNull(t6, "The sixth item is null");
        ObjectHelper.requireNonNull(t7, "The seventh item is null");
        return fromArray(t, t2, t3, t4, t5, t6, t7);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T t, T t2, T t3, T t4, T t5, T t6, T t7, T t8) {
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

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T t, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9) {
        ObjectHelper.requireNonNull(t, "The first item is null");
        ObjectHelper.requireNonNull(t2, "The second item is null");
        ObjectHelper.requireNonNull(t3, "The third item is null");
        ObjectHelper.requireNonNull(t4, "The fourth item is null");
        ObjectHelper.requireNonNull(t5, "The fifth item is null");
        ObjectHelper.requireNonNull(t6, "The sixth item is null");
        ObjectHelper.requireNonNull(t7, "The seventh item is null");
        ObjectHelper.requireNonNull(t8, "The eighth item is null");
        ObjectHelper.requireNonNull(t9, "The ninth item is null");
        return fromArray(t, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> just(T t, T t2, T t3, T t4, T t5, T t6, T t7, T t8, T t9, T t10) {
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

    @SchedulerSupport("none")
    public static <T> Observable<T> merge(Iterable<? extends ObservableSource<? extends T>> iterable, int i, int i2) {
        return fromIterable(iterable).flatMap(Functions.identity(), false, i, i2);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeArray(int i, int i2, ObservableSource<? extends T>... observableSourceArr) {
        return fromArray(observableSourceArr).flatMap(Functions.identity(), false, i, i2);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> merge(Iterable<? extends ObservableSource<? extends T>> iterable) {
        return fromIterable(iterable).flatMap(Functions.identity());
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> merge(Iterable<? extends ObservableSource<? extends T>> iterable, int i) {
        return fromIterable(iterable).flatMap(Functions.identity(), i);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> merge(ObservableSource<? extends ObservableSource<? extends T>> observableSource) {
        ObservableFlatMap observableFlatMap = new ObservableFlatMap(observableSource, Functions.identity(), false, Integer.MAX_VALUE, bufferSize());
        return RxJavaPlugins.onAssembly((Observable<T>) observableFlatMap);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> merge(ObservableSource<? extends ObservableSource<? extends T>> observableSource, int i) {
        ObservableFlatMap observableFlatMap = new ObservableFlatMap(observableSource, Functions.identity(), false, i, bufferSize());
        return RxJavaPlugins.onAssembly((Observable<T>) observableFlatMap);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> merge(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return fromArray(observableSource, observableSource2).flatMap(Functions.identity(), false, 2);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> merge(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, ObservableSource<? extends T> observableSource3) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        return fromArray(observableSource, observableSource2, observableSource3).flatMap(Functions.identity(), false, 3);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> merge(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, ObservableSource<? extends T> observableSource3, ObservableSource<? extends T> observableSource4) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        return fromArray(observableSource, observableSource2, observableSource3, observableSource4).flatMap(Functions.identity(), false, 4);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeArray(ObservableSource<? extends T>... observableSourceArr) {
        return fromArray(observableSourceArr).flatMap(Functions.identity(), observableSourceArr.length);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeDelayError(Iterable<? extends ObservableSource<? extends T>> iterable) {
        return fromIterable(iterable).flatMap(Functions.identity(), true);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeDelayError(Iterable<? extends ObservableSource<? extends T>> iterable, int i, int i2) {
        return fromIterable(iterable).flatMap(Functions.identity(), true, i, i2);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeArrayDelayError(int i, int i2, ObservableSource<? extends T>... observableSourceArr) {
        return fromArray(observableSourceArr).flatMap(Functions.identity(), true, i, i2);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeDelayError(Iterable<? extends ObservableSource<? extends T>> iterable, int i) {
        return fromIterable(iterable).flatMap(Functions.identity(), true, i);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends ObservableSource<? extends T>> observableSource) {
        ObservableFlatMap observableFlatMap = new ObservableFlatMap(observableSource, Functions.identity(), true, Integer.MAX_VALUE, bufferSize());
        return RxJavaPlugins.onAssembly((Observable<T>) observableFlatMap);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends ObservableSource<? extends T>> observableSource, int i) {
        ObservableFlatMap observableFlatMap = new ObservableFlatMap(observableSource, Functions.identity(), true, i, bufferSize());
        return RxJavaPlugins.onAssembly((Observable<T>) observableFlatMap);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        return fromArray(observableSource, observableSource2).flatMap(Functions.identity(), true, 2);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, ObservableSource<? extends T> observableSource3) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        return fromArray(observableSource, observableSource2, observableSource3).flatMap(Functions.identity(), true, 3);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeDelayError(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, ObservableSource<? extends T> observableSource3, ObservableSource<? extends T> observableSource4) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(observableSource3, "source3 is null");
        ObjectHelper.requireNonNull(observableSource4, "source4 is null");
        return fromArray(observableSource, observableSource2, observableSource3, observableSource4).flatMap(Functions.identity(), true, 4);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> mergeArrayDelayError(ObservableSource<? extends T>... observableSourceArr) {
        return fromArray(observableSourceArr).flatMap(Functions.identity(), true, observableSourceArr.length);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> never() {
        return RxJavaPlugins.onAssembly(ObservableNever.INSTANCE);
    }

    @SchedulerSupport("none")
    public static Observable<Integer> range(int i, int i2) {
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
                return RxJavaPlugins.onAssembly((Observable<T>) new ObservableRange<T>(i, i2));
            }
            throw new IllegalArgumentException("Integer overflow");
        }
    }

    @SchedulerSupport("none")
    public static Observable<Long> rangeLong(long j, long j2) {
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
                return RxJavaPlugins.onAssembly((Observable<T>) new ObservableRangeLong<T>(j, j2));
            }
            throw new IllegalArgumentException("Overflow! start + count is bigger than Long.MAX_VALUE");
        }
    }

    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2) {
        return sequenceEqual(observableSource, observableSource2, ObjectHelper.equalsPredicate(), bufferSize());
    }

    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, BiPredicate<? super T, ? super T> biPredicate) {
        return sequenceEqual(observableSource, observableSource2, biPredicate, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, BiPredicate<? super T, ? super T> biPredicate, int i) {
        ObjectHelper.requireNonNull(observableSource, "source1 is null");
        ObjectHelper.requireNonNull(observableSource2, "source2 is null");
        ObjectHelper.requireNonNull(biPredicate, "isEqual is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableSequenceEqualSingle<T>(observableSource, observableSource2, biPredicate, i));
    }

    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(ObservableSource<? extends T> observableSource, ObservableSource<? extends T> observableSource2, int i) {
        return sequenceEqual(observableSource, observableSource2, ObjectHelper.equalsPredicate(), i);
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> switchOnNext(ObservableSource<? extends ObservableSource<? extends T>> observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableSwitchMap<T>(observableSource, Functions.identity(), i, false));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> switchOnNext(ObservableSource<? extends ObservableSource<? extends T>> observableSource) {
        return switchOnNext(observableSource, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> switchOnNextDelayError(ObservableSource<? extends ObservableSource<? extends T>> observableSource) {
        return switchOnNextDelayError(observableSource, bufferSize());
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> switchOnNextDelayError(ObservableSource<? extends ObservableSource<? extends T>> observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableSwitchMap<T>(observableSource, Functions.identity(), i, true));
    }

    @SchedulerSupport("io.reactivex:computation")
    public static Observable<Long> timer(long j, TimeUnit timeUnit) {
        return timer(j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public static Observable<Long> timer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableTimer<T>(Math.max(j, 0), timeUnit, scheduler));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> unsafeCreate(ObservableSource<T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "source is null");
        ObjectHelper.requireNonNull(observableSource, "onSubscribe is null");
        if (!(observableSource instanceof Observable)) {
            return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFromUnsafeSource<T>(observableSource));
        }
        throw new IllegalArgumentException("unsafeCreate(Observable) should be upgraded");
    }

    @SchedulerSupport("none")
    public static <T, D> Observable<T> using(Callable<? extends D> callable, Function<? super D, ? extends ObservableSource<? extends T>> function, Consumer<? super D> consumer) {
        return using(callable, function, consumer, true);
    }

    @SchedulerSupport("none")
    public static <T, D> Observable<T> using(Callable<? extends D> callable, Function<? super D, ? extends ObservableSource<? extends T>> function, Consumer<? super D> consumer, boolean z) {
        ObjectHelper.requireNonNull(callable, "resourceSupplier is null");
        ObjectHelper.requireNonNull(function, "sourceSupplier is null");
        ObjectHelper.requireNonNull(consumer, "disposer is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableUsing<T>(callable, function, consumer, z));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> wrap(ObservableSource<T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "source is null");
        if (observableSource instanceof Observable) {
            return RxJavaPlugins.onAssembly((Observable) observableSource);
        }
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFromUnsafeSource<T>(observableSource));
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> zip(Iterable<? extends ObservableSource<? extends T>> iterable, Function<? super Object[], ? extends R> function) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObservableZip observableZip = new ObservableZip(null, iterable, function, bufferSize(), false);
        return RxJavaPlugins.onAssembly((Observable<T>) observableZip);
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> zip(ObservableSource<? extends ObservableSource<? extends T>> observableSource, Function<? super Object[], ? extends R> function) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(observableSource, "sources is null");
        return RxJavaPlugins.onAssembly(new ObservableToList(observableSource, 16).flatMap(ObservableInternalHelper.zipIterable(function)));
    }

    @SchedulerSupport("none")
    public static <T1, T2, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, BiFunction<? super T1, ? super T2, ? extends R> biFunction) {
        return zipArray(Functions.toFunction(biFunction), false, bufferSize(), observableSource, observableSource2);
    }

    @SchedulerSupport("none")
    public static <T1, T2, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, BiFunction<? super T1, ? super T2, ? extends R> biFunction, boolean z) {
        return zipArray(Functions.toFunction(biFunction), z, bufferSize(), observableSource, observableSource2);
    }

    @SchedulerSupport("none")
    public static <T1, T2, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, BiFunction<? super T1, ? super T2, ? extends R> biFunction, boolean z, int i) {
        return zipArray(Functions.toFunction(biFunction), z, i, observableSource, observableSource2);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, Function3<? super T1, ? super T2, ? super T3, ? extends R> function3) {
        return zipArray(Functions.toFunction(function3), false, bufferSize(), observableSource, observableSource2, observableSource3);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> function4) {
        return zipArray(Functions.toFunction(function4), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> function5) {
        return zipArray(Functions.toFunction(function5), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> function6) {
        return zipArray(Functions.toFunction(function6), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, ObservableSource<? extends T7> observableSource7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> function7) {
        return zipArray(Functions.toFunction(function7), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, ObservableSource<? extends T7> observableSource7, ObservableSource<? extends T8> observableSource8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> function8) {
        return zipArray(Functions.toFunction(function8), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7, observableSource8);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> zip(ObservableSource<? extends T1> observableSource, ObservableSource<? extends T2> observableSource2, ObservableSource<? extends T3> observableSource3, ObservableSource<? extends T4> observableSource4, ObservableSource<? extends T5> observableSource5, ObservableSource<? extends T6> observableSource6, ObservableSource<? extends T7> observableSource7, ObservableSource<? extends T8> observableSource8, ObservableSource<? extends T9> observableSource9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> function9) {
        return zipArray(Functions.toFunction(function9), false, bufferSize(), observableSource, observableSource2, observableSource3, observableSource4, observableSource5, observableSource6, observableSource7, observableSource8, observableSource9);
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> zipArray(Function<? super Object[], ? extends R> function, boolean z, int i, ObservableSource<? extends T>... observableSourceArr) {
        if (observableSourceArr.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableZip observableZip = new ObservableZip(observableSourceArr, null, function, i, z);
        return RxJavaPlugins.onAssembly((Observable<T>) observableZip);
    }

    @SchedulerSupport("none")
    public static <T, R> Observable<R> zipIterable(Iterable<? extends ObservableSource<? extends T>> iterable, Function<? super Object[], ? extends R> function, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(iterable, "sources is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableZip observableZip = new ObservableZip(null, iterable, function, i, z);
        return RxJavaPlugins.onAssembly((Observable<T>) observableZip);
    }

    @SchedulerSupport("none")
    public final Single<Boolean> all(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableAllSingle<T>(this, predicate));
    }

    @SchedulerSupport("none")
    public final Observable<T> ambWith(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return ambArray(this, observableSource);
    }

    @SchedulerSupport("none")
    public final Single<Boolean> any(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableAnySingle<T>(this, predicate));
    }

    @SchedulerSupport("none")
    public final T blockingFirst() {
        BlockingFirstObserver blockingFirstObserver = new BlockingFirstObserver();
        subscribe((Observer<? super T>) blockingFirstObserver);
        T blockingGet = blockingFirstObserver.blockingGet();
        if (blockingGet != null) {
            return blockingGet;
        }
        throw new NoSuchElementException();
    }

    @SchedulerSupport("none")
    public final T blockingFirst(T t) {
        BlockingFirstObserver blockingFirstObserver = new BlockingFirstObserver();
        subscribe((Observer<? super T>) blockingFirstObserver);
        T blockingGet = blockingFirstObserver.blockingGet();
        return blockingGet != null ? blockingGet : t;
    }

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

    @SchedulerSupport("none")
    public final Iterable<T> blockingIterable() {
        return blockingIterable(bufferSize());
    }

    @SchedulerSupport("none")
    public final Iterable<T> blockingIterable(int i) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        return new BlockingObservableIterable(this, i);
    }

    @SchedulerSupport("none")
    public final T blockingLast() {
        BlockingLastObserver blockingLastObserver = new BlockingLastObserver();
        subscribe((Observer<? super T>) blockingLastObserver);
        T blockingGet = blockingLastObserver.blockingGet();
        if (blockingGet != null) {
            return blockingGet;
        }
        throw new NoSuchElementException();
    }

    @SchedulerSupport("none")
    public final T blockingLast(T t) {
        BlockingLastObserver blockingLastObserver = new BlockingLastObserver();
        subscribe((Observer<? super T>) blockingLastObserver);
        T blockingGet = blockingLastObserver.blockingGet();
        return blockingGet != null ? blockingGet : t;
    }

    @SchedulerSupport("none")
    public final Iterable<T> blockingLatest() {
        return new BlockingObservableLatest(this);
    }

    @SchedulerSupport("none")
    public final Iterable<T> blockingMostRecent(T t) {
        return new BlockingObservableMostRecent(this, t);
    }

    @SchedulerSupport("none")
    public final Iterable<T> blockingNext() {
        return new BlockingObservableNext(this);
    }

    @SchedulerSupport("none")
    public final T blockingSingle() {
        T blockingGet = singleElement().blockingGet();
        if (blockingGet != null) {
            return blockingGet;
        }
        throw new NoSuchElementException();
    }

    @SchedulerSupport("none")
    public final T blockingSingle(T t) {
        return single(t).blockingGet();
    }

    @SchedulerSupport("none")
    public final Future<T> toFuture() {
        return (Future) subscribeWith(new FutureObserver());
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe() {
        ObservableBlockingSubscribe.subscribe(this);
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> consumer) {
        ObservableBlockingSubscribe.subscribe(this, consumer, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2) {
        ObservableBlockingSubscribe.subscribe(this, consumer, consumer2, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action) {
        ObservableBlockingSubscribe.subscribe(this, consumer, consumer2, action);
    }

    @SchedulerSupport("none")
    public final void blockingSubscribe(Observer<? super T> observer) {
        ObservableBlockingSubscribe.subscribe(this, observer);
    }

    @SchedulerSupport("none")
    public final Observable<List<T>> buffer(int i) {
        return buffer(i, i);
    }

    @SchedulerSupport("none")
    public final Observable<List<T>> buffer(int i, int i2) {
        return buffer(i, i2, ArrayListSupplier.asCallable());
    }

    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Observable<U> buffer(int i, int i2, Callable<U> callable) {
        ObjectHelper.verifyPositive(i, "count");
        ObjectHelper.verifyPositive(i2, "skip");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableBuffer<T>(this, i, i2, callable));
    }

    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Observable<U> buffer(int i, Callable<U> callable) {
        return buffer(i, i, callable);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<List<T>> buffer(long j, long j2, TimeUnit timeUnit) {
        return buffer(j, j2, timeUnit, Schedulers.computation(), ArrayListSupplier.asCallable());
    }

    @SchedulerSupport("custom")
    public final Observable<List<T>> buffer(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return buffer(j, j2, timeUnit, scheduler, ArrayListSupplier.asCallable());
    }

    @SchedulerSupport("custom")
    public final <U extends Collection<? super T>> Observable<U> buffer(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, Callable<U> callable) {
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        Callable<U> callable2 = callable;
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        ObservableBufferTimed observableBufferTimed = new ObservableBufferTimed(this, j, j2, timeUnit2, scheduler2, callable2, Integer.MAX_VALUE, false);
        return RxJavaPlugins.onAssembly((Observable<T>) observableBufferTimed);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<List<T>> buffer(long j, TimeUnit timeUnit) {
        return buffer(j, timeUnit, Schedulers.computation(), Integer.MAX_VALUE);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<List<T>> buffer(long j, TimeUnit timeUnit, int i) {
        return buffer(j, timeUnit, Schedulers.computation(), i);
    }

    @SchedulerSupport("custom")
    public final Observable<List<T>> buffer(long j, TimeUnit timeUnit, Scheduler scheduler, int i) {
        return buffer(j, timeUnit, scheduler, i, ArrayListSupplier.asCallable(), false);
    }

    @SchedulerSupport("custom")
    public final <U extends Collection<? super T>> Observable<U> buffer(long j, TimeUnit timeUnit, Scheduler scheduler, int i, Callable<U> callable, boolean z) {
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        Callable<U> callable2 = callable;
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "count");
        ObservableBufferTimed observableBufferTimed = new ObservableBufferTimed(this, j, j, timeUnit2, scheduler2, callable2, i2, z);
        return RxJavaPlugins.onAssembly((Observable<T>) observableBufferTimed);
    }

    @SchedulerSupport("custom")
    public final Observable<List<T>> buffer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return buffer(j, timeUnit, scheduler, Integer.MAX_VALUE, ArrayListSupplier.asCallable(), false);
    }

    @SchedulerSupport("none")
    public final <TOpening, TClosing> Observable<List<T>> buffer(ObservableSource<? extends TOpening> observableSource, Function<? super TOpening, ? extends ObservableSource<? extends TClosing>> function) {
        return buffer(observableSource, function, ArrayListSupplier.asCallable());
    }

    @SchedulerSupport("none")
    public final <TOpening, TClosing, U extends Collection<? super T>> Observable<U> buffer(ObservableSource<? extends TOpening> observableSource, Function<? super TOpening, ? extends ObservableSource<? extends TClosing>> function, Callable<U> callable) {
        ObjectHelper.requireNonNull(observableSource, "openingIndicator is null");
        ObjectHelper.requireNonNull(function, "closingIndicator is null");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableBufferBoundary<T>(this, observableSource, function, callable));
    }

    @SchedulerSupport("none")
    public final <B> Observable<List<T>> buffer(ObservableSource<B> observableSource) {
        return buffer(observableSource, ArrayListSupplier.asCallable());
    }

    @SchedulerSupport("none")
    public final <B> Observable<List<T>> buffer(ObservableSource<B> observableSource, int i) {
        return buffer(observableSource, Functions.createArrayList(i));
    }

    @SchedulerSupport("none")
    public final <B, U extends Collection<? super T>> Observable<U> buffer(ObservableSource<B> observableSource, Callable<U> callable) {
        ObjectHelper.requireNonNull(observableSource, "boundary is null");
        ObjectHelper.requireNonNull(callable, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableBufferExactBoundary<T>(this, observableSource, callable));
    }

    @SchedulerSupport("none")
    public final <B> Observable<List<T>> buffer(Callable<? extends ObservableSource<B>> callable) {
        return buffer(callable, ArrayListSupplier.asCallable());
    }

    @SchedulerSupport("none")
    public final <B, U extends Collection<? super T>> Observable<U> buffer(Callable<? extends ObservableSource<B>> callable, Callable<U> callable2) {
        ObjectHelper.requireNonNull(callable, "boundarySupplier is null");
        ObjectHelper.requireNonNull(callable2, "bufferSupplier is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableBufferBoundarySupplier<T>(this, callable, callable2));
    }

    @SchedulerSupport("none")
    public final Observable<T> cache() {
        return ObservableCache.from(this);
    }

    @SchedulerSupport("none")
    public final Observable<T> cacheWithInitialCapacity(int i) {
        return ObservableCache.from(this, i);
    }

    @SchedulerSupport("none")
    public final <U> Observable<U> cast(Class<U> cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return map(Functions.castFunction(cls));
    }

    @SchedulerSupport("none")
    public final <U> Single<U> collect(Callable<? extends U> callable, BiConsumer<? super U, ? super T> biConsumer) {
        ObjectHelper.requireNonNull(callable, "initialValueSupplier is null");
        ObjectHelper.requireNonNull(biConsumer, "collector is null");
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableCollectSingle<T>(this, callable, biConsumer));
    }

    @SchedulerSupport("none")
    public final <U> Single<U> collectInto(U u, BiConsumer<? super U, ? super T> biConsumer) {
        ObjectHelper.requireNonNull(u, "initialValue is null");
        return collect(Functions.justCallable(u), biConsumer);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> compose(ObservableTransformer<T, R> observableTransformer) {
        return wrap(observableTransformer.apply(this));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> concatMap(Function<? super T, ? extends ObservableSource<? extends R>> function) {
        return concatMap(function, 2);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> concatMap(Function<? super T, ? extends ObservableSource<? extends R>> function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        if (!(this instanceof ScalarCallable)) {
            return RxJavaPlugins.onAssembly((Observable<T>) new ObservableConcatMap<T>(this, function, i, ErrorMode.IMMEDIATE));
        }
        Object call = ((ScalarCallable) this).call();
        if (call == null) {
            return empty();
        }
        return ObservableScalarXMap.scalarXMap(call, function);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> concatMapDelayError(Function<? super T, ? extends ObservableSource<? extends R>> function) {
        return concatMapDelayError(function, bufferSize(), true);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> concatMapDelayError(Function<? super T, ? extends ObservableSource<? extends R>> function, int i, boolean z) {
        ObjectHelper.verifyPositive(i, "prefetch");
        if (this instanceof ScalarCallable) {
            Object call = ((ScalarCallable) this).call();
            if (call == null) {
                return empty();
            }
            return ObservableScalarXMap.scalarXMap(call, function);
        }
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableConcatMap<T>(this, function, i, z ? ErrorMode.END : ErrorMode.BOUNDARY));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> concatMapEager(Function<? super T, ? extends ObservableSource<? extends R>> function) {
        return concatMapEager(function, Integer.MAX_VALUE, bufferSize());
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> concatMapEager(Function<? super T, ? extends ObservableSource<? extends R>> function, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "prefetch");
        ObservableConcatMapEager observableConcatMapEager = new ObservableConcatMapEager(this, function, ErrorMode.IMMEDIATE, i, i2);
        return RxJavaPlugins.onAssembly((Observable<T>) observableConcatMapEager);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> concatMapEagerDelayError(Function<? super T, ? extends ObservableSource<? extends R>> function, boolean z) {
        return concatMapEagerDelayError(function, Integer.MAX_VALUE, bufferSize(), z);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> concatMapEagerDelayError(Function<? super T, ? extends ObservableSource<? extends R>> function, int i, int i2, boolean z) {
        ObservableConcatMapEager observableConcatMapEager = new ObservableConcatMapEager(this, function, z ? ErrorMode.END : ErrorMode.BOUNDARY, i, i2);
        return RxJavaPlugins.onAssembly((Observable<T>) observableConcatMapEager);
    }

    @SchedulerSupport("none")
    public final <U> Observable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFlattenIterable<T>(this, function));
    }

    @SchedulerSupport("none")
    public final <U> Observable<U> concatMapIterable(Function<? super T, ? extends Iterable<? extends U>> function, int i) {
        return concatMap(ObservableInternalHelper.flatMapIntoIterable(function), i);
    }

    @SchedulerSupport("none")
    public final Observable<T> concatWith(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return concat((ObservableSource<? extends T>) this, observableSource);
    }

    @SchedulerSupport("none")
    public final Single<Boolean> contains(Object obj) {
        ObjectHelper.requireNonNull(obj, "element is null");
        return any(Functions.equalsWith(obj));
    }

    @SchedulerSupport("none")
    public final Single<Long> count() {
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableCountSingle<T>(this));
    }

    @SchedulerSupport("none")
    public final <U> Observable<T> debounce(Function<? super T, ? extends ObservableSource<U>> function) {
        ObjectHelper.requireNonNull(function, "debounceSelector is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableDebounce<T>(this, function));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> debounce(long j, TimeUnit timeUnit) {
        return debounce(j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Observable<T> debounce(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableDebounceTimed observableDebounceTimed = new ObservableDebounceTimed(this, j, timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Observable<T>) observableDebounceTimed);
    }

    @SchedulerSupport("none")
    public final Observable<T> defaultIfEmpty(T t) {
        ObjectHelper.requireNonNull(t, "defaultItem is null");
        return switchIfEmpty(just(t));
    }

    @SchedulerSupport("none")
    public final <U> Observable<T> delay(Function<? super T, ? extends ObservableSource<U>> function) {
        ObjectHelper.requireNonNull(function, "itemDelay is null");
        return flatMap(ObservableInternalHelper.itemDelay(function));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> delay(long j, TimeUnit timeUnit) {
        return delay(j, timeUnit, Schedulers.computation(), false);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> delay(long j, TimeUnit timeUnit, boolean z) {
        return delay(j, timeUnit, Schedulers.computation(), z);
    }

    @SchedulerSupport("custom")
    public final Observable<T> delay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delay(j, timeUnit, scheduler, false);
    }

    @SchedulerSupport("custom")
    public final Observable<T> delay(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableDelay observableDelay = new ObservableDelay(this, j, timeUnit, scheduler, z);
        return RxJavaPlugins.onAssembly((Observable<T>) observableDelay);
    }

    @SchedulerSupport("none")
    public final <U, V> Observable<T> delay(ObservableSource<U> observableSource, Function<? super T, ? extends ObservableSource<V>> function) {
        return delaySubscription(observableSource).delay(function);
    }

    @SchedulerSupport("none")
    public final <U> Observable<T> delaySubscription(ObservableSource<U> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableDelaySubscriptionOther<T>(this, observableSource));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> delaySubscription(long j, TimeUnit timeUnit) {
        return delaySubscription(j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Observable<T> delaySubscription(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delaySubscription(timer(j, timeUnit, scheduler));
    }

    @SchedulerSupport("none")
    public final <T2> Observable<T2> dematerialize() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableDematerialize<T>(this));
    }

    @SchedulerSupport("none")
    public final Observable<T> distinct() {
        return distinct(Functions.identity(), Functions.createHashSet());
    }

    @SchedulerSupport("none")
    public final <K> Observable<T> distinct(Function<? super T, K> function) {
        return distinct(function, Functions.createHashSet());
    }

    @SchedulerSupport("none")
    public final <K> Observable<T> distinct(Function<? super T, K> function, Callable<? extends Collection<? super K>> callable) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(callable, "collectionSupplier is null");
        return new ObservableDistinct(this, function, callable);
    }

    @SchedulerSupport("none")
    public final Observable<T> distinctUntilChanged() {
        return distinctUntilChanged(Functions.identity());
    }

    @SchedulerSupport("none")
    public final <K> Observable<T> distinctUntilChanged(Function<? super T, K> function) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableDistinctUntilChanged<T>(this, function, ObjectHelper.equalsPredicate()));
    }

    @SchedulerSupport("none")
    public final Observable<T> distinctUntilChanged(BiPredicate<? super T, ? super T> biPredicate) {
        ObjectHelper.requireNonNull(biPredicate, "comparer is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableDistinctUntilChanged<T>(this, Functions.identity(), biPredicate));
    }

    @Experimental
    @SchedulerSupport("none")
    public final Observable<T> doAfterNext(Consumer<? super T> consumer) {
        ObjectHelper.requireNonNull(consumer, "onAfterNext is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableDoAfterNext<T>(this, consumer));
    }

    @SchedulerSupport("none")
    public final Observable<T> doAfterTerminate(Action action) {
        ObjectHelper.requireNonNull(action, "onFinally is null");
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, action);
    }

    @Experimental
    @SchedulerSupport("none")
    public final Observable<T> doFinally(Action action) {
        ObjectHelper.requireNonNull(action, "onFinally is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableDoFinally<T>(this, action));
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnDispose(Action action) {
        return doOnLifecycle(Functions.emptyConsumer(), action);
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnComplete(Action action) {
        return doOnEach(Functions.emptyConsumer(), Functions.emptyConsumer(), action, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    private Observable<T> doOnEach(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action, Action action2) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ObjectHelper.requireNonNull(consumer2, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ObjectHelper.requireNonNull(action2, "onAfterTerminate is null");
        ObservableDoOnEach observableDoOnEach = new ObservableDoOnEach(this, consumer, consumer2, action, action2);
        return RxJavaPlugins.onAssembly((Observable<T>) observableDoOnEach);
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnEach(Consumer<? super Notification<T>> consumer) {
        ObjectHelper.requireNonNull(consumer, "consumer is null");
        return doOnEach(Functions.notificationOnNext(consumer), Functions.notificationOnError(consumer), Functions.notificationOnComplete(consumer), Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnEach(Observer<? super T> observer) {
        ObjectHelper.requireNonNull(observer, "observer is null");
        return doOnEach(ObservableInternalHelper.observerOnNext(observer), ObservableInternalHelper.observerOnError(observer), ObservableInternalHelper.observerOnComplete(observer), Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnError(Consumer<? super Throwable> consumer) {
        return doOnEach(Functions.emptyConsumer(), consumer, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnLifecycle(Consumer<? super Disposable> consumer, Action action) {
        ObjectHelper.requireNonNull(consumer, "onSubscribe is null");
        ObjectHelper.requireNonNull(action, "onDispose is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableDoOnLifecycle<T>(this, consumer, action));
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnNext(Consumer<? super T> consumer) {
        return doOnEach(consumer, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnSubscribe(Consumer<? super Disposable> consumer) {
        return doOnLifecycle(consumer, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Observable<T> doOnTerminate(Action action) {
        ObjectHelper.requireNonNull(action, "onTerminate is null");
        return doOnEach(Functions.emptyConsumer(), Functions.actionConsumer(action), action, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Maybe<T> elementAt(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly((Maybe<T>) new ObservableElementAtMaybe<T>(this, j));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("index >= 0 required but it was ");
        sb.append(j);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @SchedulerSupport("none")
    public final Single<T> elementAt(long j, T t) {
        if (j < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("index >= 0 required but it was ");
            sb.append(j);
            throw new IndexOutOfBoundsException(sb.toString());
        }
        ObjectHelper.requireNonNull(t, "defaultItem is null");
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableElementAtSingle<T>(this, j, t));
    }

    @SchedulerSupport("none")
    public final Single<T> elementAtOrError(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly((Single<T>) new ObservableElementAtSingle<T>(this, j, null));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("index >= 0 required but it was ");
        sb.append(j);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    @SchedulerSupport("none")
    public final Observable<T> filter(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFilter<T>(this, predicate));
    }

    @SchedulerSupport("none")
    public final Maybe<T> firstElement() {
        return elementAt(0);
    }

    @SchedulerSupport("none")
    public final Single<T> first(T t) {
        return elementAt(0, t);
    }

    @SchedulerSupport("none")
    public final Single<T> firstOrError() {
        return elementAtOrError(0);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> function) {
        return flatMap(function, false);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> function, boolean z) {
        return flatMap(function, z, Integer.MAX_VALUE);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> function, boolean z, int i) {
        return flatMap(function, z, i, bufferSize());
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> function, boolean z, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "bufferSize");
        if (this instanceof ScalarCallable) {
            Object call = ((ScalarCallable) this).call();
            if (call == null) {
                return empty();
            }
            return ObservableScalarXMap.scalarXMap(call, function);
        }
        ObservableFlatMap observableFlatMap = new ObservableFlatMap(this, function, z, i, i2);
        return RxJavaPlugins.onAssembly((Observable<T>) observableFlatMap);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> function, Function<? super Throwable, ? extends ObservableSource<? extends R>> function2, Callable<? extends ObservableSource<? extends R>> callable) {
        ObjectHelper.requireNonNull(function, "onNextMapper is null");
        ObjectHelper.requireNonNull(function2, "onErrorMapper is null");
        ObjectHelper.requireNonNull(callable, "onCompleteSupplier is null");
        return merge((ObservableSource<? extends ObservableSource<? extends T>>) new ObservableMapNotification<Object>(this, function, function2, callable));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> function, Function<Throwable, ? extends ObservableSource<? extends R>> function2, Callable<? extends ObservableSource<? extends R>> callable, int i) {
        ObjectHelper.requireNonNull(function, "onNextMapper is null");
        ObjectHelper.requireNonNull(function2, "onErrorMapper is null");
        ObjectHelper.requireNonNull(callable, "onCompleteSupplier is null");
        return merge((ObservableSource<? extends ObservableSource<? extends T>>) new ObservableMapNotification<Object>(this, function, function2, callable), i);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends R>> function, int i) {
        return flatMap(function, false, i, bufferSize());
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        return flatMap(function, biFunction, false, bufferSize(), bufferSize());
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction, boolean z) {
        return flatMap(function, biFunction, z, bufferSize(), bufferSize());
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction, boolean z, int i) {
        return flatMap(function, biFunction, z, i, bufferSize());
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction, boolean z, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.requireNonNull(biFunction, "combiner is null");
        return flatMap(ObservableInternalHelper.flatMapWithCombiner(function, biFunction), z, i, i2);
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> flatMap(Function<? super T, ? extends ObservableSource<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction, int i) {
        return flatMap(function, biFunction, false, i, bufferSize());
    }

    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> function) {
        return flatMapCompletable(function, false);
    }

    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends CompletableSource> function, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Completable) new ObservableFlatMapCompletableCompletable(this, function, z));
    }

    @SchedulerSupport("none")
    public final <U> Observable<U> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFlattenIterable<T>(this, function));
    }

    @SchedulerSupport("none")
    public final <U, V> Observable<V> flatMapIterable(Function<? super T, ? extends Iterable<? extends U>> function, BiFunction<? super T, ? super U, ? extends V> biFunction) {
        return flatMap(ObservableInternalHelper.flatMapIntoIterable(function), biFunction, false, bufferSize(), bufferSize());
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> function) {
        return flatMapMaybe(function, false);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> function, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFlatMapMaybe<T>(this, function, z));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> function) {
        return flatMapSingle(function, false);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> function, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableFlatMapSingle<T>(this, function, z));
    }

    @SchedulerSupport("none")
    public final Disposable forEach(Consumer<? super T> consumer) {
        return subscribe(consumer);
    }

    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> predicate) {
        return forEachWhile(predicate, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> predicate, Consumer<? super Throwable> consumer) {
        return forEachWhile(predicate, consumer, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Disposable forEachWhile(Predicate<? super T> predicate, Consumer<? super Throwable> consumer, Action action) {
        ObjectHelper.requireNonNull(predicate, "onNext is null");
        ObjectHelper.requireNonNull(consumer, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ForEachWhileObserver forEachWhileObserver = new ForEachWhileObserver(predicate, consumer, action);
        subscribe((Observer<? super T>) forEachWhileObserver);
        return forEachWhileObserver;
    }

    @SchedulerSupport("none")
    public final <K> Observable<GroupedObservable<K, T>> groupBy(Function<? super T, ? extends K> function) {
        return groupBy(function, Functions.identity(), false, bufferSize());
    }

    @SchedulerSupport("none")
    public final <K> Observable<GroupedObservable<K, T>> groupBy(Function<? super T, ? extends K> function, boolean z) {
        return groupBy(function, Functions.identity(), z, bufferSize());
    }

    @SchedulerSupport("none")
    public final <K, V> Observable<GroupedObservable<K, V>> groupBy(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        return groupBy(function, function2, false, bufferSize());
    }

    @SchedulerSupport("none")
    public final <K, V> Observable<GroupedObservable<K, V>> groupBy(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, boolean z) {
        return groupBy(function, function2, z, bufferSize());
    }

    @SchedulerSupport("none")
    public final <K, V> Observable<GroupedObservable<K, V>> groupBy(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, boolean z, int i) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableGroupBy observableGroupBy = new ObservableGroupBy(this, function, function2, i, z);
        return RxJavaPlugins.onAssembly((Observable<T>) observableGroupBy);
    }

    @SchedulerSupport("none")
    public final <TRight, TLeftEnd, TRightEnd, R> Observable<R> groupJoin(ObservableSource<? extends TRight> observableSource, Function<? super T, ? extends ObservableSource<TLeftEnd>> function, Function<? super TRight, ? extends ObservableSource<TRightEnd>> function2, BiFunction<? super T, ? super Observable<TRight>, ? extends R> biFunction) {
        ObservableGroupJoin observableGroupJoin = new ObservableGroupJoin(this, observableSource, function, function2, biFunction);
        return RxJavaPlugins.onAssembly((Observable<T>) observableGroupJoin);
    }

    @SchedulerSupport("none")
    public final Observable<T> hide() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableHide<T>(this));
    }

    @SchedulerSupport("none")
    public final Completable ignoreElements() {
        return RxJavaPlugins.onAssembly((Completable) new ObservableIgnoreElementsCompletable(this));
    }

    @SchedulerSupport("none")
    public final Single<Boolean> isEmpty() {
        return all(Functions.alwaysFalse());
    }

    @SchedulerSupport("none")
    public final <TRight, TLeftEnd, TRightEnd, R> Observable<R> join(ObservableSource<? extends TRight> observableSource, Function<? super T, ? extends ObservableSource<TLeftEnd>> function, Function<? super TRight, ? extends ObservableSource<TRightEnd>> function2, BiFunction<? super T, ? super TRight, ? extends R> biFunction) {
        ObservableJoin observableJoin = new ObservableJoin(this, observableSource, function, function2, biFunction);
        return RxJavaPlugins.onAssembly((Observable<T>) observableJoin);
    }

    @SchedulerSupport("none")
    public final Maybe<T> lastElement() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new ObservableLastMaybe<T>(this));
    }

    @SchedulerSupport("none")
    public final Single<T> last(T t) {
        ObjectHelper.requireNonNull(t, "defaultItem is null");
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableLastSingle<T>(this, t));
    }

    @SchedulerSupport("none")
    public final Single<T> lastOrError() {
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableLastSingle<T>(this, null));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> lift(ObservableOperator<? extends R, ? super T> observableOperator) {
        ObjectHelper.requireNonNull(observableOperator, "onLift is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableLift<T>(this, observableOperator));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> map(Function<? super T, ? extends R> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableMap<T>(this, function));
    }

    @SchedulerSupport("none")
    public final Observable<Notification<T>> materialize() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableMaterialize<T>(this));
    }

    @SchedulerSupport("none")
    public final Observable<T> mergeWith(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return merge((ObservableSource<? extends T>) this, observableSource);
    }

    @SchedulerSupport("custom")
    public final Observable<T> observeOn(Scheduler scheduler) {
        return observeOn(scheduler, false, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> observeOn(Scheduler scheduler, boolean z) {
        return observeOn(scheduler, z, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> observeOn(Scheduler scheduler, boolean z, int i) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableObserveOn<T>(this, scheduler, z, i));
    }

    @SchedulerSupport("none")
    public final <U> Observable<U> ofType(Class<U> cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return filter(Functions.isInstanceOf(cls)).cast(cls);
    }

    @SchedulerSupport("none")
    public final Observable<T> onErrorResumeNext(Function<? super Throwable, ? extends ObservableSource<? extends T>> function) {
        ObjectHelper.requireNonNull(function, "resumeFunction is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableOnErrorNext<T>(this, function, false));
    }

    @SchedulerSupport("none")
    public final Observable<T> onErrorResumeNext(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "next is null");
        return onErrorResumeNext(Functions.justFunction(observableSource));
    }

    @SchedulerSupport("none")
    public final Observable<T> onErrorReturn(Function<? super Throwable, ? extends T> function) {
        ObjectHelper.requireNonNull(function, "valueSupplier is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableOnErrorReturn<T>(this, function));
    }

    @SchedulerSupport("none")
    public final Observable<T> onErrorReturnItem(T t) {
        ObjectHelper.requireNonNull(t, "item is null");
        return onErrorReturn(Functions.justFunction(t));
    }

    @SchedulerSupport("none")
    public final Observable<T> onExceptionResumeNext(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "next is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableOnErrorNext<T>(this, Functions.justFunction(observableSource), true));
    }

    @SchedulerSupport("none")
    public final Observable<T> onTerminateDetach() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableDetach<T>(this));
    }

    @SchedulerSupport("none")
    public final ConnectableObservable<T> publish() {
        return ObservablePublish.create(this);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> publish(Function<? super Observable<T>, ? extends ObservableSource<R>> function) {
        ObjectHelper.requireNonNull(function, "selector is null");
        return new ObservablePublishSelector(this, function);
    }

    @SchedulerSupport("none")
    public final Maybe<T> reduce(BiFunction<T, T, T> biFunction) {
        return scan(biFunction).takeLast(1).singleElement();
    }

    @SchedulerSupport("none")
    public final <R> Single<R> reduce(R r, BiFunction<R, ? super T, R> biFunction) {
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableSingleSingle<T>(scan(r, biFunction).takeLast(1), null));
    }

    @SchedulerSupport("none")
    public final <R> Single<R> reduceWith(Callable<R> callable, BiFunction<R, ? super T, R> biFunction) {
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableSingleSingle<T>(scanWith(callable, biFunction).takeLast(1), null));
    }

    @SchedulerSupport("none")
    public final Observable<T> repeat() {
        return repeat(LongCompanionObject.MAX_VALUE);
    }

    @SchedulerSupport("none")
    public final Observable<T> repeat(long j) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("times >= 0 required but it was ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        } else if (i == 0) {
            return empty();
        } else {
            return RxJavaPlugins.onAssembly((Observable<T>) new ObservableRepeat<T>(this, j));
        }
    }

    @SchedulerSupport("none")
    public final Observable<T> repeatUntil(BooleanSupplier booleanSupplier) {
        ObjectHelper.requireNonNull(booleanSupplier, "stop is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableRepeatUntil<T>(this, booleanSupplier));
    }

    @SchedulerSupport("none")
    public final Observable<T> repeatWhen(Function<? super Observable<Object>, ? extends ObservableSource<?>> function) {
        ObjectHelper.requireNonNull(function, "handler is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableRedo<T>(this, ObservableInternalHelper.repeatWhenHandler(function), false));
    }

    @SchedulerSupport("none")
    public final ConnectableObservable<T> replay() {
        return ObservableReplay.createFrom(this);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function) {
        ObjectHelper.requireNonNull(function, "selector is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this), function);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function, int i) {
        ObjectHelper.requireNonNull(function, "selector is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, i), function);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function, int i, long j, TimeUnit timeUnit) {
        return replay(function, i, j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function, int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObjectHelper.requireNonNull(function, "selector is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, i, j, timeUnit, scheduler), function);
    }

    @SchedulerSupport("custom")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function, int i, Scheduler scheduler) {
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, i), ObservableInternalHelper.replayFunction(function, scheduler));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function, long j, TimeUnit timeUnit) {
        return replay(function, j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this, j, timeUnit, scheduler), function);
    }

    @SchedulerSupport("custom")
    public final <R> Observable<R> replay(Function<? super Observable<T>, ? extends ObservableSource<R>> function, Scheduler scheduler) {
        ObjectHelper.requireNonNull(function, "selector is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.multicastSelector(ObservableInternalHelper.replayCallable(this), ObservableInternalHelper.replayFunction(function, scheduler));
    }

    @SchedulerSupport("none")
    public final ConnectableObservable<T> replay(int i) {
        return ObservableReplay.create((ObservableSource<T>) this, i);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final ConnectableObservable<T> replay(int i, long j, TimeUnit timeUnit) {
        return replay(i, j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final ConnectableObservable<T> replay(int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.create(this, j, timeUnit, scheduler, i);
    }

    @SchedulerSupport("custom")
    public final ConnectableObservable<T> replay(int i, Scheduler scheduler) {
        return ObservableReplay.observeOn(replay(i), scheduler);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final ConnectableObservable<T> replay(long j, TimeUnit timeUnit) {
        return replay(j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final ConnectableObservable<T> replay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.create(this, j, timeUnit, scheduler);
    }

    @SchedulerSupport("custom")
    public final ConnectableObservable<T> replay(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return ObservableReplay.observeOn(replay(), scheduler);
    }

    @SchedulerSupport("none")
    public final Observable<T> retry() {
        return retry(LongCompanionObject.MAX_VALUE, Functions.alwaysTrue());
    }

    @SchedulerSupport("none")
    public final Observable<T> retry(BiPredicate<? super Integer, ? super Throwable> biPredicate) {
        ObjectHelper.requireNonNull(biPredicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableRetryBiPredicate<T>(this, biPredicate));
    }

    @SchedulerSupport("none")
    public final Observable<T> retry(long j) {
        return retry(j, Functions.alwaysTrue());
    }

    @SchedulerSupport("none")
    public final Observable<T> retry(long j, Predicate<? super Throwable> predicate) {
        if (j < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("times >= 0 required but it was ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        }
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableRetryPredicate<T>(this, j, predicate));
    }

    @SchedulerSupport("none")
    public final Observable<T> retry(Predicate<? super Throwable> predicate) {
        return retry(LongCompanionObject.MAX_VALUE, predicate);
    }

    @SchedulerSupport("none")
    public final Observable<T> retryUntil(BooleanSupplier booleanSupplier) {
        ObjectHelper.requireNonNull(booleanSupplier, "stop is null");
        return retry(LongCompanionObject.MAX_VALUE, Functions.predicateReverseFor(booleanSupplier));
    }

    @SchedulerSupport("none")
    public final Observable<T> retryWhen(Function<? super Observable<Throwable>, ? extends ObservableSource<?>> function) {
        ObjectHelper.requireNonNull(function, "handler is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableRedo<T>(this, ObservableInternalHelper.retryWhenHandler(function), true));
    }

    @SchedulerSupport("none")
    public final void safeSubscribe(Observer<? super T> observer) {
        ObjectHelper.requireNonNull(observer, "s is null");
        if (observer instanceof SafeObserver) {
            subscribe(observer);
        } else {
            subscribe((Observer<? super T>) new SafeObserver<Object>(observer));
        }
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> sample(long j, TimeUnit timeUnit) {
        return sample(j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Observable<T> sample(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableSampleTimed observableSampleTimed = new ObservableSampleTimed(this, j, timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Observable<T>) observableSampleTimed);
    }

    @SchedulerSupport("none")
    public final <U> Observable<T> sample(ObservableSource<U> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "sampler is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableSampleWithObservable<T>(this, observableSource));
    }

    @SchedulerSupport("none")
    public final Observable<T> scan(BiFunction<T, T, T> biFunction) {
        ObjectHelper.requireNonNull(biFunction, "accumulator is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableScan<T>(this, biFunction));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> scan(R r, BiFunction<R, ? super T, R> biFunction) {
        ObjectHelper.requireNonNull(r, "seed is null");
        return scanWith(Functions.justCallable(r), biFunction);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> scanWith(Callable<R> callable, BiFunction<R, ? super T, R> biFunction) {
        ObjectHelper.requireNonNull(callable, "seedSupplier is null");
        ObjectHelper.requireNonNull(biFunction, "accumulator is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableScanSeed<T>(this, callable, biFunction));
    }

    @SchedulerSupport("none")
    public final Observable<T> serialize() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableSerialized<T>(this));
    }

    @SchedulerSupport("none")
    public final Observable<T> share() {
        return publish().refCount();
    }

    @SchedulerSupport("none")
    public final Maybe<T> singleElement() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new ObservableSingleMaybe<T>(this));
    }

    @SchedulerSupport("none")
    public final Single<T> single(T t) {
        ObjectHelper.requireNonNull(t, "defaultItem is null");
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableSingleSingle<T>(this, t));
    }

    @SchedulerSupport("none")
    public final Single<T> singleOrError() {
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableSingleSingle<T>(this, null));
    }

    @SchedulerSupport("none")
    public final Observable<T> skip(long j) {
        if (j <= 0) {
            return RxJavaPlugins.onAssembly(this);
        }
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableSkip<T>(this, j));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> skip(long j, TimeUnit timeUnit) {
        return skipUntil(timer(j, timeUnit));
    }

    @SchedulerSupport("custom")
    public final Observable<T> skip(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return skipUntil(timer(j, timeUnit, scheduler));
    }

    @SchedulerSupport("none")
    public final Observable<T> skipLast(int i) {
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(i);
            throw new IndexOutOfBoundsException(sb.toString());
        } else if (i == 0) {
            return RxJavaPlugins.onAssembly(this);
        } else {
            return RxJavaPlugins.onAssembly((Observable<T>) new ObservableSkipLast<T>(this, i));
        }
    }

    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable<T> skipLast(long j, TimeUnit timeUnit) {
        return skipLast(j, timeUnit, Schedulers.trampoline(), false, bufferSize());
    }

    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable<T> skipLast(long j, TimeUnit timeUnit, boolean z) {
        return skipLast(j, timeUnit, Schedulers.trampoline(), z, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> skipLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return skipLast(j, timeUnit, scheduler, false, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> skipLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        return skipLast(j, timeUnit, scheduler, z, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> skipLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableSkipLastTimed observableSkipLastTimed = new ObservableSkipLastTimed(this, j, timeUnit, scheduler, i << 1, z);
        return RxJavaPlugins.onAssembly((Observable<T>) observableSkipLastTimed);
    }

    @SchedulerSupport("none")
    public final <U> Observable<T> skipUntil(ObservableSource<U> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableSkipUntil<T>(this, observableSource));
    }

    @SchedulerSupport("none")
    public final Observable<T> skipWhile(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableSkipWhile<T>(this, predicate));
    }

    @SchedulerSupport("none")
    public final Observable<T> sorted() {
        return toList().toObservable().map(Functions.listSorter(Functions.naturalComparator())).flatMapIterable(Functions.identity());
    }

    @SchedulerSupport("none")
    public final Observable<T> sorted(Comparator<? super T> comparator) {
        return toList().toObservable().map(Functions.listSorter(comparator)).flatMapIterable(Functions.identity());
    }

    @SchedulerSupport("none")
    public final Observable<T> startWith(Iterable<? extends T> iterable) {
        return concatArray(fromIterable(iterable), this);
    }

    @SchedulerSupport("none")
    public final Observable<T> startWith(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return concatArray(observableSource, this);
    }

    @SchedulerSupport("none")
    public final Observable<T> startWith(T t) {
        ObjectHelper.requireNonNull(t, "item is null");
        return concatArray(just(t), this);
    }

    @SchedulerSupport("none")
    public final Observable<T> startWithArray(T... tArr) {
        Observable fromArray = fromArray(tArr);
        if (fromArray == empty()) {
            return RxJavaPlugins.onAssembly(this);
        }
        return concatArray(fromArray, this);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION, Functions.emptyConsumer());
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> consumer) {
        return subscribe(consumer, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION, Functions.emptyConsumer());
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2) {
        return subscribe(consumer, consumer2, Functions.EMPTY_ACTION, Functions.emptyConsumer());
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action) {
        return subscribe(consumer, consumer2, action, Functions.emptyConsumer());
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action, Consumer<? super Disposable> consumer3) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ObjectHelper.requireNonNull(consumer2, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ObjectHelper.requireNonNull(consumer3, "onSubscribe is null");
        LambdaObserver lambdaObserver = new LambdaObserver(consumer, consumer2, action, consumer3);
        subscribe((Observer<? super T>) lambdaObserver);
        return lambdaObserver;
    }

    @SchedulerSupport("none")
    public final void subscribe(Observer<? super T> observer) {
        ObjectHelper.requireNonNull(observer, "observer is null");
        try {
            Observer onSubscribe = RxJavaPlugins.onSubscribe(this, observer);
            ObjectHelper.requireNonNull(onSubscribe, "Plugin returned null Observer");
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

    @SchedulerSupport("none")
    public final <E extends Observer<? super T>> E subscribeWith(E e) {
        subscribe((Observer<? super T>) e);
        return e;
    }

    @SchedulerSupport("custom")
    public final Observable<T> subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableSubscribeOn<T>(this, scheduler));
    }

    @SchedulerSupport("none")
    public final Observable<T> switchIfEmpty(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableSwitchIfEmpty<T>(this, observableSource));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> switchMap(Function<? super T, ? extends ObservableSource<? extends R>> function) {
        return switchMap(function, bufferSize());
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> switchMap(Function<? super T, ? extends ObservableSource<? extends R>> function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        if (!(this instanceof ScalarCallable)) {
            return RxJavaPlugins.onAssembly((Observable<T>) new ObservableSwitchMap<T>(this, function, i, false));
        }
        Object call = ((ScalarCallable) this).call();
        if (call == null) {
            return empty();
        }
        return ObservableScalarXMap.scalarXMap(call, function);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> switchMapDelayError(Function<? super T, ? extends ObservableSource<? extends R>> function) {
        return switchMapDelayError(function, bufferSize());
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> switchMapDelayError(Function<? super T, ? extends ObservableSource<? extends R>> function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "bufferSize");
        if (!(this instanceof ScalarCallable)) {
            return RxJavaPlugins.onAssembly((Observable<T>) new ObservableSwitchMap<T>(this, function, i, true));
        }
        Object call = ((ScalarCallable) this).call();
        if (call == null) {
            return empty();
        }
        return ObservableScalarXMap.scalarXMap(call, function);
    }

    @SchedulerSupport("none")
    public final Observable<T> take(long j) {
        if (j >= 0) {
            return RxJavaPlugins.onAssembly((Observable<T>) new ObservableTake<T>(this, j));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("count >= 0 required but it was ");
        sb.append(j);
        throw new IllegalArgumentException(sb.toString());
    }

    @SchedulerSupport("none")
    public final Observable<T> take(long j, TimeUnit timeUnit) {
        return takeUntil((ObservableSource<U>) timer(j, timeUnit));
    }

    @SchedulerSupport("custom")
    public final Observable<T> take(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return takeUntil((ObservableSource<U>) timer(j, timeUnit, scheduler));
    }

    @SchedulerSupport("none")
    public final Observable<T> takeLast(int i) {
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("count >= 0 required but it was ");
            sb.append(i);
            throw new IndexOutOfBoundsException(sb.toString());
        } else if (i == 0) {
            return RxJavaPlugins.onAssembly((Observable<T>) new ObservableIgnoreElements<T>(this));
        } else {
            if (i == 1) {
                return RxJavaPlugins.onAssembly((Observable<T>) new ObservableTakeLastOne<T>(this));
            }
            return RxJavaPlugins.onAssembly((Observable<T>) new ObservableTakeLast<T>(this, i));
        }
    }

    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable<T> takeLast(long j, long j2, TimeUnit timeUnit) {
        return takeLast(j, j2, timeUnit, Schedulers.trampoline(), false, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> takeLast(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return takeLast(j, j2, timeUnit, scheduler, false, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> takeLast(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
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
        ObservableTakeLastTimed observableTakeLastTimed = new ObservableTakeLastTimed(this, j3, j2, timeUnit2, scheduler2, i2, z);
        return RxJavaPlugins.onAssembly((Observable<T>) observableTakeLastTimed);
    }

    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable<T> takeLast(long j, TimeUnit timeUnit) {
        return takeLast(j, timeUnit, Schedulers.trampoline(), false, bufferSize());
    }

    @SchedulerSupport("io.reactivex:trampoline")
    public final Observable<T> takeLast(long j, TimeUnit timeUnit, boolean z) {
        return takeLast(j, timeUnit, Schedulers.trampoline(), z, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> takeLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return takeLast(j, timeUnit, scheduler, false, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> takeLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        return takeLast(j, timeUnit, scheduler, z, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<T> takeLast(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z, int i) {
        return takeLast(LongCompanionObject.MAX_VALUE, j, timeUnit, scheduler, z, i);
    }

    @SchedulerSupport("none")
    public final <U> Observable<T> takeUntil(ObservableSource<U> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableTakeUntil<T>(this, observableSource));
    }

    @SchedulerSupport("none")
    public final Observable<T> takeUntil(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableTakeUntilPredicate<T>(this, predicate));
    }

    @SchedulerSupport("none")
    public final Observable<T> takeWhile(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableTakeWhile<T>(this, predicate));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> throttleFirst(long j, TimeUnit timeUnit) {
        return throttleFirst(j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Observable<T> throttleFirst(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableThrottleFirstTimed observableThrottleFirstTimed = new ObservableThrottleFirstTimed(this, j, timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Observable<T>) observableThrottleFirstTimed);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> throttleLast(long j, TimeUnit timeUnit) {
        return sample(j, timeUnit);
    }

    @SchedulerSupport("custom")
    public final Observable<T> throttleLast(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return sample(j, timeUnit, scheduler);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> throttleWithTimeout(long j, TimeUnit timeUnit) {
        return debounce(j, timeUnit);
    }

    @SchedulerSupport("custom")
    public final Observable<T> throttleWithTimeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return debounce(j, timeUnit, scheduler);
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timeInterval() {
        return timeInterval(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timeInterval(Scheduler scheduler) {
        return timeInterval(TimeUnit.MILLISECONDS, scheduler);
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timeInterval(TimeUnit timeUnit) {
        return timeInterval(timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timeInterval(TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableTimeInterval<T>(this, timeUnit, scheduler));
    }

    @SchedulerSupport("none")
    public final <V> Observable<T> timeout(Function<? super T, ? extends ObservableSource<V>> function) {
        return timeout0(null, function, null);
    }

    @SchedulerSupport("none")
    public final <V> Observable<T> timeout(Function<? super T, ? extends ObservableSource<V>> function, ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return timeout0(null, function, observableSource);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> timeout(long j, TimeUnit timeUnit) {
        return timeout0(j, timeUnit, null, Schedulers.computation());
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> timeout(long j, TimeUnit timeUnit, ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return timeout0(j, timeUnit, observableSource, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Observable<T> timeout(long j, TimeUnit timeUnit, Scheduler scheduler, ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return timeout0(j, timeUnit, observableSource, scheduler);
    }

    @SchedulerSupport("custom")
    public final Observable<T> timeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout0(j, timeUnit, null, scheduler);
    }

    @SchedulerSupport("none")
    public final <U, V> Observable<T> timeout(ObservableSource<U> observableSource, Function<? super T, ? extends ObservableSource<V>> function) {
        ObjectHelper.requireNonNull(observableSource, "firstTimeoutIndicator is null");
        return timeout0(observableSource, function, null);
    }

    @SchedulerSupport("none")
    public final <U, V> Observable<T> timeout(ObservableSource<U> observableSource, Function<? super T, ? extends ObservableSource<V>> function, ObservableSource<? extends T> observableSource2) {
        ObjectHelper.requireNonNull(observableSource, "firstTimeoutIndicator is null");
        ObjectHelper.requireNonNull(observableSource2, "other is null");
        return timeout0(observableSource, function, observableSource2);
    }

    private Observable<T> timeout0(long j, TimeUnit timeUnit, ObservableSource<? extends T> observableSource, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "timeUnit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableTimeoutTimed observableTimeoutTimed = new ObservableTimeoutTimed(this, j, timeUnit, scheduler, observableSource);
        return RxJavaPlugins.onAssembly((Observable<T>) observableTimeoutTimed);
    }

    private <U, V> Observable<T> timeout0(ObservableSource<U> observableSource, Function<? super T, ? extends ObservableSource<V>> function, ObservableSource<? extends T> observableSource2) {
        ObjectHelper.requireNonNull(function, "itemTimeoutIndicator is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableTimeout<T>(this, observableSource, function, observableSource2));
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timestamp() {
        return timestamp(TimeUnit.MILLISECONDS, Schedulers.computation());
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timestamp(Scheduler scheduler) {
        return timestamp(TimeUnit.MILLISECONDS, scheduler);
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timestamp(TimeUnit timeUnit) {
        return timestamp(timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("none")
    public final Observable<Timed<T>> timestamp(TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return map(Functions.timestampWith(timeUnit, scheduler));
    }

    @SchedulerSupport("none")
    /* renamed from: to */
    public final <R> R mo13124to(Function<? super Observable<T>, R> function) {
        try {
            return function.apply(this);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @SchedulerSupport("none")
    public final Single<List<T>> toList() {
        return toList(16);
    }

    @SchedulerSupport("none")
    public final Single<List<T>> toList(int i) {
        ObjectHelper.verifyPositive(i, "capacityHint");
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableToListSingle<T>((ObservableSource<T>) this, i));
    }

    @SchedulerSupport("none")
    public final <U extends Collection<? super T>> Single<U> toList(Callable<U> callable) {
        ObjectHelper.requireNonNull(callable, "collectionSupplier is null");
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableToListSingle<T>((ObservableSource<T>) this, callable));
    }

    @SchedulerSupport("none")
    public final <K> Single<Map<K, T>> toMap(Function<? super T, ? extends K> function) {
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeySelector(function));
    }

    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        return collect(HashMapSupplier.asCallable(), Functions.toMapKeyValueSelector(function, function2));
    }

    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, V>> toMap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, Callable<? extends Map<K, V>> callable) {
        return collect(callable, Functions.toMapKeyValueSelector(function, function2));
    }

    @SchedulerSupport("none")
    public final <K> Single<Map<K, Collection<T>>> toMultimap(Function<? super T, ? extends K> function) {
        return toMultimap(function, Functions.identity(), HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
    }

    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        return toMultimap(function, function2, HashMapSupplier.asCallable(), ArrayListSupplier.asFunction());
    }

    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, Callable<? extends Map<K, Collection<V>>> callable, Function<? super K, ? extends Collection<? super V>> function3) {
        ObjectHelper.requireNonNull(function, "keySelector is null");
        ObjectHelper.requireNonNull(function2, "valueSelector is null");
        ObjectHelper.requireNonNull(callable, "mapSupplier is null");
        ObjectHelper.requireNonNull(function3, "collectionFactory is null");
        return collect(callable, Functions.toMultimapKeyValueSelector(function, function2, function3));
    }

    @SchedulerSupport("none")
    public final <K, V> Single<Map<K, Collection<V>>> toMultimap(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, Callable<Map<K, Collection<V>>> callable) {
        return toMultimap(function, function2, callable, ArrayListSupplier.asFunction());
    }

    @BackpressureSupport(BackpressureKind.SPECIAL)
    @SchedulerSupport("none")
    public final Flowable<T> toFlowable(BackpressureStrategy backpressureStrategy) {
        FlowableFromObservable flowableFromObservable = new FlowableFromObservable(this);
        switch (backpressureStrategy) {
            case DROP:
                return flowableFromObservable.onBackpressureDrop();
            case LATEST:
                return flowableFromObservable.onBackpressureLatest();
            case MISSING:
                return flowableFromObservable;
            case ERROR:
                return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableOnBackpressureError<T>(flowableFromObservable));
            default:
                return flowableFromObservable.onBackpressureBuffer();
        }
    }

    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList() {
        return toSortedList(Functions.naturalOrder());
    }

    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(Comparator<? super T> comparator) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList().map(Functions.listSorter(comparator));
    }

    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(Comparator<? super T> comparator, int i) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        return toList(i).map(Functions.listSorter(comparator));
    }

    @SchedulerSupport("none")
    public final Single<List<T>> toSortedList(int i) {
        return toSortedList(Functions.naturalOrder(), i);
    }

    @SchedulerSupport("custom")
    public final Observable<T> unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableUnsubscribeOn<T>(this, scheduler));
    }

    @SchedulerSupport("none")
    public final Observable<Observable<T>> window(long j) {
        return window(j, j, bufferSize());
    }

    @SchedulerSupport("none")
    public final Observable<Observable<T>> window(long j, long j2) {
        return window(j, j2, bufferSize());
    }

    @SchedulerSupport("none")
    public final Observable<Observable<T>> window(long j, long j2, int i) {
        ObjectHelper.verifyPositive(j, "count");
        ObjectHelper.verifyPositive(j2, "skip");
        ObjectHelper.verifyPositive(i, "bufferSize");
        ObservableWindow observableWindow = new ObservableWindow(this, j, j2, i);
        return RxJavaPlugins.onAssembly((Observable<T>) observableWindow);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<Observable<T>> window(long j, long j2, TimeUnit timeUnit) {
        return window(j, j2, timeUnit, Schedulers.computation(), bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<Observable<T>> window(long j, long j2, TimeUnit timeUnit, Scheduler scheduler) {
        return window(j, j2, timeUnit, scheduler, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<Observable<T>> window(long j, long j2, TimeUnit timeUnit, Scheduler scheduler, int i) {
        long j3 = j;
        ObjectHelper.verifyPositive(j3, "timespan");
        long j4 = j2;
        ObjectHelper.verifyPositive(j4, "timeskip");
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "bufferSize");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        ObservableWindowTimed observableWindowTimed = new ObservableWindowTimed(this, j3, j4, timeUnit2, scheduler2, LongCompanionObject.MAX_VALUE, i2, false);
        return RxJavaPlugins.onAssembly((Observable<T>) observableWindowTimed);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<Observable<T>> window(long j, TimeUnit timeUnit) {
        return window(j, timeUnit, Schedulers.computation(), (long) LongCompanionObject.MAX_VALUE, false);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<Observable<T>> window(long j, TimeUnit timeUnit, long j2) {
        return window(j, timeUnit, Schedulers.computation(), j2, false);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Observable<Observable<T>> window(long j, TimeUnit timeUnit, long j2, boolean z) {
        return window(j, timeUnit, Schedulers.computation(), j2, z);
    }

    @SchedulerSupport("custom")
    public final Observable<Observable<T>> window(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return window(j, timeUnit, scheduler, (long) LongCompanionObject.MAX_VALUE, false);
    }

    @SchedulerSupport("custom")
    public final Observable<Observable<T>> window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2) {
        return window(j, timeUnit, scheduler, j2, false);
    }

    @SchedulerSupport("custom")
    public final Observable<Observable<T>> window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2, boolean z) {
        return window(j, timeUnit, scheduler, j2, z, bufferSize());
    }

    @SchedulerSupport("custom")
    public final Observable<Observable<T>> window(long j, TimeUnit timeUnit, Scheduler scheduler, long j2, boolean z, int i) {
        int i2 = i;
        ObjectHelper.verifyPositive(i2, "bufferSize");
        Scheduler scheduler2 = scheduler;
        ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
        TimeUnit timeUnit2 = timeUnit;
        ObjectHelper.requireNonNull(timeUnit2, "unit is null");
        long j3 = j2;
        ObjectHelper.verifyPositive(j3, "count");
        ObservableWindowTimed observableWindowTimed = new ObservableWindowTimed(this, j, j, timeUnit2, scheduler2, j3, i2, z);
        return RxJavaPlugins.onAssembly((Observable<T>) observableWindowTimed);
    }

    @SchedulerSupport("none")
    public final <B> Observable<Observable<T>> window(ObservableSource<B> observableSource) {
        return window(observableSource, bufferSize());
    }

    @SchedulerSupport("none")
    public final <B> Observable<Observable<T>> window(ObservableSource<B> observableSource, int i) {
        ObjectHelper.requireNonNull(observableSource, "boundary is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableWindowBoundary<T>(this, observableSource, i));
    }

    @SchedulerSupport("none")
    public final <U, V> Observable<Observable<T>> window(ObservableSource<U> observableSource, Function<? super U, ? extends ObservableSource<V>> function) {
        return window(observableSource, function, bufferSize());
    }

    @SchedulerSupport("none")
    public final <U, V> Observable<Observable<T>> window(ObservableSource<U> observableSource, Function<? super U, ? extends ObservableSource<V>> function, int i) {
        ObjectHelper.requireNonNull(observableSource, "openingIndicator is null");
        ObjectHelper.requireNonNull(function, "closingIndicator is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableWindowBoundarySelector<T>(this, observableSource, function, i));
    }

    @SchedulerSupport("none")
    public final <B> Observable<Observable<T>> window(Callable<? extends ObservableSource<B>> callable) {
        return window(callable, bufferSize());
    }

    @SchedulerSupport("none")
    public final <B> Observable<Observable<T>> window(Callable<? extends ObservableSource<B>> callable, int i) {
        ObjectHelper.requireNonNull(callable, "boundary is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableWindowBoundarySupplier<T>(this, callable, i));
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> withLatestFrom(ObservableSource<? extends U> observableSource, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        ObjectHelper.requireNonNull(biFunction, "combiner is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableWithLatestFrom<T>(this, biFunction, observableSource));
    }

    @SchedulerSupport("none")
    public final <T1, T2, R> Observable<R> withLatestFrom(ObservableSource<T1> observableSource, ObservableSource<T2> observableSource2, Function3<? super T, ? super T1, ? super T2, R> function3) {
        ObjectHelper.requireNonNull(observableSource, "o1 is null");
        ObjectHelper.requireNonNull(observableSource2, "o2 is null");
        ObjectHelper.requireNonNull(function3, "combiner is null");
        return withLatestFrom((ObservableSource<?>[]) new ObservableSource[]{observableSource, observableSource2}, Functions.toFunction(function3));
    }

    @SchedulerSupport("none")
    public final <T1, T2, T3, R> Observable<R> withLatestFrom(ObservableSource<T1> observableSource, ObservableSource<T2> observableSource2, ObservableSource<T3> observableSource3, Function4<? super T, ? super T1, ? super T2, ? super T3, R> function4) {
        ObjectHelper.requireNonNull(observableSource, "o1 is null");
        ObjectHelper.requireNonNull(observableSource2, "o2 is null");
        ObjectHelper.requireNonNull(observableSource3, "o3 is null");
        ObjectHelper.requireNonNull(function4, "combiner is null");
        return withLatestFrom((ObservableSource<?>[]) new ObservableSource[]{observableSource, observableSource2, observableSource3}, Functions.toFunction(function4));
    }

    @SchedulerSupport("none")
    public final <T1, T2, T3, T4, R> Observable<R> withLatestFrom(ObservableSource<T1> observableSource, ObservableSource<T2> observableSource2, ObservableSource<T3> observableSource3, ObservableSource<T4> observableSource4, Function5<? super T, ? super T1, ? super T2, ? super T3, ? super T4, R> function5) {
        ObjectHelper.requireNonNull(observableSource, "o1 is null");
        ObjectHelper.requireNonNull(observableSource2, "o2 is null");
        ObjectHelper.requireNonNull(observableSource3, "o3 is null");
        ObjectHelper.requireNonNull(observableSource4, "o4 is null");
        ObjectHelper.requireNonNull(function5, "combiner is null");
        return withLatestFrom((ObservableSource<?>[]) new ObservableSource[]{observableSource, observableSource2, observableSource3, observableSource4}, Functions.toFunction(function5));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> withLatestFrom(ObservableSource<?>[] observableSourceArr, Function<? super Object[], R> function) {
        ObjectHelper.requireNonNull(observableSourceArr, "others is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableWithLatestFromMany<T>((ObservableSource<T>) this, observableSourceArr, function));
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> withLatestFrom(Iterable<? extends ObservableSource<?>> iterable, Function<? super Object[], R> function) {
        ObjectHelper.requireNonNull(iterable, "others is null");
        ObjectHelper.requireNonNull(function, "combiner is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableWithLatestFromMany<T>((ObservableSource<T>) this, iterable, function));
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> zipWith(Iterable<U> iterable, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(iterable, "other is null");
        ObjectHelper.requireNonNull(biFunction, "zipper is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableZipIterable<T>(this, iterable, biFunction));
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> zipWith(ObservableSource<? extends U> observableSource, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(observableSource, "other is null");
        return zip(this, observableSource, biFunction);
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> zipWith(ObservableSource<? extends U> observableSource, BiFunction<? super T, ? super U, ? extends R> biFunction, boolean z) {
        return zip((ObservableSource<? extends T1>) this, observableSource, biFunction, z);
    }

    @SchedulerSupport("none")
    public final <U, R> Observable<R> zipWith(ObservableSource<? extends U> observableSource, BiFunction<? super T, ? super U, ? extends R> biFunction, boolean z, int i) {
        return zip((ObservableSource<? extends T1>) this, observableSource, biFunction, z, i);
    }

    @SchedulerSupport("none")
    public final TestObserver<T> test() {
        TestObserver<T> testObserver = new TestObserver<>();
        subscribe((Observer<? super T>) testObserver);
        return testObserver;
    }

    @SchedulerSupport("none")
    public final TestObserver<T> test(boolean z) {
        TestObserver<T> testObserver = new TestObserver<>();
        if (z) {
            testObserver.dispose();
        }
        subscribe((Observer<? super T>) testObserver);
        return testObserver;
    }
}
