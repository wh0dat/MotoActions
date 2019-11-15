package com.motorola.actions.mediacontrol.instrumentation;

import android.content.Context;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.checkin.BaseAnalytics;
import com.motorola.actions.checkin.CheckinDatastore;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.mediacontrol.MediaControlService;
import com.motorola.actions.reflect.CheckinEventProxy;
import com.motorola.actions.settings.updater.MediaControlSettingsUpdater;

public class MediaControlAnalytics extends BaseAnalytics {
    private static final String CHECKIN_TAG = "MOT_MEDIA_CONTROL";
    private static final String DAILY_CHECKIN_VERSION = "1.0";
    private static final String DATASTORE_NAME = "actions_media_control";
    private static final String KEY_DAILY_TOTAL_MUSICS_CHANGED = "n_change";

    public String getDatastoreName() {
        return DATASTORE_NAME;
    }

    public MediaControlAnalytics(Context context) {
        super(context, CHECKIN_TAG, "1.0");
        addDatastore(DATASTORE_NAME);
    }

    /* access modifiers changed from: protected */
    public void populateDailyCheckinEvent(CheckinEventProxy checkinEventProxy, String str, long j) {
        CheckinDatastore checkinDatastore = (CheckinDatastore) this.mDatastores.get(DATASTORE_NAME);
        CommonCheckinAttributes.addCommonDailyAttributes(checkinEventProxy, isFeatureEnabled(), MediaControlSettingsUpdater.getInstance().getEnabledSource(FeatureKey.QUICK_SCREENSHOT.getEnableDefaultState()));
        setOptionalIntAttribute(checkinDatastore, checkinEventProxy, KEY_DAILY_TOTAL_MUSICS_CHANGED);
    }

    /* access modifiers changed from: protected */
    public boolean isFeatureSupported() {
        return MediaControlService.isFeatureSupported();
    }

    public boolean isFeatureEnabled() {
        return MediaControlService.isServiceEnabled();
    }

    /* access modifiers changed from: 0000 */
    public synchronized void recordMusicChangeEvent() {
        ((CheckinDatastore) this.mDatastores.get(DATASTORE_NAME)).incrementIntValue(KEY_DAILY_TOTAL_MUSICS_CHANGED);
    }
}
