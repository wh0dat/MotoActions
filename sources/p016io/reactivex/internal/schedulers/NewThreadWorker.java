package p016io.reactivex.internal.schedulers;

import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.disposables.Disposables;
import p016io.reactivex.internal.disposables.DisposableContainer;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.schedulers.NewThreadWorker */
public class NewThreadWorker extends Worker implements Disposable {
    volatile boolean disposed;
    private final ScheduledExecutorService executor;

    public NewThreadWorker(ThreadFactory threadFactory) {
        this.executor = SchedulerPoolFactory.create(threadFactory);
    }

    public Disposable schedule(Runnable runnable) {
        return schedule(runnable, 0, null);
    }

    public Disposable schedule(Runnable runnable, long j, TimeUnit timeUnit) {
        if (this.disposed) {
            return EmptyDisposable.INSTANCE;
        }
        return scheduleActual(runnable, j, timeUnit, null);
    }

    public Disposable scheduleDirect(Runnable runnable, long j, TimeUnit timeUnit) {
        Future future;
        Runnable onSchedule = RxJavaPlugins.onSchedule(runnable);
        if (j <= 0) {
            try {
                future = this.executor.submit(onSchedule);
            } catch (RejectedExecutionException e) {
                RxJavaPlugins.onError(e);
                return EmptyDisposable.INSTANCE;
            }
        } else {
            future = this.executor.schedule(onSchedule, j, timeUnit);
        }
        return Disposables.fromFuture(future);
    }

    public Disposable schedulePeriodicallyDirect(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
        try {
            return Disposables.fromFuture(this.executor.scheduleAtFixedRate(RxJavaPlugins.onSchedule(runnable), j, j2, timeUnit));
        } catch (RejectedExecutionException e) {
            RxJavaPlugins.onError(e);
            return EmptyDisposable.INSTANCE;
        }
    }

    public ScheduledRunnable scheduleActual(Runnable runnable, long j, TimeUnit timeUnit, DisposableContainer disposableContainer) {
        Future future;
        ScheduledRunnable scheduledRunnable = new ScheduledRunnable(RxJavaPlugins.onSchedule(runnable), disposableContainer);
        if (disposableContainer != null && !disposableContainer.add(scheduledRunnable)) {
            return scheduledRunnable;
        }
        if (j <= 0) {
            try {
                future = this.executor.submit(scheduledRunnable);
            } catch (RejectedExecutionException e) {
                disposableContainer.remove(scheduledRunnable);
                RxJavaPlugins.onError(e);
            }
        } else {
            future = this.executor.schedule(scheduledRunnable, j, timeUnit);
        }
        scheduledRunnable.setFuture(future);
        return scheduledRunnable;
    }

    public void dispose() {
        if (!this.disposed) {
            this.disposed = true;
            this.executor.shutdownNow();
        }
    }

    public boolean isDisposed() {
        return this.disposed;
    }
}
