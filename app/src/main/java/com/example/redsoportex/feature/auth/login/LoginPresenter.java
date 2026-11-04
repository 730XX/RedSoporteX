package com.example.redsoportex.feature.auth.login;

import android.util.Patterns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.redsoportex.data.remote.dto.auth.LoginResponse;
import com.example.redsoportex.domain.repository.AuthRepository;

public final class LoginPresenter implements LoginContract.Presenter {

    @Nullable
    private LoginContract.View view;
    private final AuthRepository authRepository;

    public LoginPresenter(@NonNull LoginContract.View view, @NonNull AuthRepository authRepository) {
        this.view = view;
        this.authRepository = authRepository;
    }

    @Override
    public void onSubmit(String email, String password) {
        if (view == null) return;

        String trimmedEmail = email != null ? email.trim() : "";
        String trimmedPassword = password != null ? password.trim() : "";

        boolean invalidEmail = !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches();
        boolean invalidPassword = trimmedPassword.isEmpty();

        view.showEmailError(invalidEmail);
        view.showPasswordError(invalidPassword);

        if (!invalidEmail && !invalidPassword) {
            view.showLoading(true);
            authRepository.login(trimmedEmail, trimmedPassword, new AuthRepository.Callback<LoginResponse>() {
                @Override
                public void onSuccess(LoginResponse result) {
                    if (view != null) {
                        view.showLoading(false);
                        view.navigateToHome();
                    }
                }

                @Override
                public void onError(String message) {
                    if (view != null) {
                        view.showLoading(false);
                        view.showLoginError(message);
                    }
                }
            });
        }
    }

    @Override
    public void onRegister() {
        if (view != null) {
            view.openRegister();
        }
    }

    @Override
    public void detachView() {
        view = null;
    }
}
