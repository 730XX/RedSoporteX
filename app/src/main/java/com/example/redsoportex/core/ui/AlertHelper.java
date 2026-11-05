package com.example.redsoportex.core.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.example.redsoportex.R;
import com.example.redsoportex.databinding.DialogModalAlertBinding;

/**
 * Gestor centralizado de alertas modales elegantes tipo SweetAlertDialog.
 * Soporta diálogos modales para: Error, Éxito, Advertencia, Información y Confirmación.
 */
public final class AlertHelper {

    private AlertHelper() {}

    public enum Type {
        ERROR,
        SUCCESS,
        WARNING,
        INFO
    }

    /**
     * Muestra una alerta modal de error con título por defecto.
     */
    public static void showError(@NonNull Context context, @NonNull String message) {
        show(context, Type.ERROR, "¡Atención!", message, "Entendido", null, null, null);
    }

    /**
     * Muestra una alerta modal de error con título personalizado.
     */
    public static void showError(@NonNull Context context, @NonNull String title, @NonNull String message) {
        show(context, Type.ERROR, title, message, "Entendido", null, null, null);
    }

    /**
     * Muestra una alerta modal de éxito con acción opcional al confirmar.
     */
    public static void showSuccess(@NonNull Context context, @NonNull String title, @NonNull String message, @Nullable Runnable onConfirm) {
        show(context, Type.SUCCESS, title, message, "Aceptar", onConfirm, null, null);
    }

    /**
     * Muestra una alerta modal de advertencia con acción opcional.
     */
    public static void showWarning(@NonNull Context context, @NonNull String title, @NonNull String message, @Nullable Runnable onConfirm) {
        show(context, Type.WARNING, title, message, "Entendido", onConfirm, null, null);
    }

    /**
     * Muestra una alerta modal de confirmación con dos botones.
     */
    public static void showConfirm(@NonNull Context context,
                                   @NonNull String title,
                                   @NonNull String message,
                                   @NonNull String confirmText,
                                   @Nullable Runnable onConfirm,
                                   @NonNull String cancelText,
                                   @Nullable Runnable onCancel) {
        show(context, Type.WARNING, title, message, confirmText, onConfirm, cancelText, onCancel);
    }

    /**
     * Construcción y despliegue del diálogo modal personalizado.
     */
    public static void show(@NonNull Context context,
                            @NonNull Type type,
                            @NonNull String title,
                            @NonNull String message,
                            @NonNull String confirmText,
                            @Nullable Runnable onConfirm,
                            @Nullable String cancelText,
                            @Nullable Runnable onCancel) {

        DialogModalAlertBinding binding = DialogModalAlertBinding.inflate(LayoutInflater.from(context));

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(binding.getRoot())
                .setCancelable(false)
                .create();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Configuración de icono y color según el tipo
        switch (type) {
            case ERROR:
                binding.flBadgeContainer.setBackgroundResource(R.drawable.bg_modal_badge_error);
                binding.imgAlertIcon.setImageResource(R.drawable.ic_modal_error);
                break;
            case SUCCESS:
                binding.flBadgeContainer.setBackgroundResource(R.drawable.bg_modal_badge_success);
                binding.imgAlertIcon.setImageResource(R.drawable.ic_modal_success);
                break;
            case WARNING:
                binding.flBadgeContainer.setBackgroundResource(R.drawable.bg_modal_badge_warning);
                binding.imgAlertIcon.setImageResource(R.drawable.ic_modal_warning);
                break;
            case INFO:
                binding.flBadgeContainer.setBackgroundResource(R.drawable.bg_modal_badge_info);
                binding.imgAlertIcon.setImageResource(R.drawable.ic_modal_info);
                break;
        }

        binding.tvAlertTitle.setText(title);
        binding.tvAlertMessage.setText(message);
        binding.btnAlertConfirm.setText(confirmText);

        binding.btnAlertConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            if (onConfirm != null) {
                onConfirm.run();
            }
        });

        if (cancelText != null && !cancelText.isEmpty()) {
            binding.btnAlertCancel.setVisibility(View.VISIBLE);
            binding.btnAlertCancel.setText(cancelText);
            binding.btnAlertCancel.setOnClickListener(v -> {
                dialog.dismiss();
                if (onCancel != null) {
                    onCancel.run();
                }
            });
        } else {
            binding.btnAlertCancel.setVisibility(View.GONE);
        }

        dialog.show();
    }
}
