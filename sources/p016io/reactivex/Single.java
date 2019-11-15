package p016io.reactivex;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
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
import p016io.reactivex.internal.fuseable.FuseToMaybe;
import p016io.reactivex.internal.fuseable.FuseToObservable;
import p016io.reactivex.internal.observers.BiConsumerSingleObserver;
import p016io.reactivex.internal.observers.BlockingMultiObserver;
import p016io.reactivex.internal.observers.ConsumerSingleObserver;
import p016io.reactivex.internal.observers.FutureSingleObserver;
import p016io.reactivex.internal.operators.completable.CompletableFromSingle;
import p016io.reactivex.internal.operators.completable.CompletableToFlowable;
import p016io.reactivex.internal.operators.flowable.FlowableConcatMap;
import p016io.reactivex.internal.operators.flowable.FlowableFlatMap;
import p016io.reactivex.internal.operators.flowable.FlowableSingleSingle;
import p016io.reactivex.internal.operators.maybe.MaybeFilterSingle;
import p016io.reactivex.internal.operators.maybe.MaybeFromSingle;
import p016io.reactivex.internal.operators.observable.ObservableConcatMap;
import p016io.reactivex.internal.operators.observable.ObservableSingleSingle;
import p016io.reactivex.internal.operators.single.SingleAmb;
import p016io.reactivex.internal.operators.single.SingleCache;
import p016io.reactivex.internal.operators.single.SingleContains;
import p016io.reactivex.internal.operators.single.SingleCreate;
import p016io.reactivex.internal.operators.single.SingleDefer;
import p016io.reactivex.internal.operators.single.SingleDelay;
import p016io.reactivex.internal.operators.single.SingleDelayWithCompletable;
import p016io.reactivex.internal.operators.single.SingleDelayWithObservable;
import p016io.reactivex.internal.operators.single.SingleDelayWithPublisher;
import p016io.reactivex.internal.operators.single.SingleDelayWithSingle;
import p016io.reactivex.internal.operators.single.SingleDoAfterSuccess;
import p016io.reactivex.internal.operators.single.SingleDoFinally;
import p016io.reactivex.internal.operators.single.SingleDoOnDispose;
import p016io.reactivex.internal.operators.single.SingleDoOnError;
import p016io.reactivex.internal.operators.single.SingleDoOnEvent;
import p016io.reactivex.internal.operators.single.SingleDoOnSubscribe;
import p016io.reactivex.internal.operators.single.SingleDoOnSuccess;
import p016io.reactivex.internal.operators.single.SingleEquals;
import p016io.reactivex.internal.operators.single.SingleError;
import p016io.reactivex.internal.operators.single.SingleFlatMap;
import p016io.reactivex.internal.operators.single.SingleFlatMapCompletable;
import p016io.reactivex.internal.operators.single.SingleFlatMapIterableFlowable;
import p016io.reactivex.internal.operators.single.SingleFlatMapIterableObservable;
import p016io.reactivex.internal.operators.single.SingleFlatMapMaybe;
import p016io.reactivex.internal.operators.single.SingleFromCallable;
import p016io.reactivex.internal.operators.single.SingleFromPublisher;
import p016io.reactivex.internal.operators.single.SingleFromUnsafeSource;
import p016io.reactivex.internal.operators.single.SingleHide;
import p016io.reactivex.internal.operators.single.SingleInternalHelper;
import p016io.reactivex.internal.operators.single.SingleJust;
import p016io.reactivex.internal.operators.single.SingleLift;
import p016io.reactivex.internal.operators.single.SingleMap;
import p016io.reactivex.internal.operators.single.SingleNever;
import p016io.reactivex.internal.operators.single.SingleObserveOn;
import p016io.reactivex.internal.operators.single.SingleOnErrorReturn;
import p016io.reactivex.internal.operators.single.SingleResumeNext;
import p016io.reactivex.internal.operators.single.SingleSubscribeOn;
import p016io.reactivex.internal.operators.single.SingleTakeUntil;
import p016io.reactivex.internal.operators.single.SingleTimeout;
import p016io.reactivex.internal.operators.single.SingleTimer;
import p016io.reactivex.internal.operators.single.SingleToFlowable;
import p016io.reactivex.internal.operators.single.SingleToObservable;
import p016io.reactivex.internal.operators.single.SingleUsing;
import p016io.reactivex.internal.util.ErrorMode;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.observers.TestObserver;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.schedulers.Schedulers;

/* renamed from: io.reactivex.Single */
public abstract class Single<T> implements SingleSource<T> {
    /* access modifiers changed from: protected */
    public abstract void subscribeActual(SingleObserver<? super T> singleObserver);

    @SchedulerSupport("none")
    public static <T> Single<T> amb(Iterable<? extends SingleSource<? extends T>> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleAmb<T>(null, iterable));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> ambArray(SingleSource<? extends T>... singleSourceArr) {
        if (singleSourceArr.length == 0) {
            return error(SingleInternalHelper.emptyThrower());
        }
        if (singleSourceArr.length == 1) {
            return wrap(singleSourceArr[0]);
        }
        return RxJavaPlugins.onAssembly((Single<T>) new SingleAmb<T>(singleSourceArr, null));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Iterable<? extends SingleSource<? extends T>> iterable) {
        return concat((Publisher<? extends SingleSource<? extends T>>) Flowable.fromIterable(iterable));
    }

    @SchedulerSupport("none")
    public static <T> Observable<T> concat(ObservableSource<? extends SingleSource<? extends T>> observableSource) {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableConcatMap<T>(observableSource, SingleInternalHelper.toObservable(), 2, ErrorMode.IMMEDIATE));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends SingleSource<? extends T>> publisher) {
        return concat(publisher, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(Publisher<? extends SingleSource<? extends T>> publisher, int i) {
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatMap<T>(publisher, SingleInternalHelper.toFlowable(), i, ErrorMode.IMMEDIATE));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(SingleSource<? extends T> singleSource, SingleSource<? extends T> singleSource2) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        return concat((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(singleSource, singleSource2));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(SingleSource<? extends T> singleSource, SingleSource<? extends T> singleSource2, SingleSource<? extends T> singleSource3) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        return concat((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(singleSource, singleSource2, singleSource3));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concat(SingleSource<? extends T> singleSource, SingleSource<? extends T> singleSource2, SingleSource<? extends T> singleSource3, SingleSource<? extends T> singleSource4) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        return concat((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(singleSource, singleSource2, singleSource3, singleSource4));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> concatArray(SingleSource<? extends T>... singleSourceArr) {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableConcatMap<T>(Flowable.fromArray(singleSourceArr), SingleInternalHelper.toFlowable(), 2, ErrorMode.BOUNDARY));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> create(SingleOnSubscribe<T> singleOnSubscribe) {
        ObjectHelper.requireNonNull(singleOnSubscribe, "source is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleCreate<T>(singleOnSubscribe));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> defer(Callable<? extends SingleSource<? extends T>> callable) {
        ObjectHelper.requireNonNull(callable, "singleSupplier is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDefer<T>(callable));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> error(Callable<? extends Throwable> callable) {
        ObjectHelper.requireNonNull(callable, "errorSupplier is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleError<T>(callable));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> error(Throwable th) {
        ObjectHelper.requireNonNull(th, "error is null");
        return error(Functions.justCallable(th));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> fromCallable(Callable<? extends T> callable) {
        ObjectHelper.requireNonNull(callable, "callable is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleFromCallable<T>(callable));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> fromFuture(Future<? extends T> future) {
        return toSingle(Flowable.fromFuture(future));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> fromFuture(Future<? extends T> future, long j, TimeUnit timeUnit) {
        return toSingle(Flowable.fromFuture(future, j, timeUnit));
    }

    @SchedulerSupport("custom")
    public static <T> Single<T> fromFuture(Future<? extends T> future, long j, TimeUnit timeUnit, Scheduler scheduler) {
        return toSingle(Flowable.fromFuture(future, j, timeUnit, scheduler));
    }

    @SchedulerSupport("custom")
    public static <T> Single<T> fromFuture(Future<? extends T> future, Scheduler scheduler) {
        return toSingle(Flowable.fromFuture(future, scheduler));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public static <T> Single<T> fromPublisher(Publisher<? extends T> publisher) {
        ObjectHelper.requireNonNull(publisher, "publisher is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleFromPublisher<T>(publisher));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> fromObservable(ObservableSource<? extends T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "observableSource is null");
        return RxJavaPlugins.onAssembly((Single<T>) new ObservableSingleSingle<T>(observableSource, null));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> just(T t) {
        ObjectHelper.requireNonNull(t, "value is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleJust<T>(t));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Iterable<? extends SingleSource<? extends T>> iterable) {
        return merge((Publisher<? extends SingleSource<? extends T>>) Flowable.fromIterable(iterable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(Publisher<? extends SingleSource<? extends T>> publisher) {
        FlowableFlatMap flowableFlatMap = new FlowableFlatMap(publisher, SingleInternalHelper.toFlowable(), false, Integer.MAX_VALUE, Flowable.bufferSize());
        return RxJavaPlugins.onAssembly((Flowable<T>) flowableFlatMap);
    }

    @SchedulerSupport("none")
    public static <T> Single<T> merge(SingleSource<? extends SingleSource<? extends T>> singleSource) {
        ObjectHelper.requireNonNull(singleSource, "source is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleFlatMap<T>(singleSource, Functions.identity()));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(SingleSource<? extends T> singleSource, SingleSource<? extends T> singleSource2) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        return merge((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(singleSource, singleSource2));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(SingleSource<? extends T> singleSource, SingleSource<? extends T> singleSource2, SingleSource<? extends T> singleSource3) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        return merge((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(singleSource, singleSource2, singleSource3));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static <T> Flowable<T> merge(SingleSource<? extends T> singleSource, SingleSource<? extends T> singleSource2, SingleSource<? extends T> singleSource3, SingleSource<? extends T> singleSource4) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        return merge((Publisher<? extends SingleSource<? extends T>>) Flowable.fromArray(singleSource, singleSource2, singleSource3, singleSource4));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> never() {
        return RxJavaPlugins.onAssembly(SingleNever.INSTANCE);
    }

    @SchedulerSupport("io.reactivex:computation")
    public static Single<Long> timer(long j, TimeUnit timeUnit) {
        return timer(j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public static Single<Long> timer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleTimer<T>(j, timeUnit, scheduler));
    }

    @SchedulerSupport("none")
    public static <T> Single<Boolean> equals(SingleSource<? extends T> singleSource, SingleSource<? extends T> singleSource2) {
        ObjectHelper.requireNonNull(singleSource, "first is null");
        ObjectHelper.requireNonNull(singleSource2, "second is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleEquals<T>(singleSource, singleSource2));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> unsafeCreate(SingleSource<T> singleSource) {
        ObjectHelper.requireNonNull(singleSource, "onSubscribe is null");
        if (!(singleSource instanceof Single)) {
            return RxJavaPlugins.onAssembly((Single<T>) new SingleFromUnsafeSource<T>(singleSource));
        }
        throw new IllegalArgumentException("unsafeCreate(Single) should be upgraded");
    }

    @SchedulerSupport("none")
    public static <T, U> Single<T> using(Callable<U> callable, Function<? super U, ? extends SingleSource<? extends T>> function, Consumer<? super U> consumer) {
        return using(callable, function, consumer, true);
    }

    @SchedulerSupport("none")
    public static <T, U> Single<T> using(Callable<U> callable, Function<? super U, ? extends SingleSource<? extends T>> function, Consumer<? super U> consumer, boolean z) {
        ObjectHelper.requireNonNull(callable, "resourceSupplier is null");
        ObjectHelper.requireNonNull(function, "singleFunction is null");
        ObjectHelper.requireNonNull(consumer, "disposer is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleUsing<T>(callable, function, consumer, z));
    }

    @SchedulerSupport("none")
    public static <T> Single<T> wrap(SingleSource<T> singleSource) {
        ObjectHelper.requireNonNull(singleSource, "source is null");
        if (singleSource instanceof Single) {
            return RxJavaPlugins.onAssembly((Single) singleSource);
        }
        return RxJavaPlugins.onAssembly((Single<T>) new SingleFromUnsafeSource<T>(singleSource));
    }

    @SchedulerSupport("none")
    public static <T, R> Single<R> zip(Iterable<? extends SingleSource<? extends T>> iterable, Function<? super Object[], ? extends R> function) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return toSingle(Flowable.zipIterable(SingleInternalHelper.iterableToFlowable(iterable), function, false, 1));
    }

    @SchedulerSupport("none")
    public static <T1, T2, R> Single<R> zip(SingleSource<? extends T1> singleSource, SingleSource<? extends T2> singleSource2, BiFunction<? super T1, ? super T2, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        return zipArray(Functions.toFunction(biFunction), singleSource, singleSource2);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, R> Single<R> zip(SingleSource<? extends T1> singleSource, SingleSource<? extends T2> singleSource2, SingleSource<? extends T3> singleSource3, Function3<? super T1, ? super T2, ? super T3, ? extends R> function3) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        return zipArray(Functions.toFunction(function3), singleSource, singleSource2, singleSource3);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, R> Single<R> zip(SingleSource<? extends T1> singleSource, SingleSource<? extends T2> singleSource2, SingleSource<? extends T3> singleSource3, SingleSource<? extends T4> singleSource4, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> function4) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        return zipArray(Functions.toFunction(function4), singleSource, singleSource2, singleSource3, singleSource4);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, R> Single<R> zip(SingleSource<? extends T1> singleSource, SingleSource<? extends T2> singleSource2, SingleSource<? extends T3> singleSource3, SingleSource<? extends T4> singleSource4, SingleSource<? extends T5> singleSource5, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> function5) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        ObjectHelper.requireNonNull(singleSource5, "source5 is null");
        return zipArray(Functions.toFunction(function5), singleSource, singleSource2, singleSource3, singleSource4, singleSource5);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, R> Single<R> zip(SingleSource<? extends T1> singleSource, SingleSource<? extends T2> singleSource2, SingleSource<? extends T3> singleSource3, SingleSource<? extends T4> singleSource4, SingleSource<? extends T5> singleSource5, SingleSource<? extends T6> singleSource6, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> function6) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        ObjectHelper.requireNonNull(singleSource5, "source5 is null");
        ObjectHelper.requireNonNull(singleSource6, "source6 is null");
        return zipArray(Functions.toFunction(function6), singleSource, singleSource2, singleSource3, singleSource4, singleSource5, singleSource6);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, R> Single<R> zip(SingleSource<? extends T1> singleSource, SingleSource<? extends T2> singleSource2, SingleSource<? extends T3> singleSource3, SingleSource<? extends T4> singleSource4, SingleSource<? extends T5> singleSource5, SingleSource<? extends T6> singleSource6, SingleSource<? extends T7> singleSource7, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> function7) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        ObjectHelper.requireNonNull(singleSource5, "source5 is null");
        ObjectHelper.requireNonNull(singleSource6, "source6 is null");
        ObjectHelper.requireNonNull(singleSource7, "source7 is null");
        return zipArray(Functions.toFunction(function7), singleSource, singleSource2, singleSource3, singleSource4, singleSource5, singleSource6, singleSource7);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Single<R> zip(SingleSource<? extends T1> singleSource, SingleSource<? extends T2> singleSource2, SingleSource<? extends T3> singleSource3, SingleSource<? extends T4> singleSource4, SingleSource<? extends T5> singleSource5, SingleSource<? extends T6> singleSource6, SingleSource<? extends T7> singleSource7, SingleSource<? extends T8> singleSource8, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> function8) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        ObjectHelper.requireNonNull(singleSource5, "source5 is null");
        ObjectHelper.requireNonNull(singleSource6, "source6 is null");
        ObjectHelper.requireNonNull(singleSource7, "source7 is null");
        ObjectHelper.requireNonNull(singleSource8, "source8 is null");
        return zipArray(Functions.toFunction(function8), singleSource, singleSource2, singleSource3, singleSource4, singleSource5, singleSource6, singleSource7, singleSource8);
    }

    @SchedulerSupport("none")
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Single<R> zip(SingleSource<? extends T1> singleSource, SingleSource<? extends T2> singleSource2, SingleSource<? extends T3> singleSource3, SingleSource<? extends T4> singleSource4, SingleSource<? extends T5> singleSource5, SingleSource<? extends T6> singleSource6, SingleSource<? extends T7> singleSource7, SingleSource<? extends T8> singleSource8, SingleSource<? extends T9> singleSource9, Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> function9) {
        ObjectHelper.requireNonNull(singleSource, "source1 is null");
        ObjectHelper.requireNonNull(singleSource2, "source2 is null");
        ObjectHelper.requireNonNull(singleSource3, "source3 is null");
        ObjectHelper.requireNonNull(singleSource4, "source4 is null");
        ObjectHelper.requireNonNull(singleSource5, "source5 is null");
        ObjectHelper.requireNonNull(singleSource6, "source6 is null");
        ObjectHelper.requireNonNull(singleSource7, "source7 is null");
        ObjectHelper.requireNonNull(singleSource8, "source8 is null");
        ObjectHelper.requireNonNull(singleSource9, "source9 is null");
        return zipArray(Functions.toFunction(function9), singleSource, singleSource2, singleSource3, singleSource4, singleSource5, singleSource6, singleSource7, singleSource8, singleSource9);
    }

    @SchedulerSupport("none")
    public static <T, R> Single<R> zipArray(Function<? super Object[], ? extends R> function, SingleSource<? extends T>... singleSourceArr) {
        ObjectHelper.requireNonNull(singleSourceArr, "sources is null");
        Publisher[] publisherArr = new Publisher[singleSourceArr.length];
        int i = 0;
        for (SingleSource<? extends T> singleSource : singleSourceArr) {
            StringBuilder sb = new StringBuilder();
            sb.append("The ");
            sb.append(i);
            sb.append("th source is null");
            ObjectHelper.requireNonNull(singleSource, sb.toString());
            publisherArr[i] = RxJavaPlugins.onAssembly((Flowable<T>) new SingleToFlowable<T>(singleSource));
            i++;
        }
        return toSingle(Flowable.zipArray(function, false, 1, publisherArr));
    }

    @SchedulerSupport("none")
    public final Single<T> ambWith(SingleSource<? extends T> singleSource) {
        ObjectHelper.requireNonNull(singleSource, "other is null");
        return ambArray(this, singleSource);
    }

    @SchedulerSupport("none")
    public final Single<T> hide() {
        return RxJavaPlugins.onAssembly((Single<T>) new SingleHide<T>(this));
    }

    @SchedulerSupport("none")
    public final <R> Single<R> compose(SingleTransformer<T, R> singleTransformer) {
        return wrap(singleTransformer.apply(this));
    }

    @SchedulerSupport("none")
    public final Single<T> cache() {
        return RxJavaPlugins.onAssembly((Single<T>) new SingleCache<T>(this));
    }

    @SchedulerSupport("none")
    public final <U> Single<U> cast(Class<? extends U> cls) {
        ObjectHelper.requireNonNull(cls, "clazz is null");
        return map(Functions.castFunction(cls));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> concatWith(SingleSource<? extends T> singleSource) {
        return concat((SingleSource<? extends T>) this, singleSource);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Single<T> delay(long j, TimeUnit timeUnit) {
        return delay(j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final Single<T> delay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        SingleDelay singleDelay = new SingleDelay(this, j, timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Single<T>) singleDelay);
    }

    @SchedulerSupport("none")
    public final Single<T> delaySubscription(CompletableSource completableSource) {
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDelayWithCompletable<T>(this, completableSource));
    }

    @SchedulerSupport("none")
    public final <U> Single<T> delaySubscription(SingleSource<U> singleSource) {
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDelayWithSingle<T>(this, singleSource));
    }

    @SchedulerSupport("none")
    public final <U> Single<T> delaySubscription(ObservableSource<U> observableSource) {
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDelayWithObservable<T>(this, observableSource));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Single<T> delaySubscription(Publisher<U> publisher) {
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDelayWithPublisher<T>(this, publisher));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final <U> Single<T> delaySubscription(long j, TimeUnit timeUnit) {
        return delaySubscription(j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public final <U> Single<T> delaySubscription(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delaySubscription((ObservableSource<U>) Observable.timer(j, timeUnit, scheduler));
    }

    @Experimental
    @SchedulerSupport("none")
    public final Single<T> doAfterSuccess(Consumer<? super T> consumer) {
        ObjectHelper.requireNonNull(consumer, "doAfterSuccess is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoAfterSuccess<T>(this, consumer));
    }

    @Experimental
    @SchedulerSupport("none")
    public final Single<T> doFinally(Action action) {
        ObjectHelper.requireNonNull(action, "onFinally is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoFinally<T>(this, action));
    }

    @SchedulerSupport("none")
    public final Single<T> doOnSubscribe(Consumer<? super Disposable> consumer) {
        ObjectHelper.requireNonNull(consumer, "onSubscribe is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoOnSubscribe<T>(this, consumer));
    }

    @SchedulerSupport("none")
    public final Single<T> doOnSuccess(Consumer<? super T> consumer) {
        ObjectHelper.requireNonNull(consumer, "onSuccess is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoOnSuccess<T>(this, consumer));
    }

    @SchedulerSupport("none")
    public final Single<T> doOnEvent(BiConsumer<? super T, ? super Throwable> biConsumer) {
        ObjectHelper.requireNonNull(biConsumer, "onEvent is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoOnEvent<T>(this, biConsumer));
    }

    @SchedulerSupport("none")
    public final Single<T> doOnError(Consumer<? super Throwable> consumer) {
        ObjectHelper.requireNonNull(consumer, "onError is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoOnError<T>(this, consumer));
    }

    @SchedulerSupport("none")
    public final Single<T> doOnDispose(Action action) {
        ObjectHelper.requireNonNull(action, "onDispose is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDoOnDispose<T>(this, action));
    }

    @SchedulerSupport("none")
    public final Maybe<T> filter(Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFilterSingle<T>(this, predicate));
    }

    @SchedulerSupport("none")
    public final <R> Single<R> flatMap(Function<? super T, ? extends SingleSource<? extends R>> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleFlatMap<T>(this, function));
    }

    @SchedulerSupport("none")
    public final <R> Maybe<R> flatMapMaybe(Function<? super T, ? extends MaybeSource<? extends R>> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new SingleFlatMapMaybe<T>(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <R> Flowable<R> flatMapPublisher(Function<? super T, ? extends Publisher<? extends R>> function) {
        return toFlowable().flatMap(function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <U> Flowable<U> flattenAsFlowable(Function<? super T, ? extends Iterable<? extends U>> function) {
        return new SingleFlatMapIterableFlowable(this, function);
    }

    @SchedulerSupport("none")
    public final <U> Observable<U> flattenAsObservable(Function<? super T, ? extends Iterable<? extends U>> function) {
        return new SingleFlatMapIterableObservable(this, function);
    }

    @SchedulerSupport("none")
    public final <R> Observable<R> flatMapObservable(Function<? super T, ? extends ObservableSource<? extends R>> function) {
        return toObservable().flatMap(function);
    }

    @SchedulerSupport("none")
    public final Completable flatMapCompletable(Function<? super T, ? extends Completable> function) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        return RxJavaPlugins.onAssembly((Completable) new SingleFlatMapCompletable(this, function));
    }

    @SchedulerSupport("none")
    public final T blockingGet() {
        BlockingMultiObserver blockingMultiObserver = new BlockingMultiObserver();
        subscribe((SingleObserver<? super T>) blockingMultiObserver);
        return blockingMultiObserver.blockingGet();
    }

    @SchedulerSupport("none")
    public final <R> Single<R> lift(SingleOperator<? extends R, ? super T> singleOperator) {
        ObjectHelper.requireNonNull(singleOperator, "onLift is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleLift<T>(this, singleOperator));
    }

    @SchedulerSupport("none")
    public final <R> Single<R> map(Function<? super T, ? extends R> function) {
        return RxJavaPlugins.onAssembly((Single<T>) new SingleMap<T>(this, function));
    }

    @SchedulerSupport("none")
    public final Single<Boolean> contains(Object obj) {
        return contains(obj, ObjectHelper.equalsPredicate());
    }

    @SchedulerSupport("none")
    public final Single<Boolean> contains(Object obj, BiPredicate<Object, Object> biPredicate) {
        ObjectHelper.requireNonNull(obj, "value is null");
        ObjectHelper.requireNonNull(biPredicate, "comparer is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleContains<T>(this, obj, biPredicate));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> mergeWith(SingleSource<? extends T> singleSource) {
        return merge(this, singleSource);
    }

    @SchedulerSupport("custom")
    public final Single<T> observeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleObserveOn<T>(this, scheduler));
    }

    @SchedulerSupport("none")
    public final Single<T> onErrorReturn(Function<Throwable, ? extends T> function) {
        ObjectHelper.requireNonNull(function, "resumeFunction is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleOnErrorReturn<T>(this, function, null));
    }

    @SchedulerSupport("none")
    public final Single<T> onErrorReturnItem(T t) {
        ObjectHelper.requireNonNull(t, "value is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleOnErrorReturn<T>(this, null, t));
    }

    @SchedulerSupport("none")
    public final Single<T> onErrorResumeNext(Single<? extends T> single) {
        ObjectHelper.requireNonNull(single, "resumeSingleInCaseOfError is null");
        return onErrorResumeNext(Functions.justFunction(single));
    }

    @SchedulerSupport("none")
    public final Single<T> onErrorResumeNext(Function<? super Throwable, ? extends SingleSource<? extends T>> function) {
        ObjectHelper.requireNonNull(function, "resumeFunctionInCaseOfError is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleResumeNext<T>(this, function));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat() {
        return toFlowable().repeat();
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeat(long j) {
        return toFlowable().repeat(j);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<Object>> function) {
        return toFlowable().repeatWhen(function);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> repeatUntil(BooleanSupplier booleanSupplier) {
        return toFlowable().repeatUntil(booleanSupplier);
    }

    @SchedulerSupport("none")
    public final Single<T> retry() {
        return toSingle(toFlowable().retry());
    }

    @SchedulerSupport("none")
    public final Single<T> retry(long j) {
        return toSingle(toFlowable().retry(j));
    }

    @SchedulerSupport("none")
    public final Single<T> retry(BiPredicate<? super Integer, ? super Throwable> biPredicate) {
        return toSingle(toFlowable().retry(biPredicate));
    }

    @SchedulerSupport("none")
    public final Single<T> retry(Predicate<? super Throwable> predicate) {
        return toSingle(toFlowable().retry(predicate));
    }

    @SchedulerSupport("none")
    public final Single<T> retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<Object>> function) {
        return toSingle(toFlowable().retryWhen(function));
    }

    @SchedulerSupport("none")
    public final Disposable subscribe() {
        return subscribe(Functions.emptyConsumer(), Functions.ERROR_CONSUMER);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(BiConsumer<? super T, ? super Throwable> biConsumer) {
        ObjectHelper.requireNonNull(biConsumer, "onCallback is null");
        BiConsumerSingleObserver biConsumerSingleObserver = new BiConsumerSingleObserver(biConsumer);
        subscribe((SingleObserver<? super T>) biConsumerSingleObserver);
        return biConsumerSingleObserver;
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> consumer) {
        return subscribe(consumer, Functions.ERROR_CONSUMER);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> consumer2) {
        ObjectHelper.requireNonNull(consumer, "onSuccess is null");
        ObjectHelper.requireNonNull(consumer2, "onError is null");
        ConsumerSingleObserver consumerSingleObserver = new ConsumerSingleObserver(consumer, consumer2);
        subscribe((SingleObserver<? super T>) consumerSingleObserver);
        return consumerSingleObserver;
    }

    @SchedulerSupport("none")
    public final void subscribe(SingleObserver<? super T> singleObserver) {
        ObjectHelper.requireNonNull(singleObserver, "subscriber is null");
        SingleObserver onSubscribe = RxJavaPlugins.onSubscribe(this, singleObserver);
        ObjectHelper.requireNonNull(onSubscribe, "subscriber returned by the RxJavaPlugins hook is null");
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

    @SchedulerSupport("none")
    public final <E extends SingleObserver<? super T>> E subscribeWith(E e) {
        subscribe((SingleObserver<? super T>) e);
        return e;
    }

    @SchedulerSupport("custom")
    public final Single<T> subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleSubscribeOn<T>(this, scheduler));
    }

    @SchedulerSupport("none")
    public final Single<T> takeUntil(CompletableSource completableSource) {
        return takeUntil((Publisher<E>) new CompletableToFlowable<E>(completableSource));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <E> Single<T> takeUntil(Publisher<E> publisher) {
        return RxJavaPlugins.onAssembly((Single<T>) new SingleTakeUntil<T>(this, publisher));
    }

    @SchedulerSupport("none")
    public final <E> Single<T> takeUntil(SingleSource<? extends E> singleSource) {
        return takeUntil((Publisher<E>) new SingleToFlowable<E>(singleSource));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Single<T> timeout(long j, TimeUnit timeUnit) {
        return timeout0(j, timeUnit, Schedulers.computation(), null);
    }

    @SchedulerSupport("custom")
    public final Single<T> timeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout0(j, timeUnit, scheduler, null);
    }

    @SchedulerSupport("custom")
    public final Single<T> timeout(long j, TimeUnit timeUnit, Scheduler scheduler, SingleSource<? extends T> singleSource) {
        ObjectHelper.requireNonNull(singleSource, "other is null");
        return timeout0(j, timeUnit, scheduler, singleSource);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Single<T> timeout(long j, TimeUnit timeUnit, SingleSource<? extends T> singleSource) {
        ObjectHelper.requireNonNull(singleSource, "other is null");
        return timeout0(j, timeUnit, Schedulers.computation(), singleSource);
    }

    private Single<T> timeout0(long j, TimeUnit timeUnit, Scheduler scheduler, SingleSource<? extends T> singleSource) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        SingleTimeout singleTimeout = new SingleTimeout(this, j, timeUnit, scheduler, singleSource);
        return RxJavaPlugins.onAssembly((Single<T>) singleTimeout);
    }

    @SchedulerSupport("none")
    /* renamed from: to */
    public final <R> R mo13265to(Function<? super Single<T>, R> function) {
        try {
            return function.apply(this);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @SchedulerSupport("none")
    public final Completable toCompletable() {
        return RxJavaPlugins.onAssembly((Completable) new CompletableFromSingle(this));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> toFlowable() {
        if (this instanceof FuseToFlowable) {
            return ((FuseToFlowable) this).fuseToFlowable();
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new SingleToFlowable<T>(this));
    }

    @SchedulerSupport("none")
    public final Future<T> toFuture() {
        return (Future) subscribeWith(new FutureSingleObserver());
    }

    @SchedulerSupport("none")
    public final Maybe<T> toMaybe() {
        if (this instanceof FuseToMaybe) {
            return ((FuseToMaybe) this).fuseToMaybe();
        }
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromSingle<T>(this));
    }

    @SchedulerSupport("none")
    public final Observable<T> toObservable() {
        if (this instanceof FuseToObservable) {
            return ((FuseToObservable) this).fuseToObservable();
        }
        return RxJavaPlugins.onAssembly((Observable<T>) new SingleToObservable<T>(this));
    }

    @SchedulerSupport("none")
    public final <U, R> Single<R> zipWith(SingleSource<U> singleSource, BiFunction<? super T, ? super U, ? extends R> biFunction) {
        return zip(this, singleSource, biFunction);
    }

    @SchedulerSupport("none")
    public final TestObserver<T> test() {
        TestObserver<T> testObserver = new TestObserver<>();
        subscribe((SingleObserver<? super T>) testObserver);
        return testObserver;
    }

    @SchedulerSupport("none")
    public final TestObserver<T> test(boolean z) {
        TestObserver<T> testObserver = new TestObserver<>();
        if (z) {
            testObserver.cancel();
        }
        subscribe((SingleObserver<? super T>) testObserver);
        return testObserver;
    }

    private static <T> Single<T> toSingle(Flowable<T> flowable) {
        return RxJavaPlugins.onAssembly((Single<T>) new FlowableSingleSingle<T>(flowable, null));
    }
}
