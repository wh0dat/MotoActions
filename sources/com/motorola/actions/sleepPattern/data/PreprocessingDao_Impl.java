package com.motorola.actions.sleepPattern.data;

import android.arch.persistence.p000db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import com.motorola.actions.sleepPattern.repository.entities.PreprocessingEntity;
import com.motorola.actions.sleepPattern.repository.entities.PreviewEntity;
import java.util.ArrayList;
import java.util.List;

public class PreprocessingDao_Impl implements PreprocessingDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter __insertionAdapterOfPreprocessingEntity;
    private final SharedSQLiteStatement __preparedStmtOfCleanDatabase;

    public PreprocessingDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfPreprocessingEntity = new EntityInsertionAdapter<PreprocessingEntity>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `preprocessing_results`(`type`,`results`) VALUES (?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, PreprocessingEntity preprocessingEntity) {
                supportSQLiteStatement.bindLong(1, (long) preprocessingEntity.getType());
                if (preprocessingEntity.getResults() == null) {
                    supportSQLiteStatement.bindNull(2);
                } else {
                    supportSQLiteStatement.bindString(2, preprocessingEntity.getResults());
                }
            }
        };
        this.__preparedStmtOfCleanDatabase = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "delete FROM preprocessing_results";
            }
        };
    }

    public void insertPreprocessing(PreprocessingEntity... preprocessingEntityArr) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfPreprocessingEntity.insert((T[]) preprocessingEntityArr);
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

    public List<PreprocessingEntity> getAll() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM preprocessing_results", 0);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("type");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow(PreviewEntity.COLUMN_RESULTS_NAME);
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new PreprocessingEntity(query.getInt(columnIndexOrThrow), query.getString(columnIndexOrThrow2)));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public PreprocessingEntity getDataByType(int i) {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM preprocessing_results WHERE type=?", 1);
        acquire.bindLong(1, (long) i);
        Cursor query = this.__db.query(acquire);
        try {
            return query.moveToFirst() ? new PreprocessingEntity(query.getInt(query.getColumnIndexOrThrow("type")), query.getString(query.getColumnIndexOrThrow(PreviewEntity.COLUMN_RESULTS_NAME))) : null;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
