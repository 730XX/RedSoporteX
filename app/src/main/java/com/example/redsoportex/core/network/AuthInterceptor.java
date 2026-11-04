package com.example.redsoportex.core.network;

import androidx.annotation.NonNull;
import com.example.redsoportex.core.session.SessionManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Interceptor de OkHttp que adjunta el JWT del usuario en la cabecera Authorization
 * si existe una sesión activa y la petición no cuenta ya con dicha cabecera.
 */
public final class AuthInterceptor implements Interceptor {

    private final SessionManager sessionManager;

    public AuthInterceptor(@NonNull SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();

        // Si ya trae Authorization o no hay sesión activa, continuar sin alterar
        if (original.header("Authorization") != null || !sessionManager.isLoggedIn()) {
            return chain.proceed(original);
        }

        String authHeader = sessionManager.getAuthHeader();
        if (authHeader == null) {
            return chain.proceed(original);
        }

        Request authenticatedRequest = original.newBuilder()
                .header("Authorization", authHeader)
                .build();

        return chain.proceed(authenticatedRequest);
    }
}
