package com.example.redsoportex.feature.profile;
public final class ProfilePresenter implements ProfileContract.Presenter {private ProfileContract.View view;public ProfilePresenter(ProfileContract.View v){view=v;}public void onReports(){if(view!=null)view.openReports();}public void onExit(){if(view!=null)view.exitDemo();}public void detachView(){view=null;}}
