package com.organic.organicworld.views.fragments.other_fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.organic.organicworld.R;
import com.organic.organicworld.adapters.view_pager_adapters.ItemListFragmentAdapter;
import com.organic.organicworld.databinding.FragmentListItemsBinding;
import com.organic.organicworld.viewmodels.ProductViewModel;

import java.lang.reflect.Type;
import java.util.Objects;

import static com.organic.organicworld.adapters.view_pager_adapters.ItemListFragmentAdapter.TYPE;

public class ListItemsFragment extends Fragment {

    TYPE type;
    int id;
    String title;
    FragmentListItemsBinding binding;

    public ListItemsFragment() {
        this.type = TYPE.Category;
    }

    public ListItemsFragment(TYPE type, int id, String title) {
        this.type = type;
        this.id = id;
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_items, container, false);
        binding = FragmentListItemsBinding.bind(root);
        ItemListFragmentAdapter adapter = new ItemListFragmentAdapter(type);
        binding.listItem.setAdapter(adapter);
        binding.listItem.setHasFixedSize(true);
        ProductViewModel viewModel = new ProductViewModel();
        switch (type) {
            case Category:
                viewModel.getCategories().observe(getViewLifecycleOwner(), adapter::updateList);
                break;
            case Product:
                viewModel.getProducts(id).observe(getViewLifecycleOwner(), adapter::updateList);
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (this.type == TYPE.Category) {
            Objects.requireNonNull(((AppCompatActivity) requireContext()).getSupportActionBar()).setTitle("All Categories");
        } else {
            Objects.requireNonNull(((AppCompatActivity) requireContext()).getSupportActionBar()).setTitle(title);
        }
    }
}