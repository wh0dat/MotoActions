package com.google.android.gms.common;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;

@Class(creator = "FeatureCreator")
public class Feature extends AbstractSafeParcelable {
    public static final Creator<Feature> CREATOR = new FeatureCreator();
    @Field(getter = "getName", mo9705id = 1)
    private final String name;
    @Field(getter = "getOldVersion", mo9705id = 2)
    @Deprecated
    private final int zzaq;
    @Field(defaultValue = "-1", getter = "getVersion", mo9705id = 3)
    private final long zzar;

    @Constructor
    public Feature(@Param(mo9708id = 1) String str, @Param(mo9708id = 2) int i, @Param(mo9708id = 3) long j) {
        this.name = str;
        this.zzaq = i;
        this.zzar = j;
    }

    public Feature(String str, long j) {
        this.name = str;
        this.zzar = j;
        this.zzaq = -1;
    }

    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Feature) {
            Feature feature = (Feature) obj;
            if (((getName() != null && getName().equals(feature.getName())) || (getName() == null && feature.getName() == null)) && getVersion() == feature.getVersion()) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return this.name;
    }

    public long getVersion() {
        return this.zzar == -1 ? (long) this.zzaq : this.zzar;
    }

    public int hashCode() {
        return Objects.hashCode(getName(), Long.valueOf(getVersion()));
    }

    public String toString() {
        return Objects.toStringHelper(this).add("name", getName()).add("version", Long.valueOf(getVersion())).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 1, getName(), false);
        SafeParcelWriter.writeInt(parcel, 2, this.zzaq);
        SafeParcelWriter.writeLong(parcel, 3, getVersion());
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
