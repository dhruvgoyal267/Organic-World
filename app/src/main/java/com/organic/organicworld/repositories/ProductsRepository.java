package com.organic.organicworld.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
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
    private final MutableLiveData<List<Item>> categories, products, options, searchItems;
    private final DatabaseReference ref;
    private final MutableLiveData<Boolean> isSuggested, isFeedbackSubmitted;

    public ProductsRepository() {
        ref = FirebaseDatabase.getInstance().getReference();
        categories = new MutableLiveData<>();
        products = new MutableLiveData<>();
        options = new MutableLiveData<>();
        searchItems = new MutableLiveData<>();
        isSuggested = new MutableLiveData<>();
        isFeedbackSubmitted = new MutableLiveData<>();
    }


    public LiveData<Boolean> isFeedbackSubmitted() {
        return isFeedbackSubmitted;
    }

    public LiveData<Boolean> isSuggested() {
        return isSuggested;
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

    public void loadAllProducts() {
        List<Item> products1 = new ArrayList<>();
        ref.child("All Products").child("Categories")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int i = 1;
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            int j = 1;
                            for (DataSnapshot snapshot2 : snapshot1.child("Products").getChildren()) {
                                products1.add(new Item(Objects.requireNonNull(snapshot2.child("name").getValue()).toString()
                                        , Objects.requireNonNull(snapshot2.child("imageUrl").getValue()).toString()
                                        , (i + "/Products/" + j + "/Options")));
                                j++;
                            }
                            i++;
                        }
                        products.setValue(products1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void submitFeedBack(String message, String email) {
        if (message == null || message.isEmpty() || email == null || email.isEmpty()) {
            isFeedbackSubmitted.setValue(false);
            return;
        }
        isFeedbackSubmitted.setValue(null);
        DatabaseReference reference = ref.child("User Feedback").push().getRef();
        reference.child("Feedback").setValue(message)
                .addOnSuccessListener(aVoid -> {
                    reference.child("Email").setValue(email).addOnSuccessListener(aVoid1 -> {
                        isFeedbackSubmitted.setValue(true);
                    });
                });
    }

    public void clearSearches() {
        if (this.searchItems.getValue() != null)
            this.searchItems.getValue().clear();
    }

    public void suggestProduct(String name) {
        if (name == null || name.isEmpty()) {
            isSuggested.setValue(false);
            return;
        }
        isSuggested.setValue(null);
        ref.child("Suggested Products")
                .push().setValue(name).addOnSuccessListener(aVoid -> {
            isSuggested.setValue(true);
        });
    }

    public void loadCategories() {
        ref.child("All Products").child("Categories").addValueEventListener(new ValueEventListener() {
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
        ref.child("All Products").child("Categories").child(Integer.toString(category)).child("Products").addValueEventListener(new ValueEventListener() {
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
        ref.child("All Products").child("Categories").child(Integer.toString(category))
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
        ref.child("All Products").child("Categories").child(url).addValueEventListener(new ValueEventListener() {
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
        ref.child("All Products").child("Categories").addValueEventListener(new ValueEventListener() {
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
