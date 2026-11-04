package com.example.redsoportex.feature.welcome;
import androidx.annotation.Nullable;
public final class WelcomePresenter implements WelcomeContract.Presenter {
    @Nullable private WelcomeContract.View view;
    public WelcomePresenter(WelcomeContract.View view) { this.view = view; }
    @Override public void onLoginSelected() { if (view != null) view.openLogin(); }
    @Override public void onRegisterSelected() { if (view != null) view.openRegister(); }
    @Override public void onExploreSelected() { if (view != null) view.openCatalog(); }
    @Override public void detachView() { view = null; }
}
