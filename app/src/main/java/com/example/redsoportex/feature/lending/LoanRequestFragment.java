package com.example.redsoportex.feature.lending;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.example.redsoportex.core.ui.BaseFragment;
import com.example.redsoportex.core.ui.CardRenderer;
import com.example.redsoportex.databinding.FragmentLoanRequestBinding;
import com.example.redsoportex.domain.model.Item;
import com.google.android.material.snackbar.Snackbar;

public class LoanRequestFragment extends BaseFragment<FragmentLoanRequestBinding> implements LoanRequestContract.View {

    private LoanRequestContract.Presenter presenter;

    @NonNull
    @Override
    protected FragmentLoanRequestBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentLoanRequestBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle state) {
        super.onViewCreated(v, state);

        getBinding().toolbar.tvTitulo.setText(R.string.loan_request_title);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> close());
        getBinding().etDays.setText("14");

        Long itemId = null;
        if (getArguments() != null) {
            Object idArg = getArguments().get("itemId");
            if (idArg instanceof Long) {
                itemId = (Long) idArg;
            } else if (idArg instanceof Number) {
                itemId = ((Number) idArg).longValue();
            } else if (idArg instanceof String) {
                try {
                    itemId = Long.parseLong((String) idArg);
                } catch (NumberFormatException ignored) {
                    itemId = null;
                }
            }
        }

        presenter = new LoanRequestPresenter(this);
        presenter.load(itemId);

        getBinding().btnConfirm.setOnClickListener(x -> {
            getBinding().tilEtDays.setError(null);
            String days = getBinding().etDays.getText() != null ? getBinding().etDays.getText().toString() : "";
            String notes = getBinding().etNotes.getText() != null ? getBinding().etNotes.getText().toString() : "";
            presenter.submit(days, notes);
        });
    }

    @Override
    public void showLoading(boolean loading) {
        if (getBinding() == null) return;
        getBinding().getRoot().setAlpha(loading ? 0.6f : 1.0f);
        getBinding().btnConfirm.setEnabled(!loading);
    }

    @Override
    public void showItem(Item item) {
        if (getBinding() == null || item == null) return;
        getBinding().itemContainer.removeAllViews();
        String condition = item.getConservationStatus() != null ? item.getConservationStatus() : "";
        String subtitle = (item.getCategoryName() != null ? item.getCategoryName() + " · " : "") + condition;
        CardRenderer.add(getBinding().itemContainer, item.getTitle(), subtitle, getString(R.string.available), false, null);
    }

    @Override
    public void showDaysError(int messageResId) {
        if (getBinding() != null) {
            getBinding().tilEtDays.setError(getString(messageResId));
        }
    }

    @Override
    public void showGeneralError(String message) {
        if (getView() != null && message != null) {
            Snackbar.make(getBinding().getRoot(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void openLoans() {
        NavOptions opts = new NavOptions.Builder()
                .setPopUpTo(R.id.catalogFragment, false)
                .build();
        NavHostFragment.findNavController(this).navigate(R.id.loansFragment, null, opts);
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
