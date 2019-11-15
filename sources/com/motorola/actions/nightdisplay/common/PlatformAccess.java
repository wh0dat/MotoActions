package com.motorola.actions.nightdisplay.common;

import java.util.Calendar;

public interface PlatformAccess {
    void createRunningServiceNotification(Calendar calendar, boolean z);

    void disableTwilight();

    void enableTwilight(int i);

    int getColorTemperature();

    int getDefaultState();

    void registerNightDisplayUpdateReceiver();

    void registerNotificationActionReceiver();

    void registerScreenOnOffReceiver();

    void registerTimeStampReceiver();

    void registerUnlockReceiver();

    void registerUserChangedReceiver();

    void registerWakeUpAlarmUpdateReceiver();

    void removeRunningServiceNotification();

    void showModeUpgradeNotification();

    void unregisterNightDisplayUpdateReceiver();

    void unregisterNotificationActionReceiver();

    void unregisterScreenOnOffReceiver();

    void unregisterTimeStampReceiver();

    void unregisterUnlockReceiver();

    void unregisterUserChangedReceiver();

    void unregisterWakeUpAlarmUpdateReceiver();
}
