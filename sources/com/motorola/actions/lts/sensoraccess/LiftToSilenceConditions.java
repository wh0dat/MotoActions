package com.motorola.actions.lts.sensoraccess;

import java.util.Date;

class LiftToSilenceConditions {

    interface Condition {
        void init();

        boolean isFullfilled();

        void onChange(Event event);
    }

    enum Event {
        ACTIVE,
        INACTIVE
    }

    static class SimpleCondition implements Condition {
        State mState = State.INITIALIZED;

        enum State {
            INITIALIZED,
            ACTIVATED,
            DISABLED
        }

        SimpleCondition() {
        }

        public void init() {
            this.mState = State.INITIALIZED;
        }

        public void onChange(Event event) {
            if (State.DISABLED != this.mState) {
                this.mState = Event.ACTIVE == event ? State.ACTIVATED : State.DISABLED;
            }
        }

        public boolean isFullfilled() {
            return State.ACTIVATED == this.mState;
        }
    }

    static class TimeDependentCondition extends SimpleCondition {
        Date mActivationDate;
        long mActivationDelay;
        State mState = State.INITIALIZED;

        enum State {
            INITIALIZED,
            TRIGGERED,
            ACTIVATED,
            DISABLED
        }

        TimeDependentCondition(long j) {
            this.mActivationDelay = j;
        }

        public void init() {
            this.mState = State.INITIALIZED;
            this.mActivationDate = null;
        }

        public void onChange(Event event) {
            switch (this.mState) {
                case INITIALIZED:
                    if (Event.INACTIVE == event) {
                        this.mActivationDate = new Date();
                        this.mState = State.TRIGGERED;
                        return;
                    }
                    this.mState = State.DISABLED;
                    return;
                case TRIGGERED:
                    if (Event.ACTIVE == event) {
                        this.mState = new Date().getTime() - this.mActivationDate.getTime() > this.mActivationDelay ? State.ACTIVATED : State.DISABLED;
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        public boolean isFullfilled() {
            return State.ACTIVATED == this.mState;
        }
    }

    LiftToSilenceConditions() {
    }
}
