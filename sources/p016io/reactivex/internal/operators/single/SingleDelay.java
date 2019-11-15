package p016io.reactivex.internal.operators.single;

import java.util.concurrent.TimeUnit;
import p016io.reactivex.Scheduler;
import p016io.reactivex.Single;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.SingleSource;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.single.SingleDelay */
public final class SingleDelay<T> extends Single<T> {
    final Scheduler scheduler;
    final SingleSource<? extends T> source;
    final long time;
    final TimeUnit unit;

    public SingleDelay(SingleSource<? extends T> singleSource, long j, TimeUnit timeUnit, Scheduler scheduler2) {
        this.source = singleSource;
        this.time = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(final SingleObserver<? super T> singleObserver) {
        final SequentialDisposable sequentialDisposable = new SequentialDisposable();
        singleObserver.onSubscribe(sequentialDisposable);
        this.source.subscribe(new SingleObserver<T>() {
            public void onSubscribe(Disposable disposable) {
                sequentialDisposable.replace(disposable);
            }

            public void onSuccess(final T t) {
                sequentialDisposable.replace(SingleDelay.this.scheduler.scheduleDirect(new Runnable() {
                    public void run() {
                        singleObserver.onSuccess(t);
                    }
                }, SingleDelay.this.time, SingleDelay.this.unit));
            }

            public void onError(final Throwable th) {
                sequentialDisposable.replace(SingleDelay.this.scheduler.scheduleDirect(new Runnable() {
                    public void run() {
                        singleObserver.onError(th);
                    }
                }, 0, SingleDelay.this.unit));
            }
        });
    }
}
