package com.example.redsoportex.feature.catalog;

import com.example.redsoportex.demo.DemoStore;

public interface ItemDetailContract {
    interface View {
        void showItem(DemoStore.Item item);

        void close();
    }

    interface Presenter {
        void load(String id);

        void detachView();
    }
}
