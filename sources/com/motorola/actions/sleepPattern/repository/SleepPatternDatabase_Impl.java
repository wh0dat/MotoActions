package com.motorola.actions.sleepPattern.repository;

import android.arch.persistence.p000db.SupportSQLiteDatabase;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper;
import android.arch.persistence.p000db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase.Callback;
import android.arch.persistence.room.RoomMasterTable;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import com.motorola.actions.sleepPattern.data.AccelDao;
import com.motorola.actions.sleepPattern.data.AccelDao_Impl;
import com.motorola.actions.sleepPattern.data.PreprocessingDao;
import com.motorola.actions.sleepPattern.data.PreprocessingDao_Impl;
import com.motorola.actions.sleepPattern.data.PreviewDao;
import com.motorola.actions.sleepPattern.data.PreviewDao_Impl;
import com.motorola.actions.sleepPattern.repository.entities.PreviewEntity;
import java.util.HashMap;
import java.util.HashSet;

public class SleepPatternDatabase_Impl extends SleepPatternDatabase {
    private volatile AccelDao _accelDao;
    private volatile PreprocessingDao _preprocessingDao;
    private volatile PreviewDao _previewDao;

    /* access modifiers changed from: protected */
    public SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration databaseConfiguration) {
        return databaseConfiguration.sqliteOpenHelperFactory.create(Configuration.builder(databaseConfiguration.context).name(databaseConfiguration.name).callback(new RoomOpenHelper(databaseConfiguration, new Delegate(1) {
            public void createAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `accelerometer_logs` (`time_slot_id` INTEGER NOT NULL, `debug` INTEGER NOT NULL, `event_count` INTEGER NOT NULL, PRIMARY KEY(`time_slot_id`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `preprocessing_results` (`type` INTEGER NOT NULL, `results` TEXT, PRIMARY KEY(`type`))");
                supportSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS `preview_results` (`type` INTEGER NOT NULL, `results` INTEGER NOT NULL, PRIMARY KEY(`type`))");
                supportSQLiteDatabase.execSQL(RoomMasterTable.CREATE_QUERY);
                supportSQLiteDatabase.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"b7faadb4e1a61e88e604c653d9549186\")");
            }

            public void dropAllTables(SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `accelerometer_logs`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `preprocessing_results`");
                supportSQLiteDatabase.execSQL("DROP TABLE IF EXISTS `preview_results`");
            }

            /* access modifiers changed from: protected */
            public void onCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
                if (SleepPatternDatabase_Impl.this.mCallbacks != null) {
                    int size = SleepPatternDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) SleepPatternDatabase_Impl.this.mCallbacks.get(i)).onCreate(supportSQLiteDatabase);
                    }
                }
            }

            public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
                SleepPatternDatabase_Impl.this.mDatabase = supportSQLiteDatabase;
                SleepPatternDatabase_Impl.this.internalInitInvalidationTracker(supportSQLiteDatabase);
                if (SleepPatternDatabase_Impl.this.mCallbacks != null) {
                    int size = SleepPatternDatabase_Impl.this.mCallbacks.size();
                    for (int i = 0; i < size; i++) {
                        ((Callback) SleepPatternDatabase_Impl.this.mCallbacks.get(i)).onOpen(supportSQLiteDatabase);
                    }
                }
            }

            /* access modifiers changed from: protected */
            public void validateMigration(SupportSQLiteDatabase supportSQLiteDatabase) {
                HashMap hashMap = new HashMap(3);
                hashMap.put("time_slot_id", new Column("time_slot_id", "INTEGER", true, 1));
                hashMap.put("debug", new Column("debug", "INTEGER", true, 0));
                hashMap.put("event_count", new Column("event_count", "INTEGER", true, 0));
                TableInfo tableInfo = new TableInfo("accelerometer_logs", hashMap, new HashSet(0), new HashSet(0));
                TableInfo read = TableInfo.read(supportSQLiteDatabase, "accelerometer_logs");
                if (!tableInfo.equals(read)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Migration didn't properly handle accelerometer_logs(com.motorola.actions.sleepPattern.repository.entities.AccelEntity).\n Expected:\n");
                    sb.append(tableInfo);
                    sb.append("\n");
                    sb.append(" Found:\n");
                    sb.append(read);
                    throw new IllegalStateException(sb.toString());
                }
                HashMap hashMap2 = new HashMap(2);
                hashMap2.put("type", new Column("type", "INTEGER", true, 1));
                hashMap2.put(PreviewEntity.COLUMN_RESULTS_NAME, new Column(PreviewEntity.COLUMN_RESULTS_NAME, "TEXT", false, 0));
                TableInfo tableInfo2 = new TableInfo("preprocessing_results", hashMap2, new HashSet(0), new HashSet(0));
                TableInfo read2 = TableInfo.read(supportSQLiteDatabase, "preprocessing_results");
                if (!tableInfo2.equals(read2)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Migration didn't properly handle preprocessing_results(com.motorola.actions.sleepPattern.repository.entities.PreprocessingEntity).\n Expected:\n");
                    sb2.append(tableInfo2);
                    sb2.append("\n");
                    sb2.append(" Found:\n");
                    sb2.append(read2);
                    throw new IllegalStateException(sb2.toString());
                }
                HashMap hashMap3 = new HashMap(2);
                hashMap3.put("type", new Column("type", "INTEGER", true, 1));
                hashMap3.put(PreviewEntity.COLUMN_RESULTS_NAME, new Column(PreviewEntity.COLUMN_RESULTS_NAME, "INTEGER", true, 0));
                TableInfo tableInfo3 = new TableInfo(PreviewEntity.TABLE_NAME, hashMap3, new HashSet(0), new HashSet(0));
                TableInfo read3 = TableInfo.read(supportSQLiteDatabase, PreviewEntity.TABLE_NAME);
                if (!tableInfo3.equals(read3)) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Migration didn't properly handle preview_results(com.motorola.actions.sleepPattern.repository.entities.PreviewEntity).\n Expected:\n");
                    sb3.append(tableInfo3);
                    sb3.append("\n");
                    sb3.append(" Found:\n");
                    sb3.append(read3);
                    throw new IllegalStateException(sb3.toString());
                }
            }
        }, "b7faadb4e1a61e88e604c653d9549186")).build());
    }

    /* access modifiers changed from: protected */
    public InvalidationTracker createInvalidationTracker() {
        return new InvalidationTracker(this, "accelerometer_logs", "preprocessing_results", PreviewEntity.TABLE_NAME);
    }

    public AccelDao accelDao() {
        AccelDao accelDao;
        if (this._accelDao != null) {
            return this._accelDao;
        }
        synchronized (this) {
            if (this._accelDao == null) {
                this._accelDao = new AccelDao_Impl(this);
            }
            accelDao = this._accelDao;
        }
        return accelDao;
    }

    public PreprocessingDao preprocessingDao() {
        PreprocessingDao preprocessingDao;
        if (this._preprocessingDao != null) {
            return this._preprocessingDao;
        }
        synchronized (this) {
            if (this._preprocessingDao == null) {
                this._preprocessingDao = new PreprocessingDao_Impl(this);
            }
            preprocessingDao = this._preprocessingDao;
        }
        return preprocessingDao;
    }

    public PreviewDao previewDao() {
        PreviewDao previewDao;
        if (this._previewDao != null) {
            return this._previewDao;
        }
        synchronized (this) {
            if (this._previewDao == null) {
                this._previewDao = new PreviewDao_Impl(this);
            }
            previewDao = this._previewDao;
        }
        return previewDao;
    }
}
