package com.organic.organicworld.adapters.view_pager_adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.organic.organicworld.R;
import com.organic.organicworld.databinding.CustomProductViewBinding;
import com.organic.organicworld.models.Item;
import com.organic.organicworld.utils.UtilityFunctions;
import com.organic.organicworld.views.fragments.ListItemsFragment;

import java.util.ArrayList;
import java.util.List;

public class ItemListFragmentAdapter extends RecyclerView.Adapter<ItemListFragmentAdapter.ProductViewHolder> {

    List<Item> items = new ArrayList<>();
    Context context;

    public enum TYPE {Category, Product, Option, HomeTile, Search, All}

    TYPE type;
    int categoryId;

    public ItemListFragmentAdapter(TYPE type) {
        this.type = type;
    }

    public ItemListFragmentAdapter(TYPE type, int id) {
        this.type = type;
        this.categoryId = id;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new ProductViewHolder(CustomProductViewBinding.bind(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_product_view, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Item item = items.get(position);
        UtilityFunctions.loadImage(context, item.getIconUrl(), holder.binding.productImage);
        holder.binding.productName.setText(item.getName());

        holder.binding.getRoot().setOnClickListener(v -> {
            if (this.type == TYPE.Category) {
                ListItemsFragment fragment = new ListItemsFragment(TYPE.Product, position + 1, item.getName());
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment, "Product Fragment")
                        .addToBackStack(null)
                        .commit();
            } else if (this.type == TYPE.Product) {
                ListItemsFragment fragment = new ListItemsFragment(TYPE.Option, categoryId, position + 1, item.getName());
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment, "Options Fragment")
                        .addToBackStack(null)
                        .commit();
            } else if (type == TYPE.Option || type == TYPE.Search) {
                String url = item.getProductUrl();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            } else if (type == TYPE.All) {
                ListItemsFragment fragment = new ListItemsFragment(item.getProductUrl(), item.getName());
                AppCompatActivity activity = (AppCompatActivity) context;
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment, "Product Fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void updateList(List<Item> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }


    static class ProductViewHolder extends RecyclerView.ViewHolder {
        CustomProductViewBinding binding;

        public ProductViewHolder(CustomProductViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
