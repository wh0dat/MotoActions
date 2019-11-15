package com.motorola.actions.utils;

import android.net.Uri;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import com.motorola.actions.ActionsApplication;
import java.util.Optional;

public class SettingsWrapper {
    private static final MALogger LOGGER = new MALogger(SettingsWrapper.class);

    public static Optional<Integer> getSecureInt(String str) {
        Optional optional;
        try {
            optional = Optional.of(Integer.valueOf(Secure.getInt(ActionsApplication.getAppContext().getContentResolver(), str)));
            try {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("getSecureInt ");
                sb.append(str);
                sb.append("=");
                sb.append(optional.get());
                mALogger.mo11957d(sb.toString());
            } catch (SettingNotFoundException | SecurityException e) {
                e = e;
            }
        } catch (SettingNotFoundException | SecurityException e2) {
            e = e2;
            optional = Optional.empty();
            LOGGER.mo11960e("Exception", e);
            return optional;
        }
        return optional;
    }

    public static Optional<Integer> getSecureInt(String str, boolean z) {
        Optional optional;
        try {
            optional = Optional.of(Integer.valueOf(Secure.getInt(ActionsApplication.getAppContext().getContentResolver(), str)));
            try {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("getSecureInt ");
                sb.append(str);
                sb.append(" = ");
                sb.append(optional.get());
                mALogger.mo11957d(sb.toString());
            } catch (SettingNotFoundException | SecurityException e) {
                e = e;
            }
        } catch (SettingNotFoundException | SecurityException e2) {
            e = e2;
            optional = Optional.empty();
            if (z) {
                LOGGER.mo11958d("Exception", e);
            } else {
                LOGGER.mo11960e("Exception", e);
            }
            return optional;
        }
        return optional;
    }

    public static int getSecureInt(String str, int i) {
        int i2;
        try {
            i2 = Secure.getInt(ActionsApplication.getAppContext().getContentResolver(), str);
            try {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("getSecureInt ");
                sb.append(str);
                sb.append("=");
                sb.append(i2);
                mALogger.mo11957d(sb.toString());
            } catch (SettingNotFoundException | SecurityException e) {
                e = e;
            }
        } catch (SettingNotFoundException | SecurityException e2) {
            e = e2;
            i2 = i;
            LOGGER.mo11960e("Exception", e);
            return i2;
        }
        return i2;
    }

    public static boolean putSecureInt(String str, int i) {
        boolean z;
        try {
            z = Secure.putInt(ActionsApplication.getAppContext().getContentResolver(), str, i);
            try {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("putSecureInt ");
                sb.append(str);
                sb.append("=");
                sb.append(z);
                mALogger.mo11957d(sb.toString());
            } catch (SecurityException e) {
                e = e;
            }
        } catch (SecurityException e2) {
            e = e2;
            z = false;
            LOGGER.mo11960e("SecurityException", e);
            return z;
        }
        return z;
    }

    public static Optional<String> getSecureString(String str) {
        Optional<String> empty = Optional.empty();
        try {
            String string = Secure.getString(ActionsApplication.getAppContext().getContentResolver(), str);
            if (string != null) {
                Optional<String> of = Optional.of(string);
                try {
                    MALogger mALogger = LOGGER;
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append("=");
                    sb.append(of);
                    mALogger.mo11957d(sb.toString());
                    return of;
                } catch (SecurityException e) {
                    e = e;
                    empty = of;
                    LOGGER.mo11960e("SecurityException", e);
                    return empty;
                }
            } else {
                LOGGER.mo11959e("Settings received is null");
                return empty;
            }
        } catch (SecurityException e2) {
            e = e2;
        }
    }

    public static boolean putSecureString(String str, String str2) {
        boolean z;
        try {
            z = Secure.putString(ActionsApplication.getAppContext().getContentResolver(), str, str2);
            try {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append("=");
                sb.append(z);
                mALogger.mo11957d(sb.toString());
            } catch (SecurityException e) {
                e = e;
            }
        } catch (SecurityException e2) {
            e = e2;
            z = false;
            LOGGER.mo11960e("SecurityException", e);
            return z;
        }
        return z;
    }

    public static Optional<Uri> getSecureUriFor(String str) {
        Optional<Uri> empty = Optional.empty();
        try {
            Uri uriFor = Secure.getUriFor(str);
            if (uriFor != null) {
                return Optional.of(uriFor);
            }
            LOGGER.mo11959e("Settings received is null");
            return empty;
        } catch (SecurityException e) {
            LOGGER.mo11960e("SecurityException", e);
            return empty;
        }
    }

    public static int getGlobalInt(String str) {
        int i;
        try {
            i = Global.getInt(ActionsApplication.getAppContext().getContentResolver(), str);
            try {
                MALogger mALogger = LOGGER;
                StringBuilder sb = new StringBuilder();
                sb.append("getGlobalInt ");
                sb.append(str);
                sb.append("=");
                sb.append(i);
                mALogger.mo11957d(sb.toString());
            } catch (SettingNotFoundException | SecurityException e) {
                e = e;
            }
        } catch (SettingNotFoundException | SecurityException e2) {
            e = e2;
            i = 0;
            LOGGER.mo11960e("Exception", e);
            return i;
        }
        return i;
    }

    public static Optional<Uri> getGlobalUriFor(String str) {
        Optional<Uri> empty = Optional.empty();
        try {
            Uri uriFor = Global.getUriFor(str);
            if (uriFor != null) {
                return Optional.of(uriFor);
            }
            LOGGER.mo11959e("Settings received is null");
            return empty;
        } catch (SecurityException e) {
            LOGGER.mo11960e("SecurityException", e);
            return empty;
        }
    }
}
