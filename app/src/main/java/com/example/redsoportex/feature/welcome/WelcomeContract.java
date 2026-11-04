package com.example.redsoportex.feature.welcome;
public interface WelcomeContract {
    interface View { void openLogin(); void openRegister(); void showComingSoon(); void openCatalog(); }
    interface Presenter { void onLoginSelected(); void onRegisterSelected(); void onExploreSelected(); void detachView(); }
}
