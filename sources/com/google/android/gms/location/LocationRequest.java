package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Reserved;
import com.google.android.gms.common.util.VisibleForTesting;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import kotlin.jvm.internal.LongCompanionObject;

@Class(creator = "LocationRequestCreator")
@Reserved({1000})
public final class LocationRequest extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<LocationRequest> CREATOR = new zzab();
    public static final int PRIORITY_BALANCED_POWER_ACCURACY = 102;
    public static final int PRIORITY_HIGH_ACCURACY = 100;
    public static final int PRIORITY_LOW_POWER = 104;
    public static final int PRIORITY_NO_POWER = 105;
    @Field(defaultValueUnchecked = "LocationRequest.DEFAULT_PRIORITY", mo9705id = 1)
    private int priority;
    @Field(defaultValueUnchecked = "LocationRequest.DEFAULT_EXPIRE_AT", mo9705id = 5)
    private long zzaf;
    @Field(defaultValueUnchecked = "LocationRequest.DEFAULT_INTERVAL", mo9705id = 2)
    private long zzaw;
    @Field(defaultValueUnchecked = "LocationRequest.DEFAULT_FASTEST_INTERVAL", mo9705id = 3)
    private long zzax;
    @Field(defaultValueUnchecked = "LocationRequest.DEFAULT_EXPLICIT_FASTEST_INTERVAL", mo9705id = 4)
    private boolean zzay;
    @Field(defaultValueUnchecked = "LocationRequest.DEFAULT_SMALLEST_DISPLACEMENT", mo9705id = 7)
    private float zzaz;
    @Field(defaultValueUnchecked = "LocationRequest.DEFAULT_MAX_WAIT_TIME", mo9705id = 8)
    private long zzba;
    @Field(defaultValueUnchecked = "LocationRequest.DEFAULT_NUM_UPDATES", mo9705id = 6)
    private int zzx;

    public LocationRequest() {
        this.priority = 102;
        this.zzaw = 3600000;
        this.zzax = 600000;
        this.zzay = false;
        this.zzaf = LongCompanionObject.MAX_VALUE;
        this.zzx = Integer.MAX_VALUE;
        this.zzaz = 0.0f;
        this.zzba = 0;
    }

    @Constructor
    LocationRequest(@Param(mo9708id = 1) int i, @Param(mo9708id = 2) long j, @Param(mo9708id = 3) long j2, @Param(mo9708id = 4) boolean z, @Param(mo9708id = 5) long j3, @Param(mo9708id = 6) int i2, @Param(mo9708id = 7) float f, @Param(mo9708id = 8) long j4) {
        this.priority = i;
        this.zzaw = j;
        this.zzax = j2;
        this.zzay = z;
        this.zzaf = j3;
        this.zzx = i2;
        this.zzaz = f;
        this.zzba = j4;
    }

    @VisibleForTesting
    public static LocationRequest create() {
        return new LocationRequest();
    }

    private static void zza(long j) {
        if (j < 0) {
            StringBuilder sb = new StringBuilder(38);
            sb.append("invalid interval: ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LocationRequest)) {
            return false;
        }
        LocationRequest locationRequest = (LocationRequest) obj;
        return this.priority == locationRequest.priority && this.zzaw == locationRequest.zzaw && this.zzax == locationRequest.zzax && this.zzay == locationRequest.zzay && this.zzaf == locationRequest.zzaf && this.zzx == locationRequest.zzx && this.zzaz == locationRequest.zzaz && getMaxWaitTime() == locationRequest.getMaxWaitTime();
    }

    public final long getExpirationTime() {
        return this.zzaf;
    }

    public final long getFastestInterval() {
        return this.zzax;
    }

    public final long getInterval() {
        return this.zzaw;
    }

    public final long getMaxWaitTime() {
        long j = this.zzba;
        return j < this.zzaw ? this.zzaw : j;
    }

    public final int getNumUpdates() {
        return this.zzx;
    }

    public final int getPriority() {
        return this.priority;
    }

    public final float getSmallestDisplacement() {
        return this.zzaz;
    }

    public final int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.priority), Long.valueOf(this.zzaw), Float.valueOf(this.zzaz), Long.valueOf(this.zzba));
    }

    public final boolean isFastestIntervalExplicitlySet() {
        return this.zzay;
    }

    public final LocationRequest setExpirationDuration(long j) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (j > LongCompanionObject.MAX_VALUE - elapsedRealtime) {
            this.zzaf = LongCompanionObject.MAX_VALUE;
        } else {
            this.zzaf = j + elapsedRealtime;
        }
        if (this.zzaf < 0) {
            this.zzaf = 0;
        }
        return this;
    }

    @VisibleForTesting
    public final LocationRequest setExpirationTime(long j) {
        this.zzaf = j;
        if (this.zzaf < 0) {
            this.zzaf = 0;
        }
        return this;
    }

    public final LocationRequest setFastestInterval(long j) {
        zza(j);
        this.zzay = true;
        this.zzax = j;
        return this;
    }

    public final LocationRequest setInterval(long j) {
        zza(j);
        this.zzaw = j;
        if (!this.zzay) {
            this.zzax = (long) (((double) this.zzaw) / 6.0d);
        }
        return this;
    }

    @VisibleForTesting
    public final LocationRequest setMaxWaitTime(long j) {
        zza(j);
        this.zzba = j;
        return this;
    }

    @VisibleForTesting
    public final LocationRequest setNumUpdates(int i) {
        if (i <= 0) {
            StringBuilder sb = new StringBuilder(31);
            sb.append("invalid numUpdates: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        this.zzx = i;
        return this;
    }

    @VisibleForTesting
    public final LocationRequest setPriority(int i) {
        switch (i) {
            case 100:
            case 102:
            case 104:
            case 105:
                this.priority = i;
                return this;
            default:
                StringBuilder sb = new StringBuilder(28);
                sb.append("invalid quality: ");
                sb.append(i);
                throw new IllegalArgumentException(sb.toString());
        }
    }

    @VisibleForTesting
    public final LocationRequest setSmallestDisplacement(float f) {
        if (f < 0.0f) {
            StringBuilder sb = new StringBuilder(37);
            sb.append("invalid displacement: ");
            sb.append(f);
            throw new IllegalArgumentException(sb.toString());
        }
        this.zzaz = f;
        return this;
    }

    public final String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("Request[");
        switch (this.priority) {
            case 100:
                str = "PRIORITY_HIGH_ACCURACY";
                break;
            case 102:
                str = "PRIORITY_BALANCED_POWER_ACCURACY";
                break;
            case 104:
                str = "PRIORITY_LOW_POWER";
                break;
            case 105:
                str = "PRIORITY_NO_POWER";
                break;
            default:
                str = "???";
                break;
        }
        sb.append(str);
        if (this.priority != 105) {
            sb.append(" requested=");
            sb.append(this.zzaw);
            sb.append("ms");
        }
        sb.append(" fastest=");
        sb.append(this.zzax);
        sb.append("ms");
        if (this.zzba > this.zzaw) {
            sb.append(" maxWait=");
            sb.append(this.zzba);
            sb.append("ms");
        }
        if (this.zzaz > 0.0f) {
            sb.append(" smallestDisplacement=");
            sb.append(this.zzaz);
            sb.append(CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_MOTO_APP);
        }
        if (this.zzaf != LongCompanionObject.MAX_VALUE) {
            long elapsedRealtime = this.zzaf - SystemClock.elapsedRealtime();
            sb.append(" expireIn=");
            sb.append(elapsedRealtime);
            sb.append("ms");
        }
        if (this.zzx != Integer.MAX_VALUE) {
            sb.append(" num=");
            sb.append(this.zzx);
        }
        sb.append(']');
        return sb.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.priority);
        SafeParcelWriter.writeLong(parcel, 2, this.zzaw);
        SafeParcelWriter.writeLong(parcel, 3, this.zzax);
        SafeParcelWriter.writeBoolean(parcel, 4, this.zzay);
        SafeParcelWriter.writeLong(parcel, 5, this.zzaf);
        SafeParcelWriter.writeInt(parcel, 6, this.zzx);
        SafeParcelWriter.writeFloat(parcel, 7, this.zzaz);
        SafeParcelWriter.writeLong(parcel, 8, this.zzba);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
