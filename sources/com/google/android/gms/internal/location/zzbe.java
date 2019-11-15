package com.google.android.gms.internal.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ClientIdentity;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.location.LocationRequest;
import java.util.List;

public final class zzbe implements Creator<zzbd> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        List<ClientIdentity> list = zzbd.zzcd;
        boolean z = false;
        boolean z2 = false;
        boolean z3 = false;
        LocationRequest locationRequest = null;
        String str = null;
        String str2 = null;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            int fieldId = SafeParcelReader.getFieldId(readHeader);
            if (fieldId != 1) {
                switch (fieldId) {
                    case 5:
                        list = SafeParcelReader.createTypedList(parcel, readHeader, ClientIdentity.CREATOR);
                        break;
                    case 6:
                        str = SafeParcelReader.createString(parcel, readHeader);
                        break;
                    case 7:
                        z = SafeParcelReader.readBoolean(parcel, readHeader);
                        break;
                    case 8:
                        z2 = SafeParcelReader.readBoolean(parcel, readHeader);
                        break;
                    case 9:
                        z3 = SafeParcelReader.readBoolean(parcel, readHeader);
                        break;
                    case 10:
                        str2 = SafeParcelReader.createString(parcel, readHeader);
                        break;
                    default:
                        SafeParcelReader.skipUnknownField(parcel, readHeader);
                        break;
                }
            } else {
                locationRequest = (LocationRequest) SafeParcelReader.createParcelable(parcel, readHeader, LocationRequest.CREATOR);
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        zzbd zzbd = new zzbd(locationRequest, list, str, z, z2, z3, str2);
        return zzbd;
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzbd[i];
    }
}
