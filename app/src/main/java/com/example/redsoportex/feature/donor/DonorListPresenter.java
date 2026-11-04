package com.example.redsoportex.feature.donor;

import androidx.annotation.NonNull;

import com.example.redsoportex.core.session.SessionManager;
import com.example.redsoportex.data.repository.ItemRepositoryImpl;
import com.example.redsoportex.domain.model.Item;
import com.example.redsoportex.domain.repository.ItemRepository;

import java.util.ArrayList;
import java.util.List;

public final class DonorListPresenter {

    public interface View {
        void showDonorItems(List<Item> items);
        void showLoading(boolean loading);
        void showError(String message);
    }

    private View view;
    private final ItemRepository itemRepository;
    private final SessionManager sessionManager;

    public DonorListPresenter(@NonNull View v, @NonNull SessionManager sessionManager) {
        this(v, new ItemRepositoryImpl(), sessionManager);
    }

    public DonorListPresenter(@NonNull View v, @NonNull ItemRepository itemRepository, @NonNull SessionManager sessionManager) {
        this.view = v;
        this.itemRepository = itemRepository;
        this.sessionManager = sessionManager;
    }

    public void load() {
        if (view == null) return;
        view.showLoading(true);

        itemRepository.getItems(null, new ItemRepository.Callback<List<Item>>() {
            @Override
            public void onSuccess(List<Item> items) {
                if (view == null) return;
                view.showLoading(false);

                long currentUserId = sessionManager.getUserId();
                String currentFullName = sessionManager.getFullName().trim();

                List<Item> donorItems = new ArrayList<>();
                if (items != null) {
                    for (Item item : items) {
                        boolean matchId = currentUserId != -1 && item.getDonorId() != null && item.getDonorId().longValue() == currentUserId;
                        boolean matchName = !currentFullName.isEmpty() && item.getDonorName() != null && item.getDonorName().trim().equalsIgnoreCase(currentFullName);
                        if (matchId || matchName) {
                            donorItems.add(item);
                        }
                    }
                }
                view.showDonorItems(donorItems);
            }

            @Override
            public void onError(String message) {
                if (view == null) return;
                view.showLoading(false);
                view.showError(message);
            }
        });
    }

    public void detach() {
        view = null;
    }
}
