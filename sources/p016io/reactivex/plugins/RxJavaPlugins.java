package p016io.reactivex.plugins;

import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.Flowable;
import p016io.reactivex.Maybe;
import p016io.reactivex.MaybeObserver;
import p016io.reactivex.Observable;
import p016io.reactivex.Observer;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.flowables.ConnectableFlowable;
import p016io.reactivex.functions.BiFunction;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.observables.ConnectableObservable;

/* renamed from: io.reactivex.plugins.RxJavaPlugins */
public final class RxJavaPlugins {
    static volatile Consumer<Throwable> errorHandler;
    static volatile boolean lockdown;
    static volatile Function<Completable, Completable> onCompletableAssembly;
    static volatile BiFunction<Completable, CompletableObserver, CompletableObserver> onCompletableSubscribe;
    static volatile Function<Scheduler, Scheduler> onComputationHandler;
    static volatile Function<ConnectableFlowable, ConnectableFlowable> onConnectableFlowableAssembly;
    static volatile Function<ConnectableObservable, ConnectableObservable> onConnectableObservableAssembly;
    static volatile Function<Flowable, Flowable> onFlowableAssembly;
    static volatile BiFunction<Flowable, Subscriber, Subscriber> onFlowableSubscribe;
    static volatile Function<Callable<Scheduler>, Scheduler> onInitComputationHandler;
    static volatile Function<Callable<Scheduler>, Scheduler> onInitIoHandler;
    static volatile Function<Callable<Scheduler>, Scheduler> onInitNewThreadHandler;
    static volatile Function<Callable<Scheduler>, Scheduler> onInitSingleHandler;
    static volatile Function<Scheduler, Scheduler> onIoHandler;
    static volatile Function<Maybe, Maybe> onMaybeAssembly;
    static volatile BiFunction<Maybe, MaybeObserver, MaybeObserver> onMaybeSubscribe;
    static volatile Function<Scheduler, Scheduler> onNewThreadHandler;
    static volatile Function<Observable, Observable> onObservableAssembly;
    static volatile BiFunction<Observable, Observer, Observer> onObservableSubscribe;
    static volatile Function<Runnable, Runnable> onScheduleHandler;
    static volatile Function<Single, Single> onSingleAssembly;
    static volatile Function<Scheduler, Scheduler> onSingleHandler;
    static volatile BiFunction<Single, SingleObserver, SingleObserver> onSingleSubscribe;

    public static void lockdown() {
        lockdown = true;
    }

    public static boolean isLockdown() {
        return lockdown;
    }

    public static Function<Scheduler, Scheduler> getComputationSchedulerHandler() {
        return onComputationHandler;
    }

    public static Consumer<Throwable> getErrorHandler() {
        return errorHandler;
    }

    public static Function<Callable<Scheduler>, Scheduler> getInitComputationSchedulerHandler() {
        return onInitComputationHandler;
    }

    public static Function<Callable<Scheduler>, Scheduler> getInitIoSchedulerHandler() {
        return onInitIoHandler;
    }

    public static Function<Callable<Scheduler>, Scheduler> getInitNewThreadSchedulerHandler() {
        return onInitNewThreadHandler;
    }

    public static Function<Callable<Scheduler>, Scheduler> getInitSingleSchedulerHandler() {
        return onInitSingleHandler;
    }

    public static Function<Scheduler, Scheduler> getIoSchedulerHandler() {
        return onIoHandler;
    }

    public static Function<Scheduler, Scheduler> getNewThreadSchedulerHandler() {
        return onNewThreadHandler;
    }

    public static Function<Runnable, Runnable> getScheduleHandler() {
        return onScheduleHandler;
    }

    public static Function<Scheduler, Scheduler> getSingleSchedulerHandler() {
        return onSingleHandler;
    }

    public static Scheduler initComputationScheduler(Callable<Scheduler> callable) {
        ObjectHelper.requireNonNull(callable, "Scheduler Callable can't be null");
        Function<Callable<Scheduler>, Scheduler> function = onInitComputationHandler;
        if (function == null) {
            return callRequireNonNull(callable);
        }
        return applyRequireNonNull(function, callable);
    }

    public static Scheduler initIoScheduler(Callable<Scheduler> callable) {
        ObjectHelper.requireNonNull(callable, "Scheduler Callable can't be null");
        Function<Callable<Scheduler>, Scheduler> function = onInitIoHandler;
        if (function == null) {
            return callRequireNonNull(callable);
        }
        return applyRequireNonNull(function, callable);
    }

    public static Scheduler initNewThreadScheduler(Callable<Scheduler> callable) {
        ObjectHelper.requireNonNull(callable, "Scheduler Callable can't be null");
        Function<Callable<Scheduler>, Scheduler> function = onInitNewThreadHandler;
        if (function == null) {
            return callRequireNonNull(callable);
        }
        return applyRequireNonNull(function, callable);
    }

    public static Scheduler initSingleScheduler(Callable<Scheduler> callable) {
        ObjectHelper.requireNonNull(callable, "Scheduler Callable can't be null");
        Function<Callable<Scheduler>, Scheduler> function = onInitSingleHandler;
        if (function == null) {
            return callRequireNonNull(callable);
        }
        return applyRequireNonNull(function, callable);
    }

    public static Scheduler onComputationScheduler(Scheduler scheduler) {
        Function<Scheduler, Scheduler> function = onComputationHandler;
        if (function == null) {
            return scheduler;
        }
        return (Scheduler) apply(function, scheduler);
    }

    public static void onError(Throwable th) {
        Consumer<Throwable> consumer = errorHandler;
        if (th == null) {
            th = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        }
        if (consumer != null) {
            try {
                consumer.accept(th);
                return;
            } catch (Throwable th2) {
                th2.printStackTrace();
                uncaught(th2);
            }
        }
        th.printStackTrace();
        uncaught(th);
    }

    static void uncaught(Throwable th) {
        Thread currentThread = Thread.currentThread();
        currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, th);
    }

    public static Scheduler onIoScheduler(Scheduler scheduler) {
        Function<Scheduler, Scheduler> function = onIoHandler;
        if (function == null) {
            return scheduler;
        }
        return (Scheduler) apply(function, scheduler);
    }

    public static Scheduler onNewThreadScheduler(Scheduler scheduler) {
        Function<Scheduler, Scheduler> function = onNewThreadHandler;
        if (function == null) {
            return scheduler;
        }
        return (Scheduler) apply(function, scheduler);
    }

    public static Runnable onSchedule(Runnable runnable) {
        Function<Runnable, Runnable> function = onScheduleHandler;
        if (function == null) {
            return runnable;
        }
        return (Runnable) apply(function, runnable);
    }

    public static Scheduler onSingleScheduler(Scheduler scheduler) {
        Function<Scheduler, Scheduler> function = onSingleHandler;
        if (function == null) {
            return scheduler;
        }
        return (Scheduler) apply(function, scheduler);
    }

    public static void reset() {
        setErrorHandler(null);
        setScheduleHandler(null);
        setComputationSchedulerHandler(null);
        setInitComputationSchedulerHandler(null);
        setIoSchedulerHandler(null);
        setInitIoSchedulerHandler(null);
        setSingleSchedulerHandler(null);
        setInitSingleSchedulerHandler(null);
        setNewThreadSchedulerHandler(null);
        setInitNewThreadSchedulerHandler(null);
        setOnFlowableAssembly(null);
        setOnFlowableSubscribe(null);
        setOnObservableAssembly(null);
        setOnObservableSubscribe(null);
        setOnSingleAssembly(null);
        setOnSingleSubscribe(null);
        setOnCompletableAssembly(null);
        setOnCompletableSubscribe(null);
        setOnConnectableFlowableAssembly(null);
        setOnConnectableObservableAssembly(null);
        setOnMaybeAssembly(null);
        setOnMaybeSubscribe(null);
    }

    public static void setComputationSchedulerHandler(Function<Scheduler, Scheduler> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onComputationHandler = function;
    }

    public static void setErrorHandler(Consumer<Throwable> consumer) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        errorHandler = consumer;
    }

    public static void setInitComputationSchedulerHandler(Function<Callable<Scheduler>, Scheduler> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onInitComputationHandler = function;
    }

    public static void setInitIoSchedulerHandler(Function<Callable<Scheduler>, Scheduler> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onInitIoHandler = function;
    }

    public static void setInitNewThreadSchedulerHandler(Function<Callable<Scheduler>, Scheduler> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onInitNewThreadHandler = function;
    }

    public static void setInitSingleSchedulerHandler(Function<Callable<Scheduler>, Scheduler> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onInitSingleHandler = function;
    }

    public static void setIoSchedulerHandler(Function<Scheduler, Scheduler> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onIoHandler = function;
    }

    public static void setNewThreadSchedulerHandler(Function<Scheduler, Scheduler> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onNewThreadHandler = function;
    }

    public static void setScheduleHandler(Function<Runnable, Runnable> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onScheduleHandler = function;
    }

    public static void setSingleSchedulerHandler(Function<Scheduler, Scheduler> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onSingleHandler = function;
    }

    static void unlock() {
        lockdown = false;
    }

    public static Function<Completable, Completable> getOnCompletableAssembly() {
        return onCompletableAssembly;
    }

    public static BiFunction<Completable, CompletableObserver, CompletableObserver> getOnCompletableSubscribe() {
        return onCompletableSubscribe;
    }

    public static Function<Flowable, Flowable> getOnFlowableAssembly() {
        return onFlowableAssembly;
    }

    public static Function<ConnectableFlowable, ConnectableFlowable> getOnConnectableFlowableAssembly() {
        return onConnectableFlowableAssembly;
    }

    public static BiFunction<Flowable, Subscriber, Subscriber> getOnFlowableSubscribe() {
        return onFlowableSubscribe;
    }

    public static BiFunction<Maybe, MaybeObserver, MaybeObserver> getOnMaybeSubscribe() {
        return onMaybeSubscribe;
    }

    public static Function<Maybe, Maybe> getOnMaybeAssembly() {
        return onMaybeAssembly;
    }

    public static Function<Single, Single> getOnSingleAssembly() {
        return onSingleAssembly;
    }

    public static BiFunction<Single, SingleObserver, SingleObserver> getOnSingleSubscribe() {
        return onSingleSubscribe;
    }

    public static Function<Observable, Observable> getOnObservableAssembly() {
        return onObservableAssembly;
    }

    public static Function<ConnectableObservable, ConnectableObservable> getOnConnectableObservableAssembly() {
        return onConnectableObservableAssembly;
    }

    public static BiFunction<Observable, Observer, Observer> getOnObservableSubscribe() {
        return onObservableSubscribe;
    }

    public static void setOnCompletableAssembly(Function<Completable, Completable> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onCompletableAssembly = function;
    }

    public static void setOnCompletableSubscribe(BiFunction<Completable, CompletableObserver, CompletableObserver> biFunction) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onCompletableSubscribe = biFunction;
    }

    public static void setOnFlowableAssembly(Function<Flowable, Flowable> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onFlowableAssembly = function;
    }

    public static void setOnMaybeAssembly(Function<Maybe, Maybe> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onMaybeAssembly = function;
    }

    public static void setOnConnectableFlowableAssembly(Function<ConnectableFlowable, ConnectableFlowable> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onConnectableFlowableAssembly = function;
    }

    public static void setOnFlowableSubscribe(BiFunction<Flowable, Subscriber, Subscriber> biFunction) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onFlowableSubscribe = biFunction;
    }

    public static void setOnMaybeSubscribe(BiFunction<Maybe, MaybeObserver, MaybeObserver> biFunction) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onMaybeSubscribe = biFunction;
    }

    public static void setOnObservableAssembly(Function<Observable, Observable> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onObservableAssembly = function;
    }

    public static void setOnConnectableObservableAssembly(Function<ConnectableObservable, ConnectableObservable> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onConnectableObservableAssembly = function;
    }

    public static void setOnObservableSubscribe(BiFunction<Observable, Observer, Observer> biFunction) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onObservableSubscribe = biFunction;
    }

    public static void setOnSingleAssembly(Function<Single, Single> function) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onSingleAssembly = function;
    }

    public static void setOnSingleSubscribe(BiFunction<Single, SingleObserver, SingleObserver> biFunction) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onSingleSubscribe = biFunction;
    }

    public static <T> Subscriber<? super T> onSubscribe(Flowable<T> flowable, Subscriber<? super T> subscriber) {
        BiFunction<Flowable, Subscriber, Subscriber> biFunction = onFlowableSubscribe;
        return biFunction != null ? (Subscriber) apply(biFunction, flowable, subscriber) : subscriber;
    }

    public static <T> Observer<? super T> onSubscribe(Observable<T> observable, Observer<? super T> observer) {
        BiFunction<Observable, Observer, Observer> biFunction = onObservableSubscribe;
        return biFunction != null ? (Observer) apply(biFunction, observable, observer) : observer;
    }

    public static <T> SingleObserver<? super T> onSubscribe(Single<T> single, SingleObserver<? super T> singleObserver) {
        BiFunction<Single, SingleObserver, SingleObserver> biFunction = onSingleSubscribe;
        return biFunction != null ? (SingleObserver) apply(biFunction, single, singleObserver) : singleObserver;
    }

    public static CompletableObserver onSubscribe(Completable completable, CompletableObserver completableObserver) {
        BiFunction<Completable, CompletableObserver, CompletableObserver> biFunction = onCompletableSubscribe;
        return biFunction != null ? (CompletableObserver) apply(biFunction, completable, completableObserver) : completableObserver;
    }

    public static <T> MaybeObserver<? super T> onSubscribe(Maybe<T> maybe, MaybeObserver<? super T> maybeObserver) {
        BiFunction<Maybe, MaybeObserver, MaybeObserver> biFunction = onMaybeSubscribe;
        return biFunction != null ? (MaybeObserver) apply(biFunction, maybe, maybeObserver) : maybeObserver;
    }

    public static <T> Maybe<T> onAssembly(Maybe<T> maybe) {
        Function<Maybe, Maybe> function = onMaybeAssembly;
        return function != null ? (Maybe) apply(function, maybe) : maybe;
    }

    public static <T> Flowable<T> onAssembly(Flowable<T> flowable) {
        Function<Flowable, Flowable> function = onFlowableAssembly;
        return function != null ? (Flowable) apply(function, flowable) : flowable;
    }

    public static <T> ConnectableFlowable<T> onAssembly(ConnectableFlowable<T> connectableFlowable) {
        Function<ConnectableFlowable, ConnectableFlowable> function = onConnectableFlowableAssembly;
        return function != null ? (ConnectableFlowable) apply(function, connectableFlowable) : connectableFlowable;
    }

    public static <T> Observable<T> onAssembly(Observable<T> observable) {
        Function<Observable, Observable> function = onObservableAssembly;
        return function != null ? (Observable) apply(function, observable) : observable;
    }

    public static <T> ConnectableObservable<T> onAssembly(ConnectableObservable<T> connectableObservable) {
        Function<ConnectableObservable, ConnectableObservable> function = onConnectableObservableAssembly;
        return function != null ? (ConnectableObservable) apply(function, connectableObservable) : connectableObservable;
    }

    public static <T> Single<T> onAssembly(Single<T> single) {
        Function<Single, Single> function = onSingleAssembly;
        return function != null ? (Single) apply(function, single) : single;
    }

    public static Completable onAssembly(Completable completable) {
        Function<Completable, Completable> function = onCompletableAssembly;
        return function != null ? (Completable) apply(function, completable) : completable;
    }

    static <T, R> R apply(Function<T, R> function, T t) {
        try {
            return function.apply(t);
        } catch (Throwable th) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    static <T, U, R> R apply(BiFunction<T, U, R> biFunction, T t, U u) {
        try {
            return biFunction.apply(t, u);
        } catch (Throwable th) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    static Scheduler callRequireNonNull(Callable<Scheduler> callable) {
        try {
            return (Scheduler) ObjectHelper.requireNonNull(callable.call(), "Scheduler Callable result can't be null");
        } catch (Throwable th) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    static Scheduler applyRequireNonNull(Function<Callable<Scheduler>, Scheduler> function, Callable<Scheduler> callable) {
        return (Scheduler) ObjectHelper.requireNonNull(apply(function, callable), "Scheduler Callable result can't be null");
    }

    private RxJavaPlugins() {
        throw new IllegalStateException("No instances!");
    }
}
