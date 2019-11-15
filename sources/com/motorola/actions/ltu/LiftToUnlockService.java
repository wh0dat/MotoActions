package com.motorola.actions.ltu;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemLiftToUnlock;
import com.motorola.actions.utils.MALogger;

public class LiftToUnlockService extends Service {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(LiftToUnlockService.class);
    private LiftToUnlockObserver mLiftToUnlockObserver;

    private static final class LiftToUnlockObserver extends ContentObserver {
        private boolean mRegistered;

        private LiftToUnlockObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean z, Uri uri) {
            MALogger access$300 = LiftToUnlockService.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("LTUObserver:onChange() ");
            sb.append(uri.toString());
            access$300.mo11957d(sb.toString());
            ActionsSettingsProvider.notifyChange(ContainerProviderItemLiftToUnlock.TABLE_NAME);
        }

        /* access modifiers changed from: private */
        public void register(Context context) {
            if (!this.mRegistered) {
                Uri ltuUri = MotorolaSettings.getLtuUri();
                MALogger access$300 = LiftToUnlockService.LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("LTUObserver:register() - uri:");
                sb.append(ltuUri);
                access$300.mo11957d(sb.toString());
                context.getContentResolver().registerContentObserver(ltuUri, true, this);
                Uri faceunlockUri = MotorolaSettings.getFaceunlockUri();
                MALogger access$3002 = LiftToUnlockService.LOGGER;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("LTUObserver:register() - uri:");
                sb2.append(faceunlockUri);
                access$3002.mo11957d(sb2.toString());
                context.getContentResolver().registerContentObserver(faceunlockUri, true, this);
                this.mRegistered = true;
            }
        }

        /* access modifiers changed from: private */
        public void unregister(Context context) {
            if (this.mRegistered) {
                context.getContentResolver().unregisterContentObserver(this);
                this.mRegistered = false;
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Intent createIntent() {
        return new Intent(ActionsApplication.getAppContext(), LiftToUnlockService.class);
    }

    public void onCreate() {
        super.onCreate();
        LOGGER.mo11957d("Creating LTU service");
        this.mLiftToUnlockObserver = new LiftToUnlockObserver(null);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        LOGGER.mo11957d("Starting LTU service");
        if (this.mLiftToUnlockObserver != null) {
            this.mLiftToUnlockObserver.register(this);
        }
        return 1;
    }

    public void onDestroy() {
        LOGGER.mo11957d("Destroying LTU service");
        if (this.mLiftToUnlockObserver != null) {
            this.mLiftToUnlockObserver.unregister(this);
        }
        super.onDestroy();
    }
}
