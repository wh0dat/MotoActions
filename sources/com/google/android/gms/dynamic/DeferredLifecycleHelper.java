package com.google.android.gms.dynamic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.internal.ConnectionErrorMessages;
import com.google.android.gms.dynamic.LifecycleDelegate;
import java.util.LinkedList;

public abstract class DeferredLifecycleHelper<T extends LifecycleDelegate> {
    /* access modifiers changed from: private */
    public T zzabc;
    /* access modifiers changed from: private */
    public Bundle zzabd;
    /* access modifiers changed from: private */
    public LinkedList<zza> zzabe;
    private final OnDelegateCreatedListener<T> zzabf = new zza(this);

    private interface zza {
        int getState();

        void zza(LifecycleDelegate lifecycleDelegate);
    }

    public static void showGooglePlayUnavailableMessage(FrameLayout frameLayout) {
        GoogleApiAvailability instance = GoogleApiAvailability.getInstance();
        Context context = frameLayout.getContext();
        int isGooglePlayServicesAvailable = instance.isGooglePlayServicesAvailable(context);
        String errorMessage = ConnectionErrorMessages.getErrorMessage(context, isGooglePlayServicesAvailable);
        String errorDialogButtonMessage = ConnectionErrorMessages.getErrorDialogButtonMessage(context, isGooglePlayServicesAvailable);
        LinearLayout linearLayout = new LinearLayout(frameLayout.getContext());
        linearLayout.setOrientation(1);
        linearLayout.setLayoutParams(new LayoutParams(-2, -2));
        frameLayout.addView(linearLayout);
        TextView textView = new TextView(frameLayout.getContext());
        textView.setLayoutParams(new LayoutParams(-2, -2));
        textView.setText(errorMessage);
        linearLayout.addView(textView);
        Intent errorResolutionIntent = instance.getErrorResolutionIntent(context, isGooglePlayServicesAvailable, null);
        if (errorResolutionIntent != null) {
            Button button = new Button(context);
            button.setId(16908313);
            button.setLayoutParams(new LayoutParams(-2, -2));
            button.setText(errorDialogButtonMessage);
            linearLayout.addView(button);
            button.setOnClickListener(new zze(context, errorResolutionIntent));
        }
    }

    private final void zza(Bundle bundle, zza zza2) {
        if (this.zzabc != null) {
            zza2.zza(this.zzabc);
            return;
        }
        if (this.zzabe == null) {
            this.zzabe = new LinkedList<>();
        }
        this.zzabe.add(zza2);
        if (bundle != null) {
            if (this.zzabd == null) {
                this.zzabd = (Bundle) bundle.clone();
            } else {
                this.zzabd.putAll(bundle);
            }
        }
        createDelegate(this.zzabf);
    }

    private final void zzm(int i) {
        while (!this.zzabe.isEmpty() && ((zza) this.zzabe.getLast()).getState() >= i) {
            this.zzabe.removeLast();
        }
    }

    /* access modifiers changed from: protected */
    public abstract void createDelegate(OnDelegateCreatedListener<T> onDelegateCreatedListener);

    public T getDelegate() {
        return this.zzabc;
    }

    /* access modifiers changed from: protected */
    public void handleGooglePlayUnavailable(FrameLayout frameLayout) {
        showGooglePlayUnavailableMessage(frameLayout);
    }

    public void onCreate(Bundle bundle) {
        zza(bundle, (zza) new zzc(this, bundle));
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FrameLayout frameLayout = new FrameLayout(layoutInflater.getContext());
        zzd zzd = new zzd(this, frameLayout, layoutInflater, viewGroup, bundle);
        zza(bundle, (zza) zzd);
        if (this.zzabc == null) {
            handleGooglePlayUnavailable(frameLayout);
        }
        return frameLayout;
    }

    public void onDestroy() {
        if (this.zzabc != null) {
            this.zzabc.onDestroy();
        } else {
            zzm(1);
        }
    }

    public void onDestroyView() {
        if (this.zzabc != null) {
            this.zzabc.onDestroyView();
        } else {
            zzm(2);
        }
    }

    public void onInflate(Activity activity, Bundle bundle, Bundle bundle2) {
        zza(bundle2, (zza) new zzb(this, activity, bundle, bundle2));
    }

    public void onLowMemory() {
        if (this.zzabc != null) {
            this.zzabc.onLowMemory();
        }
    }

    public void onPause() {
        if (this.zzabc != null) {
            this.zzabc.onPause();
        } else {
            zzm(5);
        }
    }

    public void onResume() {
        zza((Bundle) null, (zza) new zzg(this));
    }

    public void onSaveInstanceState(Bundle bundle) {
        if (this.zzabc != null) {
            this.zzabc.onSaveInstanceState(bundle);
            return;
        }
        if (this.zzabd != null) {
            bundle.putAll(this.zzabd);
        }
    }

    public void onStart() {
        zza((Bundle) null, (zza) new zzf(this));
    }

    public void onStop() {
        if (this.zzabc != null) {
            this.zzabc.onStop();
        } else {
            zzm(4);
        }
    }
}
