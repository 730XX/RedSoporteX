package com.example.redsoportex.feature.donor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.example.redsoportex.core.session.SessionManager;
import com.example.redsoportex.core.ui.BaseFragment;
import com.example.redsoportex.core.ui.BottomNavHelper;
import com.example.redsoportex.core.ui.CardRenderer;
import com.example.redsoportex.data.repository.AlertRepositoryImpl;
import com.example.redsoportex.data.repository.LendingRepositoryImpl;
import com.example.redsoportex.databinding.FragmentDonorListBinding;
import com.example.redsoportex.domain.model.Item;
import com.example.redsoportex.domain.model.Lending;
import com.example.redsoportex.domain.model.NeedAlert;
import com.example.redsoportex.domain.repository.AlertRepository;
import com.example.redsoportex.domain.repository.LendingRepository;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class DonorListFragment extends BaseFragment<FragmentDonorListBinding> implements DonorListPresenter.View {

    private DonorListPresenter presenter;
    private LendingRepository lendingRepository;
    private AlertRepository alertRepository;
    private boolean incoming;

    @NonNull
    @Override
    protected FragmentDonorListBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentDonorListBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);

        incoming = NavHostFragment.findNavController(this).getCurrentDestination().getId() == R.id.incomingFragment;

        getBinding().toolbar.tvTitulo.setText(incoming ? R.string.incoming_title : R.string.my_items_title);
        getBinding().tvSubtitle.setText(incoming ? R.string.incoming_subtitle : R.string.my_items_subtitle);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> NavHostFragment.findNavController(this).navigateUp());

        getBinding().btnPublish.setVisibility(incoming ? View.GONE : View.VISIBLE);
        getBinding().btnPublish.setOnClickListener(x -> NavHostFragment.findNavController(this).navigate(R.id.donorItemFormFragment));

        BottomNavHelper.setup(getBinding().bottomNav.bottomNavigation, NavHostFragment.findNavController(this), incoming ? R.id.nav_alerts : R.id.nav_loans);

        getBinding().tvInfoTitle.setText(incoming ? R.string.incoming_note : R.string.donor_items_note);
        getBinding().tvInfoDescription.setText(incoming ? R.string.incoming_description : R.string.donor_items_description);

        lendingRepository = new LendingRepositoryImpl();
        alertRepository = new AlertRepositoryImpl();
        presenter = new DonorListPresenter(this, SessionManager.getInstance(requireContext()));

        NavBackStackEntry entry = NavHostFragment.findNavController(this).getCurrentBackStackEntry();
        if (entry != null) {
            entry.getSavedStateHandle().<Integer>getLiveData("donorMessage").observe(getViewLifecycleOwner(), msgRes -> {
                if (msgRes != null && msgRes != 0) {
                    Snackbar.make(requireView(), getString(msgRes), Snackbar.LENGTH_SHORT).show();
                    entry.getSavedStateHandle().remove("donorMessage");
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (incoming) {
            loadIncomingLoans();
        } else if (presenter != null) {
            presenter.load();
        }
    }

    private void loadIncomingLoans() {
        showLoading(true);
        if (lendingRepository == null) {
            lendingRepository = new LendingRepositoryImpl();
        }
        if (alertRepository == null) {
            alertRepository = new AlertRepositoryImpl();
        }
        lendingRepository.getMyLendings(new LendingRepository.Callback<List<Lending>>() {
            @Override
            public void onSuccess(List<Lending> lendings) {
                if (!isAdded() || getBinding() == null) return;
                alertRepository.getMyAlerts(new AlertRepository.Callback<List<NeedAlert>>() {
                    @Override
                    public void onSuccess(List<NeedAlert> alerts) {
                        if (!isAdded() || getBinding() == null) return;
                        showLoading(false);
                        displayIncomingLoansAndAlerts(lendings, alerts);
                    }

                    @Override
                    public void onError(String message) {
                        if (!isAdded() || getBinding() == null) return;
                        showLoading(false);
                        displayIncomingLoansAndAlerts(lendings, null);
                    }
                });
            }

            @Override
            public void onError(String message) {
                if (!isAdded() || getBinding() == null) return;
                alertRepository.getMyAlerts(new AlertRepository.Callback<List<NeedAlert>>() {
                    @Override
                    public void onSuccess(List<NeedAlert> alerts) {
                        if (!isAdded() || getBinding() == null) return;
                        showLoading(false);
                        displayIncomingLoansAndAlerts(null, alerts);
                    }

                    @Override
                    public void onError(String alertErr) {
                        if (!isAdded() || getBinding() == null) return;
                        showLoading(false);
                        showError(message);
                    }
                });
            }
        });
    }

    private void displayIncomingLoansAndAlerts(List<Lending> loans, List<NeedAlert> alerts) {
        if (!isAdded() || getBinding() == null) return;
        getBinding().itemsContainer.removeAllViews();
        int count = 0;

        if (loans != null) {
            for (Lending l : loans) {
                count++;
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

        if (alerts != null) {
            for (NeedAlert alert : alerts) {
                if (alert.isCancelled()) continue;
                count++;
                String title = "Alerta: " + (alert.getCategoryName() != null ? alert.getCategoryName() : "Implemento");
                String requester = (alert.getRequesterName() != null && !alert.getRequesterName().isEmpty())
                        ? alert.getRequesterName()
                        : "Comunidad";
                int radius = alert.getMaxRadiusKm() != null ? alert.getMaxRadiusKm() : 5;
                String subtitle = "Solicitante: " + requester + " · Radio " + radius + " km";
                String urgencyBadge = formatUrgencyBadge(alert.getUrgencyLevel());

                CardRenderer.add(
                        getBinding().itemsContainer,
                        title,
                        subtitle,
                        urgencyBadge,
                        true,
                        () -> NavHostFragment.findNavController(this).navigate(R.id.donorItemFormFragment)
                );
            }
        }

        getBinding().tvEmpty.setText(R.string.donor_list_empty);
        getBinding().tvEmpty.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
    }

    private String formatUrgencyBadge(String urgencyLevel) {
        if (urgencyLevel == null) {
            return "Urgencia Media";
        }
        String u = urgencyLevel.toUpperCase();
        if (u.contains("HIGH") || u.contains("ALTA") || u.contains("CRITICAL")) {
            return "Urgencia Alta";
        }
        if (u.contains("LOW") || u.contains("BAJA")) {
            return "Urgencia Baja";
        }
        return "Urgencia Media";
    }

    @Override
    public void showDonorItems(List<Item> items) {
        if (!isAdded() || getBinding() == null) return;
        if (incoming) {
            loadIncomingLoans();
            return;
        }
        getBinding().itemsContainer.removeAllViews();
        int count = 0;

        if (items != null) {
            for (Item item : items) {
                count++;
                String subtitle = item.getCategoryName() != null ? item.getCategoryName() : item.getConservationStatus();
                String statusStr = item.isAvailable() ? getString(R.string.available) : item.getAvailabilityStatus();
                CardRenderer.add(getBinding().itemsContainer, item.getTitle(), subtitle, statusStr, false, () -> {
                    Bundle b = new Bundle();
                    b.putLong("itemId", item.getId());
                    NavHostFragment.findNavController(this).navigate(R.id.donorItemFormFragment, b);
                });
            }
        }

        getBinding().tvEmpty.setText(R.string.donor_list_empty);
        getBinding().tvEmpty.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showLoading(boolean loading) {
        if (getBinding() == null) return;
        if (loading && getBinding().itemsContainer.getChildCount() == 0) {
            getBinding().tvEmpty.setText(R.string.donor_list_empty);
            getBinding().tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError(String message) {
        if (!isAdded() || getView() == null) return;
        Snackbar.make(getView(), message != null ? message : "Error al cargar las donaciones", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.detach();
            presenter = null;
        }
        super.onDestroyView();
    }
}
