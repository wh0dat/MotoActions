package com.motorola.actions.approach.p005ir.tuning;

import android.support.graphics.drawable.PathInterpolatorCompat;
import android.support.p004v7.widget.helper.ItemTouchHelper.Callback;
import com.google.android.gms.common.ConnectionResult;
import com.motorola.actions.attentivedisplay.ADConstants;
import com.motorola.actions.utils.Device;

/* renamed from: com.motorola.actions.approach.ir.tuning.Tuning6 */
public class Tuning6 {
    private static final int MAX_READING = 4096;
    private static final int PROX_APPROACH_MASK = 1;

    /* renamed from: com.motorola.actions.approach.ir.tuning.Tuning6$IRLEDTuningParameters */
    public static class IRLEDTuningParameters {
        int mAmbientOffsetHP;
        int mAmbientSelection;
        int mIdleEventMinimumAboveNoise;
        int mIdleHysteresisThreshold;
        int mPresenceEventMinimumAboveNoise;
        int mPresenceHysteresisThreshold;
        int mProxCoveringMinimumHP;
        int mProxCoveringMinimumLP;
        int mProxEventMinimumAboveNoise;
        int mProxFeatureSupport;
        int mProxHoveringMinimumAboveNoise;
        int mProxHysteresisThreshold;
        int mProxObjectCloseMinimumAboveNoise;
        int mSwipeCompleteLevel1MinimumAboveIdle;
        int mSwipeCompleteLevel1Threshold;
        int mSwipeCompleteLevel2MinimumAboveIdle;
        int mSwipeCompleteLevel2Threshold;
        int mSwipeCompleteLevel3MinimumAboveIdle;
        int mSwipeCompleteLevel3Threshold;
        int mSwipeEventMinimumAboveNoise;
        int mSwipeHysteresisThreshold;
        int mSwipeReadingHighThreshold;
    }

    /* renamed from: com.motorola.actions.approach.ir.tuning.Tuning6$IRTuningParameters */
    public static class IRTuningParameters {
        int mAmbientMaxReading;
        int mAmbientMinReading;
        IRLEDTuningParameters mBottomBoth;
        IRLEDTuningParameters mBottomLeft;
        IRLEDTuningParameters mBottomRight;
        int mIdleCount;
        int mIdleEventCount;
        int mMotionScaleShift;
        int mNoiseMinimum;
        int mNoisePadding;
        int mNoisePeriod;
        int mObjectValidDelay;
        int mPresenceEventCount;
        int mProxCoverCount;
        int mProxEventCount;
        int mProxHoverCount;
        int mSwipeCount;
        int mSwipeEventCount;
        int mSwipeFastLevel1Threshold;
        int mSwipeFastLevel2Threshold;
        int mSwipeHighLevel1Multiplier;
        int mSwipeHighLevel2Multiplier;
        int mSwipeHighThreshold;
        int mSwipeIgnoreCount;
        int mSwipeIsEqualLevel1Threshold;
        int mSwipeIsEqualLevel2Threshold;
        int mSwipeIsEqualLevel3Threshold;
        int mSwipeVerticalIgnoreCount;
        int mSwipeVerticalIgnoreDelta;
        int mSwipeVerticalPrefDelta;
        IRLEDTuningParameters mTopRight;
    }

    private static native boolean nativeSetIRTuning6(IRTuningParameters iRTuningParameters);

    public static IRTuningParameters getParams() {
        IRTuningParameters iRTuningParameters = new IRTuningParameters();
        iRTuningParameters.mTopRight = new IRLEDTuningParameters();
        iRTuningParameters.mTopRight.mAmbientSelection = 0;
        iRTuningParameters.mTopRight.mAmbientOffsetHP = 30;
        iRTuningParameters.mTopRight.mIdleEventMinimumAboveNoise = 65;
        iRTuningParameters.mTopRight.mIdleHysteresisThreshold = 50;
        iRTuningParameters.mTopRight.mPresenceEventMinimumAboveNoise = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
        iRTuningParameters.mTopRight.mPresenceHysteresisThreshold = 100;
        iRTuningParameters.mTopRight.mProxEventMinimumAboveNoise = 650;
        iRTuningParameters.mTopRight.mProxHysteresisThreshold = 100;
        iRTuningParameters.mTopRight.mProxObjectCloseMinimumAboveNoise = 3200;
        iRTuningParameters.mTopRight.mProxHoveringMinimumAboveNoise = 1200;
        iRTuningParameters.mTopRight.mProxCoveringMinimumHP = 3896;
        iRTuningParameters.mTopRight.mProxCoveringMinimumLP = 2600;
        iRTuningParameters.mTopRight.mProxFeatureSupport = 1;
        iRTuningParameters.mTopRight.mSwipeEventMinimumAboveNoise = 100;
        iRTuningParameters.mTopRight.mSwipeHysteresisThreshold = 50;
        iRTuningParameters.mTopRight.mSwipeCompleteLevel1MinimumAboveIdle = PathInterpolatorCompat.MAX_NUM_POINTS;
        iRTuningParameters.mTopRight.mSwipeCompleteLevel1Threshold = 400;
        iRTuningParameters.mTopRight.mSwipeCompleteLevel2MinimumAboveIdle = 1800;
        iRTuningParameters.mTopRight.mSwipeCompleteLevel2Threshold = 300;
        iRTuningParameters.mTopRight.mSwipeCompleteLevel3MinimumAboveIdle = 600;
        iRTuningParameters.mTopRight.mSwipeCompleteLevel3Threshold = Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
        iRTuningParameters.mTopRight.mSwipeReadingHighThreshold = PathInterpolatorCompat.MAX_NUM_POINTS;
        iRTuningParameters.mBottomLeft = new IRLEDTuningParameters();
        iRTuningParameters.mBottomLeft.mAmbientSelection = 0;
        iRTuningParameters.mBottomLeft.mAmbientOffsetHP = 30;
        iRTuningParameters.mBottomLeft.mIdleEventMinimumAboveNoise = 65;
        iRTuningParameters.mBottomLeft.mIdleHysteresisThreshold = 50;
        iRTuningParameters.mBottomLeft.mPresenceEventMinimumAboveNoise = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
        iRTuningParameters.mBottomLeft.mPresenceHysteresisThreshold = 100;
        iRTuningParameters.mBottomLeft.mProxEventMinimumAboveNoise = 550;
        iRTuningParameters.mBottomLeft.mProxHysteresisThreshold = 100;
        iRTuningParameters.mBottomLeft.mProxObjectCloseMinimumAboveNoise = PathInterpolatorCompat.MAX_NUM_POINTS;
        iRTuningParameters.mBottomLeft.mProxHoveringMinimumAboveNoise = 1200;
        iRTuningParameters.mBottomLeft.mProxCoveringMinimumHP = 3896;
        iRTuningParameters.mBottomLeft.mProxCoveringMinimumLP = 2600;
        iRTuningParameters.mBottomLeft.mProxFeatureSupport = 0;
        iRTuningParameters.mBottomLeft.mSwipeEventMinimumAboveNoise = 100;
        iRTuningParameters.mBottomLeft.mSwipeHysteresisThreshold = 50;
        iRTuningParameters.mBottomLeft.mSwipeCompleteLevel1MinimumAboveIdle = 1600;
        iRTuningParameters.mBottomLeft.mSwipeCompleteLevel1Threshold = 400;
        iRTuningParameters.mBottomLeft.mSwipeCompleteLevel2MinimumAboveIdle = 800;
        iRTuningParameters.mBottomLeft.mSwipeCompleteLevel2Threshold = 300;
        iRTuningParameters.mBottomLeft.mSwipeCompleteLevel3MinimumAboveIdle = 300;
        iRTuningParameters.mBottomLeft.mSwipeCompleteLevel3Threshold = 160;
        iRTuningParameters.mBottomLeft.mSwipeReadingHighThreshold = 900;
        iRTuningParameters.mBottomRight = new IRLEDTuningParameters();
        iRTuningParameters.mBottomRight.mAmbientSelection = 0;
        iRTuningParameters.mBottomRight.mAmbientOffsetHP = 30;
        iRTuningParameters.mBottomRight.mIdleEventMinimumAboveNoise = 65;
        iRTuningParameters.mBottomRight.mIdleHysteresisThreshold = 50;
        iRTuningParameters.mBottomRight.mPresenceEventMinimumAboveNoise = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
        iRTuningParameters.mBottomRight.mPresenceHysteresisThreshold = 100;
        iRTuningParameters.mBottomRight.mProxEventMinimumAboveNoise = 550;
        iRTuningParameters.mBottomRight.mProxHysteresisThreshold = 100;
        iRTuningParameters.mBottomRight.mProxObjectCloseMinimumAboveNoise = PathInterpolatorCompat.MAX_NUM_POINTS;
        iRTuningParameters.mBottomRight.mProxHoveringMinimumAboveNoise = 1200;
        iRTuningParameters.mBottomRight.mProxCoveringMinimumHP = 3896;
        iRTuningParameters.mBottomRight.mProxCoveringMinimumLP = 2600;
        iRTuningParameters.mBottomRight.mProxFeatureSupport = 0;
        iRTuningParameters.mBottomRight.mSwipeEventMinimumAboveNoise = 100;
        iRTuningParameters.mBottomRight.mSwipeHysteresisThreshold = 50;
        iRTuningParameters.mBottomRight.mSwipeCompleteLevel1MinimumAboveIdle = 1600;
        iRTuningParameters.mBottomRight.mSwipeCompleteLevel1Threshold = 400;
        iRTuningParameters.mBottomRight.mSwipeCompleteLevel2MinimumAboveIdle = 800;
        iRTuningParameters.mBottomRight.mSwipeCompleteLevel2Threshold = 300;
        iRTuningParameters.mBottomRight.mSwipeCompleteLevel3MinimumAboveIdle = 300;
        iRTuningParameters.mBottomRight.mSwipeCompleteLevel3Threshold = 160;
        iRTuningParameters.mBottomRight.mSwipeReadingHighThreshold = 900;
        iRTuningParameters.mBottomBoth = new IRLEDTuningParameters();
        iRTuningParameters.mBottomBoth.mAmbientSelection = 0;
        iRTuningParameters.mBottomBoth.mAmbientOffsetHP = 60;
        iRTuningParameters.mBottomBoth.mIdleEventMinimumAboveNoise = 65;
        iRTuningParameters.mBottomBoth.mIdleHysteresisThreshold = 50;
        iRTuningParameters.mBottomBoth.mPresenceEventMinimumAboveNoise = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
        iRTuningParameters.mBottomBoth.mPresenceHysteresisThreshold = 100;
        iRTuningParameters.mBottomBoth.mProxEventMinimumAboveNoise = 550;
        iRTuningParameters.mBottomBoth.mProxHysteresisThreshold = 100;
        iRTuningParameters.mBottomBoth.mProxObjectCloseMinimumAboveNoise = PathInterpolatorCompat.MAX_NUM_POINTS;
        iRTuningParameters.mBottomBoth.mProxHoveringMinimumAboveNoise = 1200;
        iRTuningParameters.mBottomBoth.mProxCoveringMinimumHP = 3896;
        iRTuningParameters.mBottomBoth.mProxCoveringMinimumLP = 2600;
        iRTuningParameters.mBottomBoth.mProxFeatureSupport = 1;
        iRTuningParameters.mBottomBoth.mSwipeEventMinimumAboveNoise = 100;
        iRTuningParameters.mBottomBoth.mSwipeHysteresisThreshold = 50;
        iRTuningParameters.mBottomBoth.mSwipeCompleteLevel1MinimumAboveIdle = PathInterpolatorCompat.MAX_NUM_POINTS;
        iRTuningParameters.mBottomBoth.mSwipeCompleteLevel1Threshold = 400;
        iRTuningParameters.mBottomBoth.mSwipeCompleteLevel2MinimumAboveIdle = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
        iRTuningParameters.mBottomBoth.mSwipeCompleteLevel2Threshold = 300;
        iRTuningParameters.mBottomBoth.mSwipeCompleteLevel3MinimumAboveIdle = 600;
        iRTuningParameters.mBottomBoth.mSwipeCompleteLevel3Threshold = Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
        iRTuningParameters.mBottomBoth.mSwipeReadingHighThreshold = PathInterpolatorCompat.MAX_NUM_POINTS;
        iRTuningParameters.mIdleCount = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
        iRTuningParameters.mIdleEventCount = 1;
        iRTuningParameters.mNoiseMinimum = 100;
        iRTuningParameters.mNoisePadding = 10;
        iRTuningParameters.mNoisePeriod = 50;
        iRTuningParameters.mObjectValidDelay = 5;
        iRTuningParameters.mProxCoverCount = 110;
        iRTuningParameters.mProxEventCount = 3;
        iRTuningParameters.mProxHoverCount = 50;
        iRTuningParameters.mPresenceEventCount = 3;
        iRTuningParameters.mSwipeCount = 8;
        iRTuningParameters.mSwipeEventCount = 2;
        iRTuningParameters.mSwipeHighThreshold = 50;
        iRTuningParameters.mSwipeFastLevel1Threshold = 12;
        iRTuningParameters.mSwipeFastLevel2Threshold = 25;
        iRTuningParameters.mSwipeIsEqualLevel1Threshold = 0;
        iRTuningParameters.mSwipeIsEqualLevel2Threshold = 2;
        iRTuningParameters.mSwipeIsEqualLevel3Threshold = 4;
        iRTuningParameters.mMotionScaleShift = 16;
        iRTuningParameters.mSwipeHighLevel1Multiplier = 2;
        iRTuningParameters.mSwipeHighLevel2Multiplier = 3;
        iRTuningParameters.mAmbientMinReading = 255;
        iRTuningParameters.mAmbientMaxReading = ADConstants.PRE_DIM_TIME_MILLIS;
        iRTuningParameters.mSwipeIgnoreCount = 18;
        iRTuningParameters.mSwipeVerticalPrefDelta = 1100;
        iRTuningParameters.mSwipeVerticalIgnoreDelta = 1800;
        iRTuningParameters.mSwipeVerticalIgnoreCount = 15;
        if (Device.isWhiteDevice()) {
            applyWhiteConfig(iRTuningParameters);
        }
        return iRTuningParameters;
    }

    public static boolean setIRTuning(IRTuningParameters iRTuningParameters) {
        return nativeSetIRTuning6(iRTuningParameters);
    }

    private static void applyWhiteConfig(IRTuningParameters iRTuningParameters) {
        iRTuningParameters.mTopRight.mAmbientOffsetHP = 40;
        iRTuningParameters.mTopRight.mProxObjectCloseMinimumAboveNoise = 3400;
        iRTuningParameters.mBottomLeft.mAmbientOffsetHP = 50;
        iRTuningParameters.mBottomLeft.mSwipeReadingHighThreshold = 1000;
        iRTuningParameters.mBottomRight.mAmbientOffsetHP = 55;
        iRTuningParameters.mBottomRight.mSwipeReadingHighThreshold = 1000;
        iRTuningParameters.mBottomBoth.mAmbientOffsetHP = 100;
        iRTuningParameters.mBottomBoth.mProxObjectCloseMinimumAboveNoise = 3400;
        iRTuningParameters.mBottomBoth.mSwipeReadingHighThreshold = 3200;
    }
}
