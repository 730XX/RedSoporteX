package com.example.redsoportex.feature.donor;

import com.example.redsoportex.domain.model.Category;
import com.example.redsoportex.domain.model.Item;

import java.util.List;

public interface ItemFormContract {
    enum Field {CATEGORY, TITLE, DESCRIPTION, CONDITION, LOCATION}

    interface View {
        void showItem(Item item);

        void showCategories(List<Category> categories);

        void showLoading(boolean loading);

        void showError(Field field, int message);

        void showGeneralError(String message);

        void close();

        void saved(boolean editing);
    }

    interface Presenter {
        void loadInitialData();

        void load(Long id);

        void save(Long categoryId, String title, String description, String condition, String image, String location);

        void detachView();
    }
}
