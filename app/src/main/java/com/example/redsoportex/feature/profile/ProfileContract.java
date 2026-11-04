package com.example.redsoportex.feature.profile;
public interface ProfileContract {interface View {void openReports();void exitDemo();}interface Presenter {void onReports();void onExit();void detachView();}}
