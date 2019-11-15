package p016io.reactivex.internal.schedulers;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReferenceArray;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.internal.disposables.DisposableContainer;

/* renamed from: io.reactivex.internal.schedulers.ScheduledRunnable */
public final class ScheduledRunnable extends AtomicReferenceArray<Object> implements Runnable, Callable<Object>, Disposable {
    static final Object DISPOSED = new Object();
    static final Object DONE = new Object();
    static final int FUTURE_INDEX = 1;
    static final int PARENT_INDEX = 0;
    private static final long serialVersionUID = -6120223772001106981L;
    final Runnable actual;

    public ScheduledRunnable(Runnable runnable, DisposableContainer disposableContainer) {
        super(2);
        this.actual = runnable;
        lazySet(0, disposableContainer);
    }

    public Object call() {
        run();
        return null;
    }

    public void run() {
        Object obj;
        Object obj2;
        try {
            this.actual.run();
        } catch (Throwable th) {
            Object obj3 = get(0);
            if (!(obj3 == DISPOSED || obj3 == null || !compareAndSet(0, obj3, DONE))) {
                ((DisposableContainer) obj3).delete(this);
            }
            do {
                obj2 = get(1);
                if (obj2 == DISPOSED) {
                    break;
                }
            } while (!compareAndSet(1, obj2, DONE));
            throw th;
        }
        Object obj4 = get(0);
        if (!(obj4 == DISPOSED || obj4 == null || !compareAndSet(0, obj4, DONE))) {
            ((DisposableContainer) obj4).delete(this);
        }
        do {
            obj = get(1);
            if (obj == DISPOSED) {
                return;
            }
        } while (!compareAndSet(1, obj, DONE));
    }

    public void setFuture(Future<?> future) {
        Object obj;
        do {
            obj = get(1);
            if (obj != DONE) {
                if (obj == DISPOSED) {
                    future.cancel(true);
                    return;
                }
            } else {
                return;
            }
        } while (!compareAndSet(1, obj, future));
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void dispose() {
        /*
            r3 = this;
        L_0x0000:
            r0 = 1
            java.lang.Object r1 = r3.get(r0)
            java.lang.Object r2 = DONE
            if (r1 == r2) goto L_0x001d
            java.lang.Object r2 = DISPOSED
            if (r1 != r2) goto L_0x000e
            goto L_0x001d
        L_0x000e:
            java.lang.Object r2 = DISPOSED
            boolean r2 = r3.compareAndSet(r0, r1, r2)
            if (r2 == 0) goto L_0x0000
            if (r1 == 0) goto L_0x001d
            java.util.concurrent.Future r1 = (java.util.concurrent.Future) r1
            r1.cancel(r0)
        L_0x001d:
            r0 = 0
            java.lang.Object r1 = r3.get(r0)
            java.lang.Object r2 = DONE
            if (r1 == r2) goto L_0x003b
            java.lang.Object r2 = DISPOSED
            if (r1 == r2) goto L_0x003b
            if (r1 != 0) goto L_0x002d
            goto L_0x003b
        L_0x002d:
            java.lang.Object r2 = DISPOSED
            boolean r0 = r3.compareAndSet(r0, r1, r2)
            if (r0 == 0) goto L_0x001d
            io.reactivex.internal.disposables.DisposableContainer r1 = (p016io.reactivex.internal.disposables.DisposableContainer) r1
            r1.delete(r3)
            return
        L_0x003b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.schedulers.ScheduledRunnable.dispose():void");
    }

    public boolean isDisposed() {
        Object obj = get(1);
        if (obj == DISPOSED || obj == DONE) {
            return true;
        }
        return false;
    }
}
