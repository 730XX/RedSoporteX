package com.example.redsoportex.feature.catalog;

import com.example.redsoportex.demo.DemoStore;

public final class ItemDetailPresenter implements ItemDetailContract.Presenter {
    private ItemDetailContract.View view;

    public ItemDetailPresenter(ItemDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void load(String id) {
        if (view == null) return;
        DemoStore.Item i = DemoStore.get().item(id);
        if (i == null) view.close();
        else view.showItem(i);
    }

    @Override
    public void detachView() {
        view = null;
    }
}
