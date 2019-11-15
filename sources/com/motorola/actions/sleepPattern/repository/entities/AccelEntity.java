package com.motorola.actions.sleepPattern.repository.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "accelerometer_logs")
public class AccelEntity {
    @ColumnInfo(name = "debug")
    private boolean mDebug;
    @ColumnInfo(name = "event_count")
    private int mEventCount;
    @PrimaryKey
    @ColumnInfo(name = "time_slot_id")
    private long mTimeSlotId;

    public AccelEntity(long j, int i, boolean z) {
        this.mTimeSlotId = j;
        this.mEventCount = i;
        this.mDebug = z;
    }

    public long getTimeSlotId() {
        return this.mTimeSlotId;
    }

    public boolean getDebug() {
        return this.mDebug;
    }

    public void setDebug(boolean z) {
        this.mDebug = z;
    }

    public int getEventCount() {
        return this.mEventCount;
    }

    public void addEventCount(int i) {
        this.mEventCount += i;
    }
}
