package com.motorola.actions.attentivedisplay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.motorola.actions.reflect.PowerManagerPrivateProxy;

public class ScreenStatusReceiver extends BroadcastReceiver {
    private final Context mContext;
    private final ScreenStatusListener mListener;

    public interface ScreenStatusListener {
        void onScreenBright();

        void onScreenDim();

        void onScreenOff(int i);

        void onScreenOn();
    }

    public ScreenStatusReceiver(Context context, ScreenStatusListener screenStatusListener) {
        this.mContext = context;
        this.mListener = screenStatusListener;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction(PowerManagerPrivateProxy.ACTION_SCREEN_OFF_REASON);
        intentFilter.addAction(PowerManagerPrivateProxy.ACTION_SCREEN_DIM);
        intentFilter.addAction(PowerManagerPrivateProxy.ACTION_SCREEN_BRIGHT);
        this.mContext.registerReceiver(this, intentFilter);
    }

    public void unregister() {
        this.mContext.unregisterReceiver(this);
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.SCREEN_ON".equals(action)) {
            this.mListener.onScreenOn();
        } else if (PowerManagerPrivateProxy.ACTION_SCREEN_OFF_REASON.equals(action)) {
            this.mListener.onScreenOff(intent.getIntExtra(PowerManagerPrivateProxy.EXTRA_SCREEN_OFF_REASON, -1));
        } else if (PowerManagerPrivateProxy.ACTION_SCREEN_DIM.equals(action)) {
            this.mListener.onScreenDim();
        } else if (PowerManagerPrivateProxy.ACTION_SCREEN_BRIGHT.equals(action)) {
            this.mListener.onScreenBright();
        }
    }
}
