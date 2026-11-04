package com.example.redsoportex.feature.catalog;

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
import com.example.redsoportex.databinding.FragmentItemDetailBinding;
import com.example.redsoportex.domain.model.Item;
import com.google.android.material.snackbar.Snackbar;

public class ItemDetailFragment extends BaseFragment<FragmentItemDetailBinding> implements ItemDetailContract.View {

    private ItemDetailContract.Presenter presenter;
    private Long itemId;

    @NonNull
    @Override
    protected FragmentItemDetailBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentItemDetailBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle state) {
        super.onViewCreated(v, state);

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

        getBinding().toolbar.tvTitulo.setText(R.string.item_detail_title);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> close());

        getBinding().btnRequest.setOnClickListener(x -> {
            Bundle b = new Bundle();
            if (itemId != null) {
                b.putLong("itemId", itemId);
                b.putString("itemId", String.valueOf(itemId));
            }
            NavHostFragment.findNavController(this).navigate(R.id.loanRequestFragment, b);
        });

        boolean isDonor = SessionManager.getInstance(requireContext()).isDonor();
        getBinding().btnRequest.setVisibility(isDonor ? View.GONE : View.VISIBLE);

        presenter = new ItemDetailPresenter(this);
        presenter.load(itemId);
    }

    @Override
    public void showLoading(boolean loading) {
        if (getBinding() == null) return;
        getBinding().getRoot().setAlpha(loading ? 0.6f : 1.0f);
    }

    @Override
    public void showItem(Item item) {
        if (getBinding() == null) return;
        getBinding().tvItemTitle.setText(item.getTitle());

        String category = item.getCategoryName() != null ? item.getCategoryName() : "";
        String condition = item.getConservationStatus() != null ? item.getConservationStatus() : "";
        getBinding().tvItemSubtitle.setText(getString(R.string.category_condition_format, category, condition));

        getBinding().tvDescription.setText(item.getDescription());

        String donor = item.getDonorName() != null ? item.getDonorName() : "-";
        getBinding().tvDonor.setText(getString(R.string.donor_format, donor));
        getBinding().tvLocation.setText(getString(R.string.item_location));

        String status = item.getAvailabilityStatus() != null ? item.getAvailabilityStatus() : getString(R.string.available);
        if (item.isAvailable()) {
            status = getString(R.string.available);
        }
        getBinding().tvStatus.setText(status);
    }

    @Override
    public void showError(String message) {
        if (getView() != null && message != null) {
            Snackbar.make(getBinding().getRoot(), message, Snackbar.LENGTH_LONG).show();
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
