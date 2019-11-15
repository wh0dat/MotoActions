package android.arch.persistence.room;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.persistence.p000db.SimpleSQLiteQuery;
import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper.Factory;
import android.arch.persistence.p000db.SupportSQLiteQuery;
import android.arch.persistence.p000db.SupportSQLiteStatement;
import android.arch.persistence.p000db.framework.FrameworkSQLiteOpenHelperFactory;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.p001v4.util.SparseArrayCompat;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class RoomDatabase {
    private static final String DB_IMPL_SUFFIX = "_Impl";
    private boolean mAllowMainThreadQueries;
    @Nullable
    protected List<Callback> mCallbacks;
    private final ReentrantLock mCloseLock = new ReentrantLock();
    protected volatile SupportSQLiteDatabase mDatabase;
    private final InvalidationTracker mInvalidationTracker = createInvalidationTracker();
    private SupportSQLiteOpenHelper mOpenHelper;

    public static class Builder<T extends RoomDatabase> {
        private boolean mAllowMainThreadQueries;
        private ArrayList<Callback> mCallbacks;
        private final Context mContext;
        private final Class<T> mDatabaseClass;
        private Factory mFactory;
        private MigrationContainer mMigrationContainer = new MigrationContainer();
        private final String mName;
        private boolean mRequireMigration = true;

        Builder(@NonNull Context context, @NonNull Class<T> cls, @Nullable String str) {
            this.mContext = context;
            this.mDatabaseClass = cls;
            this.mName = str;
        }

        @NonNull
        public Builder<T> openHelperFactory(@Nullable Factory factory) {
            this.mFactory = factory;
            return this;
        }

        @NonNull
        public Builder<T> addMigrations(Migration... migrationArr) {
            this.mMigrationContainer.addMigrations(migrationArr);
            return this;
        }

        @NonNull
        public Builder<T> allowMainThreadQueries() {
            this.mAllowMainThreadQueries = true;
            return this;
        }

        @NonNull
        public Builder<T> fallbackToDestructiveMigration() {
            this.mRequireMigration = false;
            return this;
        }

        @NonNull
        public Builder<T> addCallback(@NonNull Callback callback) {
            if (this.mCallbacks == null) {
                this.mCallbacks = new ArrayList<>();
            }
            this.mCallbacks.add(callback);
            return this;
        }

        @NonNull
        public T build() {
            if (this.mContext == null) {
                throw new IllegalArgumentException("Cannot provide null context for the database.");
            } else if (this.mDatabaseClass == null) {
                throw new IllegalArgumentException("Must provide an abstract class that extends RoomDatabase");
            } else {
                if (this.mFactory == null) {
                    this.mFactory = new FrameworkSQLiteOpenHelperFactory();
                }
                DatabaseConfiguration databaseConfiguration = new DatabaseConfiguration(this.mContext, this.mName, this.mFactory, this.mMigrationContainer, this.mCallbacks, this.mAllowMainThreadQueries, this.mRequireMigration);
                T t = (RoomDatabase) Room.getGeneratedImplementation(this.mDatabaseClass, RoomDatabase.DB_IMPL_SUFFIX);
                t.init(databaseConfiguration);
                return t;
            }
        }
    }

    public static abstract class Callback {
        public void onCreate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
        }

        public void onOpen(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
        }
    }

    public static class MigrationContainer {
        private SparseArrayCompat<SparseArrayCompat<Migration>> mMigrations = new SparseArrayCompat<>();

        public void addMigrations(Migration... migrationArr) {
            for (Migration addMigration : migrationArr) {
                addMigration(addMigration);
            }
        }

        private void addMigration(Migration migration) {
            int i = migration.startVersion;
            int i2 = migration.endVersion;
            SparseArrayCompat sparseArrayCompat = (SparseArrayCompat) this.mMigrations.get(i);
            if (sparseArrayCompat == null) {
                sparseArrayCompat = new SparseArrayCompat();
                this.mMigrations.put(i, sparseArrayCompat);
            }
            Migration migration2 = (Migration) sparseArrayCompat.get(i2);
            if (migration2 != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Overriding migration ");
                sb.append(migration2);
                sb.append(" with ");
                sb.append(migration);
                Log.w("ROOM", sb.toString());
            }
            sparseArrayCompat.append(i2, migration);
        }

        @Nullable
        public List<Migration> findMigrationPath(int i, int i2) {
            if (i == i2) {
                return Collections.emptyList();
            }
            return findUpMigrationPath(new ArrayList(), i2 > i, i, i2);
        }

        private List<Migration> findUpMigrationPath(List<Migration> list, boolean z, int i, int i2) {
            boolean z2;
            int i3;
            int i4;
            int i5 = z ? -1 : 1;
            do {
                if (z) {
                    if (i >= i2) {
                        return list;
                    }
                } else if (i <= i2) {
                    return list;
                }
                SparseArrayCompat sparseArrayCompat = (SparseArrayCompat) this.mMigrations.get(i);
                if (sparseArrayCompat != null) {
                    int size = sparseArrayCompat.size();
                    z2 = false;
                    if (z) {
                        i4 = size - 1;
                        i3 = -1;
                    } else {
                        i3 = size;
                        i4 = 0;
                    }
                    while (true) {
                        if (i4 == i3) {
                            break;
                        }
                        int keyAt = sparseArrayCompat.keyAt(i4);
                        if (keyAt <= i2 && keyAt > i) {
                            list.add(sparseArrayCompat.valueAt(i4));
                            z2 = true;
                            i = keyAt;
                            continue;
                            break;
                        }
                        i4 += i5;
                    }
                } else {
                    return null;
                }
            } while (z2);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public abstract InvalidationTracker createInvalidationTracker();

    /* access modifiers changed from: protected */
    public abstract SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration);

    /* access modifiers changed from: 0000 */
    public Lock getCloseLock() {
        return this.mCloseLock;
    }

    @CallSuper
    public void init(DatabaseConfiguration databaseConfiguration) {
        this.mOpenHelper = createOpenHelper(databaseConfiguration);
        this.mCallbacks = databaseConfiguration.callbacks;
        this.mAllowMainThreadQueries = databaseConfiguration.allowMainThreadQueries;
    }

    public SupportSQLiteOpenHelper getOpenHelper() {
        return this.mOpenHelper;
    }

    public boolean isOpen() {
        SupportSQLiteDatabase supportSQLiteDatabase = this.mDatabase;
        return supportSQLiteDatabase != null && supportSQLiteDatabase.isOpen();
    }

    public void close() {
        if (isOpen()) {
            try {
                this.mCloseLock.lock();
                this.mOpenHelper.close();
            } finally {
                this.mCloseLock.unlock();
            }
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void assertNotMainThread() {
        if (!this.mAllowMainThreadQueries && ArchTaskExecutor.getInstance().isMainThread()) {
            throw new IllegalStateException("Cannot access database on the main thread since it may potentially lock the UI for a long period of time.");
        }
    }

    public Cursor query(String str, @Nullable Object[] objArr) {
        return this.mOpenHelper.getWritableDatabase().query((SupportSQLiteQuery) new SimpleSQLiteQuery(str, objArr));
    }

    public Cursor query(SupportSQLiteQuery supportSQLiteQuery) {
        assertNotMainThread();
        return this.mOpenHelper.getWritableDatabase().query(supportSQLiteQuery);
    }

    public SupportSQLiteStatement compileStatement(String str) {
        assertNotMainThread();
        return this.mOpenHelper.getWritableDatabase().compileStatement(str);
    }

    public void beginTransaction() {
        assertNotMainThread();
        this.mInvalidationTracker.syncTriggers();
        this.mOpenHelper.getWritableDatabase().beginTransaction();
    }

    public void endTransaction() {
        this.mOpenHelper.getWritableDatabase().endTransaction();
        if (!inTransaction()) {
            this.mInvalidationTracker.refreshVersionsAsync();
        }
    }

    public void setTransactionSuccessful() {
        this.mOpenHelper.getWritableDatabase().setTransactionSuccessful();
    }

    public void runInTransaction(Runnable runnable) {
        beginTransaction();
        try {
            runnable.run();
            setTransactionSuccessful();
        } finally {
            endTransaction();
        }
    }

    public <V> V runInTransaction(Callable<V> callable) {
        beginTransaction();
        try {
            V call = callable.call();
            setTransactionSuccessful();
            endTransaction();
            return call;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new RuntimeException("Exception in transaction", e2);
        } catch (Throwable th) {
            endTransaction();
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public void internalInitInvalidationTracker(SupportSQLiteDatabase supportSQLiteDatabase) {
        this.mInvalidationTracker.internalInit(supportSQLiteDatabase);
    }

    public InvalidationTracker getInvalidationTracker() {
        return this.mInvalidationTracker;
    }

    public boolean inTransaction() {
        return this.mOpenHelper.getWritableDatabase().inTransaction();
    }
}
