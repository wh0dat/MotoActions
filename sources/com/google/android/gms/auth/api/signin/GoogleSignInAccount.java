package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.AccountType;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.DefaultClock;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Class(creator = "GoogleSignInAccountCreator")
public class GoogleSignInAccount extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<GoogleSignInAccount> CREATOR = new GoogleSignInAccountCreator();
    @VisibleForTesting
    public static Clock sClock = DefaultClock.getInstance();
    @VersionField(mo9711id = 1)
    private final int versionCode;
    @Field(getter = "getId", mo9705id = 2)
    private String zze;
    @Field(getter = "getIdToken", mo9705id = 3)
    private String zzf;
    @Field(getter = "getEmail", mo9705id = 4)
    private String zzg;
    @Field(getter = "getDisplayName", mo9705id = 5)
    private String zzh;
    @Field(getter = "getPhotoUrl", mo9705id = 6)
    private Uri zzi;
    @Field(getter = "getServerAuthCode", mo9705id = 7)
    private String zzj;
    @Field(getter = "getExpirationTimeSecs", mo9705id = 8)
    private long zzk;
    @Field(getter = "getObfuscatedIdentifier", mo9705id = 9)
    private String zzl;
    @Field(mo9705id = 10)
    private List<Scope> zzm;
    @Field(getter = "getGivenName", mo9705id = 11)
    private String zzn;
    @Field(getter = "getFamilyName", mo9705id = 12)
    private String zzo;
    private Set<Scope> zzp = new HashSet();

    @Constructor
    GoogleSignInAccount(@Param(mo9708id = 1) int i, @Param(mo9708id = 2) String str, @Param(mo9708id = 3) String str2, @Param(mo9708id = 4) String str3, @Param(mo9708id = 5) String str4, @Param(mo9708id = 6) Uri uri, @Param(mo9708id = 7) String str5, @Param(mo9708id = 8) long j, @Param(mo9708id = 9) String str6, @Param(mo9708id = 10) List<Scope> list, @Param(mo9708id = 11) String str7, @Param(mo9708id = 12) String str8) {
        this.versionCode = i;
        this.zze = str;
        this.zzf = str2;
        this.zzg = str3;
        this.zzh = str4;
        this.zzi = uri;
        this.zzj = str5;
        this.zzk = j;
        this.zzl = str6;
        this.zzm = list;
        this.zzn = str7;
        this.zzo = str8;
    }

    public static GoogleSignInAccount create(@Nullable String str, @Nullable String str2, @Nullable String str3, @Nullable String str4, @Nullable Uri uri, @Nullable Long l, @NonNull String str5, @NonNull Set<Scope> set) {
        return create(str, str2, str3, str4, null, null, uri, l, str5, set);
    }

    public static GoogleSignInAccount create(@Nullable String str, @Nullable String str2, @Nullable String str3, @Nullable String str4, @Nullable String str5, @Nullable String str6, @Nullable Uri uri, @Nullable Long l, @NonNull String str7, @NonNull Set<Scope> set) {
        GoogleSignInAccount googleSignInAccount = new GoogleSignInAccount(3, str, str2, str3, str4, uri, null, (l == null ? Long.valueOf(sClock.currentTimeMillis() / 1000) : l).longValue(), Preconditions.checkNotEmpty(str7), new ArrayList((Collection) Preconditions.checkNotNull(set)), str5, str6);
        return googleSignInAccount;
    }

    public static GoogleSignInAccount createDefault() {
        return zza(new Account("<<default account>>", AccountType.GOOGLE), (Set<Scope>) new HashSet<Scope>());
    }

    public static GoogleSignInAccount fromAccountAndScopes(@NonNull Account account, @NonNull Scope scope, Scope... scopeArr) {
        Preconditions.checkNotNull(account);
        Preconditions.checkNotNull(scope);
        HashSet hashSet = new HashSet();
        hashSet.add(scope);
        hashSet.addAll(Arrays.asList(scopeArr));
        return zza(account, (Set<Scope>) hashSet);
    }

    @Nullable
    public static GoogleSignInAccount fromJsonString(@Nullable String str) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        JSONObject jSONObject = new JSONObject(str);
        String optString = jSONObject.optString("photoUrl", null);
        Uri parse = !TextUtils.isEmpty(optString) ? Uri.parse(optString) : null;
        long parseLong = Long.parseLong(jSONObject.getString("expirationTime"));
        HashSet hashSet = new HashSet();
        JSONArray jSONArray = jSONObject.getJSONArray("grantedScopes");
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            hashSet.add(new Scope(jSONArray.getString(i)));
        }
        return create(jSONObject.optString("id"), jSONObject.optString("tokenId", null), jSONObject.optString("email", null), jSONObject.optString("displayName", null), jSONObject.optString("givenName", null), jSONObject.optString("familyName", null), parse, Long.valueOf(parseLong), jSONObject.getString("obfuscatedIdentifier"), hashSet).setServerAuthCode(jSONObject.optString("serverAuthCode", null));
    }

    private static GoogleSignInAccount zza(Account account, Set<Scope> set) {
        return create(null, null, account.name, null, null, null, null, Long.valueOf(0), account.name, set);
    }

    private final JSONObject zza() {
        JSONObject jSONObject = new JSONObject();
        try {
            if (getId() != null) {
                jSONObject.put("id", getId());
            }
            if (getIdToken() != null) {
                jSONObject.put("tokenId", getIdToken());
            }
            if (getEmail() != null) {
                jSONObject.put("email", getEmail());
            }
            if (getDisplayName() != null) {
                jSONObject.put("displayName", getDisplayName());
            }
            if (getGivenName() != null) {
                jSONObject.put("givenName", getGivenName());
            }
            if (getFamilyName() != null) {
                jSONObject.put("familyName", getFamilyName());
            }
            if (getPhotoUrl() != null) {
                jSONObject.put("photoUrl", getPhotoUrl().toString());
            }
            if (getServerAuthCode() != null) {
                jSONObject.put("serverAuthCode", getServerAuthCode());
            }
            jSONObject.put("expirationTime", this.zzk);
            jSONObject.put("obfuscatedIdentifier", getObfuscatedIdentifier());
            JSONArray jSONArray = new JSONArray();
            Scope[] scopeArr = (Scope[]) this.zzm.toArray(new Scope[this.zzm.size()]);
            Arrays.sort(scopeArr, zza.zzq);
            for (Scope scopeUri : scopeArr) {
                jSONArray.put(scopeUri.getScopeUri());
            }
            jSONObject.put("grantedScopes", jSONArray);
            return jSONObject;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof GoogleSignInAccount)) {
            return false;
        }
        GoogleSignInAccount googleSignInAccount = (GoogleSignInAccount) obj;
        return googleSignInAccount.getObfuscatedIdentifier().equals(getObfuscatedIdentifier()) && googleSignInAccount.getRequestedScopes().equals(getRequestedScopes());
    }

    @Nullable
    public Account getAccount() {
        if (this.zzg == null) {
            return null;
        }
        return new Account(this.zzg, AccountType.GOOGLE);
    }

    @Nullable
    public String getDisplayName() {
        return this.zzh;
    }

    @Nullable
    public String getEmail() {
        return this.zzg;
    }

    public long getExpirationTimeSecs() {
        return this.zzk;
    }

    @Nullable
    public String getFamilyName() {
        return this.zzo;
    }

    @Nullable
    public String getGivenName() {
        return this.zzn;
    }

    @NonNull
    public Set<Scope> getGrantedScopes() {
        return new HashSet(this.zzm);
    }

    @Nullable
    public String getId() {
        return this.zze;
    }

    @Nullable
    public String getIdToken() {
        return this.zzf;
    }

    @NonNull
    public String getObfuscatedIdentifier() {
        return this.zzl;
    }

    @Nullable
    public Uri getPhotoUrl() {
        return this.zzi;
    }

    @NonNull
    public Set<Scope> getRequestedScopes() {
        HashSet hashSet = new HashSet(this.zzm);
        hashSet.addAll(this.zzp);
        return hashSet;
    }

    @Nullable
    public String getServerAuthCode() {
        return this.zzj;
    }

    public int hashCode() {
        return ((getObfuscatedIdentifier().hashCode() + 527) * 31) + getRequestedScopes().hashCode();
    }

    public boolean isExpired() {
        return sClock.currentTimeMillis() / 1000 >= this.zzk - 300;
    }

    public GoogleSignInAccount requestExtraScopes(Scope... scopeArr) {
        if (scopeArr != null) {
            Collections.addAll(this.zzp, scopeArr);
        }
        return this;
    }

    public GoogleSignInAccount setServerAuthCode(String str) {
        this.zzj = str;
        return this;
    }

    public String toJson() {
        return zza().toString();
    }

    public String toJsonForStorage() {
        JSONObject zza = zza();
        zza.remove("serverAuthCode");
        return zza.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
        SafeParcelWriter.writeString(parcel, 2, getId(), false);
        SafeParcelWriter.writeString(parcel, 3, getIdToken(), false);
        SafeParcelWriter.writeString(parcel, 4, getEmail(), false);
        SafeParcelWriter.writeString(parcel, 5, getDisplayName(), false);
        SafeParcelWriter.writeParcelable(parcel, 6, getPhotoUrl(), i, false);
        SafeParcelWriter.writeString(parcel, 7, getServerAuthCode(), false);
        SafeParcelWriter.writeLong(parcel, 8, getExpirationTimeSecs());
        SafeParcelWriter.writeString(parcel, 9, getObfuscatedIdentifier(), false);
        SafeParcelWriter.writeTypedList(parcel, 10, this.zzm, false);
        SafeParcelWriter.writeString(parcel, 11, getGivenName(), false);
        SafeParcelWriter.writeString(parcel, 12, getFamilyName(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
