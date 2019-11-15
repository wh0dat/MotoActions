package com.motorola.actions.checkin;

import android.content.SharedPreferences;
import com.motorola.actions.SharedPreferenceManager;
import java.util.Collections;
import java.util.Map;

public class CheckinDatastore {
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;
    private static final int DEFAULT_INT_VALUE = 0;
    private static final String DEFAULT_STRING_VALUE = "";
    private final String mDatastoreName;
    private final SharedPreferences mSharedPrefs = SharedPreferenceManager.getSharedPreferences(this.mDatastoreName);

    public CheckinDatastore(String str) {
        this.mDatastoreName = str;
    }

    public String getName() {
        return this.mDatastoreName;
    }

    public void resetValues() {
        if (this.mSharedPrefs != null) {
            this.mSharedPrefs.edit().clear().apply();
        }
    }

    public long getLongValue(String str, long j) {
        return this.mSharedPrefs != null ? this.mSharedPrefs.getLong(str, j) : j;
    }

    public int getIntValue(String str) {
        if (this.mSharedPrefs != null) {
            return this.mSharedPrefs.getInt(str, 0);
        }
        return 0;
    }

    public boolean getBooleanValue(String str) {
        if (this.mSharedPrefs != null) {
            return this.mSharedPrefs.getBoolean(str, false);
        }
        return false;
    }

    public String getStringValue(String str) {
        return this.mSharedPrefs != null ? this.mSharedPrefs.getString(str, "") : "";
    }

    public void setLongValue(String str, long j) {
        if (this.mSharedPrefs != null) {
            this.mSharedPrefs.edit().putLong(str, j).apply();
        }
    }

    public void setIntValue(String str, int i) {
        if (this.mSharedPrefs != null) {
            this.mSharedPrefs.edit().putInt(str, i).apply();
        }
    }

    public void setBooleanValue(String str, boolean z) {
        if (this.mSharedPrefs != null) {
            this.mSharedPrefs.edit().putBoolean(str, z).apply();
        }
    }

    public void setStringValue(String str, String str2) {
        if (this.mSharedPrefs != null) {
            this.mSharedPrefs.edit().putString(str, str2).apply();
        }
    }

    public boolean containsKey(String str) {
        if (this.mSharedPrefs != null) {
            return this.mSharedPrefs.contains(str);
        }
        return false;
    }

    public void addToIntValue(String str, int i) {
        if (this.mSharedPrefs != null) {
            setIntValue(str, this.mSharedPrefs.getInt(str, 0) + i);
        }
    }

    public void incrementIntValue(String str) {
        if (this.mSharedPrefs != null) {
            setIntValue(str, this.mSharedPrefs.getInt(str, 0) + 1);
        }
    }

    public Map<String, ?> getAll() {
        return this.mSharedPrefs != null ? this.mSharedPrefs.getAll() : Collections.emptyMap();
    }
}
