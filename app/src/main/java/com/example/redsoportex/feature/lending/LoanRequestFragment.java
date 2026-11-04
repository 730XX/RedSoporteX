package com.example.redsoportex.feature.lending;
import android.os.Bundle;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;
import androidx.annotation.NonNull;import androidx.annotation.Nullable;import androidx.navigation.NavOptions;import androidx.navigation.fragment.NavHostFragment;
import com.example.redsoportex.R;import com.example.redsoportex.core.ui.BaseFragment;import com.example.redsoportex.core.ui.CardRenderer;import com.example.redsoportex.databinding.FragmentLoanRequestBinding;import com.example.redsoportex.demo.DemoStore;
public class LoanRequestFragment extends BaseFragment<FragmentLoanRequestBinding> implements LoanRequestContract.View {
    private LoanRequestContract.Presenter presenter;
    @NonNull @Override protected FragmentLoanRequestBinding createBinding(@NonNull LayoutInflater i,@Nullable ViewGroup c){return FragmentLoanRequestBinding.inflate(i,c,false);}
    @Override public void onViewCreated(@NonNull View v,@Nullable Bundle state){super.onViewCreated(v,state);getBinding().toolbar.tvTitulo.setText(R.string.loan_request_title);getBinding().toolbar.btnVolver.setOnClickListener(x->close());getBinding().etDays.setText("14");presenter=new LoanRequestPresenter(this);presenter.load(requireArguments().getString("itemId"));getBinding().btnConfirm.setOnClickListener(x->{getBinding().tilEtDays.setError(null);presenter.submit(getBinding().etDays.getText().toString(),getBinding().etNotes.getText().toString());});}
    @Override public void showItem(DemoStore.Item i){getBinding().itemContainer.removeAllViews();CardRenderer.add(getBinding().itemContainer,i.title,i.condition+" · "+i.location,getString(R.string.available),false,null);}
    @Override public void showDaysError(){getBinding().tilEtDays.setError(getString(R.string.invalid_days));}
    @Override public void openLoans(){NavOptions opts=new NavOptions.Builder().setPopUpTo(R.id.catalogFragment,false).build();NavHostFragment.findNavController(this).navigate(R.id.loansFragment,null,opts);}
    @Override public void close(){NavHostFragment.findNavController(this).navigateUp();}
    @Override public void onDestroyView(){if(presenter!=null){presenter.detachView();presenter=null;}super.onDestroyView();}
}
