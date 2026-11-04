package com.example.redsoportex.feature.auth.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;
import com.example.redsoportex.R;
import com.example.redsoportex.core.session.SessionManager;
import com.example.redsoportex.core.ui.BaseFragment;
import com.example.redsoportex.data.repository.AuthRepositoryImpl;
import com.example.redsoportex.databinding.FragmentRegisterBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends BaseFragment<FragmentRegisterBinding> implements RegisterContract.View {

    private RegisterContract.Presenter presenter;

    @NonNull
    @Override
    protected FragmentRegisterBinding createBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentRegisterBinding.inflate(inflater, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle state) {
        super.onViewCreated(view, state);
        SessionManager sessionManager = SessionManager.getInstance(requireContext());
        presenter = new RegisterPresenter(this, new AuthRepositoryImpl(sessionManager));

        String[] roles = {getString(R.string.role_requester), getString(R.string.role_donor)};
        getBinding().etRol.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, roles));

        getBinding().btnVolver.setOnClickListener(v -> NavHostFragment.findNavController(this).navigateUp());
        getBinding().btnIrLogin.setOnClickListener(v -> presenter.onLogin());
        getBinding().btnRegistrar.setOnClickListener(v -> presenter.onSubmit(
                getBinding().etNombre.getText() != null ? getBinding().etNombre.getText().toString() : "",
                getBinding().etCorreo.getText() != null ? getBinding().etCorreo.getText().toString() : "",
                getBinding().etContrasena.getText() != null ? getBinding().etContrasena.getText().toString() : "",
                getBinding().etTelefono.getText() != null ? getBinding().etTelefono.getText().toString() : "",
                getBinding().etRol.getText() != null ? getBinding().etRol.getText().toString() : "",
                getBinding().etDireccion.getText() != null ? getBinding().etDireccion.getText().toString() : ""
        ));
    }

    @Override
    public void showError(RegisterContract.Field field, boolean show) {
        TextInputLayout layout;
        int message;
        switch (field) {
            case NAME:
                layout = getBinding().tilNombre;
                message = R.string.error_required;
                break;
            case EMAIL:
                layout = getBinding().tilCorreo;
                message = R.string.error_email;
                break;
            case PASSWORD:
                layout = getBinding().tilContrasena;
                message = R.string.error_password;
                break;
            case PHONE:
                layout = getBinding().tilTelefono;
                message = R.string.error_required;
                break;
            case ROLE:
                layout = getBinding().tilRol;
                message = R.string.error_role;
                break;
            case ADDRESS:
                layout = getBinding().tilDireccion;
                message = R.string.error_required;
                break;
            default:
                return;
        }
        layout.setError(show ? getString(message) : null);
    }

    @Override
    public void showLoading(boolean show) {
        getBinding().btnRegistrar.setEnabled(!show);
        getBinding().btnRegistrar.setText(show ? "Registrando..." : getString(R.string.register_title));
    }

    @Override
    public void showRegisterSuccess(String message) {
        Snackbar.make(getBinding().getRoot(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showRegisterError(String message) {
        Snackbar.make(getBinding().getRoot(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void openLogin() {
        if (!NavHostFragment.findNavController(this).popBackStack(R.id.loginFragment, false)) {
            NavHostFragment.findNavController(this).navigate(R.id.action_register_to_login);
        }
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
