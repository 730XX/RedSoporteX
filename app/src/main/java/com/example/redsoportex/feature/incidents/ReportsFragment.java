package com.example.redsoportex.feature.incidents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.example.redsoportex.core.ui.BaseFragment;
import com.example.redsoportex.core.ui.CardRenderer;
import com.example.redsoportex.databinding.FragmentReportsBinding;
import com.example.redsoportex.domain.model.Incident;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ReportsFragment extends BaseFragment<FragmentReportsBinding> implements ReportsContract.View {

    private ReportsContract.Presenter presenter;

    @NonNull
    @Override
    protected FragmentReportsBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentReportsBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        getBinding().toolbar.tvTitulo.setText(R.string.my_reports);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> NavHostFragment.findNavController(this).navigateUp());
        getBinding().tvDemo.setVisibility(View.GONE);

        presenter = new ReportsPresenter(this);
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
    public void showReports(List<Incident> reports) {
        if (getBinding() == null) return;
        getBinding().itemsContainer.removeAllViews();
        boolean isEmpty = (reports == null || reports.isEmpty());
        getBinding().tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);

        if (!isEmpty) {
            for (Incident r : reports) {
                String title = r.getItemTitle() != null ? r.getItemTitle() : ("Préstamo #" + r.getLendingId());
                String type = r.getIncidentType() != null ? r.getIncidentType() : "";
                String status = r.getResolutionStatus() != null ? r.getResolutionStatus() : getString(R.string.incident_open);
                CardRenderer.add(getBinding().itemsContainer, title, type, status, false, R.drawable.extra_shield, null);
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
