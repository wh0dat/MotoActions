package com.motorola.actions.sleepPattern.data;

import android.arch.persistence.p000db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import com.motorola.actions.sleepPattern.repository.entities.AccelEntity;
import java.util.ArrayList;
import java.util.List;

public class AccelDao_Impl implements AccelDao {
    private final RoomDatabase __db;
    private final EntityInsertionAdapter __insertionAdapterOfAccelEntity;
    private final SharedSQLiteStatement __preparedStmtOfCleanDailyDatabase;
    private final EntityDeletionOrUpdateAdapter __updateAdapterOfAccelEntity;

    public AccelDao_Impl(RoomDatabase roomDatabase) {
        this.__db = roomDatabase;
        this.__insertionAdapterOfAccelEntity = new EntityInsertionAdapter<AccelEntity>(roomDatabase) {
            public String createQuery() {
                return "INSERT OR REPLACE INTO `accelerometer_logs`(`time_slot_id`,`debug`,`event_count`) VALUES (?,?,?)";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, AccelEntity accelEntity) {
                supportSQLiteStatement.bindLong(1, accelEntity.getTimeSlotId());
                supportSQLiteStatement.bindLong(2, accelEntity.getDebug() ? 1 : 0);
                supportSQLiteStatement.bindLong(3, (long) accelEntity.getEventCount());
            }
        };
        this.__updateAdapterOfAccelEntity = new EntityDeletionOrUpdateAdapter<AccelEntity>(roomDatabase) {
            public String createQuery() {
                return "UPDATE OR REPLACE `accelerometer_logs` SET `time_slot_id` = ?,`debug` = ?,`event_count` = ? WHERE `time_slot_id` = ?";
            }

            public void bind(SupportSQLiteStatement supportSQLiteStatement, AccelEntity accelEntity) {
                supportSQLiteStatement.bindLong(1, accelEntity.getTimeSlotId());
                supportSQLiteStatement.bindLong(2, accelEntity.getDebug() ? 1 : 0);
                supportSQLiteStatement.bindLong(3, (long) accelEntity.getEventCount());
                supportSQLiteStatement.bindLong(4, accelEntity.getTimeSlotId());
            }
        };
        this.__preparedStmtOfCleanDailyDatabase = new SharedSQLiteStatement(roomDatabase) {
            public String createQuery() {
                return "delete FROM accelerometer_logs WHERE debug=0";
            }
        };
    }

    public void insert(AccelEntity... accelEntityArr) {
        this.__db.beginTransaction();
        try {
            this.__insertionAdapterOfAccelEntity.insert((T[]) accelEntityArr);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void update(List<AccelEntity> list) {
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfAccelEntity.handleMultiple((Iterable<T>) list);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void update(AccelEntity... accelEntityArr) {
        this.__db.beginTransaction();
        try {
            this.__updateAdapterOfAccelEntity.handleMultiple((T[]) accelEntityArr);
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
        }
    }

    public void cleanDailyDatabase() {
        SupportSQLiteStatement acquire = this.__preparedStmtOfCleanDailyDatabase.acquire();
        this.__db.beginTransaction();
        try {
            acquire.executeUpdateDelete();
            this.__db.setTransactionSuccessful();
        } finally {
            this.__db.endTransaction();
            this.__preparedStmtOfCleanDailyDatabase.release(acquire);
        }
    }

    public List<AccelEntity> getAllDaily() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM accelerometer_logs WHERE debug=0", 0);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("time_slot_id");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow("debug");
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow("event_count");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new AccelEntity(query.getLong(columnIndexOrThrow), query.getInt(columnIndexOrThrow3), query.getInt(columnIndexOrThrow2) != 0));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public AccelEntity getSpecificDaily(long j) {
        AccelEntity accelEntity;
        boolean z = true;
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM accelerometer_logs WHERE time_slot_id=? AND debug=0", 1);
        acquire.bindLong(1, j);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("time_slot_id");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow("debug");
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow("event_count");
            if (query.moveToFirst()) {
                long j2 = query.getLong(columnIndexOrThrow);
                if (query.getInt(columnIndexOrThrow2) == 0) {
                    z = false;
                }
                accelEntity = new AccelEntity(j2, query.getInt(columnIndexOrThrow3), z);
            } else {
                accelEntity = null;
            }
            return accelEntity;
        } finally {
            query.close();
            acquire.release();
        }
    }

    public List<AccelEntity> getAll() {
        RoomSQLiteQuery acquire = RoomSQLiteQuery.acquire("SELECT * FROM accelerometer_logs", 0);
        Cursor query = this.__db.query(acquire);
        try {
            int columnIndexOrThrow = query.getColumnIndexOrThrow("time_slot_id");
            int columnIndexOrThrow2 = query.getColumnIndexOrThrow("debug");
            int columnIndexOrThrow3 = query.getColumnIndexOrThrow("event_count");
            ArrayList arrayList = new ArrayList(query.getCount());
            while (query.moveToNext()) {
                arrayList.add(new AccelEntity(query.getLong(columnIndexOrThrow), query.getInt(columnIndexOrThrow3), query.getInt(columnIndexOrThrow2) != 0));
            }
            return arrayList;
        } finally {
            query.close();
            acquire.release();
        }
    }
}
