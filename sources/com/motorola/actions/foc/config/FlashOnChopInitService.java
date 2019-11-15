package com.motorola.actions.foc.config;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.motorola.actions.approach.p005ir.SensorHub;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SensorhubServiceManager;
import com.motorola.actions.utils.SensorhubServiceManager.SensorhubUpdateListener;

public class FlashOnChopInitService extends Service implements SensorhubUpdateListener {
    private static final MALogger LOGGER = new MALogger(FlashOnChopInitService.class);
    private SensorhubServiceManager mSensorHubManager;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, FlashOnChopInitService.class);
    }

    private static boolean isRequired() {
        return Device.isAffinityDevice() || Device.isVectorDevice() || Device.isVertexDevice() || Device.isCopperfieldDevice() || Device.isDanteDevice();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        super.onStartCommand(intent, i, i2);
        if (isRequired()) {
            if (this.mSensorHubManager == null) {
                this.mSensorHubManager = new SensorhubServiceManager(this, this);
            }
            writeSensorHubConfig();
            return 1;
        }
        stopSelf();
        return 2;
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mSensorHubManager != null) {
            this.mSensorHubManager.destroy();
        }
    }

    public void onSensorhubReset() {
        LOGGER.mo11957d("onSensorhubReset");
        writeSensorHubConfig();
    }

    public void onSensorhubConnected() {
        LOGGER.mo11957d("onSensorhubConnected");
        writeSensorHubConfig();
    }

    private void writeSensorHubConfig() {
        if (Device.isAffinityDevice()) {
            FlashOnChopL0AccelGyroConfig flashOnChopL0AccelGyroConfig = new FlashOnChopL0AccelGyroConfig();
            flashOnChopL0AccelGyroConfig.maxGyroRotation = 939651;
            flashOnChopL0AccelGyroConfig.maxChopDuration = 350;
            flashOnChopL0AccelGyroConfig.firstAccelThresold = 40;
            flashOnChopL0AccelGyroConfig.secondAccelThresold = 38.0d;
            flashOnChopL0AccelGyroConfig.minMagnitudePercentage = 20;
            flashOnChopL0AccelGyroConfig.maxXyPercentage = 45;
            SensorHub.setChopConfig(flashOnChopL0AccelGyroConfig, FlashOnChopInitService$$Lambda$0.$instance);
        } else if (Device.isVectorDevice()) {
            FlashOnChopL4AccelGyroConfig flashOnChopL4AccelGyroConfig = new FlashOnChopL4AccelGyroConfig();
            flashOnChopL4AccelGyroConfig.maxGyroRotation = 939651;
            flashOnChopL4AccelGyroConfig.maxChopDuration = 350;
            flashOnChopL4AccelGyroConfig.firstAccelThresold = 33;
            flashOnChopL4AccelGyroConfig.secondAccelThresold = 23.0d;
            flashOnChopL4AccelGyroConfig.minMagnitudePercentage = 20;
            flashOnChopL4AccelGyroConfig.maxXyPercentage = 55;
            SensorHub.setChopConfig(flashOnChopL4AccelGyroConfig, FlashOnChopInitService$$Lambda$1.$instance);
        } else if (Device.isVertexDevice()) {
            FlashOnChopL4AccelGyroConfig flashOnChopL4AccelGyroConfig2 = new FlashOnChopL4AccelGyroConfig();
            flashOnChopL4AccelGyroConfig2.maxGyroRotation = 939651;
            flashOnChopL4AccelGyroConfig2.maxChopDuration = 350;
            flashOnChopL4AccelGyroConfig2.firstAccelThresold = 24;
            flashOnChopL4AccelGyroConfig2.secondAccelThresold = 11.0d;
            flashOnChopL4AccelGyroConfig2.minMagnitudePercentage = 20;
            flashOnChopL4AccelGyroConfig2.maxXyPercentage = 55;
            SensorHub.setChopConfig(flashOnChopL4AccelGyroConfig2, FlashOnChopInitService$$Lambda$2.$instance);
        } else if (Device.isCopperfieldDevice()) {
            FlashOnChopL0AccelGyroConfig flashOnChopL0AccelGyroConfig2 = new FlashOnChopL0AccelGyroConfig();
            flashOnChopL0AccelGyroConfig2.maxGyroRotation = 939651;
            flashOnChopL0AccelGyroConfig2.maxChopDuration = 350;
            flashOnChopL0AccelGyroConfig2.firstAccelThresold = 24;
            flashOnChopL0AccelGyroConfig2.secondAccelThresold = 30.0d;
            flashOnChopL0AccelGyroConfig2.minMagnitudePercentage = 50;
            flashOnChopL0AccelGyroConfig2.maxXyPercentage = 50;
            SensorHub.setChopConfig(flashOnChopL0AccelGyroConfig2, FlashOnChopInitService$$Lambda$3.$instance);
        } else if (Device.isDanteDevice()) {
            FlashOnChopL0AccelGyroConfig flashOnChopL0AccelGyroConfig3 = new FlashOnChopL0AccelGyroConfig();
            flashOnChopL0AccelGyroConfig3.maxGyroRotation = 939651;
            flashOnChopL0AccelGyroConfig3.maxChopDuration = 350;
            flashOnChopL0AccelGyroConfig3.firstAccelThresold = 37;
            flashOnChopL0AccelGyroConfig3.secondAccelThresold = 32.0d;
            flashOnChopL0AccelGyroConfig3.minMagnitudePercentage = 10;
            flashOnChopL0AccelGyroConfig3.maxXyPercentage = 20;
            SensorHub.setChopConfig(flashOnChopL0AccelGyroConfig3, FlashOnChopInitService$$Lambda$4.$instance);
        }
    }

    static final /* synthetic */ void lambda$writeSensorHubConfig$0$FlashOnChopInitService(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Affinity Sensor hub config done: ");
        sb.append(z ? "success" : "failed");
        mALogger.mo11957d(sb.toString());
    }

    static final /* synthetic */ void lambda$writeSensorHubConfig$1$FlashOnChopInitService(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Griffin Sensor hub config done: ");
        sb.append(z ? "success" : "failed");
        mALogger.mo11957d(sb.toString());
    }

    static final /* synthetic */ void lambda$writeSensorHubConfig$2$FlashOnChopInitService(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Vertex Sensor hub config done: ");
        sb.append(z ? "success" : "failed");
        mALogger.mo11957d(sb.toString());
    }

    static final /* synthetic */ void lambda$writeSensorHubConfig$3$FlashOnChopInitService(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Copperfield Sensor hub config done: ");
        sb.append(z ? "success" : "failed");
        mALogger.mo11957d(sb.toString());
    }

    static final /* synthetic */ void lambda$writeSensorHubConfig$4$FlashOnChopInitService(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Dante Sensor hub config done: ");
        sb.append(z ? "success" : "failed");
        mALogger.mo11957d(sb.toString());
    }
}
