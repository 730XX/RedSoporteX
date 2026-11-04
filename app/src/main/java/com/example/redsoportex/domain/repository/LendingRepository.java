package com.example.redsoportex.domain.repository;

import com.example.redsoportex.domain.model.Lending;

import java.util.List;

public interface LendingRepository {

    interface Callback<T> {
        void onSuccess(T result);
        void onError(String message);
    }

    void requestLending(Long itemId, int expectedReturnDays, String handoverNotes, Callback<Lending> callback);

    void getMyLendings(Callback<List<Lending>> callback);

    void checkIn(Long lendingId, Callback<Lending> callback);

    void checkOut(Long lendingId, Callback<Lending> callback);

    void getLendingById(Long lendingId, Callback<Lending> callback);
}
