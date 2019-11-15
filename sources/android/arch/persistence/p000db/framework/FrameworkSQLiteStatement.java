package android.arch.persistence.p000db.framework;

import android.arch.persistence.p000db.SupportSQLiteStatement;
import android.database.sqlite.SQLiteStatement;

/* renamed from: android.arch.persistence.db.framework.FrameworkSQLiteStatement */
class FrameworkSQLiteStatement implements SupportSQLiteStatement {
    private final SQLiteStatement mDelegate;

    FrameworkSQLiteStatement(SQLiteStatement sQLiteStatement) {
        this.mDelegate = sQLiteStatement;
    }

    public void bindNull(int i) {
        this.mDelegate.bindNull(i);
    }

    public void bindLong(int i, long j) {
        this.mDelegate.bindLong(i, j);
    }

    public void bindDouble(int i, double d) {
        this.mDelegate.bindDouble(i, d);
    }

    public void bindString(int i, String str) {
        this.mDelegate.bindString(i, str);
    }

    public void bindBlob(int i, byte[] bArr) {
        this.mDelegate.bindBlob(i, bArr);
    }

    public void clearBindings() {
        this.mDelegate.clearBindings();
    }

    public void execute() {
        this.mDelegate.execute();
    }

    public int executeUpdateDelete() {
        return this.mDelegate.executeUpdateDelete();
    }

    public long executeInsert() {
        return this.mDelegate.executeInsert();
    }

    public long simpleQueryForLong() {
        return this.mDelegate.simpleQueryForLong();
    }

    public String simpleQueryForString() {
        return this.mDelegate.simpleQueryForString();
    }

    public void close() throws Exception {
        this.mDelegate.close();
    }
}
