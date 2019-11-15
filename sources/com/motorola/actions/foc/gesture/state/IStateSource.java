package com.motorola.actions.foc.gesture.state;

public interface IStateSource {
    boolean isStateAcceptableToTurnOff();

    boolean isStateAcceptableToTurnOn();

    void start();

    void stop();
}
