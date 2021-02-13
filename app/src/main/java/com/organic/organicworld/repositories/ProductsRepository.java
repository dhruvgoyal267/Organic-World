package com.organic.organicworld.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.organic.organicworld.models.Item;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductsRepository {
    private final MutableLiveData<List<Item>> categories, products, options, searchItems;
    private final DatabaseReference ref;

    public ProductsRepository() {
        ref = FirebaseDatabase.getInstance().getReference().child("All Products").child("Categories").getRef();
        categories = new MutableLiveData<>();
        products = new MutableLiveData<>();
        options = new MutableLiveData<>();
        searchItems = new MutableLiveData<>();
    }

    public void clearSearches() {
        if (this.searchItems.getValue() != null)
            this.searchItems.getValue().clear();
    }


    public LiveData<List<Item>> getSearchItems() {
        return searchItems;
    }

    public LiveData<List<Item>> getProducts() {
        return products;
    }

    public LiveData<List<Item>> getCategories() {
        return categories;
    }

    public LiveData<List<Item>> getOptions() {
        return options;
    }

    public void loadCategories() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    items.add(new Item(Objects.requireNonNull(snapshot1.child("name").getValue()).toString()
                            , Objects.requireNonNull(snapshot1.child("iconUrl").getValue()).toString()));
                }
                categories.setValue(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void loadProducts(int category) {
        ref.child(Integer.toString(category)).child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    items.add(new Item(Objects.requireNonNull(snapshot1.child("name").getValue()).toString()
                            , Objects.requireNonNull(snapshot1.child("imageUrl").getValue()).toString()));
                }
                products.setValue(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loadOptions(int product, int category) {
        ref.child(Integer.toString(category))
                .child("Products")
                .child(Integer.toString(product))
                .child("Options")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Item> items = new ArrayList<>();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            items.add(new Item(Objects.requireNonNull(snapshot1.child("name").getValue()).toString()
                                    , Objects.requireNonNull(snapshot1.child("imageUrl").getValue()).toString()
                                    , Objects.requireNonNull(snapshot1.child("productUrl").getValue()).toString()));
                        }
                        options.setValue(items);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void loadOptionsWithUrl(String url) {
        ref.child(url).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    items.add(new Item(Objects.requireNonNull(snapshot1.child("name").getValue()).toString()
                            , Objects.requireNonNull(snapshot1.child("imageUrl").getValue()).toString()
                            , Objects.requireNonNull(snapshot1.child("productUrl").getValue()).toString()));
                }
                options.setValue(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void query(String s) {
        List<Item> items = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        for (DataSnapshot snapshot2 : snapshot1.child("Products").getChildren()) {
                            if (!s.isEmpty() && Objects.requireNonNull(snapshot2.child("name").getValue()).toString().toLowerCase().contains(s.toLowerCase())) {
                                for (DataSnapshot snapshot3 : snapshot2.child("Options").getChildren()) {
                                    items.add(new Item(Objects.requireNonNull(snapshot3.child("name").getValue()).toString()
                                            , Objects.requireNonNull(snapshot3.child("imageUrl").getValue()).toString(), Objects.requireNonNull(snapshot3.child("productUrl").getValue()).toString()));
                                }
                            }
                        }

                        searchItems.setValue(items);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Hello", "Error");
            }
        });
    }

}
