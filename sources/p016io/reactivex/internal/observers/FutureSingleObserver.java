package p016io.reactivex.internal.observers;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.SingleObserver;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.observers.FutureSingleObserver */
public final class FutureSingleObserver<T> extends CountDownLatch implements SingleObserver<T>, Future<T>, Disposable {
    Throwable error;

    /* renamed from: s */
    final AtomicReference<Disposable> f218s = new AtomicReference<>();
    T value;

    public void dispose() {
    }

    public FutureSingleObserver() {
        super(1);
    }

    public boolean cancel(boolean z) {
        Disposable disposable;
        do {
            disposable = (Disposable) this.f218s.get();
            if (disposable == this || disposable == DisposableHelper.DISPOSED) {
                return false;
            }
        } while (!this.f218s.compareAndSet(disposable, DisposableHelper.DISPOSED));
        if (disposable != null) {
            disposable.dispose();
        }
        countDown();
        return true;
    }

    public boolean isCancelled() {
        return DisposableHelper.isDisposed((Disposable) this.f218s.get());
    }

    public boolean isDone() {
        return getCount() == 0;
    }

    public T get() throws InterruptedException, ExecutionException {
        if (getCount() != 0) {
            await();
        }
        if (isCancelled()) {
            throw new CancellationException();
        }
        Throwable th = this.error;
        if (th == null) {
            return this.value;
        }
        throw new ExecutionException(th);
    }

    public T get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        if (getCount() != 0 && !await(j, timeUnit)) {
            throw new TimeoutException();
        } else if (isCancelled()) {
            throw new CancellationException();
        } else {
            Throwable th = this.error;
            if (th == null) {
                return this.value;
            }
            throw new ExecutionException(th);
        }
    }

    public void onSubscribe(Disposable disposable) {
        DisposableHelper.setOnce(this.f218s, disposable);
    }

    public void onSuccess(T t) {
        Disposable disposable = (Disposable) this.f218s.get();
        if (disposable != DisposableHelper.DISPOSED) {
            this.value = t;
            this.f218s.compareAndSet(disposable, this);
            countDown();
        }
    }

    public void onError(Throwable th) {
        Disposable disposable;
        do {
            disposable = (Disposable) this.f218s.get();
            if (disposable == DisposableHelper.DISPOSED) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.error = th;
        } while (!this.f218s.compareAndSet(disposable, this));
        countDown();
    }

    public boolean isDisposed() {
        return isDone();
    }
}
