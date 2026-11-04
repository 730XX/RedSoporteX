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
import com.example.redsoportex.domain.model.Category;
import com.example.redsoportex.domain.model.Item;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ItemFormFragment extends BaseFragment<FragmentDonorItemFormBinding> implements ItemFormContract.View {

    private ItemFormContract.Presenter presenter;
    private List<Category> categories = new ArrayList<>();
    private Long selectedCategoryId = null;

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

        String[] conditions = {
                getString(R.string.donor_condition_excellent),
                getString(R.string.donor_condition_good),
                getString(R.string.donor_condition_fair)
        };
        getBinding().etCondition.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, conditions));
        getBinding().etCondition.setOnItemClickListener((a, b, c, d) -> getBinding().tilCondition.setError(null));

        presenter = new ItemFormPresenter(this);
        presenter.loadInitialData();

        if (getArguments() != null && getArguments().containsKey("itemId")) {
            Object itemObj = getArguments().get("itemId");
            Long itemId = null;
            if (itemObj instanceof Long) {
                itemId = (Long) itemObj;
            } else if (itemObj instanceof String) {
                try {
                    itemId = Long.parseLong((String) itemObj);
                } catch (NumberFormatException ignored) {
                }
            }
            if (itemId != null) {
                presenter.load(itemId);
            }
        }

        getBinding().btnSave.setOnClickListener(v -> {
            String title = getBinding().etTitle.getText() != null ? getBinding().etTitle.getText().toString() : "";
            String description = getBinding().etDescription.getText() != null ? getBinding().etDescription.getText().toString() : "";
            String condition = getBinding().etCondition.getText() != null ? getBinding().etCondition.getText().toString() : "";
            String image = getBinding().etImage.getText() != null ? getBinding().etImage.getText().toString() : "";
            String location = getBinding().etLocation.getText() != null ? getBinding().etLocation.getText().toString() : "";

            presenter.save(selectedCategoryId, title, description, condition, image, location);
        });
    }

    @Override
    public void showCategories(List<Category> categories) {
        if (!isAdded() || getContext() == null) return;
        this.categories = categories != null ? categories : new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (Category c : this.categories) {
            names.add(c.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, names);
        getBinding().etCategory.setAdapter(adapter);
        getBinding().etCategory.setOnItemClickListener((parent, v, position, id) -> {
            getBinding().tilCategory.setError(null);
            Object item = parent.getItemAtPosition(position);
            if (item != null) {
                String selectedName = item.toString();
                for (Category c : this.categories) {
                    if (c.getName().equalsIgnoreCase(selectedName)) {
                        selectedCategoryId = c.getId();
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void showItem(Item item) {
        if (!isAdded()) return;
        getBinding().toolbar.tvTitulo.setText(R.string.donor_edit_title);
        getBinding().btnSave.setText(R.string.donor_save);

        selectedCategoryId = item.getCategoryId();
        if (item.getCategoryName() != null) {
            getBinding().etCategory.setText(item.getCategoryName(), false);
        }
        getBinding().tilCategory.setEnabled(false);

        getBinding().etTitle.setText(item.getTitle());
        getBinding().etDescription.setText(item.getDescription());

        String cond = item.getConservationStatus();
        if ("EXCELLENT".equalsIgnoreCase(cond)) {
            getBinding().etCondition.setText(getString(R.string.donor_condition_excellent), false);
        } else if ("FAIR".equalsIgnoreCase(cond)) {
            getBinding().etCondition.setText(getString(R.string.donor_condition_fair), false);
        } else {
            getBinding().etCondition.setText(getString(R.string.donor_condition_good), false);
        }

        getBinding().etImage.setText(item.getImageUrl());
    }

    @Override
    public void showLoading(boolean loading) {
        if (getBinding() == null) return;
        getBinding().btnSave.setEnabled(!loading);
        getBinding().btnSave.setAlpha(loading ? 0.6f : 1.0f);
    }

    @Override
    public void showError(ItemFormContract.Field field, int message) {
        if (getBinding() == null) return;
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
    public void showGeneralError(String message) {
        if (getView() != null && message != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void close() {
        if (isAdded()) {
            NavHostFragment.findNavController(this).navigateUp();
        }
    }

    @Override
    public void saved(boolean editing) {
        if (isAdded()) {
            NavHostFragment.findNavController(this).getPreviousBackStackEntry().getSavedStateHandle()
                    .set("donorMessage", editing ? R.string.donor_saved : R.string.donor_published);
            close();
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
