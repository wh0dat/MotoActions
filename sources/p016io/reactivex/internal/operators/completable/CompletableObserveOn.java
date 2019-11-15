package p016io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.Scheduler;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.completable.CompletableObserveOn */
public final class CompletableObserveOn extends Completable {
    final Scheduler scheduler;
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableObserveOn$ObserveOnCompletableObserver */
    static final class ObserveOnCompletableObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable, Runnable {
        private static final long serialVersionUID = 8571289934935992137L;
        final CompletableObserver actual;
        Throwable error;
        final Scheduler scheduler;

        ObserveOnCompletableObserver(CompletableObserver completableObserver, Scheduler scheduler2) {
            this.actual = completableObserver;
            this.scheduler = scheduler2;
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

        public void onError(Throwable th) {
            this.error = th;
            DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
        }

        public void onComplete() {
            DisposableHelper.replace(this, this.scheduler.scheduleDirect(this));
        }

        public void run() {
            Throwable th = this.error;
            if (th != null) {
                this.error = null;
                this.actual.onError(th);
                return;
            }
            this.actual.onComplete();
        }
    }

    public CompletableObserveOn(CompletableSource completableSource, Scheduler scheduler2) {
        this.source = completableSource;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        this.source.subscribe(new ObserveOnCompletableObserver(completableObserver, this.scheduler));
    }
}
