package com.example.redsoportex.feature.lending;

import com.example.redsoportex.domain.model.Lending;

public interface LoanDetailContract {

    interface View {
        void showLoading(boolean loading);
        void showLoan(Lending loan, boolean isDonor);
        void showError(String message);
        void showActionSuccess(int messageResId);
        void close();
    }

    interface Presenter {
        void load(Long loanId);
        void advance();
        void checkIn();
        void checkOut();
        void detachView();
    }
}
