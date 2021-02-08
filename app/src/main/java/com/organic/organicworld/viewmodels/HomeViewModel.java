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

    private final LiveData<List<PromotionalModel>> banners;
    private final LiveData<List<HomeScreenTabModel>> homeTab;

    public HomeViewModel() {
        HomeScreenRepository repository = new HomeScreenRepository();
        repository.loadBanners();
        banners = repository.getBanners();
        repository.loadCategories();
        homeTab = repository.getTabs();
    }

    public LiveData<List<PromotionalModel>> loadBanners() {
        return banners;
    }

    public LiveData<List<HomeScreenTabModel>> loadHomeTabs() {
        return homeTab;
    }
}