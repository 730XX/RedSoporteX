package com.example.redsoportex.feature.incidents;

import com.example.redsoportex.domain.model.Incident;

import java.util.List;

public interface ReportsContract {

    interface View {
        void showReports(List<Incident> reports);
        void showLoading(boolean loading);
        void showError(String message);
    }

    interface Presenter {
        void load();
        void detachView();
    }
}
