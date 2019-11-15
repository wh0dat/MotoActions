package com.google.android.gms.common.internal;

import android.support.annotation.NonNull;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;

public class ApiExceptionUtil {
    @NonNull
    public static ApiException fromConnectionResult(@NonNull ConnectionResult connectionResult) {
        return fromStatus(new Status(connectionResult.getErrorCode(), connectionResult.getErrorMessage(), connectionResult.getResolution()));
    }

    @NonNull
    public static ApiException fromStatus(@NonNull Status status) {
        return status.hasResolution() ? new ResolvableApiException(status) : new ApiException(status);
    }
}
