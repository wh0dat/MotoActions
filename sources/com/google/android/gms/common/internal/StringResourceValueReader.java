package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import com.google.android.gms.common.C0499R;
import javax.annotation.Nullable;

public class StringResourceValueReader {
    private final Resources zzvb;
    private final String zzvc = this.zzvb.getResourcePackageName(C0499R.string.common_google_play_services_unknown_issue);

    public StringResourceValueReader(Context context) {
        Preconditions.checkNotNull(context);
        this.zzvb = context.getResources();
    }

    @Nullable
    public String getString(String str) {
        int identifier = this.zzvb.getIdentifier(str, "string", this.zzvc);
        if (identifier == 0) {
            return null;
        }
        return this.zzvb.getString(identifier);
    }
}
