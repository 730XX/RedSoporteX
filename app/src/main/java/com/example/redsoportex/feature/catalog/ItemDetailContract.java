package com.example.redsoportex.feature.catalog;

import com.example.redsoportex.domain.model.Item;

public interface ItemDetailContract {

    interface View {
        void showLoading(boolean loading);

        void showItem(Item item);

        void showError(String message);

        void close();
    }

    interface Presenter {
        void load(Long id);

        void detachView();
    }
}
