package com.motorola.actions.onenav;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import com.motorola.actions.settings.provider.ActionsSettingsProvider;
import com.motorola.actions.settings.provider.p012v2.actions.ContainerProviderItemOneNav;
import com.motorola.actions.utils.MALogger;

public class OneNavContentObserver extends ContentObserver {
    public static final MALogger LOGGER = new MALogger(OneNavContentObserver.class);

    public OneNavContentObserver(Handler handler) {
        super(handler);
    }

    public void onChange(boolean z, Uri uri) {
        boolean z2 = OneNavHelper.isOneNavPresent() && OneNavHelper.isOneNavEnabled();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("OneNav Content Observer, onChange is enabled : ");
        sb.append(z2);
        mALogger.mo11957d(sb.toString());
        ActionsSettingsProvider.notifyChange(ContainerProviderItemOneNav.TABLE_NAME);
    }
}
