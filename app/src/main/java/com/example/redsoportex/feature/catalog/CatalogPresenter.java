package com.example.redsoportex.feature.catalog;
import com.example.redsoportex.demo.DemoStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public final class CatalogPresenter implements CatalogContract.Presenter {
    private CatalogContract.View view;
    public CatalogPresenter(CatalogContract.View view) { this.view=view; }
    @Override public void filter(String query,String category) {
        if(view==null)return; List<DemoStore.Item> result=new ArrayList<>();String q=query.trim().toLowerCase(Locale.ROOT);
        for(DemoStore.Item i:DemoStore.get().items()) if((category.isEmpty() || i.category.equals(category)) && (i.title.toLowerCase(Locale.ROOT).contains(q) || i.category.toLowerCase(Locale.ROOT).contains(q))) result.add(i);
        view.showItems(result);
    }
    @Override public void detachView() { view=null; }
}
