package com.organic.organicworld.adapters.view_pager_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.organic.organicworld.databinding.CustomPromotionalBannerBinding;
import com.organic.organicworld.models.PromotionalModel;
import com.organic.organicworld.utils.UtilityFunctions;

import java.util.ArrayList;
import java.util.List;

public class PromotionViewPagerAdapter extends RecyclerView.Adapter<PromotionViewPagerAdapter.promotionalViewHolder> {
    List<PromotionalModel> promotionalModels = new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public promotionalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new promotionalViewHolder(CustomPromotionalBannerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull promotionalViewHolder holder, int position) {
        Glide.with(context)
                .load(promotionalModels.get(position).getImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.bannerBinding.promotionalImage);
    }


    @Override
    public int getItemCount() {
        return promotionalModels.size();
    }

    public void updateList(List<PromotionalModel> newList) {
        this.promotionalModels = newList;
        notifyDataSetChanged();
    }

    static class promotionalViewHolder extends RecyclerView.ViewHolder {
        CustomPromotionalBannerBinding bannerBinding;

        promotionalViewHolder(CustomPromotionalBannerBinding binding) {
            super(binding.getRoot());
            this.bannerBinding = binding;
        }
    }
}
