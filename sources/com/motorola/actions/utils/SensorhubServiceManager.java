package com.motorola.actions.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;
import android.os.RemoteException;
import com.motorola.actions.ActionsApplication;
import com.motorola.slpc.ISensorhubListener;
import com.motorola.slpc.ISensorhubService;
import com.motorola.slpc.ISensorhubService.Stub;

public class SensorhubServiceManager {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(SensorhubServiceManager.class);
    private static final String SLPC_PACKAGE_NAME = "com.motorola.slpc";
    private final Context mContext;
    /* access modifiers changed from: private */
    public final SensorhubUpdateListener mListener;
    private final ServiceConnection mSensorHubConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SensorhubServiceManager.this.mSensorHubService = Stub.asInterface(iBinder);
            SensorhubServiceManager.LOGGER.mo11957d("onServiceConnected");
            try {
                SensorhubServiceManager.this.mSensorHubService.addSensorhubListener(SensorhubServiceManager.this.mSensorHubListener);
            } catch (RemoteException e) {
                SensorhubServiceManager.LOGGER.mo11960e("Unable to add SensorHubListener: ", e);
            }
            if (SensorhubServiceManager.this.mListener != null) {
                SensorhubServiceManager.this.mListener.onSensorhubConnected();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            SensorhubServiceManager.LOGGER.mo11957d("onServiceDisconnected");
            SensorhubServiceManager.this.mSensorHubService = null;
            SensorhubServiceManager.this.connect();
        }
    };
    /* access modifiers changed from: private */
    public final SensorhubListener mSensorHubListener = new SensorhubListener();
    /* access modifiers changed from: private */
    public ISensorhubService mSensorHubService;

    private final class SensorhubListener extends ISensorhubListener.Stub {
        private SensorhubListener() {
        }

        public void onSensorhubReset() throws RemoteException {
            if (SensorhubServiceManager.this.mListener != null) {
                SensorhubServiceManager.this.mListener.onSensorhubReset();
            }
        }
    }

    public interface SensorhubUpdateListener {
        void onSensorhubConnected();

        void onSensorhubReset();
    }

    public SensorhubServiceManager(Context context, SensorhubUpdateListener sensorhubUpdateListener) {
        this.mContext = context;
        this.mListener = sensorhubUpdateListener;
        connect();
    }

    public int read(byte[] bArr, byte[] bArr2) {
        if (this.mSensorHubService != null) {
            try {
                return this.mSensorHubService.read(bArr, bArr2);
            } catch (RemoteException e) {
                LOGGER.mo11960e("read remote exception, ", e);
            }
        }
        return -1;
    }

    public int write(byte[] bArr, byte[] bArr2) {
        if (this.mSensorHubService != null) {
            try {
                return this.mSensorHubService.write(bArr, bArr2);
            } catch (RemoteException e) {
                LOGGER.mo11960e("write remote exception, ", e);
            }
        }
        return -1;
    }

    public void destroy() {
        disconnect();
    }

    /* access modifiers changed from: private */
    public void connect() {
        LOGGER.mo11957d("connect");
        if (this.mSensorHubService != null) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Service already bound mSensorHubService[");
            sb.append(this.mSensorHubService);
            sb.append("]");
            mALogger.mo11957d(sb.toString());
            return;
        }
        Intent intent = new Intent("com.motorola.slpc.SensorhubService.Connect");
        intent.setPackage(SLPC_PACKAGE_NAME);
        this.mContext.bindService(intent, this.mSensorHubConnection, 1);
    }

    private void disconnect() {
        LOGGER.mo11957d("disconnect");
        if (this.mSensorHubService == null) {
            LOGGER.mo11957d("Already disconnected from ISensorhubService");
            return;
        }
        try {
            this.mSensorHubService.removeSensorhubListener(this.mSensorHubListener);
        } catch (RemoteException e) {
            LOGGER.mo11960e("Unable to remove SensorHubListener - ", e);
        }
        if (this.mContext != null) {
            this.mContext.unbindService(this.mSensorHubConnection);
            this.mSensorHubService = null;
        }
    }

    public static boolean packageExists() {
        try {
            ActionsApplication.getAppContext().getPackageManager().getApplicationInfo(SLPC_PACKAGE_NAME, 1048576);
            return true;
        } catch (NameNotFoundException unused) {
            return false;
        }
    }
}
