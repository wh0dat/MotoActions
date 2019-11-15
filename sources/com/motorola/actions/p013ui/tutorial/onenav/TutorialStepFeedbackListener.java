package com.motorola.actions.p013ui.tutorial.onenav;

/* renamed from: com.motorola.actions.ui.tutorial.onenav.TutorialStepFeedbackListener */
public interface TutorialStepFeedbackListener {
    void onConsecutiveErrors();

    void onDone();

    void onLongTimeout();

    void onSuccess();
}
