package com.motorola.actions.zenmode;

public interface AutomaticRulesConfigSubject {
    void notifyObservers();

    void register(AutomaticRulesConfigObserver automaticRulesConfigObserver);

    void unregister(AutomaticRulesConfigObserver automaticRulesConfigObserver);
}
