package com.motorola.actions.nightdisplay.p009pi.states;

import com.motorola.actions.nightdisplay.p009pi.eventhandler.EventHandler;

/* renamed from: com.motorola.actions.nightdisplay.pi.states.UninitializedState */
public class UninitializedState extends State {
    public void takeEventHandler(EventHandler eventHandler) {
        eventHandler.inInitialState(this);
    }
}
