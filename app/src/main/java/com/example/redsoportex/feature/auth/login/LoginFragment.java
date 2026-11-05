package com.example.redsoportex.feature.auth.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.redsoportex.R;
import com.example.redsoportex.core.session.SessionManager;
import com.example.redsoportex.core.ui.AlertHelper;
import com.example.redsoportex.core.ui.BaseFragment;
import com.example.redsoportex.data.repository.AuthRepositoryImpl;
import com.example.redsoportex.databinding.FragmentLoginBinding;

public class LoginFragment extends BaseFragment<FragmentLoginBinding> implements LoginContract.View {

    private LoginContract.Presenter presenter;

    @NonNull
    @Override
    protected FragmentLoginBinding createBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentLoginBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        SessionManager sessionManager = SessionManager.getInstance(requireContext());
        presenter = new LoginPresenter(this, new AuthRepositoryImpl(sessionManager));

        getBinding().btnVolver.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        getBinding().btnIrRegistro.setOnClickListener(v -> presenter.onRegister());
        getBinding().btnIngresar.setOnClickListener(v -> presenter.onSubmit(
                getBinding().etCorreo.getText() != null ? getBinding().etCorreo.getText().toString() : "",
                getBinding().etContrasena.getText() != null ? getBinding().etContrasena.getText().toString() : ""
        ));
    }

    @Override
    public void showEmailError(boolean show) {
        getBinding().tilCorreo.setError(show ? getString(R.string.error_email) : null);
    }

    @Override
    public void showPasswordError(boolean show) {
        getBinding().tilContrasena.setError(show ? getString(R.string.error_required) : null);
    }

    @Override
    public void showLoading(boolean show) {
        getBinding().btnIngresar.setEnabled(!show);
        getBinding().btnIngresar.setText(show ? "Ingresando..." : getString(R.string.login_submit));
    }

    @Override
    public void showLoginError(String message) {
        AlertHelper.showError(requireContext(), "Error de autenticación", message);
    }

    @Override
    public void navigateToHome() {
        NavHostFragment.findNavController(this).navigate(R.id.action_login_to_catalog);
    }

    @Override
    public void openRegister() {
        NavHostFragment.findNavController(this).navigate(R.id.action_login_to_register);
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
