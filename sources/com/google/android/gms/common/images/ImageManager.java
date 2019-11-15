package com.google.android.gms.common.images;

import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.p001v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.images.ImageRequest.ImageViewImageRequest;
import com.google.android.gms.common.images.ImageRequest.ListenerImageRequest;
import com.google.android.gms.common.images.internal.PostProcessedResourceCache;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.common.internal.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ImageManager {
    public static final int PRIORITY_HIGH = 3;
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_MEDIUM = 2;
    /* access modifiers changed from: private */
    public static final Object zzov = new Object();
    /* access modifiers changed from: private */
    public static HashSet<Uri> zzow = new HashSet<>();
    private static ImageManager zzox;
    private static ImageManager zzoy;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public final ExecutorService zzoz = Executors.newFixedThreadPool(4);
    /* access modifiers changed from: private */
    public final zza zzpa;
    /* access modifiers changed from: private */
    public final PostProcessedResourceCache zzpb;
    /* access modifiers changed from: private */
    public final Map<ImageRequest, ImageReceiver> zzpc;
    /* access modifiers changed from: private */
    public final Map<Uri, ImageReceiver> zzpd;
    /* access modifiers changed from: private */
    public final Map<Uri, Long> zzpe;

    @KeepName
    private final class ImageReceiver extends ResultReceiver {
        private final Uri mUri;
        /* access modifiers changed from: private */
        public final ArrayList<ImageRequest> zzpf = new ArrayList<>();

        ImageReceiver(Uri uri) {
            super(new Handler(Looper.getMainLooper()));
            this.mUri = uri;
        }

        public final void onReceiveResult(int i, Bundle bundle) {
            ImageManager.this.zzoz.execute(new zzb(this.mUri, (ParcelFileDescriptor) bundle.getParcelable("com.google.android.gms.extra.fileDescriptor")));
        }

        public final void zza(ImageRequest imageRequest) {
            Asserts.checkMainThread("ImageReceiver.addImageRequest() must be called in the main thread");
            this.zzpf.add(imageRequest);
        }

        public final void zzb(ImageRequest imageRequest) {
            Asserts.checkMainThread("ImageReceiver.removeImageRequest() must be called in the main thread");
            this.zzpf.remove(imageRequest);
        }

        public final void zzco() {
            Intent intent = new Intent(Constants.ACTION_LOAD_IMAGE);
            intent.putExtra(Constants.EXTRA_URI, this.mUri);
            intent.putExtra(Constants.EXTRA_RESULT_RECEIVER, this);
            intent.putExtra(Constants.EXTRA_PRIORITY, 3);
            ImageManager.this.mContext.sendBroadcast(intent);
        }
    }

    public interface OnImageLoadedListener {
        void onImageLoaded(Uri uri, Drawable drawable, boolean z);
    }

    private static final class zza extends LruCache<zza, Bitmap> {
        public zza(Context context) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            super((int) (((float) (((context.getApplicationInfo().flags & 1048576) != 0 ? activityManager.getLargeMemoryClass() : activityManager.getMemoryClass()) * 1048576)) * 0.33f));
        }

        /* access modifiers changed from: protected */
        public final /* synthetic */ void entryRemoved(boolean z, Object obj, Object obj2, Object obj3) {
            super.entryRemoved(z, (zza) obj, (Bitmap) obj2, (Bitmap) obj3);
        }

        /* access modifiers changed from: protected */
        public final /* synthetic */ int sizeOf(Object obj, Object obj2) {
            Bitmap bitmap = (Bitmap) obj2;
            return bitmap.getHeight() * bitmap.getRowBytes();
        }
    }

    private final class zzb implements Runnable {
        private final Uri mUri;
        private final ParcelFileDescriptor zzph;

        public zzb(Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
            this.mUri = uri;
            this.zzph = parcelFileDescriptor;
        }

        public final void run() {
            Asserts.checkNotMainThread("LoadBitmapFromDiskRunnable can't be executed in the main thread");
            boolean z = false;
            Bitmap bitmap = null;
            if (this.zzph != null) {
                try {
                    bitmap = BitmapFactory.decodeFileDescriptor(this.zzph.getFileDescriptor());
                } catch (OutOfMemoryError e) {
                    String valueOf = String.valueOf(this.mUri);
                    StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 34);
                    sb.append("OOM while loading bitmap for uri: ");
                    sb.append(valueOf);
                    Log.e("ImageManager", sb.toString(), e);
                    z = true;
                }
                try {
                    this.zzph.close();
                } catch (IOException e2) {
                    Log.e("ImageManager", "closed failed", e2);
                }
            }
            boolean z2 = z;
            Bitmap bitmap2 = bitmap;
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Handler zzg = ImageManager.this.mHandler;
            zze zze = new zze(this.mUri, bitmap2, z2, countDownLatch);
            zzg.post(zze);
            try {
                countDownLatch.await();
            } catch (InterruptedException unused) {
                String valueOf2 = String.valueOf(this.mUri);
                StringBuilder sb2 = new StringBuilder(String.valueOf(valueOf2).length() + 32);
                sb2.append("Latch interrupted while posting ");
                sb2.append(valueOf2);
                Log.w("ImageManager", sb2.toString());
            }
        }
    }

    private final class zzc implements Runnable {
        private final ImageRequest zzpi;

        public zzc(ImageRequest imageRequest) {
            this.zzpi = imageRequest;
        }

        public final void run() {
            Asserts.checkMainThread("LoadImageRunnable must be executed on the main thread");
            ImageReceiver imageReceiver = (ImageReceiver) ImageManager.this.zzpc.get(this.zzpi);
            if (imageReceiver != null) {
                ImageManager.this.zzpc.remove(this.zzpi);
                imageReceiver.zzb(this.zzpi);
            }
            zza zza = this.zzpi.zzpk;
            if (zza.uri == null) {
                this.zzpi.zza(ImageManager.this.mContext, ImageManager.this.zzpb, true);
                return;
            }
            Bitmap zza2 = ImageManager.this.zza(zza);
            if (zza2 != null) {
                this.zzpi.zza(ImageManager.this.mContext, zza2, true);
                return;
            }
            Long l = (Long) ImageManager.this.zzpe.get(zza.uri);
            if (l != null) {
                if (SystemClock.elapsedRealtime() - l.longValue() < 3600000) {
                    this.zzpi.zza(ImageManager.this.mContext, ImageManager.this.zzpb, true);
                    return;
                }
                ImageManager.this.zzpe.remove(zza.uri);
            }
            this.zzpi.zza(ImageManager.this.mContext, ImageManager.this.zzpb);
            ImageReceiver imageReceiver2 = (ImageReceiver) ImageManager.this.zzpd.get(zza.uri);
            if (imageReceiver2 == null) {
                imageReceiver2 = new ImageReceiver(zza.uri);
                ImageManager.this.zzpd.put(zza.uri, imageReceiver2);
            }
            imageReceiver2.zza(this.zzpi);
            if (!(this.zzpi instanceof ListenerImageRequest)) {
                ImageManager.this.zzpc.put(this.zzpi, imageReceiver2);
            }
            synchronized (ImageManager.zzov) {
                if (!ImageManager.zzow.contains(zza.uri)) {
                    ImageManager.zzow.add(zza.uri);
                    imageReceiver2.zzco();
                }
            }
        }
    }

    private static final class zzd implements ComponentCallbacks2 {
        private final zza zzpa;

        public zzd(zza zza) {
            this.zzpa = zza;
        }

        public final void onConfigurationChanged(Configuration configuration) {
        }

        public final void onLowMemory() {
            this.zzpa.evictAll();
        }

        public final void onTrimMemory(int i) {
            if (i >= 60) {
                this.zzpa.evictAll();
                return;
            }
            if (i >= 20) {
                this.zzpa.trimToSize(this.zzpa.size() / 2);
            }
        }
    }

    private final class zze implements Runnable {
        private final Bitmap mBitmap;
        private final Uri mUri;
        private final CountDownLatch zzfd;
        private boolean zzpj;

        public zze(Uri uri, Bitmap bitmap, boolean z, CountDownLatch countDownLatch) {
            this.mUri = uri;
            this.mBitmap = bitmap;
            this.zzpj = z;
            this.zzfd = countDownLatch;
        }

        public final void run() {
            Asserts.checkMainThread("OnBitmapLoadedRunnable must be executed in the main thread");
            boolean z = this.mBitmap != null;
            if (ImageManager.this.zzpa != null) {
                if (this.zzpj) {
                    ImageManager.this.zzpa.evictAll();
                    System.gc();
                    this.zzpj = false;
                    ImageManager.this.mHandler.post(this);
                    return;
                } else if (z) {
                    ImageManager.this.zzpa.put(new zza(this.mUri), this.mBitmap);
                }
            }
            ImageReceiver imageReceiver = (ImageReceiver) ImageManager.this.zzpd.remove(this.mUri);
            if (imageReceiver != null) {
                ArrayList zza = imageReceiver.zzpf;
                int size = zza.size();
                for (int i = 0; i < size; i++) {
                    ImageRequest imageRequest = (ImageRequest) zza.get(i);
                    if (z) {
                        imageRequest.zza(ImageManager.this.mContext, this.mBitmap, false);
                    } else {
                        ImageManager.this.zzpe.put(this.mUri, Long.valueOf(SystemClock.elapsedRealtime()));
                        imageRequest.zza(ImageManager.this.mContext, ImageManager.this.zzpb, false);
                    }
                    if (!(imageRequest instanceof ListenerImageRequest)) {
                        ImageManager.this.zzpc.remove(imageRequest);
                    }
                }
            }
            this.zzfd.countDown();
            synchronized (ImageManager.zzov) {
                ImageManager.zzow.remove(this.mUri);
            }
        }
    }

    private ImageManager(Context context, boolean z) {
        this.mContext = context.getApplicationContext();
        if (z) {
            this.zzpa = new zza(this.mContext);
            this.mContext.registerComponentCallbacks(new zzd(this.zzpa));
        } else {
            this.zzpa = null;
        }
        this.zzpb = new PostProcessedResourceCache();
        this.zzpc = new HashMap();
        this.zzpd = new HashMap();
        this.zzpe = new HashMap();
    }

    public static ImageManager create(Context context) {
        return create(context, false);
    }

    public static ImageManager create(Context context, boolean z) {
        if (z) {
            if (zzoy == null) {
                zzoy = new ImageManager(context, true);
            }
            return zzoy;
        }
        if (zzox == null) {
            zzox = new ImageManager(context, false);
        }
        return zzox;
    }

    /* access modifiers changed from: private */
    public final Bitmap zza(zza zza2) {
        if (this.zzpa == null) {
            return null;
        }
        return (Bitmap) this.zzpa.get(zza2);
    }

    public final void loadImage(ImageView imageView, int i) {
        loadImage(new ImageViewImageRequest(imageView, i));
    }

    public final void loadImage(ImageView imageView, Uri uri) {
        loadImage(new ImageViewImageRequest(imageView, uri));
    }

    public final void loadImage(ImageView imageView, Uri uri, int i) {
        ImageViewImageRequest imageViewImageRequest = new ImageViewImageRequest(imageView, uri);
        imageViewImageRequest.setNoDataPlaceholder(i);
        loadImage(imageViewImageRequest);
    }

    public final void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri) {
        loadImage(new ListenerImageRequest(onImageLoadedListener, uri));
    }

    public final void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri, int i) {
        ListenerImageRequest listenerImageRequest = new ListenerImageRequest(onImageLoadedListener, uri);
        listenerImageRequest.setNoDataPlaceholder(i);
        loadImage(listenerImageRequest);
    }

    public final void loadImage(ImageRequest imageRequest) {
        Asserts.checkMainThread("ImageManager.loadImage() must be called in the main thread");
        new zzc(imageRequest).run();
    }
}
