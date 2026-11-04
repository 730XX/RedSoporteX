package com.example.redsoportex.feature.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.example.redsoportex.core.ui.BaseFragment;
import com.example.redsoportex.databinding.FragmentItemDetailBinding;
import com.example.redsoportex.demo.DemoStore;

public class ItemDetailFragment extends BaseFragment<FragmentItemDetailBinding> implements ItemDetailContract.View {
    private ItemDetailContract.Presenter presenter;
    private String itemId;

    @NonNull
    @Override
    protected FragmentItemDetailBinding createBinding(@NonNull LayoutInflater i, @Nullable ViewGroup c) {
        return FragmentItemDetailBinding.inflate(i, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle state) {
        super.onViewCreated(v, state);
        itemId = requireArguments().getString("itemId");
        getBinding().toolbar.tvTitulo.setText(R.string.item_detail_title);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> close());
        getBinding().btnRequest.setOnClickListener(x -> {
            Bundle b = new Bundle();
            b.putString("itemId", itemId);
            NavHostFragment.findNavController(this).navigate(R.id.loanRequestFragment, b);
        });
        getBinding().btnRequest.setVisibility(DemoStore.get().isDonorMode() ? View.GONE : View.VISIBLE);
        presenter = new ItemDetailPresenter(this);
        presenter.load(itemId);
    }

    @Override
    public void showItem(DemoStore.Item i) {
        getBinding().tvItemTitle.setText(i.title);
        getBinding().tvItemSubtitle.setText(getString(R.string.category_condition_format, i.category, i.condition));
        getBinding().tvDescription.setText(i.description);
        getBinding().tvDonor.setText(getString(R.string.donor_format, i.donor));
        getBinding().tvLocation.setText(getString(R.string.item_location));
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
