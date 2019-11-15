package com.motorola.actions.sleepPattern.data;

import android.arch.persistence.p000db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import com.motorola.actions.sleepPattern.repository.entities.PreviewEntity;
import java.util.ArrayList;
import java.util.List;

public class PreviewDao_Impl implements PreviewDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter __insertionAdapterOfPreviewEntity;
    private final SharedSQLiteStatement __preparedStmtOfCleanDatabase;

    public PreviewDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfPreviewEntity = new EntityInsertionAdapter<PreviewEntity>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `preview_results`(`type`,`results`) VALUES (?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, PreviewEntity previewEntity) {
                supportSQLiteStatement.bindLong(1, (long) previewEntity.getType());
                supportSQLiteStatement.bindLong(2, previewEntity.getResults());
            }
        };
        this.__preparedStmtOfCleanDatabase = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "delete FROM preview_results";
            }
        };
    }

    public void insertPreview(PreviewEntity... previewEntityArr) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfPreviewEntity.insert((T[]) previewEntityArr);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void cleanDatabase() {
        SupportSQLiteStatement acquire = this.__preparedStmtOfCleanDatabase.acquire();
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfCleanDatabase.release(acquire);
        }
    }

    public List<PreviewEntity> getAll() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM preview_results", 0);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("type");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow(PreviewEntity.COLUMN_RESULTS_NAME);
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new PreviewEntity(query.getInt(columnIndexOrThrow), query.getLong(columnIndexOrThrow2)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public PreviewEntity getDataByType(int i) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM preview_results WHERE type=?", 1);
        acquire.bindLong(1, (long) i);
        Cursor query = this.__db.query(acquire);
        try {
            return query.moveToFirst() ? new PreviewEntity(query.getInt(query.getColumnIndexOrThrow("type")), query.getLong(query.getColumnIndexOrThrow(PreviewEntity.COLUMN_RESULTS_NAME))) : null;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
