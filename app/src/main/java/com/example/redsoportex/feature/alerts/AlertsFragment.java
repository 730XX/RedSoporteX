package com.example.redsoportex.feature.alerts;

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
import com.example.redsoportex.databinding.FragmentAlertsBinding;
import com.example.redsoportex.domain.model.NeedAlert;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class AlertsFragment extends BaseFragment<FragmentAlertsBinding> implements AlertsContract.View {

    private AlertsContract.Presenter presenter;

    @NonNull
    @Override
    protected FragmentAlertsBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentAlertsBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        getBinding().toolbar.tvTitulo.setText(R.string.my_alerts);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> NavHostFragment.findNavController(this).navigateUp());
        getBinding().tvDemo.setVisibility(View.GONE);

        BottomNavHelper.setup(getBinding().bottomNav.bottomNavigation, NavHostFragment.findNavController(this), R.id.nav_alerts);

        presenter = new AlertsPresenter(this);
        presenter.load();

        getBinding().btnCreate.setOnClickListener(x -> NavHostFragment.findNavController(this).navigate(R.id.alertFormFragment));
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
        getBinding().btnCreate.setEnabled(!loading);
    }

    @Override
    public void showAlerts(List<NeedAlert> alerts) {
        if (getBinding() == null) return;
        getBinding().itemsContainer.removeAllViews();
        boolean isEmpty = (alerts == null || alerts.isEmpty());
        getBinding().tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);

        if (!isEmpty) {
            for (NeedAlert a : alerts) {
                String category = a.getCategoryName() != null ? a.getCategoryName() : "Categoría";
                int radius = a.getMaxRadiusKm() != null ? a.getMaxRadiusKm() : 10;
                String urgency = a.getUrgencyLevel() != null ? a.getUrgencyLevel() : "";
                String details = getString(R.string.alert_details_format, urgency, radius);
                String status = getString(a.isActive() ? R.string.alert_active : R.string.alert_cancelled);

                CardRenderer.add(
                        getBinding().itemsContainer,
                        category,
                        details,
                        status,
                        false,
                        R.drawable.extra_bell34,
                        a.isActive() ? () -> new MaterialAlertDialogBuilder(requireContext())
                                .setTitle(R.string.cancel_alert)
                                .setMessage(R.string.cancel_alert_message)
                                .setNegativeButton(R.string.keep_action, null)
                                .setPositiveButton(R.string.cancel_action, (d, w) -> presenter.cancel(a.getId()))
                                .show() : null
                );
            }
        }
    }

    @Override
    public void showError(String message) {
        if (getView() != null && getBinding() != null && message != null) {
            boolean isAuthError = message.toLowerCase().contains("no autorizado")
                    || message.toLowerCase().contains("autenticar")
                    || !com.example.redsoportex.core.session.SessionManager.getInstance(requireContext()).isLoggedIn();

            if (isAuthError) {
                getBinding().tvEmpty.setText("Inicia sesión para ver y crear alertas comunitarias.");
                getBinding().tvEmpty.setVisibility(View.VISIBLE);
                Snackbar.make(getBinding().getRoot(), "Inicia sesión para gestionar tus alertas", Snackbar.LENGTH_LONG)
                        .setAction("Ingresar", v -> NavHostFragment.findNavController(this).navigate(R.id.loginFragment))
                        .show();
            } else {
                Snackbar.make(getBinding().getRoot(), message, Snackbar.LENGTH_LONG).show();
            }
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
