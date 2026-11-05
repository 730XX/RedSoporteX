package com.example.redsoportex.feature.auth.register;

public interface RegisterContract {
    enum Field {NAME, EMAIL, PASSWORD, PHONE, ROLE, ADDRESS, PRIVACY}

    interface View {
        void showError(Field field, boolean show);

        void showLoading(boolean show);

        void showRegisterSuccess(String message);

        void showRegisterError(String message);

        void showPrivacyPolicyDialog();

        void openLogin();
    }

    interface Presenter {
        void onSubmit(String name, String email, String password, String phone, String role, String address, boolean acceptedPrivacy);

        void onPrivacyPolicyClicked();

        void onLogin();

        void detachView();
    }
}
