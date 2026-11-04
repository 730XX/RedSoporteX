package com.example.redsoportex.feature.incidents;

import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;

import androidx.annotation.*;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.example.redsoportex.core.ui.BaseFragment;
import com.example.redsoportex.databinding.FragmentIncidentFormBinding;
import com.example.redsoportex.demo.DemoStore;

public class IncidentFormFragment extends BaseFragment<FragmentIncidentFormBinding> implements IncidentFormContract.View {
    private IncidentFormContract.Presenter presenter;

    @NonNull
    protected FragmentIncidentFormBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentIncidentFormBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        getBinding().toolbar.tvTitulo.setText(R.string.report_incident_title);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> close());
        presenter = new IncidentFormPresenter(this);
        presenter.load(requireArguments().getString("loanId"));
        String[] types = {getString(R.string.incident_damage), getString(R.string.incident_nonreturn), getString(R.string.incident_hygiene), getString(R.string.incident_other)};
        getBinding().etTipo.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, types));
        getBinding().etTipo.setOnItemClickListener((parent, view, pos, id) -> getBinding().tilTipo.setError(null));
        getBinding().btnSave.setOnClickListener(x -> presenter.submit(getBinding().etTipo.getText().toString(), getBinding().etDescripcion.getText().toString()));
    }

    public void showLoan(DemoStore.Item item) {
        getBinding().tvLoan.setText(item.title);
    }

    public void showTypeError(boolean value) {
        getBinding().tilTipo.setError(value ? getString(R.string.required_selection) : null);
    }

    public void showDescriptionError(int code) {
        getBinding().tilDescripcion.setError(code == 0 ? null : getString(code == 1 ? R.string.error_required : R.string.max_description));
    }

    public void openReports() {
        NavHostFragment.findNavController(this).navigate(R.id.reportsFragment);
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
