package p016io.reactivex.internal.operators.single;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.CompositeDisposable;
import p016io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.operators.single.SingleTimeout */
public final class SingleTimeout<T> extends Single<T> {
    final SingleSource<? extends T> other;
    final Scheduler scheduler;
    final SingleSource<T> source;
    final long timeout;
    final TimeUnit unit;

    public SingleTimeout(SingleSource<T> singleSource, long j, TimeUnit timeUnit, Scheduler scheduler2, SingleSource<? extends T> singleSource2) {
        this.source = singleSource;
        this.timeout = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.other = singleSource2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super T> singleObserver) {
        final CompositeDisposable compositeDisposable = new CompositeDisposable();
        singleObserver.onSubscribe(compositeDisposable);
        final AtomicBoolean atomicBoolean = new AtomicBoolean();
        compositeDisposable.add(this.scheduler.scheduleDirect(new Runnable() {
            public void run() {
                if (!atomicBoolean.compareAndSet(false, true)) {
                    return;
                }
                if (SingleTimeout.this.other != null) {
                    compositeDisposable.clear();
                    SingleTimeout.this.other.subscribe(new SingleObserver<T>() {
                        public void onError(Throwable th) {
                            compositeDisposable.dispose();
                            singleObserver.onError(th);
                        }

                        public void onSubscribe(Disposable disposable) {
                            compositeDisposable.add(disposable);
                        }

                        public void onSuccess(T t) {
                            compositeDisposable.dispose();
                            singleObserver.onSuccess(t);
                        }
                    });
                    return;
                }
                compositeDisposable.dispose();
                singleObserver.onError(new TimeoutException());
            }
        }, this.timeout, this.unit));
        this.source.subscribe(new SingleObserver<T>() {
            public void onError(Throwable th) {
                if (atomicBoolean.compareAndSet(false, true)) {
                    compositeDisposable.dispose();
                    singleObserver.onError(th);
                }
            }

            public void onSubscribe(Disposable disposable) {
                compositeDisposable.add(disposable);
            }

            public void onSuccess(T t) {
                if (atomicBoolean.compareAndSet(false, true)) {
                    compositeDisposable.dispose();
                    singleObserver.onSuccess(t);
                }
            }
        });
    }
}
