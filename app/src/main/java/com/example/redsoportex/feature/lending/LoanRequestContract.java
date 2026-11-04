package com.example.redsoportex.feature.lending;

import androidx.annotation.StringRes;

import com.example.redsoportex.domain.model.Item;

public interface LoanRequestContract {

    interface View {
        void showLoading(boolean loading);
        void showItem(Item item);
        void showDaysError(@StringRes int messageResId);
        void showGeneralError(String message);
        void openLoans();
        void close();
    }

    interface Presenter {
        void load(Long itemId);
        void submit(String days, String notes);
        void detachView();
    }
}
