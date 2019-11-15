package p016io.reactivex.android.schedulers;

import android.os.Handler;
import android.os.Message;
import java.util.concurrent.TimeUnit;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.disposables.Disposables;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.android.schedulers.HandlerScheduler */
final class HandlerScheduler extends Scheduler {
    private final Handler handler;

    /* renamed from: io.reactivex.android.schedulers.HandlerScheduler$HandlerWorker */
    private static final class HandlerWorker extends Worker {
        private volatile boolean disposed;
        private final Handler handler;

        HandlerWorker(Handler handler2) {
            this.handler = handler2;
        }

        public Disposable schedule(Runnable runnable, long j, TimeUnit timeUnit) {
            if (runnable == null) {
                throw new NullPointerException("run == null");
            } else if (timeUnit == null) {
                throw new NullPointerException("unit == null");
            } else if (this.disposed) {
                return Disposables.disposed();
            } else {
                ScheduledRunnable scheduledRunnable = new ScheduledRunnable(this.handler, RxJavaPlugins.onSchedule(runnable));
                Message obtain = Message.obtain(this.handler, scheduledRunnable);
                obtain.obj = this;
                this.handler.sendMessageDelayed(obtain, Math.max(0, timeUnit.toMillis(j)));
                if (!this.disposed) {
                    return scheduledRunnable;
                }
                this.handler.removeCallbacks(scheduledRunnable);
                return Disposables.disposed();
            }
        }

        public void dispose() {
            this.disposed = true;
            this.handler.removeCallbacksAndMessages(this);
        }

        public boolean isDisposed() {
            return this.disposed;
        }
    }

    /* renamed from: io.reactivex.android.schedulers.HandlerScheduler$ScheduledRunnable */
    private static final class ScheduledRunnable implements Runnable, Disposable {
        private final Runnable delegate;
        private volatile boolean disposed;
        private final Handler handler;

        ScheduledRunnable(Handler handler2, Runnable runnable) {
            this.handler = handler2;
            this.delegate = runnable;
        }

        public void run() {
            try {
                this.delegate.run();
            } catch (Throwable th) {
                IllegalStateException illegalStateException = new IllegalStateException("Fatal Exception thrown on Scheduler.", th);
                RxJavaPlugins.onError(illegalStateException);
                Thread currentThread = Thread.currentThread();
                currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, illegalStateException);
            }
        }

        public void dispose() {
            this.disposed = true;
            this.handler.removeCallbacks(this);
        }

        public boolean isDisposed() {
            return this.disposed;
        }
    }

    HandlerScheduler(Handler handler2) {
        this.handler = handler2;
    }

    public Disposable scheduleDirect(Runnable runnable, long j, TimeUnit timeUnit) {
        if (runnable == null) {
            throw new NullPointerException("run == null");
        } else if (timeUnit == null) {
            throw new NullPointerException("unit == null");
        } else {
            ScheduledRunnable scheduledRunnable = new ScheduledRunnable(this.handler, RxJavaPlugins.onSchedule(runnable));
            this.handler.postDelayed(scheduledRunnable, Math.max(0, timeUnit.toMillis(j)));
            return scheduledRunnable;
        }
    }

    public Worker createWorker() {
        return new HandlerWorker(this.handler);
    }
}
