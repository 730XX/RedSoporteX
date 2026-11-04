package com.example.redsoportex.feature.auth.register;

public interface RegisterContract {
    enum Field {NAME, EMAIL, PASSWORD, PHONE, ROLE, ADDRESS}

    interface View {
        void showError(Field field, boolean show);

        void showLoading(boolean show);

        void showRegisterSuccess(String message);

        void showRegisterError(String message);

        void openLogin();
    }

    interface Presenter {
        void onSubmit(String name, String email, String password, String phone, String role, String address);

        void onLogin();

        void detachView();
    }
}
