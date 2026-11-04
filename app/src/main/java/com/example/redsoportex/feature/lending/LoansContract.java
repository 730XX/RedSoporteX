package com.example.redsoportex.feature.lending;
import com.example.redsoportex.demo.DemoStore;import java.util.List;
public interface LoansContract { interface View { void showLoans(List<DemoStore.Loan> loans); } interface Presenter { void load();void detachView(); } }
