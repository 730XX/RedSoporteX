package com.example.redsoportex.feature.alerts;

import com.example.redsoportex.domain.model.Category;

import java.util.List;

public interface AlertFormContract {

    interface View {
        void showCategories(List<Category> categories);
        void showCategoryError(boolean show);
        void showUrgencyError(boolean show);
        void showRadiusError(boolean show);
        void showLoading(boolean loading);
        void showError(String message);
        void close();
    }

    interface Presenter {
        void loadCategories();
        void save(Long categoryId, String urgency, String radius);
        void detachView();
    }
}
