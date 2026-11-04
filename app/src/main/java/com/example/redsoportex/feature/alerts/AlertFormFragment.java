package com.example.redsoportex.feature.alerts;

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
import com.example.redsoportex.databinding.FragmentAlertFormBinding;
import com.example.redsoportex.domain.model.Category;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AlertFormFragment extends BaseFragment<FragmentAlertFormBinding> implements AlertFormContract.View {

    private AlertFormContract.Presenter presenter;
    private List<Category> categories = new ArrayList<>();
    private Long selectedCategoryId = null;

    @NonNull
    @Override
    protected FragmentAlertFormBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentAlertFormBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        getBinding().toolbar.tvTitulo.setText(R.string.create_alert);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> close());

        presenter = new AlertFormPresenter(this);

        String[] urgencies = {
                getString(R.string.urgency_high),
                getString(R.string.urgency_medium),
                getString(R.string.urgency_low)
        };
        getBinding().etUrgencia.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, urgencies));
        getBinding().etUrgencia.setOnItemClickListener((parent, view, pos, id) -> getBinding().tilUrgencia.setError(null));

        if (getBinding().etRadio.getText() == null || getBinding().etRadio.getText().toString().trim().isEmpty()) {
            getBinding().etRadio.setText("10");
        }

        presenter.loadCategories();

        getBinding().btnSave.setOnClickListener(x -> {
            String urgency = getBinding().etUrgencia.getText() != null ? getBinding().etUrgencia.getText().toString() : "";
            String radius = getBinding().etRadio.getText() != null ? getBinding().etRadio.getText().toString() : "";
            presenter.save(selectedCategoryId, urgency, radius);
        });
    }

    @Override
    public void showCategories(List<Category> categories) {
        if (!isAdded() || getContext() == null) return;
        this.categories = (categories != null) ? categories : new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (Category c : this.categories) {
            names.add(c.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, names);
        getBinding().etCategoria.setAdapter(adapter);
        getBinding().etCategoria.setOnItemClickListener((parent, view, pos, id) -> {
            getBinding().tilCategoria.setError(null);
            Object item = parent.getItemAtPosition(pos);
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
    public void showCategoryError(boolean show) {
        getBinding().tilCategoria.setError(show ? getString(R.string.required_selection) : null);
    }

    @Override
    public void showUrgencyError(boolean show) {
        getBinding().tilUrgencia.setError(show ? getString(R.string.required_selection) : null);
    }

    @Override
    public void showRadiusError(boolean show) {
        getBinding().tilRadio.setError(show ? getString(R.string.invalid_radius) : null);
    }

    @Override
    public void showLoading(boolean loading) {
        if (getBinding() == null) return;
        getBinding().getRoot().setAlpha(loading ? 0.6f : 1.0f);
        getBinding().btnSave.setEnabled(!loading);
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
