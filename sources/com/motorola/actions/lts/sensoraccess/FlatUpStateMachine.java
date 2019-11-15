package com.motorola.actions.lts.sensoraccess;

import com.motorola.actions.utils.MALogger;
import java.util.HashMap;

class FlatUpStateMachine {
    private static final long FLATUP_DELAY = 500;
    private static final MALogger LOGGER = new MALogger(FlatUpStateMachine.class);
    private HashMap<Conditions, Condition> mConditions = new HashMap<>();
    private ActionsSensorObserver mSensorObserver;

    private enum Conditions {
        UNKNOWN,
        LIFTING,
        MOTION_LESS,
        NOT_STOWED
    }

    enum Event {
        FLAT_UP,
        MOTION,
        STOWED
    }

    FlatUpStateMachine(ActionsSensorObserver actionsSensorObserver) {
        this.mSensorObserver = actionsSensorObserver;
    }

    /* access modifiers changed from: 0000 */
    public void registerConditions() {
        this.mConditions.clear();
        this.mConditions.put(Conditions.LIFTING, new TimeDependentCondition(FLATUP_DELAY));
        ((Condition) this.mConditions.get(Conditions.LIFTING)).init();
        this.mConditions.put(Conditions.MOTION_LESS, new SimpleCondition());
        ((Condition) this.mConditions.get(Conditions.MOTION_LESS)).init();
        this.mConditions.put(Conditions.NOT_STOWED, new SimpleCondition());
        ((Condition) this.mConditions.get(Conditions.NOT_STOWED)).init();
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x003e, code lost:
        r1 = r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x005b, code lost:
        if (r0 == com.motorola.actions.lts.sensoraccess.FlatUpStateMachine.Conditions.UNKNOWN) goto L_0x0076;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x005d, code lost:
        ((com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions.Condition) r3.mConditions.get(r0)).onChange(r1);
        checkConditions();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void fireEvent(com.motorola.actions.lts.sensoraccess.FlatUpStateMachine.Event r4, boolean r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            com.motorola.actions.utils.MALogger r0 = LOGGER     // Catch:{ all -> 0x0078 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0078 }
            r1.<init>()     // Catch:{ all -> 0x0078 }
            java.lang.String r2 = "Event fired: type='"
            r1.append(r2)     // Catch:{ all -> 0x0078 }
            java.lang.String r2 = r4.toString()     // Catch:{ all -> 0x0078 }
            r1.append(r2)     // Catch:{ all -> 0x0078 }
            java.lang.String r2 = "' value="
            r1.append(r2)     // Catch:{ all -> 0x0078 }
            r1.append(r5)     // Catch:{ all -> 0x0078 }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x0078 }
            r0.mo11957d(r1)     // Catch:{ all -> 0x0078 }
            com.motorola.actions.lts.sensoraccess.FlatUpStateMachine$Conditions r0 = com.motorola.actions.lts.sensoraccess.FlatUpStateMachine.Conditions.UNKNOWN     // Catch:{ Exception -> 0x006c }
            com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions$Event r1 = com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions.Event.ACTIVE     // Catch:{ Exception -> 0x006c }
            int[] r2 = com.motorola.actions.lts.sensoraccess.FlatUpStateMachine.C05681.f41x3c30dd74     // Catch:{ Exception -> 0x006c }
            int r4 = r4.ordinal()     // Catch:{ Exception -> 0x006c }
            r4 = r2[r4]     // Catch:{ Exception -> 0x006c }
            switch(r4) {
                case 1: goto L_0x004a;
                case 2: goto L_0x0040;
                case 3: goto L_0x0035;
                default: goto L_0x0032;
            }     // Catch:{ Exception -> 0x006c }
        L_0x0032:
            com.motorola.actions.utils.MALogger r4 = LOGGER     // Catch:{ Exception -> 0x006c }
            goto L_0x0054
        L_0x0035:
            com.motorola.actions.lts.sensoraccess.FlatUpStateMachine$Conditions r0 = com.motorola.actions.lts.sensoraccess.FlatUpStateMachine.Conditions.NOT_STOWED     // Catch:{ Exception -> 0x006c }
            if (r5 == 0) goto L_0x003c
            com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions$Event r4 = com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions.Event.INACTIVE     // Catch:{ Exception -> 0x006c }
            goto L_0x003e
        L_0x003c:
            com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions$Event r4 = com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions.Event.ACTIVE     // Catch:{ Exception -> 0x006c }
        L_0x003e:
            r1 = r4
            goto L_0x0059
        L_0x0040:
            com.motorola.actions.lts.sensoraccess.FlatUpStateMachine$Conditions r0 = com.motorola.actions.lts.sensoraccess.FlatUpStateMachine.Conditions.MOTION_LESS     // Catch:{ Exception -> 0x006c }
            if (r5 == 0) goto L_0x0047
            com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions$Event r4 = com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions.Event.INACTIVE     // Catch:{ Exception -> 0x006c }
            goto L_0x003e
        L_0x0047:
            com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions$Event r4 = com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions.Event.ACTIVE     // Catch:{ Exception -> 0x006c }
            goto L_0x003e
        L_0x004a:
            com.motorola.actions.lts.sensoraccess.FlatUpStateMachine$Conditions r0 = com.motorola.actions.lts.sensoraccess.FlatUpStateMachine.Conditions.LIFTING     // Catch:{ Exception -> 0x006c }
            if (r5 == 0) goto L_0x0051
            com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions$Event r4 = com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions.Event.INACTIVE     // Catch:{ Exception -> 0x006c }
            goto L_0x003e
        L_0x0051:
            com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions$Event r4 = com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions.Event.ACTIVE     // Catch:{ Exception -> 0x006c }
            goto L_0x003e
        L_0x0054:
            java.lang.String r5 = "Unknown Event received"
            r4.mo11959e(r5)     // Catch:{ Exception -> 0x006c }
        L_0x0059:
            com.motorola.actions.lts.sensoraccess.FlatUpStateMachine$Conditions r4 = com.motorola.actions.lts.sensoraccess.FlatUpStateMachine.Conditions.UNKNOWN     // Catch:{ Exception -> 0x006c }
            if (r0 == r4) goto L_0x0076
            java.util.HashMap<com.motorola.actions.lts.sensoraccess.FlatUpStateMachine$Conditions, com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions$Condition> r4 = r3.mConditions     // Catch:{ Exception -> 0x006c }
            java.lang.Object r4 = r4.get(r0)     // Catch:{ Exception -> 0x006c }
            com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions$Condition r4 = (com.motorola.actions.lts.sensoraccess.LiftToSilenceConditions.Condition) r4     // Catch:{ Exception -> 0x006c }
            r4.onChange(r1)     // Catch:{ Exception -> 0x006c }
            r3.checkConditions()     // Catch:{ Exception -> 0x006c }
            goto L_0x0076
        L_0x006c:
            r4 = move-exception
            com.motorola.actions.utils.MALogger r5 = LOGGER     // Catch:{ all -> 0x0078 }
            java.lang.String r4 = r4.getMessage()     // Catch:{ all -> 0x0078 }
            r5.mo11959e(r4)     // Catch:{ all -> 0x0078 }
        L_0x0076:
            monitor-exit(r3)
            return
        L_0x0078:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.motorola.actions.lts.sensoraccess.FlatUpStateMachine.fireEvent(com.motorola.actions.lts.sensoraccess.FlatUpStateMachine$Event, boolean):void");
    }

    private void checkConditions() {
        boolean z = true;
        for (Condition isFullfilled : this.mConditions.values()) {
            z &= isFullfilled.isFullfilled();
        }
        if (z && this.mSensorObserver != null) {
            this.mSensorObserver.onLiftToSilence();
        }
    }

    /* access modifiers changed from: 0000 */
    public void reset() {
        for (Condition init : this.mConditions.values()) {
            init.init();
        }
    }
}
