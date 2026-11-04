package com.example.redsoportex.feature.alerts;

import androidx.annotation.NonNull;

import com.example.redsoportex.data.repository.AlertRepositoryImpl;
import com.example.redsoportex.domain.model.NeedAlert;
import com.example.redsoportex.domain.repository.AlertRepository;

import java.util.List;

public final class AlertsPresenter implements AlertsContract.Presenter {

    private AlertsContract.View view;
    private final AlertRepository alertRepository;

    public AlertsPresenter(AlertsContract.View view) {
        this(view, new AlertRepositoryImpl());
    }

    public AlertsPresenter(AlertsContract.View view, @NonNull AlertRepository alertRepository) {
        this.view = view;
        this.alertRepository = alertRepository;
    }

    @Override
    public void load() {
        if (view == null) return;
        view.showLoading(true);
        alertRepository.getMyAlerts(new AlertRepository.Callback<List<NeedAlert>>() {
            @Override
            public void onSuccess(List<NeedAlert> result) {
                if (view != null) {
                    view.showLoading(false);
                    view.showAlerts(result);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showLoading(false);
                    view.showError(message);
                }
            }
        });
    }

    @Override
    public void cancel(Long id) {
        if (view == null || id == null) return;
        view.showLoading(true);
        alertRepository.cancelAlert(id, new AlertRepository.Callback<NeedAlert>() {
            @Override
            public void onSuccess(NeedAlert result) {
                if (view != null) {
                    view.showLoading(false);
                    load();
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showLoading(false);
                    view.showError(message);
                }
            }
        });
    }

    @Override
    public void detachView() {
        view = null;
    }
}
