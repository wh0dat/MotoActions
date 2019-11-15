package p016io.reactivex.internal.schedulers;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.Flowable;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Scheduler.Worker;
import p016io.reactivex.annotations.Experimental;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.disposables.Disposables;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Function;
import p016io.reactivex.processors.FlowableProcessor;
import p016io.reactivex.processors.UnicastProcessor;

@Experimental
/* renamed from: io.reactivex.internal.schedulers.SchedulerWhen */
public class SchedulerWhen extends Scheduler implements Disposable {
    static final Disposable DISPOSED = Disposables.disposed();
    static final Disposable SUBSCRIBED = new Disposable() {
        public void dispose() {
        }

        public boolean isDisposed() {
            return false;
        }
    };
    private final Scheduler actualScheduler;
    private Disposable disposable;
    private final FlowableProcessor<Flowable<Completable>> workerProcessor = UnicastProcessor.create().toSerialized();

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$DelayedAction */
    static class DelayedAction extends ScheduledAction {
        private final Runnable action;
        private final long delayTime;
        private final TimeUnit unit;

        DelayedAction(Runnable runnable, long j, TimeUnit timeUnit) {
            this.action = runnable;
            this.delayTime = j;
            this.unit = timeUnit;
        }

        /* access modifiers changed from: protected */
        public Disposable callActual(Worker worker, CompletableObserver completableObserver) {
            return worker.schedule(new OnCompletedAction(this.action, completableObserver), this.delayTime, this.unit);
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$ImmediateAction */
    static class ImmediateAction extends ScheduledAction {
        private final Runnable action;

        ImmediateAction(Runnable runnable) {
            this.action = runnable;
        }

        /* access modifiers changed from: protected */
        public Disposable callActual(Worker worker, CompletableObserver completableObserver) {
            return worker.schedule(new OnCompletedAction(this.action, completableObserver));
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$OnCompletedAction */
    static class OnCompletedAction implements Runnable {
        private Runnable action;
        private CompletableObserver actionCompletable;

        OnCompletedAction(Runnable runnable, CompletableObserver completableObserver) {
            this.action = runnable;
            this.actionCompletable = completableObserver;
        }

        public void run() {
            try {
                this.action.run();
            } finally {
                this.actionCompletable.onComplete();
            }
        }
    }

    /* renamed from: io.reactivex.internal.schedulers.SchedulerWhen$ScheduledAction */
    static abstract class ScheduledAction extends AtomicReference<Disposable> implements Disposable {
        /* access modifiers changed from: protected */
        public abstract Disposable callActual(Worker worker, CompletableObserver completableObserver);

        ScheduledAction() {
            super(SchedulerWhen.SUBSCRIBED);
        }

        /* access modifiers changed from: 0000 */
        public void call(Worker worker, CompletableObserver completableObserver) {
            Disposable disposable = (Disposable) get();
            if (disposable != SchedulerWhen.DISPOSED && disposable == SchedulerWhen.SUBSCRIBED) {
                Disposable callActual = callActual(worker, completableObserver);
                if (!compareAndSet(SchedulerWhen.SUBSCRIBED, callActual)) {
                    callActual.dispose();
                }
            }
        }

        public boolean isDisposed() {
            return ((Disposable) get()).isDisposed();
        }

        public void dispose() {
            Disposable disposable;
            Disposable disposable2 = SchedulerWhen.DISPOSED;
            do {
                disposable = (Disposable) get();
                if (disposable == SchedulerWhen.DISPOSED) {
                    return;
                }
            } while (!compareAndSet(disposable, disposable2));
            if (disposable != SchedulerWhen.SUBSCRIBED) {
                disposable.dispose();
            }
        }
    }

    public SchedulerWhen(Function<Flowable<Flowable<Completable>>, Completable> function, Scheduler scheduler) {
        this.actualScheduler = scheduler;
        try {
            this.disposable = ((Completable) function.apply(this.workerProcessor)).subscribe();
        } catch (Throwable th) {
            Exceptions.propagate(th);
        }
    }

    public void dispose() {
        this.disposable.dispose();
    }

    public boolean isDisposed() {
        return this.disposable.isDisposed();
    }

    public Worker createWorker() {
        final Worker createWorker = this.actualScheduler.createWorker();
        final FlowableProcessor serialized = UnicastProcessor.create().toSerialized();
        Flowable map = serialized.map(new Function<ScheduledAction, Completable>() {
            public Completable apply(final ScheduledAction scheduledAction) {
                return new Completable() {
                    /* access modifiers changed from: protected */
                    public void subscribeActual(CompletableObserver completableObserver) {
                        completableObserver.onSubscribe(scheduledAction);
                        scheduledAction.call(createWorker, completableObserver);
                    }
                };
            }
        });
        C08032 r3 = new Worker() {
            private final AtomicBoolean unsubscribed = new AtomicBoolean();

            public void dispose() {
                if (this.unsubscribed.compareAndSet(false, true)) {
                    createWorker.dispose();
                    serialized.onComplete();
                }
            }

            public boolean isDisposed() {
                return this.unsubscribed.get();
            }

            public Disposable schedule(Runnable runnable, long j, TimeUnit timeUnit) {
                DelayedAction delayedAction = new DelayedAction(runnable, j, timeUnit);
                serialized.onNext(delayedAction);
                return delayedAction;
            }

            public Disposable schedule(Runnable runnable) {
                ImmediateAction immediateAction = new ImmediateAction(runnable);
                serialized.onNext(immediateAction);
                return immediateAction;
            }
        };
        this.workerProcessor.onNext(map);
        return r3;
    }
}
