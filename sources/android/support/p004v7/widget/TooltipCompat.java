package android.support.p004v7.widget;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/* renamed from: android.support.v7.widget.TooltipCompat */
public class TooltipCompat {
    private static final ViewCompatImpl IMPL;

    @TargetApi(26)
    /* renamed from: android.support.v7.widget.TooltipCompat$Api26ViewCompatImpl */
    private static class Api26ViewCompatImpl implements ViewCompatImpl {
        private Api26ViewCompatImpl() {
        }

        public void setTooltipText(@NonNull View view, @Nullable CharSequence charSequence) {
            view.setTooltipText(charSequence);
        }
    }

    /* renamed from: android.support.v7.widget.TooltipCompat$BaseViewCompatImpl */
    private static class BaseViewCompatImpl implements ViewCompatImpl {
        private BaseViewCompatImpl() {
        }

        public void setTooltipText(@NonNull View view, @Nullable CharSequence charSequence) {
            TooltipCompatHandler.setTooltipText(view, charSequence);
        }
    }

    /* renamed from: android.support.v7.widget.TooltipCompat$ViewCompatImpl */
    private interface ViewCompatImpl {
        void setTooltipText(@NonNull View view, @Nullable CharSequence charSequence);
    }

    static {
        if (VERSION.SDK_INT >= 26) {
            IMPL = new Api26ViewCompatImpl();
        } else {
            IMPL = new BaseViewCompatImpl();
        }
    }

    public static void setTooltipText(@NonNull View view, @Nullable CharSequence charSequence) {
        IMPL.setTooltipText(view, charSequence);
    }

    private TooltipCompat() {
    }
}
