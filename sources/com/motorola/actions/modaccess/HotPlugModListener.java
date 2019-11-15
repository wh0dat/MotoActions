package com.motorola.actions.modaccess;

import com.motorola.actions.modaccess.modanalyzer.ModAnalyzer;
import com.motorola.actions.utils.MALogger;
import com.motorola.mod.ModConnection;
import com.motorola.mod.ModDevice;
import com.motorola.mod.ModDevice.Interface;
import com.motorola.mod.ModInterfaceDelegation;
import com.motorola.mod.ModListener;

class HotPlugModListener implements ModListener {
    private static final MALogger LOGGER = new MALogger(HotPlugModListener.class);
    private ModAnalyzer mModAnalyzer;

    public void onCapabilityChanged(ModDevice modDevice) {
    }

    public void onConnectionStatusChanged(ModDevice modDevice, ModConnection modConnection) {
    }

    public void onEnumerationDone(ModDevice modDevice) {
    }

    public void onEnumerationDone(ModDevice modDevice, Interface interfaceR, boolean z) {
    }

    public void onInterfaceUpDown(ModDevice modDevice, Interface interfaceR, boolean z) {
    }

    public void onLinuxDeviceChanged(ModDevice modDevice, ModInterfaceDelegation modInterfaceDelegation, boolean z) {
    }

    HotPlugModListener(ModAnalyzer modAnalyzer) {
        this.mModAnalyzer = modAnalyzer;
    }

    public void onDeviceHotplug(ModDevice modDevice, boolean z) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("onDeviceHotplug: ");
        sb.append(modDevice.toString());
        sb.append(" plugged=");
        sb.append(z);
        mALogger.mo11957d(sb.toString());
        this.mModAnalyzer.analyze(modDevice.getProductId() >> 4, z);
    }
}
