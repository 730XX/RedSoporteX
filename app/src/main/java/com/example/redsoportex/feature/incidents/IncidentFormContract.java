package com.example.redsoportex.feature.incidents;

import com.example.redsoportex.demo.DemoStore;

public interface IncidentFormContract {
    interface View {
        void showLoan(DemoStore.Item item);

        void showTypeError(boolean value);

        void showDescriptionError(int code);

        void openReports();

        void close();
    }

    interface Presenter {
        void load(String id);

        void submit(String type, String description);

        void detachView();
    }
}
