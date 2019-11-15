package com.google.android.gms.internal.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import com.google.android.gms.common.internal.ClientIdentity;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.location.LocationRequest;
import java.util.Collections;
import java.util.List;

@Class(creator = "LocationRequestInternalCreator")
@Reserved({1000, 2, 3, 4})
public final class zzbd extends AbstractSafeParcelable {
    public static final Creator<zzbd> CREATOR = new zzbe();
    static final List<ClientIdentity> zzcd = Collections.emptyList();
    @Nullable
    @Field(defaultValueUnchecked = "null", mo9705id = 10)
    private String moduleId;
    @Nullable
    @Field(defaultValueUnchecked = "null", mo9705id = 6)
    private String tag;
    @Field(defaultValueUnchecked = "null", mo9705id = 1)
    private LocationRequest zzdg;
    @Field(defaultValueUnchecked = "LocationRequestInternal.DEFAULT_HIDE_FROM_APP_OPS", mo9705id = 7)
    private boolean zzdh;
    @Field(defaultValueUnchecked = "LocationRequestInternal.DEFAULT_FORCE_COARSE_LOCATION", mo9705id = 8)
    private boolean zzdi;
    @Field(defaultValueUnchecked = "LocationRequestInternal.DEFAULT_EXEMPT_FROM_THROTTLE", mo9705id = 9)
    private boolean zzdj;
    private boolean zzdk = true;
    @Field(defaultValueUnchecked = "LocationRequestInternal.DEFAULT_CLIENTS", mo9705id = 5)
    private List<ClientIdentity> zzm;

    @Constructor
    zzbd(@Param(mo9708id = 1) LocationRequest locationRequest, @Param(mo9708id = 5) List<ClientIdentity> list, @Nullable @Param(mo9708id = 6) String str, @Param(mo9708id = 7) boolean z, @Param(mo9708id = 8) boolean z2, @Param(mo9708id = 9) boolean z3, @Param(mo9708id = 10) String str2) {
        this.zzdg = locationRequest;
        this.zzm = list;
        this.tag = str;
        this.zzdh = z;
        this.zzdi = z2;
        this.zzdj = z3;
        this.moduleId = str2;
    }

    @Deprecated
    public static zzbd zza(LocationRequest locationRequest) {
        zzbd zzbd = new zzbd(locationRequest, zzcd, null, false, false, false, null);
        return zzbd;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzbd)) {
            return false;
        }
        zzbd zzbd = (zzbd) obj;
        return Objects.equal(this.zzdg, zzbd.zzdg) && Objects.equal(this.zzm, zzbd.zzm) && Objects.equal(this.tag, zzbd.tag) && this.zzdh == zzbd.zzdh && this.zzdi == zzbd.zzdi && this.zzdj == zzbd.zzdj && Objects.equal(this.moduleId, zzbd.moduleId);
    }

    public final int hashCode() {
        return this.zzdg.hashCode();
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.zzdg);
        if (this.tag != null) {
            sb.append(" tag=");
            sb.append(this.tag);
        }
        if (this.moduleId != null) {
            sb.append(" moduleId=");
            sb.append(this.moduleId);
        }
        sb.append(" hideAppOps=");
        sb.append(this.zzdh);
        sb.append(" clients=");
        sb.append(this.zzm);
        sb.append(" forceCoarseLocation=");
        sb.append(this.zzdi);
        if (this.zzdj) {
            sb.append(" exemptFromBackgroundThrottle");
        }
        return sb.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeParcelable(parcel, 1, this.zzdg, i, false);
        SafeParcelWriter.writeTypedList(parcel, 5, this.zzm, false);
        SafeParcelWriter.writeString(parcel, 6, this.tag, false);
        SafeParcelWriter.writeBoolean(parcel, 7, this.zzdh);
        SafeParcelWriter.writeBoolean(parcel, 8, this.zzdi);
        SafeParcelWriter.writeBoolean(parcel, 9, this.zzdj);
        SafeParcelWriter.writeString(parcel, 10, this.moduleId, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
