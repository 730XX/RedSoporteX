package com.example.redsoportex.feature.alerts;
public interface AlertFormContract { interface View { void showCategoryError(boolean show);void showUrgencyError(boolean show);void showRadiusError(boolean show);void close(); } interface Presenter { void save(String category,String urgency,String radius);void detachView(); } }
