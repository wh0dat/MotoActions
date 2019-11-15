package com.motorola.actions.debug.items;

import android.widget.Toast;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.SharedPreferenceManager;

public class ADSensorDebugItem extends DebugItem {
    public static final String KEY_TEST_USE_SENSOR = "key_test_use_sensor";
    public static final int SENSOR_ACCEL = 0;
    public static final int SENSOR_GYRO = 1;
    private final int mSensorId;
    private final int mToastMsgId;

    public ADSensorDebugItem(int i, int i2, int i3, int i4) {
        setTitle(getString(i2));
        setDescription(getString(i3));
        this.mSensorId = i;
        this.mToastMsgId = i4;
    }

    /* access modifiers changed from: protected */
    public void startBackgroundWork() {
        super.startBackgroundWork();
        SharedPreferenceManager.putInt(KEY_TEST_USE_SENSOR, this.mSensorId);
    }

    /* access modifiers changed from: protected */
    public void startPostExecuteWork() {
        super.startPostExecuteWork();
        Toast.makeText(ActionsApplication.getAppContext(), this.mToastMsgId, 0).show();
    }
}
