package com.google.android.gms.common.images;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ImagesContract;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

@Class(creator = "WebImageCreator")
public final class WebImage extends AbstractSafeParcelable {
    public static final Creator<WebImage> CREATOR = new WebImageCreator();
    @VersionField(mo9711id = 1)
    private final int zzal;
    @Field(getter = "getWidth", mo9705id = 3)
    private final int zzps;
    @Field(getter = "getHeight", mo9705id = 4)
    private final int zzpt;
    @Field(getter = "getUrl", mo9705id = 2)
    private final Uri zzpu;

    @Constructor
    WebImage(@Param(mo9708id = 1) int i, @Param(mo9708id = 2) Uri uri, @Param(mo9708id = 3) int i2, @Param(mo9708id = 4) int i3) {
        this.zzal = i;
        this.zzpu = uri;
        this.zzps = i2;
        this.zzpt = i3;
    }

    public WebImage(Uri uri) throws IllegalArgumentException {
        this(uri, 0, 0);
    }

    public WebImage(Uri uri, int i, int i2) throws IllegalArgumentException {
        this(1, uri, i, i2);
        if (uri == null) {
            throw new IllegalArgumentException("url cannot be null");
        } else if (i < 0 || i2 < 0) {
            throw new IllegalArgumentException("width and height must not be negative");
        }
    }

    public WebImage(JSONObject jSONObject) throws IllegalArgumentException {
        this(zza(jSONObject), jSONObject.optInt("width", 0), jSONObject.optInt("height", 0));
    }

    private static Uri zza(JSONObject jSONObject) {
        if (jSONObject.has(ImagesContract.URL)) {
            try {
                return Uri.parse(jSONObject.getString(ImagesContract.URL));
            } catch (JSONException unused) {
            }
        }
        return null;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof WebImage)) {
            return false;
        }
        WebImage webImage = (WebImage) obj;
        return Objects.equal(this.zzpu, webImage.zzpu) && this.zzps == webImage.zzps && this.zzpt == webImage.zzpt;
    }

    public final int getHeight() {
        return this.zzpt;
    }

    public final Uri getUrl() {
        return this.zzpu;
    }

    public final int getWidth() {
        return this.zzps;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zzpu, Integer.valueOf(this.zzps), Integer.valueOf(this.zzpt));
    }

    public final JSONObject toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(ImagesContract.URL, this.zzpu.toString());
            jSONObject.put("width", this.zzps);
            jSONObject.put("height", this.zzpt);
        } catch (JSONException unused) {
        }
        return jSONObject;
    }

    public final String toString() {
        return String.format(Locale.US, "Image %dx%d %s", new Object[]{Integer.valueOf(this.zzps), Integer.valueOf(this.zzpt), this.zzpu.toString()});
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeParcelable(parcel, 2, getUrl(), i, false);
        SafeParcelWriter.writeInt(parcel, 3, getWidth());
        SafeParcelWriter.writeInt(parcel, 4, getHeight());
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
