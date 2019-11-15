package com.motorola.actions.onenav;

final /* synthetic */ class ScreenLockNotificationManager$$Lambda$0 implements Runnable {
    private final ScreenLockNotificationManager arg$1;

    ScreenLockNotificationManager$$Lambda$0(ScreenLockNotificationManager screenLockNotificationManager) {
        this.arg$1 = screenLockNotificationManager;
    }

    public void run() {
        this.arg$1.triggerIfMotoActionsInBackground();
    }
}
