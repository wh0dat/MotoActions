package com.motorola.actions.nightdisplay.p009pi.eventhandler;

import com.motorola.actions.nightdisplay.p009pi.PIContext;
import com.motorola.actions.nightdisplay.p009pi.PIEvent;
import com.motorola.actions.utils.MALogger;
import java.util.Calendar;

/* renamed from: com.motorola.actions.nightdisplay.pi.eventhandler.EventHandlerFactory */
public final class EventHandlerFactory {
    private static final MALogger LOGGER = new MALogger(EventHandlerFactory.class);

    public static EventHandler createEventHandler(PIEvent pIEvent, PIContext pIContext, Calendar calendar) {
        MALogger mALogger = LOGGER;
        StringBuilder sb = new StringBuilder();
        sb.append("Received event: ");
        sb.append(pIEvent.getEvent());
        sb.append(" at ");
        sb.append(calendar.getTime().toString());
        mALogger.mo11957d(sb.toString());
        switch (pIEvent.getEvent()) {
            case DESTROY:
                return new DestroyEventHandler(pIContext, calendar);
            case TIME_CHANGED:
                return new TimeChangeEventHandler(pIContext, calendar);
            case SCREEN_OFF:
                return new ScreenOffEventHandler(pIContext, calendar);
            case SCREEN_ON:
                return new ScreenOnEventHandler(pIContext, calendar);
            case INIT:
                return new InitEventHandler(pIContext, calendar);
            case WAKE_UP_ALARM_UPDATE:
                return new WakeUpAlarmUpdateEventHandler(pIContext, calendar);
            case CONFIGURATION_CHANGED:
                return new ConfigEventHandler(pIContext, calendar);
            case SERVICE_PAUSED:
                return new ServicePauseEventHandler(pIContext, calendar);
            case SERVICE_RESUMED:
                return new ServiceResumeEventHandler(pIContext, calendar);
            case USER_CHANGED:
                return new UserChangedEventHandler(pIContext, calendar);
            case PREVIEW_UPDATE:
                return new PreviewUpdateEventHandler(pIContext, calendar, pIEvent.getBundle());
            case UNLOCKED:
                return new UnlockEventHandler(pIContext, calendar);
            case TIME_CONFIG_CHANGED:
                return new TimeConfigChangedHandler(pIContext, calendar);
            default:
                LOGGER.mo11959e("Invalid event received.Ignore");
                return null;
        }
    }
}
