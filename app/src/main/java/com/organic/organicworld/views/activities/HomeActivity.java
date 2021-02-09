package com.organic.organicworld.views.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.organic.organicworld.R;
import com.organic.organicworld.databinding.ActivityHomeBinding;
import com.organic.organicworld.views.fragments.other_fragments.HomeFragment;
import com.organic.organicworld.views.fragments.other_fragments.ListItemsFragment;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout
                , toolbar, R.string.open, R.string.close);

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setCheckedItem(R.id.nav_home);

        binding.navView.setNavigationItemSelectedListener(item -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            if (item.isChecked())
                item.setChecked(false);
            else {
                item.setChecked(true);
                if (item.getItemId() == R.id.showAll) {
                    item.setCheckable(true);
                    toolbar.setTitle(R.string.all_categories);
                    ListItemsFragment fragment = new ListItemsFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, fragment, "Categories")
                            .addToBackStack("Categories")
                            .commit();
                } else if (item.getItemId() == R.id.nav_home) {
                    toolbar.setTitle(R.string.app_name);
                    HomeFragment fragment = new HomeFragment();
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, fragment, "Home")
                            .commit();
                }
            }
            return true;
        });
    }


    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}