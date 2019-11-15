package com.motorola.actions.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils.SimpleStringSplitter;
import android.util.ArraySet;
import android.view.accessibility.AccessibilityManager;
import com.motorola.actions.ActionsApplication;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AccessibilityUtils {
    private static final MALogger LOGGER = new MALogger(AccessibilityUtils.class);
    private static final String SERVICE_SEPARATOR = ":";
    private static final SimpleStringSplitter STRING_COLLON_SPLITTER = new SimpleStringSplitter(SERVICE_SEPARATOR.charAt(0));

    public static void setAccessibilityServiceState(ComponentName componentName, boolean z) {
        Set<ComponentName> enabledServicesFromSettings = getEnabledServicesFromSettings();
        if (enabledServicesFromSettings.isEmpty()) {
            enabledServicesFromSettings = new ArraySet<>(1);
        }
        if (z) {
            enabledServicesFromSettings.add(componentName);
        } else {
            enabledServicesFromSettings.remove(componentName);
        }
        StringBuilder sb = new StringBuilder();
        for (ComponentName flattenToString : enabledServicesFromSettings) {
            sb.append(flattenToString.flattenToString());
            sb.append(SERVICE_SEPARATOR);
        }
        int length = sb.length();
        if (length > 0) {
            sb.deleteCharAt(length - 1);
        }
        SettingsWrapper.putSecureString("enabled_accessibility_services", sb.toString());
    }

    @NonNull
    public static Set<ComponentName> getEnabledServicesFromSettings() {
        Optional secureString = SettingsWrapper.getSecureString("enabled_accessibility_services");
        if (!secureString.isPresent()) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet();
        SimpleStringSplitter simpleStringSplitter = STRING_COLLON_SPLITTER;
        simpleStringSplitter.setString((String) secureString.get());
        while (simpleStringSplitter.hasNext()) {
            ComponentName unflattenFromString = ComponentName.unflattenFromString(simpleStringSplitter.next());
            if (unflattenFromString != null) {
                hashSet.add(unflattenFromString);
            }
        }
        return hashSet;
    }

    public static boolean isTalkbackActive() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) ActionsApplication.getAppContext().getSystemService("accessibility");
        return accessibilityManager != null && accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled();
    }

    public static void openAccessibilitySettings(@NonNull Activity activity) {
        try {
            activity.startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
        } catch (ActivityNotFoundException e) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("openAccessibilitySettings ");
            sb.append(e);
            mALogger.mo11959e(sb.toString());
        }
    }
}
