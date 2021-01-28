package com.organic.organicworld.view_pager_adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class PromotionViewPagerAdapter extends FragmentStateAdapter {
    List<Fragment> fragments;

    public PromotionViewPagerAdapter(List<Fragment> fragments, FragmentManager fm, Lifecycle lifecycle) {
        super(fm, lifecycle);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
