package com.organic.organicworld.views.fragments.other_fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.organic.organicworld.R;
import com.organic.organicworld.adapters.view_pager_adapters.ItemListFragmentAdapter;
import com.organic.organicworld.databinding.FragmentListItemsBinding;
import com.organic.organicworld.models.Item;
import com.organic.organicworld.viewmodels.ProductViewModel;

import java.util.List;
import java.util.Objects;

import static com.organic.organicworld.adapters.view_pager_adapters.ItemListFragmentAdapter.TYPE;

public class ListItemsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    TYPE type;
    int categoryId, productId;
    String title, productUrl;
    FragmentListItemsBinding binding;
    ProductViewModel model, viewModel;
    ItemListFragmentAdapter itemAdapter;

    public ListItemsFragment() {
        this.type = TYPE.Category;
    }

    public ListItemsFragment(TYPE type, ProductViewModel model) {
        this.type = type;
        this.model = model;
    }

    public ListItemsFragment(String productUrl, String title) {
        this.productUrl = productUrl;
        this.type = TYPE.HomeTile;
        this.title = title;
    }

    public ListItemsFragment(TYPE type, int categoryId, String title) {
        this.type = type;
        this.categoryId = categoryId;
        this.title = title;
    }

    public ListItemsFragment(TYPE type, int categoryId, int productId, String title) {
        this.type = type;
        this.categoryId = categoryId;
        this.productId = productId;
        this.title = title;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_items, container, false);
        binding = FragmentListItemsBinding.bind(root);
        itemAdapter = new ItemListFragmentAdapter(type);
        binding.listItem.setAdapter(itemAdapter);
        binding.listItem.setHasFixedSize(true);
        binding.itemListView.setColorSchemeColors(Color.GREEN);

        viewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        switch (type) {
            case Category:
                viewModel.getCategories().observe(getViewLifecycleOwner(), this::updateProgressBar);
                break;
            case Product:
                itemAdapter = new ItemListFragmentAdapter(type, categoryId);
                binding.listItem.setAdapter(itemAdapter);
                viewModel.getProducts(categoryId).observe(getViewLifecycleOwner(), this::updateProgressBar);
                break;
            case Option:
                viewModel.getOptions(productId, categoryId).observe(getViewLifecycleOwner(), this::updateProgressBar);
                break;
            case HomeTile:
                viewModel.getOptionsWithUrl(productUrl).observe(getViewLifecycleOwner(), this::updateProgressBar);
                break;
            case Search:
                binding.loadingLayout.getRoot().setVisibility(View.GONE);
                binding.itemListView.setVisibility(View.VISIBLE);
                model.getSearchItems().observe(getViewLifecycleOwner(), itemAdapter::updateList);
        }

        return root;
    }

    private void updateProgressBar(List<Item> items) {
        if (items != null) {
            binding.loadingLayout.getRoot().setVisibility(View.GONE);
            binding.itemListView.setVisibility(View.VISIBLE);
            binding.itemListView.setOnRefreshListener(this);
        } else {
            binding.itemListView.setVisibility(View.GONE);
            binding.loadingLayout.getRoot().setVisibility(View.VISIBLE);
        }
        itemAdapter.updateList(items);
    }

    @Override
    public void onRefresh() {
        binding.itemListView.setRefreshing(false);
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