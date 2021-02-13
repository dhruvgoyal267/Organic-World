package com.organic.organicworld.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.organic.organicworld.models.Item;
import com.organic.organicworld.repositories.ProductsRepository;

import java.util.List;

public class ProductViewModel extends ViewModel {
    ProductsRepository repository;

    public ProductViewModel() {
        repository = new ProductsRepository();
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
}
