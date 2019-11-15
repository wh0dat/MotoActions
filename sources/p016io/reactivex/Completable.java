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
import p016io.reactivex.functions.BiPredicate;
import p016io.reactivex.functions.BooleanSupplier;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.functions.Function;
import p016io.reactivex.functions.Predicate;
import p016io.reactivex.internal.functions.Functions;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.fuseable.FuseToFlowable;
import p016io.reactivex.internal.fuseable.FuseToMaybe;
import p016io.reactivex.internal.fuseable.FuseToObservable;
import p016io.reactivex.internal.observers.BlockingMultiObserver;
import p016io.reactivex.internal.observers.CallbackCompletableObserver;
import p016io.reactivex.internal.observers.EmptyCompletableObserver;
import p016io.reactivex.internal.operators.completable.CompletableAmb;
import p016io.reactivex.internal.operators.completable.CompletableConcat;
import p016io.reactivex.internal.operators.completable.CompletableConcatArray;
import p016io.reactivex.internal.operators.completable.CompletableConcatIterable;
import p016io.reactivex.internal.operators.completable.CompletableCreate;
import p016io.reactivex.internal.operators.completable.CompletableDefer;
import p016io.reactivex.internal.operators.completable.CompletableDelay;
import p016io.reactivex.internal.operators.completable.CompletableDisposeOn;
import p016io.reactivex.internal.operators.completable.CompletableDoFinally;
import p016io.reactivex.internal.operators.completable.CompletableDoOnEvent;
import p016io.reactivex.internal.operators.completable.CompletableEmpty;
import p016io.reactivex.internal.operators.completable.CompletableError;
import p016io.reactivex.internal.operators.completable.CompletableErrorSupplier;
import p016io.reactivex.internal.operators.completable.CompletableFromAction;
import p016io.reactivex.internal.operators.completable.CompletableFromCallable;
import p016io.reactivex.internal.operators.completable.CompletableFromObservable;
import p016io.reactivex.internal.operators.completable.CompletableFromPublisher;
import p016io.reactivex.internal.operators.completable.CompletableFromRunnable;
import p016io.reactivex.internal.operators.completable.CompletableFromSingle;
import p016io.reactivex.internal.operators.completable.CompletableFromUnsafeSource;
import p016io.reactivex.internal.operators.completable.CompletableLift;
import p016io.reactivex.internal.operators.completable.CompletableMerge;
import p016io.reactivex.internal.operators.completable.CompletableMergeArray;
import p016io.reactivex.internal.operators.completable.CompletableMergeDelayErrorArray;
import p016io.reactivex.internal.operators.completable.CompletableMergeDelayErrorIterable;
import p016io.reactivex.internal.operators.completable.CompletableMergeIterable;
import p016io.reactivex.internal.operators.completable.CompletableNever;
import p016io.reactivex.internal.operators.completable.CompletableObserveOn;
import p016io.reactivex.internal.operators.completable.CompletableOnErrorComplete;
import p016io.reactivex.internal.operators.completable.CompletablePeek;
import p016io.reactivex.internal.operators.completable.CompletableResumeNext;
import p016io.reactivex.internal.operators.completable.CompletableSubscribeOn;
import p016io.reactivex.internal.operators.completable.CompletableTimeout;
import p016io.reactivex.internal.operators.completable.CompletableTimer;
import p016io.reactivex.internal.operators.completable.CompletableToFlowable;
import p016io.reactivex.internal.operators.completable.CompletableToObservable;
import p016io.reactivex.internal.operators.completable.CompletableToSingle;
import p016io.reactivex.internal.operators.completable.CompletableUsing;
import p016io.reactivex.internal.operators.flowable.FlowableDelaySubscriptionOther;
import p016io.reactivex.internal.operators.maybe.MaybeDelayWithCompletable;
import p016io.reactivex.internal.operators.maybe.MaybeFromCompletable;
import p016io.reactivex.internal.operators.observable.ObservableDelaySubscriptionOther;
import p016io.reactivex.internal.operators.single.SingleDelayWithCompletable;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.observers.TestObserver;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.schedulers.Schedulers;

/* renamed from: io.reactivex.Completable */
public abstract class Completable implements CompletableSource {
    /* access modifiers changed from: protected */
    public abstract void subscribeActual(CompletableObserver completableObserver);

    @SchedulerSupport("none")
    public static Completable ambArray(CompletableSource... completableSourceArr) {
        ObjectHelper.requireNonNull(completableSourceArr, "sources is null");
        if (completableSourceArr.length == 0) {
            return complete();
        }
        if (completableSourceArr.length == 1) {
            return wrap(completableSourceArr[0]);
        }
        return RxJavaPlugins.onAssembly((Completable) new CompletableAmb(completableSourceArr, null));
    }

    @SchedulerSupport("none")
    public static Completable amb(Iterable<? extends CompletableSource> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableAmb(null, iterable));
    }

    @SchedulerSupport("none")
    public static Completable complete() {
        return RxJavaPlugins.onAssembly(CompletableEmpty.INSTANCE);
    }

    @SchedulerSupport("none")
    public static Completable concatArray(CompletableSource... completableSourceArr) {
        ObjectHelper.requireNonNull(completableSourceArr, "sources is null");
        if (completableSourceArr.length == 0) {
            return complete();
        }
        if (completableSourceArr.length == 1) {
            return wrap(completableSourceArr[0]);
        }
        return RxJavaPlugins.onAssembly((Completable) new CompletableConcatArray(completableSourceArr));
    }

    @SchedulerSupport("none")
    public static Completable concat(Iterable<? extends CompletableSource> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableConcatIterable(iterable));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Completable concat(Publisher<? extends CompletableSource> publisher) {
        return concat(publisher, 2);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Completable concat(Publisher<? extends CompletableSource> publisher, int i) {
        ObjectHelper.requireNonNull(publisher, "sources is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Completable) new CompletableConcat(publisher, i));
    }

    @SchedulerSupport("none")
    public static Completable create(CompletableOnSubscribe completableOnSubscribe) {
        ObjectHelper.requireNonNull(completableOnSubscribe, "source is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableCreate(completableOnSubscribe));
    }

    @SchedulerSupport("none")
    public static Completable unsafeCreate(CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "source is null");
        if (!(completableSource instanceof Completable)) {
            return RxJavaPlugins.onAssembly((Completable) new CompletableFromUnsafeSource(completableSource));
        }
        throw new IllegalArgumentException("Use of unsafeCreate(Completable)!");
    }

    @SchedulerSupport("none")
    public static Completable defer(Callable<? extends CompletableSource> callable) {
        ObjectHelper.requireNonNull(callable, "completableSupplier");
        return RxJavaPlugins.onAssembly((Completable) new CompletableDefer(callable));
    }

    @SchedulerSupport("none")
    public static Completable error(Callable<? extends Throwable> callable) {
        ObjectHelper.requireNonNull(callable, "errorSupplier is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableErrorSupplier(callable));
    }

    @SchedulerSupport("none")
    public static Completable error(Throwable th) {
        ObjectHelper.requireNonNull(th, "error is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableError(th));
    }

    @SchedulerSupport("none")
    public static Completable fromAction(Action action) {
        ObjectHelper.requireNonNull(action, "run is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableFromAction(action));
    }

    @SchedulerSupport("none")
    public static Completable fromCallable(Callable<?> callable) {
        ObjectHelper.requireNonNull(callable, "callable is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableFromCallable(callable));
    }

    @SchedulerSupport("none")
    public static Completable fromFuture(Future<?> future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return fromAction(Functions.futureAction(future));
    }

    @SchedulerSupport("none")
    public static Completable fromRunnable(Runnable runnable) {
        ObjectHelper.requireNonNull(runnable, "run is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableFromRunnable(runnable));
    }

    @SchedulerSupport("none")
    public static <T> Completable fromObservable(ObservableSource<T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "observable is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableFromObservable(observableSource));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public static <T> Completable fromPublisher(Publisher<T> publisher) {
        ObjectHelper.requireNonNull(publisher, "publisher is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableFromPublisher(publisher));
    }

    @SchedulerSupport("none")
    public static <T> Completable fromSingle(SingleSource<T> singleSource) {
        ObjectHelper.requireNonNull(singleSource, "single is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableFromSingle(singleSource));
    }

    @SchedulerSupport("none")
    public static Completable mergeArray(CompletableSource... completableSourceArr) {
        ObjectHelper.requireNonNull(completableSourceArr, "sources is null");
        if (completableSourceArr.length == 0) {
            return complete();
        }
        if (completableSourceArr.length == 1) {
            return wrap(completableSourceArr[0]);
        }
        return RxJavaPlugins.onAssembly((Completable) new CompletableMergeArray(completableSourceArr));
    }

    @SchedulerSupport("none")
    public static Completable merge(Iterable<? extends CompletableSource> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableMergeIterable(iterable));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public static Completable merge(Publisher<? extends CompletableSource> publisher) {
        return merge0(publisher, Integer.MAX_VALUE, false);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Completable merge(Publisher<? extends CompletableSource> publisher, int i) {
        return merge0(publisher, i, false);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    private static Completable merge0(Publisher<? extends CompletableSource> publisher, int i, boolean z) {
        ObjectHelper.requireNonNull(publisher, "sources is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        return RxJavaPlugins.onAssembly((Completable) new CompletableMerge(publisher, i, z));
    }

    @SchedulerSupport("none")
    public static Completable mergeArrayDelayError(CompletableSource... completableSourceArr) {
        ObjectHelper.requireNonNull(completableSourceArr, "sources is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableMergeDelayErrorArray(completableSourceArr));
    }

    @SchedulerSupport("none")
    public static Completable mergeDelayError(Iterable<? extends CompletableSource> iterable) {
        ObjectHelper.requireNonNull(iterable, "sources is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableMergeDelayErrorIterable(iterable));
    }

    @BackpressureSupport(BackpressureKind.UNBOUNDED_IN)
    @SchedulerSupport("none")
    public static Completable mergeDelayError(Publisher<? extends CompletableSource> publisher) {
        return merge0(publisher, Integer.MAX_VALUE, true);
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public static Completable mergeDelayError(Publisher<? extends CompletableSource> publisher, int i) {
        return merge0(publisher, i, true);
    }

    @SchedulerSupport("none")
    public static Completable never() {
        return RxJavaPlugins.onAssembly(CompletableNever.INSTANCE);
    }

    @SchedulerSupport("io.reactivex:computation")
    public static Completable timer(long j, TimeUnit timeUnit) {
        return timer(j, timeUnit, Schedulers.computation());
    }

    @SchedulerSupport("custom")
    public static Completable timer(long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableTimer(j, timeUnit, scheduler));
    }

    private static NullPointerException toNpe(Throwable th) {
        NullPointerException nullPointerException = new NullPointerException("Actually not, but can't pass out an exception otherwise...");
        nullPointerException.initCause(th);
        return nullPointerException;
    }

    @SchedulerSupport("none")
    public static <R> Completable using(Callable<R> callable, Function<? super R, ? extends CompletableSource> function, Consumer<? super R> consumer) {
        return using(callable, function, consumer, true);
    }

    @SchedulerSupport("none")
    public static <R> Completable using(Callable<R> callable, Function<? super R, ? extends CompletableSource> function, Consumer<? super R> consumer, boolean z) {
        ObjectHelper.requireNonNull(callable, "resourceSupplier is null");
        ObjectHelper.requireNonNull(function, "completableFunction is null");
        ObjectHelper.requireNonNull(consumer, "disposer is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableUsing(callable, function, consumer, z));
    }

    @SchedulerSupport("none")
    public static Completable wrap(CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "source is null");
        if (completableSource instanceof Completable) {
            return RxJavaPlugins.onAssembly((Completable) completableSource);
        }
        return RxJavaPlugins.onAssembly((Completable) new CompletableFromUnsafeSource(completableSource));
    }

    @SchedulerSupport("none")
    public final Completable ambWith(CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "other is null");
        return ambArray(this, completableSource);
    }

    @SchedulerSupport("none")
    public final <T> Observable<T> andThen(ObservableSource<T> observableSource) {
        ObjectHelper.requireNonNull(observableSource, "next is null");
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableDelaySubscriptionOther<T>(observableSource, toObservable()));
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <T> Flowable<T> andThen(Publisher<T> publisher) {
        ObjectHelper.requireNonNull(publisher, "next is null");
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableDelaySubscriptionOther<T>(publisher, toFlowable()));
    }

    @SchedulerSupport("none")
    public final <T> Single<T> andThen(SingleSource<T> singleSource) {
        ObjectHelper.requireNonNull(singleSource, "next is null");
        return RxJavaPlugins.onAssembly((Single<T>) new SingleDelayWithCompletable<T>(singleSource, this));
    }

    @SchedulerSupport("none")
    public final <T> Maybe<T> andThen(MaybeSource<T> maybeSource) {
        ObjectHelper.requireNonNull(maybeSource, "next is null");
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeDelayWithCompletable<T>(maybeSource, this));
    }

    @SchedulerSupport("none")
    public final Completable andThen(CompletableSource completableSource) {
        return concatWith(completableSource);
    }

    @SchedulerSupport("none")
    public final void blockingAwait() {
        BlockingMultiObserver blockingMultiObserver = new BlockingMultiObserver();
        subscribe((CompletableObserver) blockingMultiObserver);
        blockingMultiObserver.blockingGet();
    }

    @SchedulerSupport("none")
    public final boolean blockingAwait(long j, TimeUnit timeUnit) {
        BlockingMultiObserver blockingMultiObserver = new BlockingMultiObserver();
        subscribe((CompletableObserver) blockingMultiObserver);
        return blockingMultiObserver.blockingAwait(j, timeUnit);
    }

    @SchedulerSupport("none")
    public final Throwable blockingGet() {
        BlockingMultiObserver blockingMultiObserver = new BlockingMultiObserver();
        subscribe((CompletableObserver) blockingMultiObserver);
        return blockingMultiObserver.blockingGetError();
    }

    @SchedulerSupport("none")
    public final Throwable blockingGet(long j, TimeUnit timeUnit) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        BlockingMultiObserver blockingMultiObserver = new BlockingMultiObserver();
        subscribe((CompletableObserver) blockingMultiObserver);
        return blockingMultiObserver.blockingGetError(j, timeUnit);
    }

    @SchedulerSupport("none")
    public final Completable compose(CompletableTransformer completableTransformer) {
        return wrap(completableTransformer.apply(this));
    }

    @SchedulerSupport("none")
    public final Completable concatWith(CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "other is null");
        return concatArray(this, completableSource);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Completable delay(long j, TimeUnit timeUnit) {
        return delay(j, timeUnit, Schedulers.computation(), false);
    }

    @SchedulerSupport("custom")
    public final Completable delay(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return delay(j, timeUnit, scheduler, false);
    }

    @SchedulerSupport("custom")
    public final Completable delay(long j, TimeUnit timeUnit, Scheduler scheduler, boolean z) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        CompletableDelay completableDelay = new CompletableDelay(this, j, timeUnit, scheduler, z);
        return RxJavaPlugins.onAssembly((Completable) completableDelay);
    }

    @SchedulerSupport("none")
    public final Completable doOnComplete(Action action) {
        return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), action, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Completable doOnDispose(Action action) {
        return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, action);
    }

    @SchedulerSupport("none")
    public final Completable doOnError(Consumer<? super Throwable> consumer) {
        return doOnLifecycle(Functions.emptyConsumer(), consumer, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Completable doOnEvent(Consumer<? super Throwable> consumer) {
        ObjectHelper.requireNonNull(consumer, "onEvent is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableDoOnEvent(this, consumer));
    }

    @SchedulerSupport("none")
    private Completable doOnLifecycle(Consumer<? super Disposable> consumer, Consumer<? super Throwable> consumer2, Action action, Action action2, Action action3, Action action4) {
        ObjectHelper.requireNonNull(consumer, "onSubscribe is null");
        ObjectHelper.requireNonNull(consumer2, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ObjectHelper.requireNonNull(action2, "onTerminate is null");
        ObjectHelper.requireNonNull(action3, "onAfterTerminate is null");
        ObjectHelper.requireNonNull(action4, "onDispose is null");
        CompletablePeek completablePeek = new CompletablePeek(this, consumer, consumer2, action, action2, action3, action4);
        return RxJavaPlugins.onAssembly((Completable) completablePeek);
    }

    @SchedulerSupport("none")
    public final Completable doOnSubscribe(Consumer<? super Disposable> consumer) {
        return doOnLifecycle(consumer, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Completable doOnTerminate(Action action) {
        return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, action, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION);
    }

    @SchedulerSupport("none")
    public final Completable doAfterTerminate(Action action) {
        return doOnLifecycle(Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, action, Functions.EMPTY_ACTION);
    }

    @Experimental
    @SchedulerSupport("none")
    public final Completable doFinally(Action action) {
        ObjectHelper.requireNonNull(action, "onFinally is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableDoFinally(this, action));
    }

    @SchedulerSupport("none")
    public final Completable lift(CompletableOperator completableOperator) {
        ObjectHelper.requireNonNull(completableOperator, "onLift is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableLift(this, completableOperator));
    }

    @SchedulerSupport("none")
    public final Completable mergeWith(CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "other is null");
        return mergeArray(this, completableSource);
    }

    @SchedulerSupport("custom")
    public final Completable observeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableObserveOn(this, scheduler));
    }

    @SchedulerSupport("none")
    public final Completable onErrorComplete() {
        return onErrorComplete(Functions.alwaysTrue());
    }

    @SchedulerSupport("none")
    public final Completable onErrorComplete(Predicate<? super Throwable> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableOnErrorComplete(this, predicate));
    }

    @SchedulerSupport("none")
    public final Completable onErrorResumeNext(Function<? super Throwable, ? extends CompletableSource> function) {
        ObjectHelper.requireNonNull(function, "errorMapper is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableResumeNext(this, function));
    }

    @SchedulerSupport("none")
    public final Completable repeat() {
        return fromPublisher(toFlowable().repeat());
    }

    @SchedulerSupport("none")
    public final Completable repeat(long j) {
        return fromPublisher(toFlowable().repeat(j));
    }

    @SchedulerSupport("none")
    public final Completable repeatUntil(BooleanSupplier booleanSupplier) {
        return fromPublisher(toFlowable().repeatUntil(booleanSupplier));
    }

    @SchedulerSupport("none")
    public final Completable repeatWhen(Function<? super Flowable<Object>, ? extends Publisher<Object>> function) {
        return fromPublisher(toFlowable().repeatWhen(function));
    }

    @SchedulerSupport("none")
    public final Completable retry() {
        return fromPublisher(toFlowable().retry());
    }

    @SchedulerSupport("none")
    public final Completable retry(BiPredicate<? super Integer, ? super Throwable> biPredicate) {
        return fromPublisher(toFlowable().retry(biPredicate));
    }

    @SchedulerSupport("none")
    public final Completable retry(long j) {
        return fromPublisher(toFlowable().retry(j));
    }

    @SchedulerSupport("none")
    public final Completable retry(Predicate<? super Throwable> predicate) {
        return fromPublisher(toFlowable().retry(predicate));
    }

    @SchedulerSupport("none")
    public final Completable retryWhen(Function<? super Flowable<Throwable>, ? extends Publisher<Object>> function) {
        return fromPublisher(toFlowable().retryWhen(function));
    }

    @SchedulerSupport("none")
    public final Completable startWith(CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "other is null");
        return concatArray(completableSource, this);
    }

    @SchedulerSupport("none")
    public final <T> Observable<T> startWith(Observable<T> observable) {
        ObjectHelper.requireNonNull(observable, "other is null");
        return observable.concatWith(toObservable());
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <T> Flowable<T> startWith(Publisher<T> publisher) {
        ObjectHelper.requireNonNull(publisher, "other is null");
        return toFlowable().startWith(publisher);
    }

    @SchedulerSupport("none")
    public final Disposable subscribe() {
        EmptyCompletableObserver emptyCompletableObserver = new EmptyCompletableObserver();
        subscribe((CompletableObserver) emptyCompletableObserver);
        return emptyCompletableObserver;
    }

    @SchedulerSupport("none")
    public final void subscribe(CompletableObserver completableObserver) {
        ObjectHelper.requireNonNull(completableObserver, "s is null");
        try {
            subscribeActual(RxJavaPlugins.onSubscribe(this, completableObserver));
        } catch (NullPointerException e) {
            throw e;
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            RxJavaPlugins.onError(th);
            throw toNpe(th);
        }
    }

    @SchedulerSupport("none")
    public final <E extends CompletableObserver> E subscribeWith(E e) {
        subscribe((CompletableObserver) e);
        return e;
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Action action, Consumer<? super Throwable> consumer) {
        ObjectHelper.requireNonNull(consumer, "onError is null");
        ObjectHelper.requireNonNull(action, "onComplete is null");
        CallbackCompletableObserver callbackCompletableObserver = new CallbackCompletableObserver(consumer, action);
        subscribe((CompletableObserver) callbackCompletableObserver);
        return callbackCompletableObserver;
    }

    @SchedulerSupport("none")
    public final Disposable subscribe(Action action) {
        ObjectHelper.requireNonNull(action, "onComplete is null");
        CallbackCompletableObserver callbackCompletableObserver = new CallbackCompletableObserver(action);
        subscribe((CompletableObserver) callbackCompletableObserver);
        return callbackCompletableObserver;
    }

    @SchedulerSupport("custom")
    public final Completable subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableSubscribeOn(this, scheduler));
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Completable timeout(long j, TimeUnit timeUnit) {
        return timeout0(j, timeUnit, Schedulers.computation(), null);
    }

    @SchedulerSupport("io.reactivex:computation")
    public final Completable timeout(long j, TimeUnit timeUnit, CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "other is null");
        return timeout0(j, timeUnit, Schedulers.computation(), completableSource);
    }

    @SchedulerSupport("custom")
    public final Completable timeout(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return timeout0(j, timeUnit, scheduler, null);
    }

    @SchedulerSupport("custom")
    public final Completable timeout(long j, TimeUnit timeUnit, Scheduler scheduler, CompletableSource completableSource) {
        ObjectHelper.requireNonNull(completableSource, "other is null");
        return timeout0(j, timeUnit, scheduler, completableSource);
    }

    @SchedulerSupport("custom")
    private Completable timeout0(long j, TimeUnit timeUnit, Scheduler scheduler, CompletableSource completableSource) {
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        CompletableTimeout completableTimeout = new CompletableTimeout(this, j, timeUnit, scheduler, completableSource);
        return RxJavaPlugins.onAssembly((Completable) completableTimeout);
    }

    @SchedulerSupport("none")
    /* renamed from: to */
    public final <U> U mo12393to(Function<? super Completable, U> function) {
        try {
            return function.apply(this);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final <T> Flowable<T> toFlowable() {
        if (this instanceof FuseToFlowable) {
            return ((FuseToFlowable) this).fuseToFlowable();
        }
        return RxJavaPlugins.onAssembly((Flowable<T>) new CompletableToFlowable<T>(this));
    }

    @SchedulerSupport("none")
    public final <T> Maybe<T> toMaybe() {
        if (this instanceof FuseToMaybe) {
            return ((FuseToMaybe) this).fuseToMaybe();
        }
        return RxJavaPlugins.onAssembly((Maybe<T>) new MaybeFromCompletable<T>(this));
    }

    @SchedulerSupport("none")
    public final <T> Observable<T> toObservable() {
        if (this instanceof FuseToObservable) {
            return ((FuseToObservable) this).fuseToObservable();
        }
        return RxJavaPlugins.onAssembly((Observable<T>) new CompletableToObservable<T>(this));
    }

    @SchedulerSupport("none")
    public final <T> Single<T> toSingle(Callable<? extends T> callable) {
        ObjectHelper.requireNonNull(callable, "completionValueSupplier is null");
        return RxJavaPlugins.onAssembly((Single<T>) new CompletableToSingle<T>(this, callable, null));
    }

    @SchedulerSupport("none")
    public final <T> Single<T> toSingleDefault(T t) {
        ObjectHelper.requireNonNull(t, "completionValue is null");
        return RxJavaPlugins.onAssembly((Single<T>) new CompletableToSingle<T>(this, null, t));
    }

    @SchedulerSupport("custom")
    public final Completable unsubscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly((Completable) new CompletableDisposeOn(this, scheduler));
    }

    @SchedulerSupport("none")
    public final TestObserver<Void> test() {
        TestObserver<Void> testObserver = new TestObserver<>();
        subscribe((CompletableObserver) testObserver);
        return testObserver;
    }

    @SchedulerSupport("none")
    public final TestObserver<Void> test(boolean z) {
        TestObserver<Void> testObserver = new TestObserver<>();
        if (z) {
            testObserver.cancel();
        }
        subscribe((CompletableObserver) testObserver);
        return testObserver;
    }
}
