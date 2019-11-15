package com.motorola.actions.foc.gesture;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.util.Size;
import android.view.Surface;
import com.motorola.actions.C0504R;
import com.motorola.actions.foc.gesture.util.FlashOnChopUtils;
import com.motorola.actions.foc.gesture.view.FlashOnChopPermissionsActivity;
import com.motorola.actions.notificationchannel.ActionsNotificationChannel;
import com.motorola.actions.utils.MALogger;
import com.motorola.actions.utils.NotificationId;
import com.motorola.actions.utils.NotificationUtils;
import java.util.ArrayList;

public class FlashlightController {
    /* access modifiers changed from: private */
    public static final MALogger LOGGER = new MALogger(FlashlightController.class);
    private static final String NOTIFICATION_TAG = "FlashlightController";
    private static boolean sFlashlightControllerHasCamera;
    /* access modifiers changed from: private */
    public CameraDevice mCameraDevice;
    private final String mCameraId;
    private final StateCallback mCameraListener = new StateCallback() {
        public void onOpened(CameraDevice cameraDevice) {
            MALogger access$300 = FlashlightController.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("StateCallback: onOpened - camera.getId() = ");
            sb.append(cameraDevice.getId());
            access$300.mo11957d(sb.toString());
            FlashlightController.this.mCameraDevice = cameraDevice;
            FlashlightController.setFlashlightControllerHasCamera(true);
            FlashlightController.this.postUpdateFlashlight();
        }

        public void onDisconnected(CameraDevice cameraDevice) {
            MALogger access$300 = FlashlightController.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("onDisconnected: camera.getId() = ");
            sb.append(cameraDevice.getId());
            access$300.mo11957d(sb.toString());
            if (FlashlightController.this.mCameraDevice == cameraDevice) {
                FlashlightController.LOGGER.mo11957d("onDisconnected: Disconnect camera instanced by FlashlightController");
                FlashlightController.this.teardown();
                FlashOnChopGestureManager.setFlashlightOn(false);
                FlashOnChopUtils.sendToForeground(false);
                FlashlightController.LOGGER.mo11957d("onDisconnected: Send To Background");
            }
        }

        public void onError(CameraDevice cameraDevice, int i) {
            MALogger access$300 = FlashlightController.LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("Camera error: camera=");
            sb.append(cameraDevice);
            sb.append(" error=");
            sb.append(i);
            access$300.mo11959e(sb.toString());
            if (cameraDevice == FlashlightController.this.mCameraDevice || FlashlightController.this.mCameraDevice == null) {
                FlashlightController.this.handleError(true);
                FlashlightController.setFlashlightControllerHasCamera(false);
            }
        }
    };
    private final CameraManager mCameraManager;
    private final Context mContext;
    private boolean mFlashlightEnabled;
    private CaptureRequest mFlashlightRequest;
    private final Handler mHandler;
    /* access modifiers changed from: private */
    public CameraCaptureSession mSession;
    private final CameraCaptureSession.StateCallback mSessionListener = new CameraCaptureSession.StateCallback() {
        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
            if (cameraCaptureSession.getDevice() == FlashlightController.this.mCameraDevice) {
                FlashlightController.this.mSession = cameraCaptureSession;
            } else {
                cameraCaptureSession.close();
            }
            FlashlightController.this.postUpdateFlashlight();
        }

        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {
            FlashlightController.LOGGER.mo11959e("Configure failed.");
            if (FlashlightController.this.mSession == null || FlashlightController.this.mSession == cameraCaptureSession) {
                FlashlightController.this.handleError(true);
            }
        }
    };
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;

    public FlashlightController(CameraManager cameraManager, Handler handler, String str, Context context) {
        this.mCameraManager = cameraManager;
        this.mHandler = handler;
        this.mCameraId = str;
        this.mContext = context;
    }

    public synchronized void setFlashlight(boolean z) {
        this.mFlashlightEnabled = z;
        postUpdateFlashlight();
    }

    public static synchronized boolean getFlashlightControllerHasCamera() {
        boolean z;
        synchronized (FlashlightController.class) {
            z = sFlashlightControllerHasCamera;
        }
        return z;
    }

    public static synchronized void setFlashlightControllerHasCamera(boolean z) {
        synchronized (FlashlightController.class) {
            sFlashlightControllerHasCamera = z;
        }
    }

    private void startDevice() throws CameraAccessException {
        if (this.mContext.checkSelfPermission("android.permission.CAMERA") == 0) {
            NotificationUtils.dismissNotification(NotificationId.ACTIONS_FLASHLIGHTCONTROLLER_PERMISSION.ordinal());
            try {
                this.mCameraManager.openCamera(this.mCameraId, this.mCameraListener, this.mHandler);
                setFlashlightControllerHasCamera(true);
            } catch (SecurityException unused) {
                LOGGER.mo11959e("Error to open the camera");
                setFlashlightControllerHasCamera(false);
            }
        } else {
            LOGGER.mo11957d("No permission to access camera.");
            if (this.mCameraDevice != null) {
                LOGGER.mo11957d("I have a camera device");
            }
            if (((KeyguardManager) this.mContext.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
                LOGGER.mo11957d("startDevice: Screen is locked");
                if (!FlashOnChopPermissionsActivity.getHasDialogInstance()) {
                    launchFlashOnChopPermissionNotification();
                    return;
                }
                return;
            }
            LOGGER.mo11957d("Screen is not locked");
            LOGGER.mo11957d("onResume: PERMISSION_NOTIFICATION_ID notification: false");
            NotificationUtils.dismissNotification(NotificationId.ACTIONS_FLASHLIGHTCONTROLLER_PERMISSION.ordinal());
            if (!FlashOnChopPermissionsActivity.getHasDialogInstance()) {
                Intent intent = new Intent(this.mContext, FlashOnChopPermissionsActivity.class);
                intent.addFlags(813727744);
                try {
                    PendingIntent.getActivity(this.mContext, 0, intent, 0).send();
                } catch (CanceledException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void launchFlashOnChopPermissionNotification() {
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService("notification");
        FlashOnChopUtils.sendToForeground(false);
        LOGGER.mo11957d("launchFlashOnChopPermissionNotification: Send To Background");
        teardown();
        FlashOnChopGestureManager.setFlashlightOn(false);
        LOGGER.mo11957d("launchFlashOnChopPermissionNotification: Teardown");
        Notification build = new Builder(this.mContext, ActionsNotificationChannel.GENERAL.name()).setContentTitle(this.mContext.getString(C0504R.string.notification_perm_needed)).setContentText(this.mContext.getString(C0504R.string.notification_perm_to_allow)).setSmallIcon(C0504R.C0505drawable.ic_stat_flash).setContentIntent(PendingIntent.getActivity(this.mContext, 4, new Intent(this.mContext, FlashOnChopPermissionsActivity.class), 134217728)).setLocalOnly(true).setAutoCancel(true).setColor(this.mContext.getColor(C0504R.color.notification_accent)).build();
        LOGGER.mo11957d("launchFlashOnChopPermissionNotification: PERMISSION_NOTIFICATION_ID notification: true");
        notificationManager.notify(NOTIFICATION_TAG, NotificationId.ACTIONS_FLASHLIGHTCONTROLLER_PERMISSION.ordinal(), build);
    }

    private void startSession() throws CameraAccessException {
        this.mSurfaceTexture = new SurfaceTexture(0);
        Size smallestSize = getSmallestSize(this.mCameraDevice.getId());
        this.mSurfaceTexture.setDefaultBufferSize(smallestSize.getWidth(), smallestSize.getHeight());
        this.mSurface = new Surface(this.mSurfaceTexture);
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(this.mSurface);
        this.mCameraDevice.createCaptureSession(arrayList, this.mSessionListener, this.mHandler);
    }

    private Size getSmallestSize(String str) throws CameraAccessException {
        Size[] outputSizes = ((StreamConfigurationMap) this.mCameraManager.getCameraCharacteristics(str).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)).getOutputSizes(SurfaceTexture.class);
        if (outputSizes == null || outputSizes.length == 0) {
            MALogger mALogger = LOGGER;
            StringBuilder sb = new StringBuilder();
            sb.append("getSmallestSize : CameraId = ");
            sb.append(str);
            sb.append("throws an IllegalStateException");
            mALogger.mo11957d(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Camera ");
            sb2.append(str);
            sb2.append("doesn't support any outputSize.");
            throw new IllegalStateException(sb2.toString());
        }
        Size size = outputSizes[0];
        for (Size size2 : outputSizes) {
            if (size.getWidth() >= size2.getWidth() && size.getHeight() >= size2.getHeight()) {
                size = size2;
            }
        }
        return size;
    }

    /* access modifiers changed from: private */
    public void postUpdateFlashlight() {
        this.mHandler.post(new FlashlightController$$Lambda$0(this));
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$postUpdateFlashlight$0$FlashlightController() {
        updateFlashlight(false);
    }

    private void updateFlashlight(boolean z) {
        boolean z2;
        synchronized (this) {
            z2 = this.mFlashlightEnabled && !z;
        }
        if (z2) {
            try {
                if (this.mCameraDevice == null) {
                    startDevice();
                    return;
                } else if (this.mSession == null) {
                    startSession();
                    return;
                } else if (this.mFlashlightRequest == null) {
                    CaptureRequest.Builder createCaptureRequest = this.mCameraDevice.createCaptureRequest(1);
                    createCaptureRequest.set(CaptureRequest.FLASH_MODE, Integer.valueOf(2));
                    createCaptureRequest.addTarget(this.mSurface);
                    CaptureRequest build = createCaptureRequest.build();
                    this.mSession.capture(build, null, this.mHandler);
                    this.mFlashlightRequest = build;
                }
            } catch (CameraAccessException | IllegalArgumentException | IllegalStateException | UnsupportedOperationException e) {
                LOGGER.mo11960e("Error in updateFlashlight", e);
                handleError(z2);
            }
        } else {
            if (this.mCameraDevice == null) {
                startDevice();
            }
            if (this.mCameraDevice != null) {
                this.mCameraDevice.close();
                teardown();
            }
        }
        FlashOnChopUtils.sendMsgTorchModeTutorial(z2, true);
    }

    /* access modifiers changed from: private */
    public void teardown() {
        this.mCameraDevice = null;
        this.mSession = null;
        this.mFlashlightRequest = null;
        if (this.mSurface != null) {
            this.mSurface.release();
            this.mSurfaceTexture.release();
        }
        this.mSurface = null;
        this.mSurfaceTexture = null;
        synchronized (this) {
            this.mFlashlightEnabled = false;
        }
        setFlashlightControllerHasCamera(false);
    }

    /* access modifiers changed from: private */
    public void handleError(boolean z) {
        synchronized (this) {
            this.mFlashlightEnabled = false;
        }
        LOGGER.mo11959e("Error accessing flashlight");
        if (z) {
            updateFlashlight(true);
        }
    }
}
