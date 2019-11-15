package android.arch.persistence.p000db;

import android.annotation.TargetApi;

@TargetApi(19)
/* renamed from: android.arch.persistence.db.SupportSQLiteProgram */
public interface SupportSQLiteProgram extends AutoCloseable {
    void bindBlob(int i, byte[] bArr);

    void bindDouble(int i, double d);

    void bindLong(int i, long j);

    void bindNull(int i);

    void bindString(int i, String str);

    void clearBindings();
}
