package com.example.redsoportex.feature.incidents;

import com.example.redsoportex.demo.DemoStore;

public final class IncidentFormPresenter implements IncidentFormContract.Presenter {
    private IncidentFormContract.View view;
    private String loanId;

    public IncidentFormPresenter(IncidentFormContract.View v) {
        view = v;
    }

    public void load(String id) {
        loanId = id;
        if (view == null) return;
        DemoStore.Loan loan = DemoStore.get().loan(id);
        DemoStore.Item item = loan == null ? null : DemoStore.get().item(loan.itemId);
        if (item == null) view.close();
        else view.showLoan(item);
    }

    public void submit(String type, String description) {
        if (view == null) return;
        boolean t = type.trim().isEmpty();
        int d = description.trim().isEmpty() ? 1 : description.length() > 500 ? 2 : 0;
        view.showTypeError(t);
        view.showDescriptionError(d);
        if (!t && d == 0) {
            DemoStore.get().addIncident(loanId, type, description.trim());
            view.openReports();
        }
    }

    public void detachView() {
        view = null;
    }
}
