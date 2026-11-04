package com.example.redsoportex.feature.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import com.example.redsoportex.R;
import com.example.redsoportex.core.session.SessionManager;
import com.example.redsoportex.core.ui.BaseFragment;
import com.example.redsoportex.core.ui.BottomNavHelper;
import com.example.redsoportex.databinding.FragmentProfileBinding;
import java.util.Locale;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding> implements ProfileContract.View {

    private ProfileContract.Presenter presenter;

    @NonNull
    @Override
    protected FragmentProfileBinding createBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentProfileBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        getBinding().toolbar.tvTitulo.setText(R.string.profile);
        getBinding().toolbar.btnVolver.setOnClickListener(x -> NavHostFragment.findNavController(this).navigateUp());

        SessionManager sessionManager = SessionManager.getInstance(requireContext());
        String fullName = sessionManager.getFullName().trim();
        String email = sessionManager.getEmail();
        String phone = sessionManager.getPhone().trim();
        String address = sessionManager.getApproxAddress().trim();

        if (fullName.isEmpty() && email != null && !email.isEmpty()) {
            int atIdx = email.indexOf('@');
            fullName = atIdx > 0 ? email.substring(0, atIdx) : email;
        }
        if (fullName.isEmpty()) {
            fullName = "Usuario";
        }

        getBinding().tvName.setText(fullName);
        getBinding().tvAvatar.setText(computeInitials(fullName));

        String roleLabel = sessionManager.isDonor() ? "Donante"
                : sessionManager.isAdmin() ? "Administrador"
                : "Solicitante";
        getBinding().tvRole.setText(roleLabel + " · Reputación " + String.format(Locale.US, "%.1f", sessionManager.getReputationScore()));

        getBinding().tvEmail.setText("Correo electrónico\n" + (email != null && !email.isEmpty() ? email : "No registrado"));
        getBinding().tvPhone.setText("Teléfono\n" + (!phone.isEmpty() ? phone : "No registrado"));
        getBinding().tvAddress.setText("Ubicación aproximada\n" + (!address.isEmpty() ? address : "Lima, Perú"));
        getBinding().tvDemo.setVisibility(View.GONE);

        BottomNavHelper.setup(getBinding().bottomNav.bottomNavigation, NavHostFragment.findNavController(this), R.id.nav_profile);
        presenter = new ProfilePresenter(this);

        getBinding().btnReports.setOnClickListener(x -> presenter.onReports());
        getBinding().btnLogout.setOnClickListener(x -> presenter.onExit());
    }

    private String computeInitials(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "U";
        }
        String[] parts = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length && sb.length() < 2; i++) {
            if (!parts[i].isEmpty()) {
                sb.append(Character.toUpperCase(parts[i].charAt(0)));
            }
        }
        return sb.length() > 0 ? sb.toString() : "U";
    }

    @Override
    public void openReports() {
        NavHostFragment.findNavController(this).navigate(R.id.reportsFragment);
    }

    @Override
    public void exitDemo() {
        SessionManager.getInstance(requireContext()).clearSession();
        NavOptions options = new NavOptions.Builder()
                .setPopUpTo(R.id.welcomeFragment, true)
                .setLaunchSingleTop(true)
                .build();
        NavHostFragment.findNavController(this).navigate(R.id.welcomeFragment, null, options);
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
        super.onDestroyView();
    }
}
