package com.motorola.actions.ftm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.motorola.actions.ServiceStartReason;
import com.motorola.actions.ftm.FeatureManager;
import com.motorola.actions.utils.MALogger;

public class SlpcSystemBootReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(SlpcSystemBootReceiver.class);
    private static final String SLPCSYSTEM_BOOTED_ACTION = "com.motorola.slpc.SlpcSystem.BOOTED";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (SLPCSYSTEM_BOOTED_ACTION.equals(action)) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Received intent: ");
            sb.append(action);
            sb.append(". Launching FTM Service.");
            mALogger.mo11957d(sb.toString());
            FeatureManager.start(context, ServiceStartReason.SENSORHUB_BOOT);
        }
    }
}
