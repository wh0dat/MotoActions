package com.motorola.actions.notificationchannel;

import com.motorola.actions.C0504R;

public enum ActionsNotificationChannel {
    GENERAL(C0504R.string.channel_general_title, C0504R.string.channel_general_description, 2),
    FDN(C0504R.string.channel_fdn_title, C0504R.string.channel_fdn_description, 4),
    NIGHT_DISPLAY_RUNNING(C0504R.string.channel_nd_title, C0504R.string.channel_nd_description, 2),
    RUNNING_SERVICE(C0504R.string.channel_running_service_title, C0504R.string.channel_running_service_description, 2);
    
    private final int mDescription;
    private final int mImportance;
    private final int mTitle;

    public int getTitle() {
        return this.mTitle;
    }

    public int getDescription() {
        return this.mDescription;
    }

    public int getImportance() {
        return this.mImportance;
    }

    private ActionsNotificationChannel(int i, int i2, int i3) {
        this.mTitle = i;
        this.mDescription = i2;
        this.mImportance = i3;
    }
}
