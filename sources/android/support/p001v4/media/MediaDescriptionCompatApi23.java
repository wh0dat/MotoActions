package android.support.p001v4.media;

import android.media.MediaDescription;
import android.net.Uri;
import android.support.annotation.RequiresApi;

@RequiresApi(23)
/* renamed from: android.support.v4.media.MediaDescriptionCompatApi23 */
class MediaDescriptionCompatApi23 extends MediaDescriptionCompatApi21 {

    /* renamed from: android.support.v4.media.MediaDescriptionCompatApi23$Builder */
    static class Builder extends Builder {
        Builder() {
        }

        public static void setMediaUri(Object obj, Uri uri) {
            ((android.media.MediaDescription.Builder) obj).setMediaUri(uri);
        }
    }

    MediaDescriptionCompatApi23() {
    }

    public static Uri getMediaUri(Object obj) {
        return ((MediaDescription) obj).getMediaUri();
    }
}
