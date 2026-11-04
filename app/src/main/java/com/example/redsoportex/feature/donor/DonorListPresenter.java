package com.example.redsoportex.feature.donor;

public final class DonorListPresenter {
    public interface View {
        void render();
    }

    private View view;

    public DonorListPresenter(View v) {
        view = v;
    }

    public void load() {
        if (view != null) view.render();
    }

    public void detach() {
        view = null;
    }
}
