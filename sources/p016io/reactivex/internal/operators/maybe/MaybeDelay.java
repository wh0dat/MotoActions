package p016io.reactivex.internal.operators.maybe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.MaybeObserver;
import p016io.reactivex.MaybeSource;
import p016io.reactivex.Scheduler;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeDelay */
public final class MaybeDelay<T> extends AbstractMaybeWithUpstream<T, T> {
    final long delay;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.maybe.MaybeDelay$DelayMaybeObserver */
    static final class DelayMaybeObserver<T> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable, Runnable {
        private static final long serialVersionUID = 5566860102500855068L;
        final MaybeObserver<? super T> actual;
        final long delay;
        Throwable error;
        final Scheduler scheduler;
        final TimeUnit unit;
        T value;

        DelayMaybeObserver(MaybeObserver<? super T> maybeObserver, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            this.actual = maybeObserver;
            this.delay = j;
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public void run() {
            Throwable th = this.error;
            if (th != null) {
                this.actual.onError(th);
                return;
            }
            T t = this.value;
            if (t != null) {
                this.actual.onSuccess(t);
            } else {
                this.actual.onComplete();
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.setOnce(this, disposable)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.value = t;
            schedule();
        }

        public void onError(Throwable th) {
            this.error = th;
            schedule();
        }

        public void onComplete() {
            schedule();
        }

        /* access modifiers changed from: 0000 */
        public void schedule() {
            DisposableHelper.replace(this, this.scheduler.scheduleDirect(this, this.delay, this.unit));
        }
    }

    public MaybeDelay(MaybeSource<T> maybeSource, long j, TimeUnit timeUnit, Scheduler scheduler2) {
        super(maybeSource);
        this.delay = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> maybeObserver) {
        MaybeSource maybeSource = this.source;
        DelayMaybeObserver delayMaybeObserver = new DelayMaybeObserver(maybeObserver, this.delay, this.unit, this.scheduler);
        maybeSource.subscribe(delayMaybeObserver);
    }
}
