package p016io.reactivex.internal.schedulers;

import java.util.concurrent.TimeUnit;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.disposables.Disposables;

/* renamed from: io.reactivex.internal.schedulers.ImmediateThinScheduler */
public final class ImmediateThinScheduler extends Scheduler {
    static final Disposable DISPOSED = Disposables.empty();
    public static final Scheduler INSTANCE = new ImmediateThinScheduler();
    static final Worker WORKER = new ImmediateThinWorker();

    /* renamed from: io.reactivex.internal.schedulers.ImmediateThinScheduler$ImmediateThinWorker */
    static final class ImmediateThinWorker extends Worker {
        public void dispose() {
        }

        public boolean isDisposed() {
            return false;
        }

        ImmediateThinWorker() {
        }

        public Disposable schedule(Runnable runnable) {
            runnable.run();
            return ImmediateThinScheduler.DISPOSED;
        }

        public Disposable schedule(Runnable runnable, long j, TimeUnit timeUnit) {
            throw new UnsupportedOperationException("This scheduler doesn't support delayed execution");
        }

        public Disposable schedulePeriodically(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
            throw new UnsupportedOperationException("This scheduler doesn't support periodic execution");
        }
    }

    static {
        DISPOSED.dispose();
    }

    private ImmediateThinScheduler() {
    }

    public Disposable scheduleDirect(Runnable runnable) {
        runnable.run();
        return DISPOSED;
    }

    public Disposable scheduleDirect(Runnable runnable, long j, TimeUnit timeUnit) {
        throw new UnsupportedOperationException("This scheduler doesn't support delayed execution");
    }

    public Disposable schedulePeriodicallyDirect(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
        throw new UnsupportedOperationException("This scheduler doesn't support periodic execution");
    }

    public Worker createWorker() {
        return WORKER;
    }
}
