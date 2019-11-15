package p016io.reactivex.internal.schedulers;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.disposables.Disposables;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.schedulers.TrampolineScheduler */
public final class TrampolineScheduler extends Scheduler {
    private static final TrampolineScheduler INSTANCE = new TrampolineScheduler();

    /* renamed from: io.reactivex.internal.schedulers.TrampolineScheduler$SleepingRunnable */
    static final class SleepingRunnable implements Runnable {
        private final long execTime;
        private final Runnable run;
        private final TrampolineWorker worker;

        SleepingRunnable(Runnable runnable, TrampolineWorker trampolineWorker, long j) {
            this.run = runnable;
            this.worker = trampolineWorker;
            this.execTime = j;
        }

        public void run() {
            if (!this.worker.disposed) {
                long now = this.worker.now(TimeUnit.MILLISECONDS);
                if (this.execTime > now) {
                    long j = this.execTime - now;
                    if (j > 0) {
                        try {
                            Thread.sleep(j);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            RxJavaPlugins.onError(e);
                            return;
                        }
                    }
                }
                if (!this.worker.disposed) {
                    this.run.run();
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.TrampolineScheduler$TimedRunnable */
    static final class TimedRunnable implements Comparable<TimedRunnable> {
        final int count;
        volatile boolean disposed;
        final long execTime;
        final Runnable run;

        TimedRunnable(Runnable runnable, Long l, int i) {
            this.run = runnable;
            this.execTime = l.longValue();
            this.count = i;
        }

        public int compareTo(TimedRunnable timedRunnable) {
            int compare = ObjectHelper.compare(this.execTime, timedRunnable.execTime);
            return compare == 0 ? ObjectHelper.compare(this.count, timedRunnable.count) : compare;
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.TrampolineScheduler$TrampolineWorker */
    static final class TrampolineWorker extends Worker implements Disposable {
        final AtomicInteger counter = new AtomicInteger();
        volatile boolean disposed;
        final PriorityBlockingQueue<TimedRunnable> queue = new PriorityBlockingQueue<>();
        private final AtomicInteger wip = new AtomicInteger();

        TrampolineWorker() {
        }

        public Disposable schedule(Runnable runnable) {
            return enqueue(runnable, now(TimeUnit.MILLISECONDS));
        }

        public Disposable schedule(Runnable runnable, long j, TimeUnit timeUnit) {
            long now = now(TimeUnit.MILLISECONDS) + timeUnit.toMillis(j);
            return enqueue(new SleepingRunnable(runnable, this, now), now);
        }

        /* access modifiers changed from: 0000 */
        public Disposable enqueue(Runnable runnable, long j) {
            if (this.disposed) {
                return EmptyDisposable.INSTANCE;
            }
            final TimedRunnable timedRunnable = new TimedRunnable(runnable, Long.valueOf(j), this.counter.incrementAndGet());
            this.queue.add(timedRunnable);
            if (this.wip.getAndIncrement() != 0) {
                return Disposables.fromRunnable(new Runnable() {
                    public void run() {
                        timedRunnable.disposed = true;
                        TrampolineWorker.this.queue.remove(timedRunnable);
                    }
                });
            }
            int i = 1;
            while (true) {
                TimedRunnable timedRunnable2 = (TimedRunnable) this.queue.poll();
                if (timedRunnable2 == null) {
                    i = this.wip.addAndGet(-i);
                    if (i == 0) {
                        return EmptyDisposable.INSTANCE;
                    }
                } else if (!timedRunnable2.disposed) {
                    timedRunnable2.run.run();
                }
            }
        }

        public void dispose() {
            this.disposed = true;
        }

        public boolean isDisposed() {
            return this.disposed;
        }
    }

    public static TrampolineScheduler instance() {
        return INSTANCE;
    }

    public Worker createWorker() {
        return new TrampolineWorker();
    }

    TrampolineScheduler() {
    }

    public Disposable scheduleDirect(Runnable runnable) {
        runnable.run();
        return EmptyDisposable.INSTANCE;
    }

    public Disposable scheduleDirect(Runnable runnable, long j, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(j);
            runnable.run();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            RxJavaPlugins.onError(e);
        }
        return EmptyDisposable.INSTANCE;
    }
}
