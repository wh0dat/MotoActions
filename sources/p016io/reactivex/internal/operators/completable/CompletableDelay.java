package p016io.reactivex.internal.operators.completable;

import java.util.concurrent.TimeUnit;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.Scheduler;
import p016io.reactivex.disposables.CompositeDisposable;
import p016io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.operators.completable.CompletableDelay */
public final class CompletableDelay extends Completable {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final CompletableSource source;
    final TimeUnit unit;

    public CompletableDelay(CompletableSource completableSource, long j, TimeUnit timeUnit, Scheduler scheduler2, boolean z) {
        this.source = completableSource;
        this.delay = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.delayError = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final CompletableObserver completableObserver) {
        final CompositeDisposable compositeDisposable = new CompositeDisposable();
        this.source.subscribe(new CompletableObserver() {
            public void onComplete() {
                compositeDisposable.add(CompletableDelay.this.scheduler.scheduleDirect(new Runnable() {
                    public void run() {
                        completableObserver.onComplete();
                    }
                }, CompletableDelay.this.delay, CompletableDelay.this.unit));
            }

            public void onError(final Throwable th) {
                compositeDisposable.add(CompletableDelay.this.scheduler.scheduleDirect(new Runnable() {
                    public void run() {
                        completableObserver.onError(th);
                    }
                }, CompletableDelay.this.delayError ? CompletableDelay.this.delay : 0, CompletableDelay.this.unit));
            }

            public void onSubscribe(Disposable disposable) {
                compositeDisposable.add(disposable);
                completableObserver.onSubscribe(compositeDisposable);
            }
        });
    }
}
