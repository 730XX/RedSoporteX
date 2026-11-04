package com.example.redsoportex.feature.alerts;

import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;

import androidx.annotation.*;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.example.redsoportex.core.ui.BaseFragment;
import com.example.redsoportex.databinding.FragmentAlertFormBinding;

public class AlertFormFragment extends BaseFragment<FragmentAlertFormBinding> implements AlertFormContract.View {
    private AlertFormContract.Presenter presenter;

    @NonNull
    protected FragmentAlertFormBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentAlertFormBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        getBinding().toolbar.tvTitulo.setText(R.string.create_alert);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> close());
        presenter = new AlertFormPresenter(this);
        String[] categories = {getString(R.string.category_chair), getString(R.string.category_walker), "Muletas y Bastones"};
        String[] urgencies = {getString(R.string.urgency_high), getString(R.string.urgency_medium), getString(R.string.urgency_low)};
        getBinding().etCategoria.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, categories));
        getBinding().etUrgencia.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, urgencies));
        getBinding().etCategoria.setOnItemClickListener((parent, view, pos, id) -> getBinding().tilCategoria.setError(null));
        getBinding().etUrgencia.setOnItemClickListener((parent, view, pos, id) -> getBinding().tilUrgencia.setError(null));
        getBinding().btnSave.setOnClickListener(x -> presenter.save(getBinding().etCategoria.getText().toString(), getBinding().etUrgencia.getText().toString(), getBinding().etRadio.getText().toString()));
    }

    public void showCategoryError(boolean show) {
        getBinding().tilCategoria.setError(show ? getString(R.string.required_selection) : null);
    }

    public void showUrgencyError(boolean show) {
        getBinding().tilUrgencia.setError(show ? getString(R.string.required_selection) : null);
    }

    public void showRadiusError(boolean show) {
        getBinding().tilRadio.setError(show ? getString(R.string.invalid_radius) : null);
    }

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
