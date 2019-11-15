package com.motorola.actions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.motorola.actions.discovery.DiscoveryManager;
import com.motorola.actions.reflect.MotorolaSettings;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.MotoAppUtils;
import com.motorola.actions.utils.PackageManagerUtils;

public class PackageStatusReceiver extends BroadcastReceiver {
    private static final MALogger LOGGER = new MALogger(PackageStatusReceiver.class);
    private static final String PACKAGE_SCHEMA = "package";
    private boolean mRegistered;

    private static class SingletonHolder {
        static final PackageStatusReceiver INSTANCE = new PackageStatusReceiver();

        private SingletonHolder() {
        }
    }

    public static PackageStatusReceiver getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getData() != null) {
            String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
            String action = intent.getAction();
            String packageName = ActionsApplication.getAppContext().getPackageName();
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Intent: ");
            sb.append(intent);
            sb.append(", package: ");
            sb.append(schemeSpecificPart);
            mALogger.mo11957d(sb.toString());
            if (TextUtils.isEmpty(schemeSpecificPart)) {
                return;
            }
            if (!"android.intent.action.PACKAGE_CHANGED".equals(action) && !"android.intent.action.PACKAGE_REMOVED".equals(action)) {
                return;
            }
            if (!MotoAppUtils.MOTO_PACKAGE_NAME.equals(schemeSpecificPart) && !packageName.equals(schemeSpecificPart)) {
                return;
            }
            if (!MotoAppUtils.isMotoEnabled() || !PackageManagerUtils.isAppEnabled(packageName)) {
                if (!MotoAppUtils.isMotoEnabled()) {
                    DiscoveryManager.getInstance().unregisterFDNListeners();
                }
                if (!PackageManagerUtils.isAppEnabled(packageName)) {
                    BootReceiver.stopAllFeatures(context);
                    MotorolaSettings.setOneNavEnabled(0, false);
                    return;
                }
                return;
            }
            BootReceiver.startAllFeatures(context, ServiceStartReason.REPLACEMENT);
        }
    }

    /* access modifiers changed from: 0000 */
    public void register() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("register - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (!this.mRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
            intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            intentFilter.addDataScheme("package");
            ActionsApplication.getAppContext().registerReceiver(this, intentFilter);
            this.mRegistered = true;
        }
    }

    public void unregister() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("unregister - mRegistered: ");
        sb.append(this.mRegistered);
        mALogger.mo11957d(sb.toString());
        if (this.mRegistered) {
            try {
                ActionsApplication.getAppContext().unregisterReceiver(this);
            } catch (IllegalArgumentException e) {
                LOGGER.mo11960e("Unable to unregister receiver.", e);
            } catch (Throwable th) {
                this.mRegistered = false;
                throw th;
            }
            this.mRegistered = false;
        }
    }
}
