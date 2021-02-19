package com.organic.organicworld.utils;

import android.content.Context;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class UserPref {
    private final Preferences.Key<String> key;
    private final DataStore<Preferences> dataStore;


    public UserPref(Context context) {
        dataStore = new RxPreferenceDataStoreBuilder(context, "userName").build();
        key = new Preferences.Key<>("userName");
    }

    public boolean saveName(String name) {
        if (name == null || name.isEmpty())
            return false;
        RxDataStore.updateDataAsync(dataStore, preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(key, name);
            return Single.just(mutablePreferences);
        });
        return true;
    }

    public Flowable<String> getUserName() {
        return RxDataStore.data(dataStore).map(preferences -> preferences.get(key));
    }
}
