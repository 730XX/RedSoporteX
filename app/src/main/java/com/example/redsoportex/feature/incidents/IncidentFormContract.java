package com.example.redsoportex.feature.incidents;

public interface IncidentFormContract {

    interface View {
        void showLoan(String itemTitle);
        void showTypeError(boolean value);
        void showDescriptionError(int code);
        void showLoading(boolean loading);
        void showError(String message);
        void openReports();
        void close();
    }

    interface Presenter {
        void load(Object idArg);
        void submit(String type, String description);
        void detachView();
    }
}
