package com.organic.organicworld.views.fragments.other_fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.organic.organicworld.R;

public class ListItemsFragment extends Fragment {

    String itemName;
    public ListItemsFragment(){

    }
    public ListItemsFragment(String itemName) {
        this.itemName = itemName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_items, container, false);
    }
}