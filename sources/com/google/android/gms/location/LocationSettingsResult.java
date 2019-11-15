package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;

@Class(creator = "LocationSettingsResultCreator")
@Reserved({1000})
public final class LocationSettingsResult extends AbstractSafeParcelable implements Result {
    public static final Creator<LocationSettingsResult> CREATOR = new zzah();
    @Field(getter = "getStatus", mo9705id = 1)
    private final Status zzbl;
    @Field(getter = "getLocationSettingsStates", mo9705id = 2)
    private final LocationSettingsStates zzbm;

    public LocationSettingsResult(Status status) {
        this(status, null);
    }

    @Constructor
    public LocationSettingsResult(@Param(mo9708id = 1) Status status, @Param(mo9708id = 2) LocationSettingsStates locationSettingsStates) {
        this.zzbl = status;
        this.zzbm = locationSettingsStates;
    }

    public final LocationSettingsStates getLocationSettingsStates() {
        return this.zzbm;
    }

    public final Status getStatus() {
        return this.zzbl;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeParcelable(parcel, 1, getStatus(), i, false);
        SafeParcelWriter.writeParcelable(parcel, 2, getLocationSettingsStates(), i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
