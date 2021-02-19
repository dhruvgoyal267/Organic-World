package com.organic.organicworld.adapters.view_pager_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.organic.organicworld.R;
import com.organic.organicworld.databinding.CustomProductViewBinding;
import com.organic.organicworld.models.HomeScreenTileModel;
import com.organic.organicworld.utils.UtilityFunctions;
import com.organic.organicworld.views.fragments.ListItemsFragment;

import java.util.ArrayList;
import java.util.List;

public class TabsAdapter extends RecyclerView.Adapter<TabsAdapter.cardViewHolder> {
    CustomProductViewBinding binding;
    List<HomeScreenTileModel> tiles = new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        this.binding = CustomProductViewBinding.bind(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_product_view, parent, false));
        return new cardViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewHolder holder, int position) {
        HomeScreenTileModel model = tiles.get(position);
        UtilityFunctions.loadImage(context, model.getImageUrl(), holder.binding.productImage);
        holder.binding.productName.setText(model.getName());
        holder.binding.getRoot().setOnClickListener(v -> {
            ListItemsFragment fragment = new ListItemsFragment(model.getCategoryUrl(), model.getName());
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment, model.getName() + " Fragment")
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return tiles.size();
    }


    public void updateList(List<HomeScreenTileModel> tiles) {
        this.tiles = tiles;
        notifyDataSetChanged();
    }

    static class cardViewHolder extends RecyclerView.ViewHolder {
        CustomProductViewBinding binding;

        public cardViewHolder(CustomProductViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
