package com.google.android.gms.location;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import java.util.Collections;
import java.util.List;

@Class(creator = "RemoveGeofencingRequestCreator")
@Reserved({1000})
public final class zzal extends AbstractSafeParcelable {
    public static final Creator<zzal> CREATOR = new zzam();
    @Field(defaultValue = "", getter = "getTag", mo9705id = 3)
    private final String tag;
    @Field(getter = "getGeofenceIds", mo9705id = 1)
    private final List<String> zzbu;
    @Field(getter = "getPendingIntent", mo9705id = 2)
    private final PendingIntent zzbv;

    @Constructor
    zzal(@Nullable @Param(mo9708id = 1) List<String> list, @Nullable @Param(mo9708id = 2) PendingIntent pendingIntent, @Param(mo9708id = 3) String str) {
        this.zzbu = list == null ? Collections.emptyList() : Collections.unmodifiableList(list);
        this.zzbv = pendingIntent;
        this.tag = str;
    }

    public static zzal zza(PendingIntent pendingIntent) {
        Preconditions.checkNotNull(pendingIntent, "PendingIntent can not be null.");
        return new zzal(null, pendingIntent, "");
    }

    public static zzal zza(List<String> list) {
        Preconditions.checkNotNull(list, "geofence can't be null.");
        Preconditions.checkArgument(!list.isEmpty(), "Geofences must contains at least one id.");
        return new zzal(list, null, "");
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeStringList(parcel, 1, this.zzbu, false);
        SafeParcelWriter.writeParcelable(parcel, 2, this.zzbv, i, false);
        SafeParcelWriter.writeString(parcel, 3, this.tag, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
