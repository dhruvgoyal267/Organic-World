package com.organic.organicworld.adapters.view_pager_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.organic.organicworld.R;
import com.organic.organicworld.databinding.CustomProductViewBinding;
import com.organic.organicworld.models.Item;
import com.organic.organicworld.utils.UtilityFunctions;
import com.organic.organicworld.views.fragments.other_fragments.ListItemsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemListFragmentAdapter extends RecyclerView.Adapter<ItemListFragmentAdapter.ProductViewHolder> {

    List<Item> items = new ArrayList<>();
    Context context;

    public enum TYPE {Category, Product, Option}

    TYPE type;

    public ItemListFragmentAdapter(TYPE type) {
        this.type = type;
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
                ListItemsFragment fragment = new ListItemsFragment(TYPE.Product, position + 1,item.getName());
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
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        CustomProductViewBinding binding;

        public ProductViewHolder(CustomProductViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
