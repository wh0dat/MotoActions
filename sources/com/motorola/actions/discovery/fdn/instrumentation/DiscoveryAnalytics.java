package com.motorola.actions.discovery.fdn.instrumentation;

import android.content.Context;
import android.text.TextUtils;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinData;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.utils.Constants;
import com.motorola.actions.utils.MALogger;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DiscoveryAnalytics extends BaseAnalytics {
    static final String CHANGE_DEFAULT_STATE = "change";
    private static final String CHECKIN_TAG = "MOT_FDN";
    private static final String DATASTORE_NAME = "actions_discovery";
    static final String DISMISS_NOTIFICATION_NO_THANKS = "no_thks";
    static final String DISMISS_NOTIFICATION_SWIPE = "swipe";
    private static final String INSTANCE_CHECKIN_VERSION = "1.2";
    private static final MALogger LOGGER = new MALogger(DiscoveryAnalytics.class);
    static final String NOTIFICATION_CLICKED = "click";
    static final String NOTIFICATION_TRY_IT = "try_it";
    static final String SHOW_NOTIFICATION = "show";

    @Retention(RetentionPolicy.SOURCE)
    @interface ValueInstanceEnrollFDN {
    }

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public boolean isFeatureEnabled() {
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureSupported() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
    }

    DiscoveryAnalytics(Context context) {
        super(context, CHECKIN_TAG, "1.0");
        addDatastore(DATASTORE_NAME);
    }

    /* access modifiers changed from: 0000 */
    public synchronized void publishInstanceMotFDN(String str, String str2) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            CheckinData checkinData = new CheckinData(Constants.INSTRUMENTATION_INSTANCE_TAG, INSTANCE_CHECKIN_VERSION, System.currentTimeMillis());
            checkinData.setValue("feature", str);
            checkinData.setValue("enroll", str2);
            CommonCheckinAttributes.addApkVer(checkinData);
            publishCheckinData(checkinData);
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("publishInstanceMotFDN() - feature = ");
            sb.append(str);
            sb.append(", enroll = ");
            sb.append(str2);
            mALogger.mo11957d(sb.toString());
        }
    }
}
