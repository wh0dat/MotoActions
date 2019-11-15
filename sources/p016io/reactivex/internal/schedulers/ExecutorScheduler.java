package p016io.reactivex.internal.schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.disposables.CompositeDisposable;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.disposables.Disposables;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.disposables.SequentialDisposable;
import p016io.reactivex.internal.queue.MpscLinkedQueue;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.schedulers.Schedulers;

/* renamed from: io.reactivex.internal.schedulers.ExecutorScheduler */
public final class ExecutorScheduler extends Scheduler {
    static final Scheduler HELPER = Schedulers.single();
    final Executor executor;

    /* renamed from: io.reactivex.internal.schedulers.ExecutorScheduler$ExecutorWorker */
    public static final class ExecutorWorker extends Worker implements Runnable {
        volatile boolean disposed;
        final Executor executor;
        final MpscLinkedQueue<Runnable> queue;
        final CompositeDisposable tasks = new CompositeDisposable();
        final AtomicInteger wip = new AtomicInteger();

        /* renamed from: io.reactivex.internal.schedulers.ExecutorScheduler$ExecutorWorker$BooleanRunnable */
        static final class BooleanRunnable extends AtomicBoolean implements Runnable, Disposable {
            private static final long serialVersionUID = -2421395018820541164L;
            final Runnable actual;

            BooleanRunnable(Runnable runnable) {
                this.actual = runnable;
            }

            public void run() {
                if (!get()) {
                    this.actual.run();
                }
            }

            public void dispose() {
                lazySet(true);
            }

            public boolean isDisposed() {
                return get();
            }
        }

        public ExecutorWorker(Executor executor2) {
            this.executor = executor2;
            this.queue = new MpscLinkedQueue<>();
        }

        public Disposable schedule(Runnable runnable) {
            if (this.disposed) {
                return EmptyDisposable.INSTANCE;
            }
            BooleanRunnable booleanRunnable = new BooleanRunnable(RxJavaPlugins.onSchedule(runnable));
            this.queue.offer(booleanRunnable);
            if (this.wip.getAndIncrement() == 0) {
                try {
                    this.executor.execute(this);
                } catch (RejectedExecutionException e) {
                    this.disposed = true;
                    this.queue.clear();
                    RxJavaPlugins.onError(e);
                    return EmptyDisposable.INSTANCE;
                }
            }
            return booleanRunnable;
        }

        public Disposable schedule(Runnable runnable, long j, TimeUnit timeUnit) {
            if (j <= 0) {
                return schedule(runnable);
            }
            if (this.disposed) {
                return EmptyDisposable.INSTANCE;
            }
            SequentialDisposable sequentialDisposable = new SequentialDisposable();
            final SequentialDisposable sequentialDisposable2 = new SequentialDisposable(sequentialDisposable);
            final Runnable onSchedule = RxJavaPlugins.onSchedule(runnable);
            ScheduledRunnable scheduledRunnable = new ScheduledRunnable(new Runnable() {
                public void run() {
                    sequentialDisposable2.replace(ExecutorWorker.this.schedule(onSchedule));
                }
            }, this.tasks);
            this.tasks.add(scheduledRunnable);
            if (this.executor instanceof ScheduledExecutorService) {
                try {
                    scheduledRunnable.setFuture(((ScheduledExecutorService) this.executor).schedule(scheduledRunnable, j, timeUnit));
                } catch (RejectedExecutionException e) {
                    this.disposed = true;
                    RxJavaPlugins.onError(e);
                    return EmptyDisposable.INSTANCE;
                }
            } else {
                scheduledRunnable.setFuture(new DisposeOnCancel(ExecutorScheduler.HELPER.scheduleDirect(scheduledRunnable, j, timeUnit)));
            }
            sequentialDisposable.replace(scheduledRunnable);
            return sequentialDisposable2;
        }

        public void dispose() {
            if (!this.disposed) {
                this.disposed = true;
                this.tasks.dispose();
                if (this.wip.getAndIncrement() == 0) {
                    this.queue.clear();
                }
            }
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x001a, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001b, code lost:
            r1 = r3.wip.addAndGet(-r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0022, code lost:
            if (r1 != 0) goto L_0x0003;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0024, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0015, code lost:
            if (r3.disposed == false) goto L_0x001b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
            r0.clear();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r3 = this;
                io.reactivex.internal.queue.MpscLinkedQueue<java.lang.Runnable> r0 = r3.queue
                r1 = 1
            L_0x0003:
                boolean r2 = r3.disposed
                if (r2 == 0) goto L_0x000b
                r0.clear()
                return
            L_0x000b:
                java.lang.Object r2 = r0.poll()
                java.lang.Runnable r2 = (java.lang.Runnable) r2
                if (r2 != 0) goto L_0x0025
                boolean r2 = r3.disposed
                if (r2 == 0) goto L_0x001b
                r0.clear()
                return
            L_0x001b:
                java.util.concurrent.atomic.AtomicInteger r2 = r3.wip
                int r1 = -r1
                int r1 = r2.addAndGet(r1)
                if (r1 != 0) goto L_0x0003
                return
            L_0x0025:
                r2.run()
                boolean r2 = r3.disposed
                if (r2 == 0) goto L_0x000b
                r0.clear()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.schedulers.ExecutorScheduler.ExecutorWorker.run():void");
        }
    }

    public ExecutorScheduler(Executor executor2) {
        this.executor = executor2;
    }

    public Worker createWorker() {
        return new ExecutorWorker(this.executor);
    }

    public Disposable scheduleDirect(Runnable runnable) {
        Runnable onSchedule = RxJavaPlugins.onSchedule(runnable);
        try {
            if (this.executor instanceof ExecutorService) {
                return Disposables.fromFuture(((ExecutorService) this.executor).submit(onSchedule));
            }
            BooleanRunnable booleanRunnable = new BooleanRunnable(onSchedule);
            this.executor.execute(booleanRunnable);
            return booleanRunnable;
        } catch (RejectedExecutionException e) {
            RxJavaPlugins.onError(e);
            return EmptyDisposable.INSTANCE;
        }
    }

    public Disposable scheduleDirect(Runnable runnable, long j, TimeUnit timeUnit) {
        final Runnable onSchedule = RxJavaPlugins.onSchedule(runnable);
        if (this.executor instanceof ScheduledExecutorService) {
            try {
                return Disposables.fromFuture(((ScheduledExecutorService) this.executor).schedule(onSchedule, j, timeUnit));
            } catch (RejectedExecutionException e) {
                RxJavaPlugins.onError(e);
                return EmptyDisposable.INSTANCE;
            }
        } else {
            SequentialDisposable sequentialDisposable = new SequentialDisposable();
            final SequentialDisposable sequentialDisposable2 = new SequentialDisposable(sequentialDisposable);
            sequentialDisposable.replace(HELPER.scheduleDirect(new Runnable() {
                public void run() {
                    sequentialDisposable2.replace(ExecutorScheduler.this.scheduleDirect(onSchedule));
                }
            }, j, timeUnit));
            return sequentialDisposable2;
        }
    }

    public Disposable schedulePeriodicallyDirect(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
        if (!(this.executor instanceof ScheduledExecutorService)) {
            return super.schedulePeriodicallyDirect(runnable, j, j2, timeUnit);
        }
        try {
            return Disposables.fromFuture(((ScheduledExecutorService) this.executor).scheduleAtFixedRate(RxJavaPlugins.onSchedule(runnable), j, j2, timeUnit));
        } catch (RejectedExecutionException e) {
            RxJavaPlugins.onError(e);
            return EmptyDisposable.INSTANCE;
        }
    }
}
