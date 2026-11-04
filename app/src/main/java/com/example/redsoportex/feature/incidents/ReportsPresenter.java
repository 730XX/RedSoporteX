package com.example.redsoportex.feature.incidents;

import androidx.annotation.NonNull;

import com.example.redsoportex.data.repository.IncidentRepositoryImpl;
import com.example.redsoportex.domain.model.Incident;
import com.example.redsoportex.domain.repository.IncidentRepository;

import java.util.List;

public final class ReportsPresenter implements ReportsContract.Presenter {

    private ReportsContract.View view;
    private final IncidentRepository incidentRepository;

    public ReportsPresenter(ReportsContract.View v) {
        this(v, new IncidentRepositoryImpl());
    }

    public ReportsPresenter(ReportsContract.View v, @NonNull IncidentRepository incidentRepository) {
        this.view = v;
        this.incidentRepository = incidentRepository;
    }

    @Override
    public void load() {
        if (view == null) return;
        view.showLoading(true);
        incidentRepository.getMyReportedIncidents(new IncidentRepository.Callback<List<Incident>>() {
            @Override
            public void onSuccess(List<Incident> result) {
                if (view != null) {
                    view.showLoading(false);
                    view.showReports(result);
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
