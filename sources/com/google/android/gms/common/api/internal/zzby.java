package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api.AbstractClientBuilder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.ClientSettings;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import com.google.android.gms.signin.SignIn;
import com.google.android.gms.signin.SignInClient;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.signin.internal.BaseSignInCallbacks;
import com.google.android.gms.signin.internal.SignInResponse;
import java.util.Set;

public final class zzby extends BaseSignInCallbacks implements ConnectionCallbacks, OnConnectionFailedListener {
    private static AbstractClientBuilder<? extends SignInClient, SignInOptions> zzlv = SignIn.CLIENT_BUILDER;
    private final Context mContext;
    private final Handler mHandler;
    private Set<Scope> mScopes;
    private final AbstractClientBuilder<? extends SignInClient, SignInOptions> zzby;
    private ClientSettings zzgf;
    private SignInClient zzhn;
    /* access modifiers changed from: private */
    public zzcb zzlw;

    @WorkerThread
    public zzby(Context context, Handler handler, @NonNull ClientSettings clientSettings) {
        this(context, handler, clientSettings, zzlv);
    }

    @WorkerThread
    public zzby(Context context, Handler handler, @NonNull ClientSettings clientSettings, AbstractClientBuilder<? extends SignInClient, SignInOptions> abstractClientBuilder) {
        this.mContext = context;
        this.mHandler = handler;
        this.zzgf = (ClientSettings) Preconditions.checkNotNull(clientSettings, "ClientSettings must not be null");
        this.mScopes = clientSettings.getRequiredScopes();
        this.zzby = abstractClientBuilder;
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public final void zzb(SignInResponse signInResponse) {
        ConnectionResult connectionResult = signInResponse.getConnectionResult();
        if (connectionResult.isSuccess()) {
            ResolveAccountResponse resolveAccountResponse = signInResponse.getResolveAccountResponse();
            connectionResult = resolveAccountResponse.getConnectionResult();
            if (!connectionResult.isSuccess()) {
                String valueOf = String.valueOf(connectionResult);
                StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 48);
                sb.append("Sign-in succeeded with resolve account failure: ");
                sb.append(valueOf);
                Log.wtf("SignInCoordinator", sb.toString(), new Exception());
            } else {
                this.zzlw.zza(resolveAccountResponse.getAccountAccessor(), this.mScopes);
                this.zzhn.disconnect();
            }
        }
        this.zzlw.zzg(connectionResult);
        this.zzhn.disconnect();
    }

    @WorkerThread
    public final void onConnected(@Nullable Bundle bundle) {
        this.zzhn.signIn(this);
    }

    @WorkerThread
    public final void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        this.zzlw.zzg(connectionResult);
    }

    @WorkerThread
    public final void onConnectionSuspended(int i) {
        this.zzhn.disconnect();
    }

    @BinderThread
    public final void onSignInComplete(SignInResponse signInResponse) {
        this.mHandler.post(new zzca(this, signInResponse));
    }

    @WorkerThread
    public final void zza(zzcb zzcb) {
        if (this.zzhn != null) {
            this.zzhn.disconnect();
        }
        this.zzgf.setClientSessionId(Integer.valueOf(System.identityHashCode(this)));
        this.zzhn = (SignInClient) this.zzby.buildClient(this.mContext, this.mHandler.getLooper(), this.zzgf, this.zzgf.getSignInOptions(), this, this);
        this.zzlw = zzcb;
        if (this.mScopes == null || this.mScopes.isEmpty()) {
            this.mHandler.post(new zzbz(this));
        } else {
            this.zzhn.connect();
        }
    }

    public final SignInClient zzbt() {
        return this.zzhn;
    }

    public final void zzbz() {
        if (this.zzhn != null) {
            this.zzhn.disconnect();
        }
    }
}
