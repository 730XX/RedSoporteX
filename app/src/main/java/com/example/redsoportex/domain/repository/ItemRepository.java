package com.example.redsoportex.domain.repository;

import com.example.redsoportex.domain.model.Category;
import com.example.redsoportex.domain.model.Item;

import java.util.List;

public interface ItemRepository {

    interface Callback<T> {
        void onSuccess(T result);
        void onError(String message);
    }

    void getCategories(Callback<List<Category>> callback);

    void getItems(Long categoryId, Callback<List<Item>> callback);

    void getItemById(Long id, Callback<Item> callback);

    void createItem(Long categoryId, String title, String description, String conservationStatus, String imageUrl, Double approxLatitude, Double approxLongitude, Callback<Item> callback);
}
