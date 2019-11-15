package com.motorola.actions.lts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.motorola.actions.SharedPreferenceManager;
import com.motorola.actions.utils.MALogger;

public class TTMActivatedStateChangeReceiver extends BroadcastReceiver {
    private static final String ACTION_TTM_STATE_CHANGED = "com.motorola.moto.intent.action.ACTION_CHANGED";
    private static final String EXTRA_TTM_ACTIVE = "com.motorola.moto.intent.extra.ACTION_EVENT";
    private static final MALogger LOGGER = new MALogger(TTMActivatedStateChangeReceiver.class);
    public static final String TTM_ACTIVATED_STATE = "ttm_activated_state";
    private static final String TTM_START_STATE = "start";
    private static final String TTM_STOP_STATE = "stop";

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (ACTION_TTM_STATE_CHANGED.equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra(EXTRA_TTM_ACTIVE);
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("onReceive , TTMState=");
                sb.append(stringExtra);
                mALogger.mo11957d(sb.toString());
                char c = 65535;
                int hashCode = stringExtra.hashCode();
                boolean z = false;
                if (hashCode != 3540994) {
                    if (hashCode == 109757538 && stringExtra.equals(TTM_START_STATE)) {
                        c = 0;
                    }
                } else if (stringExtra.equals(TTM_STOP_STATE)) {
                    c = 1;
                }
                switch (c) {
                    case 0:
                        z = true;
                        break;
                    case 1:
                        break;
                    default:
                        LOGGER.mo11959e("Invalid TTMState.");
                        break;
                }
                SharedPreferenceManager.putBoolean(TTM_ACTIVATED_STATE, z);
                return;
            }
            LOGGER.mo11959e("Invalid Intent received.");
            return;
        }
        LOGGER.mo11959e("NULL Intent received.");
    }
}
