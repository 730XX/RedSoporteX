package com.example.redsoportex.feature.lending;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.redsoportex.R;
import com.example.redsoportex.core.session.SessionManager;
import com.example.redsoportex.data.repository.LendingRepositoryImpl;
import com.example.redsoportex.domain.model.Lending;
import com.example.redsoportex.domain.repository.LendingRepository;

public final class LoanDetailPresenter implements LoanDetailContract.Presenter {

    private LoanDetailContract.View view;
    private final LendingRepository lendingRepository;
    private final SessionManager sessionManager;
    private Lending currentLending;

    public LoanDetailPresenter(@NonNull LoanDetailContract.View view, @NonNull SessionManager sessionManager) {
        this(view, new LendingRepositoryImpl(), sessionManager);
    }

    public LoanDetailPresenter(@NonNull LoanDetailContract.View view,
                               @NonNull LendingRepository lendingRepository,
                               @NonNull SessionManager sessionManager) {
        this.view = view;
        this.lendingRepository = lendingRepository;
        this.sessionManager = sessionManager;
    }

    @Override
    public void load(@Nullable Long id) {
        if (view == null) return;
        if (id == null) {
            view.close();
            return;
        }

        view.showLoading(true);
        lendingRepository.getLendingById(id, new LendingRepository.Callback<Lending>() {
            @Override
            public void onSuccess(Lending lending) {
                if (view != null) {
                    currentLending = lending;
                    view.showLoading(false);
                    if (lending == null) {
                        view.close();
                    } else {
                        view.showLoan(lending, computeIsDonor(lending));
                    }
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
    public void advance() {
        if (currentLending == null) return;
        if (currentLending.isPending()) {
            checkIn();
        } else if (currentLending.isActive()) {
            checkOut();
        }
    }

    @Override
    public void checkIn() {
        if (currentLending == null || currentLending.getId() == null) return;
        if (view == null) return;

        view.showLoading(true);
        lendingRepository.checkIn(currentLending.getId(), new LendingRepository.Callback<Lending>() {
            @Override
            public void onSuccess(Lending result) {
                if (view != null) {
                    currentLending = result;
                    view.showLoading(false);
                    view.showActionSuccess(R.string.confirm_handover);
                    view.showLoan(result, computeIsDonor(result));
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
    public void checkOut() {
        if (currentLending == null || currentLending.getId() == null) return;
        if (view == null) return;

        view.showLoading(true);
        lendingRepository.checkOut(currentLending.getId(), new LendingRepository.Callback<Lending>() {
            @Override
            public void onSuccess(Lending result) {
                if (view != null) {
                    currentLending = result;
                    view.showLoading(false);
                    view.showActionSuccess(R.string.confirm_return);
                    view.showLoan(result, computeIsDonor(result));
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

    private boolean computeIsDonor(Lending lending) {
        if (sessionManager.isDonor()) {
            return true;
        }
        if (lending != null && lending.getRequesterId() != null && sessionManager.getUserId() != -1) {
            return lending.getRequesterId() != sessionManager.getUserId();
        }
        return false;
    }
}
