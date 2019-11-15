package com.google.android.gms.location;

import android.content.Intent;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Class(creator = "LocationResultCreator")
@Reserved({1000})
public final class LocationResult extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<LocationResult> CREATOR = new zzac();
    static final List<Location> zzbb = Collections.emptyList();
    @Field(defaultValueUnchecked = "LocationResult.DEFAULT_LOCATIONS", getter = "getLocations", mo9705id = 1)
    private final List<Location> zzbc;

    @Constructor
    LocationResult(@Param(mo9708id = 1) List<Location> list) {
        this.zzbc = list;
    }

    public static LocationResult create(List<Location> list) {
        if (list == null) {
            list = zzbb;
        }
        return new LocationResult(list);
    }

    public static LocationResult extractResult(Intent intent) {
        if (!hasResult(intent)) {
            return null;
        }
        return (LocationResult) intent.getExtras().getParcelable("com.google.android.gms.location.EXTRA_LOCATION_RESULT");
    }

    public static boolean hasResult(Intent intent) {
        if (intent == null) {
            return false;
        }
        return intent.hasExtra("com.google.android.gms.location.EXTRA_LOCATION_RESULT");
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof LocationResult)) {
            return false;
        }
        LocationResult locationResult = (LocationResult) obj;
        if (locationResult.zzbc.size() != this.zzbc.size()) {
            return false;
        }
        Iterator it = this.zzbc.iterator();
        for (Location time : locationResult.zzbc) {
            if (((Location) it.next()).getTime() != time.getTime()) {
                return false;
            }
        }
        return true;
    }

    public final Location getLastLocation() {
        int size = this.zzbc.size();
        if (size == 0) {
            return null;
        }
        return (Location) this.zzbc.get(size - 1);
    }

    @NonNull
    public final List<Location> getLocations() {
        return this.zzbc;
    }

    public final int hashCode() {
        int i = 17;
        for (Location time : this.zzbc) {
            long time2 = time.getTime();
            i = (i * 31) + ((int) (time2 ^ (time2 >>> 32)));
        }
        return i;
    }

    public final String toString() {
        String valueOf = String.valueOf(this.zzbc);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 27);
        sb.append("LocationResult[locations: ");
        sb.append(valueOf);
        sb.append("]");
        return sb.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeTypedList(parcel, 1, getLocations(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
