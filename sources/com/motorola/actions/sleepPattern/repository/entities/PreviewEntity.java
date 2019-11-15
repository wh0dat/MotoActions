package com.motorola.actions.sleepPattern.repository.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "preview_results")
public class PreviewEntity {
    @Ignore
    public static final String COLUMN_RESULTS_NAME = "results";
    @Ignore
    public static final String TABLE_NAME = "preview_results";
    @ColumnInfo(name = "results")
    private long mResults;
    @PrimaryKey
    @ColumnInfo(name = "type")
    private int mType;

    public PreviewEntity(int i, long j) {
        this.mType = i;
        this.mResults = j;
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int i) {
        this.mType = i;
    }

    public long getResults() {
        return this.mResults;
    }

    public void setResults(long j) {
        this.mResults = j;
    }
}
