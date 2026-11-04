package com.example.redsoportex.feature.alerts;

import androidx.annotation.NonNull;

import com.example.redsoportex.data.repository.AlertRepositoryImpl;
import com.example.redsoportex.data.repository.ItemRepositoryImpl;
import com.example.redsoportex.domain.model.Category;
import com.example.redsoportex.domain.model.NeedAlert;
import com.example.redsoportex.domain.repository.AlertRepository;
import com.example.redsoportex.domain.repository.ItemRepository;

import java.util.List;

public final class AlertFormPresenter implements AlertFormContract.Presenter {

    private AlertFormContract.View view;
    private final AlertRepository alertRepository;
    private final ItemRepository itemRepository;

    public AlertFormPresenter(AlertFormContract.View v) {
        this(v, new AlertRepositoryImpl(), new ItemRepositoryImpl());
    }

    public AlertFormPresenter(AlertFormContract.View v,
                              @NonNull AlertRepository alertRepository,
                              @NonNull ItemRepository itemRepository) {
        this.view = v;
        this.alertRepository = alertRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public void loadCategories() {
        if (view == null) return;
        view.showLoading(true);
        itemRepository.getCategories(new ItemRepository.Callback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {
                if (view != null) {
                    view.showLoading(false);
                    view.showCategories(result);
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
    public void save(Long categoryId, String urgency, String radiusStr) {
        if (view == null) return;

        boolean catError = (categoryId == null || categoryId <= 0);
        boolean urgError = (urgency == null || urgency.trim().isEmpty());

        int radius = 10;
        boolean radError = false;
        if (radiusStr != null && !radiusStr.trim().isEmpty()) {
            try {
                radius = Integer.parseInt(radiusStr.trim());
                if (radius <= 0) {
                    radError = true;
                }
            } catch (NumberFormatException e) {
                radError = true;
            }
        }

        view.showCategoryError(catError);
        view.showUrgencyError(urgError);
        view.showRadiusError(radError);

        if (!catError && !urgError && !radError) {
            String urgencyEnum = mapUrgencyToEnum(urgency);
            view.showLoading(true);
            alertRepository.createAlert(categoryId, urgencyEnum, radius, new AlertRepository.Callback<NeedAlert>() {
                @Override
                public void onSuccess(NeedAlert result) {
                    if (view != null) {
                        view.showLoading(false);
                        view.close();
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
    }

    private String mapUrgencyToEnum(String urgency) {
        if (urgency == null) return "MEDIUM";
        String normalized = urgency.trim().toUpperCase();
        if (normalized.contains("ALTA") || normalized.contains("HIGH")) {
            return "HIGH";
        } else if (normalized.contains("BAJA") || normalized.contains("LOW")) {
            return "LOW";
        } else {
            return "MEDIUM";
        }
    }

    @Override
    public void detachView() {
        view = null;
    }
}
