package com.motorola.actions.zenmode;

import android.annotation.SuppressLint;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import com.motorola.actions.ActionsApplication;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.SettingsWrapper;
import java.util.ArrayList;
import java.util.List;

public final class AutomaticRulesListener implements AutomaticRulesConfigSubject {
    private static final int HANDLER_MSG_NOTIFY_ZEN_MODE = 3;
    private static final int HANDLER_MSG_REGISTER_ZEN_MODE = 1;
    private static final int HANDLER_MSG_UNREGISTER_ZEN_MODE = 2;
    private static final String KEY_ZEN_MODE_SYSTEM_CONFIG = "zen_mode_config_etag";
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(AutomaticRulesListener.class);
    private static AutomaticRulesListener sInstance;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    if (message.obj != null && (message.obj instanceof AutomaticRulesConfigObserver)) {
                        AutomaticRulesListener.LOGGER.mo11957d("HANDLER_MSG_REGISTER_ZEN_MODE received");
                        AutomaticRulesListener.this.addObserver((AutomaticRulesConfigObserver) message.obj);
                        return;
                    }
                    return;
                case 2:
                    if (message.obj != null && (message.obj instanceof AutomaticRulesConfigObserver)) {
                        AutomaticRulesListener.LOGGER.mo11957d("HANDLER_MSG_UNREGISTER_ZEN_MODE received");
                        AutomaticRulesListener.this.removeObserver((AutomaticRulesConfigObserver) message.obj);
                        return;
                    }
                    return;
                case 3:
                    AutomaticRulesListener.LOGGER.mo11957d("HANDLER_MSG_NOTIFY_ZEN_MODE received");
                    AutomaticRulesListener.this.updateObservers();
                    return;
                default:
                    AutomaticRulesListener.LOGGER.mo11959e("Invalid message");
                    return;
            }
        }
    };
    private List<AutomaticRulesConfigObserver> mObservers = new ArrayList();
    private ZenModeConfigObserver mZenModeConfigObserver;

    private final class ZenModeConfigObserver extends ContentObserver {
        private ZenModeConfigObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean z) {
            super.onChange(z);
            AutomaticRulesListener.this.notifyObservers();
        }
    }

    public static synchronized AutomaticRulesListener getInstance() {
        AutomaticRulesListener automaticRulesListener;
        synchronized (AutomaticRulesListener.class) {
            if (sInstance == null) {
                sInstance = new AutomaticRulesListener();
            }
            automaticRulesListener = sInstance;
        }
        return automaticRulesListener;
    }

    @SuppressLint({"HandlerLeak"})
    private AutomaticRulesListener() {
    }

    public void register(AutomaticRulesConfigObserver automaticRulesConfigObserver) {
        sendMessage(1, automaticRulesConfigObserver);
    }

    public void unregister(AutomaticRulesConfigObserver automaticRulesConfigObserver) {
        sendMessage(2, automaticRulesConfigObserver);
    }

    public void notifyObservers() {
        sendMessage(3);
    }

    /* access modifiers changed from: private */
    public void addObserver(AutomaticRulesConfigObserver automaticRulesConfigObserver) {
        try {
            if (this.mObservers.isEmpty()) {
                registerZenModeConfigObserver();
            }
            if (!this.mObservers.contains(automaticRulesConfigObserver)) {
                this.mObservers.add(automaticRulesConfigObserver);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            LOGGER.mo11957d("NullPointerException registering Automatic Rules Observer");
        }
    }

    /* access modifiers changed from: private */
    public void removeObserver(AutomaticRulesConfigObserver automaticRulesConfigObserver) {
        this.mObservers.remove(automaticRulesConfigObserver);
        if (this.mObservers.isEmpty()) {
            unregisterZenModeConfigObserver();
        }
    }

    /* access modifiers changed from: private */
    public void updateObservers() {
        for (AutomaticRulesConfigObserver onAutomaticRulesUpdate : this.mObservers) {
            onAutomaticRulesUpdate.onAutomaticRulesUpdate();
        }
    }

    private void registerZenModeConfigObserver() {
        if (this.mZenModeConfigObserver == null) {
            LOGGER.mo11957d("registering ZenModeConfigObserver");
            this.mZenModeConfigObserver = new ZenModeConfigObserver(new Handler());
            SettingsWrapper.getGlobalUriFor(KEY_ZEN_MODE_SYSTEM_CONFIG).ifPresent(new AutomaticRulesListener$$Lambda$0(this));
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$registerZenModeConfigObserver$0$AutomaticRulesListener(Uri uri) {
        ActionsApplication.getAppContext().getContentResolver().registerContentObserver(uri, false, this.mZenModeConfigObserver);
    }

    private void unregisterZenModeConfigObserver() {
        if (this.mZenModeConfigObserver != null) {
            LOGGER.mo11957d("unregistering ZenModeConfigObserver");
            ActionsApplication.getAppContext().getContentResolver().unregisterContentObserver(this.mZenModeConfigObserver);
            this.mZenModeConfigObserver = null;
        }
    }

    private void sendMessage(int i) {
        if (this.mHandler != null) {
            Message obtainMessage = this.mHandler.obtainMessage();
            obtainMessage.what = i;
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    private void sendMessage(int i, Object obj) {
        if (this.mHandler != null) {
            Message obtainMessage = this.mHandler.obtainMessage();
            obtainMessage.what = i;
            obtainMessage.obj = obj;
            this.mHandler.sendMessage(obtainMessage);
        }
    }
}
