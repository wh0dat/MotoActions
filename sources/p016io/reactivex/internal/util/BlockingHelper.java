package p016io.reactivex.internal.util;

import java.util.concurrent.CountDownLatch;
import p016io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.internal.util.BlockingHelper */
public final class BlockingHelper {
    private BlockingHelper() {
        throw new IllegalStateException("No instances!");
    }

    public static void awaitForComplete(CountDownLatch countDownLatch, Disposable disposable) {
        if (countDownLatch.getCount() != 0) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                disposable.dispose();
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for subscription to complete.", e);
            }
        }
    }
}
