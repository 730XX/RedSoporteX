package com.example.redsoportex.domain.repository;

import com.example.redsoportex.domain.model.NeedAlert;

import java.util.List;

public interface AlertRepository {

    interface Callback<T> {
        void onSuccess(T result);
        void onError(String message);
    }

    void createAlert(Long categoryId, String urgency, int radiusKm, Callback<NeedAlert> callback);

    void getMyAlerts(Callback<List<NeedAlert>> callback);

    void cancelAlert(Long id, Callback<NeedAlert> callback);
}
