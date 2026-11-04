package com.example.redsoportex.data.repository;

import android.util.Base64;
import androidx.annotation.NonNull;
import com.example.redsoportex.core.network.ApiClient;
import com.example.redsoportex.core.network.ApiResponse;
import com.example.redsoportex.core.session.SessionManager;
import com.example.redsoportex.data.remote.api.AuthApi;
import com.example.redsoportex.data.remote.dto.auth.LoginRequest;
import com.example.redsoportex.data.remote.dto.auth.LoginResponse;
import com.example.redsoportex.data.remote.dto.auth.RegisterRequest;
import com.example.redsoportex.domain.repository.AuthRepository;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
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
                        String token = loginData.getToken();
                        String role = loginData.getRole();
                        String resolvedEmail = loginData.getEmail();
                        String fullName = loginData.getFullName();
                        Long userId = loginData.getUserId();

                        if (token != null && (role == null || fullName == null || userId == null)) {
                            try {
                                String[] parts = token.split("\\.");
                                if (parts.length >= 2) {
                                    byte[] decodedBytes = Base64.decode(parts[1], Base64.URL_SAFE | Base64.NO_WRAP | Base64.NO_PADDING);
                                    String payloadJson = new String(decodedBytes, StandardCharsets.UTF_8);
                                    JSONObject json = new JSONObject(payloadJson);
                                    if (role == null && json.has("role")) {
                                        role = json.optString("role", null);
                                    }
                                    if (fullName == null && json.has("fullName")) {
                                        fullName = json.optString("fullName", null);
                                    }
                                    if (userId == null && json.has("userId")) {
                                        userId = json.optLong("userId", -1L);
                                    }
                                    if (resolvedEmail == null && json.has("sub")) {
                                        resolvedEmail = json.optString("sub", null);
                                    }
                                }
                            } catch (Exception ignored) {
                            }
                        }

                        sessionManager.saveSession(
                                token != null ? token : "",
                                loginData.getTokenType(),
                                role != null ? role : SessionManager.ROLE_SOLICITANTE,
                                resolvedEmail != null ? resolvedEmail : email,
                                fullName,
                                userId,
                                loginData.getPhone(),
                                loginData.getApproxAddress(),
                                loginData.getReputationScore()
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
