package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;

@Class(creator = "NetworkLocationStatusCreator")
public final class zzaj extends AbstractSafeParcelable {
    public static final Creator<zzaj> CREATOR = new zzak();
    @Field(defaultValueUnchecked = "LocationAvailability.STATUS_UNKNOWN", mo9705id = 2)
    private final int zzar;
    @Field(defaultValueUnchecked = "LocationAvailability.STATUS_UNKNOWN", mo9705id = 1)
    private final int zzas;
    @Field(defaultValueUnchecked = "NetworkLocationStatus.STATUS_INVALID_TIMESTAMP", mo9705id = 4)
    private final long zzat;
    @Field(defaultValueUnchecked = "NetworkLocationStatus.STATUS_INVALID_TIMESTAMP", mo9705id = 3)
    private final long zzbt;

    @Constructor
    zzaj(@Param(mo9708id = 1) int i, @Param(mo9708id = 2) int i2, @Param(mo9708id = 3) long j, @Param(mo9708id = 4) long j2) {
        this.zzas = i;
        this.zzar = i2;
        this.zzbt = j;
        this.zzat = j2;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        zzaj zzaj = (zzaj) obj;
        return this.zzas == zzaj.zzas && this.zzar == zzaj.zzar && this.zzbt == zzaj.zzbt && this.zzat == zzaj.zzat;
    }

    public final int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.zzar), Integer.valueOf(this.zzas), Long.valueOf(this.zzat), Long.valueOf(this.zzbt));
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder("NetworkLocationStatus:");
        sb.append(" Wifi status: ");
        sb.append(this.zzas);
        sb.append(" Cell status: ");
        sb.append(this.zzar);
        sb.append(" elapsed time NS: ");
        sb.append(this.zzat);
        sb.append(" system time ms: ");
        sb.append(this.zzbt);
        return sb.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzas);
        SafeParcelWriter.writeInt(parcel, 2, this.zzar);
        SafeParcelWriter.writeLong(parcel, 3, this.zzbt);
        SafeParcelWriter.writeLong(parcel, 4, this.zzat);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
