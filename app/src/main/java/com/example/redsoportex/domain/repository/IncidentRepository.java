package com.example.redsoportex.domain.repository;

import com.example.redsoportex.domain.model.Incident;

import java.util.List;

public interface IncidentRepository {

    interface Callback<T> {
        void onSuccess(T result);
        void onError(String message);
    }

    void reportIncident(Long lendingId, String type, String description, Callback<Incident> callback);

    void getIncidentsByLending(Long lendingId, Callback<List<Incident>> callback);

    void getMyReportedIncidents(Callback<List<Incident>> callback);
}
