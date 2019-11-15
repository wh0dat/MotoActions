package com.motorola.actions.p013ui.tutorial.p014ad;

import android.os.Bundle;
import android.os.Handler;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.motorola.actions.C0504R;
import com.motorola.actions.FeatureKey;
import com.motorola.actions.attentivedisplay.AttentiveDisplayService;
import com.motorola.actions.attentivedisplay.MovementDetectManager;
import com.motorola.actions.attentivedisplay.MovementDetectManager.MovementChangeListener;
import com.motorola.actions.attentivedisplay.ObjectDetectManager;
import com.motorola.actions.attentivedisplay.ObjectDetectManager.ObjectDetectListener;
import com.motorola.actions.attentivedisplay.face.FaceDetector;
import com.motorola.actions.attentivedisplay.face.FaceDetector.FaceDetectionListener;
import com.motorola.actions.attentivedisplay.face.FaceEngineFactory;
import com.motorola.actions.checkin.CommonCheckinAttributes;
import com.motorola.actions.p013ui.ActionsBaseActivity;
import com.motorola.actions.p013ui.settings.AttentiveDisplaySettingsFragment;
import com.motorola.actions.p013ui.tutorial.VideoListener;
import com.motorola.actions.p013ui.tutorial.p014ad.LightManager.LightChangeListener;
import com.motorola.actions.settings.updater.AttentiveDisplaySettingsUpdater;
import com.motorola.actions.utils.Device;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.ui.tutorial.ad.TourDemoActivity */
public class TourDemoActivity extends ActionsBaseActivity implements FaceDetectionListener, MovementChangeListener, LightChangeListener, ObjectDetectListener {
    private static final MALogger LOGGER = new MALogger(TourDemoActivity.class);
    private static final long START_SENSORS_TIME = 2000;
    private static final long STOP_ANIMATION_DELAY = 600;
    private FaceDetector mFaceDetector;
    private Handler mHandler;
    private boolean mIsLightAcceptable;
    private boolean mIsObjectDetected;
    private boolean mIsPositionAcceptable = true;
    private boolean mIsRunning;
    private LightManager mLightManager;
    private MovementDetectManager mMovementDetectManager;
    private ObjectDetectManager mObjectDetectManager;
    private TextView mResultFaceText;
    private Runnable mRunnable = new TourDemoActivity$$Lambda$0(this);
    private TextureView mTextureView;
    private ImageView mTutorialImg;
    private TextView mTvDescription;
    private TextView mTvTitle;
    private VideoListener mVideoListener;

    /* access modifiers changed from: protected */
    public int getDoneBtnTextId() {
        return C0504R.string.done;
    }

    /* access modifiers changed from: protected */
    public int getLeftBtnTextId() {
        return C0504R.string.no_thanks;
    }

    /* access modifiers changed from: protected */
    public int getRightBtnTextId() {
        return C0504R.string.turn_it_on;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!AttentiveDisplayService.isFeatureSupported(this)) {
            finish();
            return;
        }
        this.mHandler = new Handler();
        this.mMovementDetectManager = new MovementDetectManager(this, this);
        this.mLightManager = new LightManager(this, this);
        this.mObjectDetectManager = new ObjectDetectManager(this, this);
        if (Device.isNewMoto()) {
            setTheme(C0504R.style.Theme_Tutorial_Display);
        }
        setupUI();
    }

    private void setupUI() {
        setContentView((int) C0504R.layout.ad_tour_demo_activity);
        Button button = (Button) findViewById(C0504R.C0506id.rightBtn);
        button.setText(getRightBtnTextId());
        button.setOnClickListener(new TourDemoActivity$$Lambda$1(this));
        TextView textView = (TextView) findViewById(C0504R.C0506id.leftBtn);
        textView.setText(getLeftBtnTextId());
        textView.setOnClickListener(new TourDemoActivity$$Lambda$2(this));
        Button button2 = (Button) findViewById(C0504R.C0506id.singleButton);
        button2.setText(getDoneBtnTextId());
        button2.setOnClickListener(new TourDemoActivity$$Lambda$3(this));
        View findViewById = findViewById(C0504R.C0506id.layout_cmd_two_buttons);
        View findViewById2 = findViewById(C0504R.C0506id.layout_cmd_single_button);
        if (AttentiveDisplaySettingsFragment.isStayOnEnabled()) {
            setVisibilitySafe(findViewById, 8);
            setVisibilitySafe(findViewById2, 0);
        } else {
            setVisibilitySafe(findViewById, 0);
            setVisibilitySafe(findViewById2, 8);
        }
        this.mTutorialImg = (ImageView) findViewById(C0504R.C0506id.tutorial_img);
        this.mResultFaceText = (TextView) findViewById(C0504R.C0506id.result_no_face_text);
        this.mTextureView = (TextureView) findViewById(C0504R.C0506id.video);
        this.mVideoListener = new VideoListener(this, this.mTextureView, C0504R.raw.ad_face_detected, false);
        this.mTvTitle = (TextView) findViewById(C0504R.C0506id.tv_ad_title);
        if (Device.isNewMoto()) {
            this.mTvTitle.setTextColor(getResources().getColor(C0504R.color.wave, null));
        }
        this.mTvDescription = (TextView) findViewById(C0504R.C0506id.tv_ad_description);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupUI$1$TourDemoActivity(View view) {
        rightButtonClicked();
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupUI$2$TourDemoActivity(View view) {
        leftButtonClicked();
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$setupUI$3$TourDemoActivity(View view) {
        doneButtonClicked();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        LOGGER.mo11957d("onResume()");
        super.onResume();
        this.mHandler.removeCallbacks(this.mRunnable);
        this.mHandler.postDelayed(this.mRunnable, START_SENSORS_TIME);
        this.mTutorialImg.setImageResource(C0504R.C0505drawable.ad_no_face_detected);
        this.mTutorialImg.setVisibility(0);
        this.mResultFaceText.setVisibility(0);
        this.mTextureView.setVisibility(8);
    }

    public void onMovementChange(boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onMovementChange() - isMoving: ");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        this.mIsPositionAcceptable = z;
        if (isFinishing()) {
            LOGGER.mo11963w("onMovementChange, listener called after activity has been destroyed.");
        } else {
            checkStartFaceDetection();
        }
    }

    public void onLightChange(boolean z) {
        this.mIsLightAcceptable = z;
        if (isFinishing()) {
            LOGGER.mo11963w("onLightChange, listener called after activity has been destroyed.");
        } else {
            checkStartFaceDetection();
        }
    }

    public void onObjectDetection(boolean z) {
        this.mIsObjectDetected = z;
        if (isFinishing()) {
            LOGGER.mo11963w("onObjectDetection, listener called after activity has been destroyed.");
        } else {
            checkStartFaceDetection();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: startSensors */
    public void lambda$new$0$TourDemoActivity() {
        if (Device.isVectorDevice()) {
            this.mObjectDetectManager.start();
        } else {
            this.mMovementDetectManager.start(true);
        }
        this.mLightManager.start();
    }

    private void checkStartFaceDetection() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("checkStartFaceDetection - mIsRunning: ");
        sb.append(this.mIsRunning);
        sb.append(", mIsPositionAcceptable: ");
        sb.append(this.mIsPositionAcceptable);
        sb.append(", mIsLightAcceptable: ");
        sb.append(this.mIsLightAcceptable);
        sb.append(", mIsObjectDetected: ");
        sb.append(this.mIsObjectDetected);
        mALogger.mo11957d(sb.toString());
        if (Device.isVectorDevice()) {
            if (!this.mIsRunning && this.mIsObjectDetected && this.mIsLightAcceptable) {
                startFaceDetection();
            } else if (!this.mIsObjectDetected) {
                stopFaceDetection();
                showObjectTutorial();
            } else if (!this.mIsLightAcceptable) {
                stopFaceDetection();
                showLuxTutorial();
            }
        } else if (!this.mIsRunning && this.mIsPositionAcceptable && this.mIsLightAcceptable) {
            startFaceDetection();
        } else if (!this.mIsLightAcceptable) {
            stopFaceDetection();
            showLuxTutorial();
        } else if (!this.mIsPositionAcceptable) {
            stopFaceDetection();
            showPositionTutorial();
        }
    }

    private void startFaceDetection() {
        showNoFaceTutorial();
        this.mFaceDetector = FaceEngineFactory.acquireInstance();
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("startFaceDetection - starting face detection - mFaceDetector");
        sb.append(this.mFaceDetector);
        mALogger.mo11957d(sb.toString());
        if (this.mFaceDetector != null) {
            this.mFaceDetector.startDetection(this);
            this.mIsRunning = true;
        }
    }

    private void stopFaceDetection() {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("stopFaceDetection - mIsRunning: ");
        sb.append(this.mIsRunning);
        mALogger.mo11957d(sb.toString());
        if (this.mIsRunning) {
            if (this.mFaceDetector != null) {
                this.mFaceDetector.stopDetection();
                FaceEngineFactory.releaseInstance();
                this.mFaceDetector = null;
            }
            this.mHandler.postDelayed(new TourDemoActivity$$Lambda$4(this), STOP_ANIMATION_DELAY);
        }
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void lambda$stopFaceDetection$4$TourDemoActivity() {
        this.mIsRunning = false;
    }

    private void showLuxTutorial() {
        LOGGER.mo11957d("showLuxTutorial");
        this.mVideoListener.stopVideo();
        this.mTvTitle.setText(C0504R.string.ad_tour_error_title);
        this.mTvDescription.setText(C0504R.string.ad_tour_error_description_lux);
        this.mResultFaceText.setText(C0504R.string.ad_tour_no_face_overlay_light_error);
        this.mResultFaceText.setTextColor(getResources().getColor(C0504R.color.white, null));
        this.mTutorialImg.setImageResource(C0504R.C0505drawable.ad_error_no_light);
        this.mTextureView.setVisibility(8);
        this.mResultFaceText.setVisibility(0);
        this.mTutorialImg.setVisibility(0);
    }

    private void showPositionTutorial() {
        LOGGER.mo11957d("showPositionTutorial");
        this.mVideoListener.stopVideo();
        this.mTvTitle.setText(C0504R.string.ad_tour_error_title);
        this.mTvDescription.setText(C0504R.string.ad_tour_error_description_position);
        this.mTutorialImg.setImageResource(C0504R.C0505drawable.ad_error_position);
        this.mTextureView.setVisibility(8);
        this.mResultFaceText.setVisibility(8);
        this.mTutorialImg.setVisibility(0);
    }

    private void showNoFaceTutorial() {
        LOGGER.mo11957d("showNoFaceTutorial");
        this.mVideoListener.stopVideo();
        this.mTutorialImg.setImageResource(C0504R.C0505drawable.ad_no_face_detected);
        this.mResultFaceText.setText(C0504R.string.ad_tour_no_face_overlay);
        this.mResultFaceText.setTextColor(getResources().getColor(C0504R.color.image_overlay_grey, null));
        this.mTvTitle.setText(C0504R.string.ad_tour_demo_title);
        this.mTvDescription.setText(C0504R.string.ad_tour_demo_instructions);
        this.mTextureView.setVisibility(8);
        this.mResultFaceText.setVisibility(0);
        this.mTutorialImg.setVisibility(0);
    }

    private void showObjectTutorial() {
        LOGGER.mo11957d("showObjectTutorial");
        this.mTutorialImg.setImageResource(C0504R.C0505drawable.ad_no_face_detected);
        this.mResultFaceText.setText(C0504R.string.ad_tour_no_face_overlay_object_error);
        this.mResultFaceText.setTextColor(getResources().getColor(C0504R.color.image_overlay_grey, null));
        this.mTvTitle.setText(C0504R.string.ad_tour_error_title);
        this.mTvDescription.setText(C0504R.string.ad_tour_error_description_distance);
        this.mTextureView.setVisibility(8);
        this.mResultFaceText.setVisibility(0);
        this.mTutorialImg.setVisibility(0);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        LOGGER.mo11957d("onPause()");
        super.onPause();
        if (this.mIsRunning) {
            this.mIsRunning = false;
            if (this.mFaceDetector != null) {
                this.mFaceDetector.stopDetection();
                FaceEngineFactory.releaseInstance();
                this.mFaceDetector = null;
            }
        }
        this.mMovementDetectManager.stop();
        this.mLightManager.stop();
        this.mObjectDetectManager.stop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        stopManagers();
    }

    public void onDetectionError() {
        if (this.mIsRunning) {
            Toast.makeText(this, getString(C0504R.string.ad_tour_error_camera), 1).show();
            finish();
        }
    }

    public void onFaceDetection(boolean z, byte[] bArr) {
        if (!this.mIsRunning) {
            return;
        }
        if (z) {
            stopFaceDetection();
            this.mTvTitle.setText(C0504R.string.ad_tour_demo_title_success);
            this.mTvDescription.setText(C0504R.string.ad_tour_success_description);
            this.mVideoListener.startVideo();
            this.mTutorialImg.setVisibility(8);
            this.mResultFaceText.setVisibility(8);
            this.mTextureView.setVisibility(0);
            stopManagers();
            return;
        }
        showNoFaceTutorial();
    }

    private void stopManagers() {
        this.mMovementDetectManager.stop();
        this.mLightManager.stop();
        this.mObjectDetectManager.stop();
    }

    private void leftButtonClicked() {
        AttentiveDisplaySettingsUpdater.getInstance().toggleStatus(false, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        setResult(-1);
        finish();
    }

    private void rightButtonClicked() {
        AttentiveDisplaySettingsUpdater.getInstance().toggleStatus(true, true, CommonCheckinAttributes.KEY_DAILY_ENABLE_SOURCE_TYPE_TUTORIAL);
        setResult(-1);
        getFDNSession().recordChange(FeatureKey.ATTENTIVE_DISPLAY);
        finish();
    }

    private void doneButtonClicked() {
        setResult(-1);
        finish();
    }

    private void setVisibilitySafe(View view, int i) {
        if (view != null) {
            view.setVisibility(i);
        }
    }
}
