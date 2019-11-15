package com.motorola.actions.foc.config;

public interface FlashOnChopInterfaceConfig {

    public enum FlashOnChipsetType {
        FlashOnChopL0,
        FlashOnChopL4
    }

    short[] getConfigAsArray();

    FlashOnChipsetType getFlashOnChipsetType();
}
