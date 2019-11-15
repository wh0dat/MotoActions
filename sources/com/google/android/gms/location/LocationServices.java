package com.google.android.gms.location;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.Api.ClientKey;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.internal.location.zzaf;
import com.google.android.gms.internal.location.zzaz;
import com.google.android.gms.internal.location.zzbk;
import com.google.android.gms.internal.location.zzq;

public class LocationServices {
    public static final Api<NoOptions> API = new Api<>("LocationServices.API", CLIENT_BUILDER, CLIENT_KEY);
    private static final AbstractClientBuilder<zzaz, NoOptions> CLIENT_BUILDER = new zzad();
    private static final ClientKey<zzaz> CLIENT_KEY = new ClientKey<>();
    @Deprecated
    public static final FusedLocationProviderApi FusedLocationApi = new zzq();
    @Deprecated
    public static final GeofencingApi GeofencingApi = new zzaf();
    @Deprecated
    public static final SettingsApi SettingsApi = new zzbk();

    public static abstract class zza<R extends Result> extends ApiMethodImpl<R, zzaz> {
        public zza(GoogleApiClient googleApiClient) {
            super(LocationServices.API, googleApiClient);
        }
    }

    private LocationServices() {
    }

    public static FusedLocationProviderClient getFusedLocationProviderClient(@NonNull Activity activity) {
        return new FusedLocationProviderClient(activity);
    }

    public static FusedLocationProviderClient getFusedLocationProviderClient(@NonNull Context context) {
        return new FusedLocationProviderClient(context);
    }

    public static GeofencingClient getGeofencingClient(@NonNull Activity activity) {
        return new GeofencingClient(activity);
    }

    public static GeofencingClient getGeofencingClient(@NonNull Context context) {
        return new GeofencingClient(context);
    }

    public static SettingsClient getSettingsClient(@NonNull Activity activity) {
        return new SettingsClient(activity);
    }

    public static SettingsClient getSettingsClient(@NonNull Context context) {
        return new SettingsClient(context);
    }

    public static zzaz zza(GoogleApiClient googleApiClient) {
        boolean z = false;
        Preconditions.checkArgument(googleApiClient != null, "GoogleApiClient parameter is required.");
        zzaz zzaz = (zzaz) googleApiClient.getClient(CLIENT_KEY);
        if (zzaz != null) {
            z = true;
        }
        Preconditions.checkState(z, "GoogleApiClient is not configured to use the LocationServices.API Api. Pass thisinto GoogleApiClient.Builder#addApi() to use this feature.");
        return zzaz;
    }
}
