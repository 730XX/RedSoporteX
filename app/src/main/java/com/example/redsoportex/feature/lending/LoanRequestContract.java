package com.example.redsoportex.feature.lending;
import com.example.redsoportex.demo.DemoStore;
public interface LoanRequestContract { interface View { void showItem(DemoStore.Item item);void showDaysError();void openLoans();void close(); } interface Presenter { void load(String id);void submit(String days,String notes);void detachView(); } }
