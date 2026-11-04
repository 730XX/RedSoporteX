package com.example.redsoportex.feature.lending;
import com.example.redsoportex.demo.DemoStore;
public final class LoanRequestPresenter implements LoanRequestContract.Presenter {
    private LoanRequestContract.View view; private String itemId;
    public LoanRequestPresenter(LoanRequestContract.View view){this.view=view;}
    @Override public void load(String id){itemId=id;if(view==null)return;DemoStore.Item i=DemoStore.get().item(id);if(i==null)view.close();else view.showItem(i);}
    @Override public void submit(String text,String notes){if(view==null)return;int days;try{days=Integer.parseInt(text);}catch(NumberFormatException e){view.showDaysError();return;}if(days<1){view.showDaysError();return;}DemoStore.get().addLoan(itemId,days,notes.trim());view.openLoans();}
    @Override public void detachView(){view=null;}
}
