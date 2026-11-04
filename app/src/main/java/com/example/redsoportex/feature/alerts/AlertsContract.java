package com.example.redsoportex.feature.alerts;

import com.example.redsoportex.demo.DemoStore;

import java.util.List;

public interface AlertsContract {
    interface View {
        void showAlerts(List<DemoStore.Alert> alerts);
    }

    interface Presenter {
        void load();

        void cancel(String id);

        void detachView();
    }
}
