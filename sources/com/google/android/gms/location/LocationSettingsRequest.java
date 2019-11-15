package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Class(creator = "LocationSettingsRequestCreator")
@Reserved({1000})
public final class LocationSettingsRequest extends AbstractSafeParcelable {
    public static final Creator<LocationSettingsRequest> CREATOR = new zzag();
    @Field(getter = "getLocationRequests", mo9705id = 1)
    private final List<LocationRequest> zzbg;
    @Field(defaultValue = "false", getter = "alwaysShow", mo9705id = 2)
    private final boolean zzbh;
    @Field(getter = "needBle", mo9705id = 3)
    private final boolean zzbi;
    @Field(getter = "getConfiguration", mo9705id = 5)
    private zzae zzbj;

    public static final class Builder {
        private boolean zzbh = false;
        private boolean zzbi = false;
        private zzae zzbj = null;
        private final ArrayList<LocationRequest> zzbk = new ArrayList<>();

        public final Builder addAllLocationRequests(Collection<LocationRequest> collection) {
            for (LocationRequest locationRequest : collection) {
                if (locationRequest != null) {
                    this.zzbk.add(locationRequest);
                }
            }
            return this;
        }

        public final Builder addLocationRequest(@NonNull LocationRequest locationRequest) {
            if (locationRequest != null) {
                this.zzbk.add(locationRequest);
            }
            return this;
        }

        public final LocationSettingsRequest build() {
            return new LocationSettingsRequest(this.zzbk, this.zzbh, this.zzbi, null);
        }

        public final Builder setAlwaysShow(boolean z) {
            this.zzbh = z;
            return this;
        }

        public final Builder setNeedBle(boolean z) {
            this.zzbi = z;
            return this;
        }
    }

    @Constructor
    LocationSettingsRequest(@Param(mo9708id = 1) List<LocationRequest> list, @Param(mo9708id = 2) boolean z, @Param(mo9708id = 3) boolean z2, @Param(mo9708id = 5) zzae zzae) {
        this.zzbg = list;
        this.zzbh = z;
        this.zzbi = z2;
        this.zzbj = zzae;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeTypedList(parcel, 1, Collections.unmodifiableList(this.zzbg), false);
        SafeParcelWriter.writeBoolean(parcel, 2, this.zzbh);
        SafeParcelWriter.writeBoolean(parcel, 3, this.zzbi);
        SafeParcelWriter.writeParcelable(parcel, 5, this.zzbj, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
