package com.example.redsoportex.feature.lending;
import com.example.redsoportex.demo.DemoStore;
public final class LoansPresenter implements LoansContract.Presenter { private LoansContract.View view;public LoansPresenter(LoansContract.View view){this.view=view;} @Override public void load(){if(view!=null)view.showLoans(DemoStore.get().loans());} @Override public void detachView(){view=null;} }
