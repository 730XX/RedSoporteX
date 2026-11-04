package com.example.redsoportex.feature.incidents;

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
import com.example.redsoportex.databinding.FragmentIncidentFormBinding;
import com.google.android.material.snackbar.Snackbar;

public class IncidentFormFragment extends BaseFragment<FragmentIncidentFormBinding> implements IncidentFormContract.View {

    private IncidentFormContract.Presenter presenter;

    @NonNull
    @Override
    protected FragmentIncidentFormBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentIncidentFormBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        getBinding().toolbar.tvTitulo.setText(R.string.report_incident_title);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> close());

        presenter = new IncidentFormPresenter(this);

        Object loanIdArg = null;
        if (getArguments() != null) {
            loanIdArg = getArguments().get("loanId");
        }
        presenter.load(loanIdArg);

        String[] types = {
                getString(R.string.incident_damage),
                getString(R.string.incident_nonreturn),
                getString(R.string.incident_hygiene),
                getString(R.string.incident_other)
        };
        getBinding().etTipo.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, types));
        getBinding().etTipo.setOnItemClickListener((parent, view, pos, id) -> getBinding().tilTipo.setError(null));

        getBinding().btnSave.setOnClickListener(x -> {
            String type = getBinding().etTipo.getText() != null ? getBinding().etTipo.getText().toString() : "";
            String desc = getBinding().etDescripcion.getText() != null ? getBinding().etDescripcion.getText().toString() : "";
            presenter.submit(type, desc);
        });
    }

    @Override
    public void showLoan(String itemTitle) {
        if (getBinding() == null) return;
        getBinding().tvLoan.setText(itemTitle != null ? itemTitle : "-");
    }

    @Override
    public void showTypeError(boolean value) {
        if (getBinding() == null) return;
        getBinding().tilTipo.setError(value ? getString(R.string.required_selection) : null);
    }

    @Override
    public void showDescriptionError(int code) {
        if (getBinding() == null) return;
        getBinding().tilDescripcion.setError(code == 0 ? null : getString(code == 1 ? R.string.error_required : R.string.max_description));
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
    public void openReports() {
        NavHostFragment.findNavController(this).navigate(R.id.reportsFragment);
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
