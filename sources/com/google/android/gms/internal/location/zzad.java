package com.google.android.gms.internal.location;

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

@Class(creator = "FusedLocationProviderResultCreator")
@Reserved({1000})
public final class zzad extends AbstractSafeParcelable implements Result {
    public static final Creator<zzad> CREATOR = new zzae();
    private static final zzad zzcr = new zzad(Status.RESULT_SUCCESS);
    @Field(getter = "getStatus", mo9705id = 1)
    private final Status zzbl;

    @Constructor
    public zzad(@Param(mo9708id = 1) Status status) {
        this.zzbl = status;
    }

    public final Status getStatus() {
        return this.zzbl;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeParcelable(parcel, 1, getStatus(), i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
