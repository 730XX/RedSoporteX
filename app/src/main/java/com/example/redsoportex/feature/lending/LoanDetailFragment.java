package com.example.redsoportex.feature.lending;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.example.redsoportex.core.session.SessionManager;
import com.example.redsoportex.core.ui.BaseFragment;
import com.example.redsoportex.databinding.FragmentLoanDetailBinding;
import com.example.redsoportex.domain.model.Lending;
import com.google.android.material.snackbar.Snackbar;

public class LoanDetailFragment extends BaseFragment<FragmentLoanDetailBinding> implements LoanDetailContract.View {

    private LoanDetailContract.Presenter presenter;
    private Long loanId;

    @NonNull
    @Override
    protected FragmentLoanDetailBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentLoanDetailBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle state) {
        super.onViewCreated(v, state);

        boolean isDonor = SessionManager.getInstance(requireContext()).isDonor();
        getBinding().toolbar.tvTitulo.setText(isDonor ? R.string.request_detail : R.string.loan_detail_title);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> close());
        getBinding().tvDemo.setVisibility(View.GONE);

        if (getArguments() != null) {
            Object idArg = getArguments().get("loanId");
            if (idArg instanceof Long) {
                loanId = (Long) idArg;
            } else if (idArg instanceof Number) {
                loanId = ((Number) idArg).longValue();
            } else if (idArg instanceof String) {
                try {
                    loanId = Long.parseLong((String) idArg);
                } catch (NumberFormatException ignored) {
                    loanId = null;
                }
            }
        }

        presenter = new LoanDetailPresenter(this, SessionManager.getInstance(requireContext()));
        presenter.load(loanId);

        getBinding().btnConfirm.setOnClickListener(x -> presenter.advance());

        getBinding().btnReport.setOnClickListener(x -> {
            Bundle b = new Bundle();
            if (loanId != null) {
                b.putLong("loanId", loanId);
                b.putString("loanId", String.valueOf(loanId));
            }
            NavHostFragment.findNavController(this).navigate(R.id.incidentFormFragment, b);
        });
    }

    @Override
    public void showLoading(boolean loading) {
        if (getBinding() == null) return;
        getBinding().getRoot().setAlpha(loading ? 0.6f : 1.0f);
        getBinding().btnConfirm.setEnabled(!loading);
    }

    @Override
    public void showLoan(Lending loan, boolean isDonor) {
        if (getBinding() == null || loan == null) return;

        int statusRes = loan.isPending() ? R.string.status_pending
                : loan.isActive() ? R.string.status_active
                : R.string.status_returned;
        getBinding().tvStatus.setText(statusRes);

        String title = loan.getItemTitle() != null ? loan.getItemTitle() : getString(R.string.loan_detail_title);
        getBinding().tvLoanTitle.setText(title);

        String requester = loan.getRequesterName() != null ? loan.getRequesterName() : "-";
        if (isDonor && loan.getRequesterPhone() != null && !loan.getRequesterPhone().isEmpty()) {
            requester += " (" + loan.getRequesterPhone() + ")";
        }
        getBinding().tvRequester.setText(getString(R.string.requester) + ": " + requester);

        getBinding().tvDuration.setText(getString(R.string.loan_days_format, loan.getDays()));

        if (loan.getExpectedReturnAt() != null && !loan.getExpectedReturnAt().isEmpty()) {
            getBinding().tvReturn.setVisibility(View.VISIBLE);
            getBinding().tvReturn.setText(getString(R.string.expected_return) + ": " + loan.getExpectedReturnAt());
        } else {
            getBinding().tvReturn.setVisibility(View.GONE);
        }

        String notes = loan.getHandoverNotes();
        getBinding().tvNotes.setText((notes != null && !notes.trim().isEmpty()) ? notes : getString(R.string.no_notes));

        if (loan.isPending()) {
            if (isDonor) {
                getBinding().btnConfirm.setVisibility(View.VISIBLE);
                getBinding().btnConfirm.setText(R.string.confirm_handover);
            } else {
                getBinding().btnConfirm.setVisibility(View.GONE);
            }
        } else if (loan.isActive()) {
            if (!isDonor) {
                getBinding().btnConfirm.setVisibility(View.VISIBLE);
                getBinding().btnConfirm.setText(R.string.confirm_return);
            } else {
                getBinding().btnConfirm.setVisibility(View.GONE);
            }
        } else {
            getBinding().btnConfirm.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(String message) {
        if (getView() != null && message != null) {
            Snackbar.make(getBinding().getRoot(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showActionSuccess(int messageResId) {
        if (getView() != null) {
            Snackbar.make(getBinding().getRoot(), messageResId, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void close() {
        NavHostFragment.findNavController(this).navigateUp();
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
        super.onDestroyView();
    }
}
