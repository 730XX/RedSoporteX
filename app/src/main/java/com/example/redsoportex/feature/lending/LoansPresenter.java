package com.example.redsoportex.feature.lending;

import androidx.annotation.NonNull;

import com.example.redsoportex.data.repository.LendingRepositoryImpl;
import com.example.redsoportex.domain.model.Lending;
import com.example.redsoportex.domain.repository.LendingRepository;

import java.util.List;

public final class LoansPresenter implements LoansContract.Presenter {

    private LoansContract.View view;
    private final LendingRepository lendingRepository;

    public LoansPresenter(@NonNull LoansContract.View view) {
        this(view, new LendingRepositoryImpl());
    }

    public LoansPresenter(@NonNull LoansContract.View view, @NonNull LendingRepository lendingRepository) {
        this.view = view;
        this.lendingRepository = lendingRepository;
    }

    @Override
    public void load() {
        if (view == null) return;
        view.showLoading(true);

        lendingRepository.getMyLendings(new LendingRepository.Callback<List<Lending>>() {
            @Override
            public void onSuccess(List<Lending> loans) {
                if (view != null) {
                    view.showLoading(false);
                    view.showLoans(loans);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showLoading(false);
                    view.showError(message);
                }
            }
        });
    }

    @Override
    public void detachView() {
        view = null;
    }
}
