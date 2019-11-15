package p016io.reactivex;

import java.util.concurrent.TimeUnit;
import p016io.reactivex.annotations.Experimental;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.disposables.SequentialDisposable;
import p016io.reactivex.internal.schedulers.SchedulerWhen;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.Scheduler */
public abstract class Scheduler {
    static final long CLOCK_DRIFT_TOLERANCE_NANOSECONDS = TimeUnit.MINUTES.toNanos(Long.getLong("rx2.scheduler.drift-tolerance", 15).longValue());

    /* renamed from: io.reactivex.Scheduler$PeriodicDirectTask */
    static class PeriodicDirectTask implements Runnable, Disposable {
        volatile boolean disposed;
        final Runnable run;
        final Worker worker;

        PeriodicDirectTask(Runnable runnable, Worker worker2) {
            this.run = runnable;
            this.worker = worker2;
        }

        public void run() {
            if (!this.disposed) {
                try {
                    this.run.run();
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.worker.dispose();
                    throw ExceptionHelper.wrapOrThrow(th);
                }
            }
        }

        public void dispose() {
            this.disposed = true;
            this.worker.dispose();
        }

        public boolean isDisposed() {
            return this.disposed;
        }
    }

    /* renamed from: io.reactivex.Scheduler$Worker */
    public static abstract class Worker implements Disposable {

        /* renamed from: io.reactivex.Scheduler$Worker$PeriodicTask */
        final class PeriodicTask implements Runnable {
            long count;
            final Runnable decoratedRun;
            long lastNowNanoseconds;
            final long periodInNanoseconds;

            /* renamed from: sd */
            final SequentialDisposable f207sd;
            long startInNanoseconds;

            PeriodicTask(long j, Runnable runnable, long j2, SequentialDisposable sequentialDisposable, long j3) {
                this.decoratedRun = runnable;
                this.f207sd = sequentialDisposable;
                this.periodInNanoseconds = j3;
                this.lastNowNanoseconds = j2;
                this.startInNanoseconds = j;
            }

            public void run() {
                long j;
                this.decoratedRun.run();
                if (!this.f207sd.isDisposed()) {
                    long now = Worker.this.now(TimeUnit.NANOSECONDS);
                    if (Scheduler.CLOCK_DRIFT_TOLERANCE_NANOSECONDS + now < this.lastNowNanoseconds || now >= this.lastNowNanoseconds + this.periodInNanoseconds + Scheduler.CLOCK_DRIFT_TOLERANCE_NANOSECONDS) {
                        j = this.periodInNanoseconds + now;
                        long j2 = this.periodInNanoseconds;
                        long j3 = this.count + 1;
                        this.count = j3;
                        this.startInNanoseconds = j - (j2 * j3);
                    } else {
                        long j4 = this.startInNanoseconds;
                        long j5 = this.count + 1;
                        this.count = j5;
                        j = j4 + (j5 * this.periodInNanoseconds);
                    }
                    this.lastNowNanoseconds = now;
                    this.f207sd.replace(Worker.this.schedule(this, j - now, TimeUnit.NANOSECONDS));
                }
            }
        }

        public abstract Disposable schedule(Runnable runnable, long j, TimeUnit timeUnit);

        public Disposable schedule(Runnable runnable) {
            return schedule(runnable, 0, TimeUnit.NANOSECONDS);
        }

        public Disposable schedulePeriodically(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
            long j3 = j;
            TimeUnit timeUnit2 = timeUnit;
            SequentialDisposable sequentialDisposable = new SequentialDisposable();
            SequentialDisposable sequentialDisposable2 = new SequentialDisposable(sequentialDisposable);
            Runnable onSchedule = RxJavaPlugins.onSchedule(runnable);
            long nanos = timeUnit2.toNanos(j2);
            long now = now(TimeUnit.NANOSECONDS);
            SequentialDisposable sequentialDisposable3 = sequentialDisposable;
            PeriodicTask periodicTask = r0;
            PeriodicTask periodicTask2 = new PeriodicTask(now + timeUnit2.toNanos(j3), onSchedule, now, sequentialDisposable2, nanos);
            Disposable schedule = schedule(periodicTask, j3, timeUnit2);
            if (schedule == EmptyDisposable.INSTANCE) {
                return schedule;
            }
            sequentialDisposable3.replace(schedule);
            return sequentialDisposable2;
        }

        public long now(TimeUnit timeUnit) {
            return timeUnit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }
    }

    public abstract Worker createWorker();

    public void shutdown() {
    }

    public void start() {
    }

    public static long clockDriftTolerance() {
        return CLOCK_DRIFT_TOLERANCE_NANOSECONDS;
    }

    public long now(TimeUnit timeUnit) {
        return timeUnit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public Disposable scheduleDirect(Runnable runnable) {
        return scheduleDirect(runnable, 0, TimeUnit.NANOSECONDS);
    }

    public Disposable scheduleDirect(Runnable runnable, long j, TimeUnit timeUnit) {
        final Worker createWorker = createWorker();
        final Runnable onSchedule = RxJavaPlugins.onSchedule(runnable);
        createWorker.schedule(new Runnable() {
            public void run() {
                try {
                    onSchedule.run();
                } finally {
                    createWorker.dispose();
                }
            }
        }, j, timeUnit);
        return createWorker;
    }

    public Disposable schedulePeriodicallyDirect(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
        Worker createWorker = createWorker();
        PeriodicDirectTask periodicDirectTask = new PeriodicDirectTask(RxJavaPlugins.onSchedule(runnable), createWorker);
        Disposable schedulePeriodically = createWorker.schedulePeriodically(periodicDirectTask, j, j2, timeUnit);
        return schedulePeriodically == EmptyDisposable.INSTANCE ? schedulePeriodically : periodicDirectTask;
    }

    @Experimental
    public <S extends Scheduler & Disposable> S when(Function<Flowable<Flowable<Completable>>, Completable> function) {
        return new SchedulerWhen(function, this);
    }
}
