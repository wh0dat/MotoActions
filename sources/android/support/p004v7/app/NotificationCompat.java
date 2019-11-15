package android.support.p004v7.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.p001v4.app.BundleCompat;
import android.support.p001v4.media.session.MediaSessionCompat.Token;

@Deprecated
/* renamed from: android.support.v7.app.NotificationCompat */
public class NotificationCompat extends android.support.p001v4.app.NotificationCompat {

    @Deprecated
    /* renamed from: android.support.v7.app.NotificationCompat$Builder */
    public static class Builder extends android.support.p001v4.app.NotificationCompat.Builder {
        @Deprecated
        public Builder(Context context) {
            super(context);
        }
    }

    @Deprecated
    /* renamed from: android.support.v7.app.NotificationCompat$DecoratedCustomViewStyle */
    public static class DecoratedCustomViewStyle extends android.support.p001v4.app.NotificationCompat.DecoratedCustomViewStyle {
    }

    @Deprecated
    /* renamed from: android.support.v7.app.NotificationCompat$DecoratedMediaCustomViewStyle */
    public static class DecoratedMediaCustomViewStyle extends android.support.p001v4.media.app.NotificationCompat.DecoratedMediaCustomViewStyle {
    }

    @Deprecated
    /* renamed from: android.support.v7.app.NotificationCompat$MediaStyle */
    public static class MediaStyle extends android.support.p001v4.media.app.NotificationCompat.MediaStyle {
        @Deprecated
        public MediaStyle() {
        }

        @Deprecated
        public MediaStyle(android.support.p001v4.app.NotificationCompat.Builder builder) {
            super(builder);
        }

        @Deprecated
        public MediaStyle setShowActionsInCompactView(int... iArr) {
            return (MediaStyle) super.setShowActionsInCompactView(iArr);
        }

        @Deprecated
        public MediaStyle setMediaSession(Token token) {
            return (MediaStyle) super.setMediaSession(token);
        }

        @Deprecated
        public MediaStyle setShowCancelButton(boolean z) {
            return (MediaStyle) super.setShowCancelButton(z);
        }

        @Deprecated
        public MediaStyle setCancelButtonIntent(PendingIntent pendingIntent) {
            return (MediaStyle) super.setCancelButtonIntent(pendingIntent);
        }
    }

    @Deprecated
    public static Token getMediaSession(Notification notification) {
        Bundle extras = getExtras(notification);
        if (extras != null) {
            if (VERSION.SDK_INT >= 21) {
                Parcelable parcelable = extras.getParcelable(android.support.p001v4.app.NotificationCompat.EXTRA_MEDIA_SESSION);
                if (parcelable != null) {
                    return Token.fromToken(parcelable);
                }
            } else {
                IBinder binder = BundleCompat.getBinder(extras, android.support.p001v4.app.NotificationCompat.EXTRA_MEDIA_SESSION);
                if (binder != null) {
                    Parcel obtain = Parcel.obtain();
                    obtain.writeStrongBinder(binder);
                    obtain.setDataPosition(0);
                    Token token = (Token) Token.CREATOR.createFromParcel(obtain);
                    obtain.recycle();
                    return token;
                }
            }
        }
        return null;
    }
}
