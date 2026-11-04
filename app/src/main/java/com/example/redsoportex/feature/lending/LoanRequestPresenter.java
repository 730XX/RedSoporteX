package com.example.redsoportex.feature.lending;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.redsoportex.R;
import com.example.redsoportex.data.repository.ItemRepositoryImpl;
import com.example.redsoportex.data.repository.LendingRepositoryImpl;
import com.example.redsoportex.domain.model.Item;
import com.example.redsoportex.domain.model.Lending;
import com.example.redsoportex.domain.repository.ItemRepository;
import com.example.redsoportex.domain.repository.LendingRepository;

public final class LoanRequestPresenter implements LoanRequestContract.Presenter {

    private LoanRequestContract.View view;
    private final ItemRepository itemRepository;
    private final LendingRepository lendingRepository;
    private Long itemId;

    public LoanRequestPresenter(@NonNull LoanRequestContract.View view) {
        this(view, new ItemRepositoryImpl(), new LendingRepositoryImpl());
    }

    public LoanRequestPresenter(@NonNull LoanRequestContract.View view,
                                @NonNull ItemRepository itemRepository,
                                @NonNull LendingRepository lendingRepository) {
        this.view = view;
        this.itemRepository = itemRepository;
        this.lendingRepository = lendingRepository;
    }

    @Override
    public void load(@Nullable Long id) {
        this.itemId = id;
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
                    view.showGeneralError(message);
                    view.close();
                }
            }
        });
    }

    @Override
    public void submit(String daysText, String notes) {
        if (view == null) return;

        int days;
        try {
            days = Integer.parseInt(daysText != null ? daysText.trim() : "");
        } catch (NumberFormatException e) {
            view.showDaysError(R.string.invalid_days);
            return;
        }

        if (days < 1) {
            view.showDaysError(R.string.invalid_days);
            return;
        }

        if (itemId == null) {
            view.showGeneralError("Identificador del implemento no válido");
            return;
        }

        view.showLoading(true);
        String cleanNotes = notes != null ? notes.trim() : "";
        lendingRepository.requestLending(itemId, days, cleanNotes, new LendingRepository.Callback<Lending>() {
            @Override
            public void onSuccess(Lending result) {
                if (view != null) {
                    view.showLoading(false);
                    view.openLoans();
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
