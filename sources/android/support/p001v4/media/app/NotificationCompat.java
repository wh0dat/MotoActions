package android.support.p001v4.media.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.media.session.MediaSession;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.RestrictTo.Scope;
import android.support.mediacompat.C0139R;
import android.support.p001v4.app.BundleCompat;
import android.support.p001v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.p001v4.app.NotificationCompat.Action;
import android.support.p001v4.app.NotificationCompat.Builder;
import android.support.p001v4.app.NotificationCompat.Style;
import android.support.p001v4.media.session.MediaSessionCompat.Token;
import android.widget.RemoteViews;

/* renamed from: android.support.v4.media.app.NotificationCompat */
public class NotificationCompat {

    /* renamed from: android.support.v4.media.app.NotificationCompat$DecoratedMediaCustomViewStyle */
    public static class DecoratedMediaCustomViewStyle extends MediaStyle {
        @RestrictTo({Scope.LIBRARY_GROUP})
        public void apply(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (VERSION.SDK_INT >= 24) {
                notificationBuilderWithBuilderAccessor.getBuilder().setStyle(fillInMediaStyle(new android.app.Notification.DecoratedMediaCustomViewStyle()));
            } else {
                super.apply(notificationBuilderWithBuilderAccessor);
            }
        }

        @RestrictTo({Scope.LIBRARY_GROUP})
        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (VERSION.SDK_INT >= 24) {
                return null;
            }
            boolean z = false;
            boolean z2 = this.mBuilder.getContentView() != null;
            if (VERSION.SDK_INT >= 21) {
                if (z2 || this.mBuilder.getBigContentView() != null) {
                    z = true;
                }
                if (z) {
                    RemoteViews generateContentView = generateContentView();
                    if (z2) {
                        buildIntoRemoteViews(generateContentView, this.mBuilder.getContentView());
                    }
                    setBackgroundColor(generateContentView);
                    return generateContentView;
                }
            } else {
                RemoteViews generateContentView2 = generateContentView();
                if (z2) {
                    buildIntoRemoteViews(generateContentView2, this.mBuilder.getContentView());
                    return generateContentView2;
                }
            }
            return null;
        }

        /* access modifiers changed from: 0000 */
        public int getContentViewLayoutResource() {
            if (this.mBuilder.getContentView() != null) {
                return C0139R.layout.notification_template_media_custom;
            }
            return super.getContentViewLayoutResource();
        }

        @RestrictTo({Scope.LIBRARY_GROUP})
        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            RemoteViews remoteViews;
            if (VERSION.SDK_INT >= 24) {
                return null;
            }
            if (this.mBuilder.getBigContentView() != null) {
                remoteViews = this.mBuilder.getBigContentView();
            } else {
                remoteViews = this.mBuilder.getContentView();
            }
            if (remoteViews == null) {
                return null;
            }
            RemoteViews generateBigContentView = generateBigContentView();
            buildIntoRemoteViews(generateBigContentView, remoteViews);
            if (VERSION.SDK_INT >= 21) {
                setBackgroundColor(generateBigContentView);
            }
            return generateBigContentView;
        }

        /* access modifiers changed from: 0000 */
        public int getBigContentViewLayoutResource(int i) {
            return i <= 3 ? C0139R.layout.notification_template_big_media_narrow_custom : C0139R.layout.notification_template_big_media_custom;
        }

        @RestrictTo({Scope.LIBRARY_GROUP})
        public RemoteViews makeHeadsUpContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            RemoteViews remoteViews;
            if (VERSION.SDK_INT >= 24) {
                return null;
            }
            if (this.mBuilder.getHeadsUpContentView() != null) {
                remoteViews = this.mBuilder.getHeadsUpContentView();
            } else {
                remoteViews = this.mBuilder.getContentView();
            }
            if (remoteViews == null) {
                return null;
            }
            RemoteViews generateBigContentView = generateBigContentView();
            buildIntoRemoteViews(generateBigContentView, remoteViews);
            if (VERSION.SDK_INT >= 21) {
                setBackgroundColor(generateBigContentView);
            }
            return generateBigContentView;
        }

        private void setBackgroundColor(RemoteViews remoteViews) {
            int i;
            if (this.mBuilder.getColor() != 0) {
                i = this.mBuilder.getColor();
            } else {
                i = this.mBuilder.mContext.getResources().getColor(C0139R.color.notification_material_background_media_default_color);
            }
            remoteViews.setInt(C0139R.C0141id.status_bar_latest_event_content, "setBackgroundColor", i);
        }
    }

    /* renamed from: android.support.v4.media.app.NotificationCompat$MediaStyle */
    public static class MediaStyle extends Style {
        private static final int MAX_MEDIA_BUTTONS = 5;
        private static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;
        int[] mActionsToShowInCompact = null;
        PendingIntent mCancelButtonIntent;
        boolean mShowCancelButton;
        Token mToken;

        public static Token getMediaSession(Notification notification) {
            Bundle extras = android.support.p001v4.app.NotificationCompat.getExtras(notification);
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

        public MediaStyle() {
        }

        public MediaStyle(Builder builder) {
            setBuilder(builder);
        }

        public MediaStyle setShowActionsInCompactView(int... iArr) {
            this.mActionsToShowInCompact = iArr;
            return this;
        }

        public MediaStyle setMediaSession(Token token) {
            this.mToken = token;
            return this;
        }

        public MediaStyle setShowCancelButton(boolean z) {
            if (VERSION.SDK_INT < 21) {
                this.mShowCancelButton = z;
            }
            return this;
        }

        public MediaStyle setCancelButtonIntent(PendingIntent pendingIntent) {
            this.mCancelButtonIntent = pendingIntent;
            return this;
        }

        @RestrictTo({Scope.LIBRARY_GROUP})
        public void apply(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (VERSION.SDK_INT >= 21) {
                notificationBuilderWithBuilderAccessor.getBuilder().setStyle(fillInMediaStyle(new android.app.Notification.MediaStyle()));
            } else if (this.mShowCancelButton) {
                notificationBuilderWithBuilderAccessor.getBuilder().setOngoing(true);
            }
        }

        /* access modifiers changed from: 0000 */
        @RequiresApi(21)
        public android.app.Notification.MediaStyle fillInMediaStyle(android.app.Notification.MediaStyle mediaStyle) {
            if (this.mActionsToShowInCompact != null) {
                mediaStyle.setShowActionsInCompactView(this.mActionsToShowInCompact);
            }
            if (this.mToken != null) {
                mediaStyle.setMediaSession((MediaSession.Token) this.mToken.getToken());
            }
            return mediaStyle;
        }

        @RestrictTo({Scope.LIBRARY_GROUP})
        public RemoteViews makeContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (VERSION.SDK_INT >= 21) {
                return null;
            }
            return generateContentView();
        }

        /* access modifiers changed from: 0000 */
        public RemoteViews generateContentView() {
            int i;
            RemoteViews applyStandardTemplate = applyStandardTemplate(false, getContentViewLayoutResource(), true);
            int size = this.mBuilder.mActions.size();
            if (this.mActionsToShowInCompact == null) {
                i = 0;
            } else {
                i = Math.min(this.mActionsToShowInCompact.length, 3);
            }
            applyStandardTemplate.removeAllViews(C0139R.C0141id.media_actions);
            if (i > 0) {
                for (int i2 = 0; i2 < i; i2++) {
                    if (i2 >= size) {
                        throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", new Object[]{Integer.valueOf(i2), Integer.valueOf(size - 1)}));
                    }
                    applyStandardTemplate.addView(C0139R.C0141id.media_actions, generateMediaActionButton((Action) this.mBuilder.mActions.get(this.mActionsToShowInCompact[i2])));
                }
            }
            if (this.mShowCancelButton) {
                applyStandardTemplate.setViewVisibility(C0139R.C0141id.end_padder, 8);
                applyStandardTemplate.setViewVisibility(C0139R.C0141id.cancel_action, 0);
                applyStandardTemplate.setOnClickPendingIntent(C0139R.C0141id.cancel_action, this.mCancelButtonIntent);
                applyStandardTemplate.setInt(C0139R.C0141id.cancel_action, "setAlpha", this.mBuilder.mContext.getResources().getInteger(C0139R.integer.cancel_button_image_alpha));
            } else {
                applyStandardTemplate.setViewVisibility(C0139R.C0141id.end_padder, 0);
                applyStandardTemplate.setViewVisibility(C0139R.C0141id.cancel_action, 8);
            }
            return applyStandardTemplate;
        }

        private RemoteViews generateMediaActionButton(Action action) {
            boolean z = action.getActionIntent() == null;
            RemoteViews remoteViews = new RemoteViews(this.mBuilder.mContext.getPackageName(), C0139R.layout.notification_media_action);
            remoteViews.setImageViewResource(C0139R.C0141id.action0, action.getIcon());
            if (!z) {
                remoteViews.setOnClickPendingIntent(C0139R.C0141id.action0, action.getActionIntent());
            }
            if (VERSION.SDK_INT >= 15) {
                remoteViews.setContentDescription(C0139R.C0141id.action0, action.getTitle());
            }
            return remoteViews;
        }

        /* access modifiers changed from: 0000 */
        public int getContentViewLayoutResource() {
            return C0139R.layout.notification_template_media;
        }

        @RestrictTo({Scope.LIBRARY_GROUP})
        public RemoteViews makeBigContentView(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor) {
            if (VERSION.SDK_INT >= 21) {
                return null;
            }
            return generateBigContentView();
        }

        /* access modifiers changed from: 0000 */
        public RemoteViews generateBigContentView() {
            int min = Math.min(this.mBuilder.mActions.size(), 5);
            RemoteViews applyStandardTemplate = applyStandardTemplate(false, getBigContentViewLayoutResource(min), false);
            applyStandardTemplate.removeAllViews(C0139R.C0141id.media_actions);
            if (min > 0) {
                for (int i = 0; i < min; i++) {
                    applyStandardTemplate.addView(C0139R.C0141id.media_actions, generateMediaActionButton((Action) this.mBuilder.mActions.get(i)));
                }
            }
            if (this.mShowCancelButton) {
                applyStandardTemplate.setViewVisibility(C0139R.C0141id.cancel_action, 0);
                applyStandardTemplate.setInt(C0139R.C0141id.cancel_action, "setAlpha", this.mBuilder.mContext.getResources().getInteger(C0139R.integer.cancel_button_image_alpha));
                applyStandardTemplate.setOnClickPendingIntent(C0139R.C0141id.cancel_action, this.mCancelButtonIntent);
            } else {
                applyStandardTemplate.setViewVisibility(C0139R.C0141id.cancel_action, 8);
            }
            return applyStandardTemplate;
        }

        /* access modifiers changed from: 0000 */
        public int getBigContentViewLayoutResource(int i) {
            return i <= 3 ? C0139R.layout.notification_template_big_media_narrow : C0139R.layout.notification_template_big_media;
        }
    }

    private NotificationCompat() {
    }
}
