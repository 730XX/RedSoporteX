package com.example.redsoportex;

import android.app.Application;
import com.example.redsoportex.core.network.ApiClient;
import com.example.redsoportex.core.session.SessionManager;

/**
 * Clase principal de Application de RedSoporteX.
 * Inicializa los singletons centrales de sesión y cliente HTTP.
 */
public class RedSoporteApp extends Application {

    private static RedSoporteApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // Inicializar persistencia de sesión
        SessionManager.getInstance(this);

        // Inicializar Retrofit con interceptor de autenticación
        ApiClient.init(this);
    }

    public static RedSoporteApp getInstance() {
        return instance;
    }
}
