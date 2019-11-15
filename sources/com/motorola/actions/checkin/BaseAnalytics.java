package com.motorola.actions.checkin;

import android.app.AlarmManager;
import android.content.Context;
import android.text.TextUtils;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.utils.MALogger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public abstract class BaseAnalytics {
    private static final String DEFAULT_DAILY_CHECKIN_EVENT = "DailyStats";
    private static final String KEY_CHECKIN_VERSION = "checkin_version";
    private static final String KEY_LAST_CHECKIN_TIME = "last_checkin_time";
    private static final MALogger LOGGER = new MALogger(BaseAnalytics.class);
    private static final long MAX_CHECKIN_DELTA_MS = 172800000;
    private static final long MIN_MINUTES_BETWEEN_DAILY = (TimeUnit.HOURS.toMinutes(23) + 45);
    protected final AlarmManager mAlarmManager;
    private final String mCheckinTag;
    protected final Context mContext;
    private final String mDailyCheckinEventName;
    private final String mDailyCheckinVersion;
    protected final Map<String, CheckinDatastore> mDatastores;

    public static int capValue(int i, int i2) {
        return i > i2 + -1 ? i2 : i;
    }

    public static long capValue(long j, long j2) {
        return j > j2 - 1 ? j2 : j;
    }

    public abstract String getDatastoreName();

    public abstract boolean isFeatureEnabled();

    /* access modifiers changed from: protected */
    public abstract boolean isFeatureSupported();

    /* access modifiers changed from: protected */
    public abstract void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j);

    public void updateDailyInformation() {
    }

    public BaseAnalytics(Context context, String str, String str2, String str3) {
        this.mContext = context;
        this.mDatastores = new HashMap();
        this.mDailyCheckinEventName = str2;
        this.mCheckinTag = str;
        this.mDailyCheckinVersion = str3;
        this.mAlarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
    }

    public BaseAnalytics(Context context, String str, String str2) {
        this(context, str, DEFAULT_DAILY_CHECKIN_EVENT, str2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0052, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void addDatastore(java.lang.String r5) {
        /*
            r4 = this;
            monitor-enter(r4)
            java.util.Map<java.lang.String, com.motorola.actions.checkin.CheckinDatastore> r0 = r4.mDatastores     // Catch:{ all -> 0x0053 }
            boolean r0 = r0.containsKey(r5)     // Catch:{ all -> 0x0053 }
            if (r0 == 0) goto L_0x0026
            com.motorola.actions.utils.MALogger r0 = LOGGER     // Catch:{ all -> 0x0053 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0053 }
            r1.<init>()     // Catch:{ all -> 0x0053 }
            java.lang.String r2 = "Datastore "
            r1.append(r2)     // Catch:{ all -> 0x0053 }
            r1.append(r5)     // Catch:{ all -> 0x0053 }
            java.lang.String r5 = " already exists!"
            r1.append(r5)     // Catch:{ all -> 0x0053 }
            java.lang.String r5 = r1.toString()     // Catch:{ all -> 0x0053 }
            r0.mo11963w(r5)     // Catch:{ all -> 0x0053 }
            monitor-exit(r4)
            return
        L_0x0026:
            com.motorola.actions.checkin.CheckinDatastore r0 = new com.motorola.actions.checkin.CheckinDatastore     // Catch:{ all -> 0x0053 }
            r0.<init>(r5)     // Catch:{ all -> 0x0053 }
            java.util.Map<java.lang.String, com.motorola.actions.checkin.CheckinDatastore> r1 = r4.mDatastores     // Catch:{ all -> 0x0053 }
            r1.put(r5, r0)     // Catch:{ all -> 0x0053 }
            java.lang.String r1 = r4.mDailyCheckinVersion     // Catch:{ all -> 0x0053 }
            java.lang.String r2 = "checkin_version"
            java.lang.String r2 = r0.getStringValue(r2)     // Catch:{ all -> 0x0053 }
            boolean r1 = r1.equals(r2)     // Catch:{ all -> 0x0053 }
            if (r1 == 0) goto L_0x004a
            java.lang.String r1 = "last_checkin_time"
            r2 = -1
            long r0 = r0.getLongValue(r1, r2)     // Catch:{ all -> 0x0053 }
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x0051
        L_0x004a:
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ all -> 0x0053 }
            r4.reset(r5, r0)     // Catch:{ all -> 0x0053 }
        L_0x0051:
            monitor-exit(r4)
            return
        L_0x0053:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.checkin.BaseAnalytics.addDatastore(java.lang.String):void");
    }

    public synchronized CheckinDatastore getDatastore(String str) {
        return (CheckinDatastore) this.mDatastores.get(str);
    }

    private synchronized void reset(String str, long j) {
        assertDatastoreExists(str);
        CheckinDatastore checkinDatastore = (CheckinDatastore) this.mDatastores.get(str);
        checkinDatastore.resetValues();
        checkinDatastore.setLongValue(KEY_LAST_CHECKIN_TIME, j);
        checkinDatastore.setStringValue(KEY_CHECKIN_VERSION, this.mDailyCheckinVersion);
    }

    public synchronized void performDailyCheckin() {
        long currentTimeMillis = System.currentTimeMillis();
        LOGGER.mo11957d("Beginning daily checkin.");
        HashSet hashSet = new HashSet();
        try {
            CheckinEventProxy checkinEventProxy = new CheckinEventProxy(this.mCheckinTag, this.mDailyCheckinEventName, this.mDailyCheckinVersion, currentTimeMillis);
            for (String str : this.mDatastores.keySet()) {
                long longValue = getDatastore(str).getLongValue(KEY_LAST_CHECKIN_TIME, -1);
                if (!(longValue == -1)) {
                    long min = Math.min(MAX_CHECKIN_DELTA_MS, Math.max(0, currentTimeMillis - longValue));
                    if (TimeUnit.MILLISECONDS.toMinutes(min) > MIN_MINUTES_BETWEEN_DAILY) {
                        populateDailyCheckinEvent(checkinEventProxy, str, min);
                        hashSet.add(str);
                    }
                } else {
                    reset(str, currentTimeMillis);
                }
            }
            if (!hashSet.isEmpty()) {
                checkinEventProxy.publish(this.mContext.getContentResolver());
            }
        } catch (Throwable th) {
            LOGGER.mo11965w(th);
        }
        Iterator it = hashSet.iterator();
        while (it.hasNext()) {
            reset((String) it.next(), currentTimeMillis);
        }
    }

    public void publishCheckinData(CheckinData checkinData) {
        try {
            CheckinEventProxy checkinEventProxy = new CheckinEventProxy(this.mCheckinTag, checkinData.getTag(), checkinData.getVersion(), checkinData.getTime());
            for (Entry entry : checkinData.getIntEntries()) {
                checkinEventProxy.setValue((String) entry.getKey(), ((Integer) entry.getValue()).intValue());
            }
            for (Entry entry2 : checkinData.getStringEntries()) {
                checkinEventProxy.setValue((String) entry2.getKey(), (String) entry2.getValue());
            }
            for (Entry entry3 : checkinData.getBooleanEntries()) {
                checkinEventProxy.setValue((String) entry3.getKey(), ((Boolean) entry3.getValue()).booleanValue());
            }
            for (Entry entry4 : checkinData.getDoubleEntries()) {
                checkinEventProxy.setValue((String) entry4.getKey(), ((Double) entry4.getValue()).doubleValue());
            }
            checkinEventProxy.publish(this.mContext.getContentResolver());
        } catch (Throwable th) {
            LOGGER.mo11960e("Error publishing checkin event: ", th);
        }
    }

    private void assertDatastoreExists(String str) {
        if (!this.mDatastores.containsKey(str)) {
            throw new IllegalArgumentException(String.format("Datastore %s does not exist.", new Object[]{str}));
        }
    }

    public void updateEnabledStatus() {
        ((CheckinDatastore) this.mDatastores.get(getDatastoreName())).setBooleanValue(CommonCheckinAttributes.KEY_ENABLED, isFeatureEnabled());
    }

    /* access modifiers changed from: protected */
    public void setOptionalIntAttribute(CheckinDatastore checkinDatastore, CheckinEventProxy checkinEventProxy, String str) {
        int intValue = checkinDatastore.getIntValue(str);
        if (intValue > 0) {
            checkinEventProxy.setValue(str, intValue);
        }
    }

    /* access modifiers changed from: protected */
    public void setOptionalBooleanAttribute(CheckinDatastore checkinDatastore, CheckinEventProxy checkinEventProxy, String str) {
        if (checkinDatastore.containsKey(str)) {
            checkinEventProxy.setValue(str, checkinDatastore.getBooleanValue(str));
        }
    }

    /* access modifiers changed from: protected */
    public void setOptionalStringAttribute(CheckinDatastore checkinDatastore, CheckinEventProxy checkinEventProxy, String str) {
        String stringValue = checkinDatastore.getStringValue(str);
        if (!TextUtils.isEmpty(stringValue)) {
            checkinEventProxy.setValue(str, stringValue);
        }
    }
}
