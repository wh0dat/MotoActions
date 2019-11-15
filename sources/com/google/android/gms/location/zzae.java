package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;

@Class(creator = "LocationSettingsConfigurationCreator")
@Reserved({3, 4, 1000})
public final class zzae extends AbstractSafeParcelable {
    public static final Creator<zzae> CREATOR = new zzaf();
    @Field(defaultValue = "", getter = "getJustificationText", mo9705id = 1)
    private final String zzbd;
    @Field(defaultValue = "", getter = "getExperimentId", mo9705id = 2)
    private final String zzbe;
    @Field(defaultValue = "", getter = "getTitleText", mo9705id = 5)
    private final String zzbf;

    @Constructor
    zzae(@Param(mo9708id = 5) String str, @Param(mo9708id = 1) String str2, @Param(mo9708id = 2) String str3) {
        this.zzbf = str;
        this.zzbd = str2;
        this.zzbe = str3;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 1, this.zzbd, false);
        SafeParcelWriter.writeString(parcel, 2, this.zzbe, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzbf, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
