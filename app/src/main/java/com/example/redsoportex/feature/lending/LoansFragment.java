package com.example.redsoportex.feature.lending;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.example.redsoportex.core.ui.BaseFragment;
import com.example.redsoportex.core.ui.BottomNavHelper;
import com.example.redsoportex.core.ui.CardRenderer;
import com.example.redsoportex.databinding.FragmentLoansBinding;
import com.example.redsoportex.domain.model.Lending;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class LoansFragment extends BaseFragment<FragmentLoansBinding> implements LoansContract.View {

    private LoansContract.Presenter presenter;

    @NonNull
    @Override
    protected FragmentLoansBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentLoansBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle state) {
        super.onViewCreated(v, state);

        getBinding().toolbar.tvTitulo.setText(R.string.my_loans);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> NavHostFragment.findNavController(this).navigateUp());
        getBinding().tvDemo.setVisibility(View.GONE);

        BottomNavHelper.setup(
                getBinding().bottomNav.bottomNavigation,
                NavHostFragment.findNavController(this),
                R.id.nav_loans
        );

        presenter = new LoansPresenter(this);
        presenter.load();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.load();
        }
    }

    @Override
    public void showLoading(boolean loading) {
        if (getBinding() == null) return;
        getBinding().getRoot().setAlpha(loading ? 0.6f : 1.0f);
    }

    @Override
    public void showLoans(List<Lending> loans) {
        if (getBinding() == null) return;
        getBinding().itemsContainer.removeAllViews();
        boolean isEmpty = loans == null || loans.isEmpty();
        getBinding().tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);

        if (!isEmpty) {
            for (Lending l : loans) {
                int statusRes = l.isPending() ? R.string.status_pending
                        : l.isActive() ? R.string.status_active
                        : R.string.status_returned;

                int days = l.getDays();
                String durationStr = getString(R.string.loan_days_format, days);
                String title = l.getItemTitle() != null ? l.getItemTitle() : getString(R.string.loan_detail_title);

                CardRenderer.add(
                        getBinding().itemsContainer,
                        title,
                        durationStr,
                        getString(statusRes),
                        l.isPending(),
                        () -> {
                            Bundle b = new Bundle();
                            if (l.getId() != null) {
                                b.putLong("loanId", l.getId());
                                b.putString("loanId", String.valueOf(l.getId()));
                            }
                            NavHostFragment.findNavController(this).navigate(R.id.loanDetailFragment, b);
                        }
                );
            }
        }
    }

    @Override
    public void showError(String message) {
        if (getView() != null && message != null) {
            Snackbar.make(getBinding().getRoot(), message, Snackbar.LENGTH_LONG).show();
        }
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
