package com.example.redsoportex.feature.lending;
import com.example.redsoportex.demo.DemoStore;
public interface LoanDetailContract { interface View { void showLoan(DemoStore.Loan loan,DemoStore.Item item);void close(); } interface Presenter { void load(String id);void advance();void detachView(); } }
