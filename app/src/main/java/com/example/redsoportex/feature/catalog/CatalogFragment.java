package com.example.redsoportex.feature.catalog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.example.redsoportex.core.session.SessionManager;
import com.example.redsoportex.core.ui.BaseFragment;
import com.example.redsoportex.core.ui.BottomNavHelper;
import com.example.redsoportex.core.ui.CardRenderer;
import com.example.redsoportex.databinding.FragmentCatalogBinding;
import com.example.redsoportex.domain.model.Category;
import com.example.redsoportex.domain.model.Item;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CatalogFragment extends BaseFragment<FragmentCatalogBinding> implements CatalogContract.View {

    private CatalogContract.Presenter presenter;
    private boolean isUpdatingCategories = false;

    @NonNull
    @Override
    protected FragmentCatalogBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentCatalogBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle state) {
        super.onViewCreated(v, state);
        presenter = new CatalogPresenter(this);

        boolean donor = SessionManager.getInstance(requireContext()).isDonor();
        getBinding().btnPublicar.setVisibility(donor ? View.VISIBLE : View.GONE);
        getBinding().btnPublicar.setOnClickListener(x ->
                NavHostFragment.findNavController(this).navigate(R.id.donorItemFormFragment)
        );

        BottomNavHelper.setup(getBinding().bottomNav.bottomNavigation, NavHostFragment.findNavController(this), R.id.nav_catalog);

        getBinding().etBuscar.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int count, int after) {}

            public void onTextChanged(CharSequence s, int st, int before, int count) {
                updateFilter();
            }

            public void afterTextChanged(Editable e) {}
        });

        getBinding().chipCategories.setOnCheckedStateChangeListener((group, ids) -> {
            if (!isUpdatingCategories) {
                updateFilter();
            }
        });

        presenter.loadData();
    }

    private void updateFilter() {
        if (presenter == null) return;
        int id = getBinding().chipCategories.getCheckedChipId();
        Long categoryId = null;
        if (id != View.NO_ID && id != R.id.chip_all) {
            View chip = getBinding().chipCategories.findViewById(id);
            if (chip != null && chip.getTag() instanceof Long) {
                categoryId = (Long) chip.getTag();
            }
        }
        String query = getBinding().etBuscar.getText() != null ? getBinding().etBuscar.getText().toString() : "";
        presenter.filter(query, categoryId);
    }

    @Override
    public void showLoading(boolean loading) {
        if (getBinding() == null) return;
        getBinding().itemsContainer.setAlpha(loading ? 0.5f : 1.0f);
    }

    @Override
    public void showCategories(List<Category> categories) {
        if (getBinding() == null || getContext() == null) return;
        isUpdatingCategories = true;

        ChipGroup group = getBinding().chipCategories;
        group.removeAllViews();

        Chip chipAll = new Chip(requireContext());
        chipAll.setId(R.id.chip_all);
        chipAll.setText(R.string.category_all);
        chipAll.setCheckable(true);
        chipAll.setCheckedIconVisible(false);
        chipAll.setChipBackgroundColor(ContextCompat.getColorStateList(requireContext(), R.color.chip_background));
        chipAll.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.chip_text));
        chipAll.setChipStrokeColor(ContextCompat.getColorStateList(requireContext(), R.color.outline_figma));
        chipAll.setChipStrokeWidth(getResources().getDisplayMetrics().density * 1.0f);
        chipAll.setTag(null);
        group.addView(chipAll);

        for (Category cat : categories) {
            Chip chip = new Chip(requireContext());
            chip.setText(cat.getName());
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setChipBackgroundColor(ContextCompat.getColorStateList(requireContext(), R.color.chip_background));
            chip.setTextColor(ContextCompat.getColorStateList(requireContext(), R.color.chip_text));
            chip.setChipStrokeColor(ContextCompat.getColorStateList(requireContext(), R.color.outline_figma));
            chip.setChipStrokeWidth(getResources().getDisplayMetrics().density * 1.0f);
            chip.setTag(cat.getId());
            group.addView(chip);
        }

        chipAll.setChecked(true);
        isUpdatingCategories = false;
    }

    @Override
    public void showItems(List<Item> items) {
        if (getBinding() == null || getContext() == null) return;
        getBinding().itemsContainer.removeAllViews();
        getBinding().tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);

        boolean isDonor = SessionManager.getInstance(requireContext()).isDonor();
        long currentUserId = SessionManager.getInstance(requireContext()).getUserId();

        for (Item item : items) {
            String categoryDisplay = item.getCategoryName() != null ? item.getCategoryName() : "";
            String conditionDisplay = item.getConservationStatus() != null ? item.getConservationStatus() : "";
            String subtitle = conditionDisplay;
            if (!categoryDisplay.isEmpty() && !conditionDisplay.isEmpty()) {
                subtitle = categoryDisplay + " · " + conditionDisplay;
            } else if (!categoryDisplay.isEmpty()) {
                subtitle = categoryDisplay;
            }

            String status = item.getAvailabilityStatus() != null ? item.getAvailabilityStatus() : getString(R.string.available);
            if (item.isAvailable()) {
                status = getString(R.string.available);
            }

            CardRenderer.add(getBinding().itemsContainer, item.getTitle(), subtitle, status, false, () -> {
                Bundle b = new Bundle();
                if (item.getId() != null) {
                    b.putLong("itemId", item.getId());
                    b.putString("itemId", String.valueOf(item.getId()));
                }
                boolean isOwnItem = isDonor && item.getDonorId() != null && item.getDonorId() == currentUserId;
                NavHostFragment.findNavController(this).navigate(
                        isOwnItem ? R.id.donorItemFormFragment : R.id.itemDetailFragment,
                        b
                );
            });
        }
    }

    @Override
    public void showError(String message) {
        if (getView() != null && message != null) {
            Snackbar.make(getBinding().getRoot(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        androidx.navigation.NavBackStackEntry entry = NavHostFragment.findNavController(this).getCurrentBackStackEntry();
        if (entry != null) {
            Integer message = entry.getSavedStateHandle().remove("donorMessage");
            if (message != null && getView() != null) {
                Snackbar.make(getBinding().getRoot(), message, Snackbar.LENGTH_SHORT).show();
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
