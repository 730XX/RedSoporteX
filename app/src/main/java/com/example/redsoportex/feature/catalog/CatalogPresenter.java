package com.example.redsoportex.feature.catalog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.redsoportex.data.repository.ItemRepositoryImpl;
import com.example.redsoportex.domain.model.Category;
import com.example.redsoportex.domain.model.Item;
import com.example.redsoportex.domain.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class CatalogPresenter implements CatalogContract.Presenter {

    private CatalogContract.View view;
    private final ItemRepository itemRepository;

    private final List<Item> allCachedItems = new ArrayList<>();
    private final List<Category> cachedCategories = new ArrayList<>();

    @Nullable
    private String currentQuery = "";
    @Nullable
    private Long currentCategoryId = null;

    public CatalogPresenter(@NonNull CatalogContract.View view) {
        this(view, new ItemRepositoryImpl());
    }

    public CatalogPresenter(@NonNull CatalogContract.View view, @NonNull ItemRepository itemRepository) {
        this.view = view;
        this.itemRepository = itemRepository;
    }

    @Override
    public void loadData() {
        if (view == null) return;
        view.showLoading(true);

        itemRepository.getCategories(new ItemRepository.Callback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> categories) {
                cachedCategories.clear();
                if (categories != null) {
                    cachedCategories.addAll(categories);
                }
                if (view != null) {
                    view.showCategories(cachedCategories);
                }
            }

            @Override
            public void onError(String message) {
                // If categories fail, we still let item loading continue or surface error if needed
            }
        });

        itemRepository.getItems(null, new ItemRepository.Callback<List<Item>>() {
            @Override
            public void onSuccess(List<Item> items) {
                allCachedItems.clear();
                if (items != null) {
                    allCachedItems.addAll(items);
                }
                if (view != null) {
                    view.showLoading(false);
                    applyFilter();
                }
            }

            @Override
            public void onError(String message) {
                if (view != null) {
                    view.showLoading(false);
                    view.showError(message);
                }
            }
        });
    }

    @Override
    public void filter(@Nullable String query, @Nullable Long categoryId) {
        this.currentQuery = query;
        this.currentCategoryId = categoryId;
        applyFilter();
    }

    private void applyFilter() {
        if (view == null) return;

        String q = currentQuery != null ? currentQuery.trim().toLowerCase(Locale.ROOT) : "";
        List<Item> filtered = new ArrayList<>();

        for (Item item : allCachedItems) {
            boolean matchesCategory = (currentCategoryId == null || currentCategoryId <= 0)
                    || (item.getCategoryId() != null && item.getCategoryId().equals(currentCategoryId));
            if (!matchesCategory) continue;

            boolean matchesQuery = q.isEmpty()
                    || (item.getTitle() != null && item.getTitle().toLowerCase(Locale.ROOT).contains(q))
                    || (item.getDescription() != null && item.getDescription().toLowerCase(Locale.ROOT).contains(q))
                    || (item.getCategoryName() != null && item.getCategoryName().toLowerCase(Locale.ROOT).contains(q));

            if (matchesQuery) {
                filtered.add(item);
            }
        }

        view.showItems(filtered);
    }

    @Override
    public void detachView() {
        this.view = null;
    }
}
