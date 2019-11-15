package p016io.reactivex;

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
import p016io.reactivex.internal.fuseable.FuseToFlowable;
import p016io.reactivex.internal.fuseable.FuseToObservable;
import p016io.reactivex.internal.observers.BlockingMultiObserver;
import p016io.reactivex.internal.operators.flowable.FlowableConcatMap;
import p016io.reactivex.internal.operators.flowable.FlowableFlatMap;
import p016io.reactivex.internal.operators.maybe.MaybeAmb;
import p016io.reactivex.internal.operators.maybe.MaybeCache;
import p016io.reactivex.internal.operators.maybe.MaybeCallbackObserver;
import p016io.reactivex.internal.operators.maybe.MaybeConcatArray;
import p016io.reactivex.internal.operators.maybe.MaybeConcatArrayDelayError;
import p016io.reactivex.internal.operators.maybe.MaybeConcatIterable;
import p016io.reactivex.internal.operators.maybe.MaybeContains;
import p016io.reactivex.internal.operators.maybe.MaybeCount;
import p016io.reactivex.internal.operators.maybe.MaybeCreate;
import p016io.reactivex.internal.operators.maybe.MaybeDefer;
import p016io.reactivex.internal.operators.maybe.MaybeDelay;
import p016io.reactivex.internal.operators.maybe.MaybeDelayOtherPublisher;
import p016io.reactivex.internal.operators.maybe.MaybeDelaySubscriptionOtherPublisher;
import p016io.reactivex.internal.operators.maybe.MaybeDetach;
import p016io.reactivex.internal.operators.maybe.MaybeDoAfterSuccess;
import p016io.reactivex.internal.operators.maybe.MaybeDoFinally;
import p016io.reactivex.internal.operators.maybe.MaybeDoOnEvent;
import p016io.reactivex.internal.operators.maybe.MaybeEmpty;
import p016io.reactivex.internal.operators.maybe.MaybeEqualSingle;
import p016io.reactivex.internal.operators.maybe.MaybeError;
import p016io.reactivex.internal.operators.maybe.MaybeErrorCallable;
import p016io.reactivex.internal.operators.maybe.MaybeFilter;
import p016io.reactivex.internal.operators.maybe.MaybeFlatMapBiSelector;
import p016io.reactivex.internal.operators.maybe.MaybeFlatMapCompletable;
import p016io.reactivex.internal.operators.maybe.MaybeFlatMapIterableFlowable;
import p016io.reactivex.internal.operators.maybe.MaybeFlatMapIterableObservable;
import p016io.reactivex.internal.operators.maybe.MaybeFlatMapNotification;
import p016io.reactivex.internal.operators.maybe.MaybeFlatMapSingle;
import p016io.reactivex.internal.operators.maybe.MaybeFlatten;
import p016io.reactivex.internal.operators.maybe.MaybeFromAction;
import p016io.reactivex.internal.operators.maybe.MaybeFromCallable;
import p016io.reactivex.internal.operators.maybe.MaybeFromCompletable;
import p016io.reactivex.internal.operators.maybe.MaybeFromFuture;
import p016io.reactivex.internal.operators.maybe.MaybeFromRunnable;
import p016io.reactivex.internal.operators.maybe.MaybeFromSingle;
import p016io.reactivex.internal.operators.maybe.MaybeHide;
import p016io.reactivex.internal.operators.maybe.MaybeIgnoreElementCompletable;
import p016io.reactivex.internal.operators.maybe.MaybeIsEmptySingle;
import p016io.reactivex.internal.operators.maybe.MaybeJust;
import p016io.reactivex.internal.operators.maybe.MaybeLift;
import p016io.reactivex.internal.operators.maybe.MaybeMap;
import p016io.reactivex.internal.operators.maybe.MaybeMergeArray;
import p016io.reactivex.internal.operators.maybe.MaybeNever;
import p016io.reactivex.internal.operators.maybe.MaybeObserveOn;
import p016io.reactivex.internal.operators.maybe.MaybeOnErrorComplete;
import p016io.reactivex.internal.operators.maybe.MaybeOnErrorNext;
import p016io.reactivex.internal.operators.maybe.MaybeOnErrorReturn;
import p016io.reactivex.internal.operators.maybe.MaybePeek;
import p016io.reactivex.internal.operators.maybe.MaybeSubscribeOn;
import p016io.reactivex.internal.operators.maybe.MaybeSwitchIfEmpty;
import p016io.reactivex.internal.operators.maybe.MaybeTakeUntilMaybe;
import p016io.reactivex.internal.operators.maybe.MaybeTakeUntilPublisher;
import p016io.reactivex.internal.operators.maybe.MaybeTimeoutMaybe;
import p016io.reactivex.internal.operators.maybe.MaybeTimeoutPublisher;
import p016io.reactivex.internal.operators.maybe.MaybeTimer;
import p016io.reactivex.internal.operators.maybe.MaybeToFlowable;
import p016io.reactivex.internal.operators.maybe.MaybeToObservable;
import p016io.reactivex.internal.operators.maybe.MaybeToPublisher;
import p016io.reactivex.internal.operators.maybe.MaybeToSingle;
import p016io.reactivex.internal.operators.maybe.MaybeUnsafeCreate;
import p016io.reactivex.internal.operators.maybe.MaybeUnsubscribeOn;
import p016io.reactivex.internal.operators.maybe.MaybeUsing;
import p016io.reactivex.internal.operators.maybe.MaybeZipArray;
import p016io.reactivex.internal.operators.maybe.MaybeZipIterable;
import p016io.reactivex.internal.util.ErrorMode;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.observers.TestObserver;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.schedulers.Schedulers;

/* renamed from: io.reactivex.Maybe */
public abstract class Maybe<T> implements MaybeSource<T> {
    /* access modifiers changed from: protected */
    public abstract void subscribeActual(MaybeObserver<? super T> maybeObserver);

    @SchedulerSupport("none")
    public static <T> Maybe<T> amb(Iterable<? extends MaybeSource<? extends T>> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeAmb<T>(null, iterable));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> ambArray(MaybeSource<? extends T>... maybeSourceArr) {
        if (maybeSourceArr.length == 0) {
            return empty();
        }
        if (maybeSourceArr.length == 1) {
            return wrap(maybeSourceArr[0]);
        }
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeAmb<T>(maybeSourceArr, null));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Iterable<? extends MaybeSource<? extends T>> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeConcatIterable<T>(iterable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(MaybeSource<? extends T> maybeSource, MaybeSource<? extends T> maybeSource2) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        return concatArray(maybeSource, maybeSource2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(MaybeSource<? extends T> maybeSource, MaybeSource<? extends T> maybeSource2, MaybeSource<? extends T> maybeSource3) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        return concatArray(maybeSource, maybeSource2, maybeSource3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(MaybeSource<? extends T> maybeSource, MaybeSource<? extends T> maybeSource2, MaybeSource<? extends T> maybeSource3, MaybeSource<? extends T> maybeSource4) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        return concatArray(maybeSource, maybeSource2, maybeSource3, maybeSource4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends MaybeSource<? extends T>> publisher) {
        return concat(publisher, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends MaybeSource<? extends T>> publisher, int i) {
        ObjectHelper.requireNonNull(publisher, "sources is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatMap<T>(publisher, MaybeToPublisher.instance(), i, ErrorMode.IMMEDIATE));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArray(MaybeSource<? extends T>... maybeSourceArr) {
        ObjectHelper.requireNonNull(maybeSourceArr, "sources is null");
        if (maybeSourceArr.length == 0) {
            return Flowable.empty();
        }
        if (maybeSourceArr.length == 1) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeToFlowable<T>(maybeSourceArr[0]));
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeConcatArray<T>(maybeSourceArr));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayDelayError(MaybeSource<? extends T>... maybeSourceArr) {
        if (maybeSourceArr.length == 0) {
            return Flowable.empty();
        }
        if (maybeSourceArr.length == 1) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeToFlowable<T>(maybeSourceArr[0]));
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeConcatArrayDelayError<T>(maybeSourceArr));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArrayEager(MaybeSource<? extends T>... maybeSourceArr) {
        return Flowable.fromArray(maybeSourceArr).concatMapEager(MaybeToPublisher.instance());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatDelayError(Iterable<? extends MaybeSource<? extends T>> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return Flowable.fromIterable(iterable).concatMapDelayError(MaybeToPublisher.instance());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatDelayError(Publisher<? extends MaybeSource<? extends T>> publisher) {
        return Flowable.fromPublisher(publisher).concatMapDelayError(MaybeToPublisher.instance());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Iterable<? extends MaybeSource<? extends T>> iterable) {
        return Flowable.fromIterable(iterable).concatMapEager(MaybeToPublisher.instance());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatEager(Publisher<? extends MaybeSource<? extends T>> publisher) {
        return Flowable.fromPublisher(publisher).concatMapEager(MaybeToPublisher.instance());
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> create(MaybeOnSubscribe<T> maybeOnSubscribe) {
        ObjectHelper.requireNonNull(maybeOnSubscribe, "onSubscribe is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeCreate<T>(maybeOnSubscribe));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> defer(Callable<? extends MaybeSource<? extends T>> callable) {
        ObjectHelper.requireNonNull(callable, "maybeSupplier is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDefer<T>(callable));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> empty() {
        return RxJavaPlugins.onAssembly((Maybe<T>) MaybeEmpty.INSTANCE);
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> error(Throwable th) {
        ObjectHelper.requireNonNull(th, "exception is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeError<T>(th));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> error(Callable<? extends Throwable> callable) {
        ObjectHelper.requireNonNull(callable, "errorSupplier is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeErrorCallable<T>(callable));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> fromAction(Action action) {
        ObjectHelper.requireNonNull(action, "run is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromAction<T>(action));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> fromCompletable(CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "completableSource is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromCompletable<T>(completableSource));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> fromSingle(SingleSource<T> singleSource) {
        ObjectHelper.requireNonNull(singleSource, "singleSource is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromSingle<T>(singleSource));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> fromCallable(Callable<? extends T> callable) {
        ObjectHelper.requireNonNull(callable, "callable is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromCallable<T>(callable));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> fromFuture(Future<? extends T> future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromFuture<T>(future, 0, null));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> fromFuture(Future<? extends T> future, long j, TimeUnit timeUnit) {
        ObjectHelper.requireNonNull(future, "future is null");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromFuture<T>(future, j, timeUnit));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> fromRunnable(Runnable runnable) {
        ObjectHelper.requireNonNull(runnable, "run is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromRunnable<T>(runnable));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> just(T t) {
        ObjectHelper.requireNonNull(t, "item is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeJust<T>(t));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Iterable<? extends MaybeSource<? extends T>> iterable) {
        return merge((Publisher<? extends MaybeSource<? extends T>>) Flowable.fromIterable(iterable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends MaybeSource<? extends T>> publisher) {
        return merge(publisher, Integer.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends MaybeSource<? extends T>> publisher, int i) {
        FlowableFlatMap flowableFlatMap = new FlowableFlatMap(publisher, MaybeToPublisher.instance(), false, i, Flowable.bufferSize());
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableFlatMap);
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> merge(MaybeSource<? extends MaybeSource<? extends T>> maybeSource) {
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFlatten<T>(maybeSource, Functions.identity()));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(MaybeSource<? extends T> maybeSource, MaybeSource<? extends T> maybeSource2) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        return mergeArray(maybeSource, maybeSource2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(MaybeSource<? extends T> maybeSource, MaybeSource<? extends T> maybeSource2, MaybeSource<? extends T> maybeSource3) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        return mergeArray(maybeSource, maybeSource2, maybeSource3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(MaybeSource<? extends T> maybeSource, MaybeSource<? extends T> maybeSource2, MaybeSource<? extends T> maybeSource3, MaybeSource<? extends T> maybeSource4) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        return mergeArray(maybeSource, maybeSource2, maybeSource3, maybeSource4);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArray(MaybeSource<? extends T>... maybeSourceArr) {
        ObjectHelper.requireNonNull(maybeSourceArr, "sources is null");
        if (maybeSourceArr.length == 0) {
            return Flowable.empty();
        }
        if (maybeSourceArr.length == 1) {
            return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeToFlowable<T>(maybeSourceArr[0]));
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeMergeArray<T>(maybeSourceArr));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeArrayDelayError(MaybeSource<? extends T>... maybeSourceArr) {
        return Flowable.fromArray(maybeSourceArr).flatMap(MaybeToPublisher.instance(), true, maybeSourceArr.length);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Iterable<? extends MaybeSource<? extends T>> iterable) {
        return Flowable.fromIterable(iterable).flatMap(MaybeToPublisher.instance(), true);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(Publisher<? extends MaybeSource<? extends T>> publisher) {
        return Flowable.fromPublisher(publisher).flatMap(MaybeToPublisher.instance(), true);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(MaybeSource<? extends T> maybeSource, MaybeSource<? extends T> maybeSource2) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        return mergeArrayDelayError(maybeSource, maybeSource2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(MaybeSource<? extends T> maybeSource, MaybeSource<? extends T> maybeSource2, MaybeSource<? extends T> maybeSource3) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        return mergeArrayDelayError(maybeSource, maybeSource2, maybeSource3);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> mergeDelayError(MaybeSource<? extends T> maybeSource, MaybeSource<? extends T> maybeSource2, MaybeSource<? extends T> maybeSource3, MaybeSource<? extends T> maybeSource4) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        return mergeArrayDelayError(maybeSource, maybeSource2, maybeSource3, maybeSource4);
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> never() {
        return RxJavaPlugins.onAssembly((Maybe<T>) MaybeNever.INSTANCE);
    }

    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(MaybeSource<? extends T> maybeSource, MaybeSource<? extends T> maybeSource2) {
        return sequenceEqual(maybeSource, maybeSource2, ObjectHelper.equalsPredicate());
    }

    @SchedulerSupport("none")
    public static <T> Single<Boolean> sequenceEqual(MaybeSource<? extends T> maybeSource, MaybeSource<? extends T> maybeSource2, BiPredicate<? super T, ? super T> biPredicate) {
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeEqualSingle<T>(maybeSource, maybeSource2, biPredicate));
    }

    @SchedulerSupport("io.reactivex:computation")
    public static Maybe<Long> timer(long j, TimeUnit timeUnit) {
        return timer(j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public static Maybe<Long> timer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeTimer<T>(Math.max(0, j), timeUnit, scheduler));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> unsafeCreate(MaybeSource<T> maybeSource) {
        if (maybeSource instanceof Maybe) {
            throw new IllegalArgumentException("unsafeCreate(Maybe) should be upgraded");
        }
        ObjectHelper.requireNonNull(maybeSource, "onSubscribe is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeUnsafeCreate<T>(maybeSource));
    }

    @SchedulerSupport("none")
    public static <T, D> Maybe<T> using(Callable<? extends D> callable, Function<? super D, ? extends MaybeSource<? extends T>> function, Consumer<? super D> consumer) {
        return using(callable, function, consumer, true);
    }

    @SchedulerSupport("none")
    public static <T, D> Maybe<T> using(Callable<? extends D> callable, Function<? super D, ? extends MaybeSource<? extends T>> function, Consumer<? super D> consumer, boolean z) {
        ObjectHelper.requireNonNull(callable, "resourceSupplier is null");
        ObjectHelper.requireNonNull(function, "sourceSupplier is null");
        ObjectHelper.requireNonNull(consumer, "disposer is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeUsing<T>(callable, function, consumer, z));
    }

    @SchedulerSupport("none")
    public static <T> Maybe<T> wrap(MaybeSource<T> maybeSource) {
        if (maybeSource instanceof Maybe) {
            return RxJavaPlugins.onAssembly((Maybe) maybeSource);
        }
        ObjectHelper.requireNonNull(maybeSource, "onSubscribe is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeUnsafeCreate<T>(maybeSource));
    }

    @SchedulerSupport("none")
    public static <T, R> Maybe<R> zip(Iterable<? extends MaybeSource<? extends T>> iterable, Function<? super Object[], ? extends R> function) {
        ObjectHelper.requireNonNull(function, "zipper is null");
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeZipIterable<T>(iterable, function));
    }

    @SchedulerSupport("none")
    public static <T1, T2, R> Maybe<R> zip(MaybeSource<? extends T1> maybeSource, MaybeSource<? extends T2> maybeSource2, BiFunction<? super T1, ? super T2, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), maybeSource, maybeSource2);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Maybe<R> zip(MaybeSource<? extends T1> maybeSource, MaybeSource<? extends T2> maybeSource2, MaybeSource<? extends T3> maybeSource3, Function3<? super T1, ? super T2, ? super T3, ? extends R> function3) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        return zipArray(Functions.toFunction(function3), maybeSource, maybeSource2, maybeSource3);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Maybe<R> zip(MaybeSource<? extends T1> maybeSource, MaybeSource<? extends T2> maybeSource2, MaybeSource<? extends T3> maybeSource3, MaybeSource<? extends T4> maybeSource4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> function4) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        return zipArray(Functions.toFunction(function4), maybeSource, maybeSource2, maybeSource3, maybeSource4);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Maybe<R> zip(MaybeSource<? extends T1> maybeSource, MaybeSource<? extends T2> maybeSource2, MaybeSource<? extends T3> maybeSource3, MaybeSource<? extends T4> maybeSource4, MaybeSource<? extends T5> maybeSource5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> function5) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        ObjectHelper.requireNonNull(maybeSource5, "source5 is null");
        return zipArray(Functions.toFunction(function5), maybeSource, maybeSource2, maybeSource3, maybeSource4, maybeSource5);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Maybe<R> zip(MaybeSource<? extends T1> maybeSource, MaybeSource<? extends T2> maybeSource2, MaybeSource<? extends T3> maybeSource3, MaybeSource<? extends T4> maybeSource4, MaybeSource<? extends T5> maybeSource5, MaybeSource<? extends T6> maybeSource6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> function6) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        ObjectHelper.requireNonNull(maybeSource5, "source5 is null");
        ObjectHelper.requireNonNull(maybeSource6, "source6 is null");
        return zipArray(Functions.toFunction(function6), maybeSource, maybeSource2, maybeSource3, maybeSource4, maybeSource5, maybeSource6);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Maybe<R> zip(MaybeSource<? extends T1> maybeSource, MaybeSource<? extends T2> maybeSource2, MaybeSource<? extends T3> maybeSource3, MaybeSource<? extends T4> maybeSource4, MaybeSource<? extends T5> maybeSource5, MaybeSource<? extends T6> maybeSource6, MaybeSource<? extends T7> maybeSource7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> function7) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        ObjectHelper.requireNonNull(maybeSource5, "source5 is null");
        ObjectHelper.requireNonNull(maybeSource6, "source6 is null");
        ObjectHelper.requireNonNull(maybeSource7, "source7 is null");
        return zipArray(Functions.toFunction(function7), maybeSource, maybeSource2, maybeSource3, maybeSource4, maybeSource5, maybeSource6, maybeSource7);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Maybe<R> zip(MaybeSource<? extends T1> maybeSource, MaybeSource<? extends T2> maybeSource2, MaybeSource<? extends T3> maybeSource3, MaybeSource<? extends T4> maybeSource4, MaybeSource<? extends T5> maybeSource5, MaybeSource<? extends T6> maybeSource6, MaybeSource<? extends T7> maybeSource7, MaybeSource<? extends T8> maybeSource8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> function8) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        ObjectHelper.requireNonNull(maybeSource5, "source5 is null");
        ObjectHelper.requireNonNull(maybeSource6, "source6 is null");
        ObjectHelper.requireNonNull(maybeSource7, "source7 is null");
        ObjectHelper.requireNonNull(maybeSource8, "source8 is null");
        return zipArray(Functions.toFunction(function8), maybeSource, maybeSource2, maybeSource3, maybeSource4, maybeSource5, maybeSource6, maybeSource7, maybeSource8);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Maybe<R> zip(MaybeSource<? extends T1> maybeSource, MaybeSource<? extends T2> maybeSource2, MaybeSource<? extends T3> maybeSource3, MaybeSource<? extends T4> maybeSource4, MaybeSource<? extends T5> maybeSource5, MaybeSource<? extends T6> maybeSource6, MaybeSource<? extends T7> maybeSource7, MaybeSource<? extends T8> maybeSource8, MaybeSource<? extends T9> maybeSource9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> function9) {
        ObjectHelper.requireNonNull(maybeSource, "source1 is null");
        ObjectHelper.requireNonNull(maybeSource2, "source2 is null");
        ObjectHelper.requireNonNull(maybeSource3, "source3 is null");
        ObjectHelper.requireNonNull(maybeSource4, "source4 is null");
        ObjectHelper.requireNonNull(maybeSource5, "source5 is null");
        ObjectHelper.requireNonNull(maybeSource6, "source6 is null");
        ObjectHelper.requireNonNull(maybeSource7, "source7 is null");
        ObjectHelper.requireNonNull(maybeSource8, "source8 is null");
        ObjectHelper.requireNonNull(maybeSource9, "source9 is null");
        return zipArray(Functions.toFunction(function9), maybeSource, maybeSource2, maybeSource3, maybeSource4, maybeSource5, maybeSource6, maybeSource7, maybeSource8, maybeSource9);
    }

    @SchedulerSupport("none")
    public static <T, R> Maybe<R> zipArray(Function<? super Object[], ? extends R> function, MaybeSource<? extends T>... maybeSourceArr) {
        ObjectHelper.requireNonNull(maybeSourceArr, "sources is null");
        if (maybeSourceArr.length == 0) {
            return empty();
        }
        ObjectHelper.requireNonNull(function, "zipper is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeZipArray<T>(maybeSourceArr, function));
    }

    @SchedulerSupport("none")
    public final Maybe<T> ambWith(MaybeSource<? extends T> maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return ambArray(this, maybeSource);
    }

    @SchedulerSupport("none")
    public final T blockingGet() {
        BlockingMultiObserver blockingMultiObserver = new BlockingMultiObserver();
        subscribe((MaybeObserver<? super T>) blockingMultiObserver);
        return blockingMultiObserver.blockingGet();
    }

    @SchedulerSupport("none")
    public final T blockingGet(T t) {
        ObjectHelper.requireNonNull(t, "defaultValue is null");
        BlockingMultiObserver blockingMultiObserver = new BlockingMultiObserver();
        subscribe((MaybeObserver<? super T>) blockingMultiObserver);
        return blockingMultiObserver.blockingGet(t);
    }

    @SchedulerSupport("none")
    public final Maybe<T> cache() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeCache<T>(this));
    }

    @SchedulerSupport("none")
    public final <U> Maybe<U> cast(Class<? extends U> cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return map(Functions.castFunction(cls));
    }

    @SchedulerSupport("none")
    public final <R> Maybe<R> compose(MaybeTransformer<T, R> maybeTransformer) {
        return wrap(maybeTransformer.apply(this));
    }

    @SchedulerSupport("none")
    public final <R> Maybe<R> concatMap(Function<? super T, ? extends MaybeSource<? extends R>> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFlatten<T>(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> concatWith(MaybeSource<? extends T> maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return concat((MaybeSource<? extends T>) this, maybeSource);
    }

    @SchedulerSupport("none")
    public final Single<Boolean> contains(Object obj) {
        ObjectHelper.requireNonNull(obj, "item is null");
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeContains<T>(this, obj));
    }

    @SchedulerSupport("none")
    public final Single<Long> count() {
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeCount<T>(this));
    }

    @SchedulerSupport("none")
    public final Maybe<T> defaultIfEmpty(T t) {
        ObjectHelper.requireNonNull(t, "item is null");
        return switchIfEmpty(just(t));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Maybe<T> delay(long j, TimeUnit timeUnit) {
        return delay(j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Maybe<T> delay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        MaybeDelay maybeDelay = new MaybeDelay(this, Math.max(0, j), timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Maybe<T>) maybeDelay);
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U, V> Maybe<T> delay(Publisher<U> publisher) {
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDelayOtherPublisher<T>(this, publisher));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Maybe<T> delaySubscription(Publisher<U> publisher) {
        ObjectHelper.requireNonNull(publisher, "subscriptionIndicator is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDelaySubscriptionOtherPublisher<T>(this, publisher));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Maybe<T> delaySubscription(long j, TimeUnit timeUnit) {
        return delaySubscription(j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Maybe<T> delaySubscription(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delaySubscription(Flowable.timer(j, timeUnit, scheduler));
    }

    @Experimental
    @SchedulerSupport("none")
    public final Maybe<T> doAfterSuccess(Consumer<? super T> consumer) {
        ObjectHelper.requireNonNull(consumer, "doAfterSuccess is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDoAfterSuccess<T>(this, consumer));
    }

    @SchedulerSupport("none")
    public final Maybe<T> doAfterTerminate(Action action) {
        MaybePeek maybePeek = new MaybePeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, (Action) ObjectHelper.requireNonNull(action, "onAfterTerminate is null"), Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((Maybe<T>) maybePeek);
    }

    @Experimental
    @SchedulerSupport("none")
    public final Maybe<T> doFinally(Action action) {
        ObjectHelper.requireNonNull(action, "onFinally is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDoFinally<T>(this, action));
    }

    @SchedulerSupport("none")
    public final Maybe<T> doOnDispose(Action action) {
        MaybePeek maybePeek = new MaybePeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, (Action) ObjectHelper.requireNonNull(action, "onDispose is null"));
        return RxJavaPlugins.onAssembly((Maybe<T>) maybePeek);
    }

    @SchedulerSupport("none")
    public final Maybe<T> doOnComplete(Action action) {
        MaybePeek maybePeek = new MaybePeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), (Action) ObjectHelper.requireNonNull(action, "onComplete is null"), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((Maybe<T>) maybePeek);
    }

    @SchedulerSupport("none")
    public final Maybe<T> doOnError(Consumer<? super Throwable> consumer) {
        MaybePeek maybePeek = new MaybePeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), (Consumer) ObjectHelper.requireNonNull(consumer, "onError is null"), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((Maybe<T>) maybePeek);
    }

    @SchedulerSupport("none")
    public final Maybe<T> doOnEvent(BiConsumer<? super T, ? super Throwable> biConsumer) {
        ObjectHelper.requireNonNull(biConsumer, "onEvent is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDoOnEvent<T>(this, biConsumer));
    }

    @SchedulerSupport("none")
    public final Maybe<T> doOnSubscribe(Consumer<? super Disposable> consumer) {
        MaybePeek maybePeek = new MaybePeek(this, (Consumer) ObjectHelper.requireNonNull(consumer, "onSubscribe is null"), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((Maybe<T>) maybePeek);
    }

    @SchedulerSupport("none")
    public final Maybe<T> doOnSuccess(Consumer<? super T> consumer) {
        MaybePeek maybePeek = new MaybePeek(this, Functions.emptyConsumer(), (Consumer) ObjectHelper.requireNonNull(consumer, "onSubscribe is null"), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((Maybe<T>) maybePeek);
    }

    @SchedulerSupport("none")
    public final Maybe<T> filter(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFilter<T>(this, predicate));
    }

    @SchedulerSupport("none")
    public final <R> Maybe<R> flatMap(Function<? super T, ? extends MaybeSource<? extends R>> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFlatten<T>(this, function));
    }

    @SchedulerSupport("none")
    public final <R> Maybe<R> flatMap(Function<? super T, ? extends MaybeSource<? extends R>> function, Function<? super Throwable, ? extends MaybeSource<? extends R>> function2, Callable<? extends MaybeSource<? extends R>> callable) {
        ObjectHelper.requireNonNull(function, "onSuccessMapper is null");
        ObjectHelper.requireNonNull(function2, "onErrorMapper is null");
        ObjectHelper.requireNonNull(callable, "onCompleteSupplier is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFlatMapNotification<T>(this, function, function2, callable));
    }

    @SchedulerSupport("none")
    public final <U, R> Maybe<R> flatMap(Function<? super T, ? extends MaybeSource<? extends U>> function, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFlatMapBiSelector<T>(this, function, biFunction));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> flattenAsFlowable(Function<? super T, ? extends Iterable<? extends U>> function) {
        return new MaybeFlatMapIterableFlowable(this, function);
    }

    @SchedulerSupport("none")
    public final <U> Observable<U> flattenAsObservable(Function<? super T, ? extends Iterable<? extends U>> function) {
        return new MaybeFlatMapIterableObservable(this, function);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMapObservable(Function<? super T, ? extends ObservableSource<? extends R>> function) {
        return toObservable().flatMap(function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapPublisher(Function<? super T, ? extends Publisher<? extends R>> function) {
        return toFlowable().flatMap(function);
    }

    @SchedulerSupport("none")
    public final <R> Single<R> flatMapSingle(Function<? super T, ? extends SingleSource<? extends R>> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeFlatMapSingle<T>(this, function));
    }

    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends Completable> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Completable) new MaybeFlatMapCompletable(this, function));
    }

    @SchedulerSupport("none")
    public final Maybe<T> hide() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeHide<T>(this));
    }

    @SchedulerSupport("none")
    public final Completable ignoreElement() {
        return RxJavaPlugins.onAssembly((Completable) new MaybeIgnoreElementCompletable(this));
    }

    @SchedulerSupport("none")
    public final Single<Boolean> isEmpty() {
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeIsEmptySingle<T>(this));
    }

    @SchedulerSupport("none")
    public final <R> Maybe<R> lift(MaybeOperator<? extends R, ? super T> maybeOperator) {
        ObjectHelper.requireNonNull(maybeOperator, "onLift is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeLift<T>(this, maybeOperator));
    }

    @SchedulerSupport("none")
    public final <R> Maybe<R> map(Function<? super T, ? extends R> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeMap<T>(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> mergeWith(MaybeSource<? extends T> maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return merge((MaybeSource<? extends T>) this, maybeSource);
    }

    @SchedulerSupport("custom")
    public final Maybe<T> observeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeObserveOn<T>(this, scheduler));
    }

    @SchedulerSupport("none")
    public final <U> Maybe<U> ofType(Class<U> cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return filter(Functions.isInstanceOf(cls)).cast(cls);
    }

    @SchedulerSupport("none")
    /* renamed from: to */
    public final <R> R mo12834to(Function<? super Maybe<T>, R> function) {
        try {
            return function.apply(this);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> toFlowable() {
        if (this instanceof FuseToFlowable) {
            return ((FuseToFlowable) this).fuseToFlowable();
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new MaybeToFlowable<T>(this));
    }

    @SchedulerSupport("none")
    public final Observable<T> toObservable() {
        if (this instanceof FuseToObservable) {
            return ((FuseToObservable) this).fuseToObservable();
        }
        return RxJavaPlugins.onAssembly((Observable<T>) new MaybeToObservable<T>(this));
    }

    @SchedulerSupport("none")
    public final Single<T> toSingle(T t) {
        ObjectHelper.requireNonNull(t, "defaultValue is null");
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeToSingle<T>(this, t));
    }

    @SchedulerSupport("none")
    public final Single<T> toSingle() {
        return RxJavaPlugins.onAssembly((Single<T>) new MaybeToSingle<T>(this, null));
    }

    @SchedulerSupport("none")
    public final Maybe<T> onErrorComplete() {
        return onErrorComplete(Functions.alwaysTrue());
    }

    @SchedulerSupport("none")
    public final Maybe<T> onErrorComplete(Predicate<? super Throwable> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeOnErrorComplete<T>(this, predicate));
    }

    @SchedulerSupport("none")
    public final Maybe<T> onErrorResumeNext(MaybeSource<? extends T> maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "next is null");
        return onErrorResumeNext(Functions.justFunction(maybeSource));
    }

    @SchedulerSupport("none")
    public final Maybe<T> onErrorResumeNext(Function<? super Throwable, ? extends MaybeSource<? extends T>> function) {
        ObjectHelper.requireNonNull(function, "resumeFunction is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeOnErrorNext<T>(this, function, true));
    }

    @SchedulerSupport("none")
    public final Maybe<T> onErrorReturn(Function<? super Throwable, ? extends T> function) {
        ObjectHelper.requireNonNull(function, "valueSupplier is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeOnErrorReturn<T>(this, function));
    }

    @SchedulerSupport("none")
    public final Maybe<T> onErrorReturnItem(T t) {
        ObjectHelper.requireNonNull(t, "item is null");
        return onErrorReturn(Functions.justFunction(t));
    }

    @SchedulerSupport("none")
    public final Maybe<T> onExceptionResumeNext(MaybeSource<? extends T> maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "next is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeOnErrorNext<T>(this, Functions.justFunction(maybeSource), false));
    }

    @SchedulerSupport("none")
    public final Maybe<T> onTerminateDetach() {
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDetach<T>(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat() {
        return repeat(LongCompanionObject.MAX_VALUE);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat(long j) {
        return toFlowable().repeat(j);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatUntil(BooleanSupplier booleanSupplier) {
        return toFlowable().repeatUntil(booleanSupplier);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<?>> function) {
        return toFlowable().repeatWhen(function);
    }

    @SchedulerSupport("none")
    public final Maybe<T> retry() {
        return retry(LongCompanionObject.MAX_VALUE, Functions.alwaysTrue());
    }

    @SchedulerSupport("none")
    public final Maybe<T> retry(BiPredicate<? super Integer, ? super Throwable> biPredicate) {
        return toFlowable().retry(biPredicate).singleElement();
    }

    @SchedulerSupport("none")
    public final Maybe<T> retry(long j) {
        return retry(j, Functions.alwaysTrue());
    }

    @SchedulerSupport("none")
    public final Maybe<T> retry(long j, Predicate<? super Throwable> predicate) {
        return toFlowable().retry(j, predicate).singleElement();
    }

    @SchedulerSupport("none")
    public final Maybe<T> retry(Predicate<? super Throwable> predicate) {
        return retry(LongCompanionObject.MAX_VALUE, predicate);
    }

    @SchedulerSupport("none")
    public final Maybe<T> retryUntil(BooleanSupplier booleanSupplier) {
        ObjectHelper.requireNonNull(booleanSupplier, "stop is null");
        return retry(LongCompanionObject.MAX_VALUE, Functions.predicateReverseFor(booleanSupplier));
    }

    @SchedulerSupport("none")
    public final Maybe<T> retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<?>> function) {
        return toFlowable().retryWhen(function).singleElement();
    }

    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> consumer) {
        return subscribe(consumer, Functions.ERROR_CONSUMER, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2) {
        return subscribe(consumer, consumer2, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action) {
        return (Disposable) subscribeWith(new MaybeCallbackObserver(consumer, consumer2, action));
    }

    @SchedulerSupport("none")
    public final void subscribe(MaybeObserver<? super T> maybeObserver) {
        ObjectHelper.requireNonNull(maybeObserver, "observer is null");
        MaybeObserver onSubscribe = RxJavaPlugins.onSubscribe(this, maybeObserver);
        ObjectHelper.requireNonNull(onSubscribe, "observer returned by the RxJavaPlugins hook is null");
        try {
            subscribeActual(onSubscribe);
        } catch (NullPointerException e) {
            throw e;
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            NullPointerException nullPointerException = new NullPointerException("subscribeActual failed");
            nullPointerException.initCause(th);
            throw nullPointerException;
        }
    }

    @SchedulerSupport("custom")
    public final Maybe<T> subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeSubscribeOn<T>(this, scheduler));
    }

    @SchedulerSupport("none")
    public final <E extends MaybeObserver<? super T>> E subscribeWith(E e) {
        subscribe((MaybeObserver<? super T>) e);
        return e;
    }

    @SchedulerSupport("none")
    public final Maybe<T> switchIfEmpty(MaybeSource<? extends T> maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeSwitchIfEmpty<T>(this, maybeSource));
    }

    @SchedulerSupport("none")
    public final <U> Maybe<T> takeUntil(MaybeSource<U> maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeTakeUntilMaybe<T>(this, maybeSource));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Maybe<T> takeUntil(Publisher<U> publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeTakeUntilPublisher<T>(this, publisher));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Maybe<T> timeout(long j, TimeUnit timeUnit) {
        return timeout(j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Maybe<T> timeout(long j, TimeUnit timeUnit, MaybeSource<? extends T> maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return timeout(j, timeUnit, Schedulers.computation(), maybeSource);
    }

    @SchedulerSupport("custom")
    public final Maybe<T> timeout(long j, TimeUnit timeUnit, Scheduler scheduler, MaybeSource<? extends T> maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "fallback is null");
        return timeout((MaybeSource<U>) timer(j, timeUnit, scheduler), maybeSource);
    }

    @SchedulerSupport("custom")
    public final Maybe<T> timeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout((MaybeSource<U>) timer(j, timeUnit, scheduler));
    }

    @SchedulerSupport("none")
    public final <U> Maybe<T> timeout(MaybeSource<U> maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "timeoutIndicator is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeTimeoutMaybe<T>(this, maybeSource, null));
    }

    @SchedulerSupport("none")
    public final <U> Maybe<T> timeout(MaybeSource<U> maybeSource, MaybeSource<? extends T> maybeSource2) {
        ObjectHelper.requireNonNull(maybeSource, "timeoutIndicator is null");
        ObjectHelper.requireNonNull(maybeSource2, "fallback is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeTimeoutMaybe<T>(this, maybeSource, maybeSource2));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Maybe<T> timeout(Publisher<U> publisher) {
        ObjectHelper.requireNonNull(publisher, "timeoutIndicator is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeTimeoutPublisher<T>(this, publisher, null));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public final <U> Maybe<T> timeout(Publisher<U> publisher, MaybeSource<? extends T> maybeSource) {
        ObjectHelper.requireNonNull(publisher, "timeoutIndicator is null");
        ObjectHelper.requireNonNull(maybeSource, "fallback is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeTimeoutPublisher<T>(this, publisher, maybeSource));
    }

    @SchedulerSupport("custom")
    public final Maybe<T> unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeUnsubscribeOn<T>(this, scheduler));
    }

    @SchedulerSupport("none")
    public final <U, R> Maybe<R> zipWith(MaybeSource<? extends U> maybeSource, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(maybeSource, "other is null");
        return zip(this, maybeSource, biFunction);
    }

    @SchedulerSupport("none")
    public final TestObserver<T> test() {
        TestObserver<T> testObserver = new TestObserver<>();
        subscribe((MaybeObserver<? super T>) testObserver);
        return testObserver;
    }

    @SchedulerSupport("none")
    public final TestObserver<T> test(boolean z) {
        TestObserver<T> testObserver = new TestObserver<>();
        if (z) {
            testObserver.cancel();
        }
        subscribe((MaybeObserver<? super T>) testObserver);
        return testObserver;
    }
}
