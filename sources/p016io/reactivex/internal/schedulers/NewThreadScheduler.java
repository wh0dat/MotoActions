package p016io.reactivex.internal.schedulers;

import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;

/* renamed from: io.reactivex.internal.schedulers.NewThreadScheduler */
public final class NewThreadScheduler extends Scheduler {
    private static final NewThreadScheduler INSTANCE = new NewThreadScheduler();
    private static final String KEY_NEWTHREAD_PRIORITY = "rx2.newthread-priority";
    private static final RxThreadFactory THREAD_FACTORY = new RxThreadFactory(THREAD_NAME_PREFIX, Math.max(1, Math.min(10, Integer.getInteger(KEY_NEWTHREAD_PRIORITY, 5).intValue())));
    private static final String THREAD_NAME_PREFIX = "RxNewThreadScheduler";

    public static NewThreadScheduler instance() {
        return INSTANCE;
    }

    private NewThreadScheduler() {
    }

    public Worker createWorker() {
        return new NewThreadWorker(THREAD_FACTORY);
    }
}
