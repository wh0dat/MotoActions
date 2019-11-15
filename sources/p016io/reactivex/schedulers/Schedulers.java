package p016io.reactivex.schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import p016io.reactivex.Scheduler;
import p016io.reactivex.internal.schedulers.ComputationScheduler;
import p016io.reactivex.internal.schedulers.ExecutorScheduler;
import p016io.reactivex.internal.schedulers.IoScheduler;
import p016io.reactivex.internal.schedulers.NewThreadScheduler;
import p016io.reactivex.internal.schedulers.SchedulerPoolFactory;
import p016io.reactivex.internal.schedulers.SingleScheduler;
import p016io.reactivex.internal.schedulers.TrampolineScheduler;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.schedulers.Schedulers */
public final class Schedulers {
    static final Scheduler COMPUTATION = RxJavaPlugins.initComputationScheduler(new Callable<Scheduler>() {
        public Scheduler call() throws Exception {
            return ComputationHolder.DEFAULT;
        }
    });

    /* renamed from: IO */
    static final Scheduler f566IO = RxJavaPlugins.initIoScheduler(new Callable<Scheduler>() {
        public Scheduler call() throws Exception {
            return IoHolder.DEFAULT;
        }
    });
    static final Scheduler NEW_THREAD = RxJavaPlugins.initNewThreadScheduler(new Callable<Scheduler>() {
        public Scheduler call() throws Exception {
            return NewThreadHolder.DEFAULT;
        }
    });
    static final Scheduler SINGLE = RxJavaPlugins.initSingleScheduler(new Callable<Scheduler>() {
        public Scheduler call() throws Exception {
            return SingleHolder.DEFAULT;
        }
    });
    static final Scheduler TRAMPOLINE = TrampolineScheduler.instance();

    /* renamed from: io.reactivex.schedulers.Schedulers$ComputationHolder */
    static final class ComputationHolder {
        static final Scheduler DEFAULT = new ComputationScheduler();

        ComputationHolder() {
        }
    }

    /* renamed from: io.reactivex.schedulers.Schedulers$IoHolder */
    static final class IoHolder {
        static final Scheduler DEFAULT = new IoScheduler();

        IoHolder() {
        }
    }

    /* renamed from: io.reactivex.schedulers.Schedulers$NewThreadHolder */
    static final class NewThreadHolder {
        static final Scheduler DEFAULT = NewThreadScheduler.instance();

        NewThreadHolder() {
        }
    }

    /* renamed from: io.reactivex.schedulers.Schedulers$SingleHolder */
    static final class SingleHolder {
        static final Scheduler DEFAULT = new SingleScheduler();

        SingleHolder() {
        }
    }

    private Schedulers() {
        throw new IllegalStateException("No instances!");
    }

    public static Scheduler computation() {
        return RxJavaPlugins.onComputationScheduler(COMPUTATION);
    }

    /* renamed from: io */
    public static Scheduler m90io() {
        return RxJavaPlugins.onIoScheduler(f566IO);
    }

    public static Scheduler trampoline() {
        return TRAMPOLINE;
    }

    public static Scheduler newThread() {
        return RxJavaPlugins.onNewThreadScheduler(NEW_THREAD);
    }

    public static Scheduler single() {
        return RxJavaPlugins.onSingleScheduler(SINGLE);
    }

    public static Scheduler from(Executor executor) {
        return new ExecutorScheduler(executor);
    }

    public static void shutdown() {
        computation().shutdown();
        m90io().shutdown();
        newThread().shutdown();
        single().shutdown();
        trampoline().shutdown();
        SchedulerPoolFactory.shutdown();
    }

    public static void start() {
        computation().start();
        m90io().start();
        newThread().start();
        single().start();
        trampoline().start();
        SchedulerPoolFactory.start();
    }
}
