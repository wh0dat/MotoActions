package com.motorola.mod;

import com.motorola.mod.ModDevice.Interface;

public interface ModListener {
    void onCapabilityChanged(ModDevice modDevice);

    void onConnectionStatusChanged(ModDevice modDevice, ModConnection modConnection);

    void onDeviceHotplug(ModDevice modDevice, boolean z);

    void onEnumerationDone(ModDevice modDevice);

    void onEnumerationDone(ModDevice modDevice, Interface interfaceR, boolean z);

    void onInterfaceUpDown(ModDevice modDevice, Interface interfaceR, boolean z);

    void onLinuxDeviceChanged(ModDevice modDevice, ModInterfaceDelegation modInterfaceDelegation, boolean z);
}
