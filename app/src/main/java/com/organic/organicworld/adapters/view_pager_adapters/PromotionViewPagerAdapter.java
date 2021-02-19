package com.organic.organicworld.adapters.view_pager_adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.organic.organicworld.R;
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
        CircularProgressDrawable placeHolder = new CircularProgressDrawable(context);
        placeHolder.setStrokeWidth(5f);
        placeHolder.setColorSchemeColors(Color.GREEN);
        placeHolder.setCenterRadius(30f);
        placeHolder.start();
        Glide.with(context)
                .load(promotionalModels.get(customPosition).getImageUrl())
                .thumbnail(0.2f)
                .placeholder(placeHolder)
                .error(R.drawable.error_icon)
                .into(holder.bannerBinding.promotionalImage);
        customPosition++;
        if (customPosition == 4) {
            customPosition = 0;
        }
    }


    @Override
    public int getItemCount() {
        if (promotionalModels.isEmpty())
            return 0;
        return Integer.MAX_VALUE;
    }

    public void updateList(List<PromotionalModel> newList) {
        if (this.promotionalModels.size() != newList.size()) {
            this.promotionalModels = newList;
            notifyDataSetChanged();
        }
    }

    static class promotionalViewHolder extends RecyclerView.ViewHolder {
        CustomPromotionalBannerBinding bannerBinding;

        promotionalViewHolder(CustomPromotionalBannerBinding binding) {
            super(binding.getRoot());
            this.bannerBinding = binding;
        }
    }
}
