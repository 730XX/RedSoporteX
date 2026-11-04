package com.example.redsoportex.feature.incidents;
import com.example.redsoportex.demo.DemoStore;
public final class ReportsPresenter implements ReportsContract.Presenter {private ReportsContract.View view;public ReportsPresenter(ReportsContract.View v){view=v;}public void load(){if(view!=null)view.showReports(DemoStore.get().incidents());}public void detachView(){view=null;}}
