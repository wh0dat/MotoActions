package com.motorola.actions.motoservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.motorola.actions.BootReceiver;
import com.motorola.actions.ServiceStartReason;
import com.motorola.actions.utils.MALogger;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \t2\u00020\u0001:\u0001\tB\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, mo14495d2 = {"Lcom/motorola/actions/motoservice/MotoService;", "Landroid/app/Service;", "()V", "mBinder", "Landroid/os/Binder;", "onBind", "Landroid/os/IBinder;", "intent", "Landroid/content/Intent;", "Companion", "MotoActions_release"}, mo14496k = 1, mo14497mv = {1, 1, 11})
/* compiled from: MotoService.kt */
public final class MotoService extends Service {
    public static final Companion Companion = new Companion(null);
    /* access modifiers changed from: private */
    @NotNull
    public static final MALogger LOGGER = new MALogger(MotoService.class);
    private final Binder mBinder = new Binder();

    @Metadata(mo14493bv = {1, 0, 2}, mo14494d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, mo14495d2 = {"Lcom/motorola/actions/motoservice/MotoService$Companion;", "", "()V", "LOGGER", "Lcom/motorola/actions/utils/MALogger;", "getLOGGER", "()Lcom/motorola/actions/utils/MALogger;", "MotoActions_release"}, mo14496k = 1, mo14497mv = {1, 1, 11})
    /* compiled from: MotoService.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @NotNull
        public final MALogger getLOGGER() {
            return MotoService.LOGGER;
        }
    }

    @Nullable
    public IBinder onBind(@NotNull Intent intent) {
        Intrinsics.checkParameterIsNotNull(intent, "intent");
        LOGGER.mo11957d("onBind");
        BootReceiver.startAllFeatures(this, ServiceStartReason.MOTO_SERVICE);
        return this.mBinder;
    }
}
