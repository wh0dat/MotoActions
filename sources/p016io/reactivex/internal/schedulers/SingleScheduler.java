package p016io.reactivex.internal.schedulers;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.disposables.CompositeDisposable;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.disposables.Disposables;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.schedulers.SingleScheduler */
public final class SingleScheduler extends Scheduler {
    private static final String KEY_SINGLE_PRIORITY = "rx2.single-priority";
    static final ScheduledExecutorService SHUTDOWN = Executors.newScheduledThreadPool(0);
    static final RxThreadFactory SINGLE_THREAD_FACTORY = new RxThreadFactory(THREAD_NAME_PREFIX, Math.max(1, Math.min(10, Integer.getInteger(KEY_SINGLE_PRIORITY, 5).intValue())));
    private static final String THREAD_NAME_PREFIX = "RxSingleScheduler";
    final AtomicReference<ScheduledExecutorService> executor = new AtomicReference<>();

    /* renamed from: io.reactivex.internal.schedulers.SingleScheduler$ScheduledWorker */
    static final class ScheduledWorker extends Worker {
        volatile boolean disposed;
        final ScheduledExecutorService executor;
        final CompositeDisposable tasks = new CompositeDisposable();

        ScheduledWorker(ScheduledExecutorService scheduledExecutorService) {
            this.executor = scheduledExecutorService;
        }

        public Disposable schedule(Runnable runnable, long j, TimeUnit timeUnit) {
            Future future;
            if (this.disposed) {
                return EmptyDisposable.INSTANCE;
            }
            ScheduledRunnable scheduledRunnable = new ScheduledRunnable(RxJavaPlugins.onSchedule(runnable), this.tasks);
            this.tasks.add(scheduledRunnable);
            if (j <= 0) {
                try {
                    future = this.executor.submit(scheduledRunnable);
                } catch (RejectedExecutionException e) {
                    dispose();
                    RxJavaPlugins.onError(e);
                    return EmptyDisposable.INSTANCE;
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
                this.tasks.dispose();
            }
        }

        public boolean isDisposed() {
            return this.disposed;
        }
    }

    static {
        SHUTDOWN.shutdown();
    }

    public SingleScheduler() {
        this.executor.lazySet(createExecutor());
    }

    static ScheduledExecutorService createExecutor() {
        return SchedulerPoolFactory.create(SINGLE_THREAD_FACTORY);
    }

    public void start() {
        ScheduledExecutorService scheduledExecutorService;
        ScheduledExecutorService scheduledExecutorService2 = null;
        do {
            scheduledExecutorService = (ScheduledExecutorService) this.executor.get();
            if (scheduledExecutorService != SHUTDOWN) {
                if (scheduledExecutorService2 != null) {
                    scheduledExecutorService2.shutdown();
                }
                return;
            } else if (scheduledExecutorService2 == null) {
                scheduledExecutorService2 = createExecutor();
            }
        } while (!this.executor.compareAndSet(scheduledExecutorService, scheduledExecutorService2));
    }

    public void shutdown() {
        if (((ScheduledExecutorService) this.executor.get()) != SHUTDOWN) {
            ScheduledExecutorService scheduledExecutorService = (ScheduledExecutorService) this.executor.getAndSet(SHUTDOWN);
            if (scheduledExecutorService != SHUTDOWN) {
                scheduledExecutorService.shutdownNow();
            }
        }
    }

    public Worker createWorker() {
        return new ScheduledWorker((ScheduledExecutorService) this.executor.get());
    }

    public Disposable scheduleDirect(Runnable runnable, long j, TimeUnit timeUnit) {
        Future future;
        Runnable onSchedule = RxJavaPlugins.onSchedule(runnable);
        if (j <= 0) {
            try {
                future = ((ScheduledExecutorService) this.executor.get()).submit(onSchedule);
            } catch (RejectedExecutionException e) {
                RxJavaPlugins.onError(e);
                return EmptyDisposable.INSTANCE;
            }
        } else {
            future = ((ScheduledExecutorService) this.executor.get()).schedule(onSchedule, j, timeUnit);
        }
        return Disposables.fromFuture(future);
    }

    public Disposable schedulePeriodicallyDirect(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
        try {
            return Disposables.fromFuture(((ScheduledExecutorService) this.executor.get()).scheduleAtFixedRate(RxJavaPlugins.onSchedule(runnable), j, j2, timeUnit));
        } catch (RejectedExecutionException e) {
            RxJavaPlugins.onError(e);
            return EmptyDisposable.INSTANCE;
        }
    }
}
