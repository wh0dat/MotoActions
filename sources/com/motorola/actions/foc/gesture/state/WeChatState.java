package com.motorola.actions.foc.gesture.state;

import com.motorola.actions.utils.RunningTasksUtils;

public class WeChatState implements IStateSource {
    public void start() {
    }

    public void stop() {
    }

    public WeChatState() {
        start();
    }

    public boolean isStateAcceptableToTurnOn() {
        return !isAppWeChatOnForeground();
    }

    public boolean isStateAcceptableToTurnOff() {
        return !isAppWeChatOnForeground();
    }

    private boolean isAppWeChatOnForeground() {
        return RunningTasksUtils.isTopTaskPackageName("com.tencent.mm");
    }
}
