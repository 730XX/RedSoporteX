package com.example.redsoportex.feature.donor;

import com.example.redsoportex.R;
import com.example.redsoportex.demo.DemoStore;

public final class ItemFormPresenter implements ItemFormContract.Presenter {
    private ItemFormContract.View view;
    private DemoStore.Item item;

    public ItemFormPresenter(ItemFormContract.View view) {
        this.view = view;
    }

    @Override
    public void load(String id) {
        if (id == null) return;
        item = DemoStore.get().item(id);
        if (view == null) return;
        if (item == null || !"Tú".equals(item.donor)) view.close();
        else view.showItem(item);
    }

    @Override
    public void save(String category, String title, String description, String condition, String image, String location) {
        if (view == null) return;
        String c = category.trim(), t = title.trim(), d = description.trim(), k = condition.trim(), l = location.trim();
        int ce = c.isEmpty() ? R.string.required_selection : 0, te = t.isEmpty() ? R.string.error_required : t.length() > 120 ? R.string.donor_title_too_long : 0, de = d.isEmpty() ? R.string.error_required : 0, ke = k.isEmpty() ? R.string.required_selection : 0, le = l.isEmpty() ? R.string.error_required : 0;
        view.showError(ItemFormContract.Field.CATEGORY, ce);
        view.showError(ItemFormContract.Field.TITLE, te);
        view.showError(ItemFormContract.Field.DESCRIPTION, de);
        view.showError(ItemFormContract.Field.CONDITION, ke);
        view.showError(ItemFormContract.Field.LOCATION, le);
        if (ce + te + de + ke + le != 0) return;
        boolean editing = item != null;
        if (editing) {
            item.title = t;
            item.description = d;
            item.condition = k;
            item.imageUrl = image.trim();
            item.location = l;
        } else {
            item = DemoStore.get().addDemoItem(c, t, d, k, image.trim(), l);
        }
        view.saved(editing);
    }

    @Override
    public void detachView() {
        view = null;
    }
}
