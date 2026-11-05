package com.example.redsoportex.core.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.json.JSONObject;
import retrofit2.Response;
import java.util.Iterator;

/**
 * Utilidad centralizada para extraer mensajes de error legibles del backend Spring Boot.
 * Extrae el campo "message" y posibles detalles de validación de campos en "data"
 * desde el errorBody() de respuestas HTTP fallidas (400, 401, 403, 404, 409, 500, etc.).
 */
public final class ErrorParser {

    private ErrorParser() {}

    @NonNull
    public static String parseError(@Nullable Response<?> response, @NonNull String fallbackMessage) {
        if (response == null) {
            return fallbackMessage;
        }

        // Si la respuesta vino en el body exitoso con success: false
        if (response.body() instanceof ApiResponse) {
            ApiResponse<?> apiResp = (ApiResponse<?>) response.body();
            if (apiResp.getMessage() != null && !apiResp.getMessage().trim().isEmpty()) {
                return apiResp.getMessage().trim();
            }
        }

        // Si vino en errorBody() (lo habitual en códigos HTTP 4xx y 5xx)
        if (response.errorBody() != null) {
            try {
                String errorJson = response.errorBody().string();
                if (errorJson != null && !errorJson.trim().isEmpty()) {
                    JSONObject json = new JSONObject(errorJson);

                    // 1. Mensaje principal del backend: {"message": "El correo ya está registrado...", ...}
                    String serverMessage = null;
                    if (json.has("message") && !json.isNull("message")) {
                        serverMessage = json.optString("message", "").trim();
                    }

                    // 2. Extraer posibles errores de validación de campos en "data"
                    StringBuilder validationDetails = new StringBuilder();
                    if (json.has("data") && !json.isNull("data")) {
                        Object dataObj = json.get("data");
                        if (dataObj instanceof JSONObject) {
                            JSONObject dataJson = (JSONObject) dataObj;
                            Iterator<String> keys = dataJson.keys();
                            while (keys.hasNext()) {
                                String field = keys.next();
                                String errorDesc = dataJson.optString(field, "");
                                if (!errorDesc.isEmpty()) {
                                    validationDetails.append("\n• ").append(errorDesc);
                                }
                            }
                        }
                    }

                    if (serverMessage != null && !serverMessage.isEmpty()) {
                        if (validationDetails.length() > 0) {
                            return serverMessage + ":" + validationDetails.toString();
                        }
                        return serverMessage;
                    } else if (validationDetails.length() > 0) {
                        return "Errores de validación:" + validationDetails.toString();
                    }
                }
            } catch (Exception ignored) {
                // Si el body no es JSON válido, continuar al fallback por código
            }
        }

        // Mensajes semánticos según el código de estado HTTP
        switch (response.code()) {
            case 400:
                return "Los datos enviados son incorrectos o incompletos.";
            case 401:
                return "Credenciales incorrectas o tu sesión ha expirado.";
            case 403:
                return "No tienes permisos para realizar esta acción.";
            case 404:
                return "El recurso o servicio solicitado no fue encontrado.";
            case 409:
                return "Conflicto: El recurso ya está reservado o en uso.";
            case 500:
                return "Ocurrió un error en el servidor. Por favor, intenta nuevamente más tarde.";
            case 503:
                return "El servicio no está disponible en este momento. Intenta de nuevo en unos minutos.";
            default:
                return fallbackMessage + " (HTTP " + response.code() + ")";
        }
    }
}
