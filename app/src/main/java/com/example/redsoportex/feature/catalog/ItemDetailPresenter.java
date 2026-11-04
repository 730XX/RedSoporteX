package com.example.redsoportex.feature.catalog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.redsoportex.data.repository.ItemRepositoryImpl;
import com.example.redsoportex.domain.model.Item;
import com.example.redsoportex.domain.repository.ItemRepository;

public final class ItemDetailPresenter implements ItemDetailContract.Presenter {

    private ItemDetailContract.View view;
    private final ItemRepository itemRepository;

    public ItemDetailPresenter(@NonNull ItemDetailContract.View view) {
        this(view, new ItemRepositoryImpl());
    }

    public ItemDetailPresenter(@NonNull ItemDetailContract.View view, @NonNull ItemRepository itemRepository) {
        this.view = view;
        this.itemRepository = itemRepository;
    }

    @Override
    public void load(@Nullable Long id) {
        if (view == null) return;
        if (id == null) {
            view.close();
            return;
        }

        view.showLoading(true);
        itemRepository.getItemById(id, new ItemRepository.Callback<Item>() {
            @Override
            public void onSuccess(Item item) {
                if (view != null) {
                    view.showLoading(false);
                    if (item == null) {
                        view.close();
                    } else {
                        view.showItem(item);
                    }
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
    public void detachView() {
        this.view = null;
    }
}
