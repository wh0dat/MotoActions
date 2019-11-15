package p016io.reactivex.internal.operators.completable;

import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.Scheduler;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableSubscribeOn */
public final class CompletableSubscribeOn extends Completable {
    final Scheduler scheduler;
    final CompletableSource source;

    /* renamed from: io.reactivex.internal.operators.completable.CompletableSubscribeOn$SubscribeOnObserver */
    static final class SubscribeOnObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable, Runnable {
        private static final long serialVersionUID = 7000911171163930287L;
        final CompletableObserver actual;
        final CompletableSource source;
        final SequentialDisposable task = new SequentialDisposable();

        SubscribeOnObserver(CompletableObserver completableObserver, CompletableSource completableSource) {
            this.actual = completableObserver;
            this.source = completableSource;
        }

        public void run() {
            this.source.subscribe(this);
        }

        public void onSubscribe(Disposable disposable) {
            DisposableHelper.setOnce(this, disposable);
        }

        public void onError(Throwable th) {
            this.actual.onError(th);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void dispose() {
            DisposableHelper.dispose(this);
            this.task.dispose();
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }

    public CompletableSubscribeOn(CompletableSource completableSource, Scheduler scheduler2) {
        this.source = completableSource;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver completableObserver) {
        SubscribeOnObserver subscribeOnObserver = new SubscribeOnObserver(completableObserver, this.source);
        completableObserver.onSubscribe(subscribeOnObserver);
        subscribeOnObserver.task.replace(this.scheduler.scheduleDirect(subscribeOnObserver));
    }
}
