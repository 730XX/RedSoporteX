package com.example.redsoportex.feature.catalog;

import com.example.redsoportex.domain.model.Category;
import com.example.redsoportex.domain.model.Item;

import java.util.List;

public interface CatalogContract {

    interface View {
        void showLoading(boolean loading);

        void showItems(List<Item> items);

        void showCategories(List<Category> categories);

        void showError(String message);
    }

    interface Presenter {
        void loadData();

        void filter(String query, Long categoryId);

        void detachView();
    }
}
