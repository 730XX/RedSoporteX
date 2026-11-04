package com.example.redsoportex.feature.lending;

import com.example.redsoportex.domain.model.Lending;

import java.util.List;

public interface LoansContract {

    interface View {
        void showLoading(boolean loading);
        void showLoans(List<Lending> loans);
        void showError(String message);
    }

    interface Presenter {
        void load();
        void detachView();
    }
}
