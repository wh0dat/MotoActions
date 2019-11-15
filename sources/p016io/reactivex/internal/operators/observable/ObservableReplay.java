package p016io.reactivex.internal.operators.observable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import p016io.reactivex.Observable;
import p016io.reactivex.ObservableSource;
import p016io.reactivex.Observer;
import p016io.reactivex.Scheduler;
import p016io.reactivex.disposables.Disposable;
import p016io.reactivex.exceptions.Exceptions;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.functions.Function;
import p016io.reactivex.internal.disposables.DisposableHelper;
import p016io.reactivex.internal.disposables.EmptyDisposable;
import p016io.reactivex.internal.fuseable.HasUpstreamObservableSource;
import p016io.reactivex.internal.util.NotificationLite;
import p016io.reactivex.observables.ConnectableObservable;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.schedulers.Timed;

/* renamed from: io.reactivex.internal.operators.observable.ObservableReplay */
public final class ObservableReplay<T> extends ConnectableObservable<T> implements HasUpstreamObservableSource<T> {
    static final BufferSupplier DEFAULT_UNBOUNDED_FACTORY = new BufferSupplier() {
        public ReplayBuffer call() {
            return new UnboundedReplayBuffer(16);
        }
    };
    final BufferSupplier<T> bufferFactory;
    final AtomicReference<ReplayObserver<T>> current;
    final ObservableSource<T> onSubscribe;
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableReplay$BoundedReplayBuffer */
    static abstract class BoundedReplayBuffer<T> extends AtomicReference<Node> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 2346567790059478686L;
        int size;
        Node tail;

        /* access modifiers changed from: 0000 */
        public Object enterTransform(Object obj) {
            return obj;
        }

        /* access modifiers changed from: 0000 */
        public Object leaveTransform(Object obj) {
            return obj;
        }

        /* access modifiers changed from: 0000 */
        public abstract void truncate();

        /* access modifiers changed from: 0000 */
        public void truncateFinal() {
        }

        BoundedReplayBuffer() {
            Node node = new Node(null);
            this.tail = node;
            set(node);
        }

        /* access modifiers changed from: 0000 */
        public final void addLast(Node node) {
            this.tail.set(node);
            this.tail = node;
            this.size++;
        }

        /* access modifiers changed from: 0000 */
        public final void removeFirst() {
            Node node = (Node) ((Node) get()).get();
            this.size--;
            setFirst(node);
        }

        /* access modifiers changed from: 0000 */
        public final void removeSome(int i) {
            Node node = (Node) get();
            while (i > 0) {
                node = (Node) node.get();
                i--;
                this.size--;
            }
            setFirst(node);
        }

        /* access modifiers changed from: 0000 */
        public final void setFirst(Node node) {
            set(node);
        }

        public final void next(T t) {
            addLast(new Node(enterTransform(NotificationLite.next(t))));
            truncate();
        }

        public final void error(Throwable th) {
            addLast(new Node(enterTransform(NotificationLite.error(th))));
            truncateFinal();
        }

        public final void complete() {
            addLast(new Node(enterTransform(NotificationLite.complete())));
            truncateFinal();
        }

        public final void replay(InnerDisposable<T> innerDisposable) {
            if (innerDisposable.getAndIncrement() == 0) {
                int i = 1;
                do {
                    Node node = (Node) innerDisposable.index();
                    if (node == null) {
                        node = (Node) get();
                        innerDisposable.index = node;
                    }
                    while (!innerDisposable.isDisposed()) {
                        Node node2 = (Node) node.get();
                        if (node2 == null) {
                            innerDisposable.index = node;
                            i = innerDisposable.addAndGet(-i);
                        } else if (NotificationLite.accept(leaveTransform(node2.value), innerDisposable.child)) {
                            innerDisposable.index = null;
                            return;
                        } else {
                            node = node2;
                        }
                    }
                    return;
                } while (i != 0);
            }
        }

        /* access modifiers changed from: 0000 */
        public final void collect(Collection<? super T> collection) {
            Node node = (Node) get();
            while (true) {
                node = (Node) node.get();
                if (node != null) {
                    Object leaveTransform = leaveTransform(node.value);
                    if (!NotificationLite.isComplete(leaveTransform) && !NotificationLite.isError(leaveTransform)) {
                        collection.add(NotificationLite.getValue(leaveTransform));
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean hasError() {
            return this.tail.value != null && NotificationLite.isError(leaveTransform(this.tail.value));
        }

        /* access modifiers changed from: 0000 */
        public boolean hasCompleted() {
            return this.tail.value != null && NotificationLite.isComplete(leaveTransform(this.tail.value));
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableReplay$BufferSupplier */
    interface BufferSupplier<T> {
        ReplayBuffer<T> call();
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableReplay$InnerDisposable */
    static final class InnerDisposable<T> extends AtomicInteger implements Disposable {
        private static final long serialVersionUID = 2728361546769921047L;
        volatile boolean cancelled;
        final Observer<? super T> child;
        Object index;
        final ReplayObserver<T> parent;

        InnerDisposable(ReplayObserver<T> replayObserver, Observer<? super T> observer) {
            this.parent = replayObserver;
            this.child = observer;
        }

        public boolean isDisposed() {
            return this.cancelled;
        }

        public void dispose() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.parent.remove(this);
            }
        }

        /* access modifiers changed from: 0000 */
        public <U> U index() {
            return this.index;
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableReplay$Node */
    static final class Node extends AtomicReference<Node> {
        private static final long serialVersionUID = 245354315435971818L;
        final Object value;

        Node(Object obj) {
            this.value = obj;
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableReplay$ReplayBuffer */
    interface ReplayBuffer<T> {
        void complete();

        void error(Throwable th);

        void next(T t);

        void replay(InnerDisposable<T> innerDisposable);
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableReplay$ReplayObserver */
    static final class ReplayObserver<T> implements Observer<T>, Disposable {
        static final InnerDisposable[] EMPTY = new InnerDisposable[0];
        static final InnerDisposable[] TERMINATED = new InnerDisposable[0];
        final ReplayBuffer<T> buffer;
        boolean done;
        final AtomicReference<InnerDisposable[]> observers = new AtomicReference<>(EMPTY);
        final AtomicBoolean shouldConnect = new AtomicBoolean();
        volatile Disposable subscription;

        ReplayObserver(ReplayBuffer<T> replayBuffer) {
            this.buffer = replayBuffer;
        }

        public boolean isDisposed() {
            return this.observers.get() == TERMINATED;
        }

        public void dispose() {
            this.observers.set(TERMINATED);
            this.subscription.dispose();
        }

        /* access modifiers changed from: 0000 */
        public boolean add(InnerDisposable<T> innerDisposable) {
            InnerDisposable[] innerDisposableArr;
            InnerDisposable[] innerDisposableArr2;
            do {
                innerDisposableArr = (InnerDisposable[]) this.observers.get();
                if (innerDisposableArr == TERMINATED) {
                    return false;
                }
                int length = innerDisposableArr.length;
                innerDisposableArr2 = new InnerDisposable[(length + 1)];
                System.arraycopy(innerDisposableArr, 0, innerDisposableArr2, 0, length);
                innerDisposableArr2[length] = innerDisposable;
            } while (!this.observers.compareAndSet(innerDisposableArr, innerDisposableArr2));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void remove(InnerDisposable<T> innerDisposable) {
            InnerDisposable[] innerDisposableArr;
            InnerDisposable[] innerDisposableArr2;
            do {
                innerDisposableArr = (InnerDisposable[]) this.observers.get();
                int length = innerDisposableArr.length;
                if (length != 0) {
                    int i = -1;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (innerDisposableArr[i2].equals(innerDisposable)) {
                            i = i2;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (i >= 0) {
                        if (length == 1) {
                            innerDisposableArr2 = EMPTY;
                        } else {
                            InnerDisposable[] innerDisposableArr3 = new InnerDisposable[(length - 1)];
                            System.arraycopy(innerDisposableArr, 0, innerDisposableArr3, 0, i);
                            System.arraycopy(innerDisposableArr, i + 1, innerDisposableArr3, i, (length - i) - 1);
                            innerDisposableArr2 = innerDisposableArr3;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } while (!this.observers.compareAndSet(innerDisposableArr, innerDisposableArr2));
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.subscription, disposable)) {
                this.subscription = disposable;
                replay();
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                this.buffer.next(t);
                replay();
            }
        }

        public void onError(Throwable th) {
            if (!this.done) {
                this.done = true;
                this.buffer.error(th);
                replayFinal();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.buffer.complete();
                replayFinal();
            }
        }

        /* access modifiers changed from: 0000 */
        public void replay() {
            for (InnerDisposable replay : (InnerDisposable[]) this.observers.get()) {
                this.buffer.replay(replay);
            }
        }

        /* access modifiers changed from: 0000 */
        public void replayFinal() {
            for (InnerDisposable replay : (InnerDisposable[]) this.observers.getAndSet(TERMINATED)) {
                this.buffer.replay(replay);
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableReplay$SizeAndTimeBoundReplayBuffer */
    static final class SizeAndTimeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = 3457957419649567404L;
        final int limit;
        final long maxAge;
        final Scheduler scheduler;
        final TimeUnit unit;

        SizeAndTimeBoundReplayBuffer(int i, long j, TimeUnit timeUnit, Scheduler scheduler2) {
            this.scheduler = scheduler2;
            this.limit = i;
            this.maxAge = j;
            this.unit = timeUnit;
        }

        /* access modifiers changed from: 0000 */
        public Object enterTransform(Object obj) {
            return new Timed(obj, this.scheduler.now(this.unit), this.unit);
        }

        /* access modifiers changed from: 0000 */
        public Object leaveTransform(Object obj) {
            return ((Timed) obj).value();
        }

        /* access modifiers changed from: 0000 */
        public void truncate() {
            Node node;
            long now = this.scheduler.now(this.unit) - this.maxAge;
            Node node2 = (Node) get();
            Node node3 = (Node) node2.get();
            int i = 0;
            while (true) {
                Node node4 = node3;
                node = node2;
                node2 = node4;
                if (node2 != null) {
                    if (this.size <= this.limit) {
                        if (((Timed) node2.value).time() > now) {
                            break;
                        }
                        i++;
                        this.size--;
                        node3 = (Node) node2.get();
                    } else {
                        i++;
                        this.size--;
                        node3 = (Node) node2.get();
                    }
                } else {
                    break;
                }
            }
            if (i != 0) {
                setFirst(node);
            }
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Removed duplicated region for block: B:14:? A[RETURN, SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:9:0x003e  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void truncateFinal() {
            /*
                r10 = this;
                io.reactivex.Scheduler r0 = r10.scheduler
                java.util.concurrent.TimeUnit r1 = r10.unit
                long r0 = r0.now(r1)
                long r2 = r10.maxAge
                long r0 = r0 - r2
                java.lang.Object r2 = r10.get()
                io.reactivex.internal.operators.observable.ObservableReplay$Node r2 = (p016io.reactivex.internal.operators.observable.ObservableReplay.Node) r2
                java.lang.Object r3 = r2.get()
                io.reactivex.internal.operators.observable.ObservableReplay$Node r3 = (p016io.reactivex.internal.operators.observable.ObservableReplay.Node) r3
                r4 = 0
            L_0x0018:
                r9 = r3
                r3 = r2
                r2 = r9
                if (r2 == 0) goto L_0x003c
                int r5 = r10.size
                r6 = 1
                if (r5 <= r6) goto L_0x003c
                java.lang.Object r5 = r2.value
                io.reactivex.schedulers.Timed r5 = (p016io.reactivex.schedulers.Timed) r5
                long r7 = r5.time()
                int r5 = (r7 > r0 ? 1 : (r7 == r0 ? 0 : -1))
                if (r5 > 0) goto L_0x003c
                int r4 = r4 + 1
                int r3 = r10.size
                int r3 = r3 - r6
                r10.size = r3
                java.lang.Object r3 = r2.get()
                io.reactivex.internal.operators.observable.ObservableReplay$Node r3 = (p016io.reactivex.internal.operators.observable.ObservableReplay.Node) r3
                goto L_0x0018
            L_0x003c:
                if (r4 == 0) goto L_0x0041
                r10.setFirst(r3)
            L_0x0041:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.operators.observable.ObservableReplay.SizeAndTimeBoundReplayBuffer.truncateFinal():void");
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableReplay$SizeBoundReplayBuffer */
    static final class SizeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = -5898283885385201806L;
        final int limit;

        SizeBoundReplayBuffer(int i) {
            this.limit = i;
        }

        /* access modifiers changed from: 0000 */
        public void truncate() {
            if (this.size > this.limit) {
                removeFirst();
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableReplay$UnboundedReplayBuffer */
    static final class UnboundedReplayBuffer<T> extends ArrayList<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 7063189396499112664L;
        volatile int size;

        UnboundedReplayBuffer(int i) {
            super(i);
        }

        public void next(T t) {
            add(NotificationLite.next(t));
            this.size++;
        }

        public void error(Throwable th) {
            add(NotificationLite.error(th));
            this.size++;
        }

        public void complete() {
            add(NotificationLite.complete());
            this.size++;
        }

        public void replay(InnerDisposable<T> innerDisposable) {
            if (innerDisposable.getAndIncrement() == 0) {
                Observer<? super T> observer = innerDisposable.child;
                int i = 1;
                while (!innerDisposable.isDisposed()) {
                    int i2 = this.size;
                    Integer num = (Integer) innerDisposable.index();
                    int intValue = num != null ? num.intValue() : 0;
                    while (intValue < i2) {
                        if (!NotificationLite.accept(get(intValue), observer) && !innerDisposable.isDisposed()) {
                            intValue++;
                        } else {
                            return;
                        }
                    }
                    innerDisposable.index = Integer.valueOf(intValue);
                    i = innerDisposable.addAndGet(-i);
                    if (i == 0) {
                        return;
                    }
                }
            }
        }
    }

    public static <U, R> Observable<R> multicastSelector(final Callable<? extends ConnectableObservable<U>> callable, final Function<? super Observable<U>, ? extends ObservableSource<R>> function) {
        return RxJavaPlugins.onAssembly((Observable<T>) new Observable<R>() {
            /* access modifiers changed from: protected */
            public void subscribeActual(Observer<? super R> observer) {
                try {
                    ConnectableObservable connectableObservable = (ConnectableObservable) callable.call();
                    ObservableSource observableSource = (ObservableSource) function.apply(connectableObservable);
                    final ObserverResourceWrapper observerResourceWrapper = new ObserverResourceWrapper(observer);
                    observableSource.subscribe(observerResourceWrapper);
                    connectableObservable.connect(new Consumer<Disposable>() {
                        public void accept(Disposable disposable) {
                            observerResourceWrapper.setResource(disposable);
                        }
                    });
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    EmptyDisposable.error(th, observer);
                }
            }
        });
    }

    public static <T> ConnectableObservable<T> observeOn(final ConnectableObservable<T> connectableObservable, Scheduler scheduler) {
        final Observable observeOn = connectableObservable.observeOn(scheduler);
        return RxJavaPlugins.onAssembly((ConnectableObservable<T>) new ConnectableObservable<T>() {
            public void connect(Consumer<? super Disposable> consumer) {
                connectableObservable.connect(consumer);
            }

            /* access modifiers changed from: protected */
            public void subscribeActual(Observer<? super T> observer) {
                observeOn.subscribe(observer);
            }
        });
    }

    public static <T> ConnectableObservable<T> createFrom(ObservableSource<? extends T> observableSource) {
        return create(observableSource, DEFAULT_UNBOUNDED_FACTORY);
    }

    public static <T> ConnectableObservable<T> create(ObservableSource<T> observableSource, final int i) {
        if (i == Integer.MAX_VALUE) {
            return createFrom(observableSource);
        }
        return create(observableSource, (BufferSupplier<T>) new BufferSupplier<T>() {
            public ReplayBuffer<T> call() {
                return new SizeBoundReplayBuffer(i);
            }
        });
    }

    public static <T> ConnectableObservable<T> create(ObservableSource<T> observableSource, long j, TimeUnit timeUnit, Scheduler scheduler) {
        return create(observableSource, j, timeUnit, scheduler, Integer.MAX_VALUE);
    }

    public static <T> ConnectableObservable<T> create(ObservableSource<T> observableSource, long j, TimeUnit timeUnit, Scheduler scheduler, int i) {
        final int i2 = i;
        final long j2 = j;
        final TimeUnit timeUnit2 = timeUnit;
        final Scheduler scheduler2 = scheduler;
        C07735 r0 = new BufferSupplier<T>() {
            public ReplayBuffer<T> call() {
                SizeAndTimeBoundReplayBuffer sizeAndTimeBoundReplayBuffer = new SizeAndTimeBoundReplayBuffer(i2, j2, timeUnit2, scheduler2);
                return sizeAndTimeBoundReplayBuffer;
            }
        };
        return create(observableSource, (BufferSupplier<T>) r0);
    }

    static <T> ConnectableObservable<T> create(ObservableSource<T> observableSource, final BufferSupplier<T> bufferSupplier) {
        final AtomicReference atomicReference = new AtomicReference();
        return RxJavaPlugins.onAssembly((ConnectableObservable<T>) new ObservableReplay<T>(new ObservableSource<T>() {
            /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void subscribe(p016io.reactivex.Observer<? super T> r4) {
                /*
                    r3 = this;
                L_0x0000:
                    java.util.concurrent.atomic.AtomicReference r0 = r0
                    java.lang.Object r0 = r0.get()
                    io.reactivex.internal.operators.observable.ObservableReplay$ReplayObserver r0 = (p016io.reactivex.internal.operators.observable.ObservableReplay.ReplayObserver) r0
                    if (r0 != 0) goto L_0x0020
                    io.reactivex.internal.operators.observable.ObservableReplay$BufferSupplier r0 = r4
                    io.reactivex.internal.operators.observable.ObservableReplay$ReplayBuffer r0 = r0.call()
                    io.reactivex.internal.operators.observable.ObservableReplay$ReplayObserver r1 = new io.reactivex.internal.operators.observable.ObservableReplay$ReplayObserver
                    r1.<init>(r0)
                    java.util.concurrent.atomic.AtomicReference r0 = r0
                    r2 = 0
                    boolean r0 = r0.compareAndSet(r2, r1)
                    if (r0 != 0) goto L_0x001f
                    goto L_0x0000
                L_0x001f:
                    r0 = r1
                L_0x0020:
                    io.reactivex.internal.operators.observable.ObservableReplay$InnerDisposable r3 = new io.reactivex.internal.operators.observable.ObservableReplay$InnerDisposable
                    r3.<init>(r0, r4)
                    r4.onSubscribe(r3)
                    r0.add(r3)
                    boolean r4 = r3.isDisposed()
                    if (r4 == 0) goto L_0x0035
                    r0.remove(r3)
                    return
                L_0x0035:
                    io.reactivex.internal.operators.observable.ObservableReplay$ReplayBuffer<T> r4 = r0.buffer
                    r4.replay(r3)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.operators.observable.ObservableReplay.C07746.subscribe(io.reactivex.Observer):void");
            }
        }, observableSource, atomicReference, bufferSupplier));
    }

    private ObservableReplay(ObservableSource<T> observableSource, ObservableSource<T> observableSource2, AtomicReference<ReplayObserver<T>> atomicReference, BufferSupplier<T> bufferSupplier) {
        this.onSubscribe = observableSource;
        this.source = observableSource2;
        this.current = atomicReference;
        this.bufferFactory = bufferSupplier;
    }

    public ObservableSource<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.onSubscribe.subscribe(observer);
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(p016io.reactivex.functions.Consumer<? super p016io.reactivex.disposables.Disposable> r5) {
        /*
            r4 = this;
        L_0x0000:
            java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.observable.ObservableReplay$ReplayObserver<T>> r0 = r4.current
            java.lang.Object r0 = r0.get()
            io.reactivex.internal.operators.observable.ObservableReplay$ReplayObserver r0 = (p016io.reactivex.internal.operators.observable.ObservableReplay.ReplayObserver) r0
            if (r0 == 0) goto L_0x0010
            boolean r1 = r0.isDisposed()
            if (r1 == 0) goto L_0x0025
        L_0x0010:
            io.reactivex.internal.operators.observable.ObservableReplay$BufferSupplier<T> r1 = r4.bufferFactory
            io.reactivex.internal.operators.observable.ObservableReplay$ReplayBuffer r1 = r1.call()
            io.reactivex.internal.operators.observable.ObservableReplay$ReplayObserver r2 = new io.reactivex.internal.operators.observable.ObservableReplay$ReplayObserver
            r2.<init>(r1)
            java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.observable.ObservableReplay$ReplayObserver<T>> r1 = r4.current
            boolean r0 = r1.compareAndSet(r0, r2)
            if (r0 != 0) goto L_0x0024
            goto L_0x0000
        L_0x0024:
            r0 = r2
        L_0x0025:
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.get()
            r2 = 1
            r3 = 0
            if (r1 != 0) goto L_0x0039
            java.util.concurrent.atomic.AtomicBoolean r1 = r0.shouldConnect
            boolean r1 = r1.compareAndSet(r3, r2)
            if (r1 == 0) goto L_0x0039
            r1 = r2
            goto L_0x003a
        L_0x0039:
            r1 = r3
        L_0x003a:
            r5.accept(r0)     // Catch:{ Throwable -> 0x0045 }
            if (r1 == 0) goto L_0x0044
            io.reactivex.ObservableSource<T> r4 = r4.source
            r4.subscribe(r0)
        L_0x0044:
            return
        L_0x0045:
            r4 = move-exception
            if (r1 == 0) goto L_0x004d
            java.util.concurrent.atomic.AtomicBoolean r5 = r0.shouldConnect
            r5.compareAndSet(r2, r3)
        L_0x004d:
            p016io.reactivex.exceptions.Exceptions.throwIfFatal(r4)
            java.lang.RuntimeException r4 = p016io.reactivex.internal.util.ExceptionHelper.wrapOrThrow(r4)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: p016io.reactivex.internal.operators.observable.ObservableReplay.connect(io.reactivex.functions.Consumer):void");
    }
}
