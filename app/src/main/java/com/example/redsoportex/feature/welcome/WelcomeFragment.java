package com.example.redsoportex.feature.welcome;

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
import com.example.redsoportex.databinding.FragmentWelcomeBinding;
import com.example.redsoportex.demo.DemoStore;
import com.google.android.material.snackbar.Snackbar;

public class WelcomeFragment extends BaseFragment<FragmentWelcomeBinding>
        implements WelcomeContract.View {
    private WelcomeContract.Presenter presenter;

    @NonNull
    @Override
    protected FragmentWelcomeBinding createBinding(@NonNull LayoutInflater inflater,
                                                   @Nullable ViewGroup container) {
        return FragmentWelcomeBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Si ya existe sesión activa, redirigir al catálogo
        SessionManager sessionManager = SessionManager.getInstance(requireContext());
        if (sessionManager.isLoggedIn()) {
            NavOptions options = new NavOptions.Builder()
                    .setPopUpTo(R.id.welcomeFragment, true)
                    .build();
            NavHostFragment.findNavController(this).navigate(R.id.catalogFragment, null, options);
            return;
        }

        presenter = new WelcomePresenter(this);
        getBinding().btnIniciarSesion.setOnClickListener(v -> presenter.onLoginSelected());
        getBinding().btnCrearCuenta.setOnClickListener(v -> presenter.onRegisterSelected());
        getBinding().btnExplorarCatalogo.setOnClickListener(v -> { DemoStore.get().setDonorMode(false); presenter.onExploreSelected(); });
        getBinding().btnDemoDonante.setOnClickListener(v -> { DemoStore.get().setDonorMode(true); presenter.onExploreSelected(); });
    }

    @Override
    public void openLogin() { NavHostFragment.findNavController(this).navigate(R.id.action_welcome_to_login); }

    @Override
    public void openRegister() { NavHostFragment.findNavController(this).navigate(R.id.action_welcome_to_register); }

    @Override
    public void openCatalog() { NavHostFragment.findNavController(this).navigate(R.id.catalogFragment); }

    @Override
    public void showComingSoon() {
        Snackbar.make(getBinding().getRoot(), R.string.feature_coming_soon, Snackbar.LENGTH_SHORT).show();
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
