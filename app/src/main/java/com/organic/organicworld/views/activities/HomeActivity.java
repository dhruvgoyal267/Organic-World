package com.organic.organicworld.views.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.google.android.material.snackbar.Snackbar;
import com.organic.organicworld.R;
import com.organic.organicworld.adapters.view_pager_adapters.ItemListFragmentAdapter;
import com.organic.organicworld.databinding.ActivityHomeBinding;
import com.organic.organicworld.databinding.NavHeaderMainBinding;
import com.organic.organicworld.listeners.ItemListener;
import com.organic.organicworld.viewmodels.ProductViewModel;
import com.organic.organicworld.views.fragments.HomeFragment;
import com.organic.organicworld.views.fragments.ListItemsFragment;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements DroidListener {

    private ActivityHomeBinding binding;
    private ActionBarDrawerToggle toggle;
    private boolean isListenerRegistered = false;
    private ProductViewModel model;
    private DroidNet droidNet;
    private Snackbar snackbar;
    ItemListener listener;

    @Override
    protected void onResume() {
        super.onResume();
        //assigning listener to navigation
        binding.navView.setNavigationItemSelectedListener(listener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        listener = new ItemListener(binding, this, model);


        droidNet = DroidNet.getInstance();
        droidNet.addInternetConnectivityListener(this);


        snackbar = Snackbar.make(binding.
                getRoot(), "Please! connect to internet", Snackbar.LENGTH_INDEFINITE);

        //clearing stuff
        clearStuff();

        //setting hamburger icon
        setupHamBurgerIcon();

        //binding nav bar
        NavHeaderMainBinding navHeaderMainBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));


        //getting user name
        if (getIntent().getStringExtra("userName") != null)
            navHeaderMainBinding.userName.setText(getIntent().getStringExtra("userName"));


        getSupportFragmentManager().addOnBackStackChangedListener(this::switchHamBurgerAndBack);


        //initializing view model
        model = new ViewModelProvider(this).get(ProductViewModel.class);


        //adding search view listener
        model.addSearchObserver(binding.appBar.searchView);

        binding.appBar.searchBtn.setOnClickListener(v -> {
            search();
        });
    }


    private void clearStuff() {
        getSupportFragmentManager().popBackStackImmediate();
    }

    private void search() {
        binding.appBar.searchBtn.setVisibility(View.GONE);
        binding.appBar.searchView.setVisibility(View.VISIBLE);
        binding.appBar.toolbar.setVisibility(View.GONE);
        binding.appBar.searchView.setIconified(false);
        ListItemsFragment fragment = new ListItemsFragment(ItemListFragmentAdapter.TYPE.Search, model);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment, "Search Items")
                .addToBackStack("Default")
                .commit();
    }

    private void switchHamBurgerAndBack() {
        int stackLength = getSupportFragmentManager().getBackStackEntryCount();
        boolean flag = (stackLength > 0);
        if (flag) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
            if (!isListenerRegistered) {
                toggle.setToolbarNavigationClickListener(v -> getSupportFragmentManager().popBackStackImmediate());
            }
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
            MenuItem item = binding.navView.getCheckedItem();
            if (item != null)
                item.setChecked(false);
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setToolbarNavigationClickListener(null);
        }
        isListenerRegistered = !isListenerRegistered;
    }

    private void setupHamBurgerIcon() {
        setSupportActionBar(binding.appBar.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout
                , binding.appBar.toolbar, R.string.open, R.string.close);

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        HomeFragment fragment = new HomeFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.nav_host_fragment, fragment, "Home")
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        else if (binding.appBar.searchView.getVisibility() == View.VISIBLE) {
            {
                binding.appBar.searchBtn.setVisibility(View.VISIBLE);
                binding.appBar.searchView.setVisibility(View.GONE);
                binding.appBar.toolbar.setVisibility(View.VISIBLE);
                if (model != null) {
                    binding.appBar.searchView.clearFocus();
                    binding.appBar.searchView.setQuery(null, false);
                    model.clearSearches();
                }
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (!isConnected) {
            snackbar.show();
        } else {
            snackbar.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.navView.setNavigationItemSelectedListener(null);
        droidNet.removeInternetConnectivityChangeListener(this);
    }
}