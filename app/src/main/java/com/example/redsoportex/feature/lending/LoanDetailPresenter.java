package com.example.redsoportex.feature.lending;
import com.example.redsoportex.demo.DemoStore;
public final class LoanDetailPresenter implements LoanDetailContract.Presenter { private LoanDetailContract.View view;private String loanId;public LoanDetailPresenter(LoanDetailContract.View view){this.view=view;}
    @Override public void load(String id){loanId=id;if(view==null)return;DemoStore.Loan l=DemoStore.get().loan(id);DemoStore.Item i=l==null?null:DemoStore.get().item(l.itemId);if(i==null)view.close();else view.showLoan(l,i);}
    @Override public void advance(){DemoStore.Loan l=DemoStore.get().loan(loanId);if(l==null||view==null)return;if(l.status.equals("PENDING"))l.status="ACTIVE";else if(l.status.equals("ACTIVE"))l.status="RETURNED";load(loanId);}
    @Override public void detachView(){view=null;}
}
