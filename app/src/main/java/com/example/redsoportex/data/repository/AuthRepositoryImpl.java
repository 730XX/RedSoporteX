package com.example.redsoportex.data.repository;

import androidx.annotation.NonNull;
import com.example.redsoportex.core.network.ApiClient;
import com.example.redsoportex.core.network.ApiResponse;
import com.example.redsoportex.core.session.SessionManager;
import com.example.redsoportex.data.remote.api.AuthApi;
import com.example.redsoportex.data.remote.dto.auth.LoginRequest;
import com.example.redsoportex.data.remote.dto.auth.LoginResponse;
import com.example.redsoportex.data.remote.dto.auth.RegisterRequest;
import com.example.redsoportex.domain.repository.AuthRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepositoryImpl implements AuthRepository {

    private final AuthApi authApi;
    private final SessionManager sessionManager;

    public AuthRepositoryImpl(@NonNull SessionManager sessionManager) {
        this.authApi = ApiClient.createService(AuthApi.class);
        this.sessionManager = sessionManager;
    }

    @Override
    public void login(String email, String password, Callback<LoginResponse> callback) {
        authApi.login(new LoginRequest(email, password)).enqueue(new retrofit2.Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<LoginResponse>> call,
                                   @NonNull Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    LoginResponse loginData = response.body().getData();
                    if (loginData != null) {
                        sessionManager.saveSession(
                                loginData.getToken(),
                                loginData.getTokenType(),
                                loginData.getRole() != null ? loginData.getRole() : SessionManager.ROLE_SOLICITANTE,
                                loginData.getEmail() != null ? loginData.getEmail() : email,
                                loginData.getFullName(),
                                loginData.getUserId()
                        );
                        callback.onSuccess(loginData);
                        return;
                    }
                }
                String msg = (response.body() != null && response.body().getMessage() != null)
                        ? response.body().getMessage()
                        : "Error de autenticación (" + response.code() + ")";
                callback.onError(msg);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<LoginResponse>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }

    @Override
    public void register(String fullName, String email, String password, String role,
                         String phone, String address, Double lat, Double lng, Callback<String> callback) {
        RegisterRequest req = new RegisterRequest(fullName, email, password, role, phone, address, lat, lng);
        authApi.register(req).enqueue(new retrofit2.Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<Object>> call,
                                   @NonNull Response<ApiResponse<Object>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onSuccess(response.body().getMessage() != null ? response.body().getMessage() : "Registro exitoso");
                } else {
                    String msg = (response.body() != null && response.body().getMessage() != null)
                            ? response.body().getMessage()
                            : "Error al registrarse (" + response.code() + ")";
                    callback.onError(msg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<Object>> call, @NonNull Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Error de red al conectar con el servidor");
            }
        });
    }
}
