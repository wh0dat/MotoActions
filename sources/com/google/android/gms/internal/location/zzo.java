package com.google.android.gms.internal.location;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.location.zzr;
import com.google.android.gms.location.zzs;

@Class(creator = "DeviceOrientationRequestUpdateDataCreator")
public final class zzo extends AbstractSafeParcelable {
    public static final Creator<zzo> CREATOR = new zzp();
    @Field(defaultValueUnchecked = "DeviceOrientationRequestUpdateData.OPERATION_ADD", mo9705id = 1)
    private int zzcg;
    @Field(defaultValueUnchecked = "null", mo9705id = 2)
    private zzm zzch;
    @Field(defaultValueUnchecked = "null", getter = "getDeviceOrientationListenerBinder", mo9705id = 3, type = "android.os.IBinder")
    private zzr zzci;
    @Field(defaultValueUnchecked = "null", getter = "getFusedLocationProviderCallbackBinder", mo9705id = 4, type = "android.os.IBinder")
    private zzaj zzcj;

    @Constructor
    zzo(@Param(mo9708id = 1) int i, @Param(mo9708id = 2) zzm zzm, @Param(mo9708id = 3) IBinder iBinder, @Param(mo9708id = 4) IBinder iBinder2) {
        this.zzcg = i;
        this.zzch = zzm;
        zzaj zzaj = null;
        this.zzci = iBinder == null ? null : zzs.zza(iBinder);
        if (!(iBinder2 == null || iBinder2 == null)) {
            IInterface queryLocalInterface = iBinder2.queryLocalInterface("com.google.android.gms.location.internal.IFusedLocationProviderCallback");
            zzaj = queryLocalInterface instanceof zzaj ? (zzaj) queryLocalInterface : new zzal(iBinder2);
        }
        this.zzcj = zzaj;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzcg);
        SafeParcelWriter.writeParcelable(parcel, 2, this.zzch, i, false);
        IBinder iBinder = null;
        SafeParcelWriter.writeIBinder(parcel, 3, this.zzci == null ? null : this.zzci.asBinder(), false);
        if (this.zzcj != null) {
            iBinder = this.zzcj.asBinder();
        }
        SafeParcelWriter.writeIBinder(parcel, 4, iBinder, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
