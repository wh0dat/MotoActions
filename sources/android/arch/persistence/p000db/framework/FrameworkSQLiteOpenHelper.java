package android.arch.persistence.p000db.framework;

import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper.Callback;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.RequiresApi;

/* renamed from: android.arch.persistence.db.framework.FrameworkSQLiteOpenHelper */
class FrameworkSQLiteOpenHelper implements SupportSQLiteOpenHelper {
    private final OpenHelper mDelegate;

    /* renamed from: android.arch.persistence.db.framework.FrameworkSQLiteOpenHelper$OpenHelper */
    static class OpenHelper extends SQLiteOpenHelper {
        final Callback mCallback;
        final FrameworkSQLiteDatabase[] mDbRef;

        OpenHelper(Context context, String str, final FrameworkSQLiteDatabase[] frameworkSQLiteDatabaseArr, final Callback callback) {
            super(context, str, null, callback.version, new DatabaseErrorHandler() {
                public void onCorruption(SQLiteDatabase sQLiteDatabase) {
                    FrameworkSQLiteDatabase frameworkSQLiteDatabase = frameworkSQLiteDatabaseArr[0];
                    if (frameworkSQLiteDatabase != null) {
                        callback.onCorruption(frameworkSQLiteDatabase);
                    }
                }
            });
            this.mCallback = callback;
            this.mDbRef = frameworkSQLiteDatabaseArr;
        }

        /* access modifiers changed from: 0000 */
        public SupportSQLiteDatabase getWritableSupportDatabase() {
            return getWrappedDb(super.getWritableDatabase());
        }

        /* access modifiers changed from: 0000 */
        public SupportSQLiteDatabase getReadableSupportDatabase() {
            return getWrappedDb(super.getReadableDatabase());
        }

        /* access modifiers changed from: 0000 */
        public FrameworkSQLiteDatabase getWrappedDb(SQLiteDatabase sQLiteDatabase) {
            if (this.mDbRef[0] == null) {
                this.mDbRef[0] = new FrameworkSQLiteDatabase(sQLiteDatabase);
            }
            return this.mDbRef[0];
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            this.mCallback.onCreate(getWrappedDb(sQLiteDatabase));
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            this.mCallback.onUpgrade(getWrappedDb(sQLiteDatabase), i, i2);
        }

        public void onConfigure(SQLiteDatabase sQLiteDatabase) {
            this.mCallback.onConfigure(getWrappedDb(sQLiteDatabase));
        }

        public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
            this.mCallback.onDowngrade(getWrappedDb(sQLiteDatabase), i, i2);
        }

        public void onOpen(SQLiteDatabase sQLiteDatabase) {
            this.mCallback.onOpen(getWrappedDb(sQLiteDatabase));
        }

        public synchronized void close() {
            super.close();
            this.mDbRef[0] = null;
        }
    }

    FrameworkSQLiteOpenHelper(Context context, String str, Callback callback) {
        this.mDelegate = createDelegate(context, str, callback);
    }

    private OpenHelper createDelegate(Context context, String str, Callback callback) {
        return new OpenHelper(context, str, new FrameworkSQLiteDatabase[1], callback);
    }

    public String getDatabaseName() {
        return this.mDelegate.getDatabaseName();
    }

    @RequiresApi(api = 16)
    public void setWriteAheadLoggingEnabled(boolean z) {
        this.mDelegate.setWriteAheadLoggingEnabled(z);
    }

    public SupportSQLiteDatabase getWritableDatabase() {
        return this.mDelegate.getWritableSupportDatabase();
    }

    public SupportSQLiteDatabase getReadableDatabase() {
        return this.mDelegate.getReadableSupportDatabase();
    }

    public void close() {
        this.mDelegate.close();
    }
}
