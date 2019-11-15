package android.arch.persistence.room;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.core.internal.SafeIterableMap;
import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.p000db.SupportSQLiteStatement;
import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;
import android.support.p001v4.util.ArrayMap;
import android.support.p001v4.util.ArraySet;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class InvalidationTracker {
    @VisibleForTesting
    static final String CLEANUP_SQL = "DELETE FROM room_table_modification_log WHERE version NOT IN( SELECT MAX(version) FROM room_table_modification_log GROUP BY table_id)";
    private static final String CREATE_VERSION_TABLE_SQL = "CREATE TEMP TABLE room_table_modification_log(version INTEGER PRIMARY KEY AUTOINCREMENT, table_id INTEGER)";
    @VisibleForTesting
    static final String SELECT_UPDATED_TABLES_SQL = "SELECT * FROM room_table_modification_log WHERE version  > ? ORDER BY version ASC;";
    private static final String TABLE_ID_COLUMN_NAME = "table_id";
    private static final String[] TRIGGERS = {"UPDATE", "DELETE", "INSERT"};
    private static final String UPDATE_TABLE_NAME = "room_table_modification_log";
    private static final String VERSION_COLUMN_NAME = "version";
    /* access modifiers changed from: private */
    public volatile SupportSQLiteStatement mCleanupStatement;
    /* access modifiers changed from: private */
    public final RoomDatabase mDatabase;
    private volatile boolean mInitialized;
    /* access modifiers changed from: private */
    public long mMaxVersion = 0;
    /* access modifiers changed from: private */
    public ObservedTableTracker mObservedTableTracker;
    @VisibleForTesting
    final SafeIterableMap<Observer, ObserverWrapper> mObserverMap;
    AtomicBoolean mPendingRefresh;
    /* access modifiers changed from: private */
    public Object[] mQueryArgs = new Object[1];
    @VisibleForTesting
    Runnable mRefreshRunnable;
    private Runnable mSyncTriggers;
    @VisibleForTesting
    @NonNull
    ArrayMap<String, Integer> mTableIdLookup;
    private String[] mTableNames;
    @VisibleForTesting
    @NonNull
    long[] mTableVersions;

    static class ObservedTableTracker {
        static final int ADD = 1;
        static final int NO_OP = 0;
        static final int REMOVE = 2;
        boolean mNeedsSync;
        boolean mPendingSync;
        final long[] mTableObservers;
        final int[] mTriggerStateChanges;
        final boolean[] mTriggerStates;

        ObservedTableTracker(int i) {
            this.mTableObservers = new long[i];
            this.mTriggerStates = new boolean[i];
            this.mTriggerStateChanges = new int[i];
            Arrays.fill(this.mTableObservers, 0);
            Arrays.fill(this.mTriggerStates, false);
        }

        /* access modifiers changed from: 0000 */
        public boolean onAdded(int... iArr) {
            boolean z;
            synchronized (this) {
                z = false;
                for (int i : iArr) {
                    long j = this.mTableObservers[i];
                    this.mTableObservers[i] = 1 + j;
                    if (j == 0) {
                        this.mNeedsSync = true;
                        z = true;
                    }
                }
            }
            return z;
        }

        /* access modifiers changed from: 0000 */
        public boolean onRemoved(int... iArr) {
            boolean z;
            synchronized (this) {
                z = false;
                for (int i : iArr) {
                    long j = this.mTableObservers[i];
                    this.mTableObservers[i] = j - 1;
                    if (j == 1) {
                        this.mNeedsSync = true;
                        z = true;
                    }
                }
            }
            return z;
        }

        /* access modifiers changed from: 0000 */
        @Nullable
        public int[] getTablesToSync() {
            synchronized (this) {
                if (this.mNeedsSync) {
                    if (!this.mPendingSync) {
                        int length = this.mTableObservers.length;
                        int i = 0;
                        while (true) {
                            int i2 = 1;
                            if (i < length) {
                                boolean z = this.mTableObservers[i] > 0;
                                if (z != this.mTriggerStates[i]) {
                                    int[] iArr = this.mTriggerStateChanges;
                                    if (!z) {
                                        i2 = 2;
                                    }
                                    iArr[i] = i2;
                                } else {
                                    this.mTriggerStateChanges[i] = 0;
                                }
                                this.mTriggerStates[i] = z;
                                i++;
                            } else {
                                this.mPendingSync = true;
                                this.mNeedsSync = false;
                                int[] iArr2 = this.mTriggerStateChanges;
                                return iArr2;
                            }
                        }
                    }
                }
                return null;
            }
        }

        /* access modifiers changed from: 0000 */
        public void onSyncCompleted() {
            synchronized (this) {
                this.mPendingSync = false;
            }
        }
    }

    public static abstract class Observer {
        final String[] mTables;

        public abstract void onInvalidated(@NonNull Set<String> set);

        protected Observer(@NonNull String str, String... strArr) {
            this.mTables = (String[]) Arrays.copyOf(strArr, strArr.length + 1);
            this.mTables[strArr.length] = str;
        }

        public Observer(@NonNull String[] strArr) {
            this.mTables = (String[]) Arrays.copyOf(strArr, strArr.length);
        }
    }

    static class ObserverWrapper {
        final Observer mObserver;
        private final Set<String> mSingleTableSet;
        final int[] mTableIds;
        private final String[] mTableNames;
        private final long[] mVersions;

        ObserverWrapper(Observer observer, int[] iArr, String[] strArr, long[] jArr) {
            this.mObserver = observer;
            this.mTableIds = iArr;
            this.mTableNames = strArr;
            this.mVersions = jArr;
            if (iArr.length == 1) {
                ArraySet arraySet = new ArraySet();
                arraySet.add(this.mTableNames[0]);
                this.mSingleTableSet = Collections.unmodifiableSet(arraySet);
                return;
            }
            this.mSingleTableSet = null;
        }

        /* access modifiers changed from: 0000 */
        public void checkForInvalidation(long[] jArr) {
            int length = this.mTableIds.length;
            Set set = null;
            for (int i = 0; i < length; i++) {
                long j = jArr[this.mTableIds[i]];
                if (this.mVersions[i] < j) {
                    this.mVersions[i] = j;
                    if (length == 1) {
                        set = this.mSingleTableSet;
                    } else {
                        if (set == null) {
                            set = new ArraySet(length);
                        }
                        set.add(this.mTableNames[i]);
                    }
                }
            }
            if (set != null) {
                this.mObserver.onInvalidated(set);
            }
        }
    }

    static class WeakObserver extends Observer {
        final WeakReference<Observer> mDelegateRef;
        final InvalidationTracker mTracker;

        WeakObserver(InvalidationTracker invalidationTracker, Observer observer) {
            super(observer.mTables);
            this.mTracker = invalidationTracker;
            this.mDelegateRef = new WeakReference<>(observer);
        }

        public void onInvalidated(@NonNull Set<String> set) {
            Observer observer = (Observer) this.mDelegateRef.get();
            if (observer == null) {
                this.mTracker.removeObserver(this);
            } else {
                observer.onInvalidated(set);
            }
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public InvalidationTracker(RoomDatabase roomDatabase, String... strArr) {
        this.mPendingRefresh = new AtomicBoolean(false);
        this.mInitialized = false;
        this.mObserverMap = new SafeIterableMap<>();
        this.mSyncTriggers = new Runnable() {
            public void run() {
                SupportSQLiteDatabase writableDatabase;
                if (!InvalidationTracker.this.mDatabase.inTransaction() && InvalidationTracker.this.ensureInitialization()) {
                    while (true) {
                        try {
                            int[] tablesToSync = InvalidationTracker.this.mObservedTableTracker.getTablesToSync();
                            if (tablesToSync != null) {
                                int length = tablesToSync.length;
                                writableDatabase = InvalidationTracker.this.mDatabase.getOpenHelper().getWritableDatabase();
                                writableDatabase.beginTransaction();
                                for (int i = 0; i < length; i++) {
                                    switch (tablesToSync[i]) {
                                        case 1:
                                            InvalidationTracker.this.startTrackingTable(writableDatabase, i);
                                            break;
                                        case 2:
                                            InvalidationTracker.this.stopTrackingTable(writableDatabase, i);
                                            break;
                                    }
                                }
                                writableDatabase.setTransactionSuccessful();
                                writableDatabase.endTransaction();
                                InvalidationTracker.this.mObservedTableTracker.onSyncCompleted();
                            } else {
                                return;
                            }
                        } catch (SQLiteException | IllegalStateException e) {
                            Log.e("ROOM", "Cannot run invalidation tracker. Is the db closed?", e);
                            return;
                        } catch (Throwable th) {
                            writableDatabase.endTransaction();
                            throw th;
                        }
                    }
                }
            }
        };
        this.mRefreshRunnable = new Runnable() {
            /* JADX WARNING: Removed duplicated region for block: B:43:0x00a1  */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r9 = this;
                    android.arch.persistence.room.InvalidationTracker r0 = android.arch.persistence.room.InvalidationTracker.this
                    android.arch.persistence.room.RoomDatabase r0 = r0.mDatabase
                    java.util.concurrent.locks.Lock r0 = r0.getCloseLock()
                    r1 = 0
                    r0.lock()     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    android.arch.persistence.room.InvalidationTracker r2 = android.arch.persistence.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    boolean r2 = r2.ensureInitialization()     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    if (r2 != 0) goto L_0x001a
                    r0.unlock()
                    return
                L_0x001a:
                    android.arch.persistence.room.InvalidationTracker r2 = android.arch.persistence.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    java.util.concurrent.atomic.AtomicBoolean r2 = r2.mPendingRefresh     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    r3 = 1
                    boolean r2 = r2.compareAndSet(r3, r1)     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    if (r2 != 0) goto L_0x0029
                    r0.unlock()
                    return
                L_0x0029:
                    android.arch.persistence.room.InvalidationTracker r2 = android.arch.persistence.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    android.arch.persistence.room.RoomDatabase r2 = r2.mDatabase     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    boolean r2 = r2.inTransaction()     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    if (r2 == 0) goto L_0x0039
                    r0.unlock()
                    return
                L_0x0039:
                    android.arch.persistence.room.InvalidationTracker r2 = android.arch.persistence.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    android.arch.persistence.db.SupportSQLiteStatement r2 = r2.mCleanupStatement     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    r2.executeUpdateDelete()     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    android.arch.persistence.room.InvalidationTracker r2 = android.arch.persistence.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    java.lang.Object[] r2 = r2.mQueryArgs     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    android.arch.persistence.room.InvalidationTracker r4 = android.arch.persistence.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    long r4 = r4.mMaxVersion     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    r2[r1] = r4     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    android.arch.persistence.room.InvalidationTracker r2 = android.arch.persistence.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    android.arch.persistence.room.RoomDatabase r2 = r2.mDatabase     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    java.lang.String r4 = "SELECT * FROM room_table_modification_log WHERE version  > ? ORDER BY version ASC;"
                    android.arch.persistence.room.InvalidationTracker r5 = android.arch.persistence.room.InvalidationTracker.this     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    java.lang.Object[] r5 = r5.mQueryArgs     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    android.database.Cursor r2 = r2.query(r4, r5)     // Catch:{ SQLiteException | IllegalStateException -> 0x0093 }
                    r4 = r1
                L_0x0067:
                    boolean r5 = r2.moveToNext()     // Catch:{ all -> 0x0089 }
                    if (r5 == 0) goto L_0x0085
                    long r5 = r2.getLong(r1)     // Catch:{ all -> 0x0089 }
                    int r7 = r2.getInt(r3)     // Catch:{ all -> 0x0089 }
                    android.arch.persistence.room.InvalidationTracker r8 = android.arch.persistence.room.InvalidationTracker.this     // Catch:{ all -> 0x0089 }
                    long[] r8 = r8.mTableVersions     // Catch:{ all -> 0x0089 }
                    r8[r7] = r5     // Catch:{ all -> 0x0089 }
                    android.arch.persistence.room.InvalidationTracker r4 = android.arch.persistence.room.InvalidationTracker.this     // Catch:{ all -> 0x0082 }
                    r4.mMaxVersion = r5     // Catch:{ all -> 0x0082 }
                    r4 = r3
                    goto L_0x0067
                L_0x0082:
                    r1 = move-exception
                    r4 = r3
                    goto L_0x008a
                L_0x0085:
                    r2.close()     // Catch:{ SQLiteException | IllegalStateException -> 0x008e }
                    goto L_0x009c
                L_0x0089:
                    r1 = move-exception
                L_0x008a:
                    r2.close()     // Catch:{ SQLiteException | IllegalStateException -> 0x008e }
                    throw r1     // Catch:{ SQLiteException | IllegalStateException -> 0x008e }
                L_0x008e:
                    r1 = move-exception
                    r2 = r1
                    goto L_0x0095
                L_0x0091:
                    r9 = move-exception
                    goto L_0x00ce
                L_0x0093:
                    r2 = move-exception
                    r4 = r1
                L_0x0095:
                    java.lang.String r1 = "ROOM"
                    java.lang.String r3 = "Cannot run invalidation tracker. Is the db closed?"
                    android.util.Log.e(r1, r3, r2)     // Catch:{ all -> 0x0091 }
                L_0x009c:
                    r0.unlock()
                    if (r4 == 0) goto L_0x00cd
                    android.arch.persistence.room.InvalidationTracker r0 = android.arch.persistence.room.InvalidationTracker.this
                    android.arch.core.internal.SafeIterableMap<android.arch.persistence.room.InvalidationTracker$Observer, android.arch.persistence.room.InvalidationTracker$ObserverWrapper> r0 = r0.mObserverMap
                    monitor-enter(r0)
                    android.arch.persistence.room.InvalidationTracker r1 = android.arch.persistence.room.InvalidationTracker.this     // Catch:{ all -> 0x00ca }
                    android.arch.core.internal.SafeIterableMap<android.arch.persistence.room.InvalidationTracker$Observer, android.arch.persistence.room.InvalidationTracker$ObserverWrapper> r1 = r1.mObserverMap     // Catch:{ all -> 0x00ca }
                    java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x00ca }
                L_0x00ae:
                    boolean r2 = r1.hasNext()     // Catch:{ all -> 0x00ca }
                    if (r2 == 0) goto L_0x00c8
                    java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x00ca }
                    java.util.Map$Entry r2 = (java.util.Map.Entry) r2     // Catch:{ all -> 0x00ca }
                    java.lang.Object r2 = r2.getValue()     // Catch:{ all -> 0x00ca }
                    android.arch.persistence.room.InvalidationTracker$ObserverWrapper r2 = (android.arch.persistence.room.InvalidationTracker.ObserverWrapper) r2     // Catch:{ all -> 0x00ca }
                    android.arch.persistence.room.InvalidationTracker r3 = android.arch.persistence.room.InvalidationTracker.this     // Catch:{ all -> 0x00ca }
                    long[] r3 = r3.mTableVersions     // Catch:{ all -> 0x00ca }
                    r2.checkForInvalidation(r3)     // Catch:{ all -> 0x00ca }
                    goto L_0x00ae
                L_0x00c8:
                    monitor-exit(r0)     // Catch:{ all -> 0x00ca }
                    goto L_0x00cd
                L_0x00ca:
                    r9 = move-exception
                    monitor-exit(r0)     // Catch:{ all -> 0x00ca }
                    throw r9
                L_0x00cd:
                    return
                L_0x00ce:
                    r0.unlock()
                    throw r9
                */
                throw new UnsupportedOperationException("Method not decompiled: android.arch.persistence.room.InvalidationTracker.C00132.run():void");
            }
        };
        this.mDatabase = roomDatabase;
        this.mObservedTableTracker = new ObservedTableTracker(strArr.length);
        this.mTableIdLookup = new ArrayMap<>();
        int length = strArr.length;
        this.mTableNames = new String[length];
        for (int i = 0; i < length; i++) {
            String lowerCase = strArr[i].toLowerCase(Locale.US);
            this.mTableIdLookup.put(lowerCase, Integer.valueOf(i));
            this.mTableNames[i] = lowerCase;
        }
        this.mTableVersions = new long[strArr.length];
        Arrays.fill(this.mTableVersions, 0);
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: 0000 */
    public void internalInit(SupportSQLiteDatabase supportSQLiteDatabase) {
        synchronized (this) {
            if (this.mInitialized) {
                Log.e("ROOM", "Invalidation tracker is initialized twice :/.");
                return;
            }
            supportSQLiteDatabase.beginTransaction();
            try {
                supportSQLiteDatabase.execSQL("PRAGMA temp_store = MEMORY;");
                supportSQLiteDatabase.execSQL("PRAGMA recursive_triggers='ON';");
                supportSQLiteDatabase.execSQL(CREATE_VERSION_TABLE_SQL);
                supportSQLiteDatabase.setTransactionSuccessful();
                supportSQLiteDatabase.endTransaction();
                this.mCleanupStatement = supportSQLiteDatabase.compileStatement(CLEANUP_SQL);
                this.mInitialized = true;
            } catch (Throwable th) {
                supportSQLiteDatabase.endTransaction();
                throw th;
            }
        }
    }

    private static void appendTriggerName(StringBuilder sb, String str, String str2) {
        sb.append("`");
        sb.append("room_table_modification_trigger_");
        sb.append(str);
        sb.append("_");
        sb.append(str2);
        sb.append("`");
    }

    /* access modifiers changed from: private */
    public void stopTrackingTable(SupportSQLiteDatabase supportSQLiteDatabase, int i) {
        String[] strArr;
        String str = this.mTableNames[i];
        StringBuilder sb = new StringBuilder();
        for (String str2 : TRIGGERS) {
            sb.setLength(0);
            sb.append("DROP TRIGGER IF EXISTS ");
            appendTriggerName(sb, str, str2);
            supportSQLiteDatabase.execSQL(sb.toString());
        }
    }

    /* access modifiers changed from: private */
    public void startTrackingTable(SupportSQLiteDatabase supportSQLiteDatabase, int i) {
        String[] strArr;
        String str = this.mTableNames[i];
        StringBuilder sb = new StringBuilder();
        for (String str2 : TRIGGERS) {
            sb.setLength(0);
            sb.append("CREATE TEMP TRIGGER IF NOT EXISTS ");
            appendTriggerName(sb, str, str2);
            sb.append(" AFTER ");
            sb.append(str2);
            sb.append(" ON `");
            sb.append(str);
            sb.append("` BEGIN INSERT OR REPLACE INTO ");
            sb.append(UPDATE_TABLE_NAME);
            sb.append(" VALUES(null, ");
            sb.append(i);
            sb.append("); END");
            supportSQLiteDatabase.execSQL(sb.toString());
        }
    }

    public void addObserver(@NonNull Observer observer) {
        ObserverWrapper observerWrapper;
        String[] strArr = observer.mTables;
        int[] iArr = new int[strArr.length];
        int length = strArr.length;
        long[] jArr = new long[strArr.length];
        for (int i = 0; i < length; i++) {
            Integer num = (Integer) this.mTableIdLookup.get(strArr[i].toLowerCase(Locale.US));
            if (num == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("There is no table with name ");
                sb.append(strArr[i]);
                throw new IllegalArgumentException(sb.toString());
            }
            iArr[i] = num.intValue();
            jArr[i] = this.mMaxVersion;
        }
        ObserverWrapper observerWrapper2 = new ObserverWrapper(observer, iArr, strArr, jArr);
        synchronized (this.mObserverMap) {
            observerWrapper = (ObserverWrapper) this.mObserverMap.putIfAbsent(observer, observerWrapper2);
        }
        if (observerWrapper == null && this.mObservedTableTracker.onAdded(iArr)) {
            ArchTaskExecutor.getInstance().executeOnDiskIO(this.mSyncTriggers);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void addWeakObserver(Observer observer) {
        addObserver(new WeakObserver(this, observer));
    }

    public void removeObserver(@NonNull Observer observer) {
        ObserverWrapper observerWrapper;
        synchronized (this.mObserverMap) {
            observerWrapper = (ObserverWrapper) this.mObserverMap.remove(observer);
        }
        if (observerWrapper != null && this.mObservedTableTracker.onRemoved(observerWrapper.mTableIds)) {
            ArchTaskExecutor.getInstance().executeOnDiskIO(this.mSyncTriggers);
        }
    }

    /* access modifiers changed from: private */
    public boolean ensureInitialization() {
        if (!this.mDatabase.isOpen()) {
            return false;
        }
        if (!this.mInitialized) {
            this.mDatabase.getOpenHelper().getWritableDatabase();
        }
        if (this.mInitialized) {
            return true;
        }
        Log.e("ROOM", "database is not initialized even though it is open");
        return false;
    }

    public void refreshVersionsAsync() {
        if (this.mPendingRefresh.compareAndSet(false, true)) {
            ArchTaskExecutor.getInstance().executeOnDiskIO(this.mRefreshRunnable);
        }
    }

    @WorkerThread
    @RestrictTo({Scope.LIBRARY_GROUP})
    public void refreshVersionsSync() {
        syncTriggers();
        this.mRefreshRunnable.run();
    }

    /* access modifiers changed from: 0000 */
    public void syncTriggers() {
        this.mSyncTriggers.run();
    }
}
