package com.organic.organicworld.views.fragments.other_fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.android.material.tabs.TabLayoutMediator;
import com.organic.organicworld.R;
import com.organic.organicworld.adapters.HomeScreenAdapter;
import com.organic.organicworld.databinding.FragmentHomeBinding;
import com.organic.organicworld.adapters.view_pager_adapters.PromotionViewPagerAdapter;
import com.organic.organicworld.models.HomeScreenTabModel;
import com.organic.organicworld.viewmodels.HomeViewModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.bind(root);

        PromotionViewPagerAdapter adapter = new PromotionViewPagerAdapter();
        binding.homeScreenPromotionViewPager.setAdapter(adapter);
        homeViewModel.loadBanners().observe(getViewLifecycleOwner(), adapter::updateList);


        HomeScreenAdapter adapter1 = new HomeScreenAdapter();
        binding.tabsList.setAdapter(adapter1);
        binding.tabsList.setHasFixedSize(true);

        homeViewModel.loadHomeTabs().observe(getViewLifecycleOwner(), adapter1::updateList);



        new TabLayoutMediator(binding.tabLayout, binding.homeScreenPromotionViewPager, (tab, pos) -> {
        }).attach();

        updateBanners();

        return root;
    }

    private void updateBanners() {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = () -> {
            int currentItem = binding.homeScreenPromotionViewPager.getCurrentItem();
            binding.homeScreenPromotionViewPager.setCurrentItem((currentItem + 1) % 4, true);
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 5000, 5000);
    }
}