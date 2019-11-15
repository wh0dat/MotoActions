package com.motorola.actions.discovery.fdn.microscreen;

import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.utils.NotificationActionId;
import com.motorola.actions.utils.NotificationId;

class MicroscreenDiscoveryNotification extends BaseDiscoveryNotification {
    /* access modifiers changed from: protected */
    public int getBigPicture() {
        return C0504R.C0505drawable.ms_fdn;
    }

    /* access modifiers changed from: protected */
    public int getNotificationContent() {
        return C0504R.string.microscreen_discovery_notification_text;
    }

    /* access modifiers changed from: protected */
    public int getNotificationTitle() {
        return C0504R.string.microscreen_discovery_notification_title;
    }

    /* access modifiers changed from: protected */
    public int getPreviewImage() {
        return C0504R.C0505drawable.ms_fdn_preview;
    }

    MicroscreenDiscoveryNotification(FeatureKey featureKey) {
        super(featureKey);
    }

    /* access modifiers changed from: protected */
    public int getFeatureFDNId() {
        return NotificationId.ACTIONS_MICROSCREEN_DISCOVERY.ordinal();
    }

    /* access modifiers changed from: protected */
    public int getFDNSettingsActionId() {
        return NotificationActionId.ACTIONS_MICROSCREEN_FDN_SETTINGS_ACTION.ordinal();
    }

    /* access modifiers changed from: protected */
    public int getFDNNoThanksActionId() {
        return NotificationActionId.ACTIONS_MICROSCREEN_FDN_NO_THANKS_ACTION.ordinal();
    }

    /* access modifiers changed from: protected */
    public int getFDNDismissSwipeActionId() {
        return NotificationActionId.ACTIONS_MICROSCREEN_FDN_DISMISS_SWIPE_ACTION.ordinal();
    }
}
