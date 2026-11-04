package com.example.redsoportex.feature.alerts;

import com.example.redsoportex.demo.DemoStore;

public final class AlertsPresenter implements AlertsContract.Presenter {
    private AlertsContract.View view;

    public AlertsPresenter(AlertsContract.View view) {
        this.view = view;
    }

    public void load() {
        if (view != null) view.showAlerts(DemoStore.get().alerts());
    }

    public void cancel(String id) {
        DemoStore.get().cancelAlert(id);
        load();
    }

    public void detachView() {
        view = null;
    }
}
