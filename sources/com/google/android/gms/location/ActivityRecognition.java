package com.google.android.gms.location;

import android.app.Activity;
import android.content.Context;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.Api.ClientKey;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.internal.location.zzaz;
import com.google.android.gms.internal.location.zze;

public class ActivityRecognition {
    public static final Api<NoOptions> API = new Api<>("ActivityRecognition.API", CLIENT_BUILDER, CLIENT_KEY);
    @Deprecated
    public static final ActivityRecognitionApi ActivityRecognitionApi = new zze();
    private static final AbstractClientBuilder<zzaz, NoOptions> CLIENT_BUILDER = new zza();
    private static final ClientKey<zzaz> CLIENT_KEY = new ClientKey<>();
    public static final String CLIENT_NAME = "activity_recognition";

    public static abstract class zza<R extends Result> extends ApiMethodImpl<R, zzaz> {
        public zza(GoogleApiClient googleApiClient) {
            super(ActivityRecognition.API, googleApiClient);
        }
    }

    private ActivityRecognition() {
    }

    public static ActivityRecognitionClient getClient(Activity activity) {
        return new ActivityRecognitionClient(activity);
    }

    public static ActivityRecognitionClient getClient(Context context) {
        return new ActivityRecognitionClient(context);
    }
}
