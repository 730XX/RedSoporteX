package com.example.redsoportex.feature.auth.login;

public interface LoginContract {
    interface View {
        void showEmailError(boolean show);

        void showPasswordError(boolean show);

        void showLoading(boolean show);

        void showLoginError(String message);

        void navigateToHome();

        void openRegister();
    }

    interface Presenter {
        void onSubmit(String email, String password);

        void onRegister();

        void detachView();
    }
}
