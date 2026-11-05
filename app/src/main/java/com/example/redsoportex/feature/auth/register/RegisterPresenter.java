package com.example.redsoportex.feature.auth.register;

import android.util.Patterns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.redsoportex.core.session.SessionManager;
import com.example.redsoportex.domain.repository.AuthRepository;

public final class RegisterPresenter implements RegisterContract.Presenter {

    @Nullable
    private RegisterContract.View view;
    private final AuthRepository authRepository;

    public RegisterPresenter(@NonNull RegisterContract.View view, @NonNull AuthRepository authRepository) {
        this.view = view;
        this.authRepository = authRepository;
    }

    @Override
    public void onSubmit(String name, String email, String password, String phone, String role, String address, boolean acceptedPrivacy) {
        if (view == null) return;

        String trimmedName = name != null ? name.trim() : "";
        String trimmedEmail = email != null ? email.trim() : "";
        String trimmedPassword = password != null ? password.trim() : "";
        String trimmedPhone = phone != null ? phone.trim() : "";
        String trimmedRole = role != null ? role.trim() : "";
        String trimmedAddress = address != null ? address.trim() : "";

        boolean invalidName = trimmedName.isEmpty();
        boolean invalidEmail = !Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches();
        boolean invalidPassword = trimmedPassword.length() < 6;
        boolean invalidPhone = trimmedPhone.isEmpty();
        boolean invalidRole = trimmedRole.isEmpty();
        boolean invalidAddress = trimmedAddress.isEmpty();
        boolean invalidPrivacy = !acceptedPrivacy;

        view.showError(RegisterContract.Field.NAME, invalidName);
        view.showError(RegisterContract.Field.EMAIL, invalidEmail);
        view.showError(RegisterContract.Field.PASSWORD, invalidPassword);
        view.showError(RegisterContract.Field.PHONE, invalidPhone);
        view.showError(RegisterContract.Field.ROLE, invalidRole);
        view.showError(RegisterContract.Field.ADDRESS, invalidAddress);
        view.showError(RegisterContract.Field.PRIVACY, invalidPrivacy);

        if (!invalidName && !invalidEmail && !invalidPassword && !invalidPhone && !invalidRole && !invalidAddress && !invalidPrivacy) {
            String backendRole = trimmedRole.toLowerCase().contains("donante")
                    ? SessionManager.ROLE_DONANTE
                    : SessionManager.ROLE_SOLICITANTE;

            // Coordenadas aproximadas predeterminadas (Lima, Perú)
            double defaultLat = -12.1219;
            double defaultLng = -77.0298;

            view.showLoading(true);
            authRepository.register(
                    trimmedName,
                    trimmedEmail,
                    trimmedPassword,
                    backendRole,
                    trimmedPhone,
                    trimmedAddress,
                    defaultLat,
                    defaultLng,
                    new AuthRepository.Callback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if (view != null) {
                                view.showLoading(false);
                                view.showRegisterSuccess(result != null ? result : "¡Cuenta registrada exitosamente!");
                                view.openLogin();
                            }
                        }

                        @Override
                        public void onError(String message) {
                            if (view != null) {
                                view.showLoading(false);
                                view.showRegisterError(message);
                            }
                        }
                    }
            );
        }
    }

    @Override
    public void onPrivacyPolicyClicked() {
        if (view != null) {
            view.showPrivacyPolicyDialog();
        }
    }

    @Override
    public void onLogin() {
        if (view != null) {
            view.openLogin();
        }
    }

    @Override
    public void detachView() {
        view = null;
    }
}
