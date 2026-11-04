package com.example.redsoportex.feature.alerts;

import com.example.redsoportex.domain.model.NeedAlert;

import java.util.List;

public interface AlertsContract {
    interface View {
        void showAlerts(List<NeedAlert> alerts);
        void showLoading(boolean loading);
        void showError(String message);
    }

    interface Presenter {
        void load();
        void cancel(Long id);
        void detachView();
    }
}
