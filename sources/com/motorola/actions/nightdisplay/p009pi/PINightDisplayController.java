package com.motorola.actions.nightdisplay.p009pi;

import com.motorola.actions.nightdisplay.common.PlatformAccess;
import com.motorola.actions.nightdisplay.p009pi.eventhandler.EventHandler;
import com.motorola.actions.nightdisplay.p009pi.eventhandler.EventHandlerFactory;
import com.motorola.actions.nightdisplay.p009pi.states.StateController;
import com.motorola.actions.utils.MALogger;

/* renamed from: com.motorola.actions.nightdisplay.pi.PINightDisplayController */
public class PINightDisplayController {
    private static final MALogger LOGGER = new MALogger(PINightDisplayController.class);
    private final PIContext mPIContext;

    public PINightDisplayController(PlatformAccess platformAccess) {
        this.mPIContext = new PIContext(new StateController(), platformAccess);
    }

    public void onEvent(PIEvent pIEvent) {
        EventHandler createEventHandler = EventHandlerFactory.createEventHandler(pIEvent, this.mPIContext, pIEvent.getTimeStamp());
        if (createEventHandler != null) {
            try {
                this.mPIContext.getStateController().onEvent(createEventHandler);
            } catch (Throwable th) {
                LOGGER.mo11960e("Error handling PI event", th);
            }
        } else {
            LOGGER.mo11959e("Could not create event handler");
        }
    }
}
