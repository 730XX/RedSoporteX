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
import com.example.redsoportex.databinding.FragmentDonorListBinding;
import com.example.redsoportex.demo.DemoStore;
import com.example.redsoportex.domain.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class DonorListFragment extends BaseFragment<FragmentDonorListBinding> implements DonorListPresenter.View {

    private DonorListPresenter presenter;
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
        if (presenter != null) {
            presenter.load();
        }
    }

    @Override
    public void showDonorItems(List<Item> items) {
        if (!isAdded() || getBinding() == null) return;
        getBinding().itemsContainer.removeAllViews();
        int count = 0;

        if (incoming) {
            for (DemoStore.Loan l : DemoStore.get().loans()) {
                DemoStore.Item i = DemoStore.get().item(l.itemId);
                if (i == null || !"Tú".equals(i.donor)) continue;
                count++;
                CardRenderer.add(getBinding().itemsContainer, i.title, getString(R.string.loan_days_format, l.days),
                        getString(l.status.equals("PENDING") ? R.string.status_pending : l.status.equals("ACTIVE") ? R.string.status_active : R.string.status_returned),
                        l.status.equals("PENDING"), () -> {
                            Bundle b = new Bundle();
                            b.putString("loanId", l.id);
                            NavHostFragment.findNavController(this).navigate(R.id.loanDetailFragment, b);
                        });
            }
        } else {
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
