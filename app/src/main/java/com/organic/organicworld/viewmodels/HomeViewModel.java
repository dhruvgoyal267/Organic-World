package com.organic.organicworld.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.organic.organicworld.models.HomeScreenTabModel;
import com.organic.organicworld.models.HomeScreenTileModel;
import com.organic.organicworld.models.PromotionalModel;
import com.organic.organicworld.repositories.HomeScreenRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    HomeScreenRepository repository;

    public HomeViewModel() {
        repository = new HomeScreenRepository();
    }

    public LiveData<List<PromotionalModel>> loadBanners() {
        repository.loadBanners();
        return repository.getBanners();
    }

    public LiveData<List<HomeScreenTabModel>> loadHomeTabs() {
        repository.loadCategories();
        return repository.getTabs();
    }
}