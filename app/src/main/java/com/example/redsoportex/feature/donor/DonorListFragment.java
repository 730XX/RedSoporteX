package com.example.redsoportex.feature.donor;

import android.os.Bundle;
import android.view.*;

import androidx.annotation.*;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.example.redsoportex.core.ui.*;
import com.example.redsoportex.databinding.FragmentDonorListBinding;
import com.example.redsoportex.demo.DemoStore;

public class DonorListFragment extends BaseFragment<FragmentDonorListBinding> implements DonorListPresenter.View {
    private DonorListPresenter presenter;
    private boolean incoming;

    @NonNull
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
        presenter = new DonorListPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.load();
    }

    public void render() {
        getBinding().itemsContainer.removeAllViews();
        int count = 0;
        if (incoming) {
            for (DemoStore.Loan l : DemoStore.get().loans()) {
                DemoStore.Item i = DemoStore.get().item(l.itemId);
                if (i == null || !"Tú".equals(i.donor)) continue;
                count++;
                CardRenderer.add(getBinding().itemsContainer, i.title, getString(R.string.loan_days_format, l.days), getString(l.status.equals("PENDING") ? R.string.status_pending : l.status.equals("ACTIVE") ? R.string.status_active : R.string.status_returned), l.status.equals("PENDING"), () -> {
                    Bundle b = new Bundle();
                    b.putString("loanId", l.id);
                    NavHostFragment.findNavController(this).navigate(R.id.loanDetailFragment, b);
                });
            }
        } else {
            for (DemoStore.Item i : DemoStore.get().donorItems()) {
                count++;
                CardRenderer.add(getBinding().itemsContainer, i.title, i.condition, getString(R.string.available), false, () -> {
                    Bundle b = new Bundle();
                    b.putString("itemId", i.id);
                    NavHostFragment.findNavController(this).navigate(R.id.donorItemFormFragment, b);
                });
            }
        }
        getBinding().tvEmpty.setText(R.string.donor_list_empty);
        getBinding().tvEmpty.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
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