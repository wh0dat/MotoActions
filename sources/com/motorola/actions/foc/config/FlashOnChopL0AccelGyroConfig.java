package com.motorola.actions.foc.config;

import android.support.p001v4.internal.view.SupportMenu;
import com.motorola.actions.foc.config.FlashOnChopInterfaceConfig.FlashOnChipsetType;

public class FlashOnChopL0AccelGyroConfig implements FlashOnChopInterfaceConfig {
    public short firstAccelThresold;
    public short maxChopDuration;
    public int maxGyroRotation;
    public byte maxXyPercentage;
    public byte minMagnitudePercentage;
    public double secondAccelThresold;

    public short[] getConfigAsArray() {
        return new short[]{(short) ((this.maxGyroRotation & SupportMenu.CATEGORY_MASK) >> 16), (short) (this.maxGyroRotation & SupportMenu.USER_MASK), this.maxChopDuration, (short) ((int) (((double) (this.firstAccelThresold * 1024)) / 9.8d)), (short) ((int) ((this.secondAccelThresold * 1024.0d) / 9.8d)), (short) ((this.maxXyPercentage & 255) | ((this.minMagnitudePercentage & 255) << 8))};
    }

    public FlashOnChipsetType getFlashOnChipsetType() {
        return FlashOnChipsetType.FlashOnChopL0;
    }
}
