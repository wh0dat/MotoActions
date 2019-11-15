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
import com.google.android.gms.common.util.VisibleForTesting;
import com.google.android.gms.location.zzj;
import java.util.Collections;
import java.util.List;

@Class(creator = "DeviceOrientationRequestInternalCreator")
public final class zzm extends AbstractSafeParcelable {
    public static final Creator<zzm> CREATOR = new zzn();
    @VisibleForTesting
    static final List<ClientIdentity> zzcd = Collections.emptyList();
    static final zzj zzce = new zzj();
    @Nullable
    @Field(defaultValueUnchecked = "null", mo9705id = 3)
    private String tag;
    @Field(defaultValueUnchecked = "DeviceOrientationRequestInternal.DEFAULT_DEVICE_ORIENTATION_REQUEST", mo9705id = 1)
    private zzj zzcf;
    @Field(defaultValueUnchecked = "DeviceOrientationRequestInternal.DEFAULT_CLIENTS", mo9705id = 2)
    private List<ClientIdentity> zzm;

    @Constructor
    zzm(@Param(mo9708id = 1) zzj zzj, @Param(mo9708id = 2) List<ClientIdentity> list, @Param(mo9708id = 3) String str) {
        this.zzcf = zzj;
        this.zzm = list;
        this.tag = str;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzm)) {
            return false;
        }
        zzm zzm2 = (zzm) obj;
        return Objects.equal(this.zzcf, zzm2.zzcf) && Objects.equal(this.zzm, zzm2.zzm) && Objects.equal(this.tag, zzm2.tag);
    }

    public final int hashCode() {
        return this.zzcf.hashCode();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeParcelable(parcel, 1, this.zzcf, i, false);
        SafeParcelWriter.writeTypedList(parcel, 2, this.zzm, false);
        SafeParcelWriter.writeString(parcel, 3, this.tag, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
