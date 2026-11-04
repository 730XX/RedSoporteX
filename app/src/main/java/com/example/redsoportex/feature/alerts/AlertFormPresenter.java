package com.example.redsoportex.feature.alerts;

import com.example.redsoportex.demo.DemoStore;

public final class AlertFormPresenter implements AlertFormContract.Presenter {
    private AlertFormContract.View view;

    public AlertFormPresenter(AlertFormContract.View v) {
        view = v;
    }

    public void save(String category, String urgency, String value) {
        if (view == null) return;
        int radius;
        try {
            radius = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            radius = 0;
        }
        boolean c = category.trim().isEmpty(), u = urgency.trim().isEmpty(), r = radius < 1;
        view.showCategoryError(c);
        view.showUrgencyError(u);
        view.showRadiusError(r);
        if (!(c || u || r)) {
            DemoStore.get().addAlert(category, urgency, radius);
            view.close();
        }
    }

    public void detachView() {
        view = null;
    }
}
