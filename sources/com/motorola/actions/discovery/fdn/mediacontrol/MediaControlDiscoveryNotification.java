package com.motorola.actions.discovery.fdn.mediacontrol;

import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.fdn.BaseDiscoveryNotification;
import com.motorola.actions.utils.NotificationActionId;
import com.motorola.actions.utils.NotificationId;

class MediaControlDiscoveryNotification extends BaseDiscoveryNotification {
    /* access modifiers changed from: protected */
    public int getBigPicture() {
        return C0504R.C0505drawable.mc_fdn;
    }

    /* access modifiers changed from: protected */
    public int getNotificationContent() {
        return C0504R.string.media_control_fdn_description;
    }

    /* access modifiers changed from: protected */
    public int getNotificationTitle() {
        return C0504R.string.media_control_fdn_title;
    }

    /* access modifiers changed from: protected */
    public int getPreviewImage() {
        return C0504R.C0505drawable.mc_fdn_preview;
    }

    MediaControlDiscoveryNotification(FeatureKey featureKey) {
        super(featureKey);
    }

    /* access modifiers changed from: protected */
    public int getFeatureFDNId() {
        return NotificationId.ACTIONS_MEDIA_CONTROL_FDN.ordinal();
    }

    /* access modifiers changed from: protected */
    public int getFDNSettingsActionId() {
        return NotificationActionId.ACTIONS_MEDIA_CONTROL_FDN_SETTINGS_ACTION.ordinal();
    }

    /* access modifiers changed from: protected */
    public int getFDNNoThanksActionId() {
        return NotificationActionId.ACTIONS_MEDIA_CONTROL_FDN_NO_THANKS_ACTION.ordinal();
    }

    /* access modifiers changed from: protected */
    public int getFDNDismissSwipeActionId() {
        return NotificationActionId.ACTIONS_MEDIA_CONTROL_FDN_DISMISS_SWIPE_ACTION.ordinal();
    }
}
