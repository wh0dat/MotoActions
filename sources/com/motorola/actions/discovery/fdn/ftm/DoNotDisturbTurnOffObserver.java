package com.motorola.actions.discovery.fdn.ftm;

import android.database.ContentObserver;
import android.os.Handler;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.MALogger;

class DoNotDisturbTurnOffObserver extends ContentObserver {
    static final int DND_NOT_TURNED_OFF = 0;
    static final int DND_TURNED_OFF = 1;
    private static final MALogger LOGGER = new MALogger(DoNotDisturbTurnOffObserver.class);

    DoNotDisturbTurnOffObserver(Handler handler) {
        super(handler);
    }

    /* access modifiers changed from: 0000 */
    public void observe() {
        LOGGER.mo11957d("Start observing Do Not Disturb turn off");
        ActionsApplication.getAppContext().getContentResolver().registerContentObserver(MotorolaSettings.getFlipToDNDFDNUri(), true, this);
    }

    /* access modifiers changed from: 0000 */
    public void stop() {
        LOGGER.mo11957d("Stop observing Do Not Disturb turn off");
        ActionsApplication.getAppContext().getContentResolver().unregisterContentObserver(this);
    }

    public void onChange(boolean z) {
        int flipToDNDFDNTurnedOffValue = MotorolaSettings.getFlipToDNDFDNTurnedOffValue(0);
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Received change on DND turned off = ");
        sb.append(flipToDNDFDNTurnedOffValue);
        mALogger.mo11957d(sb.toString());
        if (flipToDNDFDNTurnedOffValue == 1) {
            DiscoveryManager.getInstance().onFDNEvent(FeatureKey.FLIP_TO_DND);
        }
    }
}
