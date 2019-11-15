package com.motorola.actions.discovery.fdn.nightdisplay.sleeppattern;

import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.utils.NotificationActionId;
import com.motorola.actions.utils.NotificationId;

class SleepPatternDiscoveryNotification extends BaseDiscoveryNotification {
    /* access modifiers changed from: protected */
    public int getBigPicture() {
        return C0504R.C0505drawable.nd_fdn;
    }

    /* access modifiers changed from: protected */
    public int getNotificationContent() {
        return C0504R.string.night_display_upgrade_notification_description;
    }

    /* access modifiers changed from: protected */
    public int getNotificationTitle() {
        return C0504R.string.night_display_upgrade_notification_title;
    }

    /* access modifiers changed from: protected */
    public int getPreviewImage() {
        return C0504R.C0505drawable.nd_fdn_preview;
    }

    SleepPatternDiscoveryNotification(FeatureKey featureKey) {
        super(featureKey);
    }

    /* access modifiers changed from: protected */
    public int getFeatureFDNId() {
        return NotificationId.ACTIONS_SLEEP_PATTERN_DISCOVERY.ordinal();
    }

    /* access modifiers changed from: protected */
    public int getFDNSettingsActionId() {
        return NotificationActionId.ACTIONS_SLEEP_PATTERN_FDN_SETTINGS_ACTION.ordinal();
    }

    /* access modifiers changed from: protected */
    public int getFDNNoThanksActionId() {
        return NotificationActionId.ACTIONS_SLEEP_PATTERN_FDN_NO_THANKS_ACTION.ordinal();
    }

    /* access modifiers changed from: protected */
    public int getFDNDismissSwipeActionId() {
        return NotificationActionId.ACTIONS_SLEEP_PATTERN_FDN_DISMISS_SWIPE_ACTION.ordinal();
    }
}
