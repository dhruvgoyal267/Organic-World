package com.organic.organicworld.viewmodels;

import androidx.lifecycle.LiveData;

import com.organic.organicworld.models.Item;
import com.organic.organicworld.repositories.ProductsRepository;

import java.util.List;

public class ProductViewModel {
    ProductsRepository repository;

    public ProductViewModel() {
        repository = new ProductsRepository();
    }

    public LiveData<List<Item>> getCategories() {
        repository.loadCategories();
        return repository.getCategories();
    }

    public LiveData<List<Item>> getProducts(int category) {
        repository.loadProducts(category);
        return repository.getProducts();
    }
}
