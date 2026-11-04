package com.example.redsoportex.feature.donor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.example.redsoportex.core.ui.BaseFragment;
import com.example.redsoportex.databinding.FragmentDonorItemFormBinding;
import com.example.redsoportex.demo.DemoStore;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class ItemFormFragment extends BaseFragment<FragmentDonorItemFormBinding> implements ItemFormContract.View {
    private ItemFormContract.Presenter presenter;

    @NonNull
    @Override
    protected FragmentDonorItemFormBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentDonorItemFormBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        getBinding().toolbar.tvTitulo.setText(R.string.donor_publish_title);
        getBinding().toolbar.btnVolver.setOnClickListener(v -> close());
        String[] cats = {getString(R.string.category_chair), getString(R.string.category_walker), "Muletas y Bastones"};
        String[] conditions = {getString(R.string.donor_condition_excellent), getString(R.string.donor_condition_good), getString(R.string.donor_condition_fair)};
        getBinding().etCategory.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, cats));
        getBinding().etCondition.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, conditions));
        getBinding().etCategory.setOnItemClickListener((a, b, c, d) -> getBinding().tilCategory.setError(null));
        getBinding().etCondition.setOnItemClickListener((a, b, c, d) -> getBinding().tilCondition.setError(null));
        presenter = new ItemFormPresenter(this);
        if (getArguments() != null) presenter.load(getArguments().getString("itemId"));
        getBinding().btnSave.setOnClickListener(v -> presenter.save(getBinding().etCategory.getText().toString(), getBinding().etTitle.getText().toString(), getBinding().etDescription.getText().toString(), getBinding().etCondition.getText().toString(), getBinding().etImage.getText().toString(), getBinding().etLocation.getText().toString()));
    }

    @Override
    public void showItem(DemoStore.Item i) {
        getBinding().toolbar.tvTitulo.setText(R.string.donor_edit_title);
        getBinding().btnSave.setText(R.string.donor_save);
        getBinding().etCategory.setText(i.category, false);
        getBinding().tilCategory.setEnabled(false);
        getBinding().etTitle.setText(i.title);
        getBinding().etDescription.setText(i.description);
        getBinding().etCondition.setText(i.condition, false);
        getBinding().etImage.setText(i.imageUrl);
        getBinding().etLocation.setText(i.location);
    }

    @Override
    public void showError(ItemFormContract.Field field, int message) {
        TextInputLayout l;
        switch (field) {
            case CATEGORY:
                l = getBinding().tilCategory;
                break;
            case TITLE:
                l = getBinding().tilTitle;
                break;
            case DESCRIPTION:
                l = getBinding().tilDescription;
                break;
            case CONDITION:
                l = getBinding().tilCondition;
                break;
            case LOCATION:
                l = getBinding().tilLocation;
                break;
            default:
                return;
        }
        l.setError(message == 0 ? null : getString(message));
    }

    @Override
    public void close() {
        NavHostFragment.findNavController(this).navigateUp();
    }

    @Override
    public void saved(boolean editing) {
        NavHostFragment.findNavController(this).getPreviousBackStackEntry().getSavedStateHandle().set("donorMessage", editing ? R.string.donor_saved : R.string.donor_published);
        close();
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
