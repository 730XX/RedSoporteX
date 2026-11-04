package com.example.redsoportex.feature.alerts;

import android.os.Bundle;
import android.view.*;

import androidx.annotation.*;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.example.redsoportex.core.ui.*;
import com.example.redsoportex.databinding.FragmentAlertsBinding;
import com.example.redsoportex.demo.DemoStore;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class AlertsFragment extends BaseFragment<FragmentAlertsBinding> implements AlertsContract.View {
    private AlertsContract.Presenter presenter;

    @NonNull
    protected FragmentAlertsBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentAlertsBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        getBinding().toolbar.tvTitulo.setText(R.string.my_alerts);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> NavHostFragment.findNavController(this).navigateUp());
        BottomNavHelper.setup(getBinding().bottomNav.bottomNavigation, NavHostFragment.findNavController(this), R.id.nav_alerts);
        presenter = new AlertsPresenter(this);
        presenter.load();
        getBinding().btnCreate.setOnClickListener(x -> NavHostFragment.findNavController(this).navigate(R.id.alertFormFragment));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.load();
    }

    public void showAlerts(List<DemoStore.Alert> alerts) {
        getBinding().itemsContainer.removeAllViews();
        getBinding().tvEmpty.setVisibility(alerts.isEmpty() ? View.VISIBLE : View.GONE);
        for (DemoStore.Alert a : alerts)
            CardRenderer.add(getBinding().itemsContainer, a.category, getString(R.string.alert_details_format, a.urgency, a.radius), getString(a.active ? R.string.alert_active : R.string.alert_cancelled), false, R.drawable.extra_bell34, a.active ? () -> new MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.cancel_alert).setMessage(R.string.cancel_alert_message).setNegativeButton(R.string.keep_action, null).setPositiveButton(R.string.cancel_action, (d, w) -> presenter.cancel(a.id)).show() : null);
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
