package com.motorola.actions.foc.gesture.state;

import com.motorola.actions.utils.SettingsWrapper;

public class ProvisionedState implements IStateSource {
    public void start() {
    }

    public void stop() {
    }

    public ProvisionedState() {
        start();
    }

    public boolean isStateAcceptableToTurnOn() {
        return isProvisioned();
    }

    public boolean isStateAcceptableToTurnOff() {
        return isProvisioned();
    }

    private boolean isProvisioned() {
        return SettingsWrapper.getGlobalInt("device_provisioned") != 0;
    }
}
