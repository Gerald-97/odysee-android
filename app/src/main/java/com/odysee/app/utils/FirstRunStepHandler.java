package com.odysee.app.utils;

public interface FirstRunStepHandler {
    void onCompleted(int completedStep);
    void onRequestInProgress(boolean showProgress);
    void onRequestCompleted(int step);
    void onChannelNameUpdated(String channelName);
    void onStarted();
    void onSkipped();
}
