package p016io.reactivex.processors;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p016io.reactivex.Scheduler;
import p016io.reactivex.internal.functions.ObjectHelper;
import p016io.reactivex.internal.subscriptions.SubscriptionHelper;
import p016io.reactivex.internal.util.BackpressureHelper;
import p016io.reactivex.internal.util.NotificationLite;
import p016io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.processors.ReplayProcessor */
public final class ReplayProcessor<T> extends FlowableProcessor<T> {
    static final ReplaySubscription[] EMPTY = new ReplaySubscription[0];
    private static final Object[] EMPTY_ARRAY = new Object[0];
    static final ReplaySubscription[] TERMINATED = new ReplaySubscription[0];
    final ReplayBuffer<T> buffer;
    boolean done;
    final AtomicReference<ReplaySubscription<T>[]> subscribers = new AtomicReference<>(EMPTY);

    /* renamed from: io.reactivex.processors.ReplayProcessor$Node */
    static final class Node<T> extends AtomicReference<Node<T>> {
        private static final long serialVersionUID = 6404226426336033100L;
        final T value;

        Node(T t) {
            this.value = t;
        }
    }

    /* renamed from: io.reactivex.processors.ReplayProcessor$ReplayBuffer */
    interface ReplayBuffer<T> {
        void add(T t);

        void addFinal(Object obj);

        Object get();

        T getValue();

        T[] getValues(T[] tArr);

        void replay(ReplaySubscription<T> replaySubscription);

        int size();
    }

    /* renamed from: io.reactivex.processors.ReplayProcessor$ReplaySubscription */
    static final class ReplaySubscription<T> extends AtomicInteger implements Subscription {
        private static final long serialVersionUID = 466549804534799122L;
        final Subscriber<? super T> actual;
        volatile boolean cancelled;
        Object index;
        final AtomicLong requested = new AtomicLong();
        final ReplayProcessor<T> state;

        ReplaySubscription(Subscriber<? super T> subscriber, ReplayProcessor<T> replayProcessor) {
            this.actual = subscriber;
            this.state = replayProcessor;
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                this.state.buffer.replay(this);
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.state.remove(this);
            }
        }
    }

    /* renamed from: io.reactivex.processors.ReplayProcessor$SizeAndTimeBoundReplayBuffer */
    static final class SizeAndTimeBoundReplayBuffer<T> extends AtomicReference<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 1242561386470847675L;
        volatile boolean done;
        volatile TimedNode<Object> head;
        final long maxAge;
        final int maxSize;
        final Scheduler scheduler;
        int size;
        TimedNode<Object> tail;
        final TimeUnit unit;

        SizeAndTimeBoundReplayBuffer(int i, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            this.maxSize = ObjectHelper.verifyPositive(i, "maxSize");
            this.maxAge = ObjectHelper.verifyPositive(j, "maxAge");
            this.unit = (TimeUnit) ObjectHelper.requireNonNull(timeUnit, "unit is null");
            this.scheduler = (Scheduler) ObjectHelper.requireNonNull(scheduler2, "scheduler is null");
            TimedNode<Object> timedNode = new TimedNode<>(null, 0);
            this.tail = timedNode;
            this.head = timedNode;
        }

        /* access modifiers changed from: 0000 */
        public void trim() {
            if (this.size > this.maxSize) {
                this.size--;
                this.head = (TimedNode) this.head.get();
            }
            long now = this.scheduler.now(this.unit) - this.maxAge;
            TimedNode<Object> timedNode = this.head;
            while (true) {
                TimedNode<Object> timedNode2 = (TimedNode) timedNode.get();
                if (timedNode2 == null) {
                    this.head = timedNode;
                    return;
                } else if (timedNode2.time > now) {
                    this.head = timedNode;
                    return;
                } else {
                    timedNode = timedNode2;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void trimFinal() {
            long now = this.scheduler.now(this.unit) - this.maxAge;
            TimedNode<Object> timedNode = this.head;
            while (true) {
                TimedNode<Object> timedNode2 = (TimedNode) timedNode.get();
                if (timedNode2.get() == null) {
                    this.head = timedNode;
                    return;
                } else if (timedNode2.time > now) {
                    this.head = timedNode;
                    return;
                } else {
                    timedNode = timedNode2;
                }
            }
        }

        public void add(T t) {
            TimedNode<Object> timedNode = new TimedNode<>(t, this.scheduler.now(this.unit));
            TimedNode<Object> timedNode2 = this.tail;
            this.tail = timedNode;
            this.size++;
            timedNode2.set(timedNode);
            trim();
        }

        public void addFinal(Object obj) {
            lazySet(obj);
            TimedNode<Object> timedNode = new TimedNode<>(obj, LongCompanionObject.MAX_VALUE);
            TimedNode<Object> timedNode2 = this.tail;
            this.tail = timedNode;
            this.size++;
            timedNode2.set(timedNode);
            trimFinal();
            this.done = true;
        }

        public T getValue() {
            TimedNode<Object> timedNode = this.head;
            TimedNode<Object> timedNode2 = null;
            while (true) {
                TimedNode<Object> timedNode3 = (TimedNode) timedNode.get();
                if (timedNode3 == null) {
                    break;
                }
                timedNode2 = timedNode;
                timedNode = timedNode3;
            }
            T t = timedNode.value;
            if (t == null) {
                return null;
            }
            if (NotificationLite.isComplete(t) || NotificationLite.isError(t)) {
                return timedNode2.value;
            }
            return t;
        }

        public T[] getValues(T[] tArr) {
            TimedNode<Object> timedNode = this.head;
            int size2 = size();
            if (size2 != 0) {
                if (tArr.length < size2) {
                    tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size2);
                }
                for (int i = 0; i != size2; i++) {
                    timedNode = (TimedNode) timedNode.get();
                    tArr[i] = timedNode.value;
                }
                if (tArr.length > size2) {
                    tArr[size2] = null;
                }
            } else if (tArr.length != 0) {
                tArr[0] = null;
            }
            return tArr;
        }

        public void replay(ReplaySubscription<T> replaySubscription) {
            ReplaySubscription<T> replaySubscription2 = replaySubscription;
            if (replaySubscription.getAndIncrement() == 0) {
                Subscriber<? super T> subscriber = replaySubscription2.actual;
                TimedNode<Object> timedNode = (TimedNode) replaySubscription2.index;
                if (timedNode == null) {
                    timedNode = this.head;
                    if (!this.done) {
                        long now = this.scheduler.now(this.unit) - this.maxAge;
                        TimedNode<Object> timedNode2 = (TimedNode) timedNode.get();
                        while (timedNode2 != null && timedNode2.time <= now) {
                            TimedNode<Object> timedNode3 = timedNode2;
                            timedNode2 = (TimedNode) timedNode2.get();
                            timedNode = timedNode3;
                        }
                    }
                }
                int i = 1;
                do {
                    long j = replaySubscription2.requested.get();
                    long j2 = 0;
                    while (!replaySubscription2.cancelled) {
                        TimedNode<Object> timedNode4 = (TimedNode) timedNode.get();
                        if (timedNode4 != null) {
                            T t = timedNode4.value;
                            if (!this.done || timedNode4.get() != null) {
                                if (j == 0) {
                                    j = replaySubscription2.requested.get() + j2;
                                    if (j == 0) {
                                    }
                                }
                                subscriber.onNext(t);
                                j--;
                                j2--;
                                timedNode = timedNode4;
                            } else {
                                if (NotificationLite.isComplete(t)) {
                                    subscriber.onComplete();
                                } else {
                                    subscriber.onError(NotificationLite.getError(t));
                                }
                                replaySubscription2.index = null;
                                replaySubscription2.cancelled = true;
                                return;
                            }
                        }
                        if (!(j2 == 0 || replaySubscription2.requested.get() == LongCompanionObject.MAX_VALUE)) {
                            replaySubscription2.requested.addAndGet(j2);
                        }
                        replaySubscription2.index = timedNode;
                        i = replaySubscription2.addAndGet(-i);
                    }
                    replaySubscription2.index = null;
                    return;
                } while (i != 0);
            }
        }

        public int size() {
            TimedNode<Object> timedNode = this.head;
            int i = 0;
            while (i != Integer.MAX_VALUE) {
                TimedNode<Object> timedNode2 = (TimedNode) timedNode.get();
                if (timedNode2 == null) {
                    T t = timedNode.value;
                    return (NotificationLite.isComplete(t) || NotificationLite.isError(t)) ? i - 1 : i;
                }
                i++;
                timedNode = timedNode2;
            }
            return i;
        }
    }

    /* renamed from: io.reactivex.processors.ReplayProcessor$SizeBoundReplayBuffer */
    static final class SizeBoundReplayBuffer<T> extends AtomicReference<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 3027920763113911982L;
        volatile boolean done;
        volatile Node<Object> head;
        final int maxSize;
        int size;
        Node<Object> tail;

        SizeBoundReplayBuffer(int i) {
            this.maxSize = ObjectHelper.verifyPositive(i, "maxSize");
            Node<Object> node = new Node<>(null);
            this.tail = node;
            this.head = node;
        }

        /* access modifiers changed from: 0000 */
        public void trim() {
            if (this.size > this.maxSize) {
                this.size--;
                this.head = (Node) this.head.get();
            }
        }

        public void add(T t) {
            Node<Object> node = new Node<>(t);
            Node<Object> node2 = this.tail;
            this.tail = node;
            this.size++;
            node2.set(node);
            trim();
        }

        public void addFinal(Object obj) {
            lazySet(obj);
            Node<Object> node = new Node<>(obj);
            Node<Object> node2 = this.tail;
            this.tail = node;
            this.size++;
            node2.set(node);
            this.done = true;
        }

        public T getValue() {
            Node<Object> node = this.head;
            Node<Object> node2 = null;
            while (true) {
                Node<Object> node3 = (Node) node.get();
                if (node3 == null) {
                    break;
                }
                node2 = node;
                node = node3;
            }
            T t = node.value;
            if (t == null) {
                return null;
            }
            if (NotificationLite.isComplete(t) || NotificationLite.isError(t)) {
                return node2.value;
            }
            return t;
        }

        public T[] getValues(T[] tArr) {
            Node<Object> node = this.head;
            int size2 = size();
            if (size2 != 0) {
                if (tArr.length < size2) {
                    tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), size2);
                }
                for (int i = 0; i != size2; i++) {
                    node = (Node) node.get();
                    tArr[i] = node.value;
                }
                if (tArr.length > size2) {
                    tArr[size2] = null;
                }
            } else if (tArr.length != 0) {
                tArr[0] = null;
            }
            return tArr;
        }

        public void replay(ReplaySubscription<T> replaySubscription) {
            if (replaySubscription.getAndIncrement() == 0) {
                Subscriber<? super T> subscriber = replaySubscription.actual;
                Node<Object> node = (Node) replaySubscription.index;
                if (node == null) {
                    node = this.head;
                }
                int i = 1;
                do {
                    long j = replaySubscription.requested.get();
                    long j2 = 0;
                    while (!replaySubscription.cancelled) {
                        Node<Object> node2 = (Node) node.get();
                        if (node2 != null) {
                            T t = node2.value;
                            if (!this.done || node2.get() != null) {
                                if (j == 0) {
                                    j = replaySubscription.requested.get() + j2;
                                    if (j == 0) {
                                    }
                                }
                                subscriber.onNext(t);
                                j--;
                                j2--;
                                node = node2;
                            } else {
                                if (NotificationLite.isComplete(t)) {
                                    subscriber.onComplete();
                                } else {
                                    subscriber.onError(NotificationLite.getError(t));
                                }
                                replaySubscription.index = null;
                                replaySubscription.cancelled = true;
                                return;
                            }
                        }
                        if (!(j2 == 0 || replaySubscription.requested.get() == LongCompanionObject.MAX_VALUE)) {
                            replaySubscription.requested.addAndGet(j2);
                        }
                        replaySubscription.index = node;
                        i = replaySubscription.addAndGet(-i);
                    }
                    replaySubscription.index = null;
                    return;
                } while (i != 0);
            }
        }

        public int size() {
            Node<Object> node = this.head;
            int i = 0;
            while (i != Integer.MAX_VALUE) {
                Node<Object> node2 = (Node) node.get();
                if (node2 == null) {
                    T t = node.value;
                    return (NotificationLite.isComplete(t) || NotificationLite.isError(t)) ? i - 1 : i;
                }
                i++;
                node = node2;
            }
            return i;
        }
    }

    /* renamed from: io.reactivex.processors.ReplayProcessor$TimedNode */
    static final class TimedNode<T> extends AtomicReference<TimedNode<T>> {
        private static final long serialVersionUID = 6404226426336033100L;
        final long time;
        final T value;

        TimedNode(T t, long j) {
            this.value = t;
            this.time = j;
        }
    }

    /* renamed from: io.reactivex.processors.ReplayProcessor$UnboundedReplayBuffer */
    static final class UnboundedReplayBuffer<T> extends AtomicReference<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = -4457200895834877300L;
        final List<Object> buffer;
        volatile boolean done;
        volatile int size;

        UnboundedReplayBuffer(int i) {
            this.buffer = new ArrayList(ObjectHelper.verifyPositive(i, "capacityHint"));
        }

        public void add(T t) {
            this.buffer.add(t);
            this.size++;
        }

        public void addFinal(Object obj) {
            lazySet(obj);
            this.buffer.add(obj);
            this.size++;
            this.done = true;
        }

        public T getValue() {
            int i = this.size;
            if (i == 0) {
                return null;
            }
            List<Object> list = this.buffer;
            T t = list.get(i - 1);
            if (!NotificationLite.isComplete(t) && !NotificationLite.isError(t)) {
                return t;
            }
            if (i == 1) {
                return null;
            }
            return list.get(i - 2);
        }

        public T[] getValues(T[] tArr) {
            int i = this.size;
            if (i == 0) {
                if (tArr.length != 0) {
                    tArr[0] = null;
                }
                return tArr;
            }
            List<Object> list = this.buffer;
            Object obj = list.get(i - 1);
            if (NotificationLite.isComplete(obj) || NotificationLite.isError(obj)) {
                i--;
                if (i == 0) {
                    if (tArr.length != 0) {
                        tArr[0] = null;
                    }
                    return tArr;
                }
            }
            if (tArr.length < i) {
                tArr = (Object[]) Array.newInstance(tArr.getClass().getComponentType(), i);
            }
            for (int i2 = 0; i2 < i; i2++) {
                tArr[i2] = list.get(i2);
            }
            if (tArr.length > i) {
                tArr[i] = null;
            }
            return tArr;
        }

        public void replay(ReplaySubscription<T> replaySubscription) {
            ReplaySubscription<T> replaySubscription2 = replaySubscription;
            if (replaySubscription.getAndIncrement() == 0) {
                List<Object> list = this.buffer;
                Subscriber<? super T> subscriber = replaySubscription2.actual;
                Integer num = (Integer) replaySubscription2.index;
                int i = 0;
                if (num != null) {
                    i = num.intValue();
                } else {
                    replaySubscription2.index = Integer.valueOf(0);
                }
                int i2 = 1;
                while (!replaySubscription2.cancelled) {
                    int i3 = this.size;
                    long j = replaySubscription2.requested.get();
                    long j2 = 0;
                    while (i3 != i) {
                        if (replaySubscription2.cancelled) {
                            replaySubscription2.index = null;
                            return;
                        }
                        Object obj = list.get(i);
                        if (this.done) {
                            int i4 = i + 1;
                            if (i4 == i3) {
                                i3 = this.size;
                                if (i4 == i3) {
                                    if (NotificationLite.isComplete(obj)) {
                                        subscriber.onComplete();
                                    } else {
                                        subscriber.onError(NotificationLite.getError(obj));
                                    }
                                    replaySubscription2.index = null;
                                    replaySubscription2.cancelled = true;
                                    return;
                                }
                            }
                        }
                        if (j == 0) {
                            j = replaySubscription2.requested.get() + j2;
                            if (j == 0) {
                                break;
                            }
                        }
                        subscriber.onNext(obj);
                        j--;
                        j2--;
                        i++;
                    }
                    if (!(j2 == 0 || replaySubscription2.requested.get() == LongCompanionObject.MAX_VALUE)) {
                        j = replaySubscription2.requested.addAndGet(j2);
                    }
                    if (i == this.size || j == 0) {
                        replaySubscription2.index = Integer.valueOf(i);
                        i2 = replaySubscription2.addAndGet(-i2);
                        if (i2 == 0) {
                            return;
                        }
                    }
                }
                replaySubscription2.index = null;
            }
        }

        public int size() {
            int i = this.size;
            if (i == 0) {
                return 0;
            }
            int i2 = i - 1;
            Object obj = this.buffer.get(i2);
            return (NotificationLite.isComplete(obj) || NotificationLite.isError(obj)) ? i2 : i;
        }
    }

    public static <T> ReplayProcessor<T> create() {
        return new ReplayProcessor<>(new UnboundedReplayBuffer(16));
    }

    public static <T> ReplayProcessor<T> create(int i) {
        return new ReplayProcessor<>(new UnboundedReplayBuffer(i));
    }

    public static <T> ReplayProcessor<T> createWithSize(int i) {
        return new ReplayProcessor<>(new SizeBoundReplayBuffer(i));
    }

    static <T> ReplayProcessor<T> createUnbounded() {
        return new ReplayProcessor<>(new SizeBoundReplayBuffer(Integer.MAX_VALUE));
    }

    public static <T> ReplayProcessor<T> createWithTime(long j, TimeUnit timeUnit, Scheduler scheduler) {
        SizeAndTimeBoundReplayBuffer sizeAndTimeBoundReplayBuffer = new SizeAndTimeBoundReplayBuffer(Integer.MAX_VALUE, j, timeUnit, scheduler);
        return new ReplayProcessor<>(sizeAndTimeBoundReplayBuffer);
    }

    public static <T> ReplayProcessor<T> createWithTimeAndSize(long j, TimeUnit timeUnit, Scheduler scheduler, int i) {
        SizeAndTimeBoundReplayBuffer sizeAndTimeBoundReplayBuffer = new SizeAndTimeBoundReplayBuffer(i, j, timeUnit, scheduler);
        return new ReplayProcessor<>(sizeAndTimeBoundReplayBuffer);
    }

    ReplayProcessor(ReplayBuffer<T> replayBuffer) {
        this.buffer = replayBuffer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        ReplaySubscription replaySubscription = new ReplaySubscription(subscriber, this);
        subscriber.onSubscribe(replaySubscription);
        if (!add(replaySubscription) || !replaySubscription.cancelled) {
            this.buffer.replay(replaySubscription);
        } else {
            remove(replaySubscription);
        }
    }

    public void onSubscribe(Subscription subscription) {
        if (this.done) {
            subscription.cancel();
        } else {
            subscription.request(LongCompanionObject.MAX_VALUE);
        }
    }

    public void onNext(T t) {
        if (t == null) {
            onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
        } else if (!this.done) {
            ReplayBuffer<T> replayBuffer = this.buffer;
            replayBuffer.add(t);
            for (ReplaySubscription replay : (ReplaySubscription[]) this.subscribers.get()) {
                replayBuffer.replay(replay);
            }
        }
    }

    public void onError(Throwable th) {
        if (th == null) {
            th = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        }
        if (this.done) {
            RxJavaPlugins.onError(th);
            return;
        }
        this.done = true;
        Object error = NotificationLite.error(th);
        ReplayBuffer<T> replayBuffer = this.buffer;
        replayBuffer.addFinal(error);
        for (ReplaySubscription replay : (ReplaySubscription[]) this.subscribers.getAndSet(TERMINATED)) {
            replayBuffer.replay(replay);
        }
    }

    public void onComplete() {
        if (!this.done) {
            this.done = true;
            Object complete = NotificationLite.complete();
            ReplayBuffer<T> replayBuffer = this.buffer;
            replayBuffer.addFinal(complete);
            for (ReplaySubscription replay : (ReplaySubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                replayBuffer.replay(replay);
            }
        }
    }

    public boolean hasSubscribers() {
        return ((ReplaySubscription[]) this.subscribers.get()).length != 0;
    }

    /* access modifiers changed from: 0000 */
    public int subscriberCount() {
        return ((ReplaySubscription[]) this.subscribers.get()).length;
    }

    public Throwable getThrowable() {
        Object obj = this.buffer.get();
        if (NotificationLite.isError(obj)) {
            return NotificationLite.getError(obj);
        }
        return null;
    }

    public T getValue() {
        return this.buffer.getValue();
    }

    public Object[] getValues() {
        Object[] values = getValues(EMPTY_ARRAY);
        return values == EMPTY_ARRAY ? new Object[0] : values;
    }

    public T[] getValues(T[] tArr) {
        return this.buffer.getValues(tArr);
    }

    public boolean hasComplete() {
        return NotificationLite.isComplete(this.buffer.get());
    }

    public boolean hasThrowable() {
        return NotificationLite.isError(this.buffer.get());
    }

    public boolean hasValue() {
        return this.buffer.size() != 0;
    }

    /* access modifiers changed from: 0000 */
    public int size() {
        return this.buffer.size();
    }

    /* access modifiers changed from: 0000 */
    public boolean add(ReplaySubscription<T> replaySubscription) {
        ReplaySubscription[] replaySubscriptionArr;
        ReplaySubscription[] replaySubscriptionArr2;
        do {
            replaySubscriptionArr = (ReplaySubscription[]) this.subscribers.get();
            if (replaySubscriptionArr == TERMINATED) {
                return false;
            }
            int length = replaySubscriptionArr.length;
            replaySubscriptionArr2 = new ReplaySubscription[(length + 1)];
            System.arraycopy(replaySubscriptionArr, 0, replaySubscriptionArr2, 0, length);
            replaySubscriptionArr2[length] = replaySubscription;
        } while (!this.subscribers.compareAndSet(replaySubscriptionArr, replaySubscriptionArr2));
        return true;
    }

    /* access modifiers changed from: 0000 */
    public void remove(ReplaySubscription<T> replaySubscription) {
        ReplaySubscription<T>[] replaySubscriptionArr;
        ReplaySubscription[] replaySubscriptionArr2;
        do {
            replaySubscriptionArr = (ReplaySubscription[]) this.subscribers.get();
            if (replaySubscriptionArr != TERMINATED && replaySubscriptionArr != EMPTY) {
                int length = replaySubscriptionArr.length;
                int i = -1;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    } else if (replaySubscriptionArr[i2] == replaySubscription) {
                        i = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i >= 0) {
                    if (length == 1) {
                        replaySubscriptionArr2 = EMPTY;
                    } else {
                        ReplaySubscription[] replaySubscriptionArr3 = new ReplaySubscription[(length - 1)];
                        System.arraycopy(replaySubscriptionArr, 0, replaySubscriptionArr3, 0, i);
                        System.arraycopy(replaySubscriptionArr, i + 1, replaySubscriptionArr3, i, (length - i) - 1);
                        replaySubscriptionArr2 = replaySubscriptionArr3;
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        } while (!this.subscribers.compareAndSet(replaySubscriptionArr, replaySubscriptionArr2));
    }
}
