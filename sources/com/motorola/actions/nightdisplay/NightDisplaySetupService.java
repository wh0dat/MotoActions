package com.motorola.actions.nightdisplay;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import com.motorola.actions.nightdisplay.p008pd.TwilightAccess;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SettingsWrapper;
import com.motorola.actions.utils.SetupObserver;

public class NightDisplaySetupService extends Service {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(NightDisplaySetupService.class);
    private NDContentObserver mNDContentObserver;
    private final SetupObserver mSetupObserver = new SetupObserver();
    private boolean mStarted;

    private static class NDContentObserver extends ContentObserver {
        NDContentObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean z, Uri uri) {
            MALogger access$000 = NightDisplaySetupService.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("NDContentObserver:onChange() ");
            sb.append(uri.toString());
            access$000.mo11957d(sb.toString());
            super.onChange(z, uri);
            if (uri.toString().contains(TwilightAccess.SECURE_SETTINGS_NIGHT_DISPLAY_ACTIVATED)) {
                NDSecureSettingsUpdater.fixNightDisplayActivation();
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, NightDisplaySetupService.class);
    }

    public void onCreate() {
        super.onCreate();
        this.mNDContentObserver = new NDContentObserver(new Handler(Looper.getMainLooper()));
        LOGGER.mo11957d("onCreate");
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        LOGGER.mo11957d("starting...");
        if (!this.mStarted) {
            SettingsWrapper.getSecureUriFor(TwilightAccess.SECURE_SETTINGS_NIGHT_DISPLAY_ACTIVATED).ifPresent(new NightDisplaySetupService$$Lambda$0(this));
            SettingsWrapper.getSecureUriFor(NDSecureSettingsUpdater.QS_TILES).ifPresent(new NightDisplaySetupService$$Lambda$1(this));
            SettingsWrapper.getSecureUriFor(NDSecureSettingsUpdater.NIGHT_DISPLAY_AUTO_MODE).ifPresent(new NightDisplaySetupService$$Lambda$2(this));
        }
        this.mStarted = true;
        this.mSetupObserver.observe(new NightDisplaySetupService$$Lambda$3(this));
        return 1;
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$onStartCommand$0$NightDisplaySetupService() {
        getContentResolver().unregisterContentObserver(this.mNDContentObserver);
        stopSelf();
    }

    /* access modifiers changed from: private */
    /* renamed from: registerNightDisplayContentObserver */
    public void bridge$lambda$0$NightDisplaySetupService(Uri uri) {
        getContentResolver().registerContentObserver(uri, false, this.mNDContentObserver);
    }

    public void onDestroy() {
        super.onDestroy();
        LOGGER.mo11957d("onDestroy");
    }
}
