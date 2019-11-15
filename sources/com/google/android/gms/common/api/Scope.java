package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;

@KeepForSdk
@Class(creator = "ScopeCreator")
public final class Scope extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<Scope> CREATOR = new zzd();
    @VersionField(mo9711id = 1)
    private final int zzal;
    @Field(getter = "getScopeUri", mo9705id = 2)
    private final String zzdp;

    @Constructor
    Scope(@Param(mo9708id = 1) int i, @Param(mo9708id = 2) String str) {
        Preconditions.checkNotEmpty(str, "scopeUri must not be null or empty");
        this.zzal = i;
        this.zzdp = str;
    }

    public Scope(String str) {
        this(1, str);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Scope)) {
            return false;
        }
        return this.zzdp.equals(((Scope) obj).zzdp);
    }

    @KeepForSdk
    public final String getScopeUri() {
        return this.zzdp;
    }

    public final int hashCode() {
        return this.zzdp.hashCode();
    }

    public final String toString() {
        return this.zzdp;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeString(parcel, 2, getScopeUri(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
