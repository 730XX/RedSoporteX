package com.example.redsoportex.feature.incidents;

import com.example.redsoportex.demo.DemoStore;

import java.util.List;

public interface ReportsContract {
    interface View {
        void showReports(List<DemoStore.Incident> reports);
    }

    interface Presenter {
        void load();

        void detachView();
    }
}
