package com.example.redsoportex.feature.donor;

import com.example.redsoportex.demo.DemoStore;

public interface ItemFormContract {
    enum Field {CATEGORY, TITLE, DESCRIPTION, CONDITION, LOCATION}

    interface View {
        void showItem(DemoStore.Item item);

        void showError(Field field, int message);

        void close();

        void saved(boolean editing);
    }

    interface Presenter {
        void load(String id);

        void save(String category, String title, String description, String condition, String image, String location);

        void detachView();
    }
}
