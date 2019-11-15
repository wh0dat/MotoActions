package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Objects.ToStringHelper;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;

@Class(creator = "PlaceReportCreator")
public class PlaceReport extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<PlaceReport> CREATOR = new zza();
    @Field(getter = "getTag", mo9705id = 3)
    private final String tag;
    @VersionField(mo9711id = 1)
    private final int versionCode;
    @Field(getter = "getPlaceId", mo9705id = 2)
    private final String zza;
    @Field(getter = "getSource", mo9705id = 4)
    private final String zzb;

    @Constructor
    PlaceReport(@Param(mo9708id = 1) int i, @Param(mo9708id = 2) String str, @Param(mo9708id = 3) String str2, @Param(mo9708id = 4) String str3) {
        this.versionCode = i;
        this.zza = str;
        this.tag = str2;
        this.zzb = str3;
    }

    @com.google.android.gms.common.util.VisibleForTesting
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.android.gms.location.places.PlaceReport create(java.lang.String r4, java.lang.String r5) {
        /*
            java.lang.String r0 = "unknown"
            com.google.android.gms.common.internal.Preconditions.checkNotNull(r4)
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r5)
            com.google.android.gms.common.internal.Preconditions.checkNotEmpty(r0)
            int r1 = r0.hashCode()
            r2 = 0
            r3 = 1
            switch(r1) {
                case -1436706272: goto L_0x0047;
                case -1194968642: goto L_0x003d;
                case -284840886: goto L_0x0033;
                case -262743844: goto L_0x0029;
                case 1164924125: goto L_0x001f;
                case 1287171955: goto L_0x0015;
                default: goto L_0x0014;
            }
        L_0x0014:
            goto L_0x0051
        L_0x0015:
            java.lang.String r1 = "inferredRadioSignals"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0051
            r1 = 3
            goto L_0x0052
        L_0x001f:
            java.lang.String r1 = "inferredSnappedToRoad"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0051
            r1 = 5
            goto L_0x0052
        L_0x0029:
            java.lang.String r1 = "inferredReverseGeocoding"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0051
            r1 = 4
            goto L_0x0052
        L_0x0033:
            java.lang.String r1 = "unknown"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0051
            r1 = r2
            goto L_0x0052
        L_0x003d:
            java.lang.String r1 = "userReported"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0051
            r1 = r3
            goto L_0x0052
        L_0x0047:
            java.lang.String r1 = "inferredGeofencing"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0051
            r1 = 2
            goto L_0x0052
        L_0x0051:
            r1 = -1
        L_0x0052:
            switch(r1) {
                case 0: goto L_0x0056;
                case 1: goto L_0x0056;
                case 2: goto L_0x0056;
                case 3: goto L_0x0056;
                case 4: goto L_0x0056;
                case 5: goto L_0x0056;
                default: goto L_0x0055;
            }
        L_0x0055:
            goto L_0x0057
        L_0x0056:
            r2 = r3
        L_0x0057:
            java.lang.String r1 = "Invalid source"
            com.google.android.gms.common.internal.Preconditions.checkArgument(r2, r1)
            com.google.android.gms.location.places.PlaceReport r1 = new com.google.android.gms.location.places.PlaceReport
            r1.<init>(r3, r4, r5, r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.location.places.PlaceReport.create(java.lang.String, java.lang.String):com.google.android.gms.location.places.PlaceReport");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PlaceReport)) {
            return false;
        }
        PlaceReport placeReport = (PlaceReport) obj;
        return Objects.equal(this.zza, placeReport.zza) && Objects.equal(this.tag, placeReport.tag) && Objects.equal(this.zzb, placeReport.zzb);
    }

    public String getPlaceId() {
        return this.zza;
    }

    public String getTag() {
        return this.tag;
    }

    public int hashCode() {
        return Objects.hashCode(this.zza, this.tag, this.zzb);
    }

    public String toString() {
        ToStringHelper stringHelper = Objects.toStringHelper(this);
        stringHelper.add("placeId", this.zza);
        stringHelper.add("tag", this.tag);
        if (!"unknown".equals(this.zzb)) {
            stringHelper.add("source", this.zzb);
        }
        return stringHelper.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
        SafeParcelWriter.writeString(parcel, 2, getPlaceId(), false);
        SafeParcelWriter.writeString(parcel, 3, getTag(), false);
        SafeParcelWriter.writeString(parcel, 4, this.zzb, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
