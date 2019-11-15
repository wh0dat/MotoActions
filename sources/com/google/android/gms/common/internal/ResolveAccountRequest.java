package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.Nullable;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;

@Class(creator = "ResolveAccountRequestCreator")
public class ResolveAccountRequest extends AbstractSafeParcelable {
    public static final Creator<ResolveAccountRequest> CREATOR = new ResolveAccountRequestCreator();
    @VersionField(mo9711id = 1)
    private final int zzal;
    @Field(getter = "getAccount", mo9705id = 2)
    private final Account zzs;
    @Field(getter = "getSessionId", mo9705id = 3)
    private final int zzut;
    @Field(getter = "getSignInAccountHint", mo9705id = 4)
    private final GoogleSignInAccount zzuu;

    @Constructor
    ResolveAccountRequest(@Param(mo9708id = 1) int i, @Param(mo9708id = 2) Account account, @Param(mo9708id = 3) int i2, @Param(mo9708id = 4) GoogleSignInAccount googleSignInAccount) {
        this.zzal = i;
        this.zzs = account;
        this.zzut = i2;
        this.zzuu = googleSignInAccount;
    }

    public ResolveAccountRequest(Account account, int i, GoogleSignInAccount googleSignInAccount) {
        this(2, account, i, googleSignInAccount);
    }

    public Account getAccount() {
        return this.zzs;
    }

    public int getSessionId() {
        return this.zzut;
    }

    @Nullable
    public GoogleSignInAccount getSignInAccountHint() {
        return this.zzuu;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeParcelable(parcel, 2, getAccount(), i, false);
        SafeParcelWriter.writeInt(parcel, 3, getSessionId());
        SafeParcelWriter.writeParcelable(parcel, 4, getSignInAccountHint(), i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
