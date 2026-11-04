package com.example.redsoportex.feature.catalog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.google.android.material.snackbar.Snackbar;
import com.example.redsoportex.core.ui.BaseFragment;
import com.example.redsoportex.core.ui.BottomNavHelper;
import com.example.redsoportex.core.ui.CardRenderer;
import com.example.redsoportex.databinding.FragmentCatalogBinding;
import com.example.redsoportex.demo.DemoStore;

import java.util.List;

public class CatalogFragment extends BaseFragment<FragmentCatalogBinding> implements CatalogContract.View {
    private CatalogContract.Presenter presenter;

    @NonNull
    @Override
    protected FragmentCatalogBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentCatalogBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle state) {
        super.onViewCreated(v, state);
        presenter = new CatalogPresenter(this);
        boolean donor = DemoStore.get().isDonorMode();
        getBinding().btnPublicar.setVisibility(donor ? View.VISIBLE : View.GONE);
        getBinding().btnPublicar.setOnClickListener(x -> NavHostFragment.findNavController(this).navigate(R.id.donorItemFormFragment));
        BottomNavHelper.setup(getBinding().bottomNav.bottomNavigation, NavHostFragment.findNavController(this), R.id.nav_catalog);
        getBinding().etBuscar.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int st, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int st, int before, int count) {
                update();
            }

            public void afterTextChanged(Editable e) {
            }
        });
        getBinding().chipCategories.setOnCheckedStateChangeListener((group, ids) -> update());
        update();
    }

    private void update() {
        if (presenter == null) return;
        int id = getBinding().chipCategories.getCheckedChipId();
        String cat = id == R.id.chip_chairs ? getString(R.string.category_chair) : id == R.id.chip_walkers ? getString(R.string.category_walker) : "";
        presenter.filter(getBinding().etBuscar.getText().toString(), cat);
    }

    @Override
    public void showItems(List<DemoStore.Item> items) {
        getBinding().itemsContainer.removeAllViews();
        getBinding().tvEmpty.setVisibility(items.isEmpty() ? View.VISIBLE : View.GONE);
        for (DemoStore.Item i : items)
            CardRenderer.add(getBinding().itemsContainer, i.title, i.condition + " · " + i.location, getString(R.string.available), false, () -> {
                Bundle b = new Bundle();
                b.putString("itemId", i.id);
                NavHostFragment.findNavController(this).navigate(DemoStore.get().isDonorMode() && "Tú".equals(i.donor) ? R.id.donorItemFormFragment : R.id.itemDetailFragment, b);
            });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) update();
        androidx.navigation.NavBackStackEntry entry = NavHostFragment.findNavController(this).getCurrentBackStackEntry();
        if (entry != null) {
            Integer message = entry.getSavedStateHandle().remove("donorMessage");
            if (message != null)
                Snackbar.make(getBinding().getRoot(), message, Snackbar.LENGTH_SHORT).show();
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
