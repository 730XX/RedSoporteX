package com.example.redsoportex.feature.incidents;

import android.os.Bundle;
import android.view.*;

import androidx.annotation.*;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.example.redsoportex.core.ui.*;
import com.example.redsoportex.databinding.FragmentReportsBinding;
import com.example.redsoportex.demo.DemoStore;

import java.util.List;

public class ReportsFragment extends BaseFragment<FragmentReportsBinding> implements ReportsContract.View {
    private ReportsContract.Presenter presenter;

    @NonNull
    protected FragmentReportsBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentReportsBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);
        getBinding().toolbar.tvTitulo.setText(R.string.my_reports);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> NavHostFragment.findNavController(this).navigateUp());
        presenter = new ReportsPresenter(this);
        presenter.load();
    }

    public void showReports(List<DemoStore.Incident> reports) {
        getBinding().itemsContainer.removeAllViews();
        getBinding().tvEmpty.setVisibility(reports.isEmpty() ? View.VISIBLE : View.GONE);
        for (DemoStore.Incident r : reports) {
            DemoStore.Loan loan = DemoStore.get().loan(r.loanId);
            DemoStore.Item item = loan == null ? null : DemoStore.get().item(loan.itemId);
            if (item != null)
                CardRenderer.add(getBinding().itemsContainer, item.title, r.type, getString(R.string.incident_open), false, null);
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
