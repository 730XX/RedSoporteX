package com.example.redsoportex.core.session;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Gestor de sesión y persistencia segura del JWT y roles del usuario autenticado.
 * Utiliza SharedPreferences para almacenar credenciales y estado del usuario.
 */
public final class SessionManager {

    private static final String PREF_NAME = "redsoporte_session_prefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_TOKEN_TYPE = "token_type";
    private static final String KEY_ROLE = "user_role";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_FULL_NAME = "user_full_name";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_PHONE = "user_phone";
    private static final String KEY_ADDRESS = "user_address";
    private static final String KEY_REPUTATION = "user_reputation";

    // Constantes de roles según el backend Spring Boot
    public static final String ROLE_DONANTE = "ROLE_DONANTE";
    public static final String ROLE_SOLICITANTE = "ROLE_SOLICITANTE";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    private static volatile SessionManager instance;
    private final SharedPreferences prefs;

    private SessionManager(@NonNull Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SessionManager getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    /**
     * Guarda la sesión tras un login o registro exitoso.
     */
    public void saveSession(@NonNull String token,
                            @Nullable String tokenType,
                            @NonNull String role,
                            @NonNull String email,
                            @Nullable String fullName,
                            @Nullable Long userId) {
        saveSession(token, tokenType, role, email, fullName, userId, null, null, null);
    }

    public void saveSession(@NonNull String token,
                            @Nullable String tokenType,
                            @NonNull String role,
                            @NonNull String email,
                            @Nullable String fullName,
                            @Nullable Long userId,
                            @Nullable String phone,
                            @Nullable String approxAddress,
                            @Nullable Double reputationScore) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_TOKEN_TYPE, (tokenType != null && !tokenType.isEmpty()) ? tokenType : "Bearer")
                .putString(KEY_ROLE, role)
                .putString(KEY_EMAIL, email)
                .putString(KEY_FULL_NAME, fullName != null ? fullName : "")
                .putLong(KEY_USER_ID, userId != null ? userId : -1L)
                .putString(KEY_PHONE, phone != null ? phone : "")
                .putString(KEY_ADDRESS, approxAddress != null ? approxAddress : "")
                .putFloat(KEY_REPUTATION, reputationScore != null ? reputationScore.floatValue() : 5.0f)
                .apply();
    }

    @Nullable
    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    @NonNull
    public String getTokenType() {
        return prefs.getString(KEY_TOKEN_TYPE, "Bearer");
    }

    @Nullable
    public String getRole() {
        return prefs.getString(KEY_ROLE, null);
    }

    @Nullable
    public String getEmail() {
        return prefs.getString(KEY_EMAIL, null);
    }

    @NonNull
    public String getFullName() {
        return prefs.getString(KEY_FULL_NAME, "");
    }

    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, -1L);
    }

    @NonNull
    public String getPhone() {
        return prefs.getString(KEY_PHONE, "");
    }

    @NonNull
    public String getApproxAddress() {
        return prefs.getString(KEY_ADDRESS, "");
    }

    public double getReputationScore() {
        return prefs.getFloat(KEY_REPUTATION, 5.0f);
    }

    /**
     * Retorna el header de autorización formateado: "Bearer <token>" o null si no hay sesión.
     */
    @Nullable
    public String getAuthHeader() {
        String token = getToken();
        if (token == null || token.isEmpty()) {
            return null;
        }
        return getTokenType() + " " + token;
    }

    public boolean isLoggedIn() {
        String token = getToken();
        return token != null && !token.trim().isEmpty();
    }

    public boolean isDonor() {
        String r = getRole();
        return r != null && (r.equalsIgnoreCase(ROLE_DONANTE) || r.equalsIgnoreCase("DONANTE") || r.toUpperCase().contains("DONANTE"));
    }

    public boolean isSolicitant() {
        String r = getRole();
        return r != null && (r.equalsIgnoreCase(ROLE_SOLICITANTE) || r.equalsIgnoreCase("SOLICITANTE") || r.toUpperCase().contains("SOLICITANTE"));
    }

    public boolean isAdmin() {
        String r = getRole();
        return r != null && (r.equalsIgnoreCase(ROLE_ADMIN) || r.equalsIgnoreCase("ADMIN") || r.toUpperCase().contains("ADMIN"));
    }

    /**
     * Cierra la sesión activa eliminando todos los datos almacenados.
     */
    public void clearSession() {
        prefs.edit().clear().apply();
    }
}
