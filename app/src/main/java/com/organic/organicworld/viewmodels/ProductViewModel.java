package com.organic.organicworld.viewmodels;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.organic.organicworld.models.Item;
import com.organic.organicworld.repositories.ProductsRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProductViewModel extends ViewModel {
    ProductsRepository repository;
    CompositeDisposable disposable;

    public ProductViewModel() {
        repository = new ProductsRepository();
        disposable = new CompositeDisposable();
    }

    public LiveData<Boolean> suggest(String name) {
        repository.suggestProduct(name);
        return repository.isSuggested();
    }

    public LiveData<Boolean> feedback(String message, String email) {
        repository.submitFeedBack(message, email);
        return repository.isFeedbackSubmitted();
    }

    public LiveData<List<Item>> getCategories() {
        repository.loadCategories();
        return repository.getCategories();
    }

    public void clearSearches() {
        this.repository.clearSearches();
    }


    public LiveData<List<Item>> getProducts(int category) {
        repository.loadProducts(category);
        return repository.getProducts();
    }

    public LiveData<List<Item>> getAllProducts() {
        repository.loadAllProducts();
        return repository.getProducts();
    }


    public LiveData<List<Item>> getOptions(int productNo, int category) {
        repository.loadOptions(productNo, category);
        return repository.getOptions();
    }

    public LiveData<List<Item>> getOptionsWithUrl(String url) {
        repository.loadOptionsWithUrl(url);
        return repository.getOptions();
    }

    public void loadSearchItems(String s) {
        repository.query(s);
    }

    public LiveData<List<Item>> getSearchItems() {
        return repository.getSearchItems();
    }

    public void addSearchObserver(SearchView searchView) {
        Observable<String> observable = Observable.create(
                (ObservableOnSubscribe<String>) emitter ->
                        searchView.setOnQueryTextListener(
                                new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        if (!emitter.isDisposed())
                                            emitter.onNext(newText);
                                        return false;
                                    }
                                }))
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(s -> !s.isEmpty())
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io());


        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
                loadSearchItems(s);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
