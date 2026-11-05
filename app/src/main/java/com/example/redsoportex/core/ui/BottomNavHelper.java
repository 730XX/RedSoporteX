package com.example.redsoportex.core.ui;

import android.content.Context;

import androidx.core.view.ViewCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import com.example.redsoportex.R;
import com.example.redsoportex.core.session.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public final class BottomNavHelper {
    private BottomNavHelper() {
    }

    public static void setup(BottomNavigationView bar, NavController nav, int selected) {
        Context context = bar.getContext();
        SessionManager sessionManager = SessionManager.getInstance(context);
        boolean donor = sessionManager.isDonor();
        boolean loggedIn = sessionManager.isLoggedIn();

        // Eliminar por código Java cualquier padding inferior o WindowInsets residual
        bar.setPadding(0, 0, 0, 0);
        ViewCompat.setOnApplyWindowInsetsListener(bar, (v, insets) -> {
            v.setPadding(0, 0, 0, 0);
            return insets;
        });

        if (donor) {
            bar.getMenu().findItem(R.id.nav_loans).setTitle(R.string.donor_items).setIcon(R.drawable.ic_donor_package);
            bar.getMenu().findItem(R.id.nav_alerts).setTitle(R.string.donor_requests).setIcon(R.drawable.ic_nav_loans);
        }

        bar.setSelectedItemId(selected);
        bar.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selected) return true;

            // Bloquear acceso a módulos protegidos para usuarios invitados
            if (!sessionManager.isLoggedIn() && (id == R.id.nav_loans || id == R.id.nav_alerts || id == R.id.nav_profile)) {
                String moduleName = id == R.id.nav_loans ? "tus préstamos"
                        : id == R.id.nav_alerts ? "las alertas de necesidad"
                        : "tu perfil de usuario";

                new MaterialAlertDialogBuilder(context)
                        .setTitle("Iniciar sesión requerido")
                        .setMessage("Para acceder a " + moduleName + ", debes iniciar sesión o registrarte con una cuenta en RedSoporte.")
                        .setPositiveButton("Iniciar sesión", (dialog, which) -> {
                            NavOptions options = new NavOptions.Builder().setLaunchSingleTop(true).build();
                            nav.navigate(R.id.loginFragment, null, options);
                        })
                        .setNeutralButton("Crear cuenta", (dialog, which) -> {
                            NavOptions options = new NavOptions.Builder().setLaunchSingleTop(true).build();
                            nav.navigate(R.id.registerFragment, null, options);
                        })
                        .setNegativeButton("Seguir explorando", null)
                        .show();

                return false; // No cambia la selección ni redirige a la pantalla vacía
            }

            int dest = id == R.id.nav_catalog ? R.id.catalogFragment
                    : id == R.id.nav_loans ? (donor ? R.id.donorItemsFragment : R.id.loansFragment)
                    : id == R.id.nav_alerts ? (donor ? R.id.incomingFragment : R.id.alertsFragment)
                    : R.id.profileFragment;

            NavOptions options = new NavOptions.Builder()
                    .setLaunchSingleTop(true)
                    .setPopUpTo(R.id.catalogFragment, false)
                    .build();
            nav.navigate(dest, null, options);
            return true;
        });
    }
}
