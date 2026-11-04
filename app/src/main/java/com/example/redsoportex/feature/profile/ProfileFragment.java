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
import com.example.redsoportex.demo.DemoStore;

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
        if (sessionManager.isLoggedIn()) {
            if (!sessionManager.getFullName().isEmpty()) {
                getBinding().tvName.setText(sessionManager.getFullName());
            }
            if (sessionManager.getEmail() != null) {
                getBinding().tvEmail.setText(sessionManager.getEmail());
            }
            if (sessionManager.isDonor()) {
                getBinding().tvRole.setText(R.string.donor_role);
            } else if (sessionManager.isSolicitant()) {
                getBinding().tvRole.setText(R.string.role_requester);
            }
        } else if (DemoStore.get().isDonorMode()) {
            getBinding().tvRole.setText(R.string.donor_role);
        }

        BottomNavHelper.setup(getBinding().bottomNav.bottomNavigation, NavHostFragment.findNavController(this), R.id.nav_profile);
        presenter = new ProfilePresenter(this);

        getBinding().btnReports.setOnClickListener(x -> presenter.onReports());
        getBinding().btnLogout.setOnClickListener(x -> presenter.onExit());
    }

    @Override
    public void openReports() {
        NavHostFragment.findNavController(this).navigate(R.id.reportsFragment);
    }

    @Override
    public void exitDemo() {
        SessionManager.getInstance(requireContext()).clearSession();
        DemoStore.get().setDonorMode(false);
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
