package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.ArrayList;

public final class zzag implements Creator<LocationSettingsRequest> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        boolean z = false;
        ArrayList arrayList = null;
        boolean z2 = false;
        zzae zzae = null;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            int fieldId = SafeParcelReader.getFieldId(readHeader);
            if (fieldId != 5) {
                switch (fieldId) {
                    case 1:
                        arrayList = SafeParcelReader.createTypedList(parcel, readHeader, LocationRequest.CREATOR);
                        break;
                    case 2:
                        z = SafeParcelReader.readBoolean(parcel, readHeader);
                        break;
                    case 3:
                        z2 = SafeParcelReader.readBoolean(parcel, readHeader);
                        break;
                    default:
                        SafeParcelReader.skipUnknownField(parcel, readHeader);
                        break;
                }
            } else {
                zzae = (zzae) SafeParcelReader.createParcelable(parcel, readHeader, zzae.CREATOR);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new LocationSettingsRequest(arrayList, z, z2, zzae);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new LocationSettingsRequest[i];
    }
}
