package com.organic.organicworld.fragments.other_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.organic.organicworld.R;
import com.organic.organicworld.viewmodels.HomeViewModel;
import com.organic.organicworld.databinding.FragmentHomeBinding;
import com.organic.organicworld.fragments.promotional_fragments.PromotionalFragmentFour;
import com.organic.organicworld.fragments.promotional_fragments.PromotionalFragmentOne;
import com.organic.organicworld.fragments.promotional_fragments.PromotionalFragmentThree;
import com.organic.organicworld.fragments.promotional_fragments.PromotionalFragmentTwo;
import com.organic.organicworld.view_pager_adapters.PromotionViewPagerAdapter;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        com.organic.organicworld.databinding.FragmentHomeBinding binding = FragmentHomeBinding.bind(root);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new PromotionalFragmentOne());
        fragments.add(new PromotionalFragmentTwo());
        fragments.add(new PromotionalFragmentThree());
        fragments.add(new PromotionalFragmentFour());

        PromotionViewPagerAdapter adapter = new PromotionViewPagerAdapter(fragments, getParentFragmentManager(), getLifecycle());
        binding.homeScreenPromotionViewPager.setAdapter(adapter);
        binding.dotsIndicator.setViewPager(binding.homeScreenPromotionViewPager);
        return root;
    }
}