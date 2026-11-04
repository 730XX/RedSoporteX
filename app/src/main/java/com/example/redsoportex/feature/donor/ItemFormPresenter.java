package com.example.redsoportex.feature.donor;

import androidx.annotation.NonNull;

import com.example.redsoportex.R;
import com.example.redsoportex.data.repository.ItemRepositoryImpl;
import com.example.redsoportex.domain.model.Category;
import com.example.redsoportex.domain.model.Item;
import com.example.redsoportex.domain.repository.ItemRepository;

import java.util.List;

public final class ItemFormPresenter implements ItemFormContract.Presenter {

    private ItemFormContract.View view;
    private final ItemRepository itemRepository;
    private Item currentItem;

    public ItemFormPresenter(@NonNull ItemFormContract.View view) {
        this(view, new ItemRepositoryImpl());
    }

    public ItemFormPresenter(@NonNull ItemFormContract.View view, @NonNull ItemRepository itemRepository) {
        this.view = view;
        this.itemRepository = itemRepository;
    }

    @Override
    public void loadInitialData() {
        if (view == null) return;
        view.showLoading(true);
        itemRepository.getCategories(new ItemRepository.Callback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> categories) {
                if (view != null) {
                    view.showLoading(false);
                    view.showCategories(categories);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showLoading(false);
                    view.showGeneralError(message);
                }
            }
        });
    }

    @Override
    public void load(Long id) {
        if (id == null || id <= 0) return;
        if (view == null) return;
        view.showLoading(true);
        itemRepository.getItemById(id, new ItemRepository.Callback<Item>() {
            @Override
            public void onSuccess(Item item) {
                if (view != null) {
                    view.showLoading(false);
                    currentItem = item;
                    view.showItem(item);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showLoading(false);
                    view.showGeneralError(message);
                }
            }
        });
    }

    @Override
    public void save(Long categoryId, String title, String description, String condition, String image, String location) {
        if (view == null) return;

        String t = title != null ? title.trim() : "";
        String d = description != null ? description.trim() : "";
        String k = condition != null ? condition.trim() : "";
        String l = location != null ? location.trim() : "";
        String img = image != null ? image.trim() : "";

        int catError = (categoryId == null || categoryId <= 0) ? R.string.required_selection : 0;
        int titleError = t.isEmpty() ? R.string.error_required : (t.length() > 120 ? R.string.donor_title_too_long : 0);
        int descError = d.isEmpty() ? R.string.error_required : 0;
        int condError = k.isEmpty() ? R.string.required_selection : 0;
        int locError = l.isEmpty() ? R.string.error_required : 0;

        view.showError(ItemFormContract.Field.CATEGORY, catError);
        view.showError(ItemFormContract.Field.TITLE, titleError);
        view.showError(ItemFormContract.Field.DESCRIPTION, descError);
        view.showError(ItemFormContract.Field.CONDITION, condError);
        view.showError(ItemFormContract.Field.LOCATION, locError);

        if (catError != 0 || titleError != 0 || descError != 0 || condError != 0 || locError != 0) {
            return;
        }

        // Map condition string to conservation status enum value
        String lowerCond = k.toLowerCase();
        String conservationStatus;
        if (lowerCond.contains("excelente")) {
            conservationStatus = "EXCELLENT";
        } else if (lowerCond.contains("regular")) {
            conservationStatus = "FAIR";
        } else {
            conservationStatus = "GOOD";
        }

        // Default approximate coordinates (Lima center coordinates)
        Double approxLat = -12.1219;
        Double approxLng = -77.0298;

        view.showLoading(true);

        boolean editing = (currentItem != null);
        itemRepository.createItem(categoryId, t, d, conservationStatus, img, approxLat, approxLng, new ItemRepository.Callback<Item>() {
            @Override
            public void onSuccess(Item item) {
                if (view != null) {
                    view.showLoading(false);
                    view.saved(editing);
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showLoading(false);
                    view.showGeneralError(message);
                }
            }
        });
    }

    @Override
    public void detachView() {
        view = null;
    }
}
