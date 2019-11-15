package p016io.reactivex.internal.functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscription;
import p016io.reactivex.Notification;
import p016io.reactivex.Scheduler;
import p016io.reactivex.functions.Action;
import p016io.reactivex.functions.BiConsumer;
import p016io.reactivex.functions.BiFunction;
import p016io.reactivex.functions.BooleanSupplier;
import p016io.reactivex.functions.Consumer;
import p016io.reactivex.functions.Function;
import p016io.reactivex.functions.Function3;
import p016io.reactivex.functions.Function4;
import p016io.reactivex.functions.Function5;
import p016io.reactivex.functions.Function6;
import p016io.reactivex.functions.Function7;
import p016io.reactivex.functions.Function8;
import p016io.reactivex.functions.Function9;
import p016io.reactivex.functions.LongConsumer;
import p016io.reactivex.functions.Predicate;
import p016io.reactivex.plugins.RxJavaPlugins;
import p016io.reactivex.schedulers.Timed;

/* renamed from: io.reactivex.internal.functions.Functions */
public final class Functions {
    static final Predicate<Object> ALWAYS_FALSE = new Predicate<Object>() {
        public boolean test(Object obj) {
            return false;
        }
    };
    static final Predicate<Object> ALWAYS_TRUE = new Predicate<Object>() {
        public boolean test(Object obj) {
            return true;
        }
    };
    public static final Action EMPTY_ACTION = new Action() {
        public void run() {
        }

        public String toString() {
            return "EmptyAction";
        }
    };
    static final Consumer<Object> EMPTY_CONSUMER = new Consumer<Object>() {
        public void accept(Object obj) {
        }

        public String toString() {
            return "EmptyConsumer";
        }
    };
    public static final LongConsumer EMPTY_LONG_CONSUMER = new LongConsumer() {
        public void accept(long j) {
        }
    };
    public static final Runnable EMPTY_RUNNABLE = new Runnable() {
        public void run() {
        }

        public String toString() {
            return "EmptyRunnable";
        }
    };
    public static final Consumer<Throwable> ERROR_CONSUMER = new Consumer<Throwable>() {
        public void accept(Throwable th) {
            RxJavaPlugins.onError(th);
        }
    };
    static final Function<Object, Object> IDENTITY = new Function<Object, Object>() {
        public Object apply(Object obj) {
            return obj;
        }

        public String toString() {
            return "IdentityFunction";
        }
    };
    static final Comparator<Object> NATURAL_COMPARATOR = new Comparator<Object>() {
        public int compare(Object obj, Object obj2) {
            return ((Comparable) obj).compareTo(obj2);
        }
    };
    static final Callable<Object> NULL_SUPPLIER = new Callable<Object>() {
        public Object call() {
            return null;
        }
    };
    public static final Consumer<Subscription> REQUEST_MAX = new Consumer<Subscription>() {
        public void accept(Subscription subscription) throws Exception {
            subscription.request(LongCompanionObject.MAX_VALUE);
        }
    };

    /* renamed from: io.reactivex.internal.functions.Functions$ActionConsumer */
    static final class ActionConsumer<T> implements Consumer<T> {
        final Action action;

        ActionConsumer(Action action2) {
            this.action = action2;
        }

        public void accept(T t) throws Exception {
            this.action.run();
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$ArrayListCapacityCallable */
    static final class ArrayListCapacityCallable<T> implements Callable<List<T>> {
        final int capacity;

        ArrayListCapacityCallable(int i) {
            this.capacity = i;
        }

        public List<T> call() throws Exception {
            return new ArrayList(this.capacity);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$BooleanSupplierPredicateReverse */
    static final class BooleanSupplierPredicateReverse<T> implements Predicate<T> {
        final BooleanSupplier supplier;

        BooleanSupplierPredicateReverse(BooleanSupplier booleanSupplier) {
            this.supplier = booleanSupplier;
        }

        public boolean test(T t) throws Exception {
            return !this.supplier.getAsBoolean();
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$CastToClass */
    static final class CastToClass<T, U> implements Function<T, U> {
        final Class<U> clazz;

        CastToClass(Class<U> cls) {
            this.clazz = cls;
        }

        public U apply(T t) throws Exception {
            return this.clazz.cast(t);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$ClassFilter */
    static final class ClassFilter<T, U> implements Predicate<T> {
        final Class<U> clazz;

        ClassFilter(Class<U> cls) {
            this.clazz = cls;
        }

        public boolean test(T t) throws Exception {
            return this.clazz.isInstance(t);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$EqualsPredicate */
    static final class EqualsPredicate<T> implements Predicate<T> {
        final T value;

        EqualsPredicate(T t) {
            this.value = t;
        }

        public boolean test(T t) throws Exception {
            return ObjectHelper.equals(t, this.value);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$FutureAction */
    static final class FutureAction implements Action {
        final Future<?> future;

        FutureAction(Future<?> future2) {
            this.future = future2;
        }

        public void run() throws Exception {
            this.future.get();
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$HashSetCallable */
    enum HashSetCallable implements Callable<Set<Object>> {
        INSTANCE;

        public Set<Object> call() throws Exception {
            return new HashSet();
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$JustValue */
    static final class JustValue<T, U> implements Callable<U>, Function<T, U> {
        final U value;

        JustValue(U u) {
            this.value = u;
        }

        public U call() throws Exception {
            return this.value;
        }

        public U apply(T t) throws Exception {
            return this.value;
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$ListSorter */
    static final class ListSorter<T> implements Function<List<T>, List<T>> {
        private final Comparator<? super T> comparator;

        ListSorter(Comparator<? super T> comparator2) {
            this.comparator = comparator2;
        }

        public List<T> apply(List<T> list) {
            Collections.sort(list, this.comparator);
            return list;
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$NaturalComparator */
    enum NaturalComparator implements Comparator<Object> {
        INSTANCE;

        public int compare(Object obj, Object obj2) {
            return ((Comparable) obj).compareTo(obj2);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$NotificationOnComplete */
    static final class NotificationOnComplete<T> implements Action {
        final Consumer<? super Notification<T>> onNotification;

        NotificationOnComplete(Consumer<? super Notification<T>> consumer) {
            this.onNotification = consumer;
        }

        public void run() throws Exception {
            this.onNotification.accept(Notification.createOnComplete());
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$NotificationOnError */
    static final class NotificationOnError<T> implements Consumer<Throwable> {
        final Consumer<? super Notification<T>> onNotification;

        NotificationOnError(Consumer<? super Notification<T>> consumer) {
            this.onNotification = consumer;
        }

        public void accept(Throwable th) throws Exception {
            this.onNotification.accept(Notification.createOnError(th));
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$NotificationOnNext */
    static final class NotificationOnNext<T> implements Consumer<T> {
        final Consumer<? super Notification<T>> onNotification;

        NotificationOnNext(Consumer<? super Notification<T>> consumer) {
            this.onNotification = consumer;
        }

        public void accept(T t) throws Exception {
            this.onNotification.accept(Notification.createOnNext(t));
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$TimestampFunction */
    static final class TimestampFunction<T> implements Function<T, Timed<T>> {
        final Scheduler scheduler;
        final TimeUnit unit;

        TimestampFunction(TimeUnit timeUnit, Scheduler scheduler2) {
            this.unit = timeUnit;
            this.scheduler = scheduler2;
        }

        public Timed<T> apply(T t) throws Exception {
            return new Timed<>(t, this.scheduler.now(this.unit), this.unit);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$ToMapKeySelector */
    static final class ToMapKeySelector<K, T> implements BiConsumer<Map<K, T>, T> {
        private final Function<? super T, ? extends K> keySelector;

        ToMapKeySelector(Function<? super T, ? extends K> function) {
            this.keySelector = function;
        }

        public void accept(Map<K, T> map, T t) throws Exception {
            map.put(this.keySelector.apply(t), t);
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$ToMapKeyValueSelector */
    static final class ToMapKeyValueSelector<K, V, T> implements BiConsumer<Map<K, V>, T> {
        private final Function<? super T, ? extends K> keySelector;
        private final Function<? super T, ? extends V> valueSelector;

        ToMapKeyValueSelector(Function<? super T, ? extends V> function, Function<? super T, ? extends K> function2) {
            this.valueSelector = function;
            this.keySelector = function2;
        }

        public void accept(Map<K, V> map, T t) throws Exception {
            map.put(this.keySelector.apply(t), this.valueSelector.apply(t));
        }
    }

    /* renamed from: io.reactivex.internal.functions.Functions$ToMultimapKeyValueSelector */
    static final class ToMultimapKeyValueSelector<K, V, T> implements BiConsumer<Map<K, Collection<V>>, T> {
        private final Function<? super K, ? extends Collection<? super V>> collectionFactory;
        private final Function<? super T, ? extends K> keySelector;
        private final Function<? super T, ? extends V> valueSelector;

        ToMultimapKeyValueSelector(Function<? super K, ? extends Collection<? super V>> function, Function<? super T, ? extends V> function2, Function<? super T, ? extends K> function3) {
            this.collectionFactory = function;
            this.valueSelector = function2;
            this.keySelector = function3;
        }

        public void accept(Map<K, Collection<V>> map, T t) throws Exception {
            Object apply = this.keySelector.apply(t);
            Collection collection = (Collection) map.get(apply);
            if (collection == null) {
                collection = (Collection) this.collectionFactory.apply(apply);
                map.put(apply, collection);
            }
            collection.add(this.valueSelector.apply(t));
        }
    }

    private Functions() {
        throw new IllegalStateException("No instances!");
    }

    public static <T1, T2, R> Function<Object[], R> toFunction(final BiFunction<? super T1, ? super T2, ? extends R> biFunction) {
        ObjectHelper.requireNonNull(biFunction, "f is null");
        return new Function<Object[], R>() {
            public R apply(Object[] objArr) throws Exception {
                if (objArr.length == 2) {
                    return biFunction.apply(objArr[0], objArr[1]);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Array of size 2 expected but got ");
                sb.append(objArr.length);
                throw new IllegalArgumentException(sb.toString());
            }
        };
    }

    public static <T1, T2, T3, R> Function<Object[], R> toFunction(final Function3<T1, T2, T3, R> function3) {
        ObjectHelper.requireNonNull(function3, "f is null");
        return new Function<Object[], R>() {
            public R apply(Object[] objArr) throws Exception {
                if (objArr.length == 3) {
                    return function3.apply(objArr[0], objArr[1], objArr[2]);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Array of size 3 expected but got ");
                sb.append(objArr.length);
                throw new IllegalArgumentException(sb.toString());
            }
        };
    }

    public static <T1, T2, T3, T4, R> Function<Object[], R> toFunction(final Function4<T1, T2, T3, T4, R> function4) {
        ObjectHelper.requireNonNull(function4, "f is null");
        return new Function<Object[], R>() {
            public R apply(Object[] objArr) throws Exception {
                if (objArr.length == 4) {
                    return function4.apply(objArr[0], objArr[1], objArr[2], objArr[3]);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Array of size 4 expected but got ");
                sb.append(objArr.length);
                throw new IllegalArgumentException(sb.toString());
            }
        };
    }

    public static <T1, T2, T3, T4, T5, R> Function<Object[], R> toFunction(final Function5<T1, T2, T3, T4, T5, R> function5) {
        ObjectHelper.requireNonNull(function5, "f is null");
        return new Function<Object[], R>() {
            public R apply(Object[] objArr) throws Exception {
                if (objArr.length == 5) {
                    return function5.apply(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4]);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Array of size 5 expected but got ");
                sb.append(objArr.length);
                throw new IllegalArgumentException(sb.toString());
            }
        };
    }

    public static <T1, T2, T3, T4, T5, T6, R> Function<Object[], R> toFunction(final Function6<T1, T2, T3, T4, T5, T6, R> function6) {
        ObjectHelper.requireNonNull(function6, "f is null");
        return new Function<Object[], R>() {
            public R apply(Object[] objArr) throws Exception {
                if (objArr.length == 6) {
                    return function6.apply(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5]);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Array of size 6 expected but got ");
                sb.append(objArr.length);
                throw new IllegalArgumentException(sb.toString());
            }
        };
    }

    public static <T1, T2, T3, T4, T5, T6, T7, R> Function<Object[], R> toFunction(final Function7<T1, T2, T3, T4, T5, T6, T7, R> function7) {
        ObjectHelper.requireNonNull(function7, "f is null");
        return new Function<Object[], R>() {
            public R apply(Object[] objArr) throws Exception {
                if (objArr.length == 7) {
                    return function7.apply(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5], objArr[6]);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Array of size 7 expected but got ");
                sb.append(objArr.length);
                throw new IllegalArgumentException(sb.toString());
            }
        };
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Function<Object[], R> toFunction(final Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> function8) {
        ObjectHelper.requireNonNull(function8, "f is null");
        return new Function<Object[], R>() {
            public R apply(Object[] objArr) throws Exception {
                if (objArr.length == 8) {
                    return function8.apply(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5], objArr[6], objArr[7]);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Array of size 8 expected but got ");
                sb.append(objArr.length);
                throw new IllegalArgumentException(sb.toString());
            }
        };
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Function<Object[], R> toFunction(final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> function9) {
        ObjectHelper.requireNonNull(function9, "f is null");
        return new Function<Object[], R>() {
            public R apply(Object[] objArr) throws Exception {
                if (objArr.length == 9) {
                    return function9.apply(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4], objArr[5], objArr[6], objArr[7], objArr[8]);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Array of size 9 expected but got ");
                sb.append(objArr.length);
                throw new IllegalArgumentException(sb.toString());
            }
        };
    }

    public static <T> Function<T, T> identity() {
        return IDENTITY;
    }

    public static <T> Consumer<T> emptyConsumer() {
        return EMPTY_CONSUMER;
    }

    public static <T> Predicate<T> alwaysTrue() {
        return ALWAYS_TRUE;
    }

    public static <T> Predicate<T> alwaysFalse() {
        return ALWAYS_FALSE;
    }

    public static <T> Callable<T> nullSupplier() {
        return NULL_SUPPLIER;
    }

    public static <T> Comparator<T> naturalOrder() {
        return NATURAL_COMPARATOR;
    }

    public static Action futureAction(Future<?> future) {
        return new FutureAction(future);
    }

    public static <T> Callable<T> justCallable(T t) {
        return new JustValue(t);
    }

    public static <T, U> Function<T, U> justFunction(U u) {
        return new JustValue(u);
    }

    public static <T, U> Function<T, U> castFunction(Class<U> cls) {
        return new CastToClass(cls);
    }

    public static <T> Callable<List<T>> createArrayList(int i) {
        return new ArrayListCapacityCallable(i);
    }

    public static <T> Predicate<T> equalsWith(T t) {
        return new EqualsPredicate(t);
    }

    public static <T> Callable<Set<T>> createHashSet() {
        return HashSetCallable.INSTANCE;
    }

    public static <T> Consumer<T> notificationOnNext(Consumer<? super Notification<T>> consumer) {
        return new NotificationOnNext(consumer);
    }

    public static <T> Consumer<Throwable> notificationOnError(Consumer<? super Notification<T>> consumer) {
        return new NotificationOnError(consumer);
    }

    public static <T> Action notificationOnComplete(Consumer<? super Notification<T>> consumer) {
        return new NotificationOnComplete(consumer);
    }

    public static <T> Consumer<T> actionConsumer(Action action) {
        return new ActionConsumer(action);
    }

    public static <T, U> Predicate<T> isInstanceOf(Class<U> cls) {
        return new ClassFilter(cls);
    }

    public static <T> Predicate<T> predicateReverseFor(BooleanSupplier booleanSupplier) {
        return new BooleanSupplierPredicateReverse(booleanSupplier);
    }

    public static <T> Function<T, Timed<T>> timestampWith(TimeUnit timeUnit, Scheduler scheduler) {
        return new TimestampFunction(timeUnit, scheduler);
    }

    public static <T, K> BiConsumer<Map<K, T>, T> toMapKeySelector(Function<? super T, ? extends K> function) {
        return new ToMapKeySelector(function);
    }

    public static <T, K, V> BiConsumer<Map<K, V>, T> toMapKeyValueSelector(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2) {
        return new ToMapKeyValueSelector(function2, function);
    }

    public static <T, K, V> BiConsumer<Map<K, Collection<V>>, T> toMultimapKeyValueSelector(Function<? super T, ? extends K> function, Function<? super T, ? extends V> function2, Function<? super K, ? extends Collection<? super V>> function3) {
        return new ToMultimapKeyValueSelector(function3, function2, function);
    }

    public static <T> Comparator<T> naturalComparator() {
        return NaturalComparator.INSTANCE;
    }

    public static <T> Function<List<T>, List<T>> listSorter(Comparator<? super T> comparator) {
        return new ListSorter(comparator);
    }
}
