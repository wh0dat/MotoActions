package com.motorola.actions.discovery.fdn.nightdisplay;

import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.nightdisplay.common.Persistence;
import com.motorola.actions.utils.NotificationActionId;
import com.motorola.actions.utils.NotificationId;

class NightDisplayDiscoveryNotification extends BaseDiscoveryNotification {
    /* access modifiers changed from: protected */
    public int getBigPicture() {
        return C0504R.C0505drawable.nd_fdn;
    }

    /* access modifiers changed from: protected */
    public int getPreviewImage() {
        return C0504R.C0505drawable.nd_fdn_preview;
    }

    NightDisplayDiscoveryNotification(FeatureKey featureKey) {
        super(featureKey);
    }

    /* access modifiers changed from: protected */
    public int getNotificationTitle() {
        return Persistence.isSleepPatternReady() ? C0504R.string.night_display_fdn_title : C0504R.string.night_display_upgrade_notification_title;
    }

    /* access modifiers changed from: protected */
    public int getNotificationContent() {
        return Persistence.isSleepPatternReady() ? C0504R.string.night_display_fdn_description : C0504R.string.night_display_upgrade_notification_description;
    }

    /* access modifiers changed from: protected */
    public int getFeatureFDNId() {
        return NotificationId.ACTIONS_NIGHT_DISPLAY_DISCOVERY.ordinal();
    }

    /* access modifiers changed from: protected */
    public int getFDNSettingsActionId() {
        return NotificationActionId.ACTIONS_NIGHT_DISPLAY_FDN_SETTINGS_ACTION.ordinal();
    }

    /* access modifiers changed from: protected */
    public int getFDNNoThanksActionId() {
        return NotificationActionId.ACTIONS_NIGHT_DISPLAY_FDN_NO_THANKS_ACTION.ordinal();
    }

    /* access modifiers changed from: protected */
    public int getFDNDismissSwipeActionId() {
        return NotificationActionId.ACTIONS_NIGHT_DISPLAY_FDN_DISMISS_SWIPE_ACTION.ordinal();
    }
}
