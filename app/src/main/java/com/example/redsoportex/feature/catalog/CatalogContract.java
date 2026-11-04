package com.example.redsoportex.feature.catalog;

import com.example.redsoportex.demo.DemoStore;

import java.util.List;

public interface CatalogContract {
    interface View {
        void showItems(List<DemoStore.Item> items);
    }

    interface Presenter {
        void filter(String query, String category);

        void detachView();
    }
}
