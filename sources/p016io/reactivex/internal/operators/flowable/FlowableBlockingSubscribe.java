package p016io.reactivex.internal.operators.flowable;

import java.util.concurrent.LinkedBlockingQueue;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import p016io.reactivex.functions.Action;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.internal.functions.Functions;
import p016io.reactivex.internal.subscribers.BlockingSubscriber;
import p016io.reactivex.internal.subscribers.LambdaSubscriber;
import p016io.reactivex.internal.util.BlockingHelper;
import p016io.reactivex.internal.util.BlockingIgnoringReceiver;
import p016io.reactivex.internal.util.ExceptionHelper;
import p016io.reactivex.internal.util.NotificationLite;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableBlockingSubscribe */
public final class FlowableBlockingSubscribe {
    private FlowableBlockingSubscribe() {
        throw new IllegalStateException("No instances!");
    }

    public static <T> void subscribe(Publisher<? extends T> publisher, Subscriber<? super T> subscriber) {
        Object poll;
        LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
        BlockingSubscriber blockingSubscriber = new BlockingSubscriber(linkedBlockingQueue);
        publisher.subscribe(blockingSubscriber);
        do {
            try {
                if (!blockingSubscriber.isCancelled()) {
                    poll = linkedBlockingQueue.poll();
                    if (poll == null) {
                        if (!blockingSubscriber.isCancelled()) {
                            poll = linkedBlockingQueue.take();
                        } else {
                            return;
                        }
                    }
                    if (!blockingSubscriber.isCancelled()) {
                        if (publisher == BlockingSubscriber.TERMINATED) {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } catch (InterruptedException e) {
                blockingSubscriber.cancel();
                subscriber.onError(e);
                return;
            }
        } while (!NotificationLite.acceptFull(poll, subscriber));
    }

    public static <T> void subscribe(Publisher<? extends T> publisher) {
        BlockingIgnoringReceiver blockingIgnoringReceiver = new BlockingIgnoringReceiver();
        LambdaSubscriber lambdaSubscriber = new LambdaSubscriber(Functions.emptyConsumer(), blockingIgnoringReceiver, blockingIgnoringReceiver, Functions.REQUEST_MAX);
        publisher.subscribe(lambdaSubscriber);
        BlockingHelper.awaitForComplete(blockingIgnoringReceiver, lambdaSubscriber);
        Throwable th = blockingIgnoringReceiver.error;
        if (th != null) {
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    public static <T> void subscribe(Publisher<? extends T> publisher, Consumer<? super T> consumer, Consumer<? super Throwable> consumer2, Action action) {
        subscribe(publisher, new LambdaSubscriber(consumer, consumer2, action, Functions.REQUEST_MAX));
    }
}
