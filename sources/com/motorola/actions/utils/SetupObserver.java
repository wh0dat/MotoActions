package com.motorola.actions.utils;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings.Secure;
import com.motorola.actions.ActionsApplication;

public class SetupObserver {
    private static final MALogger LOGGER = new MALogger(SetupObserver.class);
    private static final String USER_SETUP_COMPLETE = "user_setup_complete";
    /* access modifiers changed from: private */
    public Runnable mCallback;
    private final ContentObserver mUserSetupObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            if (SetupObserver.isSetupFinished()) {
                SetupObserver.this.mCallback.run();
                SetupObserver.this.unregisterSetupObserver();
            }
        }
    };

    public void observe(Runnable runnable) {
        this.mCallback = runnable;
        if (this.mCallback == null) {
            LOGGER.mo11959e("invalid Callback");
            unregisterSetupObserver();
        } else if (isSetupFinished()) {
            this.mCallback.run();
        } else {
            SettingsWrapper.getSecureUriFor(USER_SETUP_COMPLETE).ifPresent(new SetupObserver$$Lambda$0(this));
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$observe$0$SetupObserver(Uri uri) {
        ActionsApplication.getAppContext().getContentResolver().registerContentObserver(uri, true, this.mUserSetupObserver);
    }

    public void clean() {
        unregisterSetupObserver();
    }

    public static boolean isSetupFinished() {
        return Secure.getInt(ActionsApplication.getAppContext().getContentResolver(), USER_SETUP_COMPLETE, 0) == 1;
    }

    /* access modifiers changed from: private */
    public void unregisterSetupObserver() {
        if (this.mUserSetupObserver != null) {
            ActionsApplication.getAppContext().getContentResolver().unregisterContentObserver(this.mUserSetupObserver);
        }
    }
}
