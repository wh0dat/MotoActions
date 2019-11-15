package com.motorola.actions.checkin;

import android.support.annotation.NonNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CheckinData implements Serializable, CheckinContainer {
    private static final long serialVersionUID = 1466650338708051580L;
    private final Map<String, Boolean> mBooleanData = new HashMap();
    private final Map<String, Double> mDoubleData = new HashMap();
    private final Map<String, Integer> mIntData = new HashMap();
    private final Map<String, String> mStringData = new HashMap();
    private final String mTag;
    private final long mTime;
    private final String mVersion;

    public CheckinData(String str, String str2, long j) {
        this.mTag = str;
        this.mVersion = str2;
        this.mTime = j;
    }

    public void setValue(@NonNull String str, int i) {
        this.mIntData.put(str, Integer.valueOf(i));
    }

    public void setValue(@NonNull String str, String str2) {
        this.mStringData.put(str, str2);
    }

    public void setValue(@NonNull String str, boolean z) {
        this.mBooleanData.put(str, Boolean.valueOf(z));
    }

    public void setValue(@NonNull String str, double d) {
        this.mDoubleData.put(str, Double.valueOf(d));
    }

    public String getTag() {
        return this.mTag;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public long getTime() {
        return this.mTime;
    }

    public Set<Entry<String, Integer>> getIntEntries() {
        return this.mIntData.entrySet();
    }

    public Set<Entry<String, String>> getStringEntries() {
        return this.mStringData.entrySet();
    }

    public Set<Entry<String, Boolean>> getBooleanEntries() {
        return this.mBooleanData.entrySet();
    }

    public Set<Entry<String, Double>> getDoubleEntries() {
        return this.mDoubleData.entrySet();
    }
}
