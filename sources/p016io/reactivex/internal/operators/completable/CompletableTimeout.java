package p016io.reactivex.internal.operators.completable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import p016io.reactivex.Completable;
import p016io.reactivex.CompletableObserver;
import p016io.reactivex.CompletableSource;
import p016io.reactivex.Scheduler;
import p016io.reactivex.disposables.CompositeDisposable;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.completable.CompletableTimeout */
public final class CompletableTimeout extends Completable {
    final CompletableSource other;
    final Scheduler scheduler;
    final CompletableSource source;
    final long timeout;
    final TimeUnit unit;

    public CompletableTimeout(CompletableSource completableSource, long j, TimeUnit timeUnit, Scheduler scheduler2, CompletableSource completableSource2) {
        this.source = completableSource;
        this.timeout = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.other = completableSource2;
    }

    public void subscribeActual(final CompletableObserver completableObserver) {
        final CompositeDisposable compositeDisposable = new CompositeDisposable();
        completableObserver.onSubscribe(compositeDisposable);
        final AtomicBoolean atomicBoolean = new AtomicBoolean();
        compositeDisposable.add(this.scheduler.scheduleDirect(new Runnable() {
            public void run() {
                if (atomicBoolean.compareAndSet(false, true)) {
                    compositeDisposable.clear();
                    if (CompletableTimeout.this.other == null) {
                        completableObserver.onError(new TimeoutException());
                    } else {
                        CompletableTimeout.this.other.subscribe(new CompletableObserver() {
                            public void onSubscribe(Disposable disposable) {
                                compositeDisposable.add(disposable);
                            }

                            public void onError(Throwable th) {
                                compositeDisposable.dispose();
                                completableObserver.onError(th);
                            }

                            public void onComplete() {
                                compositeDisposable.dispose();
                                completableObserver.onComplete();
                            }
                        });
                    }
                }
            }
        }, this.timeout, this.unit));
        this.source.subscribe(new CompletableObserver() {
            public void onSubscribe(Disposable disposable) {
                compositeDisposable.add(disposable);
            }

            public void onError(Throwable th) {
                if (atomicBoolean.compareAndSet(false, true)) {
                    compositeDisposable.dispose();
                    completableObserver.onError(th);
                    return;
                }
                RxJavaPlugins.onError(th);
            }

            public void onComplete() {
                if (atomicBoolean.compareAndSet(false, true)) {
                    compositeDisposable.dispose();
                    completableObserver.onComplete();
                }
            }
        });
    }
}
