package com.google.android.gms.location;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.common.internal.safeparcel.SafeParcelableSerializer;

@Class(creator = "LocationSettingsStatesCreator")
@Reserved({1000})
public final class LocationSettingsStates extends AbstractSafeParcelable {
    public static final Creator<LocationSettingsStates> CREATOR = new zzai();
    @Field(getter = "isGpsUsable", mo9705id = 1)
    private final boolean zzbn;
    @Field(getter = "isNetworkLocationUsable", mo9705id = 2)
    private final boolean zzbo;
    @Field(getter = "isBleUsable", mo9705id = 3)
    private final boolean zzbp;
    @Field(getter = "isGpsPresent", mo9705id = 4)
    private final boolean zzbq;
    @Field(getter = "isNetworkLocationPresent", mo9705id = 5)
    private final boolean zzbr;
    @Field(getter = "isBlePresent", mo9705id = 6)
    private final boolean zzbs;

    @Constructor
    public LocationSettingsStates(@Param(mo9708id = 1) boolean z, @Param(mo9708id = 2) boolean z2, @Param(mo9708id = 3) boolean z3, @Param(mo9708id = 4) boolean z4, @Param(mo9708id = 5) boolean z5, @Param(mo9708id = 6) boolean z6) {
        this.zzbn = z;
        this.zzbo = z2;
        this.zzbp = z3;
        this.zzbq = z4;
        this.zzbr = z5;
        this.zzbs = z6;
    }

    public static LocationSettingsStates fromIntent(Intent intent) {
        return (LocationSettingsStates) SafeParcelableSerializer.deserializeFromIntentExtra(intent, "com.google.android.gms.location.LOCATION_SETTINGS_STATES", CREATOR);
    }

    public final boolean isBlePresent() {
        return this.zzbs;
    }

    public final boolean isBleUsable() {
        return this.zzbp;
    }

    public final boolean isGpsPresent() {
        return this.zzbq;
    }

    public final boolean isGpsUsable() {
        return this.zzbn;
    }

    public final boolean isLocationPresent() {
        return this.zzbq || this.zzbr;
    }

    public final boolean isLocationUsable() {
        return this.zzbn || this.zzbo;
    }

    public final boolean isNetworkLocationPresent() {
        return this.zzbr;
    }

    public final boolean isNetworkLocationUsable() {
        return this.zzbo;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBoolean(parcel, 1, isGpsUsable());
        SafeParcelWriter.writeBoolean(parcel, 2, isNetworkLocationUsable());
        SafeParcelWriter.writeBoolean(parcel, 3, isBleUsable());
        SafeParcelWriter.writeBoolean(parcel, 4, isGpsPresent());
        SafeParcelWriter.writeBoolean(parcel, 5, isNetworkLocationPresent());
        SafeParcelWriter.writeBoolean(parcel, 6, isBlePresent());
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
