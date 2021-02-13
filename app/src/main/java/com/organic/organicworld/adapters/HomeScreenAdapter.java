package com.organic.organicworld.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.organic.organicworld.BuildConfig;
import com.organic.organicworld.R;
import com.organic.organicworld.adapters.view_pager_adapters.ItemListFragmentAdapter;
import com.organic.organicworld.adapters.view_pager_adapters.TabsAdapter;
import com.organic.organicworld.databinding.CustomTabsViewBinding;
import com.organic.organicworld.databinding.ShareLayoutBinding;
import com.organic.organicworld.models.HomeScreenTabModel;
import com.organic.organicworld.views.fragments.other_fragments.ListItemsFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeScreenAdapter extends RecyclerView.Adapter<HomeScreenAdapter.tabViewHolder> {
    CustomTabsViewBinding binding;
    List<HomeScreenTabModel> screens = new ArrayList<>();
    List<Integer> colors = Arrays.asList(R.color.orange, R.color.purple_200, R.color.yellow, R.color.skyBlue);
    Context context;

    @NonNull
    @Override
    public tabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        if (viewType == 1) {
            binding = CustomTabsViewBinding.bind(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_tabs_view, parent, false));
            return new tabViewHolder(binding);
        }
        ShareLayoutBinding binding = ShareLayoutBinding.bind(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.share_layout, parent, false));
        return new tabViewHolder(binding);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 2)
            return 2;
        return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull tabViewHolder holder, int position) {
        if (getItemViewType(position) == 2) {
            holder.shareBinding.shareViewHolder.setOnClickListener(v -> {
                //share app code
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Organic World");
                String shareMessage = "\nNo need to search organic products for long hours on Internet.\nJust download the Organic World now\n";
                shareMessage += "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                context.startActivity(Intent.createChooser(shareIntent, "Choose one"));
            });
        } else {
            holder.customTabsBinding.tabHolder.setBackgroundColor(context.getResources().getColor(colors.get(position % colors.size())));
            HomeScreenTabModel model = screens.get(position);
            TabsAdapter adapter = new TabsAdapter();
            adapter.updateList(model.getModels());
            holder.customTabsBinding.tabsCard.setAdapter(adapter);
            holder.customTabsBinding.homeTileText.setText(model.getTitle());
            holder.customTabsBinding.seeAll.setOnClickListener(v -> {
            });
        }
    }


    public void updateList(List<HomeScreenTabModel> screens) {
        this.screens = screens;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return screens.size();
    }

    static class tabViewHolder extends RecyclerView.ViewHolder {
        CustomTabsViewBinding customTabsBinding;
        ShareLayoutBinding shareBinding;

        public tabViewHolder(ShareLayoutBinding binding) {
            super(binding.getRoot());
            this.shareBinding = binding;
        }

        public tabViewHolder(CustomTabsViewBinding binding) {
            super(binding.getRoot());
            this.customTabsBinding = binding;
        }
    }
}
