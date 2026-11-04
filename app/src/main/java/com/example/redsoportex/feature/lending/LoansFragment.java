package com.example.redsoportex.feature.lending;
import android.os.Bundle;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;
import androidx.annotation.NonNull;import androidx.annotation.Nullable;import androidx.navigation.fragment.NavHostFragment;
import com.example.redsoportex.R;import com.example.redsoportex.core.ui.BaseFragment;import com.example.redsoportex.core.ui.BottomNavHelper;import com.example.redsoportex.core.ui.CardRenderer;import com.example.redsoportex.databinding.FragmentLoansBinding;import com.example.redsoportex.demo.DemoStore;import java.util.List;
public class LoansFragment extends BaseFragment<FragmentLoansBinding> implements LoansContract.View {
    private LoansContract.Presenter presenter;
    @NonNull @Override protected FragmentLoansBinding createBinding(@NonNull LayoutInflater i,@Nullable ViewGroup c){return FragmentLoansBinding.inflate(i,c,false);}
    @Override public void onViewCreated(@NonNull View v,@Nullable Bundle state){super.onViewCreated(v,state);getBinding().toolbar.tvTitulo.setText(R.string.my_loans);getBinding().toolbar.btnVolver.setOnClickListener(x->NavHostFragment.findNavController(this).navigateUp());BottomNavHelper.setup(getBinding().bottomNav.bottomNavigation,NavHostFragment.findNavController(this),R.id.nav_loans);presenter=new LoansPresenter(this);presenter.load();}
    @Override public void onResume(){super.onResume();if(presenter!=null)presenter.load();}
    @Override public void showLoans(List<DemoStore.Loan> loans){getBinding().itemsContainer.removeAllViews();getBinding().tvEmpty.setVisibility(loans.isEmpty()?View.VISIBLE:View.GONE);for(DemoStore.Loan l:loans){DemoStore.Item item=DemoStore.get().item(l.itemId);if(item==null)continue;int status=l.status.equals("PENDING")?R.string.status_pending:l.status.equals("ACTIVE")?R.string.status_active:R.string.status_returned;CardRenderer.add(getBinding().itemsContainer,item.title,getString(R.string.loan_days_format,l.days),getString(status),l.status.equals("PENDING"),()->{Bundle b=new Bundle();b.putString("loanId",l.id);NavHostFragment.findNavController(this).navigate(R.id.loanDetailFragment,b);});}}
    @Override public void onDestroyView(){if(presenter!=null){presenter.detachView();presenter=null;}super.onDestroyView();}
}
