package com.motorola.actions.foc.gesture.state;

import com.motorola.actions.FeatureKey;
import com.motorola.actions.modaccess.ModAccessManager;
import com.motorola.actions.modaccess.ModAccessManager.ModStateListener;
import com.motorola.actions.utils.MALogger;

public class ModAccessState implements IStateSource, ModStateListener {
    private static final MALogger LOGGER = new MALogger(ModAccessState.class);
    private boolean mIsCoveringCamera;

    public ModAccessState() {
        start();
    }

    public void start() {
        ModAccessManager.getInstance().connect(FeatureKey.FLASH_ON_CHOP, this);
    }

    public boolean isStateAcceptableToTurnOn() {
        return !this.mIsCoveringCamera;
    }

    public boolean isStateAcceptableToTurnOff() {
        return !this.mIsCoveringCamera;
    }

    public void stop() {
        ModAccessManager.getInstance().disconnect(FeatureKey.FLASH_ON_CHOP);
    }

    public void onModStateChanged(int i, boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Mod connected, product family = ");
        sb.append(i);
        mALogger.mo11957d(sb.toString());
        if (i == 8192 || i == 12304 || i == 24576) {
            MALogger mALogger2 = LOGGER;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Mod is covering camera: ");
            sb2.append(z);
            mALogger2.mo11957d(sb2.toString());
            this.mIsCoveringCamera = z;
        }
    }
}
