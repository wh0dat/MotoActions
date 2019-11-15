package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension.TypeId;
import com.google.android.gms.auth.api.signin.internal.GoogleSignInOptionsExtensionParcelable;
import com.google.android.gms.auth.api.signin.internal.HashAccumulator;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Api.ApiOptions.Optional;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.AccountType;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import com.google.android.gms.common.util.VisibleForTesting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Class(creator = "GoogleSignInOptionsCreator")
public class GoogleSignInOptions extends AbstractSafeParcelable implements Optional, ReflectedParcelable {
    public static final Creator<GoogleSignInOptions> CREATOR = new GoogleSignInOptionsCreator();
    public static final GoogleSignInOptions DEFAULT_GAMES_SIGN_IN = new Builder().requestScopes(SCOPE_GAMES_LITE, new Scope[0]).build();
    public static final GoogleSignInOptions DEFAULT_SIGN_IN = new Builder().requestId().requestProfile().build();
    @VisibleForTesting
    public static final Scope SCOPE_EMAIL = new Scope("email");
    @VisibleForTesting
    public static final Scope SCOPE_GAMES = new Scope(Scopes.GAMES);
    @VisibleForTesting
    public static final Scope SCOPE_GAMES_LITE = new Scope(Scopes.GAMES_LITE);
    @VisibleForTesting
    public static final Scope SCOPE_OPEN_ID = new Scope(Scopes.OPEN_ID);
    @VisibleForTesting
    public static final Scope SCOPE_PROFILE = new Scope(Scopes.PROFILE);
    private static Comparator<Scope> zzaa = new zzb();
    @VersionField(mo9711id = 1)
    private final int versionCode;
    /* access modifiers changed from: private */
    @Field(getter = "getScopes", mo9705id = 2)
    public final ArrayList<Scope> zzr;
    /* access modifiers changed from: private */
    @Field(getter = "getAccount", mo9705id = 3)
    public Account zzs;
    /* access modifiers changed from: private */
    @Field(getter = "isIdTokenRequested", mo9705id = 4)
    public boolean zzt;
    /* access modifiers changed from: private */
    @Field(getter = "isServerAuthCodeRequested", mo9705id = 5)
    public final boolean zzu;
    /* access modifiers changed from: private */
    @Field(getter = "isForceCodeForRefreshToken", mo9705id = 6)
    public final boolean zzv;
    /* access modifiers changed from: private */
    @Field(getter = "getServerClientId", mo9705id = 7)
    public String zzw;
    /* access modifiers changed from: private */
    @Field(getter = "getHostedDomain", mo9705id = 8)
    public String zzx;
    /* access modifiers changed from: private */
    @Field(getter = "getExtensions", mo9705id = 9)
    public ArrayList<GoogleSignInOptionsExtensionParcelable> zzy;
    private Map<Integer, GoogleSignInOptionsExtensionParcelable> zzz;

    public static final class Builder {
        private Set<Scope> mScopes = new HashSet();
        private Map<Integer, GoogleSignInOptionsExtensionParcelable> zzab = new HashMap();
        private Account zzs;
        private boolean zzt;
        private boolean zzu;
        private boolean zzv;
        private String zzw;
        private String zzx;

        public Builder() {
        }

        public Builder(@NonNull GoogleSignInOptions googleSignInOptions) {
            Preconditions.checkNotNull(googleSignInOptions);
            this.mScopes = new HashSet(googleSignInOptions.zzr);
            this.zzu = googleSignInOptions.zzu;
            this.zzv = googleSignInOptions.zzv;
            this.zzt = googleSignInOptions.zzt;
            this.zzw = googleSignInOptions.zzw;
            this.zzs = googleSignInOptions.zzs;
            this.zzx = googleSignInOptions.zzx;
            this.zzab = GoogleSignInOptions.zza((List<GoogleSignInOptionsExtensionParcelable>) googleSignInOptions.zzy);
        }

        private final String zza(String str) {
            Preconditions.checkNotEmpty(str);
            Preconditions.checkArgument(this.zzw == null || this.zzw.equals(str), "two different server client ids provided");
            return str;
        }

        public final Builder addExtension(GoogleSignInOptionsExtension googleSignInOptionsExtension) {
            if (this.zzab.containsKey(Integer.valueOf(googleSignInOptionsExtension.getExtensionType()))) {
                throw new IllegalStateException("Only one extension per type may be added");
            }
            if (googleSignInOptionsExtension.getImpliedScopes() != null) {
                this.mScopes.addAll(googleSignInOptionsExtension.getImpliedScopes());
            }
            this.zzab.put(Integer.valueOf(googleSignInOptionsExtension.getExtensionType()), new GoogleSignInOptionsExtensionParcelable(googleSignInOptionsExtension));
            return this;
        }

        public final GoogleSignInOptions build() {
            if (this.mScopes.contains(GoogleSignInOptions.SCOPE_GAMES) && this.mScopes.contains(GoogleSignInOptions.SCOPE_GAMES_LITE)) {
                this.mScopes.remove(GoogleSignInOptions.SCOPE_GAMES_LITE);
            }
            if (this.zzt && (this.zzs == null || !this.mScopes.isEmpty())) {
                requestId();
            }
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions(3, new ArrayList(this.mScopes), this.zzs, this.zzt, this.zzu, this.zzv, this.zzw, this.zzx, this.zzab, null);
            return googleSignInOptions;
        }

        public final Builder requestEmail() {
            this.mScopes.add(GoogleSignInOptions.SCOPE_EMAIL);
            return this;
        }

        public final Builder requestId() {
            this.mScopes.add(GoogleSignInOptions.SCOPE_OPEN_ID);
            return this;
        }

        public final Builder requestIdToken(String str) {
            this.zzt = true;
            this.zzw = zza(str);
            return this;
        }

        public final Builder requestPhatIdToken(String str) {
            return requestIdToken(str).requestProfile().requestEmail();
        }

        public final Builder requestProfile() {
            this.mScopes.add(GoogleSignInOptions.SCOPE_PROFILE);
            return this;
        }

        public final Builder requestScopes(Scope scope, Scope... scopeArr) {
            this.mScopes.add(scope);
            this.mScopes.addAll(Arrays.asList(scopeArr));
            return this;
        }

        public final Builder requestServerAuthCode(String str) {
            return requestServerAuthCode(str, false);
        }

        public final Builder requestServerAuthCode(String str, boolean z) {
            this.zzu = true;
            this.zzw = zza(str);
            this.zzv = z;
            return this;
        }

        public final Builder setAccount(Account account) {
            this.zzs = (Account) Preconditions.checkNotNull(account);
            return this;
        }

        public final Builder setAccountName(String str) {
            this.zzs = new Account(Preconditions.checkNotEmpty(str), AccountType.GOOGLE);
            return this;
        }

        public final Builder setHostedDomain(String str) {
            this.zzx = Preconditions.checkNotEmpty(str);
            return this;
        }
    }

    @Constructor
    GoogleSignInOptions(@Param(mo9708id = 1) int i, @Param(mo9708id = 2) ArrayList<Scope> arrayList, @Param(mo9708id = 3) Account account, @Param(mo9708id = 4) boolean z, @Param(mo9708id = 5) boolean z2, @Param(mo9708id = 6) boolean z3, @Param(mo9708id = 7) String str, @Param(mo9708id = 8) String str2, @Param(mo9708id = 9) ArrayList<GoogleSignInOptionsExtensionParcelable> arrayList2) {
        this(i, arrayList, account, z, z2, z3, str, str2, zza((List<GoogleSignInOptionsExtensionParcelable>) arrayList2));
    }

    private GoogleSignInOptions(int i, ArrayList<Scope> arrayList, Account account, boolean z, boolean z2, boolean z3, String str, String str2, Map<Integer, GoogleSignInOptionsExtensionParcelable> map) {
        this.versionCode = i;
        this.zzr = arrayList;
        this.zzs = account;
        this.zzt = z;
        this.zzu = z2;
        this.zzv = z3;
        this.zzw = str;
        this.zzx = str2;
        this.zzy = new ArrayList<>(map.values());
        this.zzz = map;
    }

    /* synthetic */ GoogleSignInOptions(int i, ArrayList arrayList, Account account, boolean z, boolean z2, boolean z3, String str, String str2, Map map, zzb zzb) {
        this(3, arrayList, account, z, z2, z3, str, str2, map);
    }

    @Nullable
    public static GoogleSignInOptions fromJsonString(@Nullable String str) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        JSONObject jSONObject = new JSONObject(str);
        HashSet hashSet = new HashSet();
        JSONArray jSONArray = jSONObject.getJSONArray(Constants.KEY_SCOPES);
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            hashSet.add(new Scope(jSONArray.getString(i)));
        }
        String optString = jSONObject.optString("accountName", null);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions(3, new ArrayList<>(hashSet), !TextUtils.isEmpty(optString) ? new Account(optString, AccountType.GOOGLE) : null, jSONObject.getBoolean("idTokenRequested"), jSONObject.getBoolean("serverAuthRequested"), jSONObject.getBoolean("forceCodeForRefreshToken"), jSONObject.optString("serverClientId", null), jSONObject.optString("hostedDomain", null), (Map<Integer, GoogleSignInOptionsExtensionParcelable>) new HashMap<Integer,GoogleSignInOptionsExtensionParcelable>());
        return googleSignInOptions;
    }

    /* access modifiers changed from: private */
    public static Map<Integer, GoogleSignInOptionsExtensionParcelable> zza(@Nullable List<GoogleSignInOptionsExtensionParcelable> list) {
        HashMap hashMap = new HashMap();
        if (list == null) {
            return hashMap;
        }
        for (GoogleSignInOptionsExtensionParcelable googleSignInOptionsExtensionParcelable : list) {
            hashMap.put(Integer.valueOf(googleSignInOptionsExtensionParcelable.getType()), googleSignInOptionsExtensionParcelable);
        }
        return hashMap;
    }

    private final JSONObject zza() {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONArray jSONArray = new JSONArray();
            Collections.sort(this.zzr, zzaa);
            ArrayList arrayList = this.zzr;
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                jSONArray.put(((Scope) obj).getScopeUri());
            }
            jSONObject.put(Constants.KEY_SCOPES, jSONArray);
            if (this.zzs != null) {
                jSONObject.put("accountName", this.zzs.name);
            }
            jSONObject.put("idTokenRequested", this.zzt);
            jSONObject.put("forceCodeForRefreshToken", this.zzv);
            jSONObject.put("serverAuthRequested", this.zzu);
            if (!TextUtils.isEmpty(this.zzw)) {
                jSONObject.put("serverClientId", this.zzw);
            }
            if (!TextUtils.isEmpty(this.zzx)) {
                jSONObject.put("hostedDomain", this.zzx);
            }
            return jSONObject;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0049, code lost:
        if (r3.zzs.equals(r4.getAccount()) != false) goto L_0x004b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0068, code lost:
        if (r3.zzw.equals(r4.getServerClientId()) != false) goto L_0x006a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = 0
            if (r4 != 0) goto L_0x0004
            return r0
        L_0x0004:
            com.google.android.gms.auth.api.signin.GoogleSignInOptions r4 = (com.google.android.gms.auth.api.signin.GoogleSignInOptions) r4     // Catch:{ ClassCastException -> 0x0084 }
            java.util.ArrayList<com.google.android.gms.auth.api.signin.internal.GoogleSignInOptionsExtensionParcelable> r1 = r3.zzy     // Catch:{ ClassCastException -> 0x0084 }
            int r1 = r1.size()     // Catch:{ ClassCastException -> 0x0084 }
            if (r1 > 0) goto L_0x0084
            java.util.ArrayList<com.google.android.gms.auth.api.signin.internal.GoogleSignInOptionsExtensionParcelable> r1 = r4.zzy     // Catch:{ ClassCastException -> 0x0084 }
            int r1 = r1.size()     // Catch:{ ClassCastException -> 0x0084 }
            if (r1 <= 0) goto L_0x0017
            return r0
        L_0x0017:
            java.util.ArrayList<com.google.android.gms.common.api.Scope> r1 = r3.zzr     // Catch:{ ClassCastException -> 0x0084 }
            int r1 = r1.size()     // Catch:{ ClassCastException -> 0x0084 }
            java.util.ArrayList r2 = r4.getScopes()     // Catch:{ ClassCastException -> 0x0084 }
            int r2 = r2.size()     // Catch:{ ClassCastException -> 0x0084 }
            if (r1 != r2) goto L_0x0084
            java.util.ArrayList<com.google.android.gms.common.api.Scope> r1 = r3.zzr     // Catch:{ ClassCastException -> 0x0084 }
            java.util.ArrayList r2 = r4.getScopes()     // Catch:{ ClassCastException -> 0x0084 }
            boolean r1 = r1.containsAll(r2)     // Catch:{ ClassCastException -> 0x0084 }
            if (r1 != 0) goto L_0x0034
            return r0
        L_0x0034:
            android.accounts.Account r1 = r3.zzs     // Catch:{ ClassCastException -> 0x0084 }
            if (r1 != 0) goto L_0x003f
            android.accounts.Account r1 = r4.getAccount()     // Catch:{ ClassCastException -> 0x0084 }
            if (r1 != 0) goto L_0x0084
            goto L_0x004b
        L_0x003f:
            android.accounts.Account r1 = r3.zzs     // Catch:{ ClassCastException -> 0x0084 }
            android.accounts.Account r2 = r4.getAccount()     // Catch:{ ClassCastException -> 0x0084 }
            boolean r1 = r1.equals(r2)     // Catch:{ ClassCastException -> 0x0084 }
            if (r1 == 0) goto L_0x0084
        L_0x004b:
            java.lang.String r1 = r3.zzw     // Catch:{ ClassCastException -> 0x0084 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ ClassCastException -> 0x0084 }
            if (r1 == 0) goto L_0x005e
            java.lang.String r1 = r4.getServerClientId()     // Catch:{ ClassCastException -> 0x0084 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ ClassCastException -> 0x0084 }
            if (r1 == 0) goto L_0x0084
            goto L_0x006a
        L_0x005e:
            java.lang.String r1 = r3.zzw     // Catch:{ ClassCastException -> 0x0084 }
            java.lang.String r2 = r4.getServerClientId()     // Catch:{ ClassCastException -> 0x0084 }
            boolean r1 = r1.equals(r2)     // Catch:{ ClassCastException -> 0x0084 }
            if (r1 == 0) goto L_0x0084
        L_0x006a:
            boolean r1 = r3.zzv     // Catch:{ ClassCastException -> 0x0084 }
            boolean r2 = r4.isForceCodeForRefreshToken()     // Catch:{ ClassCastException -> 0x0084 }
            if (r1 != r2) goto L_0x0084
            boolean r1 = r3.zzt     // Catch:{ ClassCastException -> 0x0084 }
            boolean r2 = r4.isIdTokenRequested()     // Catch:{ ClassCastException -> 0x0084 }
            if (r1 != r2) goto L_0x0084
            boolean r3 = r3.zzu     // Catch:{ ClassCastException -> 0x0084 }
            boolean r4 = r4.isServerAuthCodeRequested()     // Catch:{ ClassCastException -> 0x0084 }
            if (r3 != r4) goto L_0x0084
            r3 = 1
            return r3
        L_0x0084:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.GoogleSignInOptions.equals(java.lang.Object):boolean");
    }

    public Account getAccount() {
        return this.zzs;
    }

    public GoogleSignInOptionsExtensionParcelable getExtension(@TypeId int i) {
        return (GoogleSignInOptionsExtensionParcelable) this.zzz.get(Integer.valueOf(i));
    }

    public ArrayList<GoogleSignInOptionsExtensionParcelable> getExtensions() {
        return this.zzy;
    }

    public String getHostedDomain() {
        return this.zzx;
    }

    public Scope[] getScopeArray() {
        return (Scope[]) this.zzr.toArray(new Scope[this.zzr.size()]);
    }

    public ArrayList<Scope> getScopes() {
        return new ArrayList<>(this.zzr);
    }

    public String getServerClientId() {
        return this.zzw;
    }

    public boolean hasExtension(@TypeId int i) {
        return this.zzz.containsKey(Integer.valueOf(i));
    }

    public int hashCode() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = this.zzr;
        int size = arrayList2.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList2.get(i);
            i++;
            arrayList.add(((Scope) obj).getScopeUri());
        }
        Collections.sort(arrayList);
        return new HashAccumulator().addObject(arrayList).addObject(this.zzs).addObject(this.zzw).addBoolean(this.zzv).addBoolean(this.zzt).addBoolean(this.zzu).hash();
    }

    public boolean isForceCodeForRefreshToken() {
        return this.zzv;
    }

    public boolean isIdTokenRequested() {
        return this.zzt;
    }

    public boolean isServerAuthCodeRequested() {
        return this.zzu;
    }

    public String toJson() {
        return zza().toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
        SafeParcelWriter.writeTypedList(parcel, 2, getScopes(), false);
        SafeParcelWriter.writeParcelable(parcel, 3, getAccount(), i, false);
        SafeParcelWriter.writeBoolean(parcel, 4, isIdTokenRequested());
        SafeParcelWriter.writeBoolean(parcel, 5, isServerAuthCodeRequested());
        SafeParcelWriter.writeBoolean(parcel, 6, isForceCodeForRefreshToken());
        SafeParcelWriter.writeString(parcel, 7, getServerClientId(), false);
        SafeParcelWriter.writeString(parcel, 8, getHostedDomain(), false);
        SafeParcelWriter.writeTypedList(parcel, 9, getExtensions(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
