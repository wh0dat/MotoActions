package com.motorola.actions.p013ui;

import android.app.Fragment;
import com.motorola.actions.discovery.fdn.FDNSession;

/* renamed from: com.motorola.actions.ui.FDNAwareFragment */
public class FDNAwareFragment extends Fragment {
    /* access modifiers changed from: protected */
    public FDNSession getFDNSession() {
        return ((ActionsBaseActivity) getActivity()).getFDNSession();
    }
}
