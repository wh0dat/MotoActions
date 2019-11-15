package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import com.google.android.gms.common.C0499R;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.MetadataValueReader;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.StringResourceValueReader;
import com.google.android.gms.common.util.VisibleForTesting;
import javax.annotation.concurrent.GuardedBy;

@KeepForSdk
@Deprecated
public final class GoogleServices {
    private static final Object sLock = new Object();
    @GuardedBy("sLock")
    private static GoogleServices zzku;
    private final String zzkv;
    private final Status zzkw;
    private final boolean zzkx;
    private final boolean zzky;

    @KeepForSdk
    @VisibleForTesting
    GoogleServices(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("google_app_measurement_enable", "integer", resources.getResourcePackageName(C0499R.string.common_google_play_services_unknown_issue));
        boolean z = true;
        if (identifier != 0) {
            if (resources.getInteger(identifier) == 0) {
                z = false;
            }
            this.zzky = !z;
        } else {
            this.zzky = false;
        }
        this.zzkx = z;
        String googleAppId = MetadataValueReader.getGoogleAppId(context);
        if (googleAppId == null) {
            googleAppId = new StringResourceValueReader(context).getString("google_app_id");
        }
        if (TextUtils.isEmpty(googleAppId)) {
            this.zzkw = new Status(10, "Missing google app id value from from string resources with name google_app_id.");
            this.zzkv = null;
            return;
        }
        this.zzkv = googleAppId;
        this.zzkw = Status.RESULT_SUCCESS;
    }

    @KeepForSdk
    @VisibleForTesting
    GoogleServices(String str, boolean z) {
        this.zzkv = str;
        this.zzkw = Status.RESULT_SUCCESS;
        this.zzkx = z;
        this.zzky = !z;
    }

    @KeepForSdk
    private static GoogleServices checkInitialized(String str) {
        GoogleServices googleServices;
        synchronized (sLock) {
            if (zzku == null) {
                StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 34);
                sb.append("Initialize must be called before ");
                sb.append(str);
                sb.append(".");
                throw new IllegalStateException(sb.toString());
            }
            googleServices = zzku;
        }
        return googleServices;
    }

    @KeepForSdk
    @VisibleForTesting
    static void clearInstanceForTest() {
        synchronized (sLock) {
            zzku = null;
        }
    }

    @KeepForSdk
    public static String getGoogleAppId() {
        return checkInitialized("getGoogleAppId").zzkv;
    }

    @KeepForSdk
    public static Status initialize(Context context) {
        Status status;
        Preconditions.checkNotNull(context, "Context must not be null.");
        synchronized (sLock) {
            if (zzku == null) {
                zzku = new GoogleServices(context);
            }
            status = zzku.zzkw;
        }
        return status;
    }

    @KeepForSdk
    public static Status initialize(Context context, String str, boolean z) {
        Preconditions.checkNotNull(context, "Context must not be null.");
        Preconditions.checkNotEmpty(str, "App ID must be nonempty.");
        synchronized (sLock) {
            if (zzku != null) {
                Status checkGoogleAppId = zzku.checkGoogleAppId(str);
                return checkGoogleAppId;
            }
            GoogleServices googleServices = new GoogleServices(str, z);
            zzku = googleServices;
            Status status = googleServices.zzkw;
            return status;
        }
    }

    @KeepForSdk
    public static boolean isMeasurementEnabled() {
        GoogleServices checkInitialized = checkInitialized("isMeasurementEnabled");
        return checkInitialized.zzkw.isSuccess() && checkInitialized.zzkx;
    }

    @KeepForSdk
    public static boolean isMeasurementExplicitlyDisabled() {
        return checkInitialized("isMeasurementExplicitlyDisabled").zzky;
    }

    /* access modifiers changed from: 0000 */
    @KeepForSdk
    @VisibleForTesting
    public final Status checkGoogleAppId(String str) {
        if (this.zzkv == null || this.zzkv.equals(str)) {
            return Status.RESULT_SUCCESS;
        }
        String str2 = this.zzkv;
        StringBuilder sb = new StringBuilder(String.valueOf(str2).length() + 97);
        sb.append("Initialize was called with two different Google App IDs.  Only the first app ID will be used: '");
        sb.append(str2);
        sb.append("'.");
        return new Status(10, sb.toString());
    }
}
