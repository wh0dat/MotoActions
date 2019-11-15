package android.support.p001v4.net;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* renamed from: android.support.v4.net.ConnectivityManagerCompat */
public final class ConnectivityManagerCompat {
    private static final ConnectivityManagerCompatImpl IMPL;
    public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
    public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
    public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;

    @RequiresApi(16)
    /* renamed from: android.support.v4.net.ConnectivityManagerCompat$ConnectivityManagerCompatApi16Impl */
    static class ConnectivityManagerCompatApi16Impl extends ConnectivityManagerCompatBaseImpl {
        ConnectivityManagerCompatApi16Impl() {
        }

        public boolean isActiveNetworkMetered(ConnectivityManager connectivityManager) {
            return connectivityManager.isActiveNetworkMetered();
        }
    }

    @RequiresApi(24)
    /* renamed from: android.support.v4.net.ConnectivityManagerCompat$ConnectivityManagerCompatApi24Impl */
    static class ConnectivityManagerCompatApi24Impl extends ConnectivityManagerCompatApi16Impl {
        ConnectivityManagerCompatApi24Impl() {
        }

        public int getRestrictBackgroundStatus(ConnectivityManager connectivityManager) {
            return connectivityManager.getRestrictBackgroundStatus();
        }
    }

    /* renamed from: android.support.v4.net.ConnectivityManagerCompat$ConnectivityManagerCompatBaseImpl */
    static class ConnectivityManagerCompatBaseImpl implements ConnectivityManagerCompatImpl {
        public int getRestrictBackgroundStatus(ConnectivityManager connectivityManager) {
            return 3;
        }

        ConnectivityManagerCompatBaseImpl() {
        }

        public boolean isActiveNetworkMetered(ConnectivityManager connectivityManager) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return true;
            }
            switch (activeNetworkInfo.getType()) {
                case 0:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    return true;
                case 1:
                case 7:
                case 9:
                    return false;
                default:
                    return true;
            }
        }
    }

    /* renamed from: android.support.v4.net.ConnectivityManagerCompat$ConnectivityManagerCompatImpl */
    interface ConnectivityManagerCompatImpl {
        int getRestrictBackgroundStatus(ConnectivityManager connectivityManager);

        boolean isActiveNetworkMetered(ConnectivityManager connectivityManager);
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    @Retention(RetentionPolicy.SOURCE)
    /* renamed from: android.support.v4.net.ConnectivityManagerCompat$RestrictBackgroundStatus */
    public @interface RestrictBackgroundStatus {
    }

    static {
        if (VERSION.SDK_INT >= 24) {
            IMPL = new ConnectivityManagerCompatApi24Impl();
        } else if (VERSION.SDK_INT >= 16) {
            IMPL = new ConnectivityManagerCompatApi16Impl();
        } else {
            IMPL = new ConnectivityManagerCompatBaseImpl();
        }
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public static boolean isActiveNetworkMetered(ConnectivityManager connectivityManager) {
        return IMPL.isActiveNetworkMetered(connectivityManager);
    }

    public static NetworkInfo getNetworkInfoFromBroadcast(ConnectivityManager connectivityManager, Intent intent) {
        NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra("networkInfo");
        if (networkInfo != null) {
            return connectivityManager.getNetworkInfo(networkInfo.getType());
        }
        return null;
    }

    public static int getRestrictBackgroundStatus(ConnectivityManager connectivityManager) {
        return IMPL.getRestrictBackgroundStatus(connectivityManager);
    }

    private ConnectivityManagerCompat() {
    }
}
