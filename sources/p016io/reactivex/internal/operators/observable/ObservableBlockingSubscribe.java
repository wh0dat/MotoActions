package p016io.reactivex.internal.operators.observable;

import java.util.concurrent.LinkedBlockingQueue;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.functions.Action;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.internal.functions.Functions;
import p016io.reactivex.internal.observers.BlockingObserver;
import p016io.reactivex.internal.observers.LambdaObserver;
import p016io.reactivex.internal.util.BlockingHelper;
import p016io.reactivex.internal.util.BlockingIgnoringReceiver;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.internal.util.NotificationLite;

/* renamed from: io.reactivex.internal.operators.observable.ObservableBlockingSubscribe */
public final class ObservableBlockingSubscribe {
    private ObservableBlockingSubscribe() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> void subscribe(ObservableSource<? extends T> observableSource, Observer<? super T> observer) {
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        BlockingObserver blockingObserver = new BlockingObserver(linkedBlockingQueue);
        observer.onSubscribe(blockingObserver);
        observableSource.subscribe(blockingObserver);
        while (!blockingObserver.isDisposed()) {
            Object poll = linkedBlockingQueue.poll();
            if (poll == null) {
                try {
                    poll = linkedBlockingQueue.take();
                } catch (InterruptedException e) {
                    blockingObserver.dispose();
                    observer.onError(e);
                    return;
                }
            }
            if (!blockingObserver.isDisposed() && observableSource != BlockingObserver.TERMINATED) {
                if (NotificationLite.acceptFull(poll, observer)) {
                    break;
                }
            } else {
                break;
            }
        }
    }

    public static <T> void subscribe(ObservableSource<? extends T> observableSource) {
        BlockingIgnoringReceiver blockingIgnoringReceiver = new BlockingIgnoringReceiver();
        LambdaObserver lambdaObserver = new LambdaObserver(Functions.emptyConsumer(), blockingIgnoringReceiver, blockingIgnoringReceiver, Functions.emptyConsumer());
        observableSource.subscribe(lambdaObserver);
        BlockingHelper.awaitForComplete(blockingIgnoringReceiver, lambdaObserver);
        Throwable th = blockingIgnoringReceiver.error;
        if (th != null) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    public static <T> void subscribe(ObservableSource<? extends T> observableSource, Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action) {
        subscribe(observableSource, new LambdaObserver(consumer, consumer2, action, Functions.emptyConsumer()));
    }
}
