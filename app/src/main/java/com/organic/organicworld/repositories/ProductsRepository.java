package com.organic.organicworld.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.organic.organicworld.models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductsRepository {
    private final MutableLiveData<List<Item>> categories, products;
    private final DatabaseReference ref;

    public ProductsRepository() {
        ref = FirebaseDatabase.getInstance().getReference().child("All Products").child("Categories").getRef();
        categories = new MutableLiveData<>();
        products = new MutableLiveData<>();
    }

    public MutableLiveData<List<Item>> getProducts() {
        return products;
    }

    public LiveData<List<Item>> getCategories() {
        return categories;
    }

    public void loadCategories() {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
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
        ref.child(Integer.toString(category)).child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
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

}
