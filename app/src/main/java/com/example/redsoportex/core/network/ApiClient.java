package com.example.redsoportex.core.network;

import android.content.Context;
import androidx.annotation.NonNull;
import com.example.redsoportex.core.session.SessionManager;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Cliente centralizado de Retrofit para interactuar con la API en Railway.
 */
public final class ApiClient {

    public static final String BASE_URL = "https://redsoportebackend-production.up.railway.app/";

    private static volatile Retrofit retrofit;

    private ApiClient() {}

    public static synchronized void init(@NonNull Context context) {
        if (retrofit == null) {
            SessionManager sessionManager = SessionManager.getInstance(context);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(sessionManager))
                    .addInterceptor(logging)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    @NonNull
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            throw new IllegalStateException("ApiClient.init(context) debe ser llamado antes de obtener Retrofit");
        }
        return retrofit;
    }

    @NonNull
    public static <T> T createService(@NonNull Class<T> serviceClass) {
        return getRetrofit().create(serviceClass);
    }
}
