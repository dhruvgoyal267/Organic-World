package com.organic.organicworld.adapters.view_pager_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.organic.organicworld.databinding.CustomPromotionalBannerBinding;
import com.organic.organicworld.models.PromotionalModel;

import java.util.ArrayList;
import java.util.List;

public class PromotionViewPagerAdapter extends RecyclerView.Adapter<PromotionViewPagerAdapter.promotionalViewHolder> {
    List<PromotionalModel> promotionalModels = new ArrayList<>();
    Context context;
    int customPosition = 0;

    @NonNull
    @Override
    public promotionalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new promotionalViewHolder(CustomPromotionalBannerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull promotionalViewHolder holder, int position) {

        Glide.with(context)
                .load(promotionalModels.get(customPosition).getImageUrl())
                .centerCrop()
                .into(holder.bannerBinding.promotionalImage);
        customPosition++;
        if(customPosition == 4)
            customPosition = 0;
    }


    @Override
    public int getItemCount() {
        if(promotionalModels.isEmpty())
            return 0;
        return Integer.MAX_VALUE;
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
