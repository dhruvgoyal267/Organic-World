package com.organic.organicworld.views.fragments.other_fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.tabs.TabLayoutMediator;
import com.organic.organicworld.R;
import com.organic.organicworld.adapters.HomeScreenAdapter;
import com.organic.organicworld.adapters.view_pager_adapters.PromotionViewPagerAdapter;
import com.organic.organicworld.databinding.AppBarMainBinding;
import com.organic.organicworld.databinding.FragmentHomeBinding;
import com.organic.organicworld.databinding.NavHeaderMainBinding;
import com.organic.organicworld.models.HomeScreenTabModel;
import com.organic.organicworld.utils.UserPref;
import com.organic.organicworld.utils.UtilityFunctions;
import com.organic.organicworld.viewmodels.HomeViewModel;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentHomeBinding binding;

    private HomeScreenAdapter adapter1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.bind(root);

        PromotionViewPagerAdapter adapter = new PromotionViewPagerAdapter();
        binding.homeScreenPromotionViewPager.setAdapter(adapter);
        binding.homeView.setColorSchemeColors(Color.GREEN);
        binding.homeView.setOnRefreshListener(this);

        homeViewModel.loadBanners().observe(getViewLifecycleOwner(), adapter::updateList);


        adapter1 = new HomeScreenAdapter();
        binding.tabsList.setAdapter(adapter1);
        binding.tabsList.setHasFixedSize(true);

        homeViewModel.loadHomeTabs().observe(getViewLifecycleOwner(), this::updateProgressbar);

        binding.dots.attachToPager(binding.homeScreenPromotionViewPager);

        updateBanners();
        return root;
    }

    private void updateProgressbar(List<HomeScreenTabModel> homeScreenTabModels) {
        if (homeScreenTabModels != null) {
            binding.loadingLayout.getRoot().setVisibility(View.GONE);
            binding.homeView.setVisibility(View.VISIBLE);
        } else {
            binding.loadingLayout.getRoot().setVisibility(View.VISIBLE);
            binding.homeView.setVisibility(View.GONE);
        }
        adapter1.updateList(homeScreenTabModels);
    }

    private void updateBanners() {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = () -> {
            int currentItem = binding.homeScreenPromotionViewPager.getCurrentItem();
            binding.homeScreenPromotionViewPager.setCurrentItem(currentItem + 1, true);
        };
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 5000, 5000);
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) requireContext()).getSupportActionBar()).setTitle(R.string.app_name);
    }

    @Override
    public void onRefresh() {
        binding.homeView.setRefreshing(false);
    }
}