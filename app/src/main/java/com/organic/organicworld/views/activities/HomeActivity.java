package com.organic.organicworld.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.organic.organicworld.BuildConfig;
import com.organic.organicworld.R;
import com.organic.organicworld.adapters.view_pager_adapters.ItemListFragmentAdapter;
import com.organic.organicworld.databinding.ActivityHomeBinding;
import com.organic.organicworld.databinding.NavHeaderMainBinding;
import com.organic.organicworld.viewmodels.ProductViewModel;
import com.organic.organicworld.views.fragments.other_fragments.ListItemsFragment;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity implements DroidListener, NavigationView.OnNavigationItemSelectedListener {

    private CompositeDisposable disposable;
    private ActivityHomeBinding binding;
    private ActionBarDrawerToggle toggle;
    private boolean isListenerRegistered = false;
    private ProductViewModel model;
    private DroidNet droidNet;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        droidNet = DroidNet.getInstance();
        droidNet.addInternetConnectivityListener(this);

        disposable = new CompositeDisposable();

        snackbar = Snackbar.make(binding.
                getRoot(), "Please! connect to internet", Snackbar.LENGTH_INDEFINITE);

        //setting hamburger icon
        setupHamBurgerIcon();

        //binding nav bar
        NavHeaderMainBinding navHeaderMainBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));


        //getting user name
        if (getIntent().getStringExtra("userName") != null)
            navHeaderMainBinding.userName.setText(getIntent().getStringExtra("userName"));


        //assigning listener to navigation
        binding.navView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().addOnBackStackChangedListener(this::switchHamBurgerAndBack);


        //initializing view model
        model = new ViewModelProvider(this).get(ProductViewModel.class);


        //adding search view listener
        Observable<String> observable = Observable.create(
                (ObservableOnSubscribe<String>) emitter ->
                        binding.appBar.searchView.setOnQueryTextListener(
                                new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String query) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String newText) {
                                        if (!emitter.isDisposed())
                                            emitter.onNext(newText);
                                        return false;
                                    }
                                }))
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(s -> !s.isEmpty())
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io());


        observable.subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                disposable.add(d);
            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull String s) {
                model.loadSearchItems(s);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        binding.appBar.searchBtn.setOnClickListener(v -> {
            search();
        });
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
        boolean flag = (getSupportFragmentManager().getBackStackEntryCount() > 0);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        if (!item.isChecked()) {
            item.setChecked(true);
            if (item.getItemId() == R.id.showAll) {
                item.setCheckable(true);
                binding.appBar.toolbar.setTitle(R.string.all_categories);
                ListItemsFragment fragment = new ListItemsFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment, "Categories")
                        .addToBackStack("Home")
                        .commit();
            } else if (item.getItemId() == R.id.share) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Organic World");
                String shareMessage = "\nNo need to search organic products for long hours on Internet.\nJust download the Organic World now\n";
                shareMessage += "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "Choose one"));
            } else if (item.getItemId() == R.id.join) {
                //Implement all social links
            } else if (item.getItemId() == R.id.rateApp) {
                /*Intent intent = null;
                try {
                    intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market:?//details?id=${context?.packageName}"));
                } catch (Exception e) {
                    intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=${context?.packageName}"));
                } finally {
                    if (intent != null)
                        startActivity(intent);
                }*/

                //ReviewManager manager = new ReviewManagerFactory.create(context);

            } else if (item.getItemId() == R.id.suggestProduct) {

            }
        }
        return true;
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (!isConnected)
            snackbar.show();
        else
            snackbar.dismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        droidNet.removeInternetConnectivityChangeListener(this);
        disposable.clear();
    }
}