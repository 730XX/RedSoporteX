package com.example.redsoportex.domain.repository;

import com.example.redsoportex.data.remote.dto.auth.LoginResponse;

public interface AuthRepository {

    interface Callback<T> {
        void onSuccess(T result);
        void onError(String message);
    }

    void login(String email, String password, Callback<LoginResponse> callback);

    void register(String fullName, String email, String password, String role,
                  String phone, String address, Double lat, Double lng, Callback<String> callback);
}
