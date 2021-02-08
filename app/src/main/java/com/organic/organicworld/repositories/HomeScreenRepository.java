package com.organic.organicworld.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.organic.organicworld.models.HomeScreenTabModel;
import com.organic.organicworld.models.HomeScreenTileModel;
import com.organic.organicworld.models.PromotionalModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeScreenRepository {

    private final MutableLiveData<List<PromotionalModel>> banners;
    private final MutableLiveData<List<HomeScreenTabModel>> homeTab;
    private final DatabaseReference reference;


    public LiveData<List<PromotionalModel>> getBanners() {
        return banners;
    }

    public LiveData<List<HomeScreenTabModel>> getTabs() {
        return homeTab;
    }

    public HomeScreenRepository() {
        banners = new MutableLiveData<>();
        homeTab = new MutableLiveData<>();
        reference = FirebaseDatabase.getInstance().getReference();
    }

    public void loadBanners() {
        FirebaseDatabase.getInstance().getReference()
                .child("Promotional Images")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<PromotionalModel> models = new ArrayList<>();
                        for (int i = 1; i <= 4; i++) {
                            DataSnapshot ref = snapshot.child("Image" + i);
                            models.add(new PromotionalModel(Objects.requireNonNull(ref.child("imageUrl").getValue()).toString()
                                    , Objects.requireNonNull(ref.child("productUrl").getValue()).toString()));
                        }
                        banners.setValue(models);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void loadCategories() {
        reference.child("Home Screen Tabs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getTabs(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getTabs(DataSnapshot snapshot) {
        List<HomeScreenTabModel> tiles = new ArrayList<>();
        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
            List<HomeScreenTileModel> tabs = new ArrayList<>();
            for (DataSnapshot snapshot2 : snapshot1.child("Products").getChildren()) {
                tabs.add(new HomeScreenTileModel(Objects.requireNonNull(snapshot2.child("name").getValue()).toString(),
                        Objects.requireNonNull(snapshot2.child("categoryUrl").getValue()).toString(),
                        Objects.requireNonNull(snapshot2.child("imageUrl").getValue()).toString()));
            }
            tiles.add(new HomeScreenTabModel(tabs
                    , Objects.requireNonNull(snapshot1.child("name").getValue()).toString()));
            if (tiles.size() == 2)
                tiles.add(new HomeScreenTabModel());
        }
        homeTab.setValue(tiles);
    }
}
